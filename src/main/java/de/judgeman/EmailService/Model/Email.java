package de.judgeman.EmailService.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_generator")
    @SequenceGenerator(name="email_generator", sequenceName = "email_seq", allocationSize=1)
    private long Id;

    private LocalDateTime sendingDate;

    private String senderAddress;

    private String appKeyId;

    private String emailAddress;

    private String subject;

    private String message;

    private boolean appKeyValueVerified;

    private boolean emailSent;

    private String remoteRequestAddress;

    private String errorMessage;

    public Email() {
        sendingDate = LocalDateTime.now();
        appKeyValueVerified = false;
        emailSent = false;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public LocalDateTime getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(LocalDateTime sendingDate) {
        this.sendingDate = sendingDate;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getAppKeyId() {
        return appKeyId;
    }

    public void setAppKeyId(String appKeyId) {
        this.appKeyId = appKeyId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAppKeyValueVerified() {
        return appKeyValueVerified;
    }

    public void setAppKeyValueVerified(boolean appKeyValueVerified) {
        this.appKeyValueVerified = appKeyValueVerified;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public String getRemoteRequestAddress() {
        return remoteRequestAddress;
    }

    public void setRemoteRequestAddress(String remoteRequestAddress) {
        this.remoteRequestAddress = remoteRequestAddress;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
