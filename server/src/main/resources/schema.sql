CREATE TABLE IF NOT EXISTS users
(
    id    BIGSERIAL PRIMARY KEY NOT NULL,
    name  VARCHAR(50),
    email VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS request
(
    id           BIGINT PRIMARY KEY not null,
    description  VARCHAR(50)        not null,
    requestor_id BIGINT             not null,
    FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id            BIGSERIAL PRIMARY KEY not null,
    description  VARCHAR(1024)               NOT NULL,
    requestor_id BIGINT                      not null,
    created TIMESTAMP WITHOUT TIME ZONE not null,
    FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id           BIGSERIAL PRIMARY KEY not null,
    name         VARCHAR(50)                 not null,
    description  VARCHAR(100)                not null,
    is_available boolean                     not null,
    owner_id     BIGINT                      not null,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    request_id   BIGINT,
    FOREIGN KEY (request_id) REFERENCES requests (id)
);


CREATE TABLE IF NOT EXISTS bookings
(
    id          BIGSERIAL PRIMARY KEY not null,
    start_date TIMESTAMP WITHOUT TIME ZONE not null,
    end_date   TIMESTAMP WITHOUT TIME ZONE not null,
    item_id    BIGINT,
    booker_id  BIGINT,
    status     VARCHAR(50) not null,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGSERIAL             not null,
    text      VARCHAR(1024)               NOT NULL,
    item_id   BIGINT                      not null,
    author_id BIGINT                      not null,
    created   TIMESTAMP WITHOUT TIME ZONE not null,
    FOREIGN KEY (item_id) REFERENCES items (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);