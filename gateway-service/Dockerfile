# ===== Base image =====
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY target/*.jar app.jar

# ===== Dynatrace OneAgent Layer =====
ARG DT_API_TOKEN
ARG DT_ENV_URL=https://mmg37294.live.dynatrace.com
RUN mkdir -p /dynatrace && \
    cd /dynatrace && \
    wget -O Dynatrace-OneAgent-Linux.sh "${DT_ENV_URL}/api/v1/deployment/installer/agent/unix/default/latest?arch=x86" \
      --header="Authorization: Api-Token ${DT_API_TOKEN}" && \
    /bin/sh Dynatrace-OneAgent-Linux.sh \
      --set-monitoring-mode=fullstack \
      --set-app-log-content-access=true && \
    rm -f Dynatrace-OneAgent-Linux.sh

# ===== Run app with OneAgent =====
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/opt/dynatrace/oneagent/agent/lib64/liboneagentloader.so", "-jar", "app.jar"]