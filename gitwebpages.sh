#!/bin/bash

echo "Hello!"

git config --global user.email "Elix-x@users.noreply.github.com"
git config --global user.name "Travis-Maven"

git clone https://Elix-x:$GITACCESSTOKEN@github.com/Elix-x/Elix-x.github.io.git

mkdir -p "Elix-x.github.io/maven2/code/elix_x/excomms/EXComms/$TRAVIS_BRANCH/"
cp -r "./build/libs/." "./Elix-x.github.io/maven2/code/elix_x/excomms/EXComms/$TRAVIS_BRANCH/"

cd "Elix-x.github.io"

git add *
git commit -m "Uploading maven artifacts for EXComms for $TRAVIS_BRANCH"
git push

echo "Done!"

cd ./..
rm -rf "Elix-x.github.io"