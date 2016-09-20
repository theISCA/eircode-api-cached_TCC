# Eircode-api-cached #

	This project was developed to develop a docker API service to query for addresses
	based on their Eircode (Irish post code) or post code (UK) using a third party API.

	These are the two endpoints that were implemented.
    * https://developers.alliescomputing.com/postcoder-web-api/address-lookup/eircode
    * https://developers.alliescomputing.com/postcoder-web-api/address-lookup/premise

	Each call to the third party API has a cost of 4.5 credits per request. We expect
	this API being called by multiple services that all together add up to one million
	requests per month. In order to minimize the costs we need to minimize the number
	of requests to the third party API, without interfering with how the consumer
	services work.

	This project exposes an API that is compatible with and uses the third-party API,
	providing the same API options.

	It avoids repeated requests to hit the third party API. It uses Redis as an
	production grade in-memory but disk persisted cache see [Redis FAQ](http://redis.io/topics/faq)
	to allow previous requests survive on service restarts (e.g. after a new version
	of your service is deployed).

	This repository contains the proxy service code and unit tests written in Java using
	Spring Boot and other technologies further explained.

## Requirements ##

Below follows the required software. Versions used by the time of development are provided.

* Oracle JDK 8 - 1.8.0_73 - http://www.oracle.com/technetwork/pt/java/javase/downloads
* Gradle - 2.14.1 - https://gradle.org/gradle-download/
* Git - 2.9.0 - https://git-scm.com/downloads
* Docker - 1.12.0 - https://docs.docker.com/engine/installation/
* Eclipse Neon - 4.6.0 - https://eclipse.org/downloads (optional)

## Preparing Docker containers ##

Caching is done using Redis. All the request's meaningful information (request path,
request parameters and Accept request header) are considered as key for caching requests.

In order to persist previous requests (and its responses) across application and cache
service restarts you are advised to create a folder to store redis information on Docker
host. Below  ´/c/Temp/redis´ is being considered but you can change to best suit your
development or production environment.

    docker run --name redis -d --restart=always \
    --publish 6379:6379 \
    --volume /c/Temp/redis:/var/lib/redis \
    sameersbn/redis:latest --logfile /var/log/redis/redis-server.log

To check Redis log:

    docker exec -it redis tail -f /var/log/redis/redis-server.log

## Getting the software ##

    git clone https://github.com/rodrigomalara/eircode-api-cached.git

## Building it ##

    cd eircode-api-cached/eircode-api-cached

    gradle build

## Checking unit test reports ##

Open the build/reports/tests/index.html file and check test results.
There should be 15 test cases executed.

## Running ##

### On the development environment ###
    gradle bootRun

It will run the application on the localhost so you can try it.
Logs will be shown in the console.

## Testing ##

Unit tests are provided and executed during the build process (currently 15).
Reports are available at build/reports/tests/index.html (from project's home directory).

For trying it out using your own client (or internet browser) you can do the following:

Before testing make sure to go through the sections above and have both Redis and Java containers up and running

Tests can be performed using either HTML and cURL (Unix/Cygwin) and can be made against the application running on Docker or started with gradle bootRun.

>> If you are under Windows or MacOS you will need to change _localhost_ to your Docker Virtual Machine IP address (e.g. _192.168.99.100_)

The requests below are the same tests as available in Postcoder Web by the time of the development.

https://developers.alliescomputing.com/postcoder-web-api/address-lookup/premise

https://developers.alliescomputing.com/postcoder-web-api/address-lookup/eircode

Run commands below and check the output.
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=json
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=xml
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=xml
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/manor%20farm%20barns?format=xml
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=json&lines=3

Test if Accept header is being passed over to the postcoder web service (no format parameter)
* curl -i --header "Accept:application/xml" http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/manor%20farm%20barns


## Running on containers ##

### Using Already Existing docker image ###

Running the application using an already existing Image running Alpine Linux and
JDK 8 provided by frolvlad on the Docker Hub.

    docker run -d --name eircode -d \
    --publish 8080:8080 \
    --volume "$(pwd)/build/libs":/mnt \
    --workdir /mnt \
    frolvlad/alpine-oraclejdk8:slim \
    sh -c "java -jar eircode-api-cached-0.1.0.jar > output.log"

Logs will be available at project home directory /build/libs/output.log file.

### Creating your own docker image ###

Create your own docker image and storing it on your Docker Hub account, containing
the application using frolvlad's oraclejdk8:slim image as base.

    docker login

    gradle buildDocker


## Technologies ##

* Spring Boot 1.4.0
*
* JCache API (JSR 303)
* Redis Server 2.8.4