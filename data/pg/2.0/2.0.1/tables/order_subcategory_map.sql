CREATE TABLE order_subcategory_map
(
    id              BIGSERIAL,
    order_id        BIGINT NOT NULL REFERENCES orders (id),
    subcategory_id  BIGINT NOT NULL REFERENCES sub_categories (id),
    created_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (order_id, subcategory_id),
    CONSTRAINT pk_order_subcategory_map PRIMARY KEY (id)
);