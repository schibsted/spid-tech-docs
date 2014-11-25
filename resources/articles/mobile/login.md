:title Login

:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- Login
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Access tokens](/mobile/access-tokens/)
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Migration](/mobile/migration/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

When you have completed this guide, you will have connected your application to
the SPiD API and made your first call, verifying that your client ID and secrets
are correctly configured. You will then be ready to implement login with SPiD.

## Downloading the appropriate SDK/Client

To work with the SPiD APIs, it is recommended that you use one of the official
SDKs. The SDKs are thin wrappers that primarily spare you the details of working
with OAuth. If an SDK is not available for your language, skip this section and
refer to the cURL examples below.

**NB!*- Your API secret is highly sensitive. Do not hard-code it, and be careful
who you share it with. The examples below are meant to illustrate the basics of
using the API, they are *not- examples of production code. For more complete
examples, see the example use-cases.

# :tabs

## :tab iOS

Download the iOS SDK by cloning the GitHub repository:

```sh
git clone https://github.com/schibsted/sdk-ios.git
```

If you do not have Git installed, you can also
[download it as a zip file](https://github.com/schibsted/sdk-ios/archive/master.zip).

Linking the SDK into your project requires a few steps:

- Find the `.xcodeproj` view. At the bottom of the *General- view,
  under *Linked frameworks and Libraries*, click *+- and *Add
  other*. Locate the SDK you downloaded, and add it.

- Also link in `AdSupport.framework` as an Optional link. It is only
  used during build.

- Go to *Build Settings*, search for `linking` and add `-ObjC` to *Other Linker Flags*

## :tab Android

In order to use the Android SPiD SDK, you must fist install the
[Android SDK](http://developer.android.com/sdk/index.html). Run the Android SDK
Manager (`./sdk/tools/android sdk`) and make sure you have installed at least
SDK versions 2.3, 4.0, 4.1 and 4.2.

To run the examples, you will also need
[Maven 3.1.1 or newer](http://maven.apache.org/download.cgi). As per the
[maven-android-plugin getting started](https://code.google.com/p/maven-android-plugin/wiki/GettingStarted), set
environment variable `ANDROID_HOME` to the path of your installed Android SDK
and add `$ANDROID_HOME/tools` as well as `$ANDROID_HOME/platform-tools` to your
$PATH. (or on Windows `%ANDROID_HOME%\tools` and
`%ANDROID_HOME%\platform-tools`).

Because the Android libraries are not available from any central Maven
repository, you also need to use the
[Maven Android SDK Deployer](https://github.com/mosabua/maven-android-sdk-deployer)
in order to install the Android libraries to your ~/.m2 directory in order to
build the SPiD Android SDK.

```sh
git clone https://github.com/mosabua/maven-android-sdk-deployer.git
cd maven-android-sdk-deployer
mvn install -P 2.3,4.0,4.1,4.2
```

Download the Android SDK by cloning it from GitHub:

```sh
git clone git@github.com:schibsted/sdk-android.git
```

Start by installing the root project, which will build and install the SDK,
enabling you to run the provided examples.

```sh
cd sdk-android
mvn install
```

To verify your install either install it on a real device or create a Android Virtual Device, AVD. The instructions below
presumes you updated your `$PATH` as described above.

```sh
android avd
```

If you haven't already, create a new virtual device by clicking "New". Give it a
name, and choose a device, like Nexus 4. Finally, choose "Google APIs - API
Level 17" as "Target" and click "Ok".

Select the device from the list and click "Start" to launch it. This will take a
while.

Before running one of the example apps you need to update it with your own credentials. Instructions how they can be requested are found here (TODO: add link). You also need to set the "android:scheme" in the AndroidManifest.xml to be "spidmobile-<your-client-id>".

Once this is done and you have a physical device or your emulator is running, make sure it works by running one of the SPiD example apps:

```sh
cd SPiDExampleApp
mvn android:deploy
```

This will install the app on the emulator/device. Once installed you have successfully setup your first SPiD app. Congratulations!



Once installed make sure you can log in using your own credentials. If you can successfully log in you have set up your first SPiD application. Congratulations!

# :/tabs

## Interacting with the API

Now that you have installed a SDK/Client, you will use it to make first contact with
the SPiD API. Don't worry, it will be quick and painless. When you've got
everything set up, you might want to continue with configuring single sign-on.

# :tabs

## :tab iOS

The following is a minimal example of using the iOS SDK. It fetches the
`/endpoints` endpoint, which returns a description of all available endpoints.

Open your `*AppDelegate.m` file. In the example, this is `MyAppDelegate`.

We set up the `SPiDClient` singleton, then we need to fetch a client
token to make the `/endpoints` API call.

```objc
#import "MyAppDelegate.h"
#import "SPiDClient.h"
#import "SPiDResponse.h"
#import "SPiDTokenRequest.h"

static NSString *const ClientID = @"your-client-id";
static NSString *const ClientSecret = @"your-client-secret";
static NSString *const AppURLScheme = @"https";
static NSString *const ServerURL = @"https://stage.payment.schibsted.no";

@implementation MyAppDelegate

- (void)getClientToken:(void (^)(SPiDError *response))completionHandler
{
    SPiDRequest *clientTokenRequest = [SPiDTokenRequest clientTokenRequestWithCompletionHandler:completionHandler];
    [clientTokenRequest startRequest];
}

- (void)getEndpoints:(void (^)(SPiDResponse *response))completionHandler
{
    NSString *path = [NSString stringWithFormat:@"/endpoints"];
    SPiDRequest *request = [SPiDRequest apiGetRequestWithPath:path completionHandler:completionHandler];
    [request startRequestWithAccessToken];
}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [SPiDClient setClientID:ClientID
               clientSecret:ClientSecret
               appURLScheme:AppURLScheme
                  serverURL:[NSURL URLWithString:ServerURL]];

    [self getClientToken:^(SPiDError *error) {
        if (error) {
            NSLog(@"Error: %@", error);
        } else {
            [self getEndpoints:^(SPiDResponse *response) {
                NSLog(@"Endpoints: %@", [response message]);
            }];
        }
    }];

    return YES;
}

@end
```

## :tab Android

To use the SPiD Android SDK you must first make the library available to your
application. Add the SPiD SDK as a dependency to your pom.xml:

```xml
<dependency>
    <groupId>com.schibsted.android</groupId>
    <artifactId>spid</artifactId>
    <version>1.1.3</version>
</dependency>
```

The `SPiDClient` class is a singleton, and you should configure it in the
`onCreate` handler in your main activity:

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    // ...

    SPiDClient.getInstance().configure(new SPiDConfigurationBuilder()
            .clientID("your-client-id")
            .clientSecret("your-client-secret")
            .appURLScheme("spid-example")
            .serverURL("https://stage.payment.schibsted.no")
            .context(this)
            .debugMode(true)
            .build());

    // ...
}
```

The app URL scheme is used when sending redirects to your app. It should be "spid-<your-client-credentials>" (without the angle brackets, so if your client-id is 123 your url-scheme would be spid-123). 

In order to access the endpoints that don't require a logged in user, you must
set the client up with an OAuth access token:

```java
SPiDTokenRequest request = new SPiDTokenRequest(new SPiDAuthorizationListener() {
    public void onComplete() {
        String token = SPiDClient.getInstance().getAccessToken().getAccessToken();
        System.out.println("OAuth token: " + token);
    }

    public void onSPiDException(SPiDException exception) {}
    public void onIOException(IOException exception) {}
    public void onException(Exception exception) {}
});

request.addBodyParameter("grant_type", "client_credentials");
request.addBodyParameter("client_id", SPiDClient.getInstance().getConfig().getClientID());
request.addBodyParameter("client_secret", SPiDClient.getInstance().getConfig().getClientSecret());
request.addBodyParameter("redirect_uri", "http://localhost");

request.execute();
```

The redirect URL doesn't have any effect in this scenario, but it is required
input to the API. Once the request completes successfully, the `SPiDClient`
singleton will have been configured to use it. You can now request the
/endpoints endpoint, which describes all available endpoints in the API.

```java
SPiDApiGetRequest request = new SPiDApiGetRequest("/endpoints", new SPiDRequestListener() {
    public void onComplete(SPiDResponse result) {
        // Print 40k of unformatted JSON to Android Logcat
        System.out.println(result.getBody());
    }

    public void onSPiDException(SPiDException exception) {}
    public void onIOException(IOException exception) {}
    public void onException(Exception exception) {}
});

request.addQueryParameter("oauth_token", SPiDClient.getInstance().getAccessToken().getAccessToken());

request.execute();
```

In practice, it is likely that you will start by logging in the user and then
interacting with the API on their behalf. You will learn how to do this from the
Single Sign-On guide.

# :/tabs