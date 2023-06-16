package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.Email;
import de.judgeman.EmailService.Model.EmailRequestObject;
import de.judgeman.EmailService.Model.EmailSentResult;
import de.judgeman.EmailService.Repositories.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String DEFAULT_EMAIL_TEXT_FORMAT = "UTF-8";

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private SettingsService settingsService;

    public EmailSentResult sendEmail(EmailRequestObject email, String senderAddress) {
        String username = settingsService.getSettingValue(SettingsService.SMTP_USERNAME);
        String password = settingsService.getSettingValue(SettingsService.SMTP_PASSWORD);
        Properties properties = createNewPropertiesForConnection();

        Session session = createNewJavaxMailSession(properties, username, password);

        try
        {
            MimeMessage message = new MimeMessage(session);

            setMimeMessageHeader(message);
            setMessageProperties(message, senderAddress, email.getEmailAddress(), email.getSubject(), email.getMessage());

            Transport.send(message);

            return new EmailSentResult(true);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return new EmailSentResult(false, exception);
        }
    }

    private Properties createNewPropertiesForConnection() {
        Properties properties = new Properties();

        String smtpHost = settingsService.getSettingValue(SettingsService.SMTP_HOST);
        String smtpPort = settingsService.getSettingValue(SettingsService.SMTP_PORT);

        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        return properties;
    }

    private void setMessageProperties(MimeMessage message, String senderAddress, String receiverEmail, String subject, String emailText) throws MessagingException {
        message.setFrom(new InternetAddress(senderAddress));
        message.setReplyTo(InternetAddress.parse(senderAddress, false));
        message.setSubject(subject, DEFAULT_EMAIL_TEXT_FORMAT);
        message.setText(emailText, DEFAULT_EMAIL_TEXT_FORMAT);
        message.setSentDate(new Date());
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail, false));
    }

    private void setMimeMessageHeader(MimeMessage message) throws Exception {
        message.addHeader("Content-type", "text/HTML; charset=" + DEFAULT_EMAIL_TEXT_FORMAT);
        message.addHeader("format", "flowed");
        message.addHeader("Content-Transfer-Encoding", "8bit");
    }

    private Session createNewJavaxMailSession(Properties props, String fromEmail, String password) {
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        return Session.getInstance(props, auth);
    }

    public Email saveNewEmailRequest(EmailRequestObject emailRequestObject, String senderEmailAddress, String remoteRequestAddress) {
        Email email = new Email();

        email.setEmailAddress(emailRequestObject.getEmailAddress());
        email.setMessage(emailRequestObject.getMessage());
        email.setSubject(emailRequestObject.getSubject());
        email.setSenderAddress(senderEmailAddress);
        email.setRemoteRequestAddress(remoteRequestAddress);

        if (emailRequestObject.getAppKey() != null) {
            email.setAppKeyId(emailRequestObject.getAppKey().getAppId());
        }

        emailRepository.save(email);
        return email;
    }

    public void saveEmail(Email email) {
        emailRepository.save(email);
    }

    public List<Email> getEmails(int limit) {
        if (limit <= 0) {
            return emailRepository.findAllByOrderByIdDesc();
        }

        Pageable pageable = PageRequest.of(0, limit);
        return emailRepository.findAllByOrderByIdDesc(pageable).stream().toList();
    }

    public List<Email> getEmailsForATimeSpan(LocalDateTime startDate, LocalDateTime endDate) {
        return emailRepository.findBySendingDateBetween(startDate, endDate);
    }
}
