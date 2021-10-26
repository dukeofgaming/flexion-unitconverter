pipeline {
    agent any

    stages {
        stage('Docker Image Build & Push') {
            steps {
                sh '''#!/bin/bash -xe

                    BUILD_VERSION="$VERSION-$GIT_SHORTHASH"

                    docker build --rm=false \\
                        --build-arg BUILD_VERSION="$BUILD_VERSION" \\
                        -t $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:$VERSION \\
                        -t $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:latest .'''

                script {
                    def server = Artifactory.server 'local'

                    def rtDocker = Artifactory.docker server: server

                    //latest tag
                    def buildInfo_latest = rtDocker.push env.ARTIFACTORY_DOCKER_REGISTRY + '/'+env.ARTIFACTORY_DOCKER_REPOSITORY+'/'+env.DOCKER_IMAGE+':latest', env.ARTIFACTORY_DOCKER_REPOSITORY
                    server.publishBuildInfo buildInfo_latest

                    //Versioned tag
                    def buildInfo_version = rtDocker.push env.ARTIFACTORY_DOCKER_REGISTRY + '/'+env.ARTIFACTORY_DOCKER_REPOSITORY+'/'+env.DOCKER_IMAGE+':'+env.VERSION, env.ARTIFACTORY_DOCKER_REPOSITORY
                    server.publishBuildInfo buildInfo_version
                }
            }
        }

        stage('Deploy') {
            steps {

                sshagent(credentials: ['jenkins_ssh']){
                    sh '''[ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                            ssh-keyscan -t rsa,dsa $DEPLOY_TARGET >> ~/.ssh/known_hosts'''

                    sh '''ssh root@$DEPLOY_TARGET \\
                            "docker login \\
                                $ARTIFACTORY_DOCKER_REGISTRY \\
                                --username $ARTIFACTORY_JENKINS_CREDENTIALS_USR \\
                                --password $ARTIFACTORY_JENKINS_CREDENTIALS_PSW" '''

                    sh '''ssh root@$DEPLOY_TARGET \\
                            "docker stop $DEPLOY_CONTAINER_NAME \\
                                || echo 'No $DEPLOY_CONTAINER_NAME container running'" '''

                    sh '''ssh root@$DEPLOY_TARGET \\
                            "docker rm $DEPLOY_CONTAINER_NAME \\
                                || echo 'No $DEPLOY_CONTAINER_NAME container to remove'" '''

                    sh '''ssh root@$DEPLOY_TARGET \\
                            "docker run \\
                                -d \\
                                --name $DEPLOY_CONTAINER_NAME \\
                                -p 80:8080 \\
                                $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:$VERSION" '''
                }
            }
        }

    }

    environment {
        ARTIFACTORY_DOCKER_REGISTRY = 'artifactory.zerofactorial.io'
        ARTIFACTORY_DOCKER_REPOSITORY = 'flexion'
        VERSION = '0.0.9-BETA'
        DOCKER_IMAGE = 'unitconverter'
        GIT_SHORTHASH = GIT_COMMIT.take(7)
        ARTIFACTORY_JENKINS_CREDENTIALS = credentials('jenkins_artifactory')
        DEPLOY_TARGET = 'flexion-unitconverter.zerofactorial.io'
        DEPLOY_CONTAINER_NAME = 'flexion-unitconverter'
    }

}