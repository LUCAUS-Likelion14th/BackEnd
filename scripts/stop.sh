#!/bin/bash

# 1. 기존 systemd 서비스 중단 시도 (기존 설정이 남아있을 경우를 대비)
echo "> 기존 systemd 서비스 중단 시도"
sudo systemctl stop lucaus || true

# 2. 8080 포트를 사용 중인 프로세스 ID(PID) 확인
echo "> 8080 포트 점유 프로세스 확인"
CURRENT_PID=$(pgrep -f application.jar)

# 3. 프로세스가 존재하면 강제 종료
if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 실행 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> 현재 실행 중인 애플리케이션(PID: $CURRENT_PID)을 강제 종료합니다."
    sudo kill -9 "$CURRENT_PID"
    # 프로세스가 완전히 종료될 때까지 잠시 대기
    sleep 5
fi