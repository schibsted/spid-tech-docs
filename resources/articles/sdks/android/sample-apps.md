:title Android sample applications

:aside

## More about the Android SDK

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- [Self Service](/mobile/selfservice/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - Android sample apps
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Mobile review checklist](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

There are 5 different sample apps all available in the SPiD Android SDK repository that demonstrate the different ways to login to SPiD. Clone the repository in a directory of your choosing `git clone https://github.com/schibsted/sdk-android.git <directory>`

Before using these samples you need to configure them to use your own client credentials. Do this by finding the following code statement, replacing the placeholder values with your own valid credentials.

	SPiDConfiguration config = new SPiDConfigurationBuilder()
    	.clientID("your-client-id")
    	.clientSecret("your-client-secret")
    	.appURLScheme("your-app-url-scheme")
    	.serverURL("your-spidserver-url")
    	.signSecret("your-secret-sign-key")
    	.debugMode(true)
    	.context(getApplicationContext())
    	.build();
    	
In the AndroidManifest.xml file update the intent filter to use your own url scheme. This should be on the format `<spid-clientId>`, if your client id is 123 your url scheme would be spid-123. This is used to redirect http calls to your app

	<data android:scheme="your-app-url-scheme" />

The sample apps show how to login natively, browser, webview, or via a user's Facebook or Google+ accounts. If the user is already authenticated when the app launches the user is forwarded to the redirect url. This is especially useful if you use a browser to login as the cookie from another application using SPiD to login can be used by your application, simplifying the login process for the user and increasing the conversion rate in your app.

#### SPiDExampleApp
This app shows how to login using either a browser or a SPiD webview.

#### SPiDNativeApp
The native app logs in to SPiD by username and password using the OAuth [password grant type](https://tools.ietf.org/html/draft-ietf-oauth-v2-11#section-5.1.2).

#### SPiDHybridApp
The hybrid app demonstrates a hybrid app that logs in using a webview.

#### SPiDFacebookApp
The SPiD Facebook sample app shows how to login or create a SPiD account using a user's Facebook account. To test the Facebook app you need to acquire a Facebook app id. To do that you need to first [register](https://developers.facebook.com/apps) as a Facebook developer if you are not already one. Once that is done you can acquire a Facebook app id for your application by registering a new app; My Apps->Add a New App. Set the facebook_app_id in values/strings.xml to your newly acquired app id.
You must also add the Facebook dependency in your gradle file. To add it you also need to add the mavenCentral() to your repositories if you don’t already have it.

	repositories {
		mavenCentral()
	}
	dependencies {
		compile 'com.facebook.android:facebook-android-sdk:3.23.0'
	}

#### SPiDGooglePlusApp
The SPiD Google plus app demonstrates logging in through Google plus, or creating a SPiD account from a Google+ account. If you want to implement Google+ login in your own project you have to [create a Google project](https://console.developers.google.com/project) for it in the Google developers console.

1) Create a new project in the Google Developer Console
![Google pic](/images/mobile/android-google-app-1.png)


2) Enable the Google+ API under APIs
![Google pic](/images/mobile/android-google-app-2.png)

3) Make sure Google+ API is displayed under Enabled APIs
![Google pic](/images/mobile/android-google-app-3.png)

4) Create your OAuth Client, choose the Installed application type
![Google pic](/images/mobile/android-google-app-4.png)

![Google pic](/images/mobile/android-google-app-5.png)

5) Configure your consent screen. Add at least your product name, other fields are optional
![Google pic](/images/mobile/android-google-app-6.png)

6) Once you save the consent screen the create client ID should automatically be shown. Choose Installed application and Android. Your package name must conform to what you use in your app. To find your signing certificate fingerprint use the Java keytool command. The default location of the debug keystore is ~/.android/debug.keystore, copy the value for the SHA1 fingerprint.

	keytool -list -v -keystore <keystore>

![Google pic](/images/mobile/android-google-app-7.png)

7) Update the credentials in your app, add your uri scheme in the AndroidManifest and verify that you can run the app and login using Google+.


