# Getting started with the server-side SPiD API

When you have completed this guide, you will have connected your application to
the SPiD API and made your first call, verifying that your client ID and secrets
are correctly configured. You will then be ready to implement login with SPiD.

**NB!** In order to complete this guide, you need to know your client ID and API
secret. If you do not, [see the merchant setup page](http://whoknows).

If you are looking to get started with the client-side SPiD API (Android, iOS,
JavaScript), [see the client-side getting started](#?).

## Downloading the appropriate SDK

To work with the SPiD APIs, it is recommended that you use one of the official
SDKs. The SDKs are thin wrappers that primarily spare you the details of working
with OAuth. If an SDK is not available for your language, skip this section and
refer to the cURL examples below.

**NB!** Your API secret is highly sensitive. Do not hard-code it, and be careful
who you share it with. The examples below are meant to illustrate the basics of
using the API, they are *not* examples of production code. For more complete
examples, see the example use-cases.

### :tabs Downloading the SDK

#### :tab Java
Download the Java SDK by cloning the GitHub repository:

```sh
git clone https://github.com/schibsted/sdk-java.git
```

If you do not have Git installed, you can also download a
[zip file](https://github.com/schibsted/sdk-java/archive/master.zip).

Now install the SDK into your `.m2` repository:

```sh
cd sdk-java
mvn install
```

#### :tab PHP

Download the PHP SDK by cloning the GitHub repository:

```sh
git clone https://github.com/schibsted/sdk-php.git
```

If you do not have Git installed, you can also download a
[zip file](https://github.com/schibsted/sdk-php/archive/master.zip).

Finally, make sure that the SDK is on your PHP system's `include_path`. You can
add the files to your project folder, or include it like this:

```php
// Add SPiD SDK to include_path
set_include_path(get_include_path() . PATH_SEPARATOR . "/path/to/sdk");
```

#### :tab Clojure

Eventually, the Clojure SDK will be available from Clojars. However, as the Java
SDK still is not yet available on a central nexus, you need to build both on
your own for now.

Download and install the Java SDK:

```sh
git clone https://github.com/schibsted/sdk-java.git
cd sdk-java
mvn install
```

Download the Clojure SDK by cloning it from GitHub:

```sh
git clone https://github.com/schibsted/sdk-spid-clojure.git
```

If you do not have Git installed, you can also download a
[zip file](https://github.com/schibsted/sdk-spid-clojure/archive/master.zip).

Install (you will need to have [Leiningen](http://leiningen.org/) installed for
this)

```sh
cd sdk-spid-clojure
lein intall
```

Finally, use it in your `project.clj`:

```clojure
[spid-sdk-clojure "0.1.0"]
```

#### :tab Android

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
assumes you updated your `$PATH` as described above.

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

#### :tab JavaScript

Download the JavaScript SDK by downloading the
[latest version from GitHub](https://github.com/schibsted/sdk-js/tree/master/dist).
Note that both the unminified and minified versions bundle the JSON2.js script.
If you do not need it, download the source code and build it yourself. Refer to
the [JS SDK docs](https://github.com/schibsted/sdk-js) for how to do this.

### :/tabs

## Interacting with the API

Now that you have installed an SDK, you will use it to make first contact with
the SPiD API. Don't worry, it will be quick and painless. When you've got
everything set up, you might want to continue with configuring single sign-on.

### :tabs Getting all endpoints

#### :tab Java

The following is a minimal example of using the Java SDK. It fetches the
/endpoints endpoint, which returns a description of all available endpoints.

```java
import no.spp.sdk.client.ServerClientBuilder;
import no.spp.sdk.client.SPPClient;
import no.spp.sdk.client.SPPClientResponse;
import no.spp.sdk.exception.SPPClientException;
import no.spp.sdk.exception.SPPClientResponseException;
import no.spp.sdk.exception.SPPClientRefreshTokenException;
import no.spp.sdk.oauth.ClientCredentials;

public class GettingStarted {
    // Where SPiD will redirect users after login, leave empty for now, not
    // relevant for endpoints that don't require an authenticated user
    private static String REDIRECT_URI = "";

    // Test against the staging area
    private static String SPP_BASE_URL = "https://stage.payment.schibsted.no";

    public static void main(String[] argv) {
        final String clientId = argv[0];
        final String secret = argv[1];
        final ClientCredentials credentials = new ClientCredentials(clientId, secret, REDIRECT_URI);

        try {
            SPPClient client = new ServerClientBuilder(credentials).withBaseUrl(SPP_BASE_URL).build();
            String responseJSON = client.GET("/endpoints").getResponseBody();
            System.out.println(responseJSON);
        } catch (SPPClientException | SPPClientResponseException | SPPClientRefreshTokenException e) {
            System.out.println("Failed to interact with the SPiD API");
            e.printStackTrace();
        }
    }
}
```

You can run this code from the example repository, filling in your actual
client-id and secret:

```sh
mvn compile -q exec:java -Dexec.mainClass="GettingStarted" -Dexec.args="client-id secret"
```

This returns about 40K of compressed, highly unreadable JSON data. If you have
Python installed (most *nixes have, including OSX), you can improve the output
by piping to python while including the `json.tool` library:

```sh
mvn compile -q exec:java -Dexec.mainClass="GettingStarted" -Dexec.args="client-id secret" | python -m json.tool
```

When working with the SDK from Java, you might be more interested in
`SPPClientResponse`'s `getJSONObject()` method:

```java
SPPClient client = new ServerClientBuilder(credentials).withBaseUrl(SPP_BASE_URL).build();
JSONObject result = client.GET("/endpoints").getJSONObject();
```

#### :tab PHP

The following is a minimal example of using the PHP SDK. It fetches the
/endpoints endpoint, which returns a description of all available endpoints.

```php
<?php
require_once("../../../sdk-php/src/Client.php");

$client = new VGS_Client(array(
    VGS_Client::CLIENT_ID          => $argv[1],
    VGS_Client::CLIENT_SECRET      => $argv[2],
    VGS_Client::CLIENT_SIGN_SECRET => $argv[3],
    VGS_Client::STAGING_DOMAIN     => "stage.payment.schibsted.no",
    VGS_Client::HTTPS              => true,
    VGS_Client::REDIRECT_URI       => "http://localhost:8181/explorer.php",
    VGS_Client::DOMAIN             => "localhost:8181",
    VGS_Client::COOKIE             => true,
    VGS_Client::API_VERSION        => 2,
    VGS_Client::PRODUCTION         => false
));

$client->auth();
echo var_dump($client->api("/endpoints"));
```

You can run this code from the example repository, filling in your actual
client-id, secret and signing secret:

```sh
php getting-started.php client-id secret sign-secret
```

This will print the JSON-decoded response from the server, which shows all
available endpoints along with details on how to interact with them.

#### :tab Clojure

The following is a minimal example of using the Clojure SDK. It fetches the
/endpoints endpoint, which returns a description of all available endpoints.

```clojure
(ns getting-started.core
  (:require [spid-sdk-clojure.core :refer [create-client GET]]))

(defn test-run-api [client-id secret]
  (let [options {:spp-base-url "https://stage.payment.schibsted.no"}
        client (create-client client-id secret options)]
    (clojure.pprint/pprint (GET client "/endpoints"))))
```

You can run this code from the example repository, filling in your actual
client-id and secret:

```sh
lein run client-id secret
```

This will pretty-print the JSON-decoded response from the server, which shows
all available endpoints along with details on how to interact with them.

#### :tab Android

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

#### :tab JavaScript

The JavaScript SDK is different from the other SDKs in that it only provides
information about the user's authentication/authoriation status. It is not a
general purpose SDK to make arbitrary requests against the API. A Node SDK
is being developed to fill this role for server-side JavaScript.

The user's logged in status may change while on your site due to inactivity,
logging out elsewhere or other reasons. For this reason, it is recommended
to subscribe to changes in the user's status.

```javascript
VGS.Event.subscribe("auth.login", function (data) { console.log("auth.login", data); });
VGS.Event.subscribe("auth.logout", function (data) { console.log("auth.logout", data); });

VGS.Event.subscribe("auth.sessionChange", function (data) {
    console.log("auth.sessionChange", data);
    var output = document.getElementById("spid");

    if (!data.session) {
        output.innerHTML = "Welcome. Please <a href=\"" + VGS.getLoginURI() + "\">log in</a>";
    } else {
        output.innerHTML = "Welcome <a href=\"" + VGS.getAccountURI() + "\">" +
            data.session.displayName + "</a>" +
            " <a href=\"" + VGS.getLogoutURI() + "\">Log out</a>";
    }
});

VGS.init({
    client_id: "52f8e3d9efd04bb749000000",
    server: "stage.payment.schibsted.no",
    prod: false
});
```

The `prod` property should be set to `true` (or omitted) when running against
the production server.

The example can be run from the example directory by loading index.html in your
browser. The file will reveal your own login status.

#### :tab cURL

Using [cURL](http://curl.haxx.se/dlwiz/) to interact with the API is a good way
to gain insight into how it works at the networking level. It is also the most
direct way to ensure your credentials are correct as there are fewer layers of
abstraction that might fail/be used wrongly.

Start by requesting an OAuth token:

```sh
curl -X -d "client_id=CLIENT-ID&client_secret=SECRET&grant_type=client_credentials" https://stage.payment.schibsted.no/oauth/token
```

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

```sh
curl https://stage.payment.schibsted.no/api/2/endpoints?oauth_token=68d602d1a3d3cc1b2805cdeb53fb5207d273a7ec
```

If you have Python installed (most *nixes, including OSX, does), you can grab a
nicely formatted JSON document this way:

```sh
curl https://stage.payment.schibsted.no/api/2/endpoints?oauth_token=68d602d1a3d3cc1b2805cdeb53fb5207d273a7ec | python -m json.tool
```

### :/tabs
