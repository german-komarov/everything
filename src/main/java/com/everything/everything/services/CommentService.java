package com.everything.everything.services;

import com.everything.everything.entities.Comment;
import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    public void saveComment(Comment comment)
    {
        commentRepository.save(comment);
    }

    public void deleteCommentById(Long id)
    {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(Long id)
    {
        Optional<Comment> comment=commentRepository.findById(id);
        return comment.orElse(null);
    }

    public List<Comment> getCommentsByAuthor(Person author)
    {
        return commentRepository.findCommentsByAuthor(author);
    }

    public List<Comment> getCommentsByNote(Note note)
    {
        return commentRepository.findCommentsByNote(note);
    }


}
