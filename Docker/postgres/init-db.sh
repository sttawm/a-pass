#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER $DB_USERNAME WITH ENCRYPTED PASSWORD '$DB_PASSWORD';
    CREATE DATABASE $DB_DATABASE;
    GRANT ALL PRIVILEGES ON DATABASE $DB_DATABASE TO $DB_USERNAME;
EOSQL
