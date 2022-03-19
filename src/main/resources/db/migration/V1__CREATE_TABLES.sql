create sequence hibernate_sequence start 1 increment 1;

create table user_table
(
    id       serial,
    username varchar(64) not null,
    password varchar(64) not null,
    PRIMARY KEY (id)
);

create table advertisement_table
(
    id          serial,
    title       varchar(64)   not null,
    categories  varchar(64)   not null,
    description varchar(1024) not null,
    geoposition varchar(64),
    email       varchar(100)  not null,
    phone       varchar(64)   not null,
    user_name   varchar(64),
    PRIMARY KEY (id)
);

create table photo_table
(
    id          serial,
    photo1      varchar(1024),
    photo2      varchar(1024),
    photo3      varchar(1024),
    photo4      varchar(1024),
    photo5      varchar(1024),
    photo6      varchar(1024),
    photo7      varchar(1024),
    photo8      varchar(1024),
    PRIMARY KEY (id)
);