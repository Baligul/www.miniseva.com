CREATE TABLE web_contact_us
(
    id         BIGSERIAL,
    name       VARCHAR(256),
    phone      VARCHAR(25),
    email      VARCHAR(256),
    subject    VARCHAR(256),
    message    TEXT,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_web_contact_us PRIMARY KEY (id)
);