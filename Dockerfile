# ---------- Builder Stage ----------
# FROM eclipse-temurin:17-jdk AS builder

# WORKDIR /app

# COPY . .

# RUN ./mvnw clean install -DskipTests

# FROM eclipse-temurin:17-jre

# WORKDIR /app

# COPY --from=builder /app/api/target/*.jar app.jar

# EXPOSE 8080

# ENTRYPOINT ["java", "-jar", "app.jar"]

# ---------- Builder ----------
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# ---------- Runtime ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/api/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]