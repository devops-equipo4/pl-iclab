package utilities.validate

def validBranch(){
    def branch = env.GIT_BRANCH
    env.BRANCH = branch.split('/')[1]
    
    if(branch =~ "develop*"){
        printf "Branch developer"
        return 'develop'
    }
    if(branch =~ "feature*"){
        printf "Brach feature"
        return 'feature'
    }
    if(branch =~ "release*"){
        def valRelease = validateBranchRelease(branch)
        if (valRelease){
            printf "Brach release"
            return "release"
        }
    }
    return '';
}

def isMaven(){
    if(fileExists("pom.xml")){
        return true;
    }
    return false;
}

def isGradle(){
    if(fileExists("build.gradle")){
        return true;
    }
    return false;
}

def validateBranchRelease(String branch){
    if(branch =~ "release-v[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,2}"){
        return true;
    }
    return false;
}

def validateBranchDevelop(){
    if(env.GIT_BRANCH =~ "develop*"){
        return true;
    }
    return false;
}

def versionSplit(){
    def arrVersion = env.VERSION

    env.V_BREAK = arrVersion.split("\\.")[0]
    env.V_FEATURE = arrVersion.split("\\.")[1]
    env.V_FIX = arrVersion.split("\\.")[2]
    return true;
}

return this;
