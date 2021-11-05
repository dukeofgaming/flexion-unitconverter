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

## Building & running locally

In order to build and run from the host, once you have installed and configured all the required dependencies:

```shell
export FLEXION_UNITCONVERTER_APP_VERSION="1.2.3"
gradle build -P version="${FLEXION_UNITCONVERTER_APP_VERSION}"
cd src/build/libs
java -jar -Dserver.port=8080 unitconverter-${FLEXION_UNITCONVERTER_APP_VERSION}.jar
```

By default, the application will run on port 8080, but you can use the -D system property argument to change it 
to something different.

## Building & running with Docker

The [Dockerfile](Dockerfile) at the root of this project already contains every dependency needed for building 
and it is separated in a multi-stage build, where the build stage is based on Ubuntu, where it will produce the 
required JAR file that will be passed to the second stage and final production image based on the OpenJDK Alpine
distribution.

It will also set the environment variable `FLEXION_UNITCONVERTER_APP_VERSION` which is read by the application 
to display in the main UI.

```shell
docker build --build-arg BUILD_VERSION="1.2.3" \
        -t artifactory.zerofactorial.io/flexion/unitconverter:1.2.3
        
docker run -d \
        -p 80:8080 \
        --name flexion-unitconverter \
        artifactory.zerofactorial.io/flexion/unitconverter:1.2.3
```
## Running in Kubernetes (Docker Desktop)

