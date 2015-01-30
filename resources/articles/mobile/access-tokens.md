:title Access tokens

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
- Access tokens
- [Reviews](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Migration](/mobile/migration/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

An access token is a string that uniquely identifies a user or client and can be used to make calls to the SPiD API.

When a user connects with an app to SPiD they will obtain an access token which provides secure access to the SPiD SDK. This token is needed for all calls to the SPiD SDK.

There are two kinds of access tokens as described below.

### User token

A user token represents a user, they are generated upon login. 

### Client token

Client tokens represents a client. Mobile clients never have access to client tokens.

### Admin token

Admin tokens are the tokens client admins use.