pipeline {
    agent any
    environment {
        BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"
        ENV_NAME = "${BRANCH_NAME == "preprod" ? BRANCH_NAME : ((GIT_TAG != "null") ? "prod" : "dev")}"
    }
    stages {
        stage('Build docker') {
            steps {
                    echo GIT_TAG
                    echo BRANCH_NAME
                    echo ENV_NAME
                    echo sh(script: 'env|sort', returnStdout: true)
//                     sh 'docker-compose -f docker-compose.${ENV_NAME}.yml up --build -d'
            }
        }
//         stage('Test') {
//             steps {
//                 sh 'docker exec api_backend_${ENV_NAME} mvn -P${ENV_NAME} -B -f /home/app/pom.xml test'
//             }
//         }
//         stage('JaCoCo report') {
//             steps {
//                 sh 'docker exec api_backend_${ENV_NAME} mvn -P${ENV_NAME} -B -f /home/app/pom.xml jacoco:report'
//             }
//         }
//         stage('Build') {
//             steps {
//                 sh 'docker exec api_backend_${ENV_NAME} mvn -P${ENV_NAME} -B -f /home/app/pom.xml -DskipTests package'
//             }
//         }
//         stage('Sonarqube') {
//             steps {
//                 sh 'docker exec api_backend_${ENV_NAME} mvn -P${ENV_NAME} -B -f /home/app/pom.xml sonar:sonar'
//             }
//         }
//         stage('Down build container') {
//             when {
//                 expression { ENV_NAME == 'dev' }
//             }
//             steps {
//                 sh 'docker-compose -f docker-compose.${ENV_NAME}.yml down'
//             }
//         }
    }
//     post {
//         always {
//             emailext to: "nonstopintegration@gmail.com",
//                      subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
//                      attachLog: true,
//                      body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}"
//         }
//     }
}
