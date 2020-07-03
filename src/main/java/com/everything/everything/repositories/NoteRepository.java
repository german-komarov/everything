package com.everything.everything.repositories;


import com.everything.everything.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {

}
