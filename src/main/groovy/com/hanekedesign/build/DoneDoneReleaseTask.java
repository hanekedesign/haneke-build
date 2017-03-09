package com.hanekedesign.build;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;

public class DoneDoneReleaseTask extends DefaultTask {

    public HanekeBuildPluginExtension extension;

    @TaskAction
    public boolean createNewRelease()
    {
        DoneDone doneDone = new DoneDone(extension.getDonedoneProjectId(), extension.getDonedoneUsername(), extension.getDonedoneApiKey());

        try {
            return doneDone.PushNextRelease(extension.getProjectName(), extension.getVersionName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
