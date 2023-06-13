#!/bin/zsh
build=$1
if [ "$build" = "build" ]; then
    ./build.sh
fi
docker compose up -d
