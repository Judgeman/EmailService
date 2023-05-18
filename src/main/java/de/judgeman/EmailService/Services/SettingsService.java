package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Model.SettingEntry;
import de.judgeman.EmailService.Repositories.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Paul Richter on Mon 15/05/2023
 */
@Service
public class SettingsService {

    public static final String EMAIL_FOR_WEEKLY_SUMMARY = "EmailForWeeklySummary";

    @Autowired
    private SettingsRepository settingsRepository;

    public String getSettingValue(String key) {
        SettingEntry settingEntry = settingsRepository.findById(key).orElse(null);
        if (settingEntry == null) {
            return null;
        }

        return settingEntry.getValue();
    }

    public void saveSetting(String key, String value) {
        SettingEntry settingEntry = settingsRepository.findById(key).orElse(new SettingEntry());
        settingEntry.setKey(key);
        settingEntry.setValue(value);

        settingsRepository.save(settingEntry);
    }
}
