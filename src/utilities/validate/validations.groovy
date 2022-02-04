package utilities.validate

def validBranch(){
    def branch = env.GIT_BRANCH
    def branchesValid = ['develop', 'release', 'feature']

    if(branchesValid.contains(branch)){
        if(branch.contains("develop")){
            return 'develop'
        }
        if(branch.contains("feature")){
            return 'feature'
        }
        if(branch.contains("release")){
            if(validaBranchRelease(branch)){
                return 'release'
            }
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
    if(branch.matches("release-v[0-9]{1,2}-[0-9]{1,2}-[0-9]{1,2}")){
        return true;
    }
    return false;
}

def validateBranchDevelop(){
    if(env.GIT_BRANCH.matches("/develop*/")){
        return true;
    }
    return false;
}

return this;