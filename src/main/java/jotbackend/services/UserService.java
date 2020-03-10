package jotbackend.services;
import jotbackend.classes.User;
import jotbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserByGid(String gid) {
        return userRepository.findByGid(gid);
    }

    public Optional<User> getUserByEmailAddress(String email) { return userRepository.findByEmailAddress(email); }

    public User addNewUser (String gid, String firstName, String lastName, String emailAddress, String phoneNumber) {
        User newUser = new User();
        newUser.setGid(gid);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(emailAddress);
        newUser.setPhoneNumber(phoneNumber);
        User saved = userRepository.save(newUser);
        return saved;
    }
}
