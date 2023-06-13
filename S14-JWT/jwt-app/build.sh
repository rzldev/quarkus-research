#!/bin/zsh

./generateJwtKeys.sh
cd ecommerce-cart
mvn clean compile package
cd ../ecommerce-jwt
mvn clean compile package
cd ../
