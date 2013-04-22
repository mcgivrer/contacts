# Contacts home page

[![Build Status](https://travis-ci.org/mcgivrer/contacts.png)](https://travis-ci.org/mcgivrer/contacts)

[Author](/wiki/Author.textile)

## Description

Contacts is a small web application developped over [HTML5](http://www.w3.org/html/wg/drafts/html/master/ "The official specification for HTML5 markup language from W3C") + [CSS3](http://www.w3.org/TR/2001/WD-css3-roadmap-20010523/ "The official specification for CSS3 from W3C") technologie on the browser side, with the brilliant [AngularJS](http://angularjs.org "The Google MVC approach for Javascript web client").

All this stuff is develop uppon the [JAX-RS](http://jcp.org/en/jsr/detail?id=311 "Official Java Community Process for JSR-311, the Jax-RS specification") (Restfull) technology with [Java](http://www.oracle.com/technetwork/java/javaee/downloads/java-ee-sdk-6u3-jdk-7u1-downloads-523391.html "Official download page for the multi-platform Java EE 6 (u4) SDK and JDK 7").

---

![AngilarJS small logo ](images/64x64/angularjs_logo.png "Official AngularJS logo") 
![HTML5 official Logo](images/64x64/html5_logo.png "Official HTML5 logo") 
![CSS3 Official logo](images/64x64/css3_logo.png "Custom CSS3 logo") 
![Git Official logo](images/64x64/git_logo.png "Official Git logo")
![Java logo](images/64x64/java_logo.png "Java logo")
![Official Hibernate logo](images/64x64/hibernate_logo.png "Official Hibernate logo")

---

## Context

A small web application REST artifact, based on JPA, Jersey and Jetty server as a demonstration 
concept on REST and HTML5+CSS3+AngularJS UI application usage.

## Database

The default configuration for this project is using a HSQLDB database to store data in a file persistence system (`db/contacts`). 

Unit test (Junit) use a test instance of the same schema databse (`db/test-contacts`).

Configuration of the instance, based on the JPA2 hibernate engine are stored in the standard persistence.xml files, in the 2 places as `src/main/resources/META-INF` for the appliation in run mode, and in the `src/test/resources/META-INF` for Unit testing.

A specific functionality implemented in each DAO (`GenericDAO.loadFromYml(Sgtring filename)`) will load some intiialisation data from `src/test/resources/data/*.yml` where `*` is the corresponding entity.
_e.g.:_  for the `Contact` entity, a contact.yml file would be provided.

## Maven 

The application is built with Maven, and try to use all (at least a maximum of) fantastic functionalities offered by such tool and its plugins.

* [maven-compiler-plugin](http://maven.apache.org/plugins/maven-compiler-plugin/ "Official Apache Maven Compiler plugin")
* [maven-resource-plugin](http://maven.apache.org/plugins/maven-resources-plugin/ "Official Apache Maven Resources plugin")
* [maven-javadoc-plugin](http://maven.apache.org/plugins/maven-javadoc-plugin/ "Official Apache Maven Javadoc plugin")
* [maven-source-plugin](http://maven.apache.org/plugins/maven-source-plugin/ "Official Apache Maven Source plugin")
* [maven-war-plugin](http://maven.apache.org/plugins/maven-war-plugin/ "Official Apache Maven plugin")
* [maven-assembly-plugin](http://maven.apache.org/plugins/maven-assembly-plugin/ "Apache maven assembly plugin")
* [maven-jetty-plugin](http://wiki.eclipse.org/Jetty/Feature/Jetty_Maven_Plugin "Official Eclipse maven-jetty-plugin")
* [keytool-maven-plugin](http://mojo.codehaus.org/keytool/keytool-maven-plugin/ "Mojo Introduction about KeyTool maven plugin")

This is a non exaustive list of (very to very very) usefull maven plugins.

### Assembly

You can define some specific environments configuration  stored into the `assemble/env` directory. Each subdirectory expose contextual environment files. This magic is due to [`maven-assembly-plugin`](http://maven.apache.org/plugins/maven-assembly-plugin/ "see more ..."). 

See sample folder tree belllow :

```
    assemble/
      env/           # environment filder
        local/       # Local development folder (developer workstation)
      	  WEB-INF/
      	  lib/
          jpa/
          log4j/
        dev/         # Development server environment
          ...
        int/         # Integration server environment
          ...
        prod/        # Production server environment 
          ...
```

### Server Jetty

The [`maven-jetty-plugin`](http://wiki.eclipse.org/Jetty/Feature/Jetty_Maven_Plugin "Visit Eclipse official site to discover more about jetty ...") is used to run and configure an embedded Java server (jetty :) to start this web app, and will run soon some functional test through Jasmine (Javascript functional testing framework) 

To start through Maven this configured serveur, just execute the following command line:

```bash
 $ mvn -P [environment] clean install jetty:run
```

where `[environment]` is one of the directory in the `assemble/env/` path.

e.g.:

```bash
 $ mvn -P local clean install jetty:run
```

So the jetty server will start after cleaning, building, installing and packaging of the resulting webapp.

To stop the running server, just input a `CTRL`+`P` in the terminal window. Jetty server will stop soon.

#### Users ?

A basic secutiry realm is configured on the jetty plugin, so the file `users.properties` define authorized users listand there password and profiles to access application in the Embedded Jetty run mode.

The Jetty configuration part is :

```xml
  <userRealms>
    <userRealm implementation="org.mortbay.jetty.security.HashUserRealm">
      <name>MyRealm</name>
      <config>${project.build.directory}/classes/users.properties</config>
    </userRealm>
  </userRealms>
```
The users.properties file structure  follow the bellow sample line :

```properties
username: password[,rolename ...]
```

where :

* *`username`* is the username,
* *`password`* the password,
* *`rolename`* one of the linked profile


### SSL &amp; Jetty

An SSL support is activated and a default ssl certificate is automatically generated (see [`keytool-maven-plugin`](http://mojo.codehaus.org/keytool/keytool-maven-plugin/ "Learn more certificate generation (and more) plugin ...")) on each maven-jetty-plugin execution. A keystore is generate in the `${project.build.directory}/jetty-ssl.keystore` file. The certificate would be uploaded to browser to authorize user to pars in a SSL securized maner the webapp Contacts.

The keytool-maven-plugin configuration for such a SSL certificate generation is:

```xml
  <plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>keytool-maven-plugin</artifactId>
    <version>1.2</version>
    <executions>
      <execution>
        <phase>generate-resources</phase>
        <id>clean</id>
        <goals>
          <goal>clean</goal>
        </goals>
      </execution>
      <execution>
        <phase>generate-resources</phase>
        <id>genkey</id>
        <goals>
          <goal>genkey</goal>
        </goals>
      </execution>
    </executions>
    <configuration>
      <keystore>${project.build.directory}/jetty-ssl.keystore</keystore>
      <dname>cn=www.domain.com</dname>
      <keypass>jetty6</keypass>
      <storepass>jetty6</storepass>
      <alias>jetty6</alias>
      <keyalg>RSA</keyalg>
    </configuration>
  </plugin>
```

The Jetty keystore configuration part is:

```xml
  <plugin>
    <groupId>org.mortbay.jetty</groupId>
    <artifactId>jetty-maven-plugin</artifactId>
    <version>8.0.4.v20111024</version>
    <configuration>
      ...
      <keystore>${project.build.directory}/jetty-ssl.keystore</keystore>
      <dname>cn=www.domain.com</dname>
      <keypass>jetty6</keypass>
      <storepass>jetty6</storepass>
      <alias>jetty6</alias>
      <keyalg>RSA</keyalg>
      ...
    </configuration>
    ...
  </plugin>
```
The Jetty SSL configuration connector is:

```xml
  <connectors>
    <connector implementation="org.eclipse.jetty.server.nio.SelectChannelConnector">
      <port>8080</port>
      <maxIdleTime>60000</maxIdleTime>
    </connector>
    <connector implementation="org.eclipse.jetty.server.ssl.SslSocketConnector">
      <port>8443</port>
      <maxIdleTime>60000</maxIdleTime>
      <keystore>${project.build.directory}/jetty-ssl.keystore</keystore>
      <password>jetty6</password>
      <keyPassword>jetty6</keyPassword>
    </connector>
  </connectors>
```

### Tomcat 7

An attempt to configure a Tomcat 7 server to run our project from `pom.xml` is driven with the [`tomcat7-maven-plugin`](http://tomcat.apache.org/maven-plugin-2.0/index.html "Official page for the Tomcat 7 maven plugin").

See the xml extract bellow to start on this subject:

```xml
  <plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.1</version>
    <configuration>
      <tomcatUsers>${project.build.directory}/classes/users.properties</tomcatUsers>
    </configuration>
  </plugin>
```

>**Note**: *the `users.properties` file used to declare users have the same format as the Jetty Realm user declaration file. The file is declared with the `<tomcatUsers/>` attribute into the standard `<configuration/>` tag.*


## Coming Soon ...

Some usefull functionalities and process would be added soon. Here is tha roadmap list :

* Javascript Functional testing,
* JUnit executed on Jetty test configuration.

Have fun !

Frédéric Delorme. 
