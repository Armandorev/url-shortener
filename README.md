[![Build Status](https://travis-ci.org/bengro/url-shortener.svg?branch=master)](https://travis-ci.org/bengro/url-shortener)

# url-shortener
An URL shortener based on Java 8, using spring-boot, AngularJS front-end, all deployable to PWS. 

[Pivotal Tracker](https://www.pivotaltracker.com/n/projects/1373074)

[Dev Instance](http://bengro-url-shortener.cfapps.io/)

## development

Run unit tests:
```./gradlew test```

Run end-to-end tests:
```./gradlew e2e```

Install dependencies:
```./gradlew build```

Build web app:
```./gradlew packageJS```

Start server and web server:
```./gradlew bootRun```