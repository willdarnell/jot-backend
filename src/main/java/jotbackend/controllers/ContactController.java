package jotbackend.controllers;

import jotbackend.classes.*;
import jotbackend.repositories.AttributeRepository;
import jotbackend.repositories.ContactRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jotbackend.services.UserService;
import jotbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(path = "/contacts")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    private void addAttributeToContact(Integer contactId, Integer attributeId) {
        Optional<Contact> findContactResult = contactRepository.findById(contactId);
        if (!findContactResult.isPresent()) {
            return;
        }
        Contact contact = findContactResult.get();

        Optional<Attribute> findAttributeResult = attributeRepository.findById(attributeId);
        if (!findAttributeResult.isPresent()) {
            return;
        }
        Attribute attribute = findAttributeResult.get();
        contact.getAttributes().add(attribute);
        contactRepository.save(contact);
    }

    private void addAttributeToContact(Integer contactId, String attributeTitle) {
        Optional<Contact> findContactResult = contactRepository.findById(contactId);
        if (!findContactResult.isPresent()) {
            return;
        }
        Contact contact = findContactResult.get();

        List<Attribute> findAttributeResult = attributeRepository.findByTitle(attributeTitle);
        if (findAttributeResult.size() == 0) {
            return;
        }
        Attribute attribute = findAttributeResult.get(0);
        contact.getAttributes().add(attribute);
        contactRepository.save(contact);
    }

    @PostMapping(path = "/add")
    public ResponseEntity addNewContact (@RequestParam String googleId, @RequestParam String firstName,
                                               @RequestParam String lastName, @RequestParam String emailAddress, @RequestParam String phoneNumber,
                                               @RequestParam String organization, @RequestParam String role,
                                               @RequestParam(value="attributeTitle", required = false) List<String> attributeTitles) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Contact newContact = new Contact(userId, googleId, firstName, lastName, emailAddress, phoneNumber, organization, role);
        newContact = contactRepository.save(newContact);
        Integer newId = newContact.getContactId();
        if (attributeTitles != null) {
            for (String attribute : attributeTitles) {
                addAttributeToContact(newId, attribute);
            }
        }
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity getAllContactsByUserId(@RequestHeader("authorization") String token,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String sortField,
                                                              @RequestParam String sortDirection) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return new ResponseEntity<>(contactRepository.findByUserId(userId, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/names")
    //public @ResponseBody List<ContactIdAndName> getAllIdAndNameByUserId(@RequestParam Integer userId) {
     public ResponseEntity getAllIdAndNameByUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();
        return new ResponseEntity<>(contactRepository.findByUserId(userId), HttpStatus.OK);
    }

    @GetMapping(path = "/byAttributes")
    public ResponseEntity getContactsByAttributes(@RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String sortField,
                                                              @RequestParam String sortDirection,
                                                              @RequestParam(value="attributeId") List<Integer> attributeIds) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return new ResponseEntity<>(contactRepository.getContactsByAttributes(userId, attributeIds, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/searchByName")
    public ResponseEntity searchContactsByName(@RequestParam Integer pageNum,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam String sortField,
                                                               @RequestParam String sortDirection,
                                                               @RequestParam String searchVal) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return new ResponseEntity<>(contactRepository.searchContactsByName(userId, searchVal, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{contactId}")
    public ResponseEntity getContactById(@PathVariable Integer contactId) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Contact> contact = contactRepository.findById(contactId);

        if (!contact.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (contact.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{contactId}")
    public ResponseEntity deleteContactById(@PathVariable Integer contactId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Contact> contact = contactRepository.findById(contactId);

        if (!contact.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (contact.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        contactRepository.deleteById(contactId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "update/{contactId}")
    public ResponseEntity updateContact(@PathVariable Integer contactId, @RequestParam String googleId, @RequestParam String firstName,
                                 @RequestParam String lastName, @RequestParam String emailAddress, @RequestParam String phoneNumber,
                                 @RequestParam String organization, @RequestParam String role,
                                 @RequestParam(required = false, value="attributeTitle") List<String> attributeTitles){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Contact> contact = contactRepository.findById(contactId);
        if (!contact.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (contact.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); }

        Date date = new Date();
        Contact updatedContact = contact.get();
        updatedContact.setUpdateDate(date);
        updatedContact.setGoogleId(googleId);
        updatedContact.setFirstName(firstName);
        updatedContact.setLastName(lastName);
        updatedContact.setEmailAddress(emailAddress);
        updatedContact.setPhoneNumber(phoneNumber);
        updatedContact.setOrganization(organization);
        updatedContact.setRole(role);
        updatedContact.getAttributes().clear();
        contactRepository.save(updatedContact);
        if (attributeTitles != null) {
            for (String attribute : attributeTitles) {
                addAttributeToContact(contactId, attribute);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
