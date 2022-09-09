package de.judgeman.EmailService.Repositories;

import de.judgeman.EmailService.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
    User findFirstByOrderByUsernameAsc();
    ArrayList<User> findAll();
}
