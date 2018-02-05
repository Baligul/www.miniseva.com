package com.miniseva.security.role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RoleRepository extends CrudRepository<Role, Long> {
    //@Query("select a.role from AccountRole a, Account b where b.email=?1 and a.accountId = b.id")
    //public List<String> findRolesByEmail(String email);
}
