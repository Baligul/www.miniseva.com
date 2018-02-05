package com.miniseva.app.block;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

public interface BlockRepository extends PagingAndSortingRepository<Block, Long> {
    Block findById(Long id);

    Page<Block> findAll(Pageable pageRequest);
    Page<Block> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);

    List<Block> findAll();
    List<Block> findByLeadId(Long leadId);
    long deleteByLeadId(Long leadId);

    long countByNameContainingIgnoreCase(String searchValue);
}