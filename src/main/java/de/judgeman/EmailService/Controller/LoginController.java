package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {

        model.addAttribute("authenticatedUser", userService.getAuthenticatedUser());

        return "login";
    }
}
