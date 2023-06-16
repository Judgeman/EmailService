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
import java.util.Date;
import java.util.List;

/**
 * Created by Paul Richter on Sun 14/05/2023
 */
@Service
public class CronJobService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Date startDate = new Date();

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

        logger.info("Service is running until " + startDate);
        logger.info("Week Start Date: " + weekStart);
        logger.info("Week End Date: " + weekEnd);

        List<Email> emails = emailService.getEmailsForATimeSpan(weekStart, weekEnd);
        StringBuilder emailSummary = new StringBuilder();
        emailSummary.append("Service läuft seit: ")
                    .append(startDate)
                    .append(" \n")
                    .append("Für die letzte Woche (")
                    .append(weekStart)
                    .append(" bis ")
                    .append(weekEnd)
                    .append(") wurden ")
                    .append(emails.size())
                    .append(" Emails gefunden:\n");

        for (Email email : emails) {
            emailSummary.append("----------------------------------- \n")
                        .append(email.getId())
                        .append(") ")
                        .append(email.getSubject())
                        .append(" (")
                        .append(email.getSendingDate())
                        .append(")")
                        .append("\n")
                        .append("Sender: ")
                        .append(email.getSenderAddress())
                        .append("\n")
                        .append("Empfänger: ")
                        .append(email.getEmailAddress())
                        .append("\n")
                        .append("AppKey: ")
                        .append(email.getAppKeyId())
                        .append(" (verifiziert: ")
                        .append(email.isAppKeyValueVerified())
                        .append(", gesendet: ")
                        .append(email.isEmailSent())
                        .append(email.isEmailSent() ? "" : " - Fehler:")
                        .append(email.getErrorMessage())
                        .append(")")
                        .append("\n")
                        .append("IP: ")
                        .append(email.getRemoteRequestAddress())
                        .append("\n")
                        .append(email.getMessage())
                        .append("\n");
        }

        return emailSummary.toString();
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
