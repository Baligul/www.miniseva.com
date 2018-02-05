CREATE TABLE card
(
    id                      BIGSERIAL,
    card_no                 VARCHAR(12) UNIQUE,
    amount                  INTEGER,
    expires_on              TIMESTAMP,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_card PRIMARY KEY (id)
);