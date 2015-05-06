:title Getting started with the server-side SPiD API

:aside
## Table of Contents

<spid-toc></spid-toc>

## Prerequisites

In order to complete this guide, you need to know your:

- client ID
- API secret

If you do not, [please set up your API client via our self service tool](/ss-access/)

## See also

- [SPiD Authentication](/authentication/)
- [Explaining how login flows work](/login-flows/)
- [Explaining how logout flows work](/logout-flows/)
- [The JavaScript SDK](/sdks/javascript/)
- [Native mobile development](/mobile/overview/)

:body

When you have completed this guide, you will have connected your application to
the SPiD API and made your first call, verifying that your client ID and secrets
are correctly configured. You will then be ready to implement login with SPiD.

## Downloading the appropriate SDK/Client

To work with the SPiD APIs, it is recommended that you use one of the official
SDKs. The SDKs are thin wrappers that primarily spare you the details of working
with OAuth. If an SDK is not available for your language, skip this section and
refer to the cURL examples below.

**NB!** Your API secret is highly sensitive. Do not hard-code it, and be careful
who you share it with. The examples below are meant to illustrate the basics of
using the API, they are *not* examples of production code. For more complete
examples, see the example use-cases.

# :tabs

## :tab Java

The Java API client library is deployed in Maven central, just add it to your project's `pom.xml` file:

<spid-example lang="html" src="/java/getting-started/pom.xml" title="Add SPiD client"/>

## :tab PHP

Download the PHP SDK by cloning the GitHub repository:

```sh
git clone https://github.com/schibsted/sdk-php.git
```

If you do not have Git installed, you can also download a
[zip file](https://github.com/schibsted/sdk-php/archive/master.zip).

## :tab Clojure

The Clojure client is deployed in Clojars, just put it in your `project.clj`:

```clojure
[spid-client-clojure "1.0.0"]
```

## :tab iOS

Download the iOS SDK by cloning the GitHub repository:

```sh
git clone https://github.com/schibsted/sdk-ios.git
```

If you do not have Git installed, you can also
[download it as a zip file](https://github.com/schibsted/sdk-ios/archive/master.zip).

Linking the SDK into your project requires a few steps:

- Find the `.xcodeproj` view. At the bottom of the *General* view,
  under *Linked frameworks and Libraries*, click *+* and *Add
  other*. Locate the SDK you downloaded, and add it.

- Also link in `AdSupport.framework` as an Optional link. It is only
  used during build.

- Go to *Build Settings*, search for `linking` and add `-ObjC` to *Other Linker Flags*

## :tab Android

In order to use the Android SPiD SDK, you must fist install the
[Android SDK](http://developer.android.com/sdk/index.html). Run the Android SDK
Manager (`./sdk/tools/android sdk`) and make sure you have installed at least
SDK versions 2.2, 4.0, 4.1 and 4.2.

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
mvn install -P 2.2,4.0,4.1,4.2
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

To verify your install, run the AVD - Android Virtual Device Manager. This
presumes you updated your `$PATH` as described above.

```sh
android avd
```

If you haven't already, create a new virtual device by clicking "New". Give it a
name, and choose a device, like Nexus 4. Finally, choose "Google APIs - API
Level 17" as "Target" and click "Ok".

Select the device from the list and click "Start" to launch it. This will take a
while. Maybe 10-15 minutes. Or it might just hang there for an unreasonable long
time. We've had luck with deleting the device and starting over.

When the emulator is running, run the SPiD example app:

```sh
cd SPiDExampleApp
mvn android:deploy
```

This will install the app on the emulator. When it finishes you have
successfully set up the SDK for development. Great success!

# :/tabs

## Interacting with the API

Now that you have installed a SDK/Client, you will use it to make first contact with
the SPiD API. Don't worry, it will be quick and painless. When you've got
everything set up, you might want to continue with configuring single sign-on.

# :tabs

## :tab Java

The following is a minimal example of using the Java API client. It fetches the
/endpoints endpoint, which returns a description of all available endpoints.

<spid-example lang="java" src="/getting-started/src/main/java/no/spid/examples/GettingStarted.java" title="Getting started"/>

You can run this code from the example repository, filling in your actual
client-id and secret:

```sh
mvn install -q exec:java -Dexec.mainClass="no.spid.examples.GettingStarted" -Dexec.args="<client-id> <secret>" -e
```

This returns about 40K of compressed, highly unreadable JSON data. If you have
Python installed (most *nixes have, including OSX), you can improve the output
by piping to python while including the `json.tool` library:

```sh
mvn install -q exec:java -Dexec.mainClass="no.spid.examples.GettingStarted" -Dexec.args="<client-id> <secret>" -e | python -m json.tool
```

When working with the Java client, you might be more interested in
`SpidApiResponse`'s `getJSONData()` method.

## :tab PHP

The following is a minimal example of using the PHP SDK. It fetches the
/endpoints endpoint, which returns a description of all available endpoints.

<spid-example lang="php" src="/getting-started/getting-started.php" title="Getting started"/>

You can run this code from the example repository, filling in your actual
client-id, secret and signing secret:

```sh
php getting-started.php client-id secret sign-secret
```

This will print the JSON-decoded response from the server, which shows all
available endpoints along with details on how to interact with them.

## :tab Clojure

The following is a minimal example of using the Clojure client. It fetches the
/endpoints endpoint, which returns a description of all available endpoints.

<spid-example lang="clj" src="/getting-started/src/getting_started/core.clj" title="Getting started"/>

You can run this code from the example repository, filling in your actual
client-id and secret:

```sh
lein run client-id secret
```

This will pretty-print the JSON-decoded response from the server, which shows
all available endpoints along with details on how to interact with them.

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

The app URL scheme is not important for now, and is explained in detail in the
Single Sign-On implementation guide.

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

## :tab cURL

Using [cURL](http://curl.haxx.se/dlwiz/) to interact with the API is a good way
to gain insight into how it works at the networking level. It is also the most
direct way to ensure your credentials are correct as there are fewer layers of
abstraction that might fail/be used wrongly.

Start by requesting an OAuth token:

<spid-example lang="sh" src="/getting-started.sh" title="Request an OAuth token"/>

If all goes well, you should get a response like this back:

```js
{ "access_token": "68d602d1a3d3cc1b2805cdeb53fb5207d273a7ec",
  "expires_in": 604800,
  "scope": null,
  "user_id": false,
  "is_admin": false,
  "refresh_token": "95ab17a1f78339b7a01b88c748677ed522474e16",
  "server_time": 1392194793 }
```

Using the provided `access_token`, you may now browse the API endpoints:

<spid-example lang="sh" src="/getting-started.sh" title="Fetch API endpoints"/>

The pipe into python makes the JSON document nice and readable. You can of
course skip that part if you don't have python installed.

# :/tabs
