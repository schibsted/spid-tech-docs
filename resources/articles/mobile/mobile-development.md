:title Getting started with mobile development using the SPiD API

:aside

## Native mobile development

- [Overview](/mobile/overview)
- Getting started
- [Register](/mobile/register)
- [Login](/mobile/login)
- [Android](/sdks/android)
    - [Android sample apps](/sdks/android/sample-apps)
    - [API](/sdks/android/api)
- [iOS](/sdks/ios)
    - [iOS sample apps](/sdks/ios/sample-apps)
- [Access tokens](/mobile/access-tokens)
- [Reviews](/mobile/reviews)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices)
- [Migration](/mobile/migration)
- [Best practices](/mobile/best-practices)
- [FAQ](/mobile/faq)

:body

To develop an app for SPiD the following needs to be done.

### 1. Create a user for your company

Create an account [here](https://stage.payment.schibsted.se) if you do not already have one, a schibsted.com email address is needed to register an account.

**TODO:** Need a document on user account management we can link to

### 2. Create a merchant

Create a merchant for your company.

**TODO:** Add link to a document explaining to what a merchant/client is for 2 and 3

### 3. Create a client and request stage credentials

Create the clients you need, decide which end points are required (TODO: What more can  be configured for clients?) and request credentials. These credentials are valid only for the stage environment, before you receive credentials you can use for production you need to submit your app for review.

### 4. Develop your app

Develop and test your app, following our recommendations for [best practices](/mobile/best-practices).

### 5. Submit your app for review

Once your app is done and you're sure you follow the [guidelines](/mobile/reviews) submit it for review.

### 6. Replace stage credentials

If your app passes the review you will receive credentials that you can use in production, replace your stage credentials with these when you are ready to deploy your app.

### 7. Deploy

Deploy your app.
