CREATE TABLE product
(
    id                      BIGSERIAL,
    name                    VARCHAR(256) NOT NULL UNIQUE,
    slug                    VARCHAR(256),
    description             VARCHAR(1024),
    img_path                VARCHAR(256),
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_product PRIMARY KEY (id)
);