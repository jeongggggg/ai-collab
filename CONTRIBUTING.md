# 🧭 AI Collab 기여 가이드 (CONTRIBUTING.md)

---

## 🌿 브랜치 전략

### 기본 브랜치 구조
```
main
 └─ develop
      ├─ feature/backend-*
      ├─ feature/frontend-*
      ├─ release/*
      └─ hotfix/*
```

### 브랜치 역할 설명
| 브랜치 | 용도 | 예시 |
|--------|------|------|
| **main** | 배포용 안정 버전 | v1.0, v1.1 |
| **develop** | 메인 개발 브랜치 | 기능 통합, 테스트용 |
| **feature/** | 세부 기능 개발 | feature/backend-login, feature/frontend-dashboard |
| **release/** | 배포 전 QA/테스트 | release/v1.0.0 |
| **hotfix/** | 배포 후 긴급 수정 | hotfix/fix-token-expiry |

---

## 🪜 브랜치 생성 규칙

### 신규 브랜치 생성
```bash
# 개발 브랜치 생성
git checkout -b develop

# 기능 브랜치 생성 (develop 기준)
git checkout -b feature/backend-login develop

# 릴리즈 브랜치 생성
git checkout -b release/v1.0.0 develop

# 핫픽스 브랜치 생성
git checkout -b hotfix/fix-login-error main
```

---

## 🧱 커밋 메시지 규칙

### 기본 형식
```
<이모지> <타입>: <작업 내용>
```

### 커밋 타입 예시
| 이모지 | 타입 | 설명 |
|:------:|------|------|
| ✨ | **Feat** | 새로운 기능 추가 |
| 🐛 | **Fix** | 버그 수정 |
| 🧹 | **Refactor** | 코드 리팩터링 (기능 변화 없음) |
| 🎨 | **Style** | 코드 스타일, 포맷 수정 |
| 🧪 | **Test** | 테스트 코드 추가/수정 |
| 📄 | **Docs** | 문서 수정 (README, 주석 등) |
| 🔧 | **Chore** | 빌드, 설정, 패키지 관리 등 기타 변경 |
| 🚀 | **Deploy** | 배포 관련 커밋 |
| 🧩 | **Config** | 환경 설정 추가/변경 |
| 🗑️ | **Remove** | 불필요한 코드/파일 삭제 |

### 예시
```bash
✨ Feat: 로그인 API 구현
🐛 Fix: 회원가입 시 이메일 중복 예외 수정
🧹 Refactor: User 엔티티 구조 변경
📄 Docs: README에 실행 방법 추가
```

---

## 🔀 Pull Request(PR) 규칙

### PR 제목 규칙
```
[타입] 기능/작업명 - 간단한 설명
```

### 예시
```
[Feat] backend-login - JWT 로그인 기능 구현
[Fix] frontend-dashboard - 그래프 데이터 렌더링 오류 수정
```

### PR 생성 시 체크리스트
- [ ] 코드가 정상적으로 빌드됨
- [ ] 불필요한 로그/주석 제거
- [ ] main이 아닌 develop으로 PR 생성
- [ ] 커밋 메시지 규칙 준수

---

## 🧾 코드 리뷰 원칙
- PR 단위는 **작고 명확하게 (100~300줄 이하)**
- 리뷰 코멘트는 **건설적으로, 제안 중심으로 작성**
- 리뷰 승인 후 **PR 작성자가 직접 병합 (Squash Merge 권장)**

---

## 🏷️ 태그 & 버전 관리
- 버전명은 **v[Major].[Minor].[Patch]** 형식 사용
- 예시:
    - `v1.0.0` – 첫 공식 릴리즈
    - `v1.1.0` – 기능 추가
    - `v1.1.1` – 버그 수정

---

## ✅ 요약

| 구분 | 규칙 |
|------|------|
| 브랜치 생성 | main → develop → feature/* |
| 커밋 메시지 | `<이모지> <타입>: <내용>` |
| PR 병합 기준 | develop → main |
| 릴리즈 태그 | v1.0.0 형식 |