import utilities.*

//def call(stages){
def call() {

    def listStagesOrder = [
            'compile'         : 'sCompile',
            'unitTest'        : 'sUnitTest',
            'jar'             : 'sJar',
            'sonar'           : 'sSonar',
            'nexusUpload'     : 'sNexusUpload',
            'gitCreateRelease': 'sGitCreateRelease',
    ]
    allStages()
    //if (stages.isEmpty()){
    //    allStages()
    //}
}

def allStages() {
    /* sCompile()
     sUnitTest()
     sJar()
     sSonar()
     sNexusUpload()
         */
    sGitCreateRelease()
}

def sCompile() {
    env.STAGE = "Stage Compile"
    stage("$env.STAGE") {
        sh "mvn clean compile -e"
    }
}

def sUnitTest() {
    env.STAGE = "Stage Unit Test"
    stage("$env.STAGE") {
        sh "mvn clean test -e"
    }
}

def sJar() {
    env.STAGE = "Stage Jar"
    stage("$env.STAGE") {
        sh "mvn clean package -e"
    }
}

def sSonar() {
    env.STAGE = "Stage Sonar"
    stage("$env.STAGE") {
        withSonarQubeEnv('sonarqube') {
            sh "mvn clean verify sonar:sonar -Dsonar.projectKey=$env.GIT_REPO_NAME-$env.BRANCH-$env.BUILD_NUMBER -Dsonar.java.binaries=build"
        }
    }
}

def sNexusUpload() {
    env.STAGE = "Stage Nexus Upload"
    stage("$env.STAGE") {
        nexusPublisher nexusInstanceId: 'nexus',
                nexusRepositoryId: 'devops-usach-nexus',
                packages: [
                        [$class         : 'MavenPackage',
                         mavenAssetList : [
                                 [classifier: '',
                                  extension : '.jar',
                                  filePath  : 'build/DevOpsUsach2020-0.0.1.jar'
                                 ]
                         ],
                         mavenCoordinate: [
                                 artifactId: 'DevOpsUsach2020',
                                 groupId   : 'com.devopsusach2020',
                                 packaging : 'jar',
                                 version   : '0.0.1'
                         ]
                        ]
                ]
    }
}

def sGitCreateRelease() {
    env.STAGE = "Stage Git Create Release"
    stage("Stage Git Create Release") {
        sh 'echo "RETORNO $env.STAGE"'
        if (env.GIT_BRANCH =~ "develop*") {
            sh 'git remote set-url origin git@github.com:devops-equipo4/ms-iclab.git'
          //  sh 'git config --global user.email "nestor.fuenzalida@gmail.com"'
            //sh 'git config --global user.name "nfuenzalidam"'
            sh 'git checkout -b release-v11.0.0'
            sh 'git add .'
            sh 'git commit -am "creacion de release"'
            sh 'git push'

            //sh 'git branch -D release-v4.0.0'




        }
    }
}

return this;
