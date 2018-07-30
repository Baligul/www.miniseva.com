package com.miniseva.app.orders;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdersRepository extends PagingAndSortingRepository<Orders, Long> {
    Orders findById(Long id);

    // List<Orders> findByProductId(Long productId);

    Page<Orders> findByCreatedByNotNull(Pageable pageRequest);
    Page<Orders> findByCreatedBy(Long createdBy, Pageable pageRequest);
    // Page<Orders> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);
    // Page<Orders> findByDates(String dates, Pageable pageRequest);
    
    long countByCreatedByNotNull();
    // long countByNameContainingIgnoreCase(String searchValue);
    // long countByDates(String dates);
    long countByCreatedBy(Long createdBy);
}