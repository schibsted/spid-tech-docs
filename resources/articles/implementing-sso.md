:title Implementing Single Sign-On with the Schibsted account API

:aside
## Table of Contents

<spid-toc></spid-toc>

## Prerequisites

In order to complete this guide, you need to know your:

- client ID
- client secret

You should also have gone through the
[Getting Started](/getting-started/) guide, in particular that you
have downloaded and installed the appropriate SDK for your platform.

## See also

- [Schibsted account Authentication](/authentication/)
- [Explaining how login flows work](/login-flows/)
- [Explaining how logout flows work](/logout-flows/)

:body

When you have completed this guide, your users can log into your
application via Schibsted account, and you can access the Schibsted account API on their
behalf.

## Overview

This is the flow to log in a user:

- The user is sent to the Schibsted account login page along with your client ID.
- Once the user is logged in, they are sent back to your site with a code.
- You use the code to fetch user information and set up a session.

This is a simple overview explaining the complete process between the client
service (yellow) and Schibsted account (blue):
![Single Sign on using redirect flow](/images/simple-sso-redirect-usecase.png)

We'll look at this in detail in the rest of the guide. If you prefer
to just dive in, take a look at
[these working examples](#working-examples).

## Configure your application

These variables change between production and staging environments:

- Your client ID
- Your client secret
- Your client signature secret
- The base URL to Schibsted account
- Your base URL

How you choose to configure your application is up to you, but
these variables should not be hard coded.

## Get ready to receive the user's login code

The Schibsted account login page expects a redirect URI back to your site, where
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

# :/tabs

## Send the user to the Schibsted account login page

Once you've got your configuration, you can patch together the URL
to the Schibsted account login page. It's on `<spid-base-url>/login`,
with these parameters:

- `client_id`: Yeah, it's your client ID.
- `response_type`: Which is always `code` in this version of the API.
- `redirect_uri`: The URI where the user is redirected after logging in.

Patch together and use it for your login link.

**NB!** `redirect_uri` has to be a full URL back to your site.
The domain also has to match the predefined URI that you have
registered with Schibsted account. Only predefined redirect URIs are accepted by
the Schibsted account login page.

### Build login URL

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Build login URL"/>

# :/tabs

The login URL can be served directly to your end users for logging in. As
Schibsted account supports *remember me* type functionality there is no need for
users to make a detour through a local `/login` URL or similar.

## Create an API client with the given login code

When the user finishes logging in with Schibsted account, they will be redirected
back to your application via the redirect URI you provided. The
redirect will come with a code. Using this code, you can create a
client to communicate with the Schibsted account API on behalf of the user.

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Create user client"/>

# :/tabs

## Fetch user information and create a session

Use the API client you just created to fetch basic user information,
and create a local session with it. You should also make sure to hang
on to the client. You'll need it later.

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Fetch user information and add to session"/>

# :/tabs

## Log user out

When the user wants to log out, just deleting the local session isn't
sufficient. They should also be logged out of Schibsted account. Otherwise they'll have a
hard time logging in as another user, and they will still be logged into Schibsted account
even if they think they have logged out.

To get this right, you should:

- delete the local session
- redirect the user to the Schibsted account logout URL

In addition to the user's access token, you pass along another
redirect URI, so that the user is sent back to your site after
logging out of Schibsted account.

# :tabs

## :tab Java

<spid-example lang="java" src="/sso/src/main/java/no/spid/examples/LoginController.java" title="Log user out"/>

# :/tabs

## Working examples

If you're unsure on certain details after reading this guide, do check
out these working examples:

- [SSO example for Java](https://github.com/schibsted/spid-java-examples/tree/master/sso)
