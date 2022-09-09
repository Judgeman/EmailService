package de.judgeman.EmailService.Services;

import de.judgeman.EmailService.Enums.Permission;
import de.judgeman.EmailService.Exceptions.UserNotFoundException;
import de.judgeman.EmailService.Exceptions.UsernameAlreadyExistsException;
import de.judgeman.EmailService.Model.AuthenticatedUser;
import de.judgeman.EmailService.Model.User;
import de.judgeman.EmailService.Model.UserChangeRequestObject;
import de.judgeman.EmailService.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Value("${defaultAdminUser.username}")
    private String defaultAdminUsername;

    @Value("${defaultAdminUser.password}")
    private String defaultAdminPassword;

    public void init() throws UsernameAlreadyExistsException {
        if (!existingAnyUser()) {
            createDefaultAdminUser();
        }
    }

    private User createDefaultAdminUser() throws UsernameAlreadyExistsException {
        return createUser(defaultAdminUsername, defaultAdminPassword, Permission.ADMIN_AREA);
    }

    private User createUser(String username, String password, Permission permission) throws UsernameAlreadyExistsException {
        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(permission);

        return createUser(username, password, permissions);
    }

    private User createUser(String username, String password, List<Permission> permissions) throws UsernameAlreadyExistsException {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException(username + " already exists!");
        }

        User adminUser = new User();
        adminUser.setUsername(username);
        adminUser.setPassword(encodePassword(password));
        adminUser.setPermissions(permissions);

        return userRepository.save(adminUser);
    }

    private boolean existingAnyUser() {
        return userRepository.findFirstByOrderByUsernameAsc() != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user with name " + username + " found");
        }

        return new AuthenticatedUser(user);
    }

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof AuthenticatedUser) {
            return (AuthenticatedUser) authentication.getPrincipal();
        }

        return null;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public ArrayList<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User createNewUser(String username) throws UsernameAlreadyExistsException {
        if (userRepository.findByUsername(username) != null) {
            throw new UsernameAlreadyExistsException("Username" + username + " already exists!");
        }

        return createUser(username, defaultAdminPassword, Permission.ADMIN_AREA);
    }

    public void updateUser(UserChangeRequestObject userChangeRequestObject) throws UserNotFoundException {
        User user = userRepository.findByUsername(userChangeRequestObject.getUsername());

        if (user == null) {
            throw new UserNotFoundException("User with username " + userChangeRequestObject.getUsername() + " not found!");
        }

        user.setPassword(encodePassword(userChangeRequestObject.getNewPassword()));
        userRepository.save(user);
    }

    public String encodePassword(String password) {
        return getPasswordEncoder().encode(password);
    }
}
