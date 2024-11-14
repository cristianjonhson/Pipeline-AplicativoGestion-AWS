# Gestión de Eventos

Este proyecto es una aplicación de gestión de eventos construida con Spring Boot, Spring MVC, Thymeleaf y Maven. Permite crear, gestionar y visualizar eventos en un calendario con opciones de filtrado y búsqueda.

## Características

- Crear, leer, actualizar y eliminar eventos (CRUD).
- Visualizar un calendario de eventos.
- Filtrar eventos por categoría, fecha, etc.
- Buscar eventos.

## Tecnologías Utilizadas

- Java 21
- Spring Boot 3.3.1
- Spring MVC
- Thymeleaf
- Hibernate
- PostgreSQL
- Maven
- Docker
- SonarQube
- IDE Eclipse
- Jira

## Requisitos Previos

- JDK 11 o superior
- Maven 3.6.0 o superior
- PostgreSQL 12 o superior

Para poder ejecutar esta aplicación, debes tener instalados los siguientes componentes:

- Docker
- Docker Compose (opcional, si quieres levantar otros servicios como base de datos)
- SonarQube (si deseas ejecutar el análisis de calidad de código)

## Configuración del Proyecto

### Clonar el Repositorio

```bash
git clone https://github.com/cristianjonhson/TD_Kibernum_Devops_Grupo2AmericanSummer-GestionEventos.git
cd TD_Kibernum_Devops_Grupo2AmericanSummer-GestionEventos
```

## Despliegue de Contenedores con Jenkins

A continuación, se detalla el Jenkinsfile utilizado para la construcción y despliegue del proyecto en contenedores Docker:

```groovy
def COLOR_MAP = [
  'SUCCESS': 'good',
  'FAILURE':'danger',
]

pipeline {
    agent any
    
    environment {
        // Maven and JDK tools defined in Jenkins
        MAVEN_HOME = tool 'apache-maven-3.9.8'
        JAVA_HOME = tool 'jdk 21'
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"

        // Docker environment
        NETWORK_NAME = 'gestion_eventos_network'
        PG_CONTAINER = 'pg_container'
        APP_IMAGE = 'gestion_eventos_app_image'
        APP_CONTAINER = 'gestion_eventos_app'
        POSTGRES_USER = 'postgres'
        POSTGRES_PASSWORD = 'admin'
        POSTGRES_DB = 'gestion_eventos'
        SPRING_DATASOURCE_URL = "jdbc:postgresql://pg_container:5432/gestion_eventos"
        SPRING_DATASOURCE_USERNAME = 'postgres'
        SPRING_DATASOURCE_PASSWORD = 'admin'
        SPRING_DATASOURCE_DRIVER_CLASS_NAME = 'org.postgresql.Driver'
        SPRING_JPA_DATABASE_PLATFORM = 'org.hibernate.dialect.PostgreSQLDialect'
        SERVER_PORT = '8082'
        SPRING_APPLICATION_NAME = 'G2-GestionEventos'
        SONARQUBE_URL = 'http://localhost:9000'
        scannerHome = tool 'SonarQubeScanner'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/cristianjonhson/TD_Kibernum_Devops_Grupo2AmericanSummer-GestionEventos.git', branch: 'feature/PT2-45-JM'
            }
        }

        stage('Build Project with Maven') {
            steps {
                sh 'mvn -f pom.xml clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-10.6.0.92116') {
                    sh '${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=Grupo2AmericanSummer-GestionEventos -Dsonar.host.url=${SONARQUBE_URL} -Dsonar.java.binaries=target/classes'
                }
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh 'mvn -f pom.xml test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Create Docker Network') {
            steps {
                script {
                    sh 'docker network create ${NETWORK_NAME} || echo "Network already exists"'
                }
            }
        }

        stage('Start PostgreSQL Container') {
            steps {
                script {
                    sh '''
                    docker run -d --name ${PG_CONTAINER} --network ${NETWORK_NAME} \
                        -e POSTGRES_USER=${POSTGRES_USER} \
                        -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
                        -e POSTGRES_DB=${POSTGRES_DB} \
                        -p 5432:5432 postgres:latest
                    '''
                }
            }
        }

        stage('Build Application Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${APP_IMAGE} .'
                }
            }
        }

        stage('Start Application Container') {
            steps {
                script {
                    sh '''
                    docker run -d --name ${APP_CONTAINER} --network ${NETWORK_NAME} \
                        -e "SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}" \
                        -e "SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}" \
                        -e "SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}" \
                        -e "SPRING_DATASOURCE_DRIVER_CLASS_NAME=${SPRING_DATASOURCE_DRIVER_CLASS_NAME}" \
                        -e "SPRING_JPA_DATABASE_PLATFORM=${SPRING_JPA_DATABASE_PLATFORM}" \
                        -e "SERVER_PORT=${SERVER_PORT}" \
                        -e "SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME}" \
                        -p ${SERVER_PORT}:${SERVER_PORT} ${APP_IMAGE}
                    '''
                }
            }
        }

        stage('Inspect Docker Network') {
            steps {
                script {
                    sh 'docker network inspect ${NETWORK_NAME}'
                }
            }
        }

        stage('Run SQL Script') {
            steps {
                script {
                    sh '''
                    docker exec -i ${PG_CONTAINER} psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} <<EOF
                    CREATE DATABASE gestion_eventos WITH ENCODING 'UTF8';
                    -- Restante de la creación de tablas e inserción de datos aquí...
                    EOF
                    '''
                }
            }
        }
    }

    post {
        always {
            echo 'Slack Notification desde Jenkinsfile'
            slackSend channel: 'jenkins',
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}: Job ${env.JOB_NAME} - build ${env.BUILD_NUMBER}\n More Info at: ${env.BUILD_URL}"
        }
    }
}
```

