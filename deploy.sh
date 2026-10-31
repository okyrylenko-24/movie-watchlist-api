#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

if [[ ! -f .env ]]; then
    echo "Error: .env file not found."
    exit 1
fi

if ! docker info > /dev/null 2>&1; then
    echo "Error: Docker is not running."
    exit 1
fi

if ! docker volume inspect watchlist-postgres-data > /dev/null 2>&1; then
    docker volume create watchlist-postgres-data
fi

echo "Building and starting Movie Watchlist API..."
docker compose up -d --build

echo "Container status:"
docker compose ps

echo "API URL: http://localhost:8080/api/movies"
echo "pgAdmin URL: http://localhost:5050"