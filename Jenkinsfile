pipeline {
    agent any
    
    environment {
        // Maven and JDK tools defined in Jenkins
        MAVEN_HOME = tool 'apache-maven-3.9.8'
        JAVA_HOME = tool 'jdk 17'
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
        //PROJECT_PATH = 'C:\\Users\\mmf27\\Documents\\Repositorios\\TD_Kibernum_Devops_Grupo2AmericanSummer-GestionEventos'

        // Define el SonarQube Server a utilizar
        SONARQUBE_URL = 'http://localhost:9000'
        scannerHome = tool 'SonarQubeScanner'

        //terraform
        TF_VAR_aws_region = 'us-east-1'
        GITHUB_TOKEN = credentials('github-token')
        AWS_CREDENTIALS = credentials('AWS_CREDENTIALS')
    }
  
    tools {
        terraform 'terraform'
    }
    
    stages {
        stage('Clonar o actualizar repositorio') {
            steps {
                ansiColor('xterm') {
                    echo "\033[34mClonando o actualizando el repositorio desde GitHub...\033[0m"
                    script {
                        withCredentials([string(credentialsId: 'github-token', variable: 'GITHUB_TOKEN')]) {
                            if (!fileExists('Pipeline-AplicativoGestion-AWS')) {
                                echo "\033[32mRepositorio no encontrado, clonando el repositorio...\033[0m"
                                sh 'git clone https://github.com/cristianjonhson/Pipeline-AplicativoGestion-AWS.git'
                            } else {
                                echo "\033[33mRepositorio ya existente, actualizando el repositorio...\033[0m"
                                dir('Pipeline-AplicativoGestion-AWS') {
                                    sh '''
                                        # Comprueba y establece 'user.name' solo si no está configurado
                                        if ! git config --get user.name > /dev/null; then
                                            echo "Configuración de 'user.name' no encontrada. Estableciendo 'user.name'..."
                                            git config user.name "Cristian Jonhson Alvarez"
                                        else
                                            echo "Configuración de 'user.name' ya existente"
                                        fi
        
                                        # Comprueba y establece 'user.email' solo si no está configurado
                                        if ! git config --get user.email > /dev/null; then
                                            echo "Configuración de 'user.email' no encontrada. Estableciendo 'user.email'..."
                                            git config user.email "cristian.jonhson@inacapmail.cl"
                                        else
                                            echo "Configuración de 'user.email' ya existente"
                                        fi
        
                                        git reset --hard    
                                        git pull --rebase origin master
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('Build Project with Maven') {
            steps {
                // Construir el proyecto con Maven
                echo "\u001B[32mConstruyendo proyecto con Maven...\u001B[0m"
                sh 'mvn -f pom.xml clean install'
            }
        }

       /* stage('SonarQube Analysis') {
            steps {
                // Sin especificar versión, solo usar el scanner configurado en Jenkins
                withSonarQubeEnv('SonarQubeScanner') { // Usar la configuración del servidor SonarQube sin versión específica
                    sh "${scannerHome}/bin/sonar-scanner \
                    -Dsonar.projectKey=Grupo2AmericanSummer-GestionEventos \
                    -Dsonar.host.url=${SONARQUBE_URL} \
                    -Dsonar.java.binaries=target/classes"
                }
              }
           }*/

        stage('Run Unit Tests') {
            steps {
                // Ejecutar pruebas con Maven
                echo "\u001B[34mCorriendo pruebas unitarias...\u001B[0m"
                sh 'mvn -f pom.xml test'
            }
            post {
                always {
                    // Publicar resultados de pruebas
                  echo "\u001B[34mPublicar resultados de pruebas...\u001B[0m"
                  junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Create Docker Network') {
            steps {
              echo "\u001B[33mCreando la red de Docker (si no existe)...\u001B[0m"
                script {
                    sh '''
                    docker network create ${NETWORK_NAME} || echo "Red ya existente"
                    '''
                }
            }
        }

        stage('Start PostgreSQL Container') {
            steps {
                echo "\u001B[35mStarting PostgreSQL Container...\u001B[0m"
                script {
                    // Verificar si el contenedor pg_container ya existe y eliminarlo si es necesario
                    sh '''
                    if [ "$(docker ps -a -q -f name=${PG_CONTAINER})" ]; then
                        echo -e "\033[0;31mEl contenedor '${PG_CONTAINER}' ya existe. Eliminándolo...\033[0m"
                        docker rm -f ${PG_CONTAINER}
                    fi
        
                    echo -e "\033[0;32mCreando el contenedor '${PG_CONTAINER}'...\033[0m"
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
              echo "\u001B[36mConstruyendo la imagen docker del aplicativo...\u001B[0m"
                script {
                    sh '''
                    docker build -t ${APP_IMAGE} .
                    '''
                }
            }
        }

        stage('Start Application Container') {
            steps {
               echo "\u001B[33mStarting Application Container...\u001B[0m"
                script {
                    // Verificar si el contenedor gestion_eventos_app ya existe y eliminarlo si es necesario
                    sh '''
                    if [ "$(docker ps -a -q -f name=${APP_CONTAINER})" ]; then
                        echo -e "\033[0;31mEl contenedor '${APP_CONTAINER}' ya existe. Eliminándolo...\033[0m"
                        docker rm -f ${APP_CONTAINER}
                    fi
        
                    echo -e "\033[0;32mCreando el contenedor '${APP_CONTAINER}'...\033[0m"
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
              echo "\u001B[32mInspecting Docker Network...\u001B[0m"
                script {
                    sh '''
                    docker network inspect ${NETWORK_NAME}
                    '''
                }
            }
        }

        /*stage('Run SQL Script') {
            steps {
                script {
                    sh '''
                    docker exec ${PG_CONTAINER} bash -c 'psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} -f /script.sql'
                    '''
                }
            }
        }*/
        
        stage('Ejecutar comandos SQL') {
            steps {
              echo "\u001B[34mExecuting SQL Commands in PostgreSQL Container...\u001B[0m"
                script {
                    // Ejecutar comandos SQL dentro del contenedor pg_container
                    sh '''
                        # Conectarse al contenedor y ejecutar comandos SQL
                        docker exec -i ${PG_CONTAINER} psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} <<EOF
                        CREATE DATABASE gestion_eventos WITH ENCODING 'UTF8';

                        -- Tabla de usuarios
                        CREATE TABLE usuarios (
                            id SERIAL PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL,
                            apellido VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL UNIQUE,
                            contrasena VARCHAR(100) NOT NULL,
                            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            rol VARCHAR(50) DEFAULT 'usuario'
                        );

                        -- Tabla de categoris de eventos
                        CREATE TABLE categorias (
                            id SERIAL PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL UNIQUE
                        );

                        -- Tabla de ciudades
                        CREATE TABLE ciudades (
                            id SERIAL PRIMARY KEY,
                            nombre VARCHAR(100) NOT NULL UNIQUE
                        );

                        -- Tabla de eventos
                        CREATE TABLE eventos (
                            id SERIAL PRIMARY KEY,
                            titulo VARCHAR(100) NOT NULL,
                            descripcion TEXT,
                            fecha_inicio TIMESTAMP NOT NULL,
                            fecha_fin TIMESTAMP NOT NULL,
                            ubicacion VARCHAR(255),
                            organizador_id INT NOT NULL REFERENCES usuarios(id),
                            categoria_id INT NOT NULL REFERENCES categorias(id),
                            ciudad_id INT NOT NULL REFERENCES ciudades(id),
                            valor DECIMAL(10, 2),
                            imagen_html TEXT,
                            fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        );

                        -- Tabla de inscripciones a los eventos
                        CREATE TABLE inscripciones (
                            id SERIAL PRIMARY KEY,
                            usuario_id INT NOT NULL REFERENCES usuarios(id),
                            evento_id INT NOT NULL REFERENCES eventos(id),
                            fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            UNIQUE(usuario_id, evento_id)
                        );

                        -- Insertar datos de prueba en la tabla de usuarios
                        INSERT INTO usuarios (nombre, apellido, email, contrasena, rol)
                        VALUES 
                        ('Admin', 'Admin', 'admin@eventos.com', 'admin123', 'admin'),
                        ('Juan', 'Perez', 'juan.perez@example.com', 'password123', 'ROLE_usuario'),
                        ('Maria', 'Garcia', 'maria.garcia@example.com', 'password123', 'usuario');

                        -- Insertar datos de prueba en la tabla de categorias
                        INSERT INTO categorias (nombre)
                        VALUES 
                        ('Verano Familiar'),
                        ('Programacion'),
                        ('Musica'),
                        ('Deportes');

                        -- Insertar datos de prueba en la tabla de ciudades
                        INSERT INTO ciudades (nombre)
                        VALUES 
                        ('Playa Central'),
                        ('Parque de la Ciudad'),
                        ('Sala de Conferencias'),
                        ('Polideportivo Municipal');

                        -- Insertar datos de prueba en la tabla de eventos
                        INSERT INTO eventos (titulo, descripcion, fecha_inicio, fecha_fin, ubicacion, organizador_id, categoria_id, ciudad_id, valor, imagen_html)
                        VALUES 
                        ('Festival de Verano Familiar', 'Un dia lleno de actividades familiares en la playa.', '2024-08-01 10:00:00', '2024-08-01 18:00:00', 'Playa Central', 1, 1, 1, 0.00, '<img src="festival_verano.jpg" />'),
                        ('Conferencia de Programacion', 'Una conferencia sobre las ultimas tendencias en programacion.', '2024-08-05 09:00:00', '2024-08-05 17:00:00', 'Sala de Conferencias', 1, 2, 3, 0.00, '<img src="conf_programacion.jpg" />');

                        -- Insertar datos de prueba en la tabla de inscripciones
                        INSERT INTO inscripciones (usuario_id, evento_id)
                        VALUES 
                        (2, 1),
                        (3, 2);

                        -- Consultas para verificar los datos insertados en las tablas
                        SELECT * FROM usuarios;
                        SELECT * FROM categorias;
                        SELECT * FROM ciudades;
                        SELECT * FROM eventos;
                        SELECT * FROM inscripciones;

EOF
                    '''
                }
            }
        }

        stage('Verificar recursos a eliminar') {
            steps {
                script {
                    echo "\033[33mVerificando si existen recursos a eliminar...\033[0m"
                    dir('Pipeline-AplicativoGestion-AWS') {
                        if (fileExists('tfplan')) {
                            try {
                                def planOutput = sh(script: 'terraform state list', returnStdout: true).trim()
                                if (planOutput) {
                                    echo "\033[31mSe detectaron recursos a eliminar. Ejecutando terraform destroy...\033[0m"
                                    sh 'terraform destroy -auto-approve'
                                    currentBuild.result = 'SUCCESS'
                                    return
                                } else {
                                    echo "\033[32mNo se detectaron recursos a eliminar. Continuando con terraform apply...\033[0m"
                                }
                            } catch (Exception e) {
                                echo "\033[31mError al leer el archivo tfplan: ${e.message}\033[0m"
                                currentBuild.result = 'FAILURE'
                                return
                            }
                        } else {
                            echo "\033[31mEl archivo 'tfplan' no existe. Por favor, asegúrese de ejecutar el plan primero.\033[0m"
                            currentBuild.result = 'FAILURE'
                            return
                        }
                    }
                }
            }
        }

        stage('Generar plan de Terraform') {
            when {
                expression { fileExists('tfplan') && currentBuild.result != 'SUCCESS' }
            }
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'AWS_CREDENTIALS'
                ]]) {
                    script {
                        echo "\033[33mEjecutando terraform plan...\033[0m"
                        dir('Pipeline-AplicativoGestion-AWS') {
                            if (!fileExists(".terraform")) {
                                echo "\033[33mInicializando Terraform por primera vez...\033[0m"
                                sh 'terraform init -input=false'
                            }

                            echo "\033[33mGenerando el archivo 'tfplan'...\033[0m"
                            sh 'terraform plan -out=tfplan'
                            echo "\033[32mPlan de Terraform generado con éxito...\033[0m"
                        }
                    }
                }
            }
        }

        stage('Aplicar plan de Terraform') {
            when {
                expression { fileExists('tfplan') && currentBuild.result != 'SUCCESS' }
            }
            steps {
                echo "\033[32mAplicando cambios con terraform apply...\033[0m"
                dir('Pipeline-AplicativoGestion-AWS') {
                    sh 'terraform apply -auto-approve tfplan'
                }
            }
        }
    }
}

        post {
                always {
                    echo "\033[1mPipeline finalizado\033[0m"
                }
                success {
                    echo "\033[32mPipeline ejecutado con éxito!\033[0m"
                }
                failure {
                    echo "\033[31mPipeline falló. Revisar los logs.\033[0m"
                }
            }

   /* post {
        always {
            echo 'Cleaning up Docker containers and workspace...'
            script {
                sh '''
                docker stop ${PG_CONTAINER} ${APP_CONTAINER}
                docker rm ${PG_CONTAINER} ${APP_CONTAINER}
                docker network rm ${NETWORK_NAME}
                '''
            }
            // Limpieza de archivos temporales
            cleanWs()
        }*/
        /* always{
           echo 'Slack Notification desde Jenkinsfile'
            slackSend channel: 'jenkins',
            color: COLOR_MAP[currentBuild.currentResult],
            message: "*${currentBuild.currentResult}: Job ${env.JOB_NAME} (jenkinsfile) - build ${env.BUILD_NUMBER}\n More Info at: ${env.BUILD_URL}"
        }
    }*/

