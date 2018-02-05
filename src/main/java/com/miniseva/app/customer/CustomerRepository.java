package com.miniseva.app.customer;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    Customer findById(Long id);

    @Query(value = "SELECT * FROM customer LEFT OUTER JOIN account ON account.customer_id = customer.id WHERE account.customer_id IS NULL",
    nativeQuery = true)
    List<Customer> getRemainingCustomers();

    Customer findByCardNo(String cardNo);

    Page<Customer> findAll(Pageable pageRequest);
    Page<Customer> findByNoContainingIgnoreCase(String searchValue, Pageable pageRequest);
    // Page<Customer> findByNoContainingIgnoreCaseAndLeadNameContainingIgnoreCase(String no, String leadName, Pageable pageRequest);
    // Page<Customer> findByNoContainingIgnoreCaseAndCustomerContainingIgnoreCase(String no, String customer, Pageable pageRequest);
    // Page<Customer> findByLeadNameContainingIgnoreCaseAndCustomerContainingIgnoreCase(String leadName, String customer, Pageable pageRequest);
    // Page<Customer> findByNoContainingIgnoreCaseAndLeadNameContainingIgnoreCaseAndCustomerContainingIgnoreCase(String no, String leadName, String customer, Pageable pageRequest);

    List<Customer> findAll();

    long countByNoContainingIgnoreCase(String searchValue);
    long countByCardNo(String cardNo);
    // long countByNoContainingIgnoreCaseAndLeadNameContainingIgnoreCase(String searchValue);
    // long countByNoContainingIgnoreCaseAndCustomerContainingIgnoreCase(String searchValue);
    // long countByLeadNameContainingIgnoreCaseAndCustomerContainingIgnoreCase(String searchValue);
    // long countByNoContainingIgnoreCaseAndLeadNameContainingIgnoreCaseAndCustomerContainingIgnoreCase(String no, String leadName);
}