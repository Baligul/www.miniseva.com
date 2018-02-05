CREATE TABLE order
(
    id                      BIGSERIAL,
    amount                  INTEGER,
    currency                VARCHAR(16),
    status                  VARCHAR(16),
    receipt                 VARCHAR(32),
    razorpay_order_id       VARCHAR(256),
    razorpay_payment_id     VARCHAR(256),
    coupon                  VARCHAR(6),
    created_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_on              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by              BIGINT,

    CONSTRAINT pk_orders PRIMARY KEY (id)
);