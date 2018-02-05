package com.miniseva.app.product;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Product findById(Long id);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageRequest);
    Page<Product> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);
    
    long count();
    long countByNameContainingIgnoreCase(String searchValue);
}