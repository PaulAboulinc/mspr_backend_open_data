def deploy = true;

pipeline {
    agent {
        dockerfile true
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                failure {
                    script { deploy = false }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests package'
            }
            post {
                failure {
                    script { deploy = false }
                }
            }
        }
        stage('SonarQube') {
            steps {
                sh 'mvn -B sonar:sonar'
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
                sh 'docker-compose up --build -d'
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
