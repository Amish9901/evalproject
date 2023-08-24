pipeline {
    agent any
    
    tools {
        jdk 'jdk-1.8.0'
        maven 'maven'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    rtMavenRun(
                        tool: 'maven',
                        pom: 'pom.xml',
                        goals: 'clean package', // Modify this line
                        resolverId: 'central-resolver'
                    )
                }
            }
        }
    }

    post {
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
