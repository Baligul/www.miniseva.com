package com.miniseva.app.accountrolemap;

import org.joda.time.DateTime;

import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

public interface AccountRoleMapRepository extends CrudRepository<AccountRoleMap, Long> {    
    long count();
}