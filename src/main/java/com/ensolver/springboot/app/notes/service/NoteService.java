package com.ensolver.springboot.app.notes.service;

import com.ensolver.springboot.app.notes.DTO.NotesDTO;
import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repo.INoteRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final INoteRepo noteRepo;

    public NoteService(INoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    public List<Note> findAll() {
        return noteRepo.findAll();
    }

    public Note findById(Long id) {
        return noteRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
    }

    public List<Note> findByUserAndArchived(User user, boolean archived) {
        return noteRepo.findByUserAndArchivedOrderByCreatedAtDesc(user, archived);
    }

    public List<Note> findByUser(User user) {
        return noteRepo.findByUserOrderByCreatedAtDesc(user);
    }

    public List<String> findCategoriesByUser(User user) {
        return noteRepo.findCategoriesByUser(user);
    }

    public Note save(Note note) {
        return noteRepo.save(note);
    }

    public Note create(User user, NotesDTO notesDTO) {
        Note note = new Note();
        note.setTitle(notesDTO.getTitle());
        note.setContent(notesDTO.getContent());
        note.setCategory(notesDTO.getCategory());
        note.setArchived(notesDTO.isArchived());
        note.setUser(user);
        return noteRepo.save(note);
    }

    public Note update(Long id, NotesDTO note) {
        Note currentNote = findById(id);
        currentNote.setTitle(note.getTitle());
        currentNote.setContent(note.getContent());
        currentNote.setCategory(note.getCategory());
        return noteRepo.save(currentNote);
    }

    public NotesDTO toDto(Note note) {
        NotesDTO notesDTO = new NotesDTO();
        notesDTO.setId(note.getId());
        notesDTO.setTitle(note.getTitle());
        notesDTO.setContent(note.getContent());
        notesDTO.setCategory(note.getCategory());
        notesDTO.setArchived(note.isArchived());
        notesDTO.setCreatedAt(note.getCreatedAt());
        return notesDTO;
    }

    public List<NotesDTO> toDtoList(List<Note> notes) {
        return notes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Note toggleArchive(Long id) {
        Note note = findById(id);
        note.setArchived(!note.isArchived());
        return noteRepo.save(note);
    }

    public void deleteById(Long id) {
        if (!noteRepo.existsById(id)) {
            throw new EntityNotFoundException("Note not found with id: " + id);
        }
        noteRepo.deleteById(id);
    }
}