You can refer to the resource description files in [k8s/](k8s/). Note that the Ingress resource utilizes the 
Kubernetes nginx Ingress class, so [the NGINX Ingress Controller](https://kubernetes.github.io/ingress-nginx/deploy/#docker-desktop) must be installed first:

```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.0.4/deploy/static/provider/cloud/deploy.yaml
kubectl create namespace flexion
```
After that, you will need to either do a `docker login artifactory.zerofactorial.io` with the credentials supplied by 
Uzair, and then pull the image referenced in [k8s/unitconverter-deployment.yml](k8s/unitconverter-deployment.yml), or 
preferably create a docker-registry secret with the name referenced as an imagePullSecret:

```shell
kubectl --namespace flexion \
        create secret docker-registry artifactory.zerofactorial.io-docker-registry-secret \
        --docker-server=artifactory.zerofactorial.io \
        --docker-username=${ZEROFACTORIAL_JCR_USERNAME} \
        --docker-password=${ZEROFACTORIAL_JCR_PASSWORD}
```

Then:

```shell
kubectl --namespace flexion apply -f k8s/unitconverter-deployment.yml
kubectl --namespace flexion apply -f k8s/unitconverter-service.yml
kubectl --namespace flexion apply -f k8s/unitconverter-ingress.yml
```
A succesfull deployment should show a host correctly mapped to the service endpoints for the pods in the deployment:

```shell
> kubectl --namespace flexion describe ingress unitconverter-ingress

Name:             unitconverter-ingress
Namespace:        flexion
Address:          localhost
Default backend:  default-http-backend:80 (<error: endpoints "default-http-backend" not found>)
Rules:
  Host                     Path  Backends
  ----                     ----  --------
  unitconverter.localhost
                           /   unitconverter-service:8080 (10.1.1.53:8080,10.1.1.54:8080,10.1.1.56:8080 + 7 more...)
Annotations:               kubernetes.io/ingress.class: nginx
                           nginx.ingress.kubernetes.io/rewrite-target: /
Events:                    <none>
```

As well as an ingress with an assigned

```shell
> kubectl --namespace flexion get ingress
NAME                    CLASS    HOSTS                     ADDRESS     PORTS   AGE
unitconverter-ingress   <none>   unitconverter.localhost   localhost   80      5h49m

```

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

## Live Environments

### Staging

[http://unitconverter-staging.zerofactorial.io/](http://unitconverter-staging.zerofactorial.io/)

* EC2 T2.Micro instance 
* Elastic IP bound to EC2 instance
* Regular VPC
* Deployed on master branch updates through Jenkins




### Production

* AWS EKS provisioned through `eksctl`:
  ```shell
  > eksctl create cluster \
            --name flexion-k8s-cluster \
            --region us-east-2 \
            --with-oidc \
            --ssh-access \
            --ssh-public-key flexion-unitconverter
  ```
  <details>
    <summary>Shell log</summary>
  
  ```shell
  2021-11-04 18:59:01 [ℹ]  eksctl version 0.70.0
  2021-11-04 18:59:01 [ℹ]  using region us-east-2
  2021-11-04 18:59:01 [ℹ]  setting availability zones to [us-east-2b us-east-2c us-east-2a]
  2021-11-04 18:59:01 [ℹ]  subnets for us-east-2b - public:192.168.0.0/19 private:192.168.96.0/19
  2021-11-04 18:59:01 [ℹ]  subnets for us-east-2c - public:192.168.32.0/19 private:192.168.128.0/19
  2021-11-04 18:59:01 [ℹ]  subnets for us-east-2a - public:192.168.64.0/19 private:192.168.160.0/19
  2021-11-04 18:59:01 [ℹ]  nodegroup "ng-83061225" will use "" [AmazonLinux2/1.20]
  2021-11-04 18:59:01 [ℹ]  using EC2 key pair %!q(*string=<nil>)
  2021-11-04 18:59:01 [ℹ]  using Kubernetes version 1.20
  2021-11-04 18:59:01 [ℹ]  creating EKS cluster "flexion-k8s-cluster" in "us-east-2" region with managed nodes
  2021-11-04 18:59:01 [ℹ]  will create 2 separate CloudFormation stacks for cluster itself and the initial managed nodegroup
  2021-11-04 18:59:01 [ℹ]  if you encounter any issues, check CloudFormation console or try 'eksctl utils describe-stacks --region=us-east-2 --cluster=flexion-k8s-cluster'
  2021-11-04 18:59:01 [ℹ]  CloudWatch logging will not be enabled for cluster "flexion-k8s-cluster" in "us-east-2"
  2021-11-04 18:59:01 [ℹ]  you can enable it with 'eksctl utils update-cluster-logging --enable-types={SPECIFY-YOUR-LOG-TYPES-HERE (e.g. all)} --region=us-east-2 --cluster=flexion-k8s-cluster'
  2021-11-04 18:59:01 [ℹ]  Kubernetes API endpoint access will use default of {publicAccess=true, privateAccess=false} for cluster "flexion-k8s-cluster" in "us-east-2"
  2021-11-04 18:59:01 [ℹ]
  2 sequential tasks: { create cluster control plane "flexion-k8s-cluster",
  2 sequential sub-tasks: {
  4 sequential sub-tasks: {
  wait for control plane to become ready,
  associate IAM OIDC provider,
  2 sequential sub-tasks: {
  create IAM role for serviceaccount "kube-system/aws-node",
  create serviceaccount "kube-system/aws-node",
  },
  restart daemonset "kube-system/aws-node",
  },
  create managed nodegroup "ng-83061225",
  }
  }
  2021-11-04 18:59:01 [ℹ]  building cluster stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 18:59:02 [ℹ]  deploying stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 18:59:32 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:00:02 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:01:03 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:02:03 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:03:03 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:04:03 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:05:04 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:06:04 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:07:04 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:08:05 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:09:05 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:10:05 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:11:05 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-cluster"
  2021-11-04 19:15:08 [ℹ]  building iamserviceaccount stack "eksctl-flexion-k8s-cluster-addon-iamserviceaccount-kube-system-aws-node"
  2021-11-04 19:15:08 [ℹ]  deploying stack "eksctl-flexion-k8s-cluster-addon-iamserviceaccount-kube-system-aws-node"
  2021-11-04 19:15:08 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-addon-iamserviceaccount-kube-system-aws-node"
  2021-11-04 19:15:25 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-addon-iamserviceaccount-kube-system-aws-node"
  2021-11-04 19:15:42 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-addon-iamserviceaccount-kube-system-aws-node"
  2021-11-04 19:15:42 [ℹ]  serviceaccount "kube-system/aws-node" already exists
  2021-11-04 19:15:42 [ℹ]  updated serviceaccount "kube-system/aws-node"
  2021-11-04 19:15:42 [ℹ]  building managed nodegroup stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:15:42 [ℹ]  deploying stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:15:42 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:16:15 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:16:31 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:16:48 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:17:04 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:17:21 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:17:38 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:17:55 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:18:15 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:18:32 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:18:51 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:19:08 [ℹ]  waiting for CloudFormation stack "eksctl-flexion-k8s-cluster-nodegroup-ng-83061225"
  2021-11-04 19:19:08 [ℹ]  waiting for the control plane availability...
  2021-11-04 19:19:08 [✔]  saved kubeconfig as "C:\\Users\\dukeo\\.kube\\config"
  2021-11-04 19:19:08 [ℹ]  no tasks
  2021-11-04 19:19:08 [✔]  all EKS cluster resources for "flexion-k8s-cluster" have been created
  2021-11-04 19:19:09 [ℹ]  nodegroup "ng-83061225" has 2 node(s)
  2021-11-04 19:19:09 [ℹ]  node "ip-192-168-5-149.us-east-2.compute.internal" is ready
  2021-11-04 19:19:09 [ℹ]  node "ip-192-168-80-165.us-east-2.compute.internal" is ready
  2021-11-04 19:19:09 [ℹ]  waiting for at least 2 node(s) to become ready in "ng-83061225"
  2021-11-04 19:19:09 [ℹ]  nodegroup "ng-83061225" has 2 node(s)
  2021-11-04 19:19:09 [ℹ]  node "ip-192-168-5-149.us-east-2.compute.internal" is ready
  2021-11-04 19:19:09 [ℹ]  node "ip-192-168-80-165.us-east-2.compute.internal" is ready
  2021-11-04 19:19:10 [ℹ]  kubectl command should work with "C:\\Users\\dukeo\\.kube\\config", try 'kubectl get nodes'
  2021-11-04 19:19:10 [✔]  EKS cluster "flexion-k8s-cluster" in "us-east-2" region is ready
  ```
  </details>  
  <br>
* [AWS NLB](https://kubernetes.github.io/ingress-nginx/deploy/#network-load-balancer-nlb) used to expose NGINX Ingress controller
  ```shell
  kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.0.4/deploy/static/provider/aws/deploy.yaml
  ```
* Secret of type docker-registry to pull image from artifactory.zerofactorial.io:
  ```shell
  kubectl --namespace flexion \
  create secret docker-registry artifactory.zerofactorial.io-docker-registry-secret \
  --docker-server=artifactory.zerofactorial.io \
  --docker-username=${ZEROFACTORIAL_JCR_USERNAME} \
  --docker-password=${ZEROFACTORIAL_JCR_PASSWORD}

  ```

## Future improvements

* Provision AWS EKS Cluster through Terraform instead of command line. Currently, the AWS EKS Cluster was provided via 
  `eksctl`
* Provision AWS EC2 Instance for staging through Terraform as well, currently, this was deployed manually.
* Add HTTPS to both staging and production