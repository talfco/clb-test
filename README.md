## Overview

The Cloudburo Test Utilities Library 'clb-test' provides a simple framework to load Test Data from a Excel CSV into your persistens storage layer.

Currently the following persistent storage layers are supported.

* NonSQL Mongo DB
* Any persistent storage which provides are RESTful API, as described in in [http://cloudburo.github.com/docs/opensource/clb-modelloader/](http://cloudburo.github.com/docs/opensource/clb-modelloader)

It based on the following open-source frameworks/libraries

* __testng__: estNG is a testing framework inspired from JUnit and NUnit but introducing some new functionalities that make it more powerful and easier to use,
* __com.google.gson__: Gson is a Java library that can be used to convert Java Objects into their JSON representation
* __org.joda-time__: Joda-Time provides a quality replacement for the Java date and time classes
* __org.apache.httpcomponents__: A toolset of low level Java components focused on HTTP and associated protocols. 
* __net.sf.supercsv__: Super CSV is to be the foremost, fastest, and most programmer-friendly, free CSV package for Java.

When executing

      mvn test

The Test driver will load the testng xml configuration file from the `testconfig` and loads the `testdata` into the configured persistency backend (either Google App Engine or Mongo DB) by using the Java test data objects defined in `/test/com/cloudburo/test`.

The current sample definitions/testdata will work in combination with the GoogleApp Engine Java Sample Skeleton which is available in [clb-appEngineTemplate](https://github.com/talfco/clb-appEngineTemplate)

This is an early alpha version, more to follow soon...


## Maven Repository ##

Add the following entry to your POM:

    <repositories>
        <repository>  
            <id>cloudburo-clb-test-repo</id>  
            <name>Cloudburo on GitHub</name>  
            <url>http://talfco.github.io/clb-test/repository</url>   
        </repository>
    </repositories>

And the following dependency

	<dependency>
		<groupId>com.cloudburo</groupId>
		  <artifactId>clb-test</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>  

## Documentation

For a detailed documentation refer to [http://cloudburo.github.com/docs/opensource/clb-test/](http://cloudburo.github.com/docs/opensource/clb-test) - is under construction

