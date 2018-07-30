package com.miniseva.app.schedule;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Long> {
    Schedule findById(Long id);

    // List<Schedule> findByProductId(Long productId);

    Page<Schedule> findAll(Pageable pageRequest);
    // Page<Schedule> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);
    Page<Schedule> findByDates(String dates, Pageable pageRequest);

    // @Query(value = "SELECT * FROM SCHEDULE WHERE dates LIKE ?1 \n-- #pageable\n",
    // nativeQuery = true)
    // Page<Schedule> getAllSchedulesByDate(String searchValue, Pageable pageRequest);

    Page<Schedule> findByDatesIlike(String dates, Pageable pageRequest);
        
    long count();
    // long countByNameContainingIgnoreCase(String searchValue);
    long countByDates(String dates);

    // @Query(value = "SELECT COUNT(*) FROM SCHEDULE WHERE dates LIKE ?1",
    // nativeQuery = true)
    // long countAllSchedulesByDate(String searchValue);

    Page<Schedule> findByOrderId(Long orderId, Pageable pageRequest);
    List<Schedule> findByOrderId(Long orderId);
    long countByOrderId(Long orderId);
    long countByDatesIlike(String dates);
}