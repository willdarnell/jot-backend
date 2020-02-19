package jotbackend.repositories;

import jotbackend.classes.Attribute;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttributeRepository extends PagingAndSortingRepository<Attribute, Integer> {
    Page<Attribute> findByUserId(Integer userId, Pageable pageable);
    List<Attribute> findByTitle(String title);
}
