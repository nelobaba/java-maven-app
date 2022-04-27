//library identifier: 'jenkins-shared-library@master', retriever: modernSCM()
@Library('jenkins-shared-library')
def gv

pipeline {
    agent any
    tools {
        maven 'maven-3.6'
    }
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
//                     sh 'mvn package'
//                     gv.buildJar()
                       buildJar()
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    echo "building image"
//                     withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]){
//                         sh 'docker build -t nelobaba/demo-app:2.0 .'
//                         sh "echo $PASS | docker login -u $USER --password-stdin"
//                         sh "docker push nelobaba/demo-app:2.0"
//                     }
                    //gv.buildImage()
                    buildImage()
                }
            }
        }
        stage("deploy") {
            input {
                message "Select an environment to deploy to"
                ok "Done"
                parameters {
                    choice(name: 'ENV', choices: ['dev', 'qa', 'uat'], description: '')
                }
            }
            when {
                expression {
                    params.executeDeploy
                }
            }
            steps {
                script {
                    echo "deploying ${params.VERSION} to ${ENV}"
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