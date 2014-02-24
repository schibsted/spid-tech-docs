# Implementing Single Sign-On with the SPiD API

When you have completed this guide, your users can log into your
application via SPiD, and you can access the SPiD API on their
behalf.

**NB!** This guide presupposes that you have gone throught the
[Getting Started](/getting-started) guide, in particular that you have
downloaded and installed the appropriate SDK for your platform.

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
- Your base URL
- The base URL to SPiD

How you choose to configure your application is up to you, but
these variables should not be hard coded.

## Get ready to receive the user's login code

The SPiD login page expects a redirect URI back to your site, where
the user will be sent after logging in. This is where you'll
create the user's local session with the given code.

Let's just set up a basic handler for this now, and we can fill it
in later.

#### :tabs Set up a handler to create the session
##### :tab PHP
```php
<?php // createSession.php
header("Location: /");
```
##### :tab Java
```java
@RequestMapping("/create-session")
String createSession(@RequestParam String code) {
    return "redirect:/";
}
```
#### :/tabs

## Send the user to the SPiD login page

Once you've got your configuration, you can patch together the URL
to the SPiD login page. It's on `<spid-base-url>/oauth/authorize`,
with these parameters:

- `client_id`: Yeah, it's your client ID.
- `response_code`: Which is always `code` in this version of the API.
- `redirect_uri`: The URI where the user is redirected after logging in.

Patch together and use it for your login link.

**NB!** `redirect_uri` has to be a full URL back to your site.
The domain also has to match the predefined URI that you have
registered with SPiD. Only predefined redirect URIs are accepted by
the SPiD login page.

#### :tabs Build login URL
##### :tab PHP
```php
<?php
$createSessionURL = $ourBaseURL . "/createSession.php";
$spidAuthorizeURL = $spidBaseURL . "/oauth/authorize" .
  "?client_id=" . $clientID .
  "&response_type=code" .
  "&redirect_uri=" . $createSessionURL;
```
##### :tab Java
```java
private String getSPIDAuthorizeURL() {
    return spidBaseUrl + "/oauth/authorize" +
        "?client_id=" + clientID +
        "&response_type=code" +
        "&redirect_uri=" + getCreateSessionURL();
}

private String getCreateSessionURL() {
    return ourBaseUrl + "/create-session";
}
```
#### :/tabs

The login URL can be served directly to your end users for logging in. As
SPiD supports *remember me* type functionality there is no need for
users to make a detour through a local `/login` URL or similar.

## Create an API client with the given login code

When the user finishes logging in with SPiD, they will be redirected
back to your application via the redirect URI you provided. The
redirect will come with a code. Using this code, you can create a
client to communicate with the SPiD API on behalf of the user.

#### :tabs Create an API client
##### :tab PHP
```php
<?php
// The SPiD SDK for PHP needs a few more config variables:
$spidClientConfig = array(
    VGS_Client::CLIENT_ID          => $clientID,
    VGS_Client::CLIENT_SECRET      => $clientSecret,
    VGS_Client::CLIENT_SIGN_SECRET => $clientSignSecret,
    VGS_Client::STAGING_DOMAIN     => "stage.payment.schibsted.no",
    VGS_Client::HTTPS              => true,
    VGS_Client::REDIRECT_URI       => $createSessionURL,
    VGS_Client::COOKIE             => false,
    VGS_Client::API_VERSION        => 2,
    VGS_Client::PRODUCTION         => false
);

$client = new VGS_Client($spidClientConfig);

$session = $client->getSession(); // this rudely fetches the code from the request itself
$client->setAccessToken($session['access_token']); // have to help the client remember the access token
```
##### :tab Java
```java
private SPPClient createUserClient(String code) throws SPPClientException {
    ClientCredentials cred = new ClientCredentials(clientID, clientSecret, getCreateSessionURL());
    return new UserClientBuilder(cred)
        .withUserAuthorizationCode(code)
        .withBaseUrl(spidBaseUrl)
        .build();
}
```
#### :/tabs

## Fetch user information and create a session

Use the API client you just created to fetch basic user information,
and create a local session with it. You should also make sure to hang
on to the client. You'll need it later.

#### :tabs Fetch user information and create session
##### :tab PHP
```php
<?php
$user = $client->api('/me');

session_start();
$_SESSION['client'] = $client;
$_SESSION['user'] = $user;

header("Location: /");
```
##### :tab Java
```java
@RequestMapping("/create-session")
String createSession(@RequestParam String code, HttpServletRequest request)
    throws SPPClientException, SPPClientResponseException, SPPClientRefreshTokenException {
    SPPClient client = createUserClient(code);
    JSONObject user = client.GET("/me").getJSONObject();
    HttpSession session = request.getSession();
    session.setAttribute("user", user);
    session.setAttribute("client", client);
    return "redirect:/";
}
```
#### :/tabs

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

#### :tabs Log user out
##### :tab PHP
```php
<?php // logout.php
require_once('./config.php');

session_start();

$client = isset($_SESSION['client']) ? $_SESSION['client'] : false;

if ($client) {
  unset($_SESSION['client']);
  unset($_SESSION['user']);

  $spidLogoutURL = $spidBaseURL . "/logout" .
    "?redirect_uri=" . $ourBaseURL .
    "&oauth_token=" . $client->getAccessToken();

  header("Location: " . $spidLogoutURL);
}
?>
```
##### :tab Java
```java
@RequestMapping("/logout")
String logOutUser(HttpServletRequest request) {
    HttpSession session = request.getSession();
    SPPClient client = (SPPClient) session.getAttribute("client");
    session.removeAttribute("client");
    session.removeAttribute("user");
    return "redirect:" + getSPIDLogoutURL(client);
}

private String getSPIDLogoutURL(SPPClient client) {
    return spidBaseUrl + "/logout" +
        "?redirect_uri=" + ourBaseUrl +
        "&oauth_token=" + client.getOauthCredentials().getAccessToken();
}
```
#### :/tabs

## Working examples

If you're unsure on certain details after reading this guide, do check
out these working examples:

- [SSO example for PHP](...)
- [SSO example for Java](...)
- [SSO example for Clojure](...)
