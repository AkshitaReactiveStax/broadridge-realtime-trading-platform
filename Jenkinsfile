pipeline {
  agent any

  tools {
    jdk 'jdk-17'
    maven 'maven-3.9.11'
  }

  environment {
      DOCKERHUB_USER = 'akshitasdock'
      OPENSHIFT_SERVER = 'https://api.rm3.7wse.p1.openshiftapps.com:6443'
      OPENSHIFT_PROJECT = 'akshita2002bajaj15-dev'
      OPENSHIFT_TOKEN = credentials('openshift-token-jenkins')
  }

  stages {

    stage('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/AkshitaReactiveStax/broadridge-realtime-trading-platform.git'
      }
    }

    stage('Build with Maven') {
      steps {
        sh 'mvn clean package -Dmaven.test.skip=true'
      }
    }

    stage('Docker Build & Push') {
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials-id', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh """
              echo "🔧 Building Docker images..."
              echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin

              docker build -t ${DOCKERHUB_USER}/gateway-service:latest gateway-service
              docker build -t ${DOCKERHUB_USER}/order-service:latest order-service
              docker build -t ${DOCKERHUB_USER}/trade-enrichment-service:latest trade-capture-enrichment-service

              docker push ${DOCKERHUB_USER}/gateway-service:latest
              docker push ${DOCKERHUB_USER}/order-service:latest
              docker push ${DOCKERHUB_USER}/trade-enrichment-service:latest

              docker logout
            """
          }
        }
      }
    }

    stage('Deploy to OpenShift') {
      steps {
        script {
          // Use the OpenShift secret token here
          withCredentials([string(credentialsId: 'openshift-token-jenkins', variable: 'OPENSHIFT_TOKEN')]) {
              sh """
                echo "🔐 Logging into OpenShift..."
                oc login --token=$OPENSHIFT_TOKEN --server=$OPENSHIFT_SERVER --insecure-skip-tls-verify=true
                oc project $OPENSHIFT_PROJECT

                echo "🚀 Applying Deployments..."
                oc apply -f k8s/gateway-deployment.yaml
                oc apply -f k8s/order-deployment.yaml
                oc apply -f k8s/enrichment-deployment.yaml

                echo "♻️ Restarting Deployments..."
                oc rollout restart deployment/gateway-service || true
                oc rollout restart deployment/order-service || true
                oc rollout restart deployment/enrichment-service || true
              """
          }
        }
      }
    }
  }

  post {
    success {
      echo '✅ Deployment to OpenShift successful!'
    }
    failure {
      echo '❌ Deployment failed. Check Jenkins logs.'
    }
  }
}