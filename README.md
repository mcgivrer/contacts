# Contacts

[![Build Status](https://travis-ci.org/mcgivrer/contacts.png)](https://travis-ci.org/mcgivrer/contacts)

## Description

Contacts is a small web application developped over [HTML5](http://www.w3.org/html/wg/drafts/html/master/ "The official specification for HTML5 markup language from W3C") + [CSS3](http://www.w3.org/TR/2001/WD-css3-roadmap-20010523/ "The official specification for CSS3 from W3C") technologie on the browser side, with the brilliant [AngularJS](http://angularjs.org "The Google MVC approach for Javascript web client")

![AngilarJS small logo ](https://bitbucket.org/McGivrer/contacts/raw/d6ca04693697ced5d36447b98eaf6267bf3868c2/docs/images/64x64/angularjs_logo.png "Official AngularJS logo") 
![HTML5 official Logo](https://bitbucket.org/McGivrer/contacts/raw/d6ca04693697ced5d36447b98eaf6267bf3868c2/docs/images/64x64/html5_logo.png "Official HTML5 logo") 
![CSS3 Official logo](https://bitbucket.org/McGivrer/contacts/raw/d6ca04693697ced5d36447b98eaf6267bf3868c2/docs/images/64x64/css3_logo.png "Custom CSS3 logo") 
![Git Official logo](https://bitbucket.org/McGivrer/contacts/raw/d6ca04693697ced5d36447b98eaf6267bf3868c2/docs/images/64x64/git_logo.png "Official Git logo")
 
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

The file `users.properties` define authorized user list to access application in the Embedded Jetty run mode.


Frédéric Delorme. 
