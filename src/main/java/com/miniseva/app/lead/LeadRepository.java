package com.miniseva.app.lead;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

public interface LeadRepository extends PagingAndSortingRepository<Lead, Long> {
    Lead findById(Long id);

    Page<Lead> findAll(Pageable pageRequest);
    Page<Lead> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);

    List<Lead> findAll();

    long countByNameContainingIgnoreCase(String searchValue);
}