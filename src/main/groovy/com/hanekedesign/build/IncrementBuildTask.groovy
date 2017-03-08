package com.hanekedesign.build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class IncrementBuildTask extends DefaultTask {

    @TaskAction
    void incrementBuild(){
        def versionProps = new Properties()
        def versionFile = new File("${project.rootDir}/version.properties")

        def versionName = project.haneke.versionName
        int versionCode = project.haneke.versionCode

        versionCode++
        String[] nums = versionName.split("\\.")
        int minor = Integer.valueOf(nums[nums.length-1])
        minor++

        StringBuilder b = new StringBuilder()
        b.append(nums[0])
        b.append('.')
        b.append(nums[1])
        b.append('.')
        b.append(minor)

        versionProps.versionName = b.toString()
        versionProps.versionCode = versionCode.toString()
        versionProps.store(versionFile.newWriter(), null)

        System.out.println("increment task ran")
    }
}
