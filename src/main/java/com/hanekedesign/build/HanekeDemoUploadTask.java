package com.hanekedesign.build;

import org.apache.commons.net.ftp.FTPClient;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;


public class HanekeDemoUploadTask extends DefaultTask {

    HanekeBuildPluginExtension extension;

    @TaskAction
    void transferApk(){

        FTPClient ftp = new FTPClient();

    }

}
