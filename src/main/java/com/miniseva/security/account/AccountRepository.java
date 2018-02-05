package com.miniseva.security.account;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Account findByUsername(String username);

    @Query("SELECT id FROM Account WHERE username=?1")
    Long findIdByUsername(String username);

    int countByUsername(String username);
    int countByCustomerId(Long customerId);

    @Query("SELECT COUNT(username) FROM Account WHERE username=?1")
    int accountExists(String username);

    @Query("SELECT COUNT(customerId) FROM Account WHERE customerId=?1")
    int customerExists(Long customerId);

    // Email focused queries (i.e. use email as username
    // =====================
    //Account findByEmail(String email);
    int countByEmail(String email);
    //    @Query("SELECT COUNT(email) FROM Account WHERE email=?1")
    //    int accountExists(String email);

    Page<Account> findByEnabled(boolean enabled, Pageable pageRequest);

    List<Account> findByEnabled(boolean enabled);

    long countByEnabled(boolean enabled);

    // @Query(value = "SELECT * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.college_id = ?1 AND arm.role_id = 3 ORDER BY a.first_name",
    // nativeQuery = true)
    // List<Account> getStudentsBasedonCollegeId(Long collegeId);

    // @Query(value = "SELECT * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.group_id = ?1 AND a.college_id = ?2 AND arm.role_id = 3 ORDER BY a.first_name",
    // nativeQuery = true)
    // List<Account> getStudentsBasedonGroupIdAndCollegeId(Long groupId, Long collegeId);

    // @Query(value = "SELECT * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.college_id = ?1 AND arm.role_id = 3 \n-- #pageable\n",
    // nativeQuery = true)
    // Page<Account> getAllStudentsBasedOnCollegeId(Long collegeId, Pageable pageRequest);

    // @Query(value = "SELECT * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.group_id = ?1 AND a.college_id = ?2 AND arm.role_id = 3 \n-- #pageable\n",
    // nativeQuery = true)
    // Page<Account> getAllStudentsBasedOnGroupIdAndCollegeId(Long groupId, Long collegeId, Pageable pageRequest);

    // @Query(value = "SELECT * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.college_id = ?1 AND arm.role_id = 3 AND (a.first_name ILIKE ?2 OR a.last_name ILIKE ?2) \n-- #pageable\n",
    // nativeQuery = true)
    // Page<Account> getAllStudentsBasedOnCollegeIdAndName(Long collegeId, String searchValue, Pageable pageRequest);

    // @Query(value = "SELECT COUNT(*) FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.college_id = ?1 AND arm.role_id = 3",
    // nativeQuery = true)
    // long countAllStudentsByCollegeId(Long collegeId);

    // @Query(value = "SELECT COUNT(*) FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.group_id = ?1 AND a.college_id = ?2 AND arm.role_id = 3",
    // nativeQuery = true)
    // long countAllStudentsByGroupIdAndCollegeId(Long groupId, Long collegeId);

    // @Query(value = "SELECT COUNT(*) FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE a.college_id = ?1 AND arm.role_id = 3 AND (a.first_name ILIKE ?2 OR a.last_name ILIKE ?2)",
    // nativeQuery = true)
    // long countAllStudentsByCollegeIdAndName(Long collegeId, String searchValue);

    @Query(value = "SELECT COUNT(DISTINCT a) FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE arm.role_id <> 1",
    nativeQuery = true)
    long countByRoleRootFalse();

    @Query(value = "SELECT DISTINCT ON (a.username, a.name) * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE arm.role_id <> 1 \n-- #pageable\n",
    nativeQuery = true)
    Page<Account> getByRoleRootFalse(Pageable pageRequest);

    @Query(value = "SELECT DISTINCT ON (a.username, a.name) * FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE arm.role_id <> 1 AND a.name ILIKE ?1 \n-- #pageable\n",
    nativeQuery = true)
    Page<Account> getByRoleRootFalseAndName(String searchValue, Pageable pageRequest);

    @Query(value = "SELECT COUNT(DISTINCT a) FROM account AS a LEFT OUTER JOIN account_role_map AS arm on a.id = arm.account_id WHERE arm.role_id <> 1 AND a.name ILIKE ?1",
    nativeQuery = true)
    long countByRoleRootFalseAndName(String searchValue);
    
    long count();
}
