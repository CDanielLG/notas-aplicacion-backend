package com.ensolver.springboot.app.notes.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.Usuario;
import com.ensolver.springboot.app.notes.repositories.NoteRepository;
import com.ensolver.springboot.app.notes.repositories.UserRepository;



@Service
public class NoteService {
	

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true) // 🔵 Solo lectura
    public List<Note> getNotesByUser(String email) {
        Usuario user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return noteRepository.findByUser(user);
    }

    @Transactional // 🟠 Modifica datos
    public Note createNoteForUser(Note note, String email) {
        Usuario user = userRepository.findByEmail(email);
        note.setUser(user);
        return noteRepository.save(note);
    }

    @Transactional(readOnly = true) // 🔵 Solo lectura
    public List<Note> getNotesByArchived(boolean archived) {
        return noteRepository.findByArchived(archived);
    }

    @Transactional(readOnly = true) // 🔵 Solo lectura
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    @Transactional // 🟠 Modifica datos
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Transactional // 🟠 Modifica datos
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    @Transactional // 🟠 Modifica datos
    public void toggleArchive(Long id) {
        Note note = noteRepository.findById(id).orElseThrow();
        note.setArchived(!note.isArchived());
        noteRepository.save(note);
    }
 
    @Transactional // 🟠 Modifica datos
    public Note updateNote(Note note) {
        return noteRepository.save(note); // Devuelve la nota actualizada
    }
    
    @Transactional(readOnly = true) // 🔵 Solo lectura
    public List<String> getAllCategories() {
        List<Note> notes = noteRepository.findAll();  // Obtener todas las notas
        Set<String> categories = new HashSet<>();
        for (Note note : notes) {
            if (note.getCategory() != null) {
                categories.add(note.getCategory());
            }
        }
        return new ArrayList<>(categories);
    }
	
}
