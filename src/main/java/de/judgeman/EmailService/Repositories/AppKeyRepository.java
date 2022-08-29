package de.judgeman.EmailService.Repositories;

import de.judgeman.EmailService.Model.AppKey;
import org.springframework.data.repository.CrudRepository;

public interface AppKeyRepository extends CrudRepository<AppKey, String> {
    AppKey findByAppId(String appId);
}
