:title Mixpanel analytics
:category analytics
:frontpage

:aside

## Read more about Mixpanel

- [Events tracked by SchAcc](/mixpanel/events-tracked/)
- [Mixpanel Page Viewed Event](/mixpanel/page-viewed-event/)
- [Implementing Mixpanel tracking](/mixpanel/implementing-tracking/)

## See also

- [JavaScript SDK](/sdks/javascript/)


:body

SchAcc use Mixpanel to analyze and display reports about users events. An event is an action the user do, for example to log in.
We can store extra information about that event by adding properties, for example age and gender.

In order to offer seamless Mixpanel tracking across multiple services and SchAcc,
the SchAcc user must be identified as soon as possible. To do this, the
[JS SDK](/sdks/javascript/) should be loaded and initialized prior to any event
tracking. If no SchAcc user is found (the user is not logged in), the SDK will
return a unique visitor ID that can be used to track events. This visitor ID
**must** be used with every event tracked by the client service through
Mixpanel. As soon as the user logs in (or registers), SchAcc will connect the
visitor ID with the actual user. When tracking events on the client's behalf,
SchAcc will use the same ID.

### User identification sequence diagram

```sequence-diagram
Browser->Client: Request a page on the client service
Client->Browser: Respond with content
Browser->JS SDK: Initiates JS SDK
JS SDK->SchAcc: Checks if current visitor is a SchAcc user
SchAcc->JS SDK: Returns a unique visitor ID
SchAcc->Mixpanel: Track autologin event if user was automatically logged in by the JS SDK
JS SDK->Browser: Trigger the "auth.visitor" event to inform the Client of an identified visitor
Note left of Browser: Option 1:\nClient side tracking
Browser->Mixpanel: Track events based on the unique visitor ID returned by the JS SDK
Note left of Browser: Option 2:\nServer side tracking
Browser->Client: Send the unique visitor ID returned by the JS SDK to the backend
Client->Mixpanel: Track events based on the unique visitor ID returned by SchAcc
```