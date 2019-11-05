#!/usr/bin/env bash

# restart docker compose for Pact broker at port 8080, see docker-compose.yml
docker-compose down
docker-compose rm
docker-compose up -d
# install
mvn clean install
# shutdown docker compose
docker-compose down
docker-compose rm