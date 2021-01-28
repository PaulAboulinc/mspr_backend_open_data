def hasFailed = false;

pipeline {
    agent any
    stages {
        stage('Init docker') {
            steps {
                sh 'docker-compose up --build -d'
            }
        }
        stage('Test') {
            steps {
                sh 'docker exec -it app mvn -B -f /home/app/pom.xml test'
            }
            post {
                failure {
                    script { hasFailed = true }
                }
            }
        }
        stage('Build') {
            when { expression { hasFailed == false }}
            steps {
                sh 'docker exec -it app mvn -B -f /home/app/pom.xml -DskipTests package'}
            }
            post {
                failure {
                    script { hasFailed = true }
                }
            }
        }
        stage('SonarQube') {
            when { expression { hasFailed == false }}
            steps {
                sh 'docker exec -it app mvn -B -f /home/app/pom.xml sonar:sonar'}
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
