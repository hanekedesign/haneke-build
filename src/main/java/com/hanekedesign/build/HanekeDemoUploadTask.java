package com.hanekedesign.build;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;


public class HanekeDemoUploadTask extends AbstractTask {

    public HanekeBuildPluginExtension hanekeData;

    public String versionName;
    public String apkPath;

    @TaskAction
    void transferApk(){

        try {
            DemoFtpUploader uploader = new DemoFtpUploader(hanekeData.getFtpAddress(), hanekeData.getFtpUser(), hanekeData.getFtpPassword());

            uploader.uploadFile(getApkPath(), new File(apkPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getApkPath(){
        String base = "demo.hanekedesign.com/Android/";

        return base + "Test/";
    }


}
