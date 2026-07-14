## LUCAUS 26th BackEnd

---
<img width="680" height="330" alt="image" src="https://github.com/user-attachments/assets/3ddab94c-b9a2-4aa2-a5b0-95fc79e4b9e8" />


### Summary

---

2026년 중앙대학교 봄 축제 LUCAUS 를 위한 공식 웹사이트의 백엔드입니다.

축제를 위한 통합 운영 플랫폼으로, 부스·푸드트럭·공연 정보를 실시간으로 제공하고 분실물, 공지사항, 프로모션 등 축제 참여자를 위한 편의 기능과 운영팀을 위한 관리자 기능을 지원하도록 설계되었습니다.

### 핵심기능

---

- 부스/푸드트럭/공연 관련 **운영** 정보, **실시간** 정보 기능 제공
- 공지사항/분실물/프로모션/도장판 등 **편의** 기능 제공
- 축제 운영팀을 위한 **관리자 대시보드** (공지사항, 분실물, 프로모션) 기능 제공
- **Redis 캐싱**을 통한 부스/푸드트럭 등 운영 정보 **조회 성능 개선**
- 스케줄러 기반 부스/푸드트럭 인기순위 스냅샷 자동 집계
- AWS **Auto Scaling Group**을 통한 **트래픽 대응** 및 **서버 안정성** 확보
- CodeDeploy를 통한 **Blue/Green 무중단 배포**로 서비스 중단 없는 안전한 배포 체계 구축
- **CloudWatch, EventBridge, Lambda** 기반 **실시간 모니터링 및 장애 알림** 체계 구축

### Contributors

---

<table>
  <tr>
    <td align="center"><img src="https://github.com/YoonisKim.png" width="100"/></td>
    <td align="center"><img src="https://github.com/chaeyeonlee898.png" width="100"/></td>
    <td align="center"><img src="https://github.com/dallaechoi.png" width="100"/></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/YoonisKim">윤형</a></td>
    <td align="center"><a href="https://github.com/chaeyeonlee898">채연</a></td>
    <td align="center"><a href="https://github.com/dallaechoi">서영</a></td>
  </tr>
  <tr>
    <td valign="top">
      • 푸드트럭/공지 API 설계 및 개발<br/>
      • JWT 기반 인증/인가 시스템 구축<br/>
      • Auto Scaling Group 기반 EC2 인프라 설계 및 구축 (Blue/Green 배포 대응)<br/>
      • Redis를 활용한 캐싱 시스템 구축으로 애플리케이션 응답 속도 개선
    </td>
    <td valign="top">
      • 공연/프로모션 API 설계 및 개발<br/>
      • Route 53, ACM, WAF를 활용한 도메인 연결 및 웹 보안 체계 구축<br/>
      • CloudWatch, EventBridge, Lambda 기반 모니터링 및 Slack 알림 시스템 구축
    </td>
    <td valign="top">
      • 부스/도장판/분실물 API 설계 및 개발<br/>
      • GitHub Actions와 AWS CodeDeploy를 활용한 CI/CD 파이프라인 구축 (Blue/Green 무중단 배포 적용)
    </td>
  </tr>
</table>

### Architecture

---

