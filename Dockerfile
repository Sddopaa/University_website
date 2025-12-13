# Используем JDK 21
FROM eclipse-temurin:21-jdk-jammy

# Рабочая директория
WORKDIR /app

# Копируем pom и скачиваем зависимости
COPY pom.xml .
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline

# Копируем исходники и собираем jar
COPY src ./src
RUN mvn clean package -DskipTests

# Указываем точку входа
CMD ["java", "-jar", "target/university.jar"]
