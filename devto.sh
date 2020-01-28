#!/usr/bin/env bash

project=$(basename "$PWD")
branch=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')
sed "s/\.\/images/https:\/\/raw.githubusercontent.com\/eon-com\/$project\/$branch\/images/" README.md | tail -n +3 > README_DEVTO.md