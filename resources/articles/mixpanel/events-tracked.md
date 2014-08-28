:title Events tracked by SPiD

:aside

## Read more about Mixpanel

- [Mixpanel Analytics](/mixpanel/analytics/)
- [Managing User-Specific Properties and Traits](/mixpanel/managing-properties-and-traits/)
- [Mixpanel Page Viewed Event](/mixpanel/page-viewed-event/)
- [Implementing Mixpanel tracking](/mixpanel/implementing-tracking/)

:body

SPiD will automatically add some profile data to events. These properties should
not be added as traits by the client, as they will be ignored (in favor of the
user's actual profile data). This keeps profile data in events from going stale.

Properties added by SPiD to all events:

- Version (Platform version)
- Revision (Platform revision)
- SPiD ID (The SPiD user id)
- Client (Client alias/name, not client id)
- ip (User's ip in order for Mixpanel to derive country, city, etc)
- User agent (Not used by Mixpanel on server side requests, but we send it anyway)
- Age
- Gender
- User status
- Registered (When the user registered)
- Tracking ref ([Client provided visitor reference](/tracking-parameters/))
- Tracking tag ([Client provided tag parameter](/tracking-parameters/), used for custom order tracking)
- Flow (payment or signup)

In addition we will save specific data on each event based on the event type. A
complete overview follows below:

- Accept agreement
- Verify email
- Login
- Logout
- Migration completed
- Migration started
- Verify phone
- Signup completed
- Signup started
- Verify email sent
- Choose payment method
- Choose product
- Order complete
- Order authorized
- Confirm * payment identifier
- Denied purchase, multisale disallowed
- Prior access to product
- Receipt sent
- Password changed
- Change password notification email sent
- Secondary email added
- Secondary email added notification email sent
- Device fingerprint tracked on user
- Back to client
- Page viewed
- Click to view agreement
- Access check