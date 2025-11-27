![alt text](./docs/readme-header.png)


<p align="center"> <img src="https://img.shields.io/badge/AI_Collab-000000?style=for-the-badge&logo=github&logoColor=white" /> <img src="https://img.shields.io/badge/Java_17-007396?style=for-the-badge&logo=openjdk&logoColor=white" /> <img src="https://img.shields.io/badge/React_18-61DAFB?style=for-the-badge&logo=react&logoColor=black" /> <img src="https://img.shields.io/badge/OpenAI_API-412991?style=for-the-badge&logo=openai&logoColor=white" /> </p>

<br>

# 🤖 AI Collab
**LLM 기반 GitHub Pull Request 자동 코드 리뷰 플랫폼**



---

## ⭐ 프로젝트 소개

AI Collab은 GitHub Pull Request 기반으로 동작하는
자동 AI 코드 리뷰 생성 플랫폼입니다.

OpenAI API를 이용해 PR diff를 분석하고
코드 개선 포인트, 리팩터링 제안, 스타일 검증 등을 자동화하여
팀원의 리뷰 부담을 줄이고 협업 효율을 높이는 데 목적이 있습니다.

---

## 🧩 주요 기능
| 기능                      | 설명                                    |
| ----------------------- | ------------------------------------- |
| 🧠 **AI 자동 리뷰 생성**      | Pull Request의 diff를 기반으로 GPT 리뷰 생성    |
| 🔍 **PR 변화 자동 감지**      | Polling Scheduler가 새로운 커밋/PR 상태를 감지   |
| 📄 **리뷰 이력 저장**         | 커밋별 리뷰 기록을 PostgreSQL에 저장             |
| 🗂 **Repo & PR 조회**     | GitHub OAuth 기반 접근 가능한 Repo/PR 리스트 조회 |
| 🔐 **GitHub OAuth 로그인** | 안전한 인증/인가 제공                          |
| 📊 **대시보드 제공**          | React 기반 리뷰 뷰어 UI                     |


---

## ⚙️ Tech Stack

### 🖥️ Backend
![Java 17](https://img.shields.io/badge/Java_17-007396?logo=openjdk&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white)
![OpenAI API](https://img.shields.io/badge/OpenAI_API-412991?logo=openai&logoColor=white)

### 💻 Frontend
![React 18](https://img.shields.io/badge/React_18-61DAFB?logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=white)
![SCSS](https://img.shields.io/badge/SCSS-CC6699?logo=sass&logoColor=white)

### ☁️ Infra
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=githubactions&logoColor=white)
![Monorepo](https://img.shields.io/badge/Monorepo-181717?logo=github&logoColor=white)

---

## 🧱 시스템 아키텍처
```mermaid
flowchart LR

    subgraph Frontend
        FE[React Dashboard]
    end

    subgraph Backend
        BE[Spring Boot API]
        PS[Polling Scheduler]
    end

    subgraph External
        GH[(GitHub API)]
        GPT[(OpenAI API)]
        DB[(PostgreSQL)]
        RS[(Redis Optional)]
    end

    FE -->|OAuth Login / PR List| BE
    BE -->|Request| GH

    PS -->|Detect PR Changes| GH
    PS -->|Fetch Diff| GH
    PS -->|Send Diff| GPT
    PS -->|Save Review| DB

    FE -->|Load Reviews| BE
    BE --> DB

```

---

## 📦 디렉토리 구조

```
ai-collab/
 ├─ backend/    # Spring Boot API 서버
 └─ frontend/   # React Dashboard
 ```
