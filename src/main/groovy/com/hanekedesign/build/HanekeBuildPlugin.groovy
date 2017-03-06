package com.hanekedesign.build

import com.android.build.gradle.AppPlugin
import org.apache.commons.lang.StringUtils;
import org.gradle.api.Plugin
import org.gradle.api.Project

public class HanekeBuildPlugin implements Plugin<Project> {

    public static final String HANEKE_BUILD = 'Haneke Build'

    @Override
    public void apply(Project project) {

        def hasAppPlugin = project.plugins.find { p -> p instanceof AppPlugin }
        if (!hasAppPlugin) {
            throw new IllegalStateException('The \'com.android.application\' plugin is required.')
        }

        def extension = project.extensions.create('haneke-build', HanekeBuildPluginExtension)


        project.android.applicationVariants.all{ variant ->

            // create name of tasks using product flavor and application variant
            def buildTypeName = variant.buildType.name.capitalize()

            def productFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
            if (productFlavorNames.isEmpty()) {
                productFlavorNames = ['']
            }
            def productFlavorName = productFlavorNames.join('')
            def flavor = StringUtils.uncapitalize(productFlavorName)

            def variationName = "${productFlavorName}${buildTypeName}"

            def ftpTaskName = "hanekeFtpUpload${variationName}"

            def ftpTask = project.tasks.create(ftpTaskName, HanekeDemoUploadTask)
            ftpTask.extension = extension
            ftpTask.description = 'Uploads the artifact to the Haneke Demo Server'
            ftpTask.group = HANEKE_BUILD

            variant.outputs.each { output -> ftpTask.dependsOn output.assemble }


        }

    }
}