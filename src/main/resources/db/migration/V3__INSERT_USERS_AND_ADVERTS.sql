insert into users (email, enabled, first_name, last_name, password, username) values ('admin@gmail.com', true, 'Tony', 'Paperoni', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW', 'Admin');
insert into wishlist (user_id, id) values (1, '7c3f782f-4fa9-4f05-9bc4-ef44647b8f3c');
UPDATE users SET wish_list = '7c3f782f-4fa9-4f05-9bc4-ef44647b8f3c' WHERE id = 1;

insert into users (email, enabled, first_name, last_name, password, username) values ('user@gmail.com', true, 'Alex', 'White', '$2a$07$jh.iuHdU8fAtvyOf3k3JwujYSuFJ698tiBRLOXjOyEEf3uhdJGgMW', 'User');
insert into wishlist (user_id, id) values (2, '0db5a110-c69c-40da-8f1f-f0936d1a0e5b');
UPDATE users SET wish_list = '0db5a110-c69c-40da-8f1f-f0936d1a0e5b' WHERE id = 2;