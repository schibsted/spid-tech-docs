:title Mixpanel analytics
:category analytics
:frontpage

:aside

## Read more about Mixpanel

- [Managing User-Specific Properties and Traits](/mixpanel/managing-properties-and-traits/)
- [Events tracked by SPiD](/mixpanel/events-tracked/)
- [Mixpanel Page Viewed Event](/mixpanel/page-viewed-event/)
- [Implementing Mixpanel tracking](/mixpanel/implementing-tracking/)

## See also

- [JavaScript SDK](/sdks/javascript/)


:body

SPiD use Mixpanel to analyze and display reports about users events. An event is an action the user do, for example to log in.
We can store extra information about that event by adding properties, for example age and gender.

In order to offer seamless Mixpanel tracking across multiple services and SPiD,
the SPiD user must be identified as soon as possible. To do this, the
[JS SDK](/sdks/javascript/) should be loaded and initialized prior to any event
tracking. If no SPiD user is found (the user is not logged in), the SDK will
return a unique visitor ID that can be used to track events. This visitor ID
**must** be used with every event tracked by the client service through
Mixpanel. As soon as the user logs in (or registers), SPiD will connect the
visitor ID with the actual user. When tracking events on the client's behalf,
SPiD will use the same ID.

### User identification sequence diagram

```sequence-diagram
Browser->Client: Request a page on the client service
Client->Browser: Respond with content
Browser->JS SDK: Initiates JS SDK
JS SDK->SPiD: Checks if current visitor is a SPiD user
SPiD->JS SDK: Returns a unique visitor ID
SPiD->Mixpanel: Track autologin event if user was automatically logged in by the JS SDK
JS SDK->Browser: Trigger the "auth.visitor" event to inform the Client of an identified visitor
Note left of Browser: Option 1:\nClient side tracking
Browser->Mixpanel: Track events based on the unique visitor ID returned by the JS SDK
Note left of Browser: Option 2:\nServer side tracking
Browser->Client: Send the unique visitor ID returned by the JS SDK to the backend
Client->Mixpanel: Track events based on the unique visitor ID returned by SPiD
```