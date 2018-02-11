package com.miniseva.app.slot;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;

public interface SlotRepository extends PagingAndSortingRepository<Slot, Long> {

    @Query(value = "SELECT * FROM slot WHERE end_time >= current_time",
    nativeQuery = true)
    List<Slot> getTodaysSlots();

    Slot findById(Long id);

    List<Slot> findAll();

    Page<Slot> findAll(Pageable pageRequest);
    Page<Slot> findBySlotValContainingIgnoreCase(String searchValue, Pageable pageRequest);
    
    long count();
    long countBySlotValContainingIgnoreCase(String searchValue);
}