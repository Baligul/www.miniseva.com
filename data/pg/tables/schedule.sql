CREATE TABLE schedule
(
    id                      BIGSERIAL,
    item_id                 BIGINT NOT NULL,
    item_name               VARCHAR(256) NOT NULL,
    item_price              INTEGER NOT NULL,
    item_discount           INTEGER DEFAULT 0,
    item_description        VARCHAR(1024),
    item_product_id         BIGINT NOT NULL,
    slot                    VARCHAR(256),
    is_scheduled            INTEGER NOT NULL,
    dates                   TEXT NOT NULL,
    quantity                INTEGER NOT NULL,
    total_price             INTEGER NOT NULL,
    order_id                BIGINT,
    status                  VARCHAR(256),
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_schedule PRIMARY KEY (id)
);