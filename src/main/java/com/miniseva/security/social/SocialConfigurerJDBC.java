package com.miniseva.security.social;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;

public class SocialConfigurerJDBC extends SocialConfigurerAdapter implements SocialConfigurer {
    private DataSource dataSource;

    public SocialConfigurerJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Connect Spring Social's connections to PostgreSQL.
     *
     * @param connectionFactoryLocator
     * @return a UsersConnectionRepository that uses JDBC to persist connections to PostgreSQL.
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // TODO: Replace noOpText with real encryption.
        // TextEncryptor encrypts credentials when persisting connections.
        TextEncryptor textEncryptor = Encryptors.noOpText();
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, textEncryptor);
//        return new UsersConnectionRepositoryJDBC(dataSource, connectionFactoryLocator, textEncryptor);
    }
}
