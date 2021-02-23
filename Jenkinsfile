pipeline {
    agent any
    environment {
        BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"
    }
    stages {
        stage('Set Environmnet'){
            steps {
                script {
                    if (BRANCH_NAME.startsWith("release-")) {
                        ENV_NAME = 'prod'
                    } else if (BRANCH_NAME == "preprod") {
                        ENV_NAME = 'preprod'
                    } else {
                        ENV_NAME = 'dev'
                    }
                }
            }

        }
        stage('Build docker') {
            steps {
                    echo BRANCH_NAME
                    echo env.GIT_TAG
                    echo ENV_NAME
//                     sh 'docker-compose -f docker-compose.${ENV_NAME}.yml up --build -d '
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
