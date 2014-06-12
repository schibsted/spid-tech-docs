:title Implementing Single Sign-On with the SPiD API

:aside

## Prerequisites

In order to complete this guide, you need to know your:

- client ID
- API secret

You should also have gone through the
[Getting Started](/getting-started/) guide, in particular that you
have downloaded and installed the appropriate SDK for your platform.

:body

When you have completed this guide, your users can log into your
application via SPiD, and you can access the SPiD API on their
behalf.

## Overview

This is the flow to log in a user:

- The user is sent to the SPiD login page along with your client ID.
- Once the user is logged in, they are sent back to your site with a code.
- You use the code to fetch user information and set up a session.

We'll look at this in detail in the rest of the guide. If you prefer
to just dive in, take a look at
[these working examples](#working-examples).

## Configure your application

These variables change between production and staging environments:

- Your client ID
- Your client secret
- Your client signature secret
- The base URL to SPiD
- Your base URL

How you choose to configure your application is up to you, but
these variables should not be hard coded.

## Get ready to receive the user's login code

The SPiD login page expects a redirect URI back to your site, where
the user will be sent after logging in. This is where you'll
create the user's local session with the given code.

Let's just set up a basic handler for this now, and we can fill it
in later.

# :tabs

## :tab Java

```java
@RequestMapping("/create-session")
String createSession(@RequestParam String code) {
    return "redirect:/";
}
```

## :tab PHP

```php
<?php // createSession.php
header("Location: /");
```

## :tab Clojure

```clj
(defn create-session [code]
  {:status 302
   :headers {"Location" "/"}})

(defroutes routes
  (GET "/create-session" [code] (create-session code)))
```

# :/tabs

## Send the user to the SPiD login page

Once you've got your configuration, you can patch together the URL
to the SPiD login page. It's on `<spid-base-url>/login`,
with these parameters:

- `client_id`: Yeah, it's your client ID.
- `response_type`: Which is always `code` in this version of the API.
- `redirect_uri`: The URI where the user is redirected after logging in.

Patch together and use it for your login link.

**NB!** `redirect_uri` has to be a full URL back to your site.
The domain also has to match the predefined URI that you have
registered with SPiD. Only predefined redirect URIs are accepted by
the SPiD login page.

### Build login URL

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Build login URL"/>

## :tab PHP

<spid-example lang="php" src="/sso/index.php" title="Build login URL"/>

## :tab Clojure

<spid-example lang="clj" src="/sso/src/spid_clojure_sso_example/core.clj" title="Build login URL"/>

# :/tabs

The login URL can be served directly to your end users for logging in. As
SPiD supports *remember me* type functionality there is no need for
users to make a detour through a local `/login` URL or similar.

## Create an API client with the given login code

When the user finishes logging in with SPiD, they will be redirected
back to your application via the redirect URI you provided. The
redirect will come with a code. Using this code, you can create a
client to communicate with the SPiD API on behalf of the user.

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Create user client"/>

## :tab PHP

The SPiD SDK for PHP needs a few config variables:

<spid-example lang="php" src="/sso/config.php.sample" title="SDK variables"/>

Create the client with the config:

<spid-example lang="php" src="/sso/createSession.php" title="Create user client"/>

## :tab Clojure

<spid-example lang="clj" src="/sso/src/spid_clojure_sso_example/core.clj" title="Create user client"/>

# :/tabs

## Fetch user information and create a session

Use the API client you just created to fetch basic user information,
and create a local session with it. You should also make sure to hang
on to the client. You'll need it later.

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Fetch user information and add to session"/>

## :tab PHP

<spid-example lang="php" src="/sso/createSession.php" title="Fetch user information and add to session"/>

## :tab Clojure

<spid-example lang="clj" src="/sso/src/spid_clojure_sso_example/core.clj" title="Fetch user information and add to session"/>

# :/tabs

## Log user out

When the user wants to log out, just deleting the local session isn't
sufficient. They should also be logged out of SPiD. Otherwise they'll have a
hard time logging in as another user, and they will still be logged into SPiD
even if they think they have logged out.

To get this right, you should:

- delete the local session
- redirect the user to the SPiD logout URL

In addition to the user's access token, you pass along another
redirect URI, so that the user is sent back to your site after
logging out of SPiD.

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Log user out"/>

## :tab PHP

<spid-example lang="php" src="/sso/logout.php" title="Log user out"/>

## :tab Clojure

<spid-example lang="clj" src="/sso/src/spid_clojure_sso_example/core.clj" title="Log user out"/>

```clj
(defroutes routes
  ;; ...
  (GET "/logout" request (log-user-out request)))
```

## :/tabs

## Working examples

If you're unsure on certain details after reading this guide, do check
out these working examples:

- [SSO example for PHP](https://github.com/schibsted/spid-php-examples/tree/master/sso)
- [SSO example for Java](https://github.com/schibsted/spid-java-examples/tree/master/sso)
- [SSO example for Clojure](https://github.com/schibsted/spid-clj-examples/tree/master/sso)
