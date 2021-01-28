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
                withDockerContainer("recipe_back_mspr") { sh 'echo "mvn test"'}
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
                withDockerContainer("recipe_back_mspr") { sh 'echo "mvn -B -DskipTests package"'}
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
                withDockerContainer("recipe_back_mspr") { sh 'echo "mvn sonar:sonar"'}
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
