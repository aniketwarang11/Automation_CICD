node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    def mvn = tool 'MAVEN_HOME';
    withSonarQubeEnv() {
  bat "${mvn}\\bin\\mvn clean verify sonar:sonar -Dsonar.projectKey=Automatio_CICD -Dsonar.projectName='Automatio_CICD'"
}
  }
}