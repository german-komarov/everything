package com.everything.everything.controllers;


import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.services.NoteService;
import com.everything.everything.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private PersonService personService;
    @Autowired
    private NoteService noteService;


    @GetMapping
    public String profileOfThePerson(@AuthenticationPrincipal Person principal, Model model)
    {
        Person person=personService.getUserById(principal.getId());
        List<Note> notes=noteService.getAllNotesOfThisUser(person.getId());
        model.addAttribute("numberOfNotes",notes.size());
        model.addAttribute("person",person);
        model.addAttribute("username",person.getUsername());
        model.addAttribute("notes",notes);
        return "my_profile";
    }






}
