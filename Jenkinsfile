pipeline {
    agent none
    stages {
        stage('Set environment') {
            agent any
            steps {
                script {
                    env.BRANCH_NAME = "${env.GIT_BRANCH.replaceFirst(/^.*\//, '')}"
                    env.ENV_NAME = getEnvName(env.BRANCH_NAME)
                }
            }
        }
        stage('Build') {
            agent {
                docker { image 'maven:3.6.0-jdk-8-slim'}
            }
            steps {
                sh 'mvn clean package -DskipTests -P${ENV_NAME}'
            }
        }
        stage('Test') {
            agent {
                docker { image 'maven:3.6.0-jdk-8-slim'}
            }
            steps {
                sh 'mvn -P${ENV_NAME} -B test'
                sh 'mvn -P${ENV_NAME} -B jacoco:report'
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
        stage("Sonarqube build and analysis") {
            agent {
                docker { image 'maven:3.6.0-jdk-8-slim'}
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -P${ENV_NAME} -B sonar:sonar'
                }
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
//         stage("Sonarqube quality gate") {
//             steps {
//                 timeout(time: 1, unit: 'HOURS') {
//                     waitForQualityGate abortPipeline: true
//                 }
//             }
//         }
        stage('Deploy') {
            agent any
            when {
                expression { ENV_NAME == 'preprod' || ENV_NAME == 'prod' }
            }
            steps {
                sh 'docker-compose -p backend_${ENV_NAME} -f docker-compose.${ENV_NAME}.yml up --build -d'
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

def getEnvName(branchName) {
    if (branchName.startsWith("release-")) {
        return 'prod';
    } else if (branchName == "preprod") {
        return 'preprod';
    }

    return "dev";
}