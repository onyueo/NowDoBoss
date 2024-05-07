# 기본 이미지로 Node.js 버전 20.11.1 사용
FROM node:20.11.1 AS build

# 작업 디렉토리 설정
WORKDIR /usr/src/app

# package.json 및 package-lock.json을 복사하여 종속성 설치
COPY package*.json ./

# 종속성 설치
RUN npm install

# 나머지 애플리케이션 코드 복사
COPY . .

# .env 파일 변경
COPY .env-dev .env

# 프론트엔드 코드 빌드
RUN npm run build