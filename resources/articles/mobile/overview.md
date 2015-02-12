:title Overview

:aside

## Native mobile development


- Overview
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

Using SPiD you will gain access to a single sign-on system consisting of users from all Schibsted subsidiaries connected to SPiD as well as simplifying the user experience and increasing your conversion rate.

The SPiD mobile SDKs provides components that simplify integrating SPiD into your app. Login is handled using [OAuth 2.0](/mobile/oauth-authentication-on-mobile-devices/) and a number of different login methods are supported; natively, using an external browser, webview, Facebook or Google+.

To develop an app for SPiD the following needs to be done.

### 1. Create a user for your company

Create an account for either [Swedish](https://stage.payment.schibsted.se) or [Norwegian](https://stage.payment.schibsted.no) environment if you do not already have one. A schibsted.com address is needed to register an account. Keep in mind this is for the stage environment and you will need to replace these before deploying your app to production.

### 2. Create a merchant

Create a merchant for your company if you do not already have one. This merchant can hold a number of clients so if you already have a merchant use that.

### 3. Create a client and request stage credentials

Create the clients you need, configure them and request credentials. These credentials are valid only for the stage environment, before you receive credentials you can use in production you need to submit your app for [review](/mobile/reviews/).

### 4. Review the default permissions

Review the [default permissions](https://docs.google.com/a/schibstedpayment.no/spreadsheets/d/1EwEV0jg4SCeqyEST_Qf09D83XaUiHSveWVrr1G5WMcM/edit#gid=0) for mobile clients, these should be enough for most clients. If additional permissions are required email support@spid.no with the topic
"Mobile permissions" stating what additional endpoints you need access to and for what purpose.

### 5. Develop your app

Develop and test your app, following our recommendations for [best practices](/mobile/best-practices/).

### 6. Submit your app for review

Once your app is done and you are sure you follow the guidelines submit it for review. Review of an app can take up to one month so be sure to submit your app for review in ample time before intended release.

### 7. Replace stage credentials

If your app passes the review you will receive credentials that you can use in production, replace your stage credentials with these when you are ready to deploy your app.

### 8. Deploy

Deploy your app.

[Get started](/mobile/mobile-development/) right now!
