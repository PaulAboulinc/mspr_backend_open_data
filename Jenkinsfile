def deploy = true;

pipeline {
    agent any
    stages {
        stage('Build docker') {
            steps {
                sh 'docker-compose up --build -d'
            }
        }
        stage('Test') {
            steps {
                sh 'docker exec recipe_back_mspr mvn -B -f /home/app/pom.xml test'
            }
            post {
                failure {
                    script { deploy = false }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'docker exec recipe_back_mspr mvn -B -f /home/app/pom.xml -DskipTests package'
            }
            post {
                failure {
                    script { deploy = false }
                }
            }
        }
        stage('SonarQube') {
            steps {
                sh 'docker exec recipe_back_mspr mvn -B -f /home/app/pom.xml sonar:sonar'
            }
            post {
                failure {
                    script { deploy = false }
                }
            }
        }
        stage('Deploy') {
            when { expression { deploy == true }}
            steps {
                sh 'docker-compose down'
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
