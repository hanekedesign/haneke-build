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

        project.tasks.hanekeFtpUploadDebug.transferApk()

    }

}
