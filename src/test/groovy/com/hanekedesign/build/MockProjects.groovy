package com.hanekedesign.build;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

public class MockProjects {

    public static Project CreateMockProject() {
        def project = ProjectBuilder.builder().withProjectDir(new File("src/test/fixtures/android_app")).build()
        project.apply plugin: 'com.android.application'
        project.pluginManager.apply(HanekeBuildPlugin)
        project.android {
            compileSdkVersion 23
            buildToolsVersion '23.0.1'

            defaultConfig {
                versionCode 1
                versionName '1.0'
                minSdkVersion 23
                targetSdkVersion 23
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
            ftpPassword = props.ftp_password
            ftpUser = props.ftp_user
            ftpAddress = props.ftp_address
            donedoneApiKey = props.donedoneApiKey
            clientName = 'TestClient'
            projectName = 'TestProject'
        }

        return project
    }
}
