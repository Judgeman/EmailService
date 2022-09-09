package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.Email;
import de.judgeman.EmailService.Model.EmailRequestObject;
import de.judgeman.EmailService.Repositories.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class EmailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailRepository emailRepository;

    public int sendEmail(EmailRequestObject email, String senderAddress) throws IOException, InterruptedException {
        String command = "echo \"" + email.getMessage() + "\" | mail -s \"" + email.getSubject() + "\" -a \"From: " + senderAddress + "\" " + email.getEmailAddress();
        logger.debug("Try to run command: " + command);

        Process process = Runtime.getRuntime().exec(new String[]{"sh","-c", command},  null, null);
        readInputStream(process);

        return process.waitFor();
    }

    private void readInputStream(Process process) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            logger.debug(line);
        }
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
}
