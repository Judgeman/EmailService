package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Exceptions.UserNotFoundException;
import de.judgeman.EmailService.Exceptions.UsernameAlreadyExistsException;
import de.judgeman.EmailService.Model.User;
import de.judgeman.EmailService.Model.UserChangeRequestObject;
import de.judgeman.EmailService.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PutMapping("/user/{username}")
    public @ResponseBody User createNewUser(@PathVariable String username) throws UsernameAlreadyExistsException {
        return userService.createNewUser(username);
    }

    @PostMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void changeUser(@RequestBody UserChangeRequestObject userChangeRequestObject) throws UserNotFoundException {
        logger.debug("Change user with username: " + userChangeRequestObject.getUsername());
        userService.updateUser(userChangeRequestObject);
    }

    @DeleteMapping("/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String username) {
        logger.debug("delete User with username " + username);

        User user = userService.getUser(username);
        if (user != null) {
            userService.delete(user);
        } else {
            throw new EntityNotFoundException("User with " + username + " not found!");
        }
    }

    @GetMapping("/user/administration")
    public String userAdministration(Model model) {

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("authenticatedUser", userService.getAuthenticatedUser());

        return "userAdministration";
    }
}
