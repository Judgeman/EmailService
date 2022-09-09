package de.judgeman.EmailService.Repositories;

import de.judgeman.EmailService.Model.AppKey;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface AppKeyRepository extends CrudRepository<AppKey, String> {
    AppKey findByAppId(String appId);
    ArrayList<AppKey> findAll();
}
