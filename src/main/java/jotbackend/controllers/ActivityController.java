package jotbackend.controllers;

import java.util.Date;
import java.util.Optional;
import jotbackend.classes.Activity;
import jotbackend.classes.Contact;
import jotbackend.classes.JotUserDetails;
import jotbackend.repositories.ActivityRepository;
import jotbackend.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(path = "/activities")
public class ActivityController {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @PostMapping(path = "/add")
    public ResponseEntity addNewActivity (@RequestParam Integer contactId,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date completeDate,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDate,
                                                @RequestParam String status,
                                                @RequestParam String type,
                                                @RequestParam String notes ){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Activity newActivity = new Activity();
        newActivity.setUserId(userId);

        Optional<Contact> findContactResult = contactRepository.findById(contactId);
        if (!findContactResult.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Contact contact = findContactResult.get();
        if (contact.getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        newActivity.setContact(contact);
        if (completeDate != null) {
            newActivity.setCompleteDate(completeDate);
        }
        if (dueDate != null) {
            newActivity.setDueDate(dueDate);
        }
        newActivity.setStatus(status);
        newActivity.setType(type);
        newActivity.setNotes(notes);
        activityRepository.save(newActivity);
        return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity getAllActivitiesByUserId(@RequestParam Integer pageNum,
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
        return new ResponseEntity<>(activityRepository.findByUserId(userId, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/byContactId")
    public ResponseEntity getAllActivitiesByContactId(@RequestParam Integer contactId,
                                                                 @RequestParam Integer pageNum,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam String sortField,
                                                                 @RequestParam String sortDirection) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Contact> contact = contactRepository.findById(contactId);

        if (!contact.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (contact.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return new ResponseEntity(activityRepository.findByContactId(contactId, pageable), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{activityId}")
    public ResponseEntity deleteActivityById(@PathVariable Integer activityId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (!activity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (activity.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/{activityId}")
    public ResponseEntity getActivityById(@PathVariable Integer activityId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();
        Optional<Activity> activity = activityRepository.findById(activityId);
        if (!activity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (activity.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(activityRepository.findById(activityId), HttpStatus.OK);
    }


    @GetMapping(path = "/byType")
    public ResponseEntity getActivitiesByType(@RequestParam Integer pageNum,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam String sortField,
                                                               @RequestParam String sortDirection,
                                                               @RequestParam(value="type") String type) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return new ResponseEntity(activityRepository.getActivitiesByType(userId, type, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/searchByNotes")
    public ResponseEntity searchActivitiesByNotes(@RequestParam Integer pageNum,
                                                            @RequestParam Integer pageSize,
                                                            @RequestParam String sortField,
                                                            @RequestParam String sortDirection,
                                                            @RequestParam String searchVal) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return new ResponseEntity(activityRepository.searchActivitiesByNotes(userId, searchVal, pageable), HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity updateActivity (@RequestParam Integer activityId,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date completeDate,
                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDate,
                                          @RequestParam String status,
                                          @RequestParam String type,
                                          @RequestParam String notes ){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        };
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Activity> activity = activityRepository.findById(activityId);
        if (!activity.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (activity.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); }

        Activity updatedActivity = activity.get();
        updatedActivity.setCompleteDate(completeDate);
        updatedActivity.setDueDate(dueDate);
        updatedActivity.setStatus(status);
        updatedActivity.setType(type);
        updatedActivity.setNotes(notes);
        activityRepository.save(updatedActivity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
