package de.judgeman.EmailService.Exceptions;

public class UsernameAlreadyExistsException extends Exception {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}