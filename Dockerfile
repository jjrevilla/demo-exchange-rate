FROM openjdk:21-jdk
VOLUME /tmp
COPY target/exchange.rate-1.0.0.jar exchange-rate.jar
ENTRYPOINT ["java","-jar","/exchange-rate.jar"]