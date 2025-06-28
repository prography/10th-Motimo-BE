#!/bin/bash

# 네트워크 생성
docker network inspect my-network >/dev/null 2>&1 || docker network create my-network

# postgres 컨테이너 실행
if [ "$(docker ps -aq -f name=my-postgres)" ]; then
  echo "Restarting existing my-postgres container..."
  docker restart my-postgres
else
  echo "Starting new my-postgres container..."
  docker run -d \
    --name my-postgres \
    --network my-network \
    -e POSTGRES_PASSWORD=mysecretpassword \
    -p 5432:5432 \
    postgres:latest
fi

docker run -d \
  --name my-postgres \
  --network my-network \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -p 5432:5432 \
  postgres:latest

# 이전 motimo-local 컨테이너 제거
if [ "$(docker ps -aq -f name=motimo-local)" ]; then
  echo "Removing existing motimo-local container..."
  docker rm -f motimo-local
fi

# Dockerfile-local로 이미지 빌드
echo "Building motimo-local image..."
docker build -f Dockerfile-local -t motimo-local .

# 컨테이너 실행
echo "Starting motimo-local container..."
docker run -d \
  --name motimo-local \
  --network my-network \
  --env-file .env \
  -p 8080:8080 \
  motimo-local
