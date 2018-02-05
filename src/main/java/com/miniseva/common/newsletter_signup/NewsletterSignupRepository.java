package com.miniseva.common.newsletter_signup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterSignupRepository extends CrudRepository<NewsletterSignup, Long> {
    NewsletterSignup findByEmail(String email);

    @Query("SELECT COUNT(email) FROM NewsletterSignup WHERE email=?1")
    int emailExists(String email);

}
