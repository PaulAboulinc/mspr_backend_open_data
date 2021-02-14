pipeline {
    agent any
    stages {
        stage('Build docker') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'master') {
                        sh 'docker-compose -f docker-composer.prod.yml up --build -d'
                    } else {
                        sh 'docker-compose up --build -d'
                    }
                }
            }
        }
        stage('Test') {
            steps {
                sh 'docker exec api_backend mvn -B -f /home/app/pom.xml test'
            }
        }
        stage('JaCoCo report') {
            steps {
                sh 'docker exec api_backend mvn -B -f /home/app/pom.xml jacoco:report'
            }
        }
        stage('Build') {
            steps {
                sh 'docker exec api_backend mvn -B -f /home/app/pom.xml -DskipTests package'
            }
        }
        stage('SonarQube') {
            steps {
                sh 'docker exec api_backend mvn -B -f /home/app/pom.xml sonar:sonar'
            }
        }
    }
    post {
        always {
            emailext to: "paul.aboulinc@gmail.com",
                     subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                     attachLog: true,
                     body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}"
        }
    }
}
