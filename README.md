[![Build Status](https://travis-ci.org/bengro/url-shortener.svg?branch=master)](https://travis-ci.org/bengro/url-shortener)

# A word-based url shortener
This url shortener spits out a real english word for an arbitrary url. It is using **Java 8**, **spring-boot**, **redis**, **postgres** and **Angular 1.4**. 

**For an example, check out: [w0rd.it/wave](https://w0rd.it/wave)**. The words are coming from the open-source [WordNet](https://wordnet.princeton.edu/) corpora.

# Development

## Backlog
The project backlog can be found [here](https://www.pivotaltracker.com/n/projects/1373074).

## Install front-end tools
The development environment needs to have npm, bower and gulp installed to be able to build the application.

    brew install npm
    npm install bower -g
    npm install gulp -g

## Environment variables
The application is parametrised with environment variables. direnv can be used, to ease the pain of setting them in different (IDE / gradle):

**Install direnv**: 
``brew install direnv``

**Run once**:
``echo 'eval "$(direnv hook bash)"' >> ~/.bash_profile``

**In project root, run**:
``direnv allow .`

*If you need to change the environment variables, see .env in the project root*.

## Persistence 

### Redis
**Install dependencies**: 

    brew install redis 
    brew install posgresql

**Start redis**: ``redis-server``

### Postgres

**Install and initialise postgres** and the user:

    brew install postgresql
    initdb /usr/local/var/$version -E utf8
    createdb
    createuser postgres

**Start postgres**: ``pg_ctl -D /usr/local/var/$version -l /usr/local/var/postgres/server.log start``

**Stop postgres**: ``pg_ctl -D /usr/local/var/$version stop -s -m fast``

Depending on the version of postgres you installed, the directory name for it will be slightly different. Therefore, replace $version with the directory name that was installed.

An easy way of creating the database is by running psql from the project root directory:

    psql -f prepare-db.sql
   
In order to run this, you need a user *postgres* with an *empty string* password.
Note: pgadmin is a handy tool to manage the database, once the database has been created.

## Build, test, run

**Run unit tests**:
``./gradlew test``

**Run end-to-end tests**:
``./gradlew e2e``

**Install dependencies**:
``./gradlew build``

**Build web app**:
``./gradlew packageJS``

**Start server and web server**:
``./gradlew bootRun``

## Deploy

* Install [cf-cli ](https://github.com/cloudfoundry/cli)
* Change name and war file reference in manifest.yml
* Run ``./gradlew build && cf push``
