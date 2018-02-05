CREATE TABLE sub_categories
(
    id                      BIGSERIAL,
    name                    VARCHAR(256),
    description             VARCHAR(1024),
    slug                    VARCHAR(256),
    category_id             BIGINT,
    duration                VARCHAR(16),
    price                   INTEGER,
    sort_order              SMALLINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,

    CONSTRAINT pk_sub_categories PRIMARY KEY (id)
);