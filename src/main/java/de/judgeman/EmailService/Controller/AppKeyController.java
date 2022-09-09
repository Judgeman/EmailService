package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Services.AppKeyService;
import de.judgeman.EmailService.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.management.openmbean.KeyAlreadyExistsException;

@Controller
public class AppKeyController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private UserService userService;

    @PutMapping("/appKey/{appId}")
    public @ResponseBody AppKey createAppKey(@PathVariable String appId) throws KeyAlreadyExistsException {
        return appKeyService.createNewAppKey(appId);
    }

    @PostMapping("/appKey/{appId}")
    @ResponseStatus(HttpStatus.OK)
    public void changeAppKey(@RequestBody AppKey appKey) {
        logger.debug("Change AppKey with id: " + appKey.getAppId());
        appKeyService.updateAppKey(appKey);
    }

    @DeleteMapping("/appKey/{appId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAppKey(@PathVariable String appId) throws EntityNotFoundException {
        logger.debug("delete AppKey with id " + appId);

        AppKey appKey = appKeyService.getAppKey(appId);
        if (appKey != null) {
            appKeyService.delete(appKey);
        } else {
            throw new EntityNotFoundException("AppKey " + appId + " not found!");
        }
    }

    @GetMapping("/appKey/administration")
    public String appKeyAdministration(Model model) {

        model.addAttribute("appKeys", appKeyService.getAllAppKeys());
        model.addAttribute("authenticatedUser", userService.getAuthenticatedUser());
        model.addAttribute("defaultEmailAddress", appKeyService.getDefaultSenderEmailAddress());

        return "appKeyAdministration";
    }
}
