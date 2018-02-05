-- Social Connections
-- Persist connections to the various social providers. Each local account may
-- have 0 to many social connections.

-- DO NOT CHANGE THE NAME OF THIS TABLE! IT IS HARDCODED INSIDE SPRING SOCIAL!
--
-- Schema: https://github.com/spring-projects/spring-social/blob/master/spring-social-core/src/main/resources/org/springframework/social/connect/jdbc/JdbcUsersConnectionRepository.sql
-- Class: https://github.com/spring-projects/spring-social/blob/2.0.0.M1/spring-social-core/src/main/java/org/springframework/social/connect/jdbc/JdbcUsersConnectionRepository.java

CREATE TABLE UserConnection
(
    userId      VARCHAR(255) NOT NULL,
    providerId     VARCHAR(255) NOT NULL,
    providerUserId VARCHAR(255),
    rank           INTEGER      NOT NULL,
    displayName    VARCHAR(255),
    profileUrl     VARCHAR(512),
    imageUrl       VARCHAR(512),
    accessToken    VARCHAR(512) NOT NULL,
    secret         VARCHAR(512),
    refreshToken   VARCHAR(512),
    expireTime    BIGINT,

    -- Only one connection from each local account to each provider
    UNIQUE (userId, providerId, rank),
    CONSTRAINT pk_UserConnection PRIMARY KEY (userId, providerId, rank)
);

CREATE UNIQUE INDEX UserConnectionRank on UserConnection(userId, providerId, rank);

-- CREATE TABLE account_connection
-- (
--     id               BIGSERIAL,
--     account_id       BIGINT       NOT NULL REFERENCES account (id),
--     provider_id      VARCHAR(256),
--     provider_user_id VARCHAR(256),
--     rank             INTEGER      NOT NULL,
--     display_name     VARCHAR(256),
--     profile_url      VARCHAR(512),
--     image_url        VARCHAR(512),
--     access_token     VARCHAR(512),
--     secret           VARCHAR(512) NOT NULL,
--     refresh_token    VARCHAR(512),
--     expire_time      BIGINT,
--
--     -- Only one connection from each local account to each provider
--     UNIQUE (account_id, provider_id, provider_user_id),
--     CONSTRAINT pk_connection PRIMARY KEY (id)
-- );
