pipeline {
    agent any

    environment {
        IMAGE_NAME = "vpdls1511/auth-api"
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
                sh 'ls build/libs/'  // 실제 파일명 확인
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
                    printf '{"auths":{"https://index.docker.io/v1/":{"auth":"%s"}}}' \
                        $(printf '%s:%s' $DOCKER_USER $DOCKER_PASS | base64 -w 0) \
                        > /kaniko/.docker/config.json
                    export DOCKER_CONFIG=/kaniko/.docker
                    kaniko \
                        --context . \
                        --dockerfile ./Dockerfile \
                        --destination vpdls1511/auth-api:latest
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
