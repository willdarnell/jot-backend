package jotbackend.controllers;

import jotbackend.classes.Attribute;
import jotbackend.repositories.AttributeRepository;

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
@RequestMapping(path = "/attributes")
public class AttributeController {
    @Autowired
    private AttributeRepository attributeRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewContact (@RequestParam Integer userId,
                        @RequestParam String title,
                        @RequestParam String description) {
        Attribute newAttribute = new Attribute();
        newAttribute.setUserId(userId);
        newAttribute.setTitle(title);
        newAttribute.setDescription(description);
        attributeRepository.save(newAttribute);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Page<Attribute> getAllAttributesByUserId(@RequestParam Integer userId,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String sortField,
                                                              @RequestParam String sortDirection) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return attributeRepository.findByUserId(userId, pageable);
    }
/*
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
*/
}
