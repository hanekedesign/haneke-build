package com.hanekedesign.build;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

public class MockProjects {

    public static Project CreateMockProject() {
        def project = ProjectBuilder.builder().withProjectDir(new File("src/test/fixtures/android_app")).build()
        project.apply plugin: 'com.android.application'
        project.pluginManager.apply(HanekeBuildPlugin)
        project.android {
            compileSdkVersion 25
            buildToolsVersion '25.0.0'

            defaultConfig {
                versionCode = project.haneke.versionCode
                versionName = project.haneke.versionName
                minSdkVersion 25
                targetSdkVersion 25
            }

            buildTypes {
                release {
                    signingConfig signingConfigs.debug
                }
            }
        }
        project.haneke{
            def props = new Properties()
            props.load(new FileInputStream("sensitive.properties"))
            ftpPassword = props.ftpPassword
            ftpUser = props.ftpUser
            ftpAddress = props.ftpAddress
            donedoneApiKey = props.donedoneApiKey
            donedoneUsername = props.donedoneUsername
            clientName = 'TestClient'
            projectName = 'TestProject'
            donedoneProjectId = 58504
        }

        return project
    }
}
