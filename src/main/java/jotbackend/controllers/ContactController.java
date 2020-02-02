package jotbackend.controllers;

import jotbackend.repositories.ContactRepository;
import jotbackend.classes.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping(path = "/contacts")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewContact (@RequestParam Integer userId, @RequestParam String googleId, @RequestParam String firstName,
                                               @RequestParam String lastName, @RequestParam String emailAddress, @RequestParam String phoneNumber,
                                               @RequestParam String organization, @RequestParam String role) {
        Contact newContact = new Contact();
        newContact.setUserId(userId);
        newContact.setGoogleId(googleId);
        newContact.setFirstName(firstName);
        newContact.setLastName(lastName);
        newContact.setEmailAddress(emailAddress);
        newContact.setPhoneNumber(phoneNumber);
        newContact.setOrganization(organization);
        newContact.setRole(role);
        newContact.setCreateTime(new Date(119,6,8));
        newContact.setUpdateDate(new Date(119,6,8));
        contactRepository.save(newContact);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Optional<Contact> getContactById(@PathVariable Integer id) {
        return contactRepository.findById(id);
    }

    @GetMapping(path = "/{id}/{userId}")
    public @ResponseBody
    Optional<Contact> findByIdandUserId(@PathVariable Integer id, @PathVariable Integer userId){
        return contactRepository.findByIdandUserId(id, userId);
    }

}
