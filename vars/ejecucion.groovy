import utilities.*

def call(){
    pipeline {
        agent any
        environment {
            NEXUS_USER      = credentials('user-nexus')
            NEXUS_PASSWORD  = credentials('password-nexus')
        }
        parameters {
            string  name: 'stages', description: 'Ingrese los stages para ejecutar', trim: true
        }
        stages {
            stage('pipeline') {
                steps {
                    script {
                        sh "env"
                        env.STAGE  = ""
                        env.PIPELINE = ""
                        def validations = new validate.validations();

                        if(!validations.validBranch().isEmpty()){
                            if(validations.validBranch().contains("develop") || validations.validBranch().contains("feature")){
                                //Integracion Continua
                                env.PIPELINE = "IC"
                            }
                                //Despliegue continuo
                                env.PIPELINE = "RELEASE"
                            }

                            }
                        }
                    }
                }
                post{
                    success{
                        slackSend color: 'good', message: "[Grupo4][Pipeline ${env.PIPELINE}][Rama ${env.BRANCH}][Stage: ${env.STAGE}][Resultado: Ok]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-rmateluna'
                    }
                    failure{
                        slackSend color: 'danger', message: "[Grupo4][Pipeline ${env.PIPELINE}][Rama ${env.BRANCH}][Stage: ${env.STAGE}][Resultado: No Ok]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'slack-rmateluna'
                    }
                }
            }
        }
    }
}

return this;