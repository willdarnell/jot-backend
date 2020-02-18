package jotbackend.services;
import jotbackend.classes.User;
import jotbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserByGid(String gid) {
        return userRepository.findByGid(gid);
    }

    public String addNewUser (String gid, String firstName, String lastName, String emailAddress, String phoneNumber) {
        User newUser = new User();
        newUser.setGid(gid);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(emailAddress);
        newUser.setPhoneNumber(phoneNumber);
        // newUser.setCreateTime(new Date(119, 6, 8));
        userRepository.save(newUser);
        return "Created";
    }
}
