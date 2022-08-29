package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Model.Email;
import de.judgeman.EmailService.Services.AppKeyService;
import de.judgeman.EmailService.Services.EmailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Controller
public class EmailSendController {

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private EmailSendService emailSendService;

    @PostMapping(value = "/sendEmail", consumes = "application/json", produces = "application/json")
    @ResponseStatus(code = HttpStatus.OK)
    public void sendEmail(@RequestBody Email email) throws IOException, InterruptedException {
        AppKey appKey = email.getAppKey();

        if (!appKeyService.isAppKeyValid(appKey)) {
            throw new AccessDeniedException("Key value " + appKey.getKeyValue() + " for " + appKey.getAppId() + " not accepted");
        }

        emailSendService.sendEmail(email);
    }
}
