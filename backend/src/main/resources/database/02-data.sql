

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
-- TOC entry 3419 (class 0 OID 24577)
-- Dependencies: 215
-- Data for Name: book_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'Adventure stories');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'Classics');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (3, 'Crime');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (4, 'Fairy tales');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (5, 'Fantasy');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (6, 'Horror');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (7, 'Humour and satire');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (8, 'Mystery');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (9, 'Fiction');
INSERT INTO public.book_types (id, name) OVERRIDING SYSTEM VALUE VALUES (10, 'Romance');


--
-- TOC entry 3421 (class 0 OID 24583)
-- Dependencies: 217
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (1, 'Dracula', 'Stoker Bram', 2010, true, 5, 6);
INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (2, 'The Higgler', 'A.E Coppard', 2016, true, 13, 10);
INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (3, 'Murder!', 'Arnold Bennet', 2005, true, 15, 3);
INSERT INTO public.books (id, title, author, publish_year, can_be_borrow, available_amount, type_id) OVERRIDING SYSTEM VALUE VALUES (4, 'The Three Musketeers', 'Alexandre Dumas', 1980, true, 9, 1);

--
-- TOC entry 3428 (class 0 OID 24629)
-- Dependencies: 224
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles (id, name) OVERRIDING SYSTEM VALUE VALUES (1, 'EMPLOYEE');
INSERT INTO public.roles (id, name) OVERRIDING SYSTEM VALUE VALUES (2, 'CUSTOMER');


--
-- TOC entry 3423 (class 0 OID 24589)
-- Dependencies: 219
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, first_name, last_name, phone, email, password) OVERRIDING SYSTEM VALUE VALUES (1, 'Susan', 'Wilner', NULL, 'susanW@gmail.com', '$2a$10$Rena0FQU.puzOytnK7ML9eCwWYOj7o5yJEx4YgPYL7Swhmax6xyQK');
INSERT INTO public.users (id, first_name, last_name, phone, email, password) OVERRIDING SYSTEM VALUE VALUES (2, 'Horace', 'Williams', NULL, 'horaceW@gmail.com', '$2a$10$KFNWjdVY50TcKh8R9rkGV.uyeRgwT9kF6K3lHTqDwdCopibn.oiUq');
INSERT INTO public.users (id, first_name, last_name, phone, email, password) OVERRIDING SYSTEM VALUE VALUES (3, 'Anne', 'Smith', '576815233', 'anneS@gmail.com', '$2a$10$Squ9/8Paj70j9T3Z/XXq5eCcMCLmuhrhziB6pQZjjLB/AU4qhmYpu');


--
-- TOC entry 3425 (class 0 OID 24595)
-- Dependencies: 221
-- Data for Name: users_to_books; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (1, 1, 1);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (2, 2, 5);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (3, 2, 3);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (4, 1, 3);
INSERT INTO public.users_to_books (id, user_id, book_id) OVERRIDING SYSTEM VALUE VALUES (5, 1, 4);

--
-- TOC entry 3430 (class 0 OID 24637)
-- Dependencies: 226
-- Data for Name: users_to_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (1, 1, 2);
INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (2, 2, 2);
INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (3, 3, 2);
INSERT INTO public.users_to_roles (id, user_id, role_id) OVERRIDING SYSTEM VALUE VALUES (4, 3, 1);

--
-- TOC entry 3436 (class 0 OID 0)
-- Dependencies: 216
-- Name: book_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_types_id_seq', 10, true);


--
-- TOC entry 3437 (class 0 OID 0)
-- Dependencies: 218
-- Name: books_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.books_id_seq', 48, true);


--
-- TOC entry 3438 (class 0 OID 0)
-- Dependencies: 220
-- Name: customers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customers_id_seq', 13, true);


--
-- TOC entry 3439 (class 0 OID 0)
-- Dependencies: 222
-- Name: customers_to_books_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customers_to_books_id_seq', 90, true);


--
-- TOC entry 3440 (class 0 OID 0)
-- Dependencies: 223
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 2, true);


--
-- TOC entry 3441 (class 0 OID 0)
-- Dependencies: 225
-- Name: users_to_roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_to_roles_id_seq', 10, true);


-- Completed on 2023-05-13 07:26:34 UTC

--
-- PostgreSQL database dump complete
--

