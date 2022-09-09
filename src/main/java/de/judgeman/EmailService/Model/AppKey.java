package de.judgeman.EmailService.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class AppKey {

    @Id
    @NotEmpty
    private String appId;

    @NotEmpty
    private String keyValue;

    private String specificSenderEmailAddress;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getSpecificSenderEmailAddress() {
        return specificSenderEmailAddress;
    }

    public void setSpecificSenderEmailAddress(String specificSenderEmailAddress) {
        this.specificSenderEmailAddress = specificSenderEmailAddress;
    }
}
