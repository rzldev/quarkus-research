#!/bin/bash

clear
docker-compose -f docker-compose-infinispan.yml down 
docker volume rm $(docker volume ls)
docker-compose -f docker-compose-infinispan.yml up -d
#mvn clean quarkus:dev
