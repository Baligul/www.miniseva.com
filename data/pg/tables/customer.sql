CREATE TABLE customer
(
    id                      BIGSERIAL,
    card_no                 VARCHAR(256) NOT NULL UNIQUE,
    no                      VARCHAR(12) NOT NULL,
    lead_id                 BIGINT NOT NULL,
    block_id                BIGINT NULL,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_customer PRIMARY KEY (id)
);