## Despliegue Local de Contenedores

Si prefieres ejecutar los contenedores de forma local, puedes utilizar los siguientes comandos:

1. Crear una red de Docker:

```bash
docker network create gestion_eventos_network
```

2. Iniciar el contenedor de PostgreSQL:

```bash
docker run -d --name pg_container --network gestion_eventos_network -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=gestion_eventos -p 5432:5432 postgres:latest
```

3. Construir la imagen de la aplicación:

```bash
docker build -t gestion_eventos_app_image .
```

4. Iniciar el contenedor de la aplicación:

```bash
docker run -d --name gestion_eventos_app --network gestion_eventos_network -e "SPRING_DATASOURCE_URL=jdbc:postgresql://pg_container:5432/gestion_eventos" -e "SPRING_DATASOURCE_USERNAME=postgres" -e "SPRING_DATASOURCE_PASSWORD=admin" -e "SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver" -e "SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect" -e "SERVER_PORT=8082" -e "SPRING_APPLICATION_NAME=G2-GestionEventos" -p 8082:8082 gestion_eventos_app_image
```

5. Inspeccionar la red de Docker:

```bash
docker network inspect gestion_eventos_network
```

6. Para ejecutar un script SQL, puedes copiarlo al contenedor y conectarte a la base de datos:

```bash
docker cp C:\ruta\del\script.sql pg_container:/script.sql
docker exec -it pg_container bash
psql -U postgres -d gestion_eventos
\i /script.sql
```

## Configuración de SonarQube

Puedes analizar el código fuente con SonarQube usando el siguiente comando de Docker para ejecutar SonarQube:

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts
```

Para ejecutar Jenkins de forma local:

```bash
docker run -d --name jenkins1 -p 7080:8080 -p 50000:50000 jenkins/jenkins:lts
```

## Análisis de Código con SonarQube

Si deseas realizar el análisis del código con SonarQube, asegúrate de que SonarQube esté en ejecución en el puerto configurado (por defecto, 9000). Puedes cambiar la URL de SonarQube y el token de autenticación en el Dockerfile.

### Dockerfile

El archivo Dockerfile está configurado en dos etapas:

1. **Etapa de construcción**: Utiliza una imagen base de Maven para compilar el proyecto y ejecutar un análisis con SonarQube.
2. **Etapa de ejecución**: Utiliza una imagen base de OpenJDK para ejecutar la aplicación generada.

#### Dockerfile

```dockerfile
# Etapa 1: Usar una imagen base de Maven para la construcción
FROM maven:3.9.6 AS builder

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml y el directorio de código fuente al contenedor
COPY pom.xml ./
COPY src ./src

# Ejecutar el análisis de SonarQube y construir la aplicación
RUN mvn clean verify sonar:sonar -Dsonar.host.url=http://host.docker.internal:9000 \
    -Dsonar.login=sqp_fc460ee7637d88918967791ee63e485c3852a112 -e -X

# Etapa 2: Usar una imagen base de OpenJDK con JRE 21 para la ejecución
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Crear el directorio de logs
RUN mkdir -p /var/log/app_logs

# Copiar el archivo JAR de la aplicación generado por Maven a la imagen
COPY --from=builder /app/target/calendarapp-0.0.1-SNAPSHOT.jar /app/calendarapp.jar

# Exponer el puerto en el que la aplicación estará escuchando
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app/calendarapp.jar"]
```

## Construcción y Ejecución con Docker

### 1. Construir la imagen de Docker

Para construir la imagen de la aplicación, asegúrate de estar en el directorio raíz del proyecto, donde se encuentra el archivo Dockerfile. Luego, ejecuta el siguiente comando:

```bash
docker build --no-cache -t gestion_eventos_app_image .
```

### 2s. Acceder a la Aplicación

La aplicación estará disponible en la siguiente URL, asumiendo que la estás ejecutando en tu máquina local:

```bash
http://localhost:8082
```

---