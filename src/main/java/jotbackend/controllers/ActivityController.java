package jotbackend.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jotbackend.classes.Activity;
import jotbackend.repositories.ActivityRepository;

@Controller
@RequestMapping(path = "/activities")
public class ActivityController {
    @Autowired
    private ActivityRepository activityRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewActivity (@RequestParam Integer userId, @RequestParam Date completeDate, @RequestParam Date dueDate,
                                                @RequestParam String status, @RequestParam String type, @RequestParam String notes ){
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
    public @ResponseBody Iterable<Activity> getAllActivities() { return activityRepository.findAll(); }

}
