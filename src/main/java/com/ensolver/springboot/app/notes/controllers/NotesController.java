package com.ensolver.springboot.app.notes.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.Usuario;
import com.ensolver.springboot.app.notes.repositories.NoteRepository;
import com.ensolver.springboot.app.notes.repositories.UserRepository;
import com.ensolver.springboot.app.notes.security.JwtTokenProvider;
import com.ensolver.springboot.app.notes.service.NoteService;

@CrossOrigin(origins = {"https://misnotasweb-98015.web.app" })
@RestController
@RequestMapping("/api/notes")
public class NotesController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(Principal principal) {
        String email = principal.getName(); // el username del JWT
        return ResponseEntity.ok(noteService.getNotesByUser(email));
    }

    @GetMapping("/status/{archived}")
    public List<Note> getNotesByArchived(@PathVariable boolean archived, Principal principal) {
        String email = principal.getName(); // Obtenemos el email del usuario autenticado
        Usuario usuario = userRepository.findByEmail(email); // Ya no usamos Optional

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        return noteRepository.findByUserAndArchived(usuario, archived);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note, @RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        String token = authHeader.substring(7); // Eliminamos 'Bearer ' del encabezado
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        String email = jwtTokenProvider.getEmailFromToken(token); // Obtiene el email desde el JWT

        // Buscar al usuario por su email
        Usuario user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Asociar el usuario a la nota
        note.setUser(user);

        // Guardar la nota
        Note savedNote = noteRepository.save(note);

        // Devolver la respuesta con la nota creada
        return new ResponseEntity<>(savedNote, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note, Principal principal) {
        String email = principal.getName();
        Usuario user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Note> existingNote = noteService.getNoteById(id);

        if (existingNote.isPresent() && existingNote.get().getUser().getId().equals(user.getId())) {
            Note noteToUpdate = existingNote.get();
            noteToUpdate.setTitle(note.getTitle());
            noteToUpdate.setContent(note.getContent());
            noteToUpdate.setCategory(note.getCategory());
            Note updatedNote = noteService.updateNote(noteToUpdate);
            return ResponseEntity.ok(updatedNote);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Usuario user = userRepository.findByEmail(email);

        Optional<Note> note = noteService.getNoteById(id);
        if (note.isPresent() && note.get().getUser().getId().equals(user.getId())) {
            noteService.deleteNote(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories(Principal principal) {
        String email = principal.getName();
        Usuario usuario = userRepository.findByEmail(email);
    
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }
    
        List<String> categorias = noteRepository.findDistinctCategoriesByUser(usuario);
        return ResponseEntity.ok(categorias);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Note> toggleArchiveStatus(@PathVariable Long id, Principal principal) {
        String email = principal.getName(); // Email desde JWT
        Usuario user = userRepository.findByEmail(email);

        Optional<Note> optionalNote = noteService.getNoteById(id);

        if (optionalNote.isPresent() && optionalNote.get().getUser().getId().equals(user.getId())) {
            Note note = optionalNote.get();
            note.setArchived(!note.isArchived()); // Toggle del estado archivado
            Note updatedNote = noteService.updateNote(note);
            return ResponseEntity.ok(updatedNote);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
