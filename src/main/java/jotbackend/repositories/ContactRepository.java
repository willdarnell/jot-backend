package jotbackend.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import jotbackend.classes.Contact;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface ContactRepository extends PagingAndSortingRepository<Contact, Integer> {

    Page<Contact> findByUserId(Integer userId, Pageable pageable);

    @Query("SELECT item FROM Contact item WHERE item.id = :id AND item.userId = :userId")
    Optional<Contact> findByIdandUserId(@Param("id") Integer id, @Param("userId") Integer userId);
}
