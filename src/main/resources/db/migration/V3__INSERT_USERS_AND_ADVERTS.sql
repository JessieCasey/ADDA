insert into users (id, email, enabled, first_name, last_name, password, username)
values (1, 'admin@gmail.com', true, 'Tony', 'Paperoni', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW',
        'Admin');
insert into wishlist (user_id, id)
values (1, '7c3f782f-4fa9-4f05-9bc4-ef44647b8f3c');
UPDATE users
SET wish_list = '7c3f782f-4fa9-4f05-9bc4-ef44647b8f3c'
WHERE id = 1;

insert into users (id, email, enabled, first_name, last_name, password, username)
values (2, 'user@gmail.com', true, 'Alex', 'White', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW',
        'User');
insert into wishlist (user_id, id)
values (2, '0db5a110-c69c-40da-8f1f-f0936d1a0e5b');

UPDATE users
SET wish_list = '0db5a110-c69c-40da-8f1f-f0936d1a0e5b'
WHERE id = 2;

insert into photos (id, size) values
    (1,  0);

insert into advertisements (id, date, description, price, qr_code, title, viewers, category_category_id, photos_id, user_id) values
                  ('46ef9821-5f1f-4927-a98f-1a94f71703eb', '20-11-2022',
                  'The BMW M5 is a high performance variant', 90549.0,
                  'https://i.ibb.co/LnsBJLY/qr-code-46ef9821-5f1f-4927-a98f-1a94f71703eb.png', 'BMW F90', 0,
                  1, 1, 2);

insert into photos (id, size)
values (2,  0);

insert into advertisements (id, date, description, price, qr_code, title, viewers, category_category_id, photos_id, user_id)
values ('e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86', '21-11-2022',
        'Apple iPhone 12 mini is a novelty of 2020, which was a pleasant surprise for all fans of compact smartphones',
        670.0,
        'https://i.ibb.co/fq5ZSyZ/qr-code-e0bbdb63-8cbc-49aa-a442-b7ba6ca20e86.png', 'Iphone 12 mini 256 gb', 0,
        2, 2, 2);


insert into photos (id, size)
values (3, 0);

insert into advertisements (id, date, description, price, qr_code, title, viewers, category_category_id, photos_id, user_id)
values ('73e0b7d7-0051-48d5-b263-3b9bccc20ddb', '21-11-2022',
        'Decorate your everyday looks with charm and ease from the popular American brand Maybelline New York!',
        25.0,
        'https://i.ibb.co/R3hsjb1/qr-code-73e0b7d7-0051-48d5-b263-3b9bccc20ddb.png', 'Lip gloss MaybeLine', 0,
        4, 3, 1);

