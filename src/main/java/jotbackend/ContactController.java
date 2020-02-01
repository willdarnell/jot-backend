package jotbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
}
