package de.judgeman.EmailService.Model;

import java.io.Serializable;

public class Email implements Serializable {

    private AppKey appKey;

    private String emailAddress;

    private String subject;

    private String message;

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

    public AppKey getAppKey() {
        return appKey;
    }

    public void setAppKey(AppKey appKey) {
        this.appKey = appKey;
    }
}
