#!/usr/bin/env bash

docker run --name my-postgis -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgis/postgis:14-3.2-alpine


