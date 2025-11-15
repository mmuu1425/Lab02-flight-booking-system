#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- 创建用户服务数据库
    CREATE DATABASE user_db;

    -- 创建航班服务数据库
    CREATE DATABASE flight_db;

    -- 创建预订服务数据库
    CREATE DATABASE booking_db;

    -- 授予权限
    GRANT ALL PRIVILEGES ON DATABASE user_db TO program;
    GRANT ALL PRIVILEGES ON DATABASE flight_db TO program;
    GRANT ALL PRIVILEGES ON DATABASE booking_db TO program;
EOSQL