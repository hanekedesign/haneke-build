package com.hanekedesign.build

import com.android.build.gradle.AppPlugin
import org.apache.commons.lang3.StringUtils;
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
        incrementTask.group = HANEKE_BUILD
        incrementTask.description = 'Increments the version code and name of the project (run automatically after a deploy)'

        def doneDoneTask = project.tasks.create('doneDoneReleaseTask', DoneDoneReleaseTask)
        doneDoneTask.extension = extension
        doneDoneTask.group = HANEKE_BUILD
        doneDoneTask.description = 'Creates a new release in done done with the current version name'


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

            ftpTask.finalizedBy(incrementTask)

            def qaBuildTask = project.tasks.create("createQaBuild${variationName}")
            qaBuildTask.description = 'Creates a new QA build, which uploads the output apk to the demo ftp, creates a DoneDone release, and increments the build number'
            qaBuildTask.group = HANEKE_BUILD
            qaBuildTask.dependsOn ftpTask
            qaBuildTask.dependsOn doneDoneTask
            qaBuildTask.dependsOn incrementTask


            variant.outputs.each { output -> ftpTask.dependsOn output.assemble }

//            System.out.println("added task: "+ftpTask.name)
        }

    }
}
