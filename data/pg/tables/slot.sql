CREATE TABLE slot
(
    id                      BIGSERIAL,
    slot_val                VARCHAR(12) UNIQUE,
    end_time                TIME,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,

    CONSTRAINT pk_slot PRIMARY KEY (id)
);