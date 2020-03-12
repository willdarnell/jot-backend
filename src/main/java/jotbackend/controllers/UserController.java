package jotbackend.controllers;

import jotbackend.classes.User;
import jotbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewUser (@RequestParam String firstName, @RequestParam String lastName, @RequestParam String emailAddress,
                                            @RequestParam String phoneNumber) {
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(emailAddress);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setCreateTime(new Date(119, 6, 8));
        userRepository.save(newUser);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<User> getAllUsers() { return userRepository.findAll();}

    @GetMapping(path = "/{userId}")
    public @ResponseBody
    Optional<User> getUserById(@PathVariable Integer userId) { return userRepository.findById(userId);}

    @DeleteMapping(path = "/{userId}")
    public @ResponseBody
    void deleteUserById(@PathVariable Integer userId) {

        userRepository.deleteById(userId);
    }

    @PutMapping(path = "update/{userId}")
    void updateUser(@PathVariable Integer userId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String emailAddress,
                    @RequestParam String phoneNumber) {
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException());
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setEmailAddress(emailAddress);
        updatedUser.setPhoneNumber(phoneNumber);
        User savedUser = userRepository.save(updatedUser);

    }

}


