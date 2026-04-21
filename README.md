# 📔 Emotion Diary

> 감정을 기록하고, AI가 따뜻하게 답장해주는 감정 일기 서비스

하루의 감정을 일기로 남기면 AI가 **부모님(PARENT)** 또는 **선생님(TEACHER)** 톤으로 답장을 보내주고, 게시판에서 비슷한 감정을 가진 사람들과 공감할 수 있는 서비스입니다.

- 🔗 **Backend**: [Leeeydia/diary-backend](https://github.com/Leeeydia/diary-backend)
- 🔗 **Frontend**: [Leeeydia/diary-frontend](https://github.com/Leeeydia/diary-frontend)

---

## ✨ 주요 기능

| 기능 | 설명 |
| --- | --- |
| 🔐 회원 관리 | 회원가입 / 로그인 / 프로필 이미지 업로드 / 닉네임·답장모드 변경 |
| 📖 감정 일기 | 6가지 감정(HAPPY, SAD, ANGRY, TIRED, CALM, EXCITED) 기반 일기 작성·수정·삭제(소프트), 감정 필터링 + 페이징 |
| 🤖 AI 답장 | OpenAI `gpt-4o-mini`로 부모(PARENT) 또는 선생님(TEACHER) 톤의 맞춤 답장 생성 |
| 💬 감정 게시판 | 로그인 사용자 간 공유 게시판, 감정별 필터링, 제목·내용 검색 |
| 🔑 JWT 인증 | Access Token(30분) + Refresh Token(7일) 기반 인증 |

---

## 🧱 기술 스택

### Backend ([diary-backend](https://github.com/Leeeydia/diary-backend))

| 분류 | 내용 |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Build | Gradle |
| Persistence | MyBatis 3.0.3, MySQL |
| Auth | JJWT 0.12.6, Spring Security Crypto (password hashing) |
| External API | OpenAI API (`gpt-4o-mini`) via Spring WebFlux WebClient |
| Utility | Lombok |
| Port | `55000` |
| Default Branch | `dev` |

### Frontend ([diary-frontend](https://github.com/Leeeydia/diary-frontend))

| 분류 | 내용 |
| --- | --- |
| Framework | React + Vite + TypeScript |
| Routing | React Router DOM (createBrowserRouter) |
| Styling | Tailwind CSS |
| Structure | Feature-based (`features/auth`, `features/diary`, `features/board`, `features/member`, `features/home`) |

> ℹ️ 프론트엔드의 정확한 React/Vite 버전은 저장소의 `package.json`을 참고해주세요. (본 README 작성 시점에 `package.json` 전체 내용은 확인하지 못했습니다.)

---

## 📁 프로젝트 구조

### Backend

```
diary-backend/
├── src/main/java/com/diary/backend/
│   ├── vo/                    # MemberVO, DiaryVO, BoardVO, AiReplyVO 등
│   ├── mapper/                # MyBatis Mapper 인터페이스
│   ├── aireply/               # AI 답장 도메인 (자체 패키지)
│   ├── controller/            # REST API Controller (추정)
│   ├── service/               # 비즈니스 로직 (추정)
│   └── config/                # JWT, Security, MyBatis 설정 (추정)
├── src/main/resources/
│   ├── mappers/               # MyBatis XML (AiReplyMapper, BoardMapper, DiaryMapper, MemberMapper, RefreshTokenMapper)
│   └── application.yml
├── uploads/profile/           # 프로필 이미지 업로드 경로
├── build.gradle
└── auth.http                  # 인증 API 테스트 스크립트
```

### Frontend

```
diary-frontend/
├── src/
│   ├── main.tsx               # 엔트리포인트 (StrictMode + createRoot)
│   ├── App.tsx                # RouterProvider
│   ├── router/
│   │   └── index.tsx          # createBrowserRouter 설정
│   ├── features/
│   │   ├── home/pages/        # HomePage
│   │   ├── auth/pages/        # LoginPage, RegisterPage
│   │   ├── diary/pages/       # DiaryListPage, DiaryDetailPage, DiaryWritePage
│   │   ├── board/pages/       # BoardListPage, BoardDetailPage, BoardWritePage
│   │   └── member/pages/      # ProfilePage
│   ├── shared/components/
│   │   └── ProtectedRoute.tsx # 로그인 필수 라우트 가드
│   ├── App.css
│   └── index.css              # Tailwind directives + font-diary
└── index.html
```

---

## 🗄️ 데이터베이스 스키마

DB: `diary` (MySQL, utf8mb4)

| 테이블 | 역할 | 주요 컬럼 |
| --- | --- | --- |
| `member` | 회원 정보 | id, username(UK), password, email(UK), nickname, role, **reply_mode**(`PARENT`/`TEACHER`), profile_image_url |
| `refresh_token` | Refresh Token 저장 | id, member_id(UK, FK→member), token, expires_at |
| `diary` | 감정 일기 | id, member_id(FK), content, **emotion**(ENUM 6종), is_deleted (소프트 삭제) |
| `board` | 감정 게시판 | id, member_id(FK), title, content, emotion, is_deleted |
| `ai_reply` | AI 답장 | id, diary_id(FK), reply_type, reply_content |

**emotion ENUM**: `HAPPY`, `SAD`, `ANGRY`, `TIRED`, `CALM`, `EXCITED`

전체 스키마는 `schema.sql` 참고.

---

## 🌐 주요 API

> ⚠️ 아래 엔드포인트는 **MyBatis Mapper 파일 기반으로 유추한 리소스입니다.** 실제 Controller 코드를 직접 확인하지 못했으므로, 정확한 경로와 요청/응답 스펙은 저장소의 Controller 소스를 확인해주세요.

### 🔐 Auth

| Method | Path (추정) | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 (Access + Refresh 발급) | ❌ |
| POST | `/api/auth/refresh` | Access Token 재발급 | Refresh |
| POST | `/api/auth/logout` | 로그아웃 (Refresh 제거) | ✅ |

### 👤 Member

| Method | Path (추정) | 설명 |
| --- | --- | --- |
| GET | `/api/members/me` | 내 프로필 조회 |
| PATCH | `/api/members/me/nickname` | 닉네임 변경 |
| PATCH | `/api/members/me/reply-mode` | 답장 모드 변경 (PARENT / TEACHER) |
| POST | `/api/members/me/profile-image` | 프로필 이미지 업로드 (multipart, 최대 5MB) |

### 📖 Diary

| Method | Path (추정) | 설명 |
| --- | --- | --- |
| POST | `/api/diaries` | 일기 작성 |
| GET | `/api/diaries` | 내 일기 목록 (감정 필터 + 페이징) |
| GET | `/api/diaries/{id}` | 일기 상세 |
| PUT | `/api/diaries/{id}` | 일기 수정 |
| DELETE | `/api/diaries/{id}` | 일기 소프트 삭제 |

### 💬 Board

| Method | Path (추정) | 설명 |
| --- | --- | --- |
| POST | `/api/boards` | 게시글 작성 |
| GET | `/api/boards` | 게시글 목록 (감정 필터 + 페이징) |
| GET | `/api/boards/{id}` | 게시글 상세 |
| PUT | `/api/boards/{id}` | 게시글 수정 |
| DELETE | `/api/boards/{id}` | 게시글 소프트 삭제 |

### 🤖 AI Reply

| Method | Path (추정) | 설명 |
| --- | --- | --- |
| POST | `/api/diaries/{diaryId}/ai-reply` | AI 답장 생성 (OpenAI 호출) |
| GET | `/api/diaries/{diaryId}/ai-reply` | AI 답장 조회 (diary당 1개) |

---

## ⚙️ 환경 변수 & 설정

### Backend (`application.yml`)

```yaml
server:
  port: 55000

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/diary
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 5MB

jwt:
  secret: <BASE64_ENCODED_SECRET>
  access-token-expiry: 1800000      # 30분
  refresh-token-expiry: 604800000   # 7일

openai:
  api-key: ${OPENAI_API_KEY}
  model: gpt-4o-mini
  max-tokens: 120

file:
  upload:
    profile-dir: ${user.dir}/uploads/profile

mybatis:
  mapper-locations: classpath:/mappers/**/*.xml
  type-aliases-package: com.diary.backend.vo
  configuration:
    map-underscore-to-camel-case: true
```

**필수 환경 변수**

| 변수 | 설명 |
| --- | --- |
| `OPENAI_API_KEY` | OpenAI API 키 (AI 답장 기능에 필수) |

### Frontend

프론트엔드 API Base URL은 `http://localhost:55000` (백엔드 포트)로 설정해야 합니다. 정확한 환경 변수 이름은 저장소의 `.env.example` 또는 API 클라이언트 코드를 확인해주세요.

---

## 🚀 실행 방법

### 사전 준비

- Java 17
- MySQL 8.x (DB `diary` 생성)
- Node.js (권장 LTS)
- OpenAI API Key

### 1. 데이터베이스 준비

```bash
mysql -u root -p < schema.sql
```

### 2. Backend 실행

```bash
git clone https://github.com/Leeeydia/diary-backend.git
cd diary-backend
git checkout dev

# 환경 변수 설정
export OPENAI_API_KEY="sk-..."

# 실행
./gradlew bootRun
# → http://localhost:55000
```

### 3. Frontend 실행

```bash
git clone https://github.com/Leeeydia/diary-frontend.git
cd diary-frontend

# 패키지 설치 (pnpm 또는 npm - 저장소의 lock 파일에 맞춰 사용)
pnpm install   # 또는 npm install

# 개발 서버 실행
pnpm dev       # 또는 npm run dev
```

---

## 🗺️ 프론트엔드 라우팅

| Path | Page | 인증 |
| --- | --- | --- |
| `/` | HomePage | ❌ |
| `/login` | LoginPage | ❌ |
| `/register` | RegisterPage | ❌ |
| `/write` | DiaryWritePage | ❌ |
| `/diary` | DiaryListPage | ❌ |
| `/diary/:id` | DiaryDetailPage | ❌ |
| `/diary/:id/edit` | DiaryWritePage | ❌ |
| `/board` | BoardListPage | ✅ |
| `/board/new` | BoardWritePage | ✅ |
| `/board/:id` | BoardDetailPage | ✅ |
| `/board/:id/edit` | BoardWritePage | ✅ |
| `/mypage` | ProfilePage | ❌ |

> `/board/*`는 `ProtectedRoute`로 묶여 있어 토큰이 없으면 `/login`으로 리다이렉트됩니다. (출처: `src/router/index.tsx`)

---

## 📝 커밋 규칙

```
type: 한글 설명
```

| type | 용도 |
| --- | --- |
| `feat` | 새로운 기능 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 |
| `chore` | 설정, 문서, 기타 |

- 관련 없는 변경은 커밋 분리
- 여러 파일이 포함되면 파일명 명시

---

## ⚠️ 문서 작성 시 참고사항 (추측 표기)

이 README는 다음 자료를 근거로 작성되었습니다.

**확실한 근거:**
- `build.gradle` (Spring Boot 3.2.5, Java 17, 의존성)
- `application.yml` (포트, JWT, OpenAI 설정)
- `schema.sql` (DB 테이블 구조)
- MyBatis Mapper XML 5종 (기능·리소스 유추)
- `src/router/index.tsx` (프론트 라우팅)

**추측/미확인 부분 (실제 코드 확인 필요):**
- API 엔드포인트의 정확한 경로·요청/응답 스펙 (Controller 코드 미확인)
- 프론트엔드 `package.json` 상의 정확한 라이브러리 버전 및 패키지 매니저
- `.env.example`의 변수명

추가 정보나 수정이 필요한 부분은 실제 소스를 기준으로 보완해주세요.
