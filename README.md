OpenTrapp
=========

[![Build Status](https://secure.travis-ci.org/Pragmatists/OpenTrapp.png)](http://travis-ci.org/Pragmatists/OpenTrapp)

Open Time Registration Application

## In order to build:
```
  gradle clean test
```
Until https://github.com/Pragmatists/OpenTrapp/issues/73 is not fixed, use Java 7 for building project.
## Starting server:
```
  gradle jettyRun -Dspring.profiles.active={profilesToActivate}
```
### Available profiles:
 * ```transients``` - use in memory transient persistence (fastest, all data is lost after server restart),
 * ```mongo``` - use mongo persistence,
 * ```mongo-lab``` - connect to mongolab database (requires ```mongo``` profile),
 * ```mongo-dev``` - connect to embedded mongo database (requires ```mongo``` profile, all data is lost after server restart),
 * ```mock-security``` - every request is authenticated as mock user (homer.simpson),
 * ```spring-security``` - authentication against Google OpenID authentication provider,
 * ```demo``` - populate database with sample data.
 
### Examples:
 * ```gradle jettyRun -Dspring.profiles.active=transients,security,mock-security,demo``` - mock mode
 * ```gradle jettyRun -Dspring.profiles.active=mongo,mongo-lab,security,spring-security,google-security``` - production mode
 
## REST API executable specification:
[http://pragmatists.github.io/OpenTrapp](http://pragmatists.github.io/OpenTrapp)
