:title Android sample applications

:aside

## More about the Android SDK

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - Android sample apps
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

**TODO:** Rename apps to more sensible names and make them look better

There are 5 different sample apps all available in the Android-SDK repository that showcase the different ways to login to SPiD.

The sample apps show how to login natively, browser, webview, or via a user's Facebook or Google+ accounts. If the user is already authenticated when the app launches the user is forwarded to the redirect where to. This is especially useful if you use a browser to login as the cookie from another application using SPiD to login can be used by your application, simplifying the login process for the user and increasing the conversion rate in your app.

TODO: Add screenshots

#### SPiDExampleApp

This app showcases login using browser redirects or a SPiD webview.

#### SPiDNativeApp

The native app logs in to SPiD by username and password.

#### SPiDFacebookApp

The SPiD Facebook sample app shows how to login or create a SPiD account using a user's Facebook account. To test the Facebook app you need to acquire a Facebook app id. To do that you need to first [register](https://developers.facebook.com/apps) as a Facebook developer if you are not already one. Once that is done you can acquire a Facebook app id for your application. Set the facebook_app_id in values/strings.xml to your newly acquired app id and you're ready to go.

#### SPiDGooglePlusApp

TODO: Add screenshots from Google+ console

The SPiD Google plus app demonstrates logging in through Google plus, or creating a SPiD account from a Google+ account. If you want to implement Google+ login in your own project you have to [create a Google project](https://console.developers.google.com/project) for it in the Google developers console. 

#### SPiDHybridApp

The hybrid app demonstrates a hybrid app that logs in using a webview.

