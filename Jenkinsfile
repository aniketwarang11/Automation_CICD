Sonar cloud token -> 933d983a6c810601f7b432418d667c43613185b8


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
  bat 'C:\\Softwares\\sonar-scanner-7.1.0.4889-windows-x64\\bin\\sonar-scanner.bat ^
      -Dsonar.projectKey=cucumber_bdd ^
	  -Dsonar.projectName=cucumber_bdd ^
	  -Dsonar.projectVersion=1.0 ^
	  -Dsonar.sources=src/main/java ^
	  -Dsonar.java.binaries=src/main/java ^
      -Dsonar.organization=automation_cicd ^
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
