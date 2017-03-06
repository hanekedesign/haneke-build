package com.hanekedesign.build;

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

public class MockProjects {

    public static Project CreateMockProject() {
        def project = ProjectBuilder.builder().build()
        project.apply plugin: 'com.android.application'
        project.apply plugin: 'com.hanekedesign.build'
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

        return project
    }
}
