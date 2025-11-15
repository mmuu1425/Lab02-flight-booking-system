@echo off
echo ====================================
echo Building Flight Booking System
echo ====================================

echo Building user-service...
cd user-service
call mvn clean package -DskipTests
cd ..

echo Building flight-service...
cd flight-service
call mvn clean package -DskipTests
cd ..

echo Building booking-service...
cd booking-service
call mvn clean package -DskipTests
cd ..

echo Building gateway...
cd gateway
call mvn clean package -DskipTests
cd ..

echo ====================================
echo Starting Docker Services...
echo ====================================
docker-compose up --build