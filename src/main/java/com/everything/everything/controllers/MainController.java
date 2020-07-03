package com.everything.everything.controllers;

import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.services.NoteService;
import com.everything.everything.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class MainController {

    @Autowired
    private PersonService personService;

    @Autowired
    private NoteService noteService;

    @GetMapping("/")
    public String entirePage()
    {
        return "welcome_page";
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal Person person,
                           @PageableDefault(size = 7,sort = {"creationDate"},direction = Sort.Direction.DESC) Pageable pageable,
                           Model model)
    {
        Page<Note> notes=noteService.getAllNotesFromChannels(person.getId(),pageable);

        model.addAttribute("notes", notes.getContent());
        model.addAttribute("username",person.getUsername());
        return "main";
    }







}
