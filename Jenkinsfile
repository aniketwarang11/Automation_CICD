pipeline {
  agent any

  tools {
    maven "MAVEN_HOME"
	jdk "JAVA_17"
  }

  stages {
    stage('fetch code') {
      steps {
        git branch: 'main', url: 'https://github.com/aniketwarang11/Automation_CICD.git'
      }
    }
	
    stage('build-app') {
      steps {
        sh 'mvn clean install -DskipTests'
      }
    }
	
    stage('app-compile') {
      steps {
        sh 'mvn clean compile -DskipTests'
      }
    }

    stage('Code analysis with sonarqube') {
	
    environment{
    scannerHome = tool 'sonar-scanner-6'
    }
	
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
  
