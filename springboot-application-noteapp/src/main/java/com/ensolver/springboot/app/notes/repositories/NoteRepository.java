package com.ensolver.springboot.app.notes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.Usuario;

public interface NoteRepository extends JpaRepository<Note, Long>{

	
    List<Note> findByArchived(boolean archived);
    List<Note> findByUser(Usuario user);
    List<Note> findByUserAndArchived(Usuario user, boolean archived);


@Query("SELECT DISTINCT n.category FROM Note n WHERE n.user = :user AND n.category IS NOT NULL")
List<String> findDistinctCategoriesByUser(@Param("user") Usuario user);

    List<Note> findByUserAndCategory(Usuario user, String category);
}
