pipeline {
    agent any

    environment {
        IMAGE_NAME = "ngyu/auth-api"
        IMAGE_TAG = "latest"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'DockerHub_nGyu',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                    kaniko \
                        --context . \
                        --dockerfile ./Dockerfile \
                        --destination $IMAGE_NAME:$IMAGE_TAG \
                        --registry-username=$DOCKER_USER \
                        --registry-password=$DOCKER_PASS
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'kubectl rollout restart deployment auth-api'
            }
        }
    }
}
