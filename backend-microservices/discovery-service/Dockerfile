FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/discovery-service-*.jar app.jar

HEALTHCHECK --interval=30s --timeout=30s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]