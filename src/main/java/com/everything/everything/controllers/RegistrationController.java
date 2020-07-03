package com.everything.everything.controllers;


import com.everything.everything.entities.Person;
import com.everything.everything.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private PersonService personService;

    @GetMapping("/registration")
    public String getPage()
    {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationOfTheUser(@ModelAttribute Person person, Model model)
    {
        String registrationStatus=personService.registerUser(person);

        if(registrationStatus.equals("OK"))
        {
            model.addAttribute("email",person.getEmail());
            return "mail_was_sent";
        }

        model.addAttribute("error",registrationStatus);
        return "registration";
    }

    @GetMapping("/registration/activate/{activationCode}")
    public String activateUser(@PathVariable("activationCode") String activationCode,Model model)
    {
        Person person=personService.getUserByActivationCode(activationCode);
        if(person!=null)
        {
            person.setIsActivated(1);
            person.setActivationCode("");
            personService.saveUser(person);
            model.addAttribute("username",person.getUsername());
            return "user_was_activated";
        }

        return "denied_page";
    }

}
