# 수업 예약 시스템

## 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Data**: JPA, MySQL
- **Security**: JWT, Spring security
- **Build**: Gradle


## 주요 기능
- 회원가입/로그인(JWT 토큰 기반 인증)
- 마이페이지 조회, 수정
- 회원 탈퇴
- 강사 조회
- 수업 CURD
- 예약 관리 (예약, 취소 등)
- 예외처리 및 유효성 검사

## 프로젝트 구조 (수정필요)
src/
├── main/java/com/example/reservation
│   ├──  config
│       ├──DataInitializer
│       ├──JwtAuthenticationFilter
│       ├──JwtTokenProvider
│       ├──SecurityConfig
│       └──SwaggerConfig
│   ├── domain
│   ├── golbal
│   ├── 
│   ├── 
│   ├── 
│   └── 
└── resources/
└── application.yml


## 실행 방법 (수정필요)
### 필수 환경
-Java 17
-MySQL 8.0+

### 설치 및 실행 (수정필요)
```bash
# 1. 프로젝트 클론
git clone [repository-url]

# 2. 의존성 설치
./gradlew build

# 3. 데이터베이스 설치
# application.yml에서 MySQL 연결 정보 수정

# 4. 애플리케이션 실행
./gradlew bootRRun
```

## API 엔드포인트 (수정필요)
| 기능 | 메서드 | 엔드포인트 |
|------|--------|----------|
| 회원가입 | POST | `/api/auth/signup` |
| 로그인 | POST | `/api/auth/login` |
| 강사 조회 | GET | `/api/instructors` |
| 수업 조회 | GET | `/api/lessons` |
| 예약 생성 | POST | `/api/reservations` |
| 예약 취소 | PATCH | `/api/reservations/{id}` |


## 배운점
- REST API 설계 원칙
- JWT 토큰 기반 인증
- Spring Security 활용
- JPA와 데이터베이스 설계
- DTO를 통한 계층 분리
- 예외처리 및 @Valid를 이용한 유효성 검사
- ResponseEntity를 통한 HTTP 응답 관리
- 메서드 추출을 통한 코드 리팩토링

## 트러블슈팅
(추가할 사항이 있으면 기록)

## 향후 개선사항
- 결제 기능 추가
- 수업 후기 및 평점 시스템
- 강사 스케줄 관리