package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Repositories.AppKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AppKeyService {

    @Value("${defaultSystemEmailAddress}")
    private String defaultSystemEmail;

    @Autowired
    private AppKeyRepository appKeyRepository;

    public AppKey createNewAppKey(String appId) throws KeyAlreadyExistsException {
        AppKey appKey = appKeyRepository.findByAppId(appId);
        if (appKey != null) {
            throw new KeyAlreadyExistsException(appId + " already exists");
        }

        appKey = new AppKey();
        appKey.setAppId(appId);
        appKey.setKeyValue(createUUID());

        appKeyRepository.save(appKey);

        return appKey;
    }

    public String createUUID() {
        return UUID.randomUUID().toString();
    }

    public AppKey getAppKey(String appId) {
        return appKeyRepository.findByAppId(appId);
    }

    public boolean isAppKeyValid(AppKey appKey) {
        AppKey appKeyFromDatabase = getAppKey(appKey.getAppId());
        if (appKeyFromDatabase == null) {
            return false;
        }

        return appKey.getKeyValue().equals(appKeyFromDatabase.getKeyValue());
    }

    public String getSenderEmailForAppKey(AppKey appKey) {
        AppKey appKeyFromDatabase = getAppKey(appKey.getAppId());
        if (appKeyFromDatabase == null || appKeyFromDatabase.getSpecificSenderEmailAddress() == null) {
            return defaultSystemEmail;
        }

        return appKeyFromDatabase.getSpecificSenderEmailAddress();
    }

    public ArrayList<AppKey> getAllAppKeys() {
        return appKeyRepository.findAll();
    }

    public void delete(AppKey appKey) {
        appKeyRepository.delete(appKey);
    }

    public Object getDefaultSenderEmailAddress() {
        return defaultSystemEmail;
    }

    public void updateAppKey(AppKey appKey) {
        appKeyRepository.save(appKey);
    }

    /*
    *   Hier kennst du den schon?
    *
    *   Kunde: "Do you have a four volt, two watt light bulb?"
    *   Verkäufer: "For what?"
    *   Kunde: "No, two."
    *   Verkäufer: "To what..."
    *   Kunde: "Yes."
    *   Verkäufer: "No"
    *   Kunde: "Thank you. Goodbye"
    *   Verkäufer: "Goodbye"
    *
    *   :D
    * */
}
