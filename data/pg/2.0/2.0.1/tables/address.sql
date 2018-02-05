CREATE TABLE customer
(
    id                      BIGSERIAL,
    no                 VARCHAR(12),
    lead_name          VARCHAR(256),
    customer2                VARCHAR(256),
    city                    VARCHAR(256),
    state                   VARCHAR(256),
    postal_code             VARCHAR(25),
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_customer PRIMARY KEY (id)
);
