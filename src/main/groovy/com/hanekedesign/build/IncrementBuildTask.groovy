package com.hanekedesign.build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class IncrementBuildTask extends DefaultTask {

    @TaskAction
    void incrementBuild(){


        def versionName = project.haneke.versionName
        def versionCode = project.haneke.versionCode

        versionCode++
        string[] nums = versionName.split(".")
        int minor = Integer.valueOf(nums[nums.length-1])
        minor++

        StringBuilder b = new StringBuilder()
        b.append(nums[0])
        b.append(nums[1])
        b.append(minor)
        
        versionProps.versionName = b.toString()
        versionProps.versionCode = versionCode
        versionProps.store(versionFile.newWriter(), null)
    }
}
