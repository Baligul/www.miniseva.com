package com.miniseva.security.social;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.SocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class SocialConfiguration {
    @Bean
    public SocialConfigurer socialConfigurer(DataSource dataSource) {
        return new SocialConfigurerJDBC(dataSource);
    }
}
