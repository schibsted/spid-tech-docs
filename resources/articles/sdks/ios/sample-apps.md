:title iOS sample applications

:aside

## More about the iOS SDK

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - iOS sample apps
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

There are four different sample apps all available in the iOS-SDK repository that showcase the different ways to login to SPiD.

The sample apps show how to login using username and password, browser, webview, or via a user's Facebook account. If the user is already authenticated when the app launches the user is sent to the redirect uri. This is especially useful if you use a browser to login as the cookie from another application using SPiD to login can be used by your application, simplifying the login process for the user and increasing the conversion rate in your app.

#### SPiDExampleApp

This app showcases login using browser redirects or a SPiD webview.

#### SPiDNativeApp

The native app logs in to SPiD by username and password using the OAuth password grant type.

#### SPiDHybridApp

The hybrid app logs in using a webview.

#### SPiDFacebookApp

The SPiD Facebook sample app uses Facebook to log in, or can create a SPiD user from a Facebook account if the user does not already have one. To test the Facebook app you need to acquire a Facebook app id. Register as a Facebook developer at https://developers.facebook.com/ then create your app in the Facebook developer console to acquire an app id for your application. Follow the quick start guide to update your project to use your Facebook app id.