import utilities.*

def call(stages){

    def listStagesOrder = [
        'gitDiff': 'sGitDiff',
        'nexusDownload': 'sNexusDownload',
        'run': 'sRun',
        'gitMergeMaster': 'sGitMergeMaster',
        'gitMergeDevelop': 'sGitMergeDevelop',
        'gitTagMaster': 'sGitTagMaster',
    ]

    if (stages.isEmpty()){
        allStages()
    }
}

def allStages(){
    sGitDiff()
    sNexusDownload()
    sRun()
    sGitMergeMain()
    sGitMergeDevelop()
    sGitTagMain()
}

def sGitDiff(){
    env.STAGE = "Stage Git Diff"
    stage("$env.STAGE"){
        sh "git diff ${env.BRANCH} .. main"
    }
}

def sNexusDownload(){
    env.STAGE = "Stage Nexus Download"
    stage("$env.STAGE"){

    }
}

def sRun(){
    env.STAGE = "Stage Run"
    stage("$env.STAGE"){

    }
}

def sGitMergeMain(){
    env.STAGE = "Stage Git Merge Main"
    stage("$env.STAGE"){
        sh "git checkout main"
        sh "git merge ${env.BRANCH}"
    }
}

def sGitMergeDevelop() {
    env.STAGE = "Stage Git Merge Develop"
    stage("$env.STAGE"){
        sh "git checkout develop"
        sh "git merge ${env.BRANCH}"
    }
}

def sGitTagMain(){
    env.STAGE = "Stage Git Tag Main"
    stage("$env.STAGE"){
        sh "git checkout main"
        sh "git tag ${env.BRANCH}"
    }
}

return this;