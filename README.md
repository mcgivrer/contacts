Contacts
========

A small web application REST artifact, based on JPA, Jersey and Jetty server as a demonstration 
concept on REST and HTML5+CSS3+AngularJS UI application usage.

To build a specific environment version, just follow the next command line:

 mvn -P local clean package

The file @users.properties@ define authorized user list to access application in the Embedded Jetty run mode.

specific env configuration are stored into the assemble/env directory. Each subdirectory expose environment contextual files.


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



Frédéric Delorme. 
