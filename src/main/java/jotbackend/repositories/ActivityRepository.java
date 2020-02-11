package jotbackend.repositories;

import jotbackend.classes.Activity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActivityRepository extends PagingAndSortingRepository<Activity, Integer> {
}
