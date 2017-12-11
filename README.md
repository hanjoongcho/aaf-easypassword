# Easy Password
[![License][licensesvg]][LICENSE.md]

<img src="screenshots/ic_launcher.png" >

Easy Password is password management application.   
This application uses pattern locks to manage information that requires security.   

<a href='https://play.google.com/store/apps/details?id=io.github.hanjoongcho.easypassword'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="258" height="98"/></a>   

# Support Features
```
01. Application lock with pattern
02. Password strength check
03. Encryption and decryption with AES of passwords to be managed using lock pattern
04. Backup and recovery of management list using Google Drive
```

# Screen Shots
<img src="screenshots/en/01_compressed.jpg">&nbsp;
<img src="screenshots/en/02_compressed.jpg">   
<img src="screenshots/en/03_compressed.jpg">&nbsp;
<img src="screenshots/en/04_compressed.jpg">

# How to build
```
Step1. Fork or download 'aaf-easypassword' project.
Step2. Import 'aaf-easypassword' project into android studio.
Step3. Use below link download svg images from Flaticon.
Step4. Import 6 svg files for your project from your Android studio.
       Project rightclick > New > Vector Asset... You can import from the menu and the resource names are:
       001-password.svg    -> ic_password.xml
       003-folder.svg      -> ic_folder.xml
       005-credit-card.svg -> ic_credit_card.xml
       009-lock.svg        -> ic_lock.xml
       012-television.svg  -> ic_web.xml
       015-home.svg        -> ic_home.xml
Step5. Generate your salt string from GenerateSaltString.kt android testcase.
       'GenerateSaltString.kt' file rightclick > Run(Android instrumented test) > GenerateSaltString 
       Apply filter name 'GenerateSaltString-testGenerateSaltString' to Logcat and copy salt string from log.
Step6. Define salt string into 'AesUtils.kt' file       
Step7. Register your package name and SHA-1 signature certificate fingerprint for Google Drive use.(https://console.developers.google.com/)
Step8. Build 'aaf-easypassword' project with android studio.
```
[Download security-40 svg images from Flaticon](https://www.flaticon.com/packs/security-40)

# License
[LICENSE][LICENSE.md]

[licensesvg]: https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg
[LICENSE.md]: https://github.com/hanjoongcho/aaf-easypassword/blob/master/LICENSE.md