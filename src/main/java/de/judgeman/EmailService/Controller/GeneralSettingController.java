package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Model.EmailChangeRequestObject;
import de.judgeman.EmailService.Model.SmtpChangeRequestObject;
import de.judgeman.EmailService.Services.SettingsService;
import de.judgeman.EmailService.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Paul Richter on Mon 15/05/2023
 */
@Controller
public class GeneralSettingController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private SettingsService settingsService;

    @Value("${application.version}")
    private String version;

    @GetMapping("/general/administration")
    public String userAdministration(Model model) {

        model.addAttribute("generalSystemReceivingEmailAddress", settingsService.getSettingValue(SettingsService.EMAIL_FOR_WEEKLY_SUMMARY));

        model.addAttribute("smtpHost", settingsService.getSettingValue(SettingsService.SMTP_HOST));
        model.addAttribute("smtpPort", settingsService.getSettingValue(SettingsService.SMTP_PORT));
        model.addAttribute("smtpUsername", settingsService.getSettingValue(SettingsService.SMTP_USERNAME));

        model.addAttribute("version", version);
        model.addAttribute("authenticatedUser", userService.getAuthenticatedUser());

        return "generalAdministration";
    }

    @PostMapping("/general/changeEmailAddress")
    @ResponseStatus(HttpStatus.OK)
    public void changeEmailAddress(@RequestBody EmailChangeRequestObject emailChangeRequestObject) {
        logger.info("Change email address for weekly summary to: " + emailChangeRequestObject.getEmailAddress());
        settingsService.saveSetting(SettingsService.EMAIL_FOR_WEEKLY_SUMMARY, emailChangeRequestObject.getEmailAddress());
    }

    @PostMapping("/general/changeSmtpSettings")
    @ResponseStatus(HttpStatus.OK)
    public void changeEmailAddress(@RequestBody SmtpChangeRequestObject smtpChangeRequestObject) {
        logger.info("Change smtp settings to host: " + smtpChangeRequestObject.getSmtpHost() + " and port: " + smtpChangeRequestObject.getSmtpPort() + " - with a new password");

        settingsService.saveSetting(SettingsService.SMTP_HOST, smtpChangeRequestObject.getSmtpHost());
        settingsService.saveSetting(SettingsService.SMTP_PORT, smtpChangeRequestObject.getSmtpPort());
        settingsService.saveSetting(SettingsService.SMTP_USERNAME, smtpChangeRequestObject.getSmtpUsername());
        settingsService.saveSetting(SettingsService.SMTP_PASSWORD, smtpChangeRequestObject.getSmtpPassword());
    }
}
