:title Mixpanel analytics
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

[![Sequence diagram: Identifying the visitor](/images/identifying_the_visitor.png)](/images/identifying_the_visitor.png)