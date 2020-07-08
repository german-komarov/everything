package com.everything.everything.controllers;


import com.everything.everything.entities.Contact;
import com.everything.everything.entities.Note;
import com.everything.everything.entities.Person;
import com.everything.everything.services.ContactService;
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

    @Autowired
    private ContactService contactService;

    @GetMapping("{id}")
    public String channelOfThePerson(@AuthenticationPrincipal Person principal, @PathVariable Long id, Model model)
    {
        Person person=personService.getUserById(principal.getId());
        Person channel=personService.getUserById(id);
        if(person==channel)
        {
            return "redirect:/profile";
        }
        Contact contactFrom=contactService.getContactByFromAndTo(principal,channel);
        Contact contactTo=contactService.getContactByFromAndTo(channel,principal);
        List<Note> notes=noteService.getAllNotesOfThisUser(id);
        if(person.getSubscriptions().contains(channel)) {
            model.addAttribute("isSubscribed", "True");
        }
        else
        {
            model.addAttribute("isSubscribed", "False");
        }
        if (contactFrom!=null)
        {
            model.addAttribute("contactFrom",true);
        }
        if(contactTo!=null)
        {
            model.addAttribute("contactTo",true);
        }
        if(contactFrom==null && contactTo==null && !person.getContacts().contains(channel))
        {
            model.addAttribute("contact",true);
        }
        if(person.getContacts().contains(channel))
        {
            model.addAttribute("alreadyContacts",true);
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





    @GetMapping("/sendContact/{id}")
    public String doContact(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        Contact contact=new Contact();
        contact.setFrom(personService.getUserById(principal.getId()));
        contact.setTo(personService.getUserById(id));
        contactService.saveContact(contact);
        return "redirect:/channel/"+id;
    }

    @GetMapping("/addContact/{id}")
    public String doAddContact(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        Person person1=personService.getUserById(principal.getId());
        Person person2=personService.getUserById(id);
        personService.addContact(person1,person2);
        personService.addContact(person2,person1);
        Contact contact1=contactService.getContactByFromAndTo(person1,person2);
        Contact contact2=contactService.getContactByFromAndTo(person2,person1);
        if(contact1!=null)
        {
            contactService.deleteContactById(contact1.getId());
        }
        if(contact2!=null)
        {
            contactService.deleteContactById(contact2.getId());
        }

        return "redirect:/channel/"+id;

    }

    @GetMapping("/deleteContact/{id}")
    public String doDeleteContact(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        Person person1=personService.getUserById(principal.getId());
        Person person2=personService.getUserById(id);
        personService.deleteContact(person1,person2);
        return "redirect:/channel/"+id;
    }

    @GetMapping("/refuseContact/{id}")
    public String doRefuseContact(@AuthenticationPrincipal Person principal,@PathVariable Long id)
    {
        Person person1=personService.getUserById(principal.getId());
        Person person2=personService.getUserById(id);
        Contact contact1=contactService.getContactByFromAndTo(person1,person2);
        Contact contact2=contactService.getContactByFromAndTo(person2,person1);
        if(contact1!=null)
        {
            contactService.deleteContactById(contact1.getId());
        }
        if(contact2!=null)
        {
            contactService.deleteContactById(contact2.getId());
        }
        return "redirect:/channel/"+id;
    }
}

