SET MODE MYSQL;

DROP TABLE IF EXISTS balance;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS account;
CREATE TABLE account
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL
);

CREATE TABLE balance
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    account_id BIGINT                            NOT NULL,
    amount     DOUBLE                            NOT NULL DEFAULT 0,
    currency   VARCHAR(3)                        NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT uc_balance UNIQUE (account_id, currency)
);

CREATE TABLE transaction
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    sending_id   BIGINT     NOT NULL,
    receiving_id BIGINT     NOT NULL,
    amount       DECIMAL    NOT NULL,
    currency     VARCHAR(3) NOT NULL,
    created_date DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    xfer_date    DATETIME   NOT NULL,

    FOREIGN KEY (sending_id) REFERENCES account (id),
    FOREIGN KEY (receiving_id) REFERENCES account (id)
);