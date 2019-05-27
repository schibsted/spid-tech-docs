:title Getting started with the server-side Schibsted account API

:aside
## Table of Contents

<spid-toc></spid-toc>

## Prerequisites

In order to complete this guide, you need to know your:

- client ID
- client secret

If you do not, [please set up your API client via our self service tool](/selfservice/access/)

## See also

- [Schibsted account Authentication](/authentication/)
- [Explaining how login flows work](/login-flows/)
- [Explaining how logout flows work](/logout-flows/)
- [The JavaScript SDK](/sdks/javascript/)
- [Native mobile development](/mobile/overview/)

:body

When you have completed this guide, you will have connected your application to
the Schibsted account API and made your first call, verifying that your client ID and secrets
are correctly configured. You will then be ready to implement login with Schibsted account.

## Downloading the appropriate SDK/Client

To work with the Schibsted account APIs, it is recommended that you use one of the official
SDKs. The SDKs are thin wrappers that primarily spare you the details of working
with OAuth. If an SDK is not available for your language, skip this section and
refer to the cURL examples below.

**NB!** Your client secret is highly sensitive. Do not hard-code it, and be careful
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
[spid-client-clojure "1.2.0"]
```

## :tab iOS

In order to use the Schibsted account iOS SDK, see the [getting started documentation](https://schibsted.github.io/account-sdk-ios/#setup).

## :tab Android

In order to use the Schibsted account Android SDK, see the [getting started documentation](https://schibsted.github.io/account-sdk-android/#getting-started).

# :/tabs

## Interacting with the API

Now that you have installed a SDK/Client, you will use it to make first contact with
the Schibsted account API. Don't worry, it will be quick and painless. When you've got
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

See the [published documentation](https://schibsted.github.io/account-sdk-ios/#usage).

## :tab Android

See the [published documentation](https://schibsted.github.io/account-sdk-android/ui/).

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
