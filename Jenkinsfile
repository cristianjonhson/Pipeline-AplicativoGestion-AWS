pipeline {
    agent any
    
    environment {
        // Maven and JDK tools defined in Jenkins
        MAVEN_HOME = tool 'apache-maven-3.9.8'
        JAVA_HOME = tool 'jdk 17'
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"

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
        
        // Define el SonarQube Server a utilizar
        SONARQUBE_URL = 'http://localhost:9000'
        scannerHome = tool 'SonarQubeScanner'

        //terraform
        TF_VAR_aws_region = 'us-east-1'
        GITHUB_TOKEN = credentials('github-token')
        AWS_CREDENTIALS = credentials('AWS_CREDENTIALS')
        SSH_CREDENTIALS_ID = 'ssh-credentials-id' // ID de las credenciales SSH en Jenkins
        EC2_USER = 'ec2-user' // Usuario de la instancia EC2
    
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
                                    configureGit()
                                    sh 'git reset --hard'
                                    sh 'git pull --rebase origin master'
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('Build Project with Maven') {
            steps {
                echo "\u001B[32mConstruyendo proyecto con Maven...\u001B[0m"
                sh 'mvn -f pom.xml clean install'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo "\u001B[34mCorriendo pruebas unitarias...\u001B[0m"
                sh 'mvn -f pom.xml test'
            }
            post {
                always {
                    echo "\u001B[34mPublicar resultados de pruebas...\u001B[0m"
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Create Docker Network') {
            steps {
                echo "\u001B[33mCreando la red de Docker (si no existe)...\u001B[0m"
                script {
                    sh 'docker network create ${NETWORK_NAME} || echo "Red ya existente"'
                }
            }
        }

        stage('Start PostgreSQL Container') {
            steps {
                echo "\u001B[35mStarting PostgreSQL Container...\u001B[0m"
                script {
                    manageContainer(PG_CONTAINER, 'postgres:latest', [
                        "-e POSTGRES_USER=${POSTGRES_USER}",
                        "-e POSTGRES_PASSWORD=${POSTGRES_PASSWORD}",
                        "-e POSTGRES_DB=${POSTGRES_DB}",
                        "-p 5432:5432"
                    ])
                }
            }
        }

        stage('Build Application Docker Image') {
            steps {
                echo "\u001B[36mConstruyendo la imagen docker del aplicativo...\u001B[0m"
                script {
                    sh 'docker build -t ${APP_IMAGE} .'
                }
            }
        }

        stage('Start Application Container') {
            steps {
                echo "\u001B[33mStarting Application Container...\u001B[0m"
                script {
                    manageContainer(APP_CONTAINER, APP_IMAGE, [
                        "-e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}",
                        "-e SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}",
                        "-e SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}",
                        "-e SPRING_DATASOURCE_DRIVER_CLASS_NAME=${SPRING_DATASOURCE_DRIVER_CLASS_NAME}",
                        "-e SPRING_JPA_DATABASE_PLATFORM=${SPRING_JPA_DATABASE_PLATFORM}",
                        "-e SERVER_PORT=${SERVER_PORT}",
                        "-e SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME}",
                        "-p ${SERVER_PORT}:${SERVER_PORT}"
                    ])
                }
            }
        }
      
        stage('Inspect Docker Network') {
            steps {
                echo "\u001B[32mInspecting Docker Network...\u001B[0m"
                script {
                    sh 'docker network inspect ${NETWORK_NAME}'
                }
            }
        }

        stage('Ejecutar comandos SQL') {
            steps {
                echo "\u001B[34mExecuting SQL Commands in PostgreSQL Container...\u001B[0m"
                script {
                    executeSQLCommands()
                }
            }
        }

        stage('Inicializar Terraform') {
            steps {
                dir('Pipeline-AplicativoGestion-AWS') {
                    script {
                        echo "\033[33mInicializando Terraform...\033[0m"
                        sh 'terraform init -input=false'
                        echo "\033[32mInicialización completada.\033[0m"
                    }
                }
            }
        }

        stage('Verificar recursos a eliminar') {
            steps {
                script {
                    echo "\033[33mVerificando si existen recursos a eliminar...\033[0m"
                    dir('Pipeline-AplicativoGestion-AWS') {
                        try {
                            def planOutput = sh(script: 'terraform state list', returnStdout: true).trim()
                            if (planOutput) {
                                echo "\033[31mSe detectaron recursos a eliminar. Ejecutando terraform destroy...\033[0m"
                                sh 'terraform destroy -auto-approve'
                                currentBuild.result = 'SUCCESS'
                                error("Pipeline terminado después de destruir los recursos.")
                            } else {
                                echo "\033[32mNo se detectaron recursos a eliminar. Continuando...\033[0m"
                            }
                        } catch (Exception e) {
                            echo "\033[31mError: No se encontró el archivo de estado de Terraform. Continuando...\033[0m"
                        }
                    }
                }
            }
        }

        stage('Generar y aplicar plan de Terraform') {
            when {
                expression { currentBuild.result != 'SUCCESS' }
            }
            steps {
                withCredentials([[ $class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'AWS_CREDENTIALS' ]]) {
                    dir('Pipeline-AplicativoGestion-AWS') {
                        script {
                            echo "\033[33mGenerando el archivo 'tfplan'...\033[0m"
                            sh 'terraform plan -out=tfplan'
                            echo "\033[32mPlan de Terraform generado con éxito...\033[0m"
                            echo "\033[32mAplicando cambios con terraform apply...\033[0m"
                            sh 'terraform apply -auto-approve tfplan'
                            echo "\033[32mCambios aplicados exitosamente.\033[0m"
                        }
                    }
                }
            }
        }

        stage('Obtener ID y IP de EC2') {
            when {
                expression { currentBuild.result != 'SUCCESS' }
            }
            steps {
                echo "\u001B[34mObteniendo ID y dirección IP de la instancia EC2...\u001B[0m"
                script {
                    dir('Pipeline-AplicativoGestion-AWS') {
                        def ec2Id = sh(script: 'terraform output -raw instance_id', returnStdout: true).trim()
                        def ec2Ip = sh(script: 'terraform output -raw public_ip', returnStdout: true).trim()
                        echo "ID de la instancia EC2: ${ec2Id}"
                        echo "IP de la instancia EC2: ${ec2Ip}"
                        env.EC2_HOST = ec2Ip
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
}

def configureGit() {
    sh '''
        if ! git config --get user.name > /dev/null; then
            git config user.name "Cristian Jonhson Alvarez"
        fi
        if ! git config --get user.email > /dev/null; then
            git config user.email "cristian.jonhson@inacapmail.cl"
        fi
    '''
}

def manageContainer(containerName, imageName, options) {
    sh """
        if [ "\$(docker ps -a -q -f name=${containerName})" ]; then
            docker rm -f ${containerName}
        fi
        docker run -d --name ${containerName} --network ${NETWORK_NAME} ${options.join(' ')} ${imageName}
    """
}

def executeSQLCommands() {
    sh '''
        docker exec -i ${PG_CONTAINER} psql -U ${POSTGRES_USER} -d ${POSTGRES_DB} <<EOF
        CREATE DATABASE gestion_eventos WITH ENCODING 'UTF8';
        CREATE TABLE usuarios (
            id SERIAL PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            apellido VARCHAR(100) NOT NULL,
            email VARCHAR(100) NOT NULL UNIQUE,
            contrasena VARCHAR(100) NOT NULL,
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            rol VARCHAR(50) DEFAULT 'usuario'
        );
        CREATE TABLE categorias (
            id SERIAL PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL UNIQUE
        );
        CREATE TABLE ciudades (
            id SERIAL PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL UNIQUE
        );
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
        CREATE TABLE inscripciones (
            id SERIAL PRIMARY KEY,
            usuario_id INT NOT NULL REFERENCES usuarios(id),
            evento_id INT NOT NULL REFERENCES eventos(id),
            fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UNIQUE(usuario_id, evento_id)
        );
        INSERT INTO usuarios (nombre, apellido, email, contrasena, rol)
        VALUES 
        ('Admin', 'Admin', 'admin@eventos.com', 'admin123', 'admin'),
        ('Juan', 'Perez', 'juan.perez@example.com', 'password123', 'ROLE_usuario'),
        ('Maria', 'Garcia', 'maria.garcia@example.com', 'password123', 'usuario');
        INSERT INTO categorias (nombre)
        VALUES 
        ('Verano Familiar'),
        ('Programacion'),
        ('Musica'),
        ('Deportes');
        INSERT INTO ciudades (nombre)
        VALUES 
        ('Playa Central'),
        ('Parque de la Ciudad'),
        ('Sala de Conferencias'),
        ('Polideportivo Municipal');
        INSERT INTO eventos (titulo, descripcion, fecha_inicio, fecha_fin, ubicacion, organizador_id, categoria_id, ciudad_id, valor, imagen_html)
        VALUES 
        ('Festival de Verano Familiar', 'Un dia lleno de actividades familiares en la playa.', '2024-08-01 10:00:00', '2024-08-01 18:00:00', 'Playa Central', 1, 1, 1, 0.00, '<img src="festival_verano.jpg" />'),
        ('Conferencia de Programacion', 'Una conferencia sobre las ultimas tendencias en programacion.', '2024-08-05 09:00:00', '2024-08-05 17:00:00', 'Sala de Conferencias', 1, 2, 3, 0.00, '<img src="conf_programacion.jpg" />');
        INSERT INTO inscripciones (usuario_id, evento_id)
        VALUES 
        (2, 1),
        (3, 2);
        SELECT * FROM usuarios;
        SELECT * FROM categorias;
        SELECT * FROM ciudades;
        SELECT * FROM eventos;
        SELECT * FROM inscripciones;
EOF
    '''
}