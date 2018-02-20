CREATE TABLE orders
(
    id                      BIGSERIAL,
    amount                  INTEGER,
    currency_code           VARCHAR(16),
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,

    CONSTRAINT pk_orders PRIMARY KEY (id)
);