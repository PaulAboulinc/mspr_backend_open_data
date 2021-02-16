pipeline {
    agent any
    environment {
        GIT_TAG = "${sh(script:'git tag --contains | head -1', returnStdout: true).trim() ?: "null"}"
        BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"
        ENV_NAME = "${BRANCH_NAME == "preprod" || (BRANCH_NAME == "prod" && GIT_TAG != "null") ? BRANCH_NAME : "build"}"
    }
    stages {
        stage('Build docker') {
            steps {
                sh 'docker-compose -f docker-compose.${ENV_NAME}.yml up --build -d'
            }
        }
        stage('Test') {
            steps {
                sh 'docker exec api_backend_${ENV_NAME} mvn -B -f /home/app/pom.xml test'
            }
        }
        stage('JaCoCo report') {
            steps {
                sh 'docker exec api_backend_${ENV_NAME} mvn -B -f /home/app/pom.xml jacoco:report'
            }
        }
        stage('Build') {
            steps {
                sh 'docker exec api_backend_${ENV_NAME} mvn -B -f /home/app/pom.xml -DskipTests package'
            }
        }
        stage('SonarQube') {
            steps {
                sh 'docker exec api_backend_${ENV_NAME} mvn -B -f /home/app/pom.xml sonar:sonar'
            }
        }
        stage('Down build container') {
            when {
                expression { ENV_NAME == 'build' }
            }
            steps {
                sh 'docker-compose -f docker-compose.${ENV_NAME}.yml down'
            }
        }
    }
    post {
        always {
            emailext to: "nonstopintegration@gmail.com",
                     subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                     attachLog: true,
                     body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}"
        }
    }
}
