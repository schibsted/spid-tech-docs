:frontpage
:category api-integration
:aside
## SDKS

- [Java](#java)
- [JavaScript](#javascript)
- [Android](#android)
- [iOS](#ios)

## See also

- [Getting started](/getting-started/)
- [Implementing single sign-on](/implementing-sso/)
- [Endpoints](/endpoints/)

:title Schibsted account API clients/SDKs

:body

To assist you in integrating with the platform, Schibsted account provides officially
supported API clients/SDKs. The SDKs implement the OAuth communication and token
exchanges (including renewing expired tokens etc), as well as automating part of
the API call workflow.

There are SDKs for Java, Android, iOS and client-side JavaScript.

All SDKs are available on [Schibsted's GitHub account](https://github.com/schibsted/).

## Java

The [Java SDK](https://github.schibsted.io/spt-identity/identity-sdk-java) is
available from [Schibsted Artifactory](). Add the following to your `build.gradle`:

```groovy
repositories {
  maven {
    url "https://artifacts.schibsted.io/artifactory/libs-release-local"
    credentials {
      username = "${artifactory_user}"
      password = "${artifactory_pwd}"
    }
  }
}

compile 'com.schibsted.identity:identity-sdk-java-core:<version>'
```

In lieu of API docs, please refer to the [Getting started](/getting-started/) and
[Implementing Single Sign-On](/implementing-sso/) guides for a flying start.

## JavaScript

The JavaScript SDK is designed to be used in the browser to
seamlessly manage the user's login state and more.

Source code is published on
[Github](https://github.com/schibsted/account-sdk-browser). Refer to the
[JavaScript SDK page](/sdks/javascript/) for more information.

## Android

Supports Android version 4.0 (API level 14) or greater.
[Check out the code](https://github.com/schibsted/account-sdk-android) and include it in
your project, then refer to the SDK documentation on
[getting started](https://schibsted.github.io/account-sdk-android/#getting-started),
or see the
[included example application](https://github.com/schibsted/account-sdk-android/tree/master/example).

## iOS

Supports iOS 9 and above.
[Check out the code](https://github.com/schibsted/account-sdk-ios) and include it in your
project, then refer to the SDK documentation on
[getting started](https://schibsted.github.io/account-sdk-ios/#setup), or
see the [included example application](https://github.com/schibsted/account-sdk-ios/tree/master/Example).

