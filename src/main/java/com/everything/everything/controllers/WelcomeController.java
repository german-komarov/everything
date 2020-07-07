package com.everything.everything.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String entirePage()
    {
        return "welcome_page";
    }
}
