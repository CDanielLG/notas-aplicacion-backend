package com.ensolver.springboot.app.notes.repo;

import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface INoteRepo extends JpaRepository<Note, Long> {

    List<Note> findByUserAndArchivedOrderByCreatedAtDesc(User user, boolean archived);

    List<Note> findByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT DISTINCT n.category FROM Note n WHERE n.user = :user ORDER BY n.category ASC")
    List<String> findCategoriesByUser(@Param("user") User user);
}
