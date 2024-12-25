FROM amazoncorretto:20-alpine-jdk
WORKDIR /app
COPY build/libs/hotel-booking-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]