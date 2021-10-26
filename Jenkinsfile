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
                //TODO: Change usage of root to a user with Docker privileges or invoke normal commands through ssh executable
                sshagent(credentials: ['jenkins_ssh']){
                    sh '''docker -H ssh://root@$DEPLOY_TARGET \\
                            login \\
                                --username $ARTIFACTORY_JENKINS_CREDENTIALS_USR \\
                                --password $ARTIFACTORY_JENKINS_CREDENTIALS_PSW'''

                    sh '''docker -H ssh://root@$DEPLOY_TARGET \\
                            stop $DEPLOY_CONTAINER_NAME || echo 'No $DEPLOY_CONTAINER_NAME container running' '''

                    sh '''docker -H ssh://root@$DEPLOY_TARGET \\
                            rm $DEPLOY_CONTAINER_NAME || echo 'No $DEPLOY_CONTAINER_NAME container to remove' '''

                    sh '''docker -H ssh://root@$DEPLOY_TARGET \\
                            run $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:$VERSION \\
                                -d --name $DEPLOY_CONTAINER_NAME -p 80:8080
                        '''
                }
            }
        }

    }

    environment {
        ARTIFACTORY_DOCKER_REGISTRY = 'artifactory.zerofactorial.io'
        ARTIFACTORY_DOCKER_REPOSITORY = 'flexion'
        VERSION = '0.0.8-BETA'
        DOCKER_IMAGE = 'unitconverter'
        GIT_SHORTHASH = GIT_COMMIT.take(7)
        ARTIFACTORY_JENKINS_CREDENTIALS = credentials('jenkins_artifactory')
        DEPLOY_TARGET = 'flexion-unitconverter.zerofactorial.io'
        DEPLOY_CONTAINER_NAME = 'flexion-unitconverter'
    }

}