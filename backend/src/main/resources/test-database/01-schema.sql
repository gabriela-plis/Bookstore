
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


CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


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


CREATE TABLE public.books (
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


CREATE TABLE public.users (
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


CREATE TABLE public.users_to_books (
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

CREATE TABLE public.roles (
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


CREATE TABLE public.users_to_roles (
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

ALTER TABLE ONLY public.book_types
    ADD CONSTRAINT book_types_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT users_to_books_pkey PRIMARY KEY (id);

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
    ADD CONSTRAINT "usersToBooks_users_fkey" FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;



