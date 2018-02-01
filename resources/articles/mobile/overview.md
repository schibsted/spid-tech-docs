:title Overview

:aside

## Native mobile development


- Overview
- [Getting started](/mobile/mobile-development/)
- [Self Service](/mobile/selfservice/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
- [iOS](/sdks/ios/)
- [Best practices](/mobile/best-practices/)

:body

Using SPiD you will gain access to a single sign-on system consisting of users from all Schibsted subsidiaries connected to SPiD as well as simplifying the user experience and increasing your conversion rate.

The SPiD mobile SDKs provides components that simplify integrating SPiD into your app. Login is handled using [OAuth 2.0](/mobile/oauth-authentication-on-mobile-devices/) and a number of different login methods are supported; natively, using an external browser, webview, Facebook or Google+.

To develop an app for SPiD the following needs to be done.

### 1. Request access to self service

If you don't already have access to self service request access, see [self service access](/selfservice/access/) for further details.

### 2. Create a client and request stage/pre credentials

Create the clients you need, configure them and request credentials. These credentials are valid only for the stage environment, before you receive credentials you can use in production you need to submit your app for [review](/mobile/reviews/).

### 3. Review the default permissions

Review the default permissions for mobile clients, these should be enough for most clients. If additional permissions are required email support@spid.no with the topic
"Mobile permissions" stating what additional endpoints you need access to and for what purpose.

### 4. Develop your app

Develop and test your app, following our recommendations for [best practices](/mobile/best-practices/).

### 5. Submit your app for review

Once your app is developed and you are sure you follow the guidelines submit it for review. Review of an app can take up to one month so be sure to submit your app for review in ample time before intended release.

### 6. Replace stage credentials

If your app passes the review you will receive credentials that you can use in production, replace your stage credentials with these when you are ready to deploy your app.

### 7. Deploy

Deploy your app.

[Get started](/mobile/mobile-development/) right now!
