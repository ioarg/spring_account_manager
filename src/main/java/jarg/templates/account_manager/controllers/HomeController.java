package jarg.templates.account_manager.controllers;

import jarg.templates.account_manager.security.users.UserRepository;
import jarg.templates.account_manager.security.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    private void updateModel(Model model, HttpSession session){
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("firstName", session.getAttribute("firstName"));
        model.addAttribute("lastName", session.getAttribute("lastName"));
    }

    @GetMapping("/")
    public String getDashboard(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        if(session.getAttribute("set") == null){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userName;
            if(principal instanceof UserDetails){
                userName = ((UserDetails) principal).getUsername();
            }else {
                userName = principal.toString();
            }
            // Get user from db - should use caching since this is already executed once at login
            Optional<User> userOpt = userRepository.findByUsername(userName);
            User user = userOpt.get();
            session.setAttribute("set", "true");
            session.setAttribute("username", userName);
            session.setAttribute("firstName", user.getFirstName());
            session.setAttribute("lastName", user.getLastName());
        }
        updateModel(model, session);
        return "dashboard";
    }

    @GetMapping("/home")
    public String getHomePage(){
        return "home";
    }

}
