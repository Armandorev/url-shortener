--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.4
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-08-19 23:37:59 BST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE url_shortener;
--
-- TOC entry 2268 (class 1262 OID 16392)
-- Name: url_shortener; Type: DATABASE; Schema: -; Owner: bengro
--

CREATE DATABASE url_shortener WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'de_CH.UTF-8' LC_CTYPE = 'de_CH.UTF-8';


ALTER DATABASE url_shortener OWNER TO bengro;

\connect url_shortener

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: bengro
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO bengro;

--
-- TOC entry 2269 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: bengro
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 174 (class 3079 OID 12123)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2271 (class 0 OID 0)
-- Dependencies: 174
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 16422)
-- Name: dictionary; Type: TABLE; Schema: public; Owner: bengro; Tablespace: 
--

CREATE TABLE dictionary (
    hash_id bigint NOT NULL,
    hash character(20) NOT NULL,
    description character(250),
    available boolean DEFAULT false,
    language character varying(2)
);


ALTER TABLE dictionary OWNER TO bengro;

--
-- TOC entry 172 (class 1259 OID 16420)
-- Name: dictionary_hash_id_seq; Type: SEQUENCE; Schema: public; Owner: bengro
--

CREATE SEQUENCE dictionary_hash_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE dictionary_hash_id_seq OWNER TO bengro;

--
-- TOC entry 2272 (class 0 OID 0)
-- Dependencies: 172
-- Name: dictionary_hash_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bengro
--

ALTER SEQUENCE dictionary_hash_id_seq OWNED BY dictionary.hash_id;


--
-- TOC entry 2149 (class 2604 OID 16433)
-- Name: hash_id; Type: DEFAULT; Schema: public; Owner: bengro
--

ALTER TABLE ONLY dictionary ALTER COLUMN hash_id SET DEFAULT nextval('dictionary_hash_id_seq'::regclass);


--
-- TOC entry 2263 (class 0 OID 16422)
-- Dependencies: 173
-- Data for Name: dictionary; Type: TABLE DATA; Schema: public; Owner: bengro
--

COPY dictionary (hash_id, hash, description, available, language) FROM stdin;
\.


--
-- TOC entry 2273 (class 0 OID 0)
-- Dependencies: 172
-- Name: dictionary_hash_id_seq; Type: SEQUENCE SET; Schema: public; Owner: bengro
--

SELECT pg_catalog.setval('dictionary_hash_id_seq', 1, false);


--
-- TOC entry 2151 (class 2606 OID 16428)
-- Name: dictionary_pkey; Type: CONSTRAINT; Schema: public; Owner: bengro; Tablespace: 
--

ALTER TABLE ONLY dictionary
    ADD CONSTRAINT dictionary_pkey PRIMARY KEY (hash_id);


--
-- TOC entry 2152 (class 1259 OID 16432)
-- Name: hash; Type: INDEX; Schema: public; Owner: bengro; Tablespace: 
--

CREATE INDEX hash ON dictionary USING btree (hash);


--
-- TOC entry 2270 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: bengro
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM bengro;
GRANT ALL ON SCHEMA public TO bengro;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-08-19 23:37:59 BST

--
-- PostgreSQL database dump complete
--

