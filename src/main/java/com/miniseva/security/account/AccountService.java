package com.miniseva.security.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    Account save(Account account);

    // Read

    Account findById(Long id);

    Account findByUsername(String username);

    Long getUserId(String username);

    List<Account> findByEnabled(boolean enabled);

    // Pagable list of enabled Accounts
    Page<Account> findByEnabled(boolean enabled, Pageable pageRequest);

    /**
     * Check if an account already exists.
     *
     * @param username Account's username.
     * @return true if Account exists, false if it does not exist.
     */
    boolean accountExists(String username);

    /**
     * Check if an customer already exists.
     *
     * @param customerId Customers's Id.
     * @return true if Customer exists, false if it does not exist.
     */
    boolean customerExists(Long customerId);

    long count();

    /**
     * Total number of enabled accounts;
     * @return the number of enabled accounts
     */
    long countEnabled();

    long countDisabled();

    // List<Account> getStudentsBasedonCollegeId(Long collegeId);

    // List<Account> getStudentsBasedonGroupIdAndCollegeId(Long groupId, Long collegeId);

    // Page<Account> getAllStudentsBasedOnCollegeId(Long collegeId, Pageable pageRequest);

    // Page<Account> getAllStudentsBasedOnGroupIdAndCollegeId(Long groupId, Long collegeId, Pageable pageRequest);

    // Page<Account> getAllStudentsBasedOnCollegeIdAndName(Long collegeId, String searchValue, Pageable pageRequest);

    // long countAllStudentsByCollegeId(Long collegeId);

    // long countAllStudentsByGroupIdAndCollegeId(Long groupId, Long collegeId);

    // long countAllStudentsByCollegeIdAndName(Long collegeId, String searchValue);

    long countByRoleRootFalse();

    Page<Account> getByRoleRootFalse(Pageable pageRequest);

    Page<Account> getByRoleRootFalseAndName(String searchValue, Pageable pageRequest);

    long countByRoleRootFalseAndName(String searchValue);    
}