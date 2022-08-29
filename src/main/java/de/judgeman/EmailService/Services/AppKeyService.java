package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Repositories.AppKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AppKeyService {

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
            throw new NoSuchElementException(appKey.getAppId() + " not exist!");
        }

        return appKey.getKeyValue().equals(appKeyFromDatabase.getKeyValue());
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
