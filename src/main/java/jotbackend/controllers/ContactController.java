package jotbackend.controllers;

import jotbackend.classes.Contact;
import jotbackend.repositories.ContactRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(allowedHeaders = "*")
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

    @GetMapping(path = "/byAttributes")
    public @ResponseBody Page<Contact> getContactsByAttributes(@RequestParam Integer userId,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String sortField,
                                                              @RequestParam String sortDirection,
                                                              @RequestParam(value="attributeId") List<Integer> attributeIds) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return contactRepository.getContactsByAttributes(userId, attributeIds, pageable);
    }

    @GetMapping(path = "/searchByName")
    public @ResponseBody Page<Contact> searchContactsByName(@RequestParam Integer userId,
                                                               @RequestParam Integer pageNum,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam String sortField,
                                                               @RequestParam String sortDirection,
                                                               @RequestParam String searchVal) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return contactRepository.searchContactsByName(userId, searchVal, pageable);
    }

    @GetMapping(path = "/{contactId}")
    public @ResponseBody
    Optional<Contact> getContactById(@PathVariable Integer contactId) {
        return contactRepository.findById(contactId);
    }

    @PutMapping(path = "/delete/{contactId}")
    public @ResponseBody
    void deleteContactById(@PathVariable Integer contactId) {
        contactRepository.deleteById(contactId);
    }

    @PutMapping(path = "/update/{contactId}")
    public @ResponseBody String updateContact(@PathVariable Integer contactId, @RequestParam String googleId, @RequestParam String firstName,
                                       @RequestParam String lastName, @RequestParam String emailAddress, @RequestParam String phoneNumber,
                                       @RequestParam String organization, @RequestParam String role){
        Contact updatedContact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException());
        updatedContact.setGoogleId(googleId);
        updatedContact.setFirstName(firstName);
        updatedContact.setLastName(lastName);
        updatedContact.setEmailAddress(emailAddress);
        updatedContact.setPhoneNumber(phoneNumber);
        updatedContact.setOrganization(organization);
        updatedContact.setRole(role);
        Contact savedContact = contactRepository.save(updatedContact);
        return "Saved";

    }

}
