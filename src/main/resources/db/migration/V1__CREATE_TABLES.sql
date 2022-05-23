create sequence hibernate_sequence start 1 increment 1;

create table users_table
(
    id         serial,
    first_name varchar(64) not null,
    last_name  varchar(64) not null,
    username   varchar(64) not null,
    email      varchar(64) not null,
    wishList   UUID,
    PRIMARY KEY (id)
);

create table history_table
(
    id      UUID,
    user_id integer,
    PRIMARY KEY (id)
);

create table roles
(
    id   serial,
    name varchar(60) not null,
    PRIMARY KEY (id)
);
create table categories_table
(
    category_id   serial      not null,
    category_name varchar(64) not null,
    PRIMARY KEY (category_id)
);

create table photo_table
(
    id     serial,
    photo1 varchar(1024),
    photo2 varchar(1024),
    photo3 varchar(1024),
    photo4 varchar(1024),
    photo5 varchar(1024),
    photo6 varchar(1024),
    photo7 varchar(1024),
    photo8 varchar(1024),
    PRIMARY KEY (id)
);

create table advertisement_table
(
    id          UUID          NOT NULL,
    title       varchar(64)   not null,
    category_id integer,
    photo_id    integer,
    price       integer,
    description varchar(1024) not null,
    email       varchar(100)  not null,
    user_name   varchar(64),
    date        varchar(64)   not null,
    qr_code     varchar(1024) not null,
    viewers     integer       not null,
    PRIMARY KEY (id)
);

create table wishlist_table
(
    id               UUID,
    user_id          integer,
    advertisement_id UUID,
    PRIMARY KEY (id)
);

ALTER TABLE wishlist_table
    ADD CONSTRAINT wish_list_advertisement_fkey FOREIGN KEY (advertisement_id) REFERENCES advertisement_table (id);

ALTER TABLE wishlist_table
    ADD CONSTRAINT wish_list_user_fkey FOREIGN KEY (user_id) REFERENCES users_table (id);

ALTER TABLE advertisement_table
    ADD CONSTRAINT categories_advertisement_fkey FOREIGN KEY (category_id) REFERENCES categories_table (category_id);

ALTER TABLE advertisement_table
    ADD CONSTRAINT photos_advertisement_fkey FOREIGN KEY (photo_id) REFERENCES photo_table (id);

