package jotbackend.repositories;

import jotbackend.classes.Activity;
import jotbackend.classes.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends PagingAndSortingRepository<Activity, Integer> {

    Page<Activity> findByUserId(Integer userId, Pageable pageable);

    @Query("SELECT DISTINCT activityRecord " +
            "FROM Activity activityRecord " +
            "WHERE activityRecord.userId = :userId " +
            "AND activityRecord.type = :type")
    Page<Activity> getActivitiesByType(@Param("userId") Integer userId,
                                          String type,
                                          Pageable pageable);

    @Query("SELECT activity " +
            "FROM Activity activity " +
            "WHERE activity.userId = :userId " +
            "AND (activity.notes LIKE %:searchVal%) ")
    Page<Activity> searchActivitiesByNotes(@Param("userId") Integer userId,
                                       @Param("searchVal") String searchVal,
                                       Pageable pageable);

    @Query("SELECT activity " +
            "FROM Activity activity " +
            "WHERE activity.contact.contactId = :contactId" )
    Page<Activity> findByContactId(@Param("contactId") Integer contactId, Pageable pageable);
}
