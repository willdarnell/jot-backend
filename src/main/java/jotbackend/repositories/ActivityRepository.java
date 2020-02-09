package jotbackend.repositories;

import jotbackend.classes.Activity;
import org.springframework.data.repository.CrudRepository;

public interface ActivityRepository extends CrudRepository<Activity, Integer> {
}
