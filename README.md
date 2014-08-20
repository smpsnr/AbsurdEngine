# __AbsurdEngine__

An XMLVM-based framework for building native Android and iOS games from the same Java code,  
born of my frustration with the lack of ways to quickly test ideas on hardware

### Features

* A multithreaded game engine with sprite graphics
* Seamless access to device-specific identifiers
* Cross-platform java.net HTTP networking
* Conditional compilation of Java and integration with C/ Obj-C code, if you need them
* A cross-platform implementation of the MobFox native advertising SDK

### Game Engine

Based on the Android surface/ runner thread pattern:

* Extend GameView to implement game logic and drawing
* A RunnerThread executes the GameView's logic and instructs the main thread to render it at a specified FPS   

### Framework

Uses XMLVM to cross-compile Android Java code to native iOS code:

* AbsurdEngine Java code compiled against the XMLVM Android compatibility library
* Java byte code converted to C source code
* iOS Xcode project created with generated C code, AbsurdEngine C code, and XMLVM iOS compatibility library

## __Starting an AbsurdEngine project__

The AbsurdEngine depends upon the [Android SDK](https://developer.android.com/sdk/index.html) and a modified version of XMLVM

I write my AbsurdEngine code in Eclipse and test it on Android with the ADT plugin,   
updating the corresponding iOS project from the command line and running it with Xcode  

Before starting, make sure you have Xcode installed and an Android development workflow running

### Install [xmlvm-AbsurdEngine](https://bitbucket.org/smpsnr/xmlvm-absurdengine)

* Clone the source code
    * `hg clone https://smpsnr@bitbucket.org/smpsnr/xmlvm-absurdengine`
* Set the installation directory
    * Create the file `local.properties` in `xmlvm-absurdengine/properties`
    * Add `xmlvm.install=your_xmlvm_install_path` to `local.properties`
* Compile and install xmlvm-AbsurdEngine with Ant
    * `cd xmlvm-absurdengine`
    * `ant`
    * `ant install`
* Add `your_xmlvm_install_path/bin` to your path

### Install the AbsurdEngine
* Clone the source code
    * `mkdir absurdengine`
    * `cd absurdengine`
    * `hg clone https://smpsnr@bitbucket.org/smpsnr/absurdengine`
* Add the Android library project to your Eclipse workspace
    * *File -> Import -> Existing Android Code Into Workspace*

#### Installing the Advertisement SDK

On Android, the AbsurdEngine requires [Google Play Services](https://developer.android.com/google/play-services/index.html) and [OpenUDID](https://github.com/vieux/OpenUDID) (included) to retrieve the device advertising ID  
These services generate code in the Android resource file (R.java) and require references in the manifest (AndroidManifest.xml) that will not resolve on iOS

If you __do not__ need advertisements, simply remove the irrelevant code from the AbsurdEngine's build path in Eclipse:  

* Exclude the ad SDK from the build path
    * *AbsurdEngine -> Project -> Properties -> Java Build Path -> Source*
    * Select `AbsurdEngine/src`, choose *Edit*, then *Next*
    * *Add* the following exclusion pattern: `com/adsdk/`
* Exclude the OpenUDID library from the build path
    * Select `AbsurdEngine/src_android`, choose *Edit*, then *Next*
    * *Add* the following exclusion pattern: `org/OpenUDID/`

If you __do__ plan on using advertisements, you must first reference the Google Play Services SDK:

* Install the Google Place Services SDK
    * In the Android SDK Manager ( `sdk/tools/android` ), install *Extras -> Google Place Services*
    * Copy the Google Play Services library project ( `sdk/extras/google/google_play_services/libproject/google-play-services_lib` ) to your Eclipse workspace
* Reference the Google Play Services library project from the AbsurdEngine
    * *File -> Import -> Existing Android Code Into Workspace*
    * *AbsurdEngine -> Project -> Properties -> Android*
    * *Add* the `google-play-services_lib` library project to the list of references 

If the Google Play Services library gives strange errors, make sure that its Java compiler compliance level (in *Project -> Properties -> Java Compiler*) is set to `1.6`  

See the next section for instructions on enabling the advertisement SDK in an AbsurdEngine project

### Create a new AbsurdEngine project 

* Generate an xmlvm-AbsurdEngine skeleton project
    * Run `xmlvm` from the command line with the following parameters:
        * `--skeleton=absurdengine`
        * `--out=`        the directory you would like to create your project in    
        * `--app-name=`   the name of your project
        * `--androidsdk=` the path of your Android SDK installation
        * `--absurdsdk=`  the path of your AbsurdEngine installation 
* Set up the generated Android project in Eclipse
    * *File -> Import -> Existing Android Code Into Workspace*
    * *MyProject -> Project -> Properties -> Android*
    * *Add* the `AbsurdEngine` library project to the list of references 

#### Enabling the Advertisement SDK

To use the ad SDK in your new project, you must create iOS-compatible copies of the Android manifest and generated resource code  
before adding Android-specific Google Play and OpenUDID references  

* Reference iOS-compatible Android resources from the XMLVM project
    * If the `gen` folder does not yet exist in your project directory, clean and build the Android project from Eclipse (*MyProject -> Project -> Clean*)
    * Copy `AndroidManifest.xml` and `gen/yourpackagename` to new directories, perhaps `androidmanifest_ios` and `gen_ios` respectively
    * In `local.properties`, set the following properties:
        * `src.gen.dir=gen_ios`
        * `manifest.ios=androidmanifest_ios/AndroidManifest.xml`
* Reference the Google Play Services and OpenUDID libraries from your Android project
    * *MyProject -> Project -> Properties -> Android*
    * *Add* the `google-play-services_lib` library project to the list of references. This will automatically generate code in R.java
    * Add the following to AndroidManifest.xml, within the `<application>` element:
        * >     <meta-data  
          >          android:name="com.google.android.gms.version"  
          >          android:value="@integer/google_play_services_version" />
        * >     <service 
          >           android:name="org.openudid.OpenUDID_service" >
          >			<intent-filter>
          >				<action android:name="org.openudid.GETUDID" />
          >			</intent-filter>
          >		</service>

Note - further changes to AndroidManifest.xml or R.java that *should* be present on iOS (for example, adding permissions or drawable resources)  
must be copied manually from the Android files to their copies in `gen_ios` and `androidmanifest_ios`

### Running your project

* Android
    * Build and run in Eclipse like any other Android project
* iOS
    * Run `ant` in in your project directory
    * The Xcode project created in `myproject/dist` is opened automatically; build and run like any other iOS project

## __Notes__

AbsurdEngine
(c) by Sam Posner (http://www.arcadeoftheabsurd.com/)

AbsurdEngine is licensed under a
Creative Commons Attribution 4.0 International License. For more information, see LICENSE.txt  
Individual licenses apply to all third-party libraries used by AbsurdEngine. For more information, see the 3rdparty-license folder

You can contact me at <sam@arcadeoftheabsurd.com>