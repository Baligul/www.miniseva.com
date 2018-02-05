package com.miniseva.app.admin.orders;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrdersRepository extends PagingAndSortingRepository<Orders, Long> {   

    Orders findById(Long id);

    Page<Orders> findAll();
    
    long count();
}