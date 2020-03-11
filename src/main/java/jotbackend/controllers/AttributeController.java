package jotbackend.controllers;

import jotbackend.classes.Attribute;
import jotbackend.classes.JotUserDetails;
import jotbackend.repositories.AttributeRepository;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(allowedHeaders = "*")
@RequestMapping(path = "/attributes")
public class AttributeController {
    @Autowired
    private AttributeRepository attributeRepository;

    @PostMapping(path = "/add")
    public ResponseEntity addNewContact (@RequestParam String title,
                        @RequestParam String description) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Attribute newAttribute = new Attribute();
        newAttribute.setUserId(userId);
        newAttribute.setTitle(title);
        newAttribute.setDescription(description);
        return new ResponseEntity(attributeRepository.save(newAttribute), HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity getAllAttributesByUserId(@RequestParam Integer pageNum,
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
        return new ResponseEntity(attributeRepository.findByUserId(userId, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{attributeId}")
    public ResponseEntity getAttributeById(@PathVariable Integer attributeId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Attribute> attribute = attributeRepository.findById(attributeId);

        if (!attribute.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (attribute.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity(attribute, HttpStatus.OK);
    }

    @PutMapping(path = "/update/{attributeId}")
    public ResponseEntity updateAttribute(@PathVariable Integer attributeId,
                                              @RequestParam String title,
                                              @RequestParam String description){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Attribute> attribute = attributeRepository.findById(attributeId);

        if (!attribute.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (attribute.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Attribute updatedAttribute = attribute.get();
        updatedAttribute.setUserId(userId);
        updatedAttribute.setTitle(title);
        updatedAttribute.setDescription(description);
        attributeRepository.save(updatedAttribute);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{attributeId}")
    public ResponseEntity deleteAttributeById(@PathVariable Integer attributeId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof JotUserDetails)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Integer userId = ((JotUserDetails)principal).getId();

        Optional<Attribute> attribute = attributeRepository.findById(attributeId);

        if (!attribute.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if (attribute.get().getUserId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        attributeRepository.deleteById(attributeId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
