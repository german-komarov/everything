package com.everything.everything.controllers;

import com.everything.everything.entities.Contact;
import com.everything.everything.entities.Person;
import com.everything.everything.services.ContactService;
import com.everything.everything.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Autowired
    private PersonService personService;

    @GetMapping
    public String allContacts(@AuthenticationPrincipal Person principal, Model model)
    {
        Person person=personService.getUserById(principal.getId());
        Set<Contact> contactRequests=person.getTo();
        Set<Person> contacts=person.getContacts();
        model.addAttribute("contacts",contacts);
        model.addAttribute("person",person);
        model.addAttribute("contactRequests",contactRequests);
        return "contacts";

    }
}
