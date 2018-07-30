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

    Page<Schedule> findByCreatedByNotNull(Pageable pageRequest);
    // Page<Schedule> findByNameContainingIgnoreCase(String searchValue, Pageable pageRequest);
    Page<Schedule> findByCreatedByNotNullAndDates(String dates, Pageable pageRequest);
    Page<Schedule> findByCreatedBy(Long createdById, Pageable pageRequest);

    // @Query(value = "SELECT * FROM SCHEDULE WHERE dates LIKE ?1 \n-- #pageable\n",
    // nativeQuery = true)
    // Page<Schedule> getAllSchedulesByDate(String searchValue, Pageable pageRequest);

    Page<Schedule> findByCreatedByNotNullAndDatesLike(String dates, Pageable pageRequest);
        
    long countByCreatedByNotNull();
    // long countByNameContainingIgnoreCase(String searchValue);
    long countByCreatedByNotNullAndDates(String dates);

    // @Query(value = "SELECT COUNT(*) FROM SCHEDULE WHERE dates LIKE ?1",
    // nativeQuery = true)
    // long countAllSchedulesByDate(String searchValue);

    Page<Schedule> findByOrderIdAndCreatedByNotNull(Long orderId, Pageable pageRequest);
    Page<Schedule> findByOrderIdAndCreatedBy(Long orderId, Long createdById, Pageable pageRequest);
    List<Schedule> findByOrderId(Long orderId);
    long countByOrderIdAndCreatedByNotNull(Long orderId);
    long countByOrderIdAndCreatedBy(Long orderId, Long createdById);
    long countByCreatedByNotNullAndDatesLike(String dates);
    long countByCreatedBy(Long createdById);
}