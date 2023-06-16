package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Model.Email;
import de.judgeman.EmailService.Model.EmailRequestObject;
import de.judgeman.EmailService.Model.EmailSentResult;
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

import java.util.List;

@Controller
public class EmailController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppKeyService appKeyService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/sendEmail", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> sendEmail(HttpServletRequest request, @RequestBody EmailRequestObject emailRequestObject) {
        AppKey appKey = emailRequestObject.getAppKey();
        String senderEmailAddress = appKeyService.getSenderEmailForAppKey(appKey);

        Email email = emailService.saveNewEmailRequest(emailRequestObject, senderEmailAddress, request.getRemoteAddr());
        logger.debug("Request email sending with appKey: " + appKey.getAppId());

        if (!appKeyService.isAppKeyValid(appKey)) {
            String errorMessage = "Key value " + appKey.getKeyValue() + " for " + appKey.getAppId() + " not accepted";
            logger.debug(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }

        email.setAppKeyValueVerified(true);
        emailService.saveEmail(email);

        EmailSentResult result = emailService.sendEmail(emailRequestObject, senderEmailAddress);
        if (!result.isEmailSent()) {
            logger.debug("Email not sent");

            email.setErrorMessage(result.getException().getMessage());
            emailService.saveEmail(email);

            return new ResponseEntity<>(result.getException().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.debug("Email sent");
        email.setEmailSent(true);
        emailService.saveEmail(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/emails")
    public @ResponseBody List<Email> getEmails(@RequestParam(required = false) Integer limit) {
        if (limit == null) {
            limit = 0;
        }

        return emailService.getEmails(limit);
    }
}
