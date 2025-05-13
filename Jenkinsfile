pipeline {
  agent any

  tools {
    // Add this only if SonarScanner is configured as a tool in Jenkins
    // sonarScanner 'SonarScanner'
  }

  environment {
    SONAR_TOKEN = credentials('sonarcloud-token')
  }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/aniketwarang11/Automation_CICD.git'
      }
    }

    stage('SonarCloud Analysis') {
      steps {
        withSonarQubeEnv('Sonar-server') {
  sh '''{scannerHome}/bin/sonar-scanner \
      -Dsonar.projectKey=cucumber_bdd \
	  -Dsonar.projectName=cucumber_bdd \
	  -Dsonar.projectVersion=1.0 \
	  -Dsonar.sources=src/main/java \
	  -Dsonar.java.binaries=src/main/java \
      -Dsonar.organization=automation_cicd'''
		}
      }
    }

    stage('Build or Test') {
      steps {
        echo 'Run your build/test steps here'
      }
    }
  }
}
