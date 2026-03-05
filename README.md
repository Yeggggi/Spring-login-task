# Spring Boot 로그인 시스템

Spring Boot와 PostgreSQL을 활용한 세션 기반 로그인 구현 과제입니다.

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Backend | Spring Boot 3, Spring MVC |
| View | Thymeleaf, Bootstrap 5.3.3 |
| Database | PostgreSQL |
| Security | BCryptPasswordEncoder |
| ORM | Spring Data JPA (Hibernate) |
| Build | Gradle |

---

## 주요 기능

- 이메일 + 비밀번호 로그인
- BCrypt를 이용한 비밀번호 해시 저장
- 세션 기반 인증 (HttpSession)
- 로그인 실패 시 에러 메시지 표시 (이메일 미존재 / 비밀번호 불일치 구분)
- 로그아웃 시 세션 무효화
- 마지막 로그인 시각 자동 업데이트

---

## 프로젝트 구조

```
src/main/java/com/example/login_spring/
├── config/
│   └── SecurityConfig.java          # BCryptPasswordEncoder Bean 등록
├── controller/
│   └── AuthController.java          # 로그인 / 홈 / 로그아웃 엔드포인트
├── domain/
│   └── User.java                    # 사용자 엔티티 (UUID PK)
├── repository/
│   └── UserRepository.java          # JPA Repository
├── service/
│   └── UserService.java             # 회원가입 / 인증 비즈니스 로직
└── LoginSpringApplication.java

src/main/resources/
├── templates/
│   ├── login.html                   # 로그인 페이지
│   └── home.html                    # 홈 페이지
├── application.yml                  # 공개 설정 (템플릿)
├── application-local.yml            # 로컬 DB 설정 (gitignore 처리)
└── data.sql                         # 초기 테스트 데이터
```

---

## API 엔드포인트

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/login` | 로그인 페이지 |
| POST | `/login` | 로그인 처리 |
| GET | `/` | 홈 페이지 (로그인 후) |
| GET | `/logout` | 로그아웃 |

---

## DB 테이블 구조

**users**

| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | UUID | 기본 키 |
| email | VARCHAR | 이메일 (unique) |
| password_hash | VARCHAR | BCrypt 해시된 비밀번호 |
| created_at | TIMESTAMP | 가입 일시 |
| last_login_at | TIMESTAMP | 마지막 로그인 일시 |

---

## 실행 방법

### 1. 사전 요구사항

- Java 17 이상
- PostgreSQL 실행 중
- `login_db` 데이터베이스 생성

```sql
CREATE DATABASE login_db;
```

### 2. 환경 설정

`src/main/resources/application-local.yml` 파일을 생성하고 실제 DB 정보를 입력합니다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/login_db
    username: 실제_DB_유저명
    password: 실제_DB_비밀번호
```

> `application-local.yml`은 `.gitignore`에 등록되어 있어 GitHub에 업로드되지 않습니다.

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

### 4. 접속

브라우저에서 [http://localhost:8080/login](http://localhost:8080/login) 접속

---

## 테스트 계정

`data.sql`에 초기 테스트 계정이 등록되어 있습니다.

| 이메일 | 비밀번호 |
|--------|----------|
| test@test.com | test1234!@ |

---

## 보안 고려사항

- 비밀번호는 BCrypt로 해시 처리하여 DB에 저장 (평문 저장 없음)
- DB 접속 정보는 `application-local.yml`로 분리하여 Git에 노출되지 않도록 처리
- 세션 무효화를 통한 로그아웃 처리
