# Flexion: DevOps Challenge

## Dependencies

All of the dependencies are handled by Docker in the build stage of the root Dockerfile, however, in order to build 
the project as is, the folowing dependencies are required:

* Docker v20.10.8+ (with buildkit enabled)
* Docker Compose v 1.29.2+
* Gradle v7.2+
* nodeJS v16.9.0+
* OpenJDK 16

If running in Windows, using WSL2 is recommended, make sure everything your environment is properly setup under bash. 

In order to build and run from the host:

```shell
gradle build -P version="1.2.3-STABLE"
cd src/build/libs
java -jar -Dserver.port=8080 unitconverter-0.0.1-SNAPSHOT.jar
```

By default, the application will run on port 8080, but you can use the -D system property argument to change it 
to something different.

## CICD Environment

The CICD environment uses Jenkins to builld &n deploy Docker images which it then publishes to Artifactory as a 
container registry:

* Jenkins: https://jenkins.zerofactorial.io/blue
* jFrog Container Registry: https://artifactory.zerofactorial.io

Getting the CICD environment deployed locally with HTTPS is possible by using the bundled certs in 
`cicd/nginx/etc/ssl/certs`, these were created through [Minica](https://github.com/jsha/minica), however, for production
[Certbot](https://certbot.eff.org/lets-encrypt/pip-nginx) was used to generate a wildcard certificate. 

To start the environment, simply change to the `cicd/` directory and issue:
```shell
docker-compose build 
docker-compose up -d
```

Some considerations:

* Two local custom images will be built:  
  <br>
  - **nginx**: Will include the bundled configuration files, which are portable, as well as including the bundled 
  certs in the aforementioned folder. nginx will take care of assigning a subdomain corresponding to the application: 
  `jenkins.*` for Jenkins and `artifactory.*` for jFrog Container Registry. In a default local setting, this would be e.g. 
  `jenkins.localhost`.  
  <br>
  - **Jenkins**: For local execution, Jenkins can use DID (Docker-In-Docker) by having a local installation
  of Docker within the container accessing the host's by communicating to the Docker daemon through `/var/run/docker.sock`, 
  this can be enabled by uncommenting the volume mount for that file in the Docker Commpose file. Such mechanism is not 
  suited for production in a real setting as a malicious Jenkinsfile could be used to stop/delete the host's containers.
  <br>  
  At the **zerofactorial.io** Jenkins is configured to use the AWS EC2 plugin to spin up a separate build machine upon 
  a new job request. The T2.Small instance will take ~2.5 minutes to come up, after that it will proceed to execute the 
  Jenkinsfile, which will only publish images if the built branch is "develop" or "master", and it will only deploy on
  "master".



## Future improvements

* ...
