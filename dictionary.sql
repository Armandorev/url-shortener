CREATE DATABASE url_shortener WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'de_CH.UTF-8' LC_CTYPE = 'de_CH.UTF-8';
ALTER DATABASE url_shortener OWNER TO postgres;
\connect url_shortener
CREATE SCHEMA public;
ALTER SCHEMA public OWNER TO postgres;
CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;
CREATE TABLE dictionary (
    hash_id bigint NOT NULL,
    hash character(20) NOT NULL,
    description character(250),
    available boolean DEFAULT false,
    language character varying(2)
);
ALTER TABLE dictionary OWNER TO postgres;
CREATE SEQUENCE dictionary_hash_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE dictionary_hash_id_seq OWNER TO postgres;
ALTER SEQUENCE dictionary_hash_id_seq OWNED BY dictionary.hash_id;
ALTER TABLE ONLY dictionary ALTER COLUMN hash_id SET DEFAULT nextval('dictionary_hash_id_seq'::regclass);
SELECT pg_catalog.setval('dictionary_hash_id_seq', 1, false);
ALTER TABLE ONLY dictionary ADD CONSTRAINT dictionary_pkey PRIMARY KEY (hash_id);
CREATE INDEX hash ON dictionary USING btree (hash);

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
