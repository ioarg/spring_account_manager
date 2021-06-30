package jarg.templates.account_manager.controllers;

import jarg.templates.account_manager.security.users.UserRepository;
import jarg.templates.account_manager.security.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Controller for the Home Page
 */
@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;

    /**
     * Retrieve user's Dashboard page. This is the main user
     * page and displays basic user information.
     * When a new session is created, user information is
     * retrieved from the database and set as session attributes.
     * When a session already exists, user information is retrieved
     * from the session attributes.
     *
     * @param request The servlet request
     * @param model The model
     * @return the name of the Dashboard view
     */
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
        return "dashboard";
    }

    /**
     * The Home Page. This is where users can log in or new user
     * accounts can be created.
     *
     * @param user a {@link ModelAttribute} used for data binding.
     *             Represents the user.
     * @return the name of the Home Page view
     */
    @GetMapping("/home")
    public String getHomePage(@ModelAttribute("user") User user){
        return "home";
    }

}
