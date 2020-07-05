package com.everything.everything.controllers;

import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.services.NoteService;
import com.everything.everything.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private PersonService personService;



    @GetMapping("/createNote")
    public String createNoteGet(@AuthenticationPrincipal Person person, Model model)
    {
        model.addAttribute("username",person.getUsername());
        return "create_note";
    }

    @PostMapping("/createNote")
    public String createNotePost(@AuthenticationPrincipal Person person, @RequestParam String content, @RequestParam MultipartFile attachment) throws IOException {
        Note note=new Note();

        note.setContent(content);
        note.setAttachment(Base64.getEncoder().encodeToString(attachment.getBytes()));
        if(Objects.requireNonNull(attachment.getContentType()).startsWith("image")) {
            note.setAttachmentType("image");
        }

        else if(Objects.requireNonNull(attachment.getContentType()).startsWith("audio")) {
            note.setAttachmentType("audio");
        }
        else if(Objects.requireNonNull(attachment.getContentType()).startsWith("video")) {
            note.setAttachmentType("video");
        }

        note.setAuthor(person.getUsername());
        note.setAuthorId(person.getId());
        noteService.saveNote(note);
        return "redirect:/main";
    }

    @GetMapping("/likeNote/{id}")
    public String likeNoteInController(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        if(noteService.likeNote(id,principal.getId())) {
            return "redirect:/main";
        }

        return "denied_page";
    }

    @GetMapping("/unlikeNote/{id}")
    public String unlikeNoteInController(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        Person person=personService.getUserById(principal.getId());
        if(noteService.unlikeNote(id,principal.getId())) {
            return "redirect:/main";
        }

        return "denied_page";
    }



}
