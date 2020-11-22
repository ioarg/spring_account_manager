package jarg.templates.account_manager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getDashboard(){
        return "dashboard";
    }

    @GetMapping("/home")
    public String getHomePage(){
        return "home";
    }

}
