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

    @Query("SELECT DISTINCT activityRecord " +
            "FROM Activity activityRecord " +
            "WHERE activityRecord.userId = :userId " +
            "AND activityRecord.type = :type")
    Page<Activity> getActivitiesByType(@Param("userId") Integer userId,
                                          String type,
                                          Pageable pageable);
}
