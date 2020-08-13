--
-- PostgreSQL database dump
--

-- Dumped from database version 12.1
-- Dumped by pg_dump version 12.1

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: telegram_connected_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telegram_connected_users (
    chat_id bigint NOT NULL,
    user_name character varying(255),
    full_name character varying(255),
    city_id integer
);


ALTER TABLE public.telegram_connected_users OWNER TO postgres;

--
-- Data for Name: telegram_connected_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.telegram_connected_users (chat_id, user_name, full_name, city_id) FROM stdin;
\.


--
-- Name: telegram_connected_users telegram_connected_users_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telegram_connected_users
    ADD CONSTRAINT telegram_connected_users_pk PRIMARY KEY (chat_id);


--
-- Name: telegram_connected_users_chat_id_uindex; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX telegram_connected_users_chat_id_uindex ON public.telegram_connected_users USING btree (chat_id);


--
-- PostgreSQL database dump complete
--