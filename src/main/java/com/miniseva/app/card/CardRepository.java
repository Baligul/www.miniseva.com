package com.miniseva.app.card;

import java.util.List;

import org.joda.time.DateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends PagingAndSortingRepository<Card, Long> {

    // @Query(value = "SELECT * FROM card LEFT OUTER JOIN account ON account.card_id = card.id WHERE account.card_id IS NULL",
    // nativeQuery = true)
    // List<Card> getRemainingCards();

    @Query(value = "SELECT * FROM card LEFT OUTER JOIN customer ON customer.card_no = card.card_no WHERE customer.card_no IS NULL",
    nativeQuery = true)
    List<Card> getRemainingCardsForCustomer();

    Card findById(Long id);

    Card findByCardNo(String cardNo);

    Page<Card> findAll(Pageable pageRequest);
    Page<Card> findByCardNoContainingIgnoreCase(String searchValue, Pageable pageRequest);
    
    long count();
    long countByCardNoContainingIgnoreCase(String searchValue);
}