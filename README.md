# Eircode-api-cached #

This project was developed to develop a docker API service to query for addresses
based on their Eircode (Irish post code) or post code (UK) using a third party API.

These are the two endpoints that were implemented.
* [https://developers.alliescomputing.com/postcoder-web-api/address-lookup/eircode](https://developers.alliescomputing.com/postcoder-web-api/address-lookup/eircode)
* [https://developers.alliescomputing.com/postcoder-web-api/address-lookup/premise](https://developers.alliescomputing.com/postcoder-web-api/address-lookup/premise)

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

Although we are using redis, it is possible to switch to any other cache technology supported
by Spring Data as can be verified at [Boot Reference Caching](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html)

This repository contains the proxy service code and unit tests written in Java using
Spring Boot and other technologies further explained.

## Requirements ##

Below follows the required software. Versions used by the time of development are provided.

* Oracle JDK 8 - 1.8.0_73 - [http://www.oracle.com/technetwork/pt/java/javase/downloads](http://www.oracle.com/technetwork/pt/java/javase/downloads)
* Gradle - 2.14.1 - [https://gradle.org/gradle-download/](https://gradle.org/gradle-download/)
* Git - 2.9.0 - [https://git-scm.com/downloads](https://git-scm.com/downloads)
* Docker - 1.12.0 - [https://docs.docker.com/engine/installation/](https://docs.docker.com/engine/installation/)
* Eclipse Neon - 4.6.0 - [https://eclipse.org/downloads](https://eclipse.org/downloads) (optional)

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

Check if redis container is up. You should see something like this.

    $ docker ps -a
    CONTAINER ID        IMAGE                             COMMAND                  CREATED             STATUS                        PORTS                    NAMES
    6abc288a73cf        sameersbn/redis:latest            "/sbin/entrypoint.sh "   12 minutes ago      Up 12 minutes                 0.0.0.0:6379->6379/tcp   redis


To check Redis log files:

    docker exec -it redis tail -f /var/log/redis/redis-server.log

Without redis, unit tests will fail. Check ToDo section.

## Getting the software ##

    git clone https://github.com/rodrigomalara/eircode-api-cached.git

## Building it ##

    cd eircode-api-cached

    gradle build

## Checking unit test reports ##

If tests fail, please check the following:
1) If redis is working properly. See Redis session on how to check Redis container and log files.
2) Verify the IP address of redis container, change it on application.properties files and rebuild.

Once tests pass, open the build/reports/tests/index.html file and check test results.
There should be 15 test cases results.

## Running ##

### On the development environment ###
    gradle bootRun

It will run the application on the localhost so you can try it.
Logs will be shown in the console.

## Testing ##

Unit tests are provided and executed during the build process.
Reports are available at build/reports/tests/index.html (from project's home directory).

Once you boot the application using gradle, perform some basic tests using the commands
below and check the output.
>> If you are under Windows or MacOS you will need to change _localhost_ to your Docker Virtual Machine IP address (e.g. _192.168.99.100_)

The requests below are the same tests as available in Postcoder Web by the time of the development.

* https://developers.alliescomputing.com/postcoder-web-api/address-lookup/premise
* https://developers.alliescomputing.com/postcoder-web-api/address-lookup/eircode


* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=json
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/ie/D02X285?lines=3&format=xml
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=xml
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/manor%20farm%20barns?format=xml
* curl -i http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/NR147PZ?format=json&lines=3

Test if Accept header is being passed over to the postcoder web service (no format parameter)
* curl -i --header "Accept:application/xml" http://localhost:8080/pcw/PCW45-12345-12345-1234X/address/uk/manor%20farm%20barns

To check if responses are being stored on Redis, please install a graphican user interface client or use the
redis-cli command line tool below. The output shows that there is one record on the cache already.

    [root@localhost ~]# docker exec -it redis redis-cli
    127.0.0.1:6379> select 0
    OK
    127.0.0.1:6379> scan 0 match * count 100
    1) "0"
    2) (empty list or set)
    127.0.0.1:6379>



## Running on containers ##

For deployment please consider the following scenarios.

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

    tail -f build/libs/output.log

To check if it is running

    docker ps -a
    CONTAINER ID        IMAGE                             COMMAND                  CREATED             STATUS                        PORTS                    NAMES
    aeece05064db        frolvlad/alpine-oraclejdk8:slim   "sh -c 'ls -la build'"   4 seconds ago       Exited (0) 3 seconds ago                            eircode
To stop

    docker stop eircode

To remove it

    docker rm eircode

### Creating a new docker image ###

Create your own docker image and storing it on your Docker Hub account, containing
the application using frolvlad's oraclejdk8:slim image as base.

Before continuing

* Change the build.gradle file to contain your repository name on Docker Hub :

    group = 'your Docker Hub account name'

* Create a repository called eircode-api-cached on Docker Hub

Then perform some basic commands

    docker login -u=<user> -e=email@email.com

    gradle buildDocker

    docker images

    docker push <group>/eircode-api-cached

Then you shold have the image on Docker Hub

## Java related technologies used ##

* Spring Boot 1.4.0
* Spring 4.3.3 - modules Cache, Data, Data-Redis
* JCache API (JSR 303)
* Jedis 2.6.1
* AssertJ for unit testing
* Hamcrest JSON 0.2

## ToDo ##

1) Figure out how to disable Redis during CI unit tests.
