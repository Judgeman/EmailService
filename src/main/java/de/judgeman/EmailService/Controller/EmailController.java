package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Model.Email;
import de.judgeman.EmailService.Model.EmailRequestObject;
import de.judgeman.EmailService.Services.AppKeyService;
import de.judgeman.EmailService.Services.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class EmailController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/sendEmail", consumes = "application/json", produces = "application/json")
    public ResponseEntity sendEmail(HttpServletRequest request, @RequestBody EmailRequestObject emailRequestObject) throws IOException {
        AppKey appKey = emailRequestObject.getAppKey();
        String senderEmailAddress = appKeyService.getSenderEmailForAppKey(appKey);

        Email email = emailService.saveNewEmailRequest(emailRequestObject, senderEmailAddress, request.getRemoteAddr());
        logger.debug("Request email sending with appKey: " + appKey.getAppId());

        if (!appKeyService.isAppKeyValid(appKey)) {
            throw new AccessDeniedException("Key value " + appKey.getKeyValue() + " for " + appKey.getAppId() + " not accepted");
        }

        email.setAppKeyValueVerified(true);
        emailService.saveEmail(email);

        int exitStatus = -1;
        try {
            exitStatus = emailService.sendEmail(emailRequestObject, senderEmailAddress);
        } catch (Exception ex) {
            logger.error("Error on trying send the email", ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (exitStatus != 0) {
            logger.debug("Email not sent");
            logger.debug("Exit status of postfix: " + exitStatus);

            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Email sent");
        email.setEmailSent(true);
        emailService.saveEmail(email);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/emails")
    public @ResponseBody List<Email> getEmails(@RequestParam(required = false) Integer limit) {
        if (limit == null) {
            limit = 0;
        }

        return emailService.getEmails(limit);
    }
}
