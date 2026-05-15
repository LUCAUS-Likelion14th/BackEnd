#!/bin/bash

echo "> 로그 디렉토리 생성"
mkdir -p /var/log/lucaus
chown ubuntu:ubuntu /var/log/lucaus

mkdir -p /var/log/redis
chown redis:redis /var/log/redis

echo "> redis 재시작"
sudo systemctl restart redis

echo "> lucaus 서비스 재시작"
sudo systemctl restart lucaus

echo "> health check 시작"
sleep 20

for i in {1..30}
do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/health)

  if [ "$STATUS" = "200" ]; then
    echo "> 애플리케이션 실행 성공"
    exit 0
  fi

  echo "> 아직 startup 중... ($i)"
  sleep 5
done

echo "> 애플리케이션 실행 실패"
exit 1