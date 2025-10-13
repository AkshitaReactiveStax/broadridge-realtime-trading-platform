pipeline {
  agent any

  tools {
    jdk 'jdk-17'
    maven 'maven-3.9.6'
  }

  environment {
    DOCKERHUB_USER = 'your-dockerhub-username'
    DOCKERHUB_PASS = credentials('dockerhub-credentials-id')
    OPENSHIFT_SERVER = 'https://api.rm3.7wse.p1.openshiftapps.com:6443'
    OPENSHIFT_TOKEN = credentials('openshift-token')
    OPENSHIFT_PROJECT = 'akshita2002bajaj15-dev'
  }

  stages {

    stage('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/your-repo/broadridge-realtime-trading-platform.git'
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
          docker.withRegistry('', 'dockerhub-credentials-id') {
            sh """
              docker build -t $DOCKERHUB_USER/gateway-service:latest gateway-service
              docker build -t $DOCKERHUB_USER/order-service:latest order-service
              docker build -t $DOCKERHUB_USER/trade-enrichment-service:latest trade-capture-enrichment-service
              docker push $DOCKERHUB_USER/gateway-service:latest
              docker push $DOCKERHUB_USER/order-service:latest
              docker push $DOCKERHUB_USER/trade-enrichment-service:latest
            """
          }
        }
      }
    }

    stage('Deploy to OpenShift') {
      steps {
        script {
          sh """
            echo '🔐 Logging into OpenShift...'
            oc login --token=$OPENSHIFT_TOKEN --server=$OPENSHIFT_SERVER --insecure-skip-tls-verify=true
            oc project $OPENSHIFT_PROJECT

            echo '🚀 Applying deployments...'
            oc apply -f k8s/gateway-deployment.yaml
            oc apply -f k8s/order-deployment.yaml
            oc apply -f k8s/enrichment-deployment.yaml

            echo '♻️ Restarting deployments...'
            oc rollout restart deployment/gateway-service || true
            oc rollout restart deployment/order-service || true
            oc rollout restart deployment/enrichment-service || true
          """
        }
      }
    }
  }

  post {
    success {
      echo '✅ Deployment to OpenShift successful!'
    }
    failure {
      echo '❌ Deployment failed. Check logs.'
    }
  }
}