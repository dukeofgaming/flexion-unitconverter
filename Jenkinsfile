pipeline {
    agent any

    stages {
        stage('Build & Test') {
            steps {
                //Since --ouput prevents tagging/storing of images, we need to tag first
                sh '''docker build --rm=false \\
                        --build-arg BUILD_VERSION="$VERSION-$GIT_SHORTHASH" \\
                        -t $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:$VERSION \\
                        -t $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:$VERSION-$GIT_SHORTHASH \\
                        -t $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:latest .'''

                //Using buildkit integration build output to export artifacts and test results
                sh '''docker build --output dockerout .'''

                //Touching test results to avoid needless build error
                sh '''touch dockerout/app/test-results/test/*.xml'''
                
                archiveArtifacts artifacts: 'dockerout/app/*.jar', fingerprint: true
                junit 'dockerout/app/test-results/test/*.xml'
            }
        }

        stage('Publish') {

            when {
                anyOf {
                    branch 'develop';
                    branch 'release/*';
                }
            }

            steps {
                script {
                    def server = Artifactory.server 'local'

                    def rtDocker = Artifactory.docker server: server

                    //latest tag
                    def buildInfo_latest = rtDocker.push env.ARTIFACTORY_DOCKER_REGISTRY + '/'+env.ARTIFACTORY_DOCKER_REPOSITORY+'/'+env.DOCKER_IMAGE+':latest', env.ARTIFACTORY_DOCKER_REPOSITORY
                    server.publishBuildInfo buildInfo_latest

                    //Versioned tag
                    def buildInfo_version = rtDocker.push env.ARTIFACTORY_DOCKER_REGISTRY + '/'+env.ARTIFACTORY_DOCKER_REPOSITORY+'/'+env.DOCKER_IMAGE+':'+env.VERSION, env.ARTIFACTORY_DOCKER_REPOSITORY
                    server.publishBuildInfo buildInfo_version

                    //Versioned unique tag
                    def buildInfo_version_shorthash = rtDocker.push env.ARTIFACTORY_DOCKER_REGISTRY + '/'+env.ARTIFACTORY_DOCKER_REPOSITORY+'/'+env.DOCKER_IMAGE+':'+env.VERSION+'-'+env.GIT_SHORTHASH, env.ARTIFACTORY_DOCKER_REPOSITORY
                    server.publishBuildInfo buildInfo_version_shorthash
                }
            }
        }

        stage('Deploy to Staging') {

            when {
                anyOf {
                    branch 'develop';
                    branch 'release/*';
                }
            }

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
                                $ARTIFACTORY_DOCKER_REGISTRY/$ARTIFACTORY_DOCKER_REPOSITORY/$DOCKER_IMAGE:$VERSION-$GIT_SHORTHASH" '''
                }
            }
        }

    }

    environment {
        ARTIFACTORY_DOCKER_REGISTRY     = 'artifactory.zerofactorial.io'
        ARTIFACTORY_DOCKER_REPOSITORY   = 'flexion'
        VERSION                         = '0.1.0'
        DOCKER_IMAGE                    = 'unitconverter'
        GIT_SHORTHASH                   = GIT_COMMIT.take(7)
        ARTIFACTORY_JENKINS_CREDENTIALS = credentials('jenkins_artifactory')
        DEPLOY_TARGET                   = 'unitconverter-staging.zerofactorial.io'
        DEPLOY_CONTAINER_NAME           = 'flexion-unitconverter'
    }

}