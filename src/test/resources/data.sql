DROP SCHEMA IF EXISTS PUBLIC cascade;
CREATE SCHEMA PUBLIC;

create sequence hibernate_sequence start 5 increment 1;

create table advertisements
(
    id                   uuid not null,
    date                 varchar(255),
    description          varchar(255),
    price                float4,
    qr_code              varchar(255),
    title                varchar(255),
    viewers              int4,
    category_category_id int8,
    photos_id            int8,
    user_id              int8,
    primary key (id)
);
create table categories
(
    category_id   int8 generated by default as identity,
    category_name varchar(255),
    primary key (category_id)
);
create table histories
(
    id int8 not null,
    primary key (id)
);
create table histories_adverts
(
    history_id int8 not null,
    adverts_id uuid not null
);
create table photos
(
    id     int8 generated by default as identity,
    photo1 varchar(255),
    photo2 varchar(255),
    photo3 varchar(255),
    photo4 varchar(255),
    photo5 varchar(255),
    photo6 varchar(255),
    photo7 varchar(255),
    photo8 varchar(255),
    size   int4,
    primary key (id)
);
create table refresh_token
(
    id          int8 generated by default as identity,
    expiry_date timestamp,
    token       varchar(255),
    user_id     int8,
    primary key (id)
);
create table roles
(
    id   int8 generated by default as identity,
    name varchar(255),
    primary key (id)
);
create table update_token
(
    token          varchar(255) not null,
    expiry_date    timestamp,
    sensitive_data varchar(255),
    user_id        int8,
    primary key (token)
);
create table url
(
    id              int8 not null,
    creation_date   timestamp,
    expiration_date timestamp,
    original_url    varchar(255),
    short_link      varchar(255),
    primary key (id)
);
create table user_roles
(
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);
create table users
(
    id                int8 generated by default as identity,
    email             varchar(255),
    enabled           boolean not null,
    first_name        varchar(255),
    last_name         varchar(255),
    password          varchar(255),
    username          varchar(255),
    verification_code varchar(255),
    wish_list         uuid,
    primary key (id)
);
create table wishlist
(
    id      uuid not null,
    user_id int8,
    primary key (id)
);
create table wishlist_adverts
(
    wish_list_id uuid not null,
    adverts_id   uuid not null
);

alter table if exists histories_adverts add constraint UK_72rr913nob8msjpwydobasbj8 unique (adverts_id);
alter table if exists users add constraint UKr43af9ap4edm43mmtq01oddj6 unique (username);
alter table if exists users add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table if exists wishlist_adverts add constraint UK_rco5alhmd1xo46axpjyot9f46 unique (adverts_id);
alter table if exists advertisements add constraint FKkk60c90dq3rtnyemsiku7lsv7 foreign key (category_category_id) references categories;
alter table if exists advertisements add constraint FKl0bvnwn8jo9h2blta2nturfof foreign key (photos_id) references photos;
alter table if exists advertisements add constraint FKj69sc4qf7g4p52q8vl0hqvbmp foreign key (user_id) references users;
alter table if exists histories_adverts add constraint FKp323t78v1m672xhiean5e413s foreign key (adverts_id) references advertisements;
alter table if exists histories_adverts add constraint FKqo465vtlhrvd941wfwsgdcfup foreign key (history_id) references histories;
alter table if exists refresh_token add constraint FKjtx87i0jvq2svedphegvdwcuy foreign key (user_id) references users;
alter table if exists update_token add constraint FKjxe4uuphemvh8ksr8fje908yu foreign key (user_id) references users;
alter table if exists user_roles add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles;
alter table if exists user_roles add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users;
alter table if exists wishlist_adverts add constraint FKack99ymvrh9dnnshflqm4958k foreign key (adverts_id) references advertisements;
alter table if exists wishlist_adverts add constraint FKet6u9a9gs90gc9vphnoykax59 foreign key (wish_list_id) references wishlist;

insert into categories (category_name) values ('Car');
insert into categories (category_name) values ('Electronics');
insert into categories (category_name) values ('Beauty');
insert into categories (category_name) values ('Service');

insert into roles (name) values ('ROLE_USER');
insert into roles (name) values ('ROLE_MODERATOR');
insert into roles (name) values ('ROLE_ADMIN');
insert into roles (name) values ('ROLE_BANNED');

insert into users (email, enabled, first_name, last_name, password, username)
values ('admin@gmail.com', true, 'Tony', 'Paperoni', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW','Admin');
insert into wishlist (user_id, id) values (1, '7c3f782f-4fa9-4f05-9bc4-ef44647b8f3c');

insert into user_roles(user_id,role_id) values (1,3);

UPDATE users SET wish_list = '7c3f782f-4fa9-4f05-9bc4-ef44647b8f3c' WHERE id = 1;

insert into users (email, enabled, first_name, last_name, password, username)
values ('user@gmail.com', true, 'Alex', 'White', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW', 'User');
insert into wishlist (user_id, id) values (2, '0db5a110-c69c-40da-8f1f-f0936d1a0e5b');

insert into user_roles(user_id,role_id) values (2,1);

UPDATE users SET wish_list = '0db5a110-c69c-40da-8f1f-f0936d1a0e5b' WHERE id = 2;

insert into users (email, enabled, first_name, last_name, password, username)
values ('test@gmail.com', true, 'Tester', 'Unit', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW', 'Tester');
insert into wishlist (user_id, id) values (3, '0db5a110-c69c-40da-8f1f-f0936d1a0e6c');

insert into user_roles(user_id,role_id) values (3,1);

UPDATE users SET wish_list = '0db5a110-c69c-40da-8f1f-f0936d1a0e6c' WHERE id = 3;

insert into photos (id, size)
values (1, 0);

insert into advertisements (id, date, description, price, qr_code, title, viewers, category_category_id, photos_id,
                            user_id)
values ('46ef9821-5f1f-4927-a98f-1a94f71703eb', '20-11-2022',
        'The BMW M5 is a high performance variant', 90549.0,
        'https://i.ibb.co/LnsBJLY/qr-code-46ef9821-5f1f-4927-a98f-1a94f71703eb.png', 'BMW F90', 0,
        1, 1, 2);

insert into photos (id, size)
values (2, 0);

insert into advertisements (id, date, description, price, qr_code, title, viewers, category_category_id, photos_id,
                            user_id)
values ('e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86', '21-11-2022',
        'Apple iPhone 12 mini is a novelty of 2020, which was a pleasant surprise for all fans of compact smartphones',
        670.0,
        'https://i.ibb.co/fq5ZSyZ/qr-code-e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86.png', 'Iphone 12 mini 256 gb', 0,
        2, 2, 2);


insert into photos (id, size)
values (3, 0);

insert into advertisements (id, date, description, price, qr_code, title, viewers, category_category_id, photos_id,
                            user_id)
values ('73e0b7d7-0051-48d5-b263-3b9bccc20ddb', '21-11-2022',
        'Decorate your everyday looks with charm and ease from the popular American brand Maybelline New York!',
        25.0,
        'https://i.ibb.co/R3hsjb1/qr-code-73e0b7d7-0051-48d5-b263-3b9bccc20ddb.png', 'Lip gloss MaybeLine', 0,
        4, 3, 1);

