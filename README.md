# KickAlert

KickAlert는 멀티 모듈로 구성된 스프링 부트 프로젝트 입니다.
모바일 앱으로 실시간 축구 알림 및 기타 관련 기능을 제공합니다.

## 프로젝트 구조

프로젝트는 다음과 같은 모듈로 구성되어 있습니다:

- `api-module`: API 서버 모듈
- `batch-module`: 배치 처리 모듈
- `core-module`: 공통 코어 모듈

## 기술 스택

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- QueryDSL
- MariaDB / MySQL
- WebFlux
- Firebase Admin
- Lombok
- H2 Database (테스트용)

## 개발 환경 설정

### 필수 요구사항

- JDK 17
- Gradle 7.x 이상
- MariaDB/MySQL

### 빌드 및 실행

프로젝트 빌드:
```bash
./gradlew clean build
```

각 모듈 실행:
```bash
# API 모듈 실행
./gradlew :api-module:bootRun

# Batch 모듈 실행
./gradlew :batch-module:bootRun
```

## 데이터베이스 설정

프로젝트는 MariaDB/MySQL을 사용합니다. 개발 환경에서는 다음과 같이 설정하세요:

1. MariaDB/MySQL 서버 실행
2. 데이터베이스 생성
3. application.yml 파일에서 데이터베이스 연결 정보 설정

## 테스트

테스트 실행:
```bash
./gradlew test
```

## Docker 배포

Dockerfile이 제공되어 있어 Docker 컨테이너로 배포가 가능합니다:

```bash
# Docker 이미지 빌드
docker build -t kickalert .

# Docker 컨테이너 실행
docker run -p 8080:8080 kickalert
```
