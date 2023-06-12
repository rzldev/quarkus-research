#!/bin/bash

f=$1
docker-compose -f docker-compose-infinispan.yml down && docker-compose -f docker-compose-infinispan.yml up -d
docker cp ./infinispan/*.jar infinispan:/opt/infinispan/server/lib/
docker exec -it infinispan sh -c "ls /opt/infinispan/server/lib/"
