import utilities.*

def call(stages){

    def listStagesOrder = [
        'compile': 'sCompile',
        'unitTest': 'sUnitTest',
        'jar': 'sJar',
        'sonar': 'sSonar',
        'nexusUpload': 'sNexusUpload',
        'gitCreateRelease': 'sGitCreateRelease',
    ]
    if (stages.isEmpty()){
        allStages()
    }
}

def allStages(){
    sCompile()
    sUnitTest()
    sJar()
    sSonar()
    sNexusUpload()
    sGitCreateRelease()
}

def sCompile(){
    env.STAGE = "Stage Compile"
    stage("$env.STAGE"){
        sh "gradle clean build"
    }
}

def sUnitTest(){
    env.STAGE = "Stage Unit Test"
    stage("$env.STAGE"){

    }
}

def sJar(){
    env.STAGE = "Stage Jar"
    stage("$env.STAGE"){

    }
}

def sSonar(){
    env.STAGE = "Stage Sonar"
    stage("$env.STAGE"){

    }
}

def sNexusUpload(){
    env.STAGE = "Stage Nexus Upload"
    stage("$env.STAGE"){

    }
}

def sGitCreateRelease(){
    env.STAGE = "Stage Git Create Release"
    stage("Stage Git Create Release"){
        if(validateBranchDevelop()){
            
        }
    }
}

return this;