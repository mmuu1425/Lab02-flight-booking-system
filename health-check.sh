#!/bin/bash
echo "Checking service health endpoints - Variant 1"

check_health() {
    local service=$1
    local port=$2
    local url="http://localhost:${port}/manage/health"

    if curl -s -f "$url" >/dev/null; then
        echo "✅ $service (port $port) is healthy"
        return 0
    else
        echo "❌ $service (port $port) is not healthy"
        return 1
    fi
}

echo "Testing Variant 1 services..."
check_health "Gateway" 8080
check_health "UserService" 8050
check_health "FlightService" 8060
check_health "BookingService" 8070

if [ $? -eq 0 ]; then
    echo "🎉 All services are healthy for Variant 1!"
    exit 0
else
    echo "💥 Some services are not healthy for Variant 1"
    exit 1
fi