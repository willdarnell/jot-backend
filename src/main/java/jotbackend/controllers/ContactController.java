package jotbackend.controllers;

import jotbackend.classes.Activity;
import jotbackend.classes.Attribute;
import jotbackend.repositories.AttributeRepository;
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

    private void removeAttributeFromContact(Integer contactId, Integer attributeId) {
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

        contact.getAttributes().remove(attribute);

        contactRepository.save(contact);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewContact (@RequestParam Integer userId, @RequestParam String googleId, @RequestParam String firstName,
                                               @RequestParam String lastName, @RequestParam String emailAddress, @RequestParam String phoneNumber,
                                               @RequestParam String organization, @RequestParam String role,
                                               @RequestParam(value="attributeId") List<Integer> attributeIds) {
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
        newContact = contactRepository.save(newContact);
        Integer newId = newContact.getContactId();
        // Add any attributes to newly created contact
        for (Integer attribute : attributeIds) {
            addAttributeToContact(newId, attribute);
        }
        return "Saved";
    }

    @PostMapping(path = "/addAttribute")
    public @ResponseBody String addAttribute(@RequestParam Integer contactId,
                                             @RequestParam Integer attributeId) {

        addAttributeToContact(contactId, attributeId);

        return "Added";
    }

    @PostMapping(path = "/removeAttribute")
    public @ResponseBody String removeAttribute(@RequestParam Integer contactId,
                                                @RequestParam Integer attributeId) {

        removeAttributeFromContact(contactId, attributeId);

        return "Removed";
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
                                              @RequestParam String organization, @RequestParam String role,
                                              @RequestParam(value="attributeId") List<Integer> attributeIds){
        Contact updatedContact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException());
        updatedContact.setGoogleId(googleId);
        updatedContact.setFirstName(firstName);
        updatedContact.setLastName(lastName);
        updatedContact.setEmailAddress(emailAddress);
        updatedContact.setPhoneNumber(phoneNumber);
        updatedContact.setOrganization(organization);
        updatedContact.setRole(role);
        // Remove any attributes
        updatedContact.getAttributes().clear();
        Contact savedContact = contactRepository.save(updatedContact);
        // Re-add new list of attributes
        for (Integer attribute : attributeIds) {
            addAttributeToContact(contactId, attribute);
        }
        return "Saved";

    }

    @GetMapping(path = "/getRecentActivities/{contactId}")
    public @ResponseBody
    List<Activity> getRecentActivitiesByContact(@PathVariable Integer contactId){
        List<Activity> list = contactRepository.getRecentActivitiesByContact(contactId);
        return list;
    }

    @GetMapping(path = "getMostRecentActivity/{contactId}")
    public @ResponseBody
    Activity getMostRecentActivity(@PathVariable Integer contactId){
        List<Activity> list = contactRepository.getRecentActivitiesByContact(contactId);
        return list.get(list.size() - 1);
    }

}
