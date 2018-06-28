--------------------------------------------------------------------------------
:title iOS SDK
:frontpage
:category
:aside

## Native web development

- [Overview](/mobile/overview/)
- [Self Service](/mobile/selfservice/)
- [Android](/sdks/android/)
- iOS

:body

The SchibstedID iOS SDK provides you access to SPT identity user objects and gives you the tools to manage your users and make authenticated requests

To learn more about the SDK, the full documentation can be found in the GitHub repo at [schibsted.github.io/account-sdk-ios](https://schibsted.github.io/account-sdk-ios/).

For support, please contact [schibstedaccount@schibsted.com](mailto:schibstedaccount@schibsted.com)

## Setup

## Reference the pod

You will need to add the following sources to your podfile to include the pod `SchibstedID`
- `source "git@github.schibsted.io:CocoaPods/Specs.git,"`: for SchibstedID

The SDK is divided in to different subspecs:

- `pod 'SchibstedID'`: this is the default
- `pod 'SchibstedID/UI'`: will add the UI component

### You must include a tracking subspec as well

The UI does some internal tracking, and requires a `TrackingEventsHandler` be set in the UI's configuration.
To fulfill this, you must either implement it yourself or use one which is already implemented.

To use the SchibstedIDTracking implementation, you may include the following in your podfile

- `pod 'SchibstedID/Tracking/Pulse'`: Adds dependency to the [new](https://github.schibsted.io/spt-dataanalytics/pulse-tracker-ios) pulse SDK

### Get some client credentials

The SDK works by giving you access to Schibsted users. And what kind of access is allowed is determined by a set
of client credentials. The first thing you must do is get some credentials, which can be done through
[self service](http://techdocs.spid.no/selfservice/access/). Learn about environments and merchants. The SDK will not work accross environments or merchants.


## Building the example application(s)

In CocoaPods-based projects you work within the project of the example application, which is inside "Example" folder.
At first the workspace needs to be created by running `pod install` in the terminal, this has been wrapped in a script:

    ./pod_install.sh
    open Example/SchibstedID.xcworkspace

The last command will open "SchibstedID.xcworkspace" file in Xcode.
Run the application using &#8984;R (Product - Run).
Make sure that the "SchibstedID-Example" scheme is selected.

There's an example UI application as well that you can open at:

    open ExampleUI/ExampleUI.xcworkspace