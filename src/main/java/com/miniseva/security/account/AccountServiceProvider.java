package com.miniseva.security.account;

import com.miniseva.security.role.RoleService;
import com.miniseva.security.role.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;

/**
 * Account Service used by the application. This is distinct from AccountDetailsService in that this service provides
 * various methods whereas AccountDetailsService
 */
@Service
public class AccountServiceProvider implements AccountService {
    private PasswordEncoder passwordEncoder;
    private AccountRepository repoAccount;
    private RoleService srvRole;

    @Inject
    public AccountServiceProvider(PasswordEncoder passwordEncoder, AccountRepository repoAccount, RoleService srvRole) {
        this.passwordEncoder = passwordEncoder;
        this.repoAccount = repoAccount;
        this.srvRole = srvRole;
    }

    // BH: The below code was always setting the password after encoding 
    //     even if it is encoded already i.e. in case we just want to 
    //     save other details of an account and want password to be as it is
    //     but still it was encoding the encoded password so I commented this code.
    //     
    //     Also, the below code was always setting the role as student irrespective
    //     of whatever role we want to set.
    // @Override
    // public Account save(Account account) {
    //     account.setPassword(passwordEncoder.encode(account.getPassword()));
    //     // Delixus.com uses ROLE_USER
    //     //account.setRoles(srvRole.findByRole("ROLE_USER"));
    //     account.setRoles(srvRole.findByRole("ROLE_STUDENT"));
    //     return repoAccount.save(account);
    // }

    @Override
    public Account save(Account account) {
        return repoAccount.save(account);
    }

    @Override
    public Account findById(Long id) {
        return repoAccount.findOne(id);
    }

    @Override
    public Account findByUsername(String username) {
        return repoAccount.findByUsername(username);
    }

    /**
     * Get a userId for the provided username. Return 0 if the user does not exist.
     * @param username
     * @return
     */
    @Override
    public Long getUserId(String username) {
        Long userId = repoAccount.findIdByUsername(username);
        if (userId == null)
            return 0L;
        return userId;
    }

    @Override
    public List<Account> findByEnabled(boolean enabled) {
        return repoAccount.findByEnabled(enabled);
    }

    @Override
    public Page<Account> findByEnabled(boolean enabled, Pageable pageRequest) {
        return repoAccount.findByEnabled(enabled, pageRequest);
    }

    @Override
    public long count() {
        return repoAccount.count();
    }

    @Override
    public long countEnabled() {
        return repoAccount.countByEnabled(true);
    }

    @Override
    public long countDisabled() {
        return repoAccount.countByEnabled(false);
    }

    @Override
    public boolean accountExists(String username) {
        return repoAccount.countByEmail(username) >= 1;
    }

    @Override
    public boolean customerExists(Long customerId) {
        return repoAccount.countByCustomerId(customerId) >= 1;
    }

    // @Override
    // public List<Account> getStudentsBasedonCollegeId(Long collegeId) {
    //     return repoAccount.getStudentsBasedonCollegeId(collegeId);
    // }

    // @Override
    // public List<Account> getStudentsBasedonGroupIdAndCollegeId(Long groupId, Long collegeId) {
    //     return repoAccount.getStudentsBasedonGroupIdAndCollegeId(groupId, collegeId);
    // }
    
    // @Override
    // public Page<Account> getAllStudentsBasedOnCollegeId(Long collegeId, Pageable pageRequest) {
    //     return repoAccount.getAllStudentsBasedOnCollegeId(collegeId, pageRequest);
    // }
    
    // @Override
    // public Page<Account> getAllStudentsBasedOnGroupIdAndCollegeId(Long groupId, Long collegeId, Pageable pageRequest) {
    //     return repoAccount.getAllStudentsBasedOnGroupIdAndCollegeId(groupId, collegeId, pageRequest);
    // }

    // @Override
    // public Page<Account> getAllStudentsBasedOnCollegeIdAndName(Long collegeId, String searchValue, Pageable pageRequest) {
    //     return repoAccount.getAllStudentsBasedOnCollegeIdAndName(collegeId, searchValue, pageRequest);
    // }
    
    // @Override
    // public long countAllStudentsByCollegeId(Long collegeId) {
    //     return repoAccount.countAllStudentsByCollegeId(collegeId);
    // }
    
    // @Override
    // public long countAllStudentsByGroupIdAndCollegeId(Long groupId, Long collegeId) {
    //     return repoAccount.countAllStudentsByGroupIdAndCollegeId(groupId, collegeId);
    // }
    
    // @Override
    // public long countAllStudentsByCollegeIdAndName(Long collegeId, String searchValue) {
    //     return repoAccount.countAllStudentsByCollegeIdAndName(collegeId, searchValue);
    // }

    @Override
    public long countByRoleRootFalse() {
        return repoAccount.countByRoleRootFalse();
    }

    @Override
    public Page<Account> getByRoleRootFalse(Pageable pageRequest) {
        return repoAccount.getByRoleRootFalse(pageRequest);
    }

    @Override
    public Page<Account> getByRoleRootFalseAndName(String searchValue, Pageable pageRequest) {
        return repoAccount.getByRoleRootFalseAndName(searchValue, pageRequest);
    }
    
    @Override
    public long countByRoleRootFalseAndName(String searchValue) {
        return repoAccount.countByRoleRootFalseAndName(searchValue);
    }
}