package de.judgeman.EmailService.Repositories;

import de.judgeman.EmailService.Model.SettingEntry;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Paul Richter on Mon 15/05/2023
 */
public interface SettingsRepository extends CrudRepository<SettingEntry, String> {

}
