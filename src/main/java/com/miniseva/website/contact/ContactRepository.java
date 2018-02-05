package com.miniseva.website.contact;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
    List<Contact> findByFullname(String fullname);
    Page<Contact> findByFullnameContainingIgnoreCase(String searchValue, Pageable pageRequest);

    long countByFullnameContainingIgnoreCase(String searchValue);
}