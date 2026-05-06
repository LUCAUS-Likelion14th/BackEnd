#!/bin/bash

echo "> lucaus 서비스 재시작"

sudo systemctl restart lucaus

echo "> 서비스 시작 대기"
sleep 20

echo "> health check 확인"

STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/health)

if [ "$STATUS" = "200" ]; then
    echo "> 애플리케이션 실행 성공"
else
    echo "> 애플리케이션 실행 실패"
    exit 1
fi