
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'Adventure stories');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'Classics');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (3, 'Crime');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (4, 'Horror');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (5, 'Romance');


INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (1, 'Dracula', 'Stoker Bram', 2010, true, 5, 4);
INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (2, 'The Higgler', 'A.E Coppard', 2016, false, 13, 5);
INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (3, 'Murder!', 'Arnold Bennet', 2005, true, 15, 3);
INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (4, 'The Three Musketeers', 'Alexandre Dumas', 1980, true, 9, 1);


INSERT INTO public.roles (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'EMPLOYEE');
INSERT INTO public.roles (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'CUSTOMER');


INSERT INTO public.users (id, first_name, last_name, phone, email, password) OVERRIDING SYSTEM VALUE VALUES (1, 'Susan', 'Wilner', NULL, 'susanW@gmail.com', '$2a$10$Rena0FQU.puzOytnK7ML9eCwWYOj7o5yJEx4YgPYL7Swhmax6xyQK');
INSERT INTO public.users (id, first_name, last_name, phone, email, password) OVERRIDING SYSTEM VALUE VALUES (2, 'Horace', 'Williams', NULL, 'horaceW@gmail.com', '$2a$10$KFNWjdVY50TcKh8R9rkGV.uyeRgwT9kF6K3lHTqDwdCopibn.oiUq');
INSERT INTO public.users (id, first_name, last_name, phone, email, password) OVERRIDING SYSTEM VALUE VALUES (3, 'Anne', 'Smith', '576815233', 'anneS@gmail.com', '$2a$10$Squ9/8Paj70j9T3Z/XXq5eCcMCLmuhrhziB6pQZjjLB/AU4qhmYpu');


INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (1, 1, 1);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (2, 2, 1);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (3, 2, 3);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (4, 1, 3);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (5, 1, 4);


INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (1, 1, 2);
INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (2, 2, 2);
INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (3, 3, 2);
INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (4, 3, 1);


SELECT pg_catalog.setval('public.book_types_id_seq', 5, true);

SELECT pg_catalog.setval('public.books_id_seq', 4, true);

SELECT pg_catalog.setval('public.users_id_seq', 3, true);

SELECT pg_catalog.setval('public.users_to_books_id_seq', 5, true);

SELECT pg_catalog.setval('public.roles_id_seq', 2, true);

SELECT pg_catalog.setval('public.users_to_roles_id_seq', 4, true);

