package jotbackend.controllers;

import jotbackend.classes.JotUserDetails;
import jotbackend.classes.User;
import jotbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
/*
    // Admin functionality

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
 */

    @GetMapping(path = "/me")
    public ResponseEntity getUserById() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(user.get(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/me")
    public ResponseEntity deleteUserById() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            userRepository.deleteById(userId);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/me")
    ResponseEntity updateUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String emailAddress,
                    @RequestParam String phoneNumber) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User updatedUser = user.get();

        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setEmailAddress(emailAddress);
        updatedUser.setPhoneNumber(phoneNumber);
        User savedUser = userRepository.save(updatedUser);

        return new ResponseEntity(savedUser, HttpStatus.OK);
    }

}


