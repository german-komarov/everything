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

import java.util.List;

@Controller
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private PersonService personService;

    @Autowired
    private NoteService noteService;

    @GetMapping("{id}")
    public String channelOfThePerson(@AuthenticationPrincipal Person principal, @PathVariable Long id, Model model)
    {
        Person person=personService.getUserById(principal.getId());
        Person channel=personService.getUserById(id);
        List<Note> notes=noteService.getAllNotesOfThisUser(id);
        if(person.getSubscriptions().contains(channel)) {
            model.addAttribute("isSubscribed", "True");
        }
        else
        {
            model.addAttribute("isSubscribed", "False");
        }
        model.addAttribute("channel",channel);
        model.addAttribute("numberOfNotes",notes.size());
        model.addAttribute("username",person.getUsername());
        model.addAttribute("notes",notes);
        return "channel";
    }

    @GetMapping("/subscribe/{id}")
    public String doSubscribe(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        Person person=personService.getUserById(principal.getId());
        if(personService.subscribe(personService.getUserById(id),person))
        {

            return "redirect:/channel/"+id;
        }
        else
        {
            return "denied_page";
        }

    }

    @GetMapping("/unsubscribe/{id}")
    public String doUnsubscribe(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {

        Person person=personService.getUserById(principal.getId());
        if(personService.unsubscribe(personService.getUserById(id),person))
        {

            return "redirect:/channel/"+id;
        }
        else
        {
            return "denied_page";
        }

    }
}
