#!/usr/bin/env bash

export IMAGE_NAME=$1
docker-compose -f docker-compose.yml up --detach
echo "Success"