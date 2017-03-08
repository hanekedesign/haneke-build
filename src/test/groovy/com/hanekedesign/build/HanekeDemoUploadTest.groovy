package com.hanekedesign.build

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class HanekeDemoUploadTest {

    @Before
    public void setup() {

    }

    @Test
    public void testUpload(){
        DemoFtpUploader demoFtpUploader = new DemoFtpUploader(Sensitive.FTP_ADDRESS, Sensitive.FTP_USER, Sensitive.FTP_PASSWORD)
        demoFtpUploader.uploadFile("demo.hanekedesign.com/Android/Test/Test.apk", new File("test.apk"))
    }

    @Test
    public void testPluginCreation(){
        def project = MockProjects.CreateMockProject()
        project.evaluate()

        def debugTask = project.tasks.getByName("hanekeFtpUploadDebug")
        def releaseTask = project.tasks.getByName("hanekeFtpUploadRelease")

        Assert.assertTrue(debugTask != null)
        Assert.assertTrue(releaseTask != null)

    }

    @Test
    public void testDebugUploadApk(){
        def project = MockProjects.CreateMockProject()

        project.evaluate()

        def versionCode = project.haneke.versionCode
        def versionName = project.haneke.versionName

//        System.out.println('Before task: code: '+versionCode+", name: "+versionName)

        def transferred = project.tasks.hanekeFtpUploadDebug.transferApk()
        Assert.assertTrue(transferred)

        project.tasks.hanekeIncrementBuild.incrementBuild()

//        System.out.println('After task: code: ' + (int)project.haneke.versionCode+", name: "+project.haneke.versionName)
        Assert.assertTrue(project.haneke.versionCode == versionCode + 1)
        Assert.assertTrue(Integer.valueOf(project.haneke.versionName.split('\\.')[2]) == Integer.valueOf(versionName.split('\\.')[2]) + 1)


    }

}
