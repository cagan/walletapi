# Dockerfile

FROM eclipse-temurin:21-jdk-alpine

# Uygulama için bir çalışma dizini oluştur
WORKDIR /app

# Maven Wrapper veya build edilmiş jar'ı kopyala
COPY target/walletapi-0.0.1-SNAPSHOT.jar app.jar

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]