# swift-auth

개인 프로젝트 전반에서 사용할 중앙 인증 서버입니다.

## 설계 배경

사이드 프로젝트를 만들 때마다 회원가입, 로그인, 토큰 관리 로직을 반복 구현하는 문제가 있었습니다.
swift-auth는 이 인증 로직을 한 곳에서 관리하고, 각 프로젝트는 OAuth 클라이언트로 등록하여 인증을 위임하는 구조로 설계했습니다.

## 구조

```
클라이언트 프로젝트
    └── OAuth 클라이언트 등록 (client_id / client_secret 발급)
    └── 사용자 로그인 요청 → swift-auth
    └── JWT 발급받아 자체 서비스에서 사용
```

- 사용자 계정은 swift-auth에서 중앙 관리
- 각 프로젝트는 클라이언트로 등록 후 JWT를 통해 인증
- JWT 검증은 Public Key 배포 또는 `/userinfo` 호출로 처리 (예정)

## 기술 스택

- Java 17 / Spring Boot 3.x
- Spring Security + JWT (jjwt)
- PostgreSQL (사용자/클라이언트 데이터)
- Redis (Refresh Token 저장)
- K3s 배포 (예정)

## API

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/users/register` | 회원가입 | X |
| POST | `/api/users/login` | 로그인 + 토큰 발급 | X |
| POST | `/api/users/refresh` | Access Token 재발급 | X |
| POST | `/api/users/logout` | 로그아웃 | O |
| GET | `/api/users/me` | 내 정보 조회 | O |
| POST | `/api/clients/register` | OAuth 클라이언트 등록 | X |

## 향후 계획

- [ ] Docker 이미지 빌드 및 K3s 배포
- [ ] `/oauth2/jwks` Public Key 엔드포인트
- [ ] 입력값 검증 (`@Valid`)
- [ ] TestContainers 기반 통합 테스트
