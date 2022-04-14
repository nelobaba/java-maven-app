def gv

pipeline {
    agent any
    environment {
        NEW_VERSION = "1.3.0"
        SERVER_CREDENTIALS = credentials('github-credentials')
    }
    parameters {
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0', '1.3.0'], description: '')
        booleanParam(name: 'executeDeploy', defaultValue: true, description: '')
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage("build jar") {
            steps {
                script {
                    echo "building jar with version ${NEW_VERSION}"
                    //gv.buildJar()
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    echo "building image"
                    //gv.buildImage()
                }
            }
        }
        stage("deploy") {
            when {
                expression {
                    params.executeDeploy
                }
            }
            steps {
                script {
                    echo "deploying ${params.VERSION}"
                    //gv.deployApp()
                }
            }
        }
    }
    post {
        always {
            echo "All Done !"
        }
        success {
            echo "Successful"
        }
        failure {
            echo "Job Failed"
        }
    }
}