package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OverviewController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String overview(Model model) {

        model.addAttribute("authenticatedUser", userService.getAuthenticatedUser());

        return "overview";
    }
}
