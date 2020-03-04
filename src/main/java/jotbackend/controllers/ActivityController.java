package jotbackend.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jotbackend.classes.Activity;
import jotbackend.classes.Contact;
import jotbackend.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(path = "/activities")
public class ActivityController {
    @Autowired
    private ActivityRepository activityRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewActivity (@RequestParam Integer userId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date completeDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDate,
                                                @RequestParam String status,
                                                @RequestParam String type,
                                                @RequestParam String notes ){
        Activity newActivity = new Activity();
        newActivity.setUserId(userId);
        newActivity.setCompleteDate(completeDate);
        newActivity.setDueDate(dueDate);
        newActivity.setStatus(status);
        newActivity.setType(type);
        newActivity.setNotes(notes);
        activityRepository.save(newActivity);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Page<Activity> getAllActivitiesByUserId(@RequestParam Integer userId,
                                                              @RequestParam Integer pageNum,
                                                              @RequestParam Integer pageSize,
                                                              @RequestParam String sortField,
                                                              @RequestParam String sortDirection) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return activityRepository.findByUserId(userId, pageable);
    }

    @GetMapping(path = "/byContactId")
    public @ResponseBody Page<Activity> getAllActivitiesByContactId(@RequestParam Integer contactId,
                                                                 @RequestParam Integer pageNum,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam String sortField,
                                                                 @RequestParam String sortDirection) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return activityRepository.findByContactId(contactId, pageable);
    }

    @DeleteMapping(path = "/delete/{activityId}")
    public @ResponseBody
    void deleteActivityById(@PathVariable Integer activityId) {
        activityRepository.deleteById(activityId);
    }

    @GetMapping(path = "/{activityId}")
    public @ResponseBody
    Optional<Activity> getActivityById(@PathVariable Integer activityId) {
        return activityRepository.findById(activityId);
    }


    @GetMapping(path = "/byType")
    public @ResponseBody Page<Activity> getActivitiesByType(@RequestParam Integer userId,
                                                               @RequestParam Integer pageNum,
                                                               @RequestParam Integer pageSize,
                                                               @RequestParam String sortField,
                                                               @RequestParam String sortDirection,
                                                               @RequestParam(value="type") String type) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return activityRepository.getActivitiesByType(userId, type, pageable);
    }

    @GetMapping(path = "/searchByNotes")
    public @ResponseBody Page<Activity> searchActivitiesByNotes(@RequestParam Integer userId,
                                                            @RequestParam Integer pageNum,
                                                            @RequestParam Integer pageSize,
                                                            @RequestParam String sortField,
                                                            @RequestParam String sortDirection,
                                                            @RequestParam String searchVal) {
        Pageable pageable = PageRequest.of(pageNum, pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortField));
        return activityRepository.searchActivitiesByNotes(userId, searchVal, pageable);
    }

    @PutMapping(path = "/update")
    public @ResponseBody String updateActivity (@RequestParam Integer activityId,
                                                @RequestParam Integer userId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date completeDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dueDate,
                                                @RequestParam String status,
                                                @RequestParam String type,
                                                @RequestParam String notes ){
        Activity updatedActivity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException());
        updatedActivity.setUserId(userId);
        updatedActivity.setCompleteDate(completeDate);
        updatedActivity.setDueDate(dueDate);
        updatedActivity.setStatus(status);
        updatedActivity.setType(type);
        updatedActivity.setNotes(notes);
        activityRepository.save(updatedActivity);
        return "Saved";
    }
}
