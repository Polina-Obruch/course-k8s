# ---------Build stage---------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Копируем только файлы для зависимостей (кэширование слоев)
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Cкачиваем зависимости
RUN ./gradlew dependencies || return 0

# Копируем исходники и собираем
COPY src src
RUN ./gradlew clean build -x test -x ktlintKotlinScriptCheck -x ktlintMainSourceSetCheck -x ktlintTestSourceSetCheck

# -------Runtime stage---------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Создаем пользователя
RUN useradd --system --create-home --uid 1001 appuser

# Переключаемся на него
USER appuser

# Копируем jar с правильными правами
COPY --chown=appuser:appuser --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
