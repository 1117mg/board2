# **Spring Boot 게시판 만들기**

**Spring Boot, MyBatis, Spring Security, OAuth를 사용한 게시판 CRUD 시스템. AWS EC2를 이용한 배포.**

---

## **목차**
1. [프로젝트 개요](#프로젝트-개요)
2. [주요 기능](#주요-기능)
3. [기술 사양](#기술-사양)
4. [엔드포인트](#엔드포인트)
   - [관리자 페이지](#관리자-페이지)
   - [회원 페이지](#회원-페이지)
5. [설치 및 실행 방법](#설치-및-실행-방법)
6. [배포](#배포)

---

## **프로젝트 개요**

이 프로젝트는 게시판 CRUD 시스템을 기반으로 관리자와 회원의 기능을 분리한 개인 실습 프로젝트입니다. 주요 특징은 다음과 같습니다:

- 관리자와 회원을 위한 별도 페이지와 권한 관리.
- **Spring Security**와 **OAuth**를 활용한 인증 및 인가.
- **MySQL**과의 연동을 통해 데이터 영구 저장.
- **AWS EC2**의 Amazon Linux 2 환경에 배포.

---

## **주요 기능**

### **관리자 페이지**
- 사용자 및 게시글 관리.
- 관리자 전용 경로 제공.
- `/master/auth/login` 경로를 통해 인증 및 로그인.

### **회원 페이지**
- 게시글 열람 및 상호작용.
- `/front/auth/login` 경로를 통해 인증 및 로그인.

### **OAuth 통합**
- OAuth 기반 로그인 시스템으로 보안 강화.
- **Spring Security**를 활용한 역할 기반 접근 제어(Role-Based Access Control).

### **CRUD 기능**
- 게시판(Board), 사용자(User), 게시글(Post)에 대한 CRUD 기능 제공.

---

## **기술 사양**

- **언어**: Java
- **프레임워크**: Spring Boot `2.3.1.RELEASE`
- **ORM**: MyBatis `2.1.4`
- **빌드 도구**: Maven
- **데이터베이스**: MySQL `8.0.24`
- **보안**: Spring Security `5.6.2` (OAuth 통합)
- **IDE**: IntelliJ IDEA
- **JDK**: Java SE Development Kit `1.8`
- **배포 환경**: AWS EC2 (Amazon Linux 2)

---

## **엔드포인트**

### **관리자 페이지**

| 메서드 | 엔드포인트                              | 설명                          |
|--------|-----------------------------------------|-------------------------------|
| `POST` | `/master/auth/login`                   | 관리자 로그인                 |
| `POST` | `/master/auth/join`                    | 관리자 회원가입               |
| `GET`  | `/master/main`                         | 관리자 메인 페이지            |
| `GET`  | `/master/users`                        | 사용자 목록 조회              |
| `GET`  | `/master/user/{userId}`                | 특정 사용자 상세 정보 조회    |
| `GET`  | `/master/board/{boardIdx}/posts`       | 특정 게시판의 게시글 목록 조회|
| `GET`  | `/master/board/{boardIdx}/post/{postId}` | 특정 게시글 조회             |

### **회원 페이지**

| 메서드 | 엔드포인트                              | 설명                          |
|--------|-----------------------------------------|-------------------------------|
| `POST` | `/front/auth/login`                    | 회원 로그인                   |
| `POST` | `/front/auth/join`                     | 회원가입                      |
| `GET`  | `/front/main`                          | 회원 메인 페이지              |
| `GET`  | `/front/users`                         | 사용자 목록 조회              |
| `GET`  | `/front/user/{userId}`                 | 특정 사용자 상세 정보 조회    |
| `GET`  | `/front/board/{boardIdx}/posts`        | 특정 게시판의 게시글 목록 조회|
| `GET`  | `/front/board/{boardIdx}/post/{postId}` | 특정 게시글 조회             |

---

## **설치 및 실행 방법**

### **1. 필수 구성 요소**
- Java 8 이상 설치.
- MySQL 8.0.24 이상 설치 및 데이터베이스 생성.
- Maven 설치.

### **2. 프로젝트 클론**
```bash
git clone https://github.com/1117mg/board2.git
cd your-repository-folder
```

### **3. 환경 설정**
`application.properties`
```properties
spring.datasource.url=jdbc:mysql://<your-database-host>:3306/<your-database-name>
spring.datasource.username=<your-database-username>
spring.datasource.password=<your-database-password>
```

### **4. 실행**
```bash
mvn spring-boot:run
```

- 서버가 시작되면 아래 주소에서 접근 가능합니다.
  - 관리자 페이지: `http://localhost:8080/master/main`
  - 회원 페이지: `http://localhost:8080/front/main`
