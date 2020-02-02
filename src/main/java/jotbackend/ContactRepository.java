package jotbackend;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jotbackend.Contact;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface ContactRepository extends CrudRepository<Contact, Integer> {
    @Query("SELECT item FROM Contact item WHERE item.id = :id AND item.userId = :userId")
    Optional<Contact> findByIdandUserId(@Param("id") Integer id, @Param("userId") Integer userId);
}
