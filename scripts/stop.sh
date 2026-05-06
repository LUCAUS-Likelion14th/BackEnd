#!/bin/bash

echo "> lucaus 서비스 종료"

sudo systemctl stop lucaus || true

echo "> 종료 대기"
sleep 10

echo "> 남아있는 application.jar 프로세스 확인"

CURRENT_PID=$(pgrep -f application.jar)

if [ -z "$CURRENT_PID" ]; then
    echo "> 남아있는 프로세스 없음"
else
    echo "> 강제 종료 수행: $CURRENT_PID"
    sudo kill -9 "$CURRENT_PID"
fi