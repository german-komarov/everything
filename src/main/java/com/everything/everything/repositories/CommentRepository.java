package com.everything.everything.repositories;

import com.everything.everything.entities.Comment;
import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentsByAuthor(Person author);
    List<Comment> findCommentsByNote(Note note);

}
