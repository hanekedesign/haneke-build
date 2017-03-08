package com.hanekedesign.build


class HanekeBuildPluginExtension {

    String ftpAddress
    String ftpUser
    String ftpPassword

    String clientName
    String projectName

    int donedoneProjectId
    String donedoneApiKey

    int versionCode
    String versionName

    int getVersionCode(){
        def versionProps = new Properties()
        def versionFile = new File("${project.rootDir}/version.properties")

        if(versionFile.exists()) {
            versionProps.load(new FileInputStream(versionFile))
            if(versionProps.versionCode){
                return versionProps.versionCode
            }
        }
        else{
            return 1
        }
    }

    String getVersionName(){
        def versionProps = new Properties()
        def versionFile = new File("${project.rootDir}/version.properties")

        if(versionFile.exists()) {
            versionProps.load(new FileInputStream(versionFile))
            if(versionProps.versionName){
                return versionProps.versionName
            }
        }
        else{
            return '0.0.1'
        }
    }

}
