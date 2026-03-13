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
                sh '/usr/local/bin/gradle clean build -x test'
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'DockerHub_nGyu',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    mkdir -p /kaniko/.docker
                    echo "{\\"auths\\":{\\"https://index.docker.io/v1/\\":{\\"auth\\":\\"$(echo -n $DOCKER_USER:$DOCKER_PASS | base64)\\"}}}" > /kaniko/.docker/config.json
                    kaniko \
                        --context . \
                        --dockerfile ./Dockerfile \
                        --destination ngyu/auth-api:latest
                    '''
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
