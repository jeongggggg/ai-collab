# 🤖 AI Collab
**LLM 기반 실시간 코드 분석·리뷰 및 협업 지원 플랫폼**

---

## 🧠 프로젝트 목표
“AI를 통한 효율적 코드 품질 관리와 협업 생산성 향상”

> 💡 **LLM(Language Model)** 은 ChatGPT와 같은 대규모 언어 모델로,  
> OpenAI API를 통해 코드 이해와 리뷰 피드백을 자동으로 생성합니다.  
> AI Collab은 LLM 응답을 Kafka 이벤트로 전달하여 실시간 협업을 지원합니다.


---


## 📌 프로젝트 개요
AI Collab은 팀 프로젝트에서 발생하는 **리뷰 병목, 반복 코멘트, 기준 불일치** 문제를 해결하기 위한  
**LLM 기반 실시간 코드 분석·협업 플랫폼**입니다.  
Kafka 이벤트 스트림을 통해 코드 리뷰, 메시지, 알림을 실시간으로 연결합니다.

---

## 🧩 주요 기능
| 구분 | 기능 설명 |
|------|------------|
| 🧠 **AI 리뷰** | 코드 변경 시 자동 분석 및 개선 제안 생성 (LLM 연동) |
| 🗂️ **프로젝트 관리** | 프로젝트 생성, 팀원 초대, 역할 관리 |
| 🧾 **리뷰 히스토리** | Kafka 이벤트 기반 코드 변경 내역 추적 |
| 💬 **실시간 협업** | Gateway 서버(WebSocket) 기반 메시지/알림 기능 |
| 📊 **대시보드** | 코드 품질 점수, 리뷰 진행 현황 시각화 |
| 🔐 **인증/인가** | JWT 기반 로그인 + Spring Security |
| ☁️ **파일 업로드** | AWS S3 연동 예정 (문서/코드 스냅샷 업로드) |

---

## ⚙️ Tech Stack

### 🖥️ Backend
![Java 17](https://img.shields.io/badge/Java_17-007396?logo=openjdk&logoColor=white)
![Spring Boot 3.3.3](https://img.shields.io/badge/Spring_Boot_3.3.3-6DB33F?logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-59666C?logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?logo=apachekafka&logoColor=white)
![OpenAI API](https://img.shields.io/badge/OpenAI_API-412991?logo=openai&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?logo=jsonwebtokens&logoColor=white)
![Swagger UI](https://img.shields.io/badge/Swagger_UI-85EA2D?logo=swagger&logoColor=black)

### 💻 Frontend
![React 18](https://img.shields.io/badge/React_18-61DAFB?logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white)
![Styled Components](https://img.shields.io/badge/Styled_Components-DB7093?logo=styledcomponents&logoColor=white)
![SCSS](https://img.shields.io/badge/SCSS-CC6699?logo=sass&logoColor=white)
![Redux Toolkit](https://img.shields.io/badge/Redux_Toolkit-764ABC?logo=redux&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-646CFF?logo=vite&logoColor=white)
![CRA](https://img.shields.io/badge/Create_React_App-09D3AC?logo=createreactapp&logoColor=white)

### ☁️ Infra
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?logo=githubactions&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS_EC2-FF9900?logo=amazonec2&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?logo=amazons3&logoColor=white)
![Monorepo](https://img.shields.io/badge/Monorepo-181717?logo=github&logoColor=white)

---

## 🧱 아키텍처 설계
```mermaid
flowchart LR
    subgraph Frontend
        A[React 대시보드]
    end

    subgraph Backend
        B[Spring Boot API]
        C[Node.js Gateway]
    end

    subgraph Workers
        D[Static Analysis Worker]
        E[AI Review Worker]
    end

    subgraph Infra
        F[(Apache Kafka)]
        G[(PostgreSQL DB)]
        H[(Redis Cache)]
    end

    A -->|HTTP 요청| B
    B -->|이벤트 발행| F
    F --> D
    D -->|결과 발행| F
    F --> E
    E -->|AI 리뷰 발행| F
    F --> C
    C -->|WebSocket/SSE| A
    C -->|notify.events| I[Slack/Discord 알림 예정]
    B --> G
    C --> G

```

---

> 💬 *AI Collab은 “AI가 개발 협업을 보조하는 실시간 코드 리뷰 도구”를 목표로 합니다.*
