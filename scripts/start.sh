#!/bin/bash
# 1. 경로 설정
APP_PATH="/home/ubuntu/app"
JAR_NAME="application.jar"

echo "> 새 애플리케이션 배포"
cd "$APP_PATH" || { echo "> $APP_PATH 디렉토리가 없습니다."; exit 1; }

# 2. 실행 권한 부여
chmod +x "$JAR_NAME"

# 3. JAR 실행 (nohup을 사용하여 백그라운드 실행)
echo "> $JAR_NAME 실행"
nohup java -jar "$JAR_NAME" \
  --spring.config.import=optional:file:/home/ubuntu/app/application-secret.yml \
  > "$APP_PATH/nohup.out" 2>&1 &

# 4. 실행 확인 (선택 사항: 프로세스가 떴는지 잠시 대기 후 확인)
sleep 30
CURRENT_PID=$(pgrep -f "$JAR_NAME")
if [ -z "$CURRENT_PID" ]; then
    echo "> 애플리케이션 실행 실패!"
    exit 1
else
    echo "> 애플리케이션 실행 성공 (PID: $CURRENT_PID)"
fi

#test ver.03