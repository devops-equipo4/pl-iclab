import utilities.*

def call() {

    def listStagesOrder = [
            'gitDiff'        : 'sGitDiff',
            'nexusDownload'  : 'sNexusDownload',
            'run'            : 'sRun',
            'test'           : 'sTest',
            'gitMergeMaster' : 'sGitMergeMaster',
            'gitMergeDevelop': 'sGitMergeDevelop',
            'gitTagMaster'   : 'sGitTagMaster',
    ]

    //if (stages.isEmpty()){
    allStages()
    //}
}

def allStages() {
    sGitDiff()
    sNexusDownload()
    sRun()
    sTest()
    sGitMergeMain()
   // sGitMergeDevelop()
    sGitTagMain()
}

def sGitDiff() {
    env.STAGE = "Stage Git Diff"
    stage("$env.STAGE") {
        sh "git diff origin/main"
    }
}

def sNexusDownload() {
    env.STAGE = "Stage Nexus Download"
    stage("$env.STAGE") {
        sh 'curl -X GET -u $NEXUS_USER:$NEXUS_PASSWORD "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }
}

def sRun() {
    env.STAGE = "Stage Run"
    stage("$env.STAGE") {
        sh 'nohup java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }
}

def sTest() {
    env.STAGE = "Stage Test"
    stage("$env.STAGE") {
        sh 'echo "test"'
        // sh "sleep 60 && curl -X GET 'http://localhost:8082/rest/mscovid/test?msg=testing'"
    }
}

def sGitMergeMain() {
    env.STAGE = "Stage Git Merge Main"
    stage("$env.STAGE") {
        sh "git checkout ${env.BRANCH}"
        sh "git pull"
        sh "git checkout main"
        sh "git merge ${env.BRANCH} --no-ff --allow-unrelated-histories"
        sh "git push origin main"
    }
}

def sGitMergeDevelop() {
    env.STAGE = "Stage Git Merge Develop"
    stage("$env.STAGE") {
        sh "git checkout develop"
        sh "git merge ${env.BRANCH} --no-ff --allow-unrelated-histories"
        sh "git add ."
        sh "git commit -m 'omitir! release'"
        sh "git push origin develop "
    }
}

def sGitTagMain() {
    env.STAGE = "Stage Git Tag Main"
    stage("$env.STAGE") {
        sh "git checkout main"
        sh "git tag -f v0.1 -m 'Release v0.1'"
        sh "git push --tags"
    }
}

return this;