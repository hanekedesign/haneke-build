package com.hanekedesign.build

import com.android.build.gradle.AppPlugin
import org.apache.commons.lang.StringUtils;
import org.gradle.api.Plugin
import org.gradle.api.Project

public class HanekeBuildPlugin implements Plugin<Project> {

    public static final String HANEKE_BUILD = 'Haneke'

    @Override
    public void apply(Project project) {
        def log = project.logger

        def hasAppPlugin = project.plugins.find { p -> p instanceof AppPlugin }
        if (!hasAppPlugin) {
            throw new IllegalStateException('The \'com.android.application\' plugin is required.')
        }

        def extension = project.extensions.create('haneke', HanekeBuildPluginExtension)
        extension.project = project

        def incrementTask = project.tasks.create('hanekeIncrementBuild', IncrementBuildTask)

        def doneDoneTask = project.tasks.create('doneDoneReleaseTask', DoneDoneReleaseTask)
        doneDoneTask.extension = extension


        project.android.applicationVariants.all{ variant ->

//            System.out.println('discovered variant: ' + variant.buildType.name)

            // create name of tasks using product flavor and application variant
            def buildTypeName = variant.buildType.name.capitalize()

            def productFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
            if (productFlavorNames.isEmpty()) {
                productFlavorNames = ['']
            }
            def productFlavorName = productFlavorNames.join('')
            def flavor = StringUtils.uncapitalize(productFlavorName)

            def variationName = "${flavor}${buildTypeName}"

            def ftpTaskName = "hanekeFtpUpload${variationName}"

            def ftpTask = project.tasks.create(ftpTaskName, HanekeDemoUploadTask)
            ftpTask.hanekeData = extension
            ftpTask.description = 'Uploads the artifact to the Haneke Demo Server'
            ftpTask.group = HANEKE_BUILD
            ftpTask.versionName = project.android.defaultConfig.versionName
            ftpTask.apkPath = variant.outputs.find().outputFile

            incrementTask.mustRunAfter ftpTask

            variant.outputs.each { output -> ftpTask.dependsOn output.assemble }

//            System.out.println("added task: "+ftpTask.name)
        }

    }
}
