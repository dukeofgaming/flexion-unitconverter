pipeline {
  agent any
  stages {
    stage('Docker Image Build & Push') {
      steps {
        sh '''#!/bin/bash -xe

BUILD_VERSION="$VERSION-$GIT_BRANCH-$BUILD_NUMBER"

docker build \\
--build-arg BUILD_VERSION="$BUILD_VERSION" \\
-t artifactory.localhost/flexion/unitconverter:$VERSION \\
-t artifactory.localhost/flexion/unitconverter:latest .'''
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

  }
  environment {
    ARTIFACTORY_DOCKER_REGISTRY = 'artifactory.localhost'
    ARTIFACTORY_DOCKER_REPOSITORY = 'flexion'
    VERSION = '0.0.8-BETA'
    DOCKER_IMAGE = 'unitconverter'
  }
}