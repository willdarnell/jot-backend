package jotbackend.repositories;

import jotbackend.classes.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByGid(String gid);
}
