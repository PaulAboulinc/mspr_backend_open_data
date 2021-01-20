pipeline {
//     agent {
//         dockerfile true
//     }
    agent any
    stages {
        stage('Install Maven') {
            steps {
                sh 'export MAVEN_HOME=/opt/maven'
                sh 'export PATH=$PATH:$MAVEN_HOME/bin'
                sh 'mvn --version'
            }
        }
        stage('Test') {
            steps {
                sh 'echo "mvn test"'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Code Quality') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.projectKey=com.pafpsdnc:recipe -Dsonar.host.url=http://thedawndev.fr:9001 -Dsonar.login=b138897801d4014f1a4c1487ead8491e177fcdb3'
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