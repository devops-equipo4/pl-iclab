import utilities.*

def call() {
    pipeline {
        agent any
        environment {
            NEXUS_USER = credentials('user-nexus')
            NEXUS_PASSWORD = credentials('password-nexus')
            //GIT_USERNAME = credentials('GIT_USERNAME')
            //GIT_PASSWORD = credentials('GIT_PASSWORD')

        }
        //parameters {
        //string  name: 'stages', description: 'Ingrese los stages para ejecutar', trim: true
        //}
        stages {
            stage('pipeline') {
                steps {
                    script {
                        sh "env"
                        env.STAGE = ""
                        env.PIPELINE = ""
                        //env.STAGES = stages
                        env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
                        def validations = new validate.validations();

                        def branch = validations.validBranch()
                        echo "-----"
                        echo "$branch"
                        echo "-----"

                        if (!branch.isEmpty()) {
                            if (branch.contains("develop") || branch.contains("feature")) {
                                //Integracion Continua
                                env.PIPELINE = "IC"
                                ci.call()
                                //ci.call(params.stages)
                            } else {
                                //Despliegue continuo
                                env.PIPELINE = "RELEASE"
                                //cd.call(params.stages)
                                cd.call()
                            }
                        }
                    }
                }
                post {
                    success {
                        slackSend color: 'good', message: "[Grupo4][Pipeline ${env.PIPELINE}][Rama ${env.GIT_BRANCH}][Stage: ${env.STAGE}][Resultado: Ok]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-jenkins-slack'
                        echo "si"
                    }
                    failure {
                        slackSend color: 'danger', message: "[Grupo4][Pipeline ${env.PIPELINE}][Rama ${env.GIT_BRANCH}][Stage: ${env.STAGE}][Resultado: No Ok]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'token-jenkins-slack'
                        echo "no"
                    }
                }
            }
        }
    }
}

return this;
