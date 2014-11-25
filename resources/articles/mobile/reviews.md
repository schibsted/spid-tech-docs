:title Reviews

:aside

## Native mobile development

- [Overview](/mobile/overview)
- [Getting started](/mobile/mobile-development)
- [Register](/mobile/register)
- [Login](/mobile/login)
- [Android](/sdks/android)
    - [Android sample apps](/sdks/android/sample-apps)
    - [API](/sdks/android/api)
- [iOS](/sdks/ios)
    - [iOS sample apps](/sdks/ios/sample-apps)
- [Access tokens](/mobile/access-tokens)
- Reviews
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices)
- [Migration](/mobile/migration)
- [Best practices](/mobile/best-practices)
- [FAQ](/mobile/faq)

:body

Before you deploy your app to production you need to replace the credentials you initially used as they are only valid in the stage environment. You will receive your production credentials after you have submitted your app for review and it's been accepted.

The app review can take up to 3 weeks so be sure to submit it in ample time before your scheduled release.

TODO: What can we demand in the review? Access to code / repository?

To ensure that your app passes the review you need to pay special attention to the following items:

### OAuth

The access token is not used directly in requests other then to exchange it for a one time code using the [/oauth/exchange](http://localhost:3002/endpoints/POST/oauth/exchange/) end point. This code is then used when making requests.

Don't print sensitive data such as passwords or access tokens in logs or save them on disk.

### Terms and conditions

Terms and conditions must be accessible for the user from the login page. The user does not have to accept them by forcing them to agree to them in a modal dialog, but they have to be available so the user can easily read through it at login.

### Credentials

The client id and client secret is what identifies your app.

The client id is fully public and does not need to be hidden or obfuscated in anyway. The client secret however should not be stored as a string in plain text but rather be hidden in some way.

### Obfuscation

The app should be obfuscated, in Android this is done using [Proguard](http://developer.android.com/tools/help/proguard.html) TODO: how about for iOS?

### Logo

TODO: Link to our guidelines regarding logos

