:title Best practices

:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- Best practices
- [FAQ](/mobile/faq/)

:body

Below are best practices we encourage you to keep in mind while developing your app for use with SPiD.

### Input

Don't assume input is valid. Always sanitize it, consider keeping a whitelist for what characters are accepted, check that the length of the string is acceptable etc.

### Passwords

Never save a user's password in memory or file, or print in logs. Instead use it to get hold of an access token which you use when sending requests to SPiD.

### Access tokens

Access tokens are stored by our SDKs, if you need it you can get it from there. Don't store it yourself.

### Secrets

As mobile apps can easily be decompiled it's hard to protect the integrity of the client secrets. We recommend that you do not store the credentials as a constant or in a settings file but rather try to split it into segments, shift characters by an offset or rotate it. These methods won't stop a determined attacker but is better than just keeping it as plain text.

### Webview vulnerabilities

Don't enable Javascript unless you must as it can create security issues that can be exploited. If you do make sure you do not navigate to other websites other then your own or SPiD's.

### OWASP Mobile

Stay updated and learn the most common exploits for mobile applications and how to prevent them. [OWASP Mobile](https://www.owasp.org/index.php/OWASP_Mobile_Security_Project#tab=Top_10_Mobile_Risks) keep a well updated list with explanations. 
