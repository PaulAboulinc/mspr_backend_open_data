pipeline {
    agent any
    environment {
        BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"
        ENV_NAME = "${BRANCH_NAME == "preprod" || BRANCH_NAME == "prod" ? BRANCH_NAME : "integration"}"
        HAS_TAG = "${sh(script:'git tag --contains | head -1', returnStdout: true)}.trim()"
    }
    stages {
        stage('echo variables ') {
            steps {
                echo ENV_NAME
                echo BRANCH_NAME
                echo HAS_TAG
            }
        }
//         stage('Build docker') {
//             steps {
//                 sh 'docker-compose up --build -d'
//                 echo ENV_NAME
//                 echo HAS_TAG
//             }
//         }
//         stage('Test') {
//             steps {
//                 sh 'docker exec api_backend mvn -B -f /home/app/pom.xml test'
//             }
//         }
//         stage('JaCoCo report') {
//             steps {
//                 sh 'docker exec api_backend mvn -B -f /home/app/pom.xml jacoco:report'
//             }
//         }
//         stage('Build') {
//             when { tag "release-*" }
//             steps {
//                 sh 'docker exec api_backend mvn -B -f /home/app/pom.xml -DskipTests package'
//             }
//         }
//         stage('SonarQube') {
//             steps {
//                 sh 'docker exec api_backend mvn -B -f /home/app/pom.xml sonar:sonar'
//             }
//         }
    }
//     post {
//         always {
//             emailext to: "paul.aboulinc@gmail.com",
//                      subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
//                      attachLog: true,
//                      body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}"
//             sh 'docker-compose down'
//         }
//     }
}
