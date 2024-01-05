CREATE TABLE sms
(
    id         BIGSERIAL PRIMARY KEY,
    attributes JSON NOT NULL
);

CREATE TABLE last_fetched_sms_id
(
    id              BIGSERIAL PRIMARY KEY,
    last_fetched_id BIGINT NOT NULL
);

CREATE TABLE protection_subscription
(
    id          BIGSERIAL PRIMARY KEY,
    msisdn      BIGINT NULL,
    is_active   BOOLEAN   NOT NULL,
    change_date TIMESTAMP NOT NULL
);
