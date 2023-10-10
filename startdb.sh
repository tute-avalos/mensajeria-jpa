#!/bin/bash

docker run --name mensajeria-db \
           --rm -v /tmp/mensajeria-db:/var/lib/mysql \
           -e MYSQL_ROOT_PASSWORD=admin -e MYSQL_DATABASE=mensajeria \
           -v $PWD/db/schema.sql:/docker-entrypoint-initdb.d/schema.sql \
           -p 3306:3306 -it mysql
