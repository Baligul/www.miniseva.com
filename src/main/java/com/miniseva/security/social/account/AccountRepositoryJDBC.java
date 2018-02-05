package com.miniseva.security.social.account;

/**
 * API for querying the account table using JDBC.
 */
public interface AccountRepositoryJDBC {

    /**
     * Write a new account to the database.
     *
     * @param account user account
     * @throws UsernameAlreadyInUseException
     */
    void createAccount(Account account) throws UsernameAlreadyInUseException;

    /**
     * Find an account based on the username.
     *
     * @param username username associated with an account
     * @return an account
     */
    Account findAccountByUsername(String username);

}
