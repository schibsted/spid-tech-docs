:title Self Service

:aside

## Native mobile development

- [Overview](/mobile/overview/)
- [Getting started](/mobile/mobile-development/)
- Self Service
- [Register](/mobile/register/)
- [Login](/mobile/login/)
- [Android](/sdks/android/)
    - [Android sample apps](/sdks/android/sample-apps/)
- [iOS](/sdks/ios/)
    - [iOS sample apps](/sdks/ios/sample-apps/)
- [Mobile review checklist](/mobile/reviews/)
- [OAuth for mobile clients](/mobile/oauth-authentication-on-mobile-devices/)
- [Best practices](/mobile/best-practices/)
- [FAQ](/mobile/faq/)

:body

In self service you create and manage the client you use for SPiD. Creating a client is the first thing you need to do before you implement SPiD.

You should already have a backend client before you create your mobile client, but the mobile client has its own credentials and different API access compared to backend clients. If you create both an Android and an iOS app you should have at least 3 clients created in SPiD.

To create a client in self service login to SPiD, go to https://selfservice.identity-pre.schibsted.com, choose your merchant and click "Create a new client"

![Google pic](/images/mobile/create_client.png)

After creating your client the first thing you should do is generate secrets for it. These will only be shown once so be sure to store them somewhere safely and never communicate them to others in your organization in plain text such as in e-mails. If you lose your credentials you can create new, but doing so invalidates the old credentials. This means if you have an app in production and create a new secret for it users will not be able to login before they have updated their app to use your new credentials.

![Google pic](/images/mobile/generate_secrets.png)

![Google pic](/images/mobile/display_secrets.png)