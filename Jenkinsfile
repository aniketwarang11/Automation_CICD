pipeline {
    agent any

    tools {
        maven "MAVEN_HOME"
        jdk "JAVA_17"
    }

    stages {
        stage('Fetch Code') {
            steps {
                git branch: 'main', url: 'https://github.com/aniketwarang11/Automation_CICD.git'
            }
        }

        stage('Build App') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('App Compile') {
            steps {
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Code Analysis with SonarQube') {
            environment {
                scannerHome = tool 'sonar-scanner-6'
            }
            steps {
                withSonarQubeEnv('Sonar-server') {
                    bat """
                        ${scannerHome}\\bin\\sonar-scanner ^
                        -Dsonar.projectKey=cucumber_bdd ^
                        -Dsonar.projectName=cucumber_bdd ^
                        -Dsonar.projectVersion=1.0 ^
                        -Dsonar.sources=src/main/java ^
                        -Dsonar.java.binaries=target ^
                    """
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
