FROM openjdk:17
ARG JAR_FILE=/target/*.jar
COPY ./Bot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]




#COPY . /usr/src/main
#WORKDIR /usr/src/main
#ADD src/main/java/com/chekotovsky/Bot/BotApplication.java .
#RUN javac src/main/java/com/chekotovsky/Bot/BotApplication.java
#CMD ["java", "BotApplication"]