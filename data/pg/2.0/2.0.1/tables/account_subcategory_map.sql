CREATE TABLE account_subcategory_map
(
    id              BIGSERIAL,
    account_id      BIGINT NOT NULL REFERENCES account (id),
    subcategory_id  BIGINT NOT NULL REFERENCES sub_categories (id),
    created_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (account_id, subcategory_id),
    CONSTRAINT pk_account_subcategory_map PRIMARY KEY (id)
);