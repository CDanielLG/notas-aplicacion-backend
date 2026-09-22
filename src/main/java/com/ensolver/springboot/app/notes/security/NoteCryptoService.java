package com.ensolver.springboot.app.notes.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class NoteCryptoService {

    private static final String PREFIX = "enc:v1:";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();
    private final SecretKeySpec secretKey;

    public NoteCryptoService(@Value("${app.encryption.notes-key}") String encodedKey) {
        byte[] key = Base64.getDecoder().decode(encodedKey);
        if (key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalArgumentException("app.encryption.notes-key must be a Base64 encoded AES key of 16, 24, or 32 bytes");
        }
        this.secretKey = new SecretKeySpec(key, "AES");
    }

    public String encrypt(String plainText) {
        if (plainText == null || plainText.startsWith(PREFIX)) {
            return plainText;
        }

        try {
            byte[] iv = new byte[IV_LENGTH_BYTES];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer payload = ByteBuffer.allocate(iv.length + cipherText.length);
            payload.put(iv);
            payload.put(cipherText);

            return PREFIX + Base64.getEncoder().encodeToString(payload.array());
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Could not encrypt note field", exception);
        }
    }

    public String decrypt(String value) {
        if (value == null || !value.startsWith(PREFIX)) {
            return value;
        }

        try {
            byte[] payload = Base64.getDecoder().decode(value.substring(PREFIX.length()));
            ByteBuffer buffer = ByteBuffer.wrap(payload);

            byte[] iv = new byte[IV_LENGTH_BYTES];
            buffer.get(iv);

            byte[] cipherText = new byte[buffer.remaining()];
            buffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Could not decrypt note field", exception);
        }
    }
}
