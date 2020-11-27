package jarg.templates.account_manager.controllers;

import jarg.templates.account_manager.security.users.UserRepository;
import jarg.templates.account_manager.security.users.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Controller for managing account creation, deletion and updates.
 */
@RestController
public class AccountController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository repository;

    /**
     * Creates a user account with the provided details.
     * @param username the username
     * @param password the password
     * @param firstName the First Name of the user
     * @param lastName the Last Name of the user
     * @return A String indicating success. The client needs this as a
     *          response, in order to be redirected to the login page on
     *          success.
     */
    @PostMapping(
            path = "/create_account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String createAccount(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                @RequestParam("first_name") String firstName,
                                @RequestParam("last_name") String lastName){
        // let's encrypt the password first
        String encrPassword = passwordEncoder.encode(password);
        // save username and password to db
        User newUser = new User(username, encrPassword, firstName, lastName);
        repository.save(newUser);
        return "done";
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
        return repository.findByUsername(userName);
    }

    /**
     * Handles a request to delete an account.
     * @param request the {@link HttpServletRequest}.
     * @return  A String indicating success or failure.
     */
    @PostMapping(
            path = "/delete_account",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteAccount(HttpServletRequest request){
        Optional<User> userOpt = getUser(request);
        if(userOpt.isEmpty()){
            return "User not found.";
        }
        repository.delete(userOpt.get());
        return "done";
    }


    /**
     * Handles a request to update the First Name, the Last Name or both names
     * of a user.
     * @param request the {@link HttpServletRequest}.
     * @param firstName the First Name of the user.
     * @param lastName the Last Name of the user.
     * @return  A String indicating success or failure.
     */
    @PatchMapping(
            path = "/update_names",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String updateBothNames(HttpServletRequest request,
                                  @RequestParam("first_name") String firstName,
                                  @RequestParam("last_name") String lastName){
        if( (firstName == null) && (lastName == null) ){
            return "Only either the First Name or the Last Name can be empty, " +
                    "not both";
        }
        Optional<User> userOpt = getUser(request);
        if(userOpt.isEmpty()){
            return "User not found.";
        }
        User user = userOpt.get();
        if(firstName != null){
            user.setFirstName(firstName);
        }
        if(lastName != null) {
            user.setLastName(lastName);
        }
        repository.save(user);
        return "done";
    }
}