![아키텍처](https://github.com/user-attachments/assets/f08c936d-bd21-4a13-be45-bc8cb9b5d8d1)

### 아키텍처 설명

---

**Client Request Flow (Traffic & Networking)**

`Clients → Route 53 → ACM → WAF → ELB`

클라이언트 요청은 Route 53을 통해 도메인으로 라우팅되며, ACM에서 발급된 인증서를 통해 HTTPS 통신이 적용됩니다. WAF가 SQL Injection, XSS 등 웹 공격을 사전에 차단한 뒤, 정상 트래픽만 ELB로 전달되어 백엔드로 분산됩니다. 이를 통해 보안성과 안정적인 트래픽 분산을 동시에 확보했습니다.

**CI/CD Pipeline**

`Developer → GitHub → GitHub Actions → S3 (deployment) → CodeDeploy`

GitHub Actions로 빌드된 애플리케이션은 S3에 업로드된 후, CodeDeploy를 통해 Auto Scaling Group에 Blue/Green 방식으로 배포됩니다. 새로운 버전(Green)이 기존 버전(Blue)과 함께 배포된 후 트래픽이 전환되며, 문제 발생 시 기존 ASG로 즉시 롤백이 가능합니다. 이를 통해 무중단 배포와 안전한 배포 전략을 동시에 확보했습니다.

**Compute & Application**

`ELB → ASG (EC2 + Spring Boot + Redis, Blue/Green)`

ELB로부터 전달받은 트래픽은 Auto Scaling Group 내 EC2 인스턴스에서 처리되며, 각 인스턴스는 Spring Boot 애플리케이션과 Redis를 함께 구성해 캐싱을 통한 응답 속도 개선을 도모했습니다. Blue/Green 배포 구조를 위해 ASG는 최대 2개까지 확장 가능하도록 구성했습니다.

**Data Storage**

`ASG ↔ RDS, ASG ↔ S3 (image)`

애플리케이션은 Amazon RDS를 통해 정형 데이터를 저장 및 조회하며, 이미지와 같은 정적 파일은 별도의 S3 버킷에 저장하여 관리합니다. 이를 통해 데이터베이스 부하를 줄이고 스토리지 자원을 목적에 맞게 분리 운영했습니다.

**Monitoring & Alerting**

`ASG → CloudWatch → EventBridge → Lambda → Slack`

CloudWatch를 통해 RDS, EC2 인스턴스의 지표와 로그를 실시간으로 수집하며, 특정 이벤트 발생 시 EventBridge가 이를 감지해 Lambda를 트리거합니다. Lambda는 알림 메시지를 가공하여 Slack으로 전송함으로써, 장애나 이상 상황을 신속하게 팀에 공유할 수 있는 체계를 구축했습니다.

**ETC**

### 폴더 구조

---

```
BackEnd/
├── .github/                          # GitHub 이슈/PR 템플릿 & Actions 설정
│   ├── workflows/
│   │   ├── cicd.yml                  # CI/CD 파이프라인 정의
│   │   └── auto_assign.yml
│   └── pull_request_template.md
├── gradle/
│   └── wrapper/                      # Gradle Wrapper 관련 파일
├── scripts/                          # CodeDeploy 배포 스크립트
│   ├── start.sh
│   └── stop.sh
├── src/
│   ├── main/
│   │   ├── java/com/example/lucaus26th/
│   │   │   ├── config/               # Security, S3, Swagger, Cache 등 설정
│   │   │   ├── controller/           # REST API 컨트롤러 (booth, foodTruck, lost, notice, stage, stamp, logs)
│   │   │   ├── domain/                # Entity 클래스 (도메인별 하위 패키지)
│   │   │   ├── dto/
│   │   │   │   ├── request/          # 도메인별 Request DTO
│   │   │   │   └── response/         # 도메인별 Response DTO
│   │   │   ├── enums/                # 열거형 상수
│   │   │   ├── global/               # 공통 예외 처리, API 응답, 로깅 필터
│   │   │   │   ├── api/
│   │   │   │   └── exception/
│   │   │   ├── jwt/                  # JWT 기반 인증/토큰 처리
│   │   │   ├── repository/           # JPA Repository (도메인별 하위 패키지)
│   │   │   ├── scheduler/            # 랭킹 스냅샷 등 스케줄링 작업
│   │   │   ├── security/             # Custom UserDetails
│   │   │   └── service/              # 비즈니스 로직 (도메인별 하위 패키지)
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application.properties
│   └── test/                          # 테스트 코드
├── appspec.yml                        # AWS CodeDeploy 설정 파일
├── build.gradle
├── gradlew / gradlew.bat
├── settings.gradle
└── README.md
```

### ERD


---

![부스&도장판 ERD](https://github.com/user-attachments/assets/e7ff91e6-472e-4120-9cfa-98106c9f9465)

![공연 ERD](https://github.com/user-attachments/assets/54fefe61-a35a-4fe6-b41e-e14966a03633)

![편의 ERD](https://github.com/user-attachments/assets/0a3a9b79-bfbc-4f54-b9c0-acc480cdbb4f)

![푸드트럭 ERD](https://github.com/user-attachments/assets/76659864-2fe6-4c3f-bf85-9cc28b685f8e)

### Teck Stack

![Java](https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SPRING%20BOOT-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Redis](https://img.shields.io/badge/REDIS-DC382D?style=for-the-badge&logo=redis&logoColor=white)

![Amazon RDS](https://img.shields.io/badge/AMAZON%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)

![Amazon AWS](https://img.shields.io/badge/AMAZON%20AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![EC2](https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![S3](https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![CodeDeploy](https://img.shields.io/badge/CODEDEPLOY-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Auto Scaling](https://img.shields.io/badge/AUTO%20SCALING-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![ELB](https://img.shields.io/badge/ELB-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![WAF](https://img.shields.io/badge/WAF-DD344C?style=for-the-badge&logo=amazonaws&logoColor=white)
![ACM](https://img.shields.io/badge/ACM-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Route 53](https://img.shields.io/badge/ROUTE%2053-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white)

![GitHub Actions](https://img.shields.io/badge/GITHUB%20ACTIONS-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Git](https://img.shields.io/badge/GIT-F05032?style=for-the-badge&logo=git&logoColor=white)

![CloudWatch](https://img.shields.io/badge/CLOUDWATCH-FF4F8B?style=for-the-badge&logo=amazoncloudwatch&logoColor=white)
![EventBridge](https://img.shields.io/badge/EVENTBRIDGE-FF4F8B?style=for-the-badge&logo=amazonaws&logoColor=white)
![Lambda](https://img.shields.io/badge/LAMBDA-FF9900?style=for-the-badge&logo=awslambda&logoColor=white)
![Slack](https://img.shields.io/badge/SLACK-4A154B?style=for-the-badge&logo=slack&logoColor=white)

---

**Backend**

---

| SpringBoot | Application Server |
| --- | --- |
| Redis | In-Memory Caching |


**Infrastructure / AWS**

---

| EC2 | Application Hosting |
| --- | --- |
| Auto Scaling Group | Instance Scaling (max 2) |
| Elastic Load Balancer | Traffic Distribution |
| RDS | Relational Database |
| S3 | Deployment Artifacts & Image storage & Logs |
| CodeDeploy | Blue/Green Deployment |
| WAF | Web Application Firewall |
| Certificate Manager | SSL/TLS Certificate Management |
| Route 53 | DNS management |


**CI/CD**

---

| Github Actions | Build & Deployment Automation |
| --- | --- |
| CodeDeploy | Blue/Green Deployment Strategy |


**Monitoring & Alerting**

---

| Amazon CloudWatch | Metrics & Log Monitoring |
| --- | --- |
| Amazon EventBridge | Event-Driven Triggers |
| AWS Lambda | Alert Processing |
| Slack | Deployment/Alert Notifications |


### Notes

---
