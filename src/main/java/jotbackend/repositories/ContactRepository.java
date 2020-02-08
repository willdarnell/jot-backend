package jotbackend.repositories;

import jotbackend.classes.Contact;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends PagingAndSortingRepository<Contact, Integer> {

    Page<Contact> findByUserId(Integer userId, Pageable pageable);

    @Query("SELECT DISTINCT contactRecord " +
            "FROM Contact contactRecord " +
            "JOIN contactRecord.attributes attribute " +
            "WHERE contactRecord.userId = :userId " +
            "AND attribute.attributeId IN :attributeIds")
    Page<Contact> getContactsByAttributes(@Param("userId") Integer userId,
                                          List<Integer> attributeIds,
                                          Pageable pageable);

}
