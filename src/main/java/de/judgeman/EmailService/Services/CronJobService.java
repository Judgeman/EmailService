package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.Email;
import de.judgeman.EmailService.Model.EmailRequestObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul Richter on Sun 14/05/2023
 */
@Service
public class CronJobService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${defaultSystemEmailAddress}")
    private String defaultSystemEmail;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private EmailService emailService;

    // second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "0 0 20 * * SUN")
    public void sendWeeklyInformation() {
        String email = settingsService.getSettingValue(SettingsService.EMAIL_FOR_WEEKLY_SUMMARY);
        if (email == null || email.isEmpty()) {
            logger.info("No email address for weekly summary");
            return;
        }

        String weeklySummary = getWeeklySummary();
        logger.info("Weekly Summary: " + weeklySummary);

        EmailRequestObject emailRequestObject = new EmailRequestObject();
        emailRequestObject.setMessage(weeklySummary);
        emailRequestObject.setEmailAddress(email);
        emailRequestObject.setSubject("Wöchentliche Zusammenfassung");

        try {
            emailService.sendEmail(emailRequestObject, email);
            logger.info("Sent weekly summary as email to the address: " + email);
        } catch (Exception ex) {
            logger.error("Error on sending weekly summary: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String getWeeklySummary() {
        LocalDateTime weekEnd = getLocalDateTimeNow();
        LocalDateTime weekStart = getLocalDateTimeBeforeSevenDays();

        logger.info("Week Start Date: " + weekStart);
        logger.info("Week End Date: " + weekEnd);

        List<Email> emails = emailService.getEmailsForATimeSpan(weekStart, weekEnd);

        if (emails == null) {
            return "Keine Emails in der vergangenen Woche";
        }

        StringBuilder emailSummary = new StringBuilder();
        for (Email email : emails) {
            emailSummary.append("----------------------------------- \n");
            emailSummary.append(email.getId());
            emailSummary.append(") ");
            emailSummary.append(email.getSubject());
            emailSummary.append(" (");
            emailSummary.append(email.getSendingDate());
            emailSummary.append(")");
            emailSummary.append("\n");
            emailSummary.append("Von: ");
            emailSummary.append(email.getSenderAddress());
            emailSummary.append("\n");
            emailSummary.append("An: ");
            emailSummary.append(email.getEmailAddress());
            emailSummary.append("\n");
            emailSummary.append("AppKey: ");
            emailSummary.append(email.getAppKeyId());
            emailSummary.append(" (verifiziert: ");
            emailSummary.append(email.isAppKeyValueVerified());
            emailSummary.append(", gesendet: ");
            emailSummary.append(email.isEmailSent());
            emailSummary.append(")");
            emailSummary.append("\n");
            emailSummary.append("IP: ");
            emailSummary.append(email.getRemoteRequestAddress());
            emailSummary.append("\n");
            emailSummary.append(email.getMessage());
            emailSummary.append("\n");
        }

        return "Für die letzte Woche (" + weekStart + " bis " + weekEnd + ") wurden " + emails.size() + " Emails gefunden:\n" + emailSummary;
    }

    private LocalDateTime getLocalDateTimeBeforeSevenDays() {
        Calendar calendar = getConfiguredCalendar();
        calendar.add(Calendar.DATE, -7);
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    private LocalDateTime getLocalDateTimeNow() {
        Calendar calendar = getConfiguredCalendar();
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    private Calendar getConfiguredCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        return calendar;
    }
}
