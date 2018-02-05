package com.miniseva.app.item;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    Item findById(Long id);

    List<Item> findByProductId(Long productId);

    Page<Item> findAll(Pageable pageRequest);
    Page<Item> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);
    
    long count();
    long countByNameContainingIgnoreCase(String searchValue);
}