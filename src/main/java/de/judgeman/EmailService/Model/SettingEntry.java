package de.judgeman.EmailService.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Created by Paul Richter on Mon 15/05/2023
 */
@Entity(name = "SETTING_ENTRIES")
public class SettingEntry {

    @Id
    @Column(nullable = false, name = "settingKey")
    private String key;

    @Column(nullable = false, name = "settingValue")
    private String value;

    public SettingEntry() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
