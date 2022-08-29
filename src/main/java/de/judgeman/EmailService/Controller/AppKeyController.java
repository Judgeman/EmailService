package de.judgeman.EmailService.Controller;

import de.judgeman.EmailService.Model.AppKey;
import de.judgeman.EmailService.Services.AppKeyService;
import jakarta.transaction.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.openmbean.KeyAlreadyExistsException;

@Controller
public class AppKeyController {

    @Value("${simpleCreatingNewAppKeysViaRequestAllowed}")
    private boolean isCreatingNewAppKeysAllowed = false;

    @Autowired
    private AppKeyService appKeyService;

    @PutMapping("/appKey/{appId}")
    public @ResponseBody AppKey createAppKey(@PathVariable String appId) throws NotSupportedException, KeyAlreadyExistsException {

        if (!isCreatingNewAppKeysAllowed) {
            throw new NotSupportedException();
        }

        return appKeyService.createNewAppKey(appId);
    }
}
