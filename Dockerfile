FROM eclipse-temurin:17-jdk-alpine
COPY "target/default+uberjar/clojure-game-api-0.1.0-SNAPSHOT-standalone.jar" app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
