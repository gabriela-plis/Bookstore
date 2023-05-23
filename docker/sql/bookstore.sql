

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


-- CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;

-- COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';

SET default_tablespace = '';

SET default_table_access_method = heap;



CREATE TABLE public.book_types (
    id integer NOT NULL,
    name text NOT NULL
);

ALTER TABLE public.book_types OWNER TO postgres;

ALTER TABLE public.book_types ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.book_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS public.books (
    id integer NOT NULL,
    title text NOT NULL,
    author text NOT NULL,
    publish_year integer NOT NULL,
    can_be_borrow boolean NOT NULL,
    available_amount integer NOT NULL,
    type_id integer NOT NULL
);

ALTER TABLE public.books OWNER TO postgres;

ALTER TABLE public.books ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS public.users (
    id integer NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    phone text,
    email text NOT NULL,
    password text NOT NULL
);

ALTER TABLE public.users OWNER TO postgres;

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS public.users_to_books (
    id integer NOT NULL,
    user_id integer NOT NULL,
    book_id integer NOT NULL
);

ALTER TABLE public.users_to_books OWNER TO postgres;

ALTER TABLE public.users_to_books ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_to_books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS public.roles (
    id integer NOT NULL,
    name text NOT NULL
);

ALTER TABLE public.roles OWNER TO postgres;

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



CREATE TABLE IF NOT EXISTS public.users_to_roles (
    id integer NOT NULL,
    user_id integer,
    role_id integer
);

ALTER TABLE public.users_to_roles OWNER TO postgres;

ALTER TABLE public.users_to_roles ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_to_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);



INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (1, 'Adventure stories');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (2, 'Classics');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (3, 'Crime');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (4, 'Fairy tales');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (5, 'Fantasy');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (6, 'Horror');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (7, 'Humour and satire');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (8, 'Mystery');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (9, 'Fiction');
INSERT INTO public.book_types OVERRIDING SYSTEM VALUE VALUES (10, 'Romance');


INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (1, 'This Girl', 'Jacqueline Wilson', 2000, true, 20, 9);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (2, 'The Grass is Always Greener', 'Jeffrey Archer', 2019, true, 10, 9);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (3, 'Murder!', 'Arnold Bennet', 2005, true, 16, 3);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (4, 'The Higgler', 'A.E Coppard', 2016, true, 13, 10);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (5, 'Dracula', 'Stoker Bram', 2010, true, 25, 6);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (6, 'Sense and Sensibility', 'Jane Austen', 1999, true, 15, 10);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (7, 'The Dancing Partner:Clocks', 'Jerome K.Jerome', 2010, true, 13, 7);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (8, 'Age of Vice', 'Deepti Kapoor', 2023, true, 10, 9);
INSERT INTO public.books OVERRIDING SYSTEM VALUE VALUES (9, 'The Three Musketeers', 'Alexandre Dumas', 1980, true, 16, 1);


INSERT INTO public.roles OVERRIDING SYSTEM VALUE VALUES (1, 'EMPLOYEE');
INSERT INTO public.roles OVERRIDING SYSTEM VALUE VALUES (2, 'CUSTOMER');


INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (1, 'Anne', 'Smith', '576815233', 'anneS@gmail.com', '$2a$10$Squ9/8Paj70j9T3Z/XXq5eCcMCLmuhrhziB6pQZjjLB/AU4qhmYpu');
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (2, 'Susan', 'Wilner', NULL, 'susanW@gmail.com', '$2a$10$Rena0FQU.puzOytnK7ML9eCwWYOj7o5yJEx4YgPYL7Swhmax6xyQK');
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (3, 'Horace', 'Williams', NULL, 'horaceW@gmail.com', '$2a$10$KFNWjdVY50TcKh8R9rkGV.uyeRgwT9kF6K3lHTqDwdCopibn.oiUq');
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (4, 'James', 'Collins', NULL, 'jamesC@gmail.com', '$2a$10$UO/tO.dCLeiQGv6EtJDTaOjkS87qkuicr0jH.M4ncPZe5JdCBWweG');
INSERT INTO public.users OVERRIDING SYSTEM VALUE VALUES (5, 'Regina', 'Murray', '785421055', 'reginaM@gmail.com', '$2a$10$ZqO1y/bR7dH02hBxwEetxu5ECJf3QcuS04lWCYFJur7SIB.lidBLO');


INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (1, 1, 7);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (2, 5, 7);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (3, 2, 2);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (4, 2, 5);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (5, 2, 3);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (6, 1, 3);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (7, 5, 4);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (8, 1, 9);
INSERT INTO public.users_to_books OVERRIDING SYSTEM VALUE VALUES (9, 5, 9);


INSERT INTO public.users_to_roles OVERRIDING SYSTEM VALUE VALUES (1, 1, 2);
INSERT INTO public.users_to_roles OVERRIDING SYSTEM VALUE VALUES (2, 2, 2);
INSERT INTO public.users_to_roles OVERRIDING SYSTEM VALUE VALUES (3, 3, 2);
INSERT INTO public.users_to_roles OVERRIDING SYSTEM VALUE VALUES (4, 4, 2);
INSERT INTO public.users_to_roles OVERRIDING SYSTEM VALUE VALUES (5, 5, 2);
INSERT INTO public.users_to_roles OVERRIDING SYSTEM VALUE VALUES (6, 5, 1);


SELECT pg_catalog.setval('public.book_types_id_seq', 10, true);

SELECT pg_catalog.setval('public.books_id_seq', 9, true);

SELECT pg_catalog.setval('public.users_id_seq', 5, true);

SELECT pg_catalog.setval('public.users_to_books_id_seq', 9, true);

SELECT pg_catalog.setval('public.roles_id_seq', 2, true);

SELECT pg_catalog.setval('public.users_to_roles_id_seq', 6, true);




ALTER TABLE ONLY public.book_types
    ADD CONSTRAINT book_types_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT userrs_to_books_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT unique_book_to_user UNIQUE (user_id, book_id);


ALTER TABLE ONLY public.users
    ADD CONSTRAINT unique_email UNIQUE (email);


ALTER TABLE ONLY public.users_to_roles
    ADD CONSTRAINT users_to_roles_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.books
    ADD CONSTRAINT "books_bookType_fkey" FOREIGN KEY (type_id) REFERENCES public.book_types(id) NOT VALID;


ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT "usersToBooks_books_fkey" FOREIGN KEY (book_id) REFERENCES public.books(id) NOT VALID;


ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT "usersToBooks_customers_fkey" FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;

