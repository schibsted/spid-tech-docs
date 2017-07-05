:title Mobile review checklist

:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Self Service](/mobile/selfservice/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- Mobile review checklist
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

Before you deploy your app to production you need to replace the credentials you initially used as they are only valid in the stage/pre environment. You will receive your production credentials after you have submitted your app for review and it has been accepted.

The app review can take up to one month so be sure to submit it in ample time before your scheduled release.

To ensure that your app passes the review you need to pay special attention to the following items:

### OAuth

SPiD uses [OAuth draft 11](https://tools.ietf.org/html/draft-ietf-oauth-v2-11), make sure your implementation also follows the same specification.

Don't print sensitive data such as passwords or access tokens in logs or save them on disk.

### Oauth token usage

Use your oauth tokens until they expire, then either use your refresh token or authenticate again to acquire a new token. Do not request a new token for each API call.

### Credentials

The client id is fully public and does not need to be hidden or obfuscated in anyway. The client secret however should not be stored as a string in plain text but rather be hidden in some way. Because of the nature of mobile apps it's hard to keep secrets confidential, as a minimal effort we require clients to obfuscate credentials. This will not deter determined attackers but is considered better than storing them in plain text.

The review must be made using the stage/pre credentials of the client to be reviewed.

### Terms and conditions

Terms and conditions must be accessible to the user from the login page, the terms are fetched from the [terms](/endpoints/GET/terms/) endpoint. The user does not have to accept the terms by using a modal dialog, but they have to be available so the user can easily read through them at login.

### Logo

Make sure you display the SPiD logo according to the guidelines as described in our [brandbook](/images/brandbook.pdf).

### Obfuscation

If developing for Android the app should be obfuscated, this is done using [Proguard](http://developer.android.com/tools/help/proguard.html). For your own sake remember to store the mapping key so you can easily debug stacktraces.

### Submit for review

When you have confirmed you follow these guidelines and your app is ready for review send an email to support@spid.no with the topic "App review" including following info:  

- Name and the clientid of the app and where we can find it 
- Given access through TestFlight/Catchlyist(for iPhone) or links we need(for Android) to be able to review the app
- Name and email of the tech lead of your client
