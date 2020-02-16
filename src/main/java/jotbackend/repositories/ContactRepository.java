package jotbackend.repositories;

import jotbackend.classes.Contact;
import jotbackend.classes.Activity;

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

    @Query("SELECT contact " +
            "FROM Contact contact " +
            "WHERE contact.userId = :userId " +
            "AND (contact.firstName LIKE %:searchVal% " +
            "OR contact.lastName LIKE %:searchVal% " +
            "OR CONCAT(contact.firstName, ' ', contact.lastName) LIKE %:searchVal%)")
    Page<Contact> searchContactsByName(@Param("userId") Integer userId,
                                       @Param("searchVal") String searchVal,
                                       Pageable pageable);

    @Query(value = "SELECT activity.createDate, activity.notes, Contact.firstName " +
            "FROM Activity activity INNER JOIN contacts_activities contact_activity on (activity.activityId = contact_activity.activityId) " +
            "INNER JOIN Contact contact on (contact.contactId = contact_activity.contactId) " +
            "WHERE activity.createDate=(SELECT MAX(activity.createDate) FROM activity) ", nativeQuery = true)
    String getRecentActivitiesForContact(@Param("contactId") Integer contactId);

}
