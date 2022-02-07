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
    sCompile()
    sUnitTest()
    sJar()
    sSonar()
    sNexusUpload()
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

    printf("ARTIFACT")
    printf $env.ARTIFACT

    printf("VERSION")
    printf $env.VERSION

    printf("GROUP")
    printf $env.GROUP

    stage("$env.STAGE") {

        printf('build/'+$env.ARTIFACT+'-'+$env.VERSION+'.jar')

        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class         : 'MavenPackage',
             mavenAssetList : [
                 [classifier: '',
                  extension : 'jar',
                  filePath  : 'build/'+$env.ARTIFACT+'-'+$env.VERSION+'.jar'
                 ]
             ],
             mavenCoordinate: [
                 artifactId: $env.ARTIFACT,
                 groupId   : $env.GROUP,
                 packaging : 'jar',
                 version   : $env.VERSION
             ]
            ]
        ]
    }
}

def sGitCreateRelease() {
    env.STAGE = "Stage Git Create Release"
    stage("Stage Git Create Release") {
        if (env.GIT_BRANCH =~ "develop*") {
            sh "git checkout develop"
            //sh "git checkout -b release-v0-$env.BUILD_NUMBER-1"
            //sh "git push --set-upstream origin release-v0-$env.BUILD_NUMBER-1"
            sh "git checkout -b release-v"+env.V_BREAK+"-"+env.V_BREAK+"-"+env.V_BREAK
            sh "git push --set-upstream origin release-v"+env.V_BREAK+"-"+env.V_BREAK+"-"+env.V_BREAK
        }
    }
}
return this;
