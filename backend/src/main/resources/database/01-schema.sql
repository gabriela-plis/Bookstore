
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

--
-- TOC entry 2 (class 3079 OID 32834)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 3424 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 24577)
-- Name: book_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_types (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.book_types OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 24582)
-- Name: book_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.book_types ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.book_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 217 (class 1259 OID 24583)
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

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

--
-- TOC entry 218 (class 1259 OID 24588)
-- Name: books_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.books ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 219 (class 1259 OID 24589)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    phone text,
    email text NOT NULL,
    password text NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24594)
-- Name: customers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 221 (class 1259 OID 24595)
-- Name: users_to_books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_to_books (
    id integer NOT NULL,
    user_id integer NOT NULL,
    book_id integer NOT NULL
);


ALTER TABLE public.users_to_books OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 24598)
-- Name: customers_to_books_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users_to_books ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.customers_to_books_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 24629)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id integer NOT NULL,
    name text NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 24628)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 24637)
-- Name: users_to_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_to_roles (
    id integer NOT NULL,
    user_id integer,
    role_id integer
);


ALTER TABLE public.users_to_roles OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 24636)
-- Name: users_to_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users_to_roles ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_to_roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3259 (class 2606 OID 24600)
-- Name: book_types book_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_types
    ADD CONSTRAINT book_types_pkey PRIMARY KEY (id);


--
-- TOC entry 3261 (class 2606 OID 24602)
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);


--
-- TOC entry 3263 (class 2606 OID 24604)
-- Name: users customers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);


--
-- TOC entry 3267 (class 2606 OID 24606)
-- Name: users_to_books customers_to_books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT customers_to_books_pkey PRIMARY KEY (id);


--
-- TOC entry 3271 (class 2606 OID 24635)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 3269 (class 2606 OID 41027)
-- Name: users_to_books unique_book_to_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT unique_book_to_user UNIQUE (user_id, book_id);


--
-- TOC entry 3265 (class 2606 OID 24643)
-- Name: users unique_email; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT unique_email UNIQUE (email);


--
-- TOC entry 3273 (class 2606 OID 24641)
-- Name: users_to_roles users_to_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_to_roles
    ADD CONSTRAINT users_to_roles_pkey PRIMARY KEY (id);


--
-- TOC entry 3274 (class 2606 OID 24607)
-- Name: books books_bookType_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.books
    ADD CONSTRAINT "books_bookType_fkey" FOREIGN KEY (type_id) REFERENCES public.book_types(id) NOT VALID;


--
-- TOC entry 3275 (class 2606 OID 24612)
-- Name: users_to_books customersToBooks_books_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT "customersToBooks_books_fkey" FOREIGN KEY (book_id) REFERENCES public.books(id) NOT VALID;


--
-- TOC entry 3276 (class 2606 OID 24617)
-- Name: users_to_books customersToBooks_customers_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_to_books
    ADD CONSTRAINT "customersToBooks_customers_fkey" FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;



