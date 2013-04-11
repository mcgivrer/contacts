# Contacts [![Build Status](https://travis-ci.org/mcgivrer/contacts.png)](https://travis-ci.org/mcgivrer/contacts)

## Context

A small web application REST artifact, based on JPA, Jersey and Jetty server as a demonstration 
concept on REST and HTML5+CSS3+AngularJS UI application usage.

## Maven Assembly

specific env configuration are stored into the `assemble/env` directory. Each subdirectory expose environment contextual files.


    assemble/
      env/
        local/
      	  WEB-INF/
      	  lib/
          jpa/
          log4j/
        dev/
          ...
        int/
          ...
        prod/
          ...


## Server Jetty

to start this web app with the embedded Jetty server, just execute the following command line:

    mvn -P [environment] clean install jetty:run

where `[environment]` is one of the directory in the `assemble/env/` path.

## Users ?

The file `users.properties`define authorized user list to access application in the Embedded Jetty run mode.


Frédéric Delorme. 
