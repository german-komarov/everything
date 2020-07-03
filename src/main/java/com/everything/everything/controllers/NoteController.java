package com.everything.everything.controllers;

import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;



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
        noteService.saveNote(note);
        return "redirect:/main";
    }
}
