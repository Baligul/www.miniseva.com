CREATE TABLE block
(
    id                      BIGSERIAL,
    name                    VARCHAR(256),
    lead_id                 BIGINT NOT NULL,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_block PRIMARY KEY (id)
);