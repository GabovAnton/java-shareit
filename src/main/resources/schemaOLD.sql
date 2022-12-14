DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;



CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name  VARCHAR(255)                                        NOT NULL,
    email VARCHAR(512) UNIQUE                                 NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description  VARCHAR(255) NOT NULL,
    requestor_id BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    is_available boolean      NOT NULL,
    owner_id     BIGINT REFERENCES users (id) ON DELETE CASCADE,
    request_id   BIGINT
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT REFERENCES items (id) ON DELETE CASCADE,
    booker_id  BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status     VARCHAR(12)
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text         VARCHAR(512)                NOT NULL,
    item_id      BIGINT REFERENCES items (id) ON DELETE CASCADE,
    author_id    BIGINT REFERENCES users (id) ON DELETE CASCADE,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);