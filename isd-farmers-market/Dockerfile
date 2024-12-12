FROM jelastic/maven:3.9.5-openjdk-21 AS backend-builder
WORKDIR /app/backend

COPY ./pom.xml ./
RUN mvn dependency:go-offline

COPY . .

RUN mvn -Dmaven.repo.local=/root/.m2 -T 1C clean package -DskipTests

FROM eclipse-temurin:21-jdk AS backend-final
WORKDIR /app

COPY --from=backend-builder /app/backend/target/*.jar backend.jar

EXPOSE 8080

CMD ["java", "-jar", "backend.jar", "--spring.profiles.active=prod"]

