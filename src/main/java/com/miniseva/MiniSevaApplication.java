package com.miniseva;

import com.miniseva.security.social.signin.SimpleSignInAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.web.SignInAdapter;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.miniseva")
@EntityScan(basePackages = "com.miniseva")
public class MiniSevaApplication {
    private static final Logger log = LoggerFactory.getLogger(MiniSevaApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(MiniSevaApplication.class, args);
        log.info("Started server.");
    }

    /**
     * Log a user into the application when a matching connection is found.
     *
     * @return
     */
    @Bean
    public SignInAdapter signInAdapter() {
        return new SimpleSignInAdapter(new HttpSessionRequestCache());
    }
}
