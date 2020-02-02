package jotbackend;

import org.springframework.data.repository.CrudRepository;

import jotbackend.Contact;

public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
