CREATE TABLE item
(
    id                      BIGSERIAL,
    name                    VARCHAR(256) NOT NULL,
    discount                INTEGER DEFAULT 0,
    price                   INTEGER NOT NULL,
    description             VARCHAR(1024),
    img_path                VARCHAR(256),
    product_id              BIGINT NOT NULL,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_item PRIMARY KEY (id)
);