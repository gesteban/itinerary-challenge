#!/bin/bash

./gradlew clean build
cd admin-service
./gradlew bootBuildImage --imageName=challenge/admin
cd ..
cd config-service
./gradlew bootBuildImage --imageName=challenge/config
cd ..
cd gateway-service
./gradlew bootBuildImage --imageName=challenge/gateway
cd ..
cd itinerary-service
./gradlew bootBuildImage --imageName=challenge/itinerary
cd ..
cd registry-service
./gradlew bootBuildImage --imageName=challenge/registry
cd ..
cd search-service
./gradlew bootBuildImage --imageName=challenge/search
cd ..
cd uaa
docker build --tag challenge/uaa .
cd ..
