#!/usr/bin/env bash

# restart docker compose for Pact broker at port 8080, see docker-compose.yml
docker-compose down
docker-compose rm
docker-compose up -d
# install parent POM
mvn install -N
# install common jar
(cd ./common && exec mvn clean install)
# test and publish customer service Pact
(cd ./customer-service && exec mvn clean test pact:publish)
# test and publich creditcheck Pact
(cd ./creditcheck && exec mvn clean test pact:publish)
# verify Pact against real service
(cd ./core-data-service && exec mvn clean test pact:verify)