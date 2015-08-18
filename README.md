[![Build Status](https://travis-ci.org/bengro/url-shortener.svg?branch=master)](https://travis-ci.org/bengro/url-shortener)

# About url-shortener
A word-based URL shortener based on Java 8, spring-boot, redis, postgres and Angular 1.4. The url shortener gets its words from open-source WordNet corpora.
Given an arbitrarily long URL, it will shorten it to for example http://your.domain/dripstone and will tell you that dripstone means *a protective drip that is made of stone*.

Give it a try on on the [development instance](http://bengro-url-shortener.cfapps.io/), which is running on [Pivotal Web Services](http://run.pivotal.io/).

## Backlog
The project backlog can be found [here](https://www.pivotaltracker.com/n/projects/1373074).

## Development

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

### Environment variables
The application is parametrised with environment variables. direnv can be used, to ease the pain of setting them in different (IDE / gradle):

Install direnv: 
``brew install direnv``

Create .envrc in prject root:

    export VCAP_SERVICES='
      {
        "redis": [
         {
          "credentials": {
           "host": "localhost",
           "password": "",
           "port": 6379
          },
          "label": "redis",
          "name": "pcf-redis",
          "plan": "dedicated-vm",
          "tags": [
           "pivotal",
           "redis"
          ]
         }
        ]
      }
    '

Run:
``echo 'eval "$(direnv hook bash)"' >> ~/.bash_profile``

In project root run:
``direnv allow .`

### Persistence: Redis and Postgres

Install dependencies: ``brew install redis-server posgresql``

Start redis: ``redis-server``

Start postgres: ``pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log start``

Stop postgres: ``pg_ctl -D /usr/local/var/postgres stop -s -m fast``

## Deploy
* Install [cf-cli ](https://github.com/cloudfoundry/cli)
* Change name and war file reference in manifest.yml
* Run ``./gradlew build && cf push``
