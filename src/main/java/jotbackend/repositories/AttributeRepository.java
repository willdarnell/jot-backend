package jotbackend.repositories;

import jotbackend.classes.Attribute;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttributeRepository extends PagingAndSortingRepository<Attribute, Integer> {
}
