CREATE TABLE users
(
    id                 UUID                        NOT NULL,
    username           VARCHAR(255)                NOT NULL,
    password           VARCHAR(255)                NOT NULL,
    email              VARCHAR(255)                NOT NULL,
    status             VARCHAR(16)                 NOT NULL,
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

CREATE TABLE rooms
(
    id                 UUID                        NOT NULL,
    name               VARCHAR(255)                NOT NULL,
    owner_id           UUID                        NOT NULL,
    player_state       VARCHAR(255),
    last_video_url     VARCHAR(255),
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_rooms PRIMARY KEY (id)
);

ALTER TABLE rooms
    ADD CONSTRAINT FK_ROOMS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);

CREATE TABLE rooms_users
(
    room_id UUID NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (room_id, user_id)
);

ALTER TABLE rooms_users
    ADD CONSTRAINT fk_rooms_on_users FOREIGN KEY (room_id) REFERENCES rooms (id);

ALTER TABLE rooms_users
    ADD CONSTRAINT fk_users_on_rooms FOREIGN KEY (user_id) REFERENCES users (id);
