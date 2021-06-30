package jarg.templates.account_manager.controllers;

import jarg.templates.account_manager.security.users.UserRepository;
import jarg.templates.account_manager.security.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller for managing account creation, deletion and updates.
 */
@Controller
public class AccountController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    /**
     * Creates a user account with the provided details.
     *
     * @param user a {@link ModelAttribute} used for data binding.
     *             Represents the user.
     * @return A String indicating success. The client needs this as a
     *          response, in order to be redirected to the login page on
     *          success.
     */
    @PostMapping(
            path = "/create_account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createAccount(@Valid @ModelAttribute("user") User user){

        // check if the username already exists
        Optional<User> userOptional = repository.findByUsername(user.getUsername());
        if (userOptional.isPresent()){
            return ResponseEntity.badRequest().body("Failed to create account. This username already exists.");
        }

        // let's encrypt the password first
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // save user to db
        repository.save(user);

        return ResponseEntity.ok("Success");
    }


    /**
     * Retrieves a {@link User} from the database. The request's session
     * is used to retrieve the username with which the user will be identified
     * in the database.
     * @param request the {@link HttpServletRequest} used to retrieve the {@link
     *                  HttpSession}.
     * @return An {@link Optional} possibly containing a {@link User}
     */
    private Optional<User> getUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        System.out.println("Got user : "+userName);
        return repository.findByUsername(userName);
    }

    /**
     * Handles a request to delete an account.
     * @param request the {@link HttpServletRequest}.
     * @return  the url that the user will be redirected to.
     */
    @PostMapping("/delete_account")
    public String deleteAccount(HttpServletRequest request){
        Optional<User> userOpt = getUser(request);
        if(!userOpt.isPresent()){
            return "redirect:/";
        }
        repository.delete(userOpt.get());
        request.getSession().invalidate();
        return "redirect:/home";
    }
}
