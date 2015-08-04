[![Build Status](https://travis-ci.org/bengro/url-shortener.svg?branch=master)](https://travis-ci.org/bengro/url-shortener)

# url-shortener
An URL shortener based on Java 8, using spring-boot, AngularJS front-end, all deployable to PWS. 

[Development Instance](http://bengro-url-shortener.cfapps.io/)

## Backlog
[Pivotal Tracker](https://www.pivotaltracker.com/n/projects/1373074)

## Local development tasks

Run unit tests:
``./gradlew test``

Run end-to-end tests:
``./gradlew e2e``

Install dependencies:
``./gradlew build``

Build web app:
``./gradlew packageJS``

Start server and web server:
``./gradlew bootRun``

### Persistence layer: Redis and Postgres

Install dependencies: ``brew install redis-server posgresql``

Start redis: ``redis-server``

Start postgres: ``pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start``

Stop postgres: ``pg_ctl -D /usr/local/var/postgres stop -s -m fast``