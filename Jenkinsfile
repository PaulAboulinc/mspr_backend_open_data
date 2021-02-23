pipeline {
    agent none
//     environment {
//         BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"
//         ENV_NAME = getEnvName(env.BRANCH_NAME)
//     }
    stages {
//         stage('Build docker') {
//             agent {
//                 docker { image 'maven:3.6.0-jdk-8-slim'}
//             }
//             steps {
//                 sh 'docker-compose -f docker-compose.${ENV_NAME}.yml up --build -d'
//             }
//         }
        stage('Build') {
            agent {
                docker { image 'maven:3.6.0-jdk-8-slim'}
            }
            steps {
                sh 'mvn -Pprod -B -DskipTests package'
            }
        }
//         stage('Test') {
//             agent {
//                 docker { image 'maven:3.6.0-jdk-8-slim'}
//             }
//             steps {
//                 sh 'mvn -P${ENV_NAME} -B test'
//             }
//         }
//         stage('JaCoCo report') {
//             agent {
//                 docker { image 'maven:3.6.0-jdk-8-slim'}
//             }
//             steps {
//                 sh 'mvn -P${ENV_NAME} -B jacoco:report'
//             }
//         }
//         stage('Sonarqube') {
//             agent {
//                 docker { image 'maven:3.6.0-jdk-8-slim'}
//             }
//             steps {
//                 sh 'mvn -P${ENV_NAME} -B sonar:sonar'
//             }
//         }
//         stage('Down Build container') {
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

def getEnvName(branchName) {
    if (branchName.startsWith("release-")) {
        return 'prod';
    } else if (branchName == "preprod") {
        return 'preprod';
    }

    return "dev";
}