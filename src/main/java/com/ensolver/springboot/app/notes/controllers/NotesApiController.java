package com.ensolver.springboot.app.notes.controllers;

import com.ensolver.springboot.app.notes.DTO.NotesDTO;
import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.service.NoteService;
import com.ensolver.springboot.app.notes.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
public class NotesApiController {

    private final NoteService noteService;
    private final UserService userService;

    public NotesApiController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByEmail(email);
    }

    @GetMapping
    public ResponseEntity<List<NotesDTO>> getAllNotes() {
        User user = getCurrentUser();
        List<Note> notes = noteService.findByUser(user);
        return ResponseEntity.ok(noteService.toDtoList(notes));
    }

    @GetMapping("/status/{archived}")
    public ResponseEntity<List<NotesDTO>> getNotesByStatus(@PathVariable boolean archived) {
        User user = getCurrentUser();
        List<Note> notes = noteService.findByUserAndArchived(user, archived);
        return ResponseEntity.ok(noteService.toDtoList(notes));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        User user = getCurrentUser();
        List<String> categories = noteService.findCategoriesByUser(user);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotesDTO> getNoteById(@PathVariable Long id) {
        Note note = noteService.findById(id);
        User user = getCurrentUser();
        
        if (!note.getUser().getUser_id().equals(user.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(noteService.toDto(note));
    }

    @PostMapping
    public ResponseEntity<NotesDTO> createNote(@Valid @RequestBody NotesDTO note) {
        User user = getCurrentUser();
        Note savedNote = noteService.create(user, note);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteService.toDto(savedNote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotesDTO> updateNote(@PathVariable Long id, @Valid @RequestBody NotesDTO note) {
        Note existingNote = noteService.findById(id);
        User user = getCurrentUser();
        
        if (!existingNote.getUser().getUser_id().equals(user.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Note updatedNote = noteService.update(id, note);
        return ResponseEntity.ok(noteService.toDto(updatedNote));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<NotesDTO> toggleArchive(@PathVariable Long id) {
        Note note = noteService.findById(id);
        User user = getCurrentUser();
        
        if (!note.getUser().getUser_id().equals(user.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        Note updatedNote = noteService.toggleArchive(id);
        return ResponseEntity.ok(noteService.toDto(updatedNote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        Note note = noteService.findById(id);
        User user = getCurrentUser();
        
        if (!note.getUser().getUser_id().equals(user.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        noteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

