package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class EmailSendService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${defaultSystemEmailAddress}")
    private String defaultSystemEmail;

    public void sendEmail(Email email) throws IOException, InterruptedException {
        String command = "echo \"" + email.getMessage() + "\" | mail -s \"" + email.getSubject() + "\" -a \"From: " + defaultSystemEmail + "\" " + email.getEmailAddress();
        logger.debug("Try to run command: " + command);

        Process process = Runtime.getRuntime().exec(new String[]{"sh","-c", command},  null, null);
        readInputStream(process);

        process.waitFor();
    }

    private void readInputStream(Process process) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            logger.debug(line);
        }
    }
}
