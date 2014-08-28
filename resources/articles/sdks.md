:frontpage
:category api-integration
:aside
## SDKS

- [PHP](#php)
- [Java](#java)
- [Clojure](#clojure)
- [JavaScript](#javascript)
- [Android](#android)
- [iOS](#ios)

## See also

- [Getting started](/getting-started/)
- [Implementing single sign-on](/implementing-sso/)
- [Endpoints](/endpoints/)

:title SPiD API clients/Software development kits

:body

To assist you in integrating with the platform, SPiD provides officially
supported API clients/SDKs. The SDKs implement the OAuth communication and token
exchanges (including renewing expired tokens etc), as well as automating part of
the API call workflow.

There are standard server-side SDKs for PHP, Java and Clojure. Additionally,
there is a simpler JavaScript client that is read-only (no OAuth support) and
may be used client-side. There are also Android and iOS SDKs.

All SDKs are available on [Schibsted's GitHub account](https://github.com/schibsted/).

## PHP

To use the PHP SDK, you need to fetch the
[source code](https://github.com/schibsted/sdk-php) from GitHub:

```sh
git clone git://github.com/schibsted/sdk-php.git spid-sdk
```

There are currently no API docs for the PHP SDK. The
[Getting started](/getting-started/) and
[Implementing Single Sign-On](/implementing-sso/) guides will get you off on a
flying start. The source code repository also has
[multiple examples](https://github.com/schibsted/sdk-php/tree/master/examples).

## Java

The [Java API client](https://github.com/schibsted/spid-client-java/) is
available from Maven Central. Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>no.spid</groupId>
    <artifactId>no.spid.api.client</artifactId>
    <version>1.2</version>
</dependency>
```

In lieu of API docs, please refer to the [Getting started](/getting-started/) and
[Implementing Single Sign-On](/implementing-sso/) guides for a flying start.
There is also a separate
[repository of examples](https://github.com/schibsted/spid-java-examples).

## Clojure

The [Clojure API client](https://github.com/schibsted/spid-client-clojure) can
be installed via Clojars. Add to your `project.clj` `:dependencies`:

```clj
[spid-client-clojure "1.0.0"]
```

In lieu of API docs, please refer to the [Getting started](/getting-started/) and
[Implementing Single Sign-On](/implementing-sso/) guides for a flying start. The
[project Readme](https://github.com/schibsted/spid-client-clojure) has some
rudimentary usage information. There is also a separate
[repository of examples](https://github.com/schibsted/spid-clj-examples).

## JavaScript

The JavaScript SDK is different from the PHP, Java and Clojure ones in that it
is read-only, does not use OAuth, and is designed to be used in the browser to
query the user's authentication/authorization status.

Source code and distribution files are on
[Github](https://github.com/schibsted/sdk-js), refer to the
[JavaScript SDK](/sdks/javascript/) page for full API documentation and usage
examples.

[Documentation and examples &raquo;](/sdks/javascript/)

## Android

Enables easy SPiD login from Android applications. Supported for Android version
2.2 (API level 8) or greater.
[Check out the code](http://schibsted.github.io/sdk-android/) and include it in
your project, then refer to the SDK documentation on
[getting started](http://schibsted.github.io/sdk-android/setting-up-spid.html),
or see the
[included sample applications](https://github.com/schibsted/sdk-android).

API documentation and more information is available from the
[project's wiki](http://schibsted.github.io/sdk-android/).

## iOS

Enables easy SPiD login from iOS applications.
[Check out the code](http://schibsted.github.io/sdk-ios/) and include it in you
project, then refer to the SDK documentation on
[getting started](http://schibsted.github.io/sdk-ios/setting-up-spid.html), or
see the [included sample applications](https://github.com/schibsted/sdk-iOS).

API documentation and more information is available from the
[project's wiki](http://schibsted.github.io/sdk-ios/).
