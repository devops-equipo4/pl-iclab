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

def searchKeyInArray(String keyWordsAsString, String splitIdentifier, Map arrayMapToCompare){
    def _array = []
    keyWordsAsString.split("${splitIdentifier}").each{
        def _key = it?.trim()
        if(!_key.equals("") && ( arrayMapToCompare.containsKey(it) )){
            _array.add(arrayMapToCompare[it])
        }else{
            println("***************************************************************")
            println "No se encontró como una función válida, las opociones son:${arrayMapToCompare.keySet() as List}"
            println("***************************************************************")
        }
    }
    return _array
}

return this;
