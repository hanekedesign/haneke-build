package com.hanekedesign.build

import org.gradle.api.Project


class HanekeBuildPluginExtension {

    String ftpAddress
    String ftpUser
    String ftpPassword

    String clientName
    String projectName

    int donedoneProjectId
    String donedoneApiKey
    String donedoneUsername

    int versionCode
    String versionName

    private Project project

    public void setProject(Project project){
        this.project = project;
    }

    int getVersionCode(){

        def versionProps = new Properties()
        def versionFile = new File("${project.rootDir}/version.properties")

        if(versionFile.exists()) {
            versionProps.load(new FileInputStream(versionFile))
            if(versionProps.versionCode){
                return Integer.valueOf((String)versionProps.versionCode)
            }
        }
        return 1

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

        return '0.0.1'

    }

}
