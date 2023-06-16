package de.judgeman.EmailService.Model;

/**
 * Created by Paul Richter on Thu 15/06/2023
 */
public class EmailSentResult {

    private boolean emailSent;

    private Exception exception;

    public EmailSentResult(boolean emailSent) {
        this(emailSent, null);
    }

    public EmailSentResult(boolean emailSent, Exception exception) {
        this.emailSent = emailSent;
        this.exception = exception;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
