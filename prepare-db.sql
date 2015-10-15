DROP DATABASE url_shortener;
CREATE DATABASE url_shortener;
ALTER DATABASE url_shortener OWNER TO postgres;
\connect url_shortener
CREATE SCHEMA public;
ALTER SCHEMA public OWNER TO postgres;
CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;
CREATE TABLE dictionary (
    hash character(20) NOT NULL PRIMARY KEY,
    description character(250),
    available boolean DEFAULT false,
    language character varying(2),
    indexed timestamp default current_timestamp,
    registered timestamp
);
ALTER TABLE dictionary OWNER TO postgres;
CREATE INDEX hash ON dictionary USING btree (hash);

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
