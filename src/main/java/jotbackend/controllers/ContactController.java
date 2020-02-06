package jotbackend.controllers;

import jotbackend.repositories.ContactRepository;
import jotbackend.classes.Contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public @ResponseBody Page<Contact> getAllContactsByUserId(@RequestParam Integer userId,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String sortField,
                                                              @RequestParam String sortDirection) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return contactRepository.findByUserId(userId, pageable);
    }

    @GetMapping(path = "/{contactId}")
    public @ResponseBody
    Optional<Contact> getContactById(@PathVariable Integer contactId) {
        return contactRepository.findById(contactId);
    }

    // AB: I think this isn't necessary (based on Slack discussion) but leaving it in as I'll
    // need the query as an example for the getContactsByAttribute method
    @GetMapping(path = "/{contactId}/{userId}")
    public @ResponseBody
    Optional<Contact> findByIdandUserId(@PathVariable Integer contactId, @PathVariable Integer userId){
        return contactRepository.findByIdandUserId(contactId, userId);
    }

    @GetMapping(path = "/delete/{contactId}")
    public @ResponseBody
    void deleteContactById(@PathVariable Integer contactId) {
        contactRepository.deleteById(contactId);
    }

}
