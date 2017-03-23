# Haneke Build

The goal of this plugin is to automate deployment and organization tasks for Android Apps built at Haneke Design
 
## Beta Plugin Installation

1. Your root directory build.gradle should include the following
```groovy
buildscript {
    repositories {
        jcenter()
        flatDir dirs: 'build-plugin/lib'
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.0'
        classpath 'com.hanekedesign:haneke-build:0.0.1'

    }
}
```

2. Download the beta plugin jar from:</br>
http://demo.hanekedesign.com/Android/haneke-build-0.0.1.jar </br>
and place it in the following directory:</br>
`/build-plugin/lib`

3. At the top of your app build.gradle, add the line:</br>
`apply plugin: 'haneke-build'`

4. Set the app's versionCode and VersionName to `project.haneke.x` to take advantage of the auto incrementing build numbers. The build numbers can be manually changed in a file called `version.properties`
```groovy
defaultConfig {
   versionCode project.haneke.versionCode
   versionName project.haneke.versionName
}
```

5. Add the following block to your app build.grade:
```groovy
project.haneke{
    def props = new Properties()
    props.load(new FileInputStream("sensitive.properties"))
    ftpPassword = props.ftpPassword
    ftpUser = props.ftpUser
    ftpAddress = props.ftpAddress
    donedoneApiKey = props.donedoneApiKey
    donedoneUsername = props.donedoneUsername
    clientName = ''
    projectName = ''
    donedoneProjectId = 58504
}
```
6. Fill in values for ClientName and ProjectName. These are for identifying where builds will be found in the demo server. To automatically create Done Done releases when a new QA build is pushed out, add the `donedoneProjectId` 

Your app build.grade should look something like this:
```groovy
apply plugin: 'com.android.application'
apply plugin: 'haneke-build'

android {
    compileSdkVersion 25
    buildToolsVersion "25.0.2"
    defaultConfig {
        applicationId "com.myapp.app"
        minSdkVersion 19
        targetSdkVersion 25
        versionCode project.haneke.versionCode
        versionName project.haneke.versionName
    }
}

project.haneke{
    def props = new Properties()
    props.load(new FileInputStream("sensitive.properties"))
    ftpPassword = props.ftpPassword
    ftpUser = props.ftpUser
    ftpAddress = props.ftpAddress
    donedoneApiKey = props.donedoneApiKey
    donedoneUsername = props.donedoneUsername
    clientName = 'MyClient'
    projectName = 'MyProject'
    donedoneProjectId = 58504
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:25.2.0'
    testCompile 'junit:junit:4.12'
}

```

## Usage and Tasks

After a succesful sync of your project, your Gradle tab will have a new group called 'haneke' with the following tasks:</br>

- **doneDoneReleaseTask**:</br>
  creates a new Done Done Release if the `project.haneke{}` block has a `donedoneProjectId` defined
- **hanekeFtpUploadDebug:**</br>
    assembles the current project and uploads a debug build to the demo server under:</br>
    `demo.hanekedesign.com/Android/clientName/projectName/(version)/apk_name.apk`
- **hanekeIncrementBuild:**</br>
    increments the version numbers found in `version.properties`. These values can be used as variables in your build.grade 
