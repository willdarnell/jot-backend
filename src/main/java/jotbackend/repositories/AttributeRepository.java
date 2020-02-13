package jotbackend.repositories;

import jotbackend.classes.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttributeRepository extends PagingAndSortingRepository<Attribute, Integer> {
    Page<Attribute> findByUserId(Integer userId, Pageable pageable);
}
