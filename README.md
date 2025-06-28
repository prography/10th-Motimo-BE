### 실행 스크립트
env파일은 [motimo 내부 노션](https://www.notion.so/env-1fb8fa533c578047848dca668dbc7b67)을 참고합니다 <br>
```
DB_USERNAME=
DB_PW=
DB_URL=

JWT_SECRET_KEY
ACCESS_TOKEN_EXP=
REFRESH_TOKEN_EXP=
ISSUER=

GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=

KAKAO_CLIENT_ID=
KAKAO_CLIENT_SECRET=

SUPABASE_URL=
SUPABASE_REGION=
SUPABASE_BUCKET=
SUPABASE_API_KEY=
```
`.env` 파일을 프로젝트 루트에 추가해줍니다.

프로젝트 루트에서 아래 스크립트를 실행합니다.
```
docker-compose up --build --force-recreate
```
