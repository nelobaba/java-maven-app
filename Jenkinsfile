//library identifier: 'jenkins-shared-library@master', retriever: modernSCM() //scoped to jenkinsfile
@Library('jenkins-shared-library') //global
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
        stage('increment version'){
            steps {
                script {
                    echo 'incrementing app versions...'
                    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion}  versions:commit'
                    def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = matcher[0][1]
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER" //BUILD_NUMBER is the jenkins jobs build number
                }
            }
        }
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
                    sh 'mvn clean package'
//                     gv.buildJar()
//                        buildJar() //gotten from jenkins-shared-library global repo as a function defined there
                }
            }
        }
        stage("build image") {
            steps {
                script {
                    echo "building image"
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]){
                        sh "docker build -t nelobaba/demo-app:$IMAGE_NAME ."
                        sh "echo $PASS | docker login -u $USER --password-stdin"
                        sh "docker push nelobaba/demo-app:$IMAGE_NAME"
                    }
                    //gv.buildImage()
//                     buildImage "nelobaba/demo-app:3.0" //  buildImage() gotten from jenkins-shared-library global repo as a function defined there nelobaba/demo-app:2.0
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
        stage('commit version update'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]){
                        sh 'git config --global user.email "nelson.umunna@carleton.ca"'
                        sh 'git config --global user.username "nelobaba"'

                        sh 'git status'
                        sh 'git branch'
                        sh 'git config --list'

                        sh "git remote set-url origin https://${USER}:${PASS}@github.com/nelobaba/java-maven-app.git"
                        sh 'git add .'
                        sh 'git commit -m "CI version bump"'
                        sh "git push origin HEAD:versioning"
                    }
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