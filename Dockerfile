# JDK 이미지를 베이스로 사용
FROM eclipse-temurin:23-jdk AS build

# 작업 디렉토리 설정
WORKDIR /app

# gradlew 및 gradle 관련 파일 복사
COPY gradlew .
COPY gradle gradle

# 전체 프로젝트 복사
COPY . .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 프로젝트 빌드 (build/libs/*.jar 생성됨)
RUN ./gradlew build -x test --no-daemon

# jar 복사해서 실행 (빌드된 jar 이름 맞춰서 수정!)
CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "build/libs/Ocean-Backend-0.0.1-SNAPSHOT.jar"]
