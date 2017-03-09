package com.hanekedesign.build;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;


public class HanekeDemoUploadTask extends DefaultTask {

    private static final String BASE = "demo.hanekedesign.com/Android/";

    public HanekeBuildPluginExtension hanekeData;

    public String versionName;
    public String apkPath;

    @TaskAction
    boolean transferApk(){

        try {
            DemoFtpUploader uploader = new DemoFtpUploader(hanekeData.getFtpAddress(), hanekeData.getFtpUser(), hanekeData.getFtpPassword());

            if(uploader.uploadFile(getApkPath(), new File(apkPath))){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getApkPath(){

        return BASE +
                hanekeData.getClientName() + File.separator +
                hanekeData.getProjectName() + File.separator +
                versionName/*.replace('.', '-')*/ + File.separator +
                new File(apkPath).getName();
    }


}
