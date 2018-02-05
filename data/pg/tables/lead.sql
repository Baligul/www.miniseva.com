CREATE TABLE lead
(
    id                      BIGSERIAL,
    name                    VARCHAR(256) NOT NULL UNIQUE,
    address1                VARCHAR(256) NOT NULL,
    city                    VARCHAR(256),
    state                   VARCHAR(256),
    postal_code             VARCHAR(25),
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_lead PRIMARY KEY (id)
);