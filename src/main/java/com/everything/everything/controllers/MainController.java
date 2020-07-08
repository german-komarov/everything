package com.everything.everything.controllers;

import com.everything.everything.entities.Comment;
import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.services.CommentService;
import com.everything.everything.services.NoteService;
import com.everything.everything.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main")
public class MainController {

    @Autowired
    private PersonService personService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NoteService noteService;


    @GetMapping
    public String mainPage(@AuthenticationPrincipal Person principal, Model model)
    {
        Person person=personService.getUserById(principal.getId());
        List<Note> notes=noteService.getAllNotesFromChannels(person.getId());
        for(Note note:notes)
        {
            if(note.getLikes().contains(person))
            {
                note.setLikedByThisUser(true);
                notes.set(notes.indexOf(note),note);

            }

        }
        model.addAttribute("notes", notes);
        model.addAttribute("person",person);
        return "main";
    }


    @GetMapping("/comments/{noteId}")
    public String commentsPage(@AuthenticationPrincipal Person principal, @PathVariable Long noteId,Model model)
    {
        Person person=personService.getUserById(principal.getId());
        List<Comment> comments=commentService.getCommentsByNote(noteService.getNoteById(noteId));
        model.addAttribute("noteId",noteId);
        model.addAttribute("comments",comments);
        model.addAttribute("author",person);
        return "comments_page";
    }

    @PostMapping("/comments/{noteId}/leaveComment")
    public String leaveCommentPage(@AuthenticationPrincipal Person principal,@PathVariable Long noteId,@RequestParam String text)
    {
        Person person=personService.getUserById(principal.getId());
        Note note=noteService.getNoteById(noteId);
        Comment comment=new Comment();
        comment.setAuthor(person);
        comment.setNote(note);
        comment.setText(text);
        commentService.saveComment(comment);
        return "redirect:/main/comments/"+noteId;
    }







}
