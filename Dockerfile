# Etapa de build
FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /app

# Copia os arquivos necessários para o build
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Adiciona permissão de execução para o Maven Wrapper
RUN chmod +x mvnw

# Desabilita MAVEN_CONFIG e executa o Maven para limpar, instalar e empacotar o aplicativo sem rodar os testes
RUN unset MAVEN_CONFIG && ./mvnw clean install -DskipTests && ./mvnw package -DskipTests

# Etapa de execução
FROM openjdk:21

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia o JAR da etapa de build para a etapa de execução
COPY --from=build /app/target/app-*.jar /app/app.jar

# Exponha a porta da aplicação
EXPOSE 8080

ARG AWS_ACCESS_KEY_ID
ARG AWS_SECRET_ACCESS_KEY
ARG AWS_REGION
ARG CLOUD_SQS_NAME
ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
ENV AWS_REGION=$AWS_REGION
ENV CLOUD_SQS_NAME=$CLOUD_SQS_NAME

# Comando de inicialização da aplicação
CMD ["java", "-jar", "app.jar"]
