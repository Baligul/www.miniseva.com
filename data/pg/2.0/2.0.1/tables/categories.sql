CREATE TABLE categories
(
    id                      BIGSERIAL,
    name                    VARCHAR(256),
    description             VARCHAR(1024),
    slug                    VARCHAR(256),
    sub_categories_count    SMALLINT,
    sort_order              SMALLINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,

    CONSTRAINT pk_categories PRIMARY KEY (id)
);