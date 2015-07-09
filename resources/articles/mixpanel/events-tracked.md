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

In addition we will save specific data on each event based on the event type.

A complete overview of the SPiD triggered core events follows below:
<table class="table table-hover">
        <thead>
            <tr>
                <th nowrap="nowrap">Event name</th>
                <th>Triggered when ...</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Accept agreement</td>
                <td>When the user accepts the user agreement with SPiD and with clients.</td>
            </tr>
            <tr>
                <td>Back to client</td>
                <td>When the user clicks the navigation-text in the bottom of a SPiD page that takes the user back to whichever client the user came from.</td>
            </tr>
            <tr>
                <td>Change password notification email sent</td>
                <td>When an email is sent to the user informing of a password change by the user.</td>
            </tr>
            <tr>
                <td>Choose payment method</td>
                <td>When the user proceeds from the choose payment page with a chosen payment method.</td>
            </tr>
            <tr>
                <td>Choose product</td>
                <td>When the user proceeds from the choose product page with a chosen product.
                    <ul>
                        <li>If product is pre-selected by the client as an url-parameter, the event is sent because technically the product selection is done when the user enters SPiD.</li>
                        <li>If a paylink is used, no event is triggered because technically the product selection is done by the client before the user enters SPiD.</li>
					</ul>
                </td>
            </tr>
            <tr>
                <td>Click to view agreement</td>
                <td>When on the SPiD signup page, the user clicks the button to view the entire user agreement with SPiD.</td>
            </tr>
            <tr>
                <td>Confirm cc payment identifier</td>
                <td>When a confirmed credit card is added as payment method to the user.</td>
            </tr>
            <tr>
                <td>Confirm sms payment identifier</td>
                <td>When a confirmed mobile phone is added as payment method to the user.</td>
            </tr>
            <tr>
                <td>Confirm invoice payment identifier</td>
                <td>When a confirmed invoice address is added as payment method to the user.</td>
            </tr>
            <tr>
                <td>Denied purchase, multisale disallowed</td>
                <td>When the user attempts to make a purchase of a product where multisale has been disabled by the client. Triggered on product selection.</td>
            </tr>
            <tr>
                <td>Login</td>
                <td>When a user logs in to the SPiD platform.
					<ul>
                        <li><strong>PS!</strong> Not the same as a login to the client</li>
					</ul>
				</td>
            </tr>
            <tr>
                <td>Login failed</td>
                <td>When a user attempts to log in, but the login fails, e.g. if the wrong credentials were used.</td>
            </tr>
            <tr>
                <td>Logout</td>
                <td>When the user manually logs out from SPiD.</td>
            </tr>
            <tr>
                <td>Migration completed</td>
                <td>When users who have been imported with unverfied emails to the SPiD database verify their email address.
					<ul>
                        <li><strong>PS!</strong> In this case, the event "Signup completed" is not triggered, because the user is not creating a SPiD profile; it already exists from the import.</li>
					</ul>
				</td>
            </tr>
            <tr>
                <td>Migration started</td>
                <td>When users who have been imported with unverfied emails to the SPiD database attempt to log in for the first time. 
					<ul>
                        <li><strong>PS!</strong> In this case, the event "Signup started" is not triggered, because the user is not creating a SPiD profile; it already exists from the
                        import.</li>
					</ul>
				</td>
            </tr>
            <tr>
                <td>Order authorized</td>
                <td>When the order status is set to "Authorized" in a 2-phase payment processes, which happens when SPiD has completed initial order processing and is awaiting feedback before proceeding to order completion and capture processing.
                    <ul>
                        <li>For credit card payments, 2-phase payment is determined by the client. In such cases, SPiD will await order completion processing until notified by client.</li>
                        <li>For sms-payments, 2-phase payment is forced by SPiD, as required handshaking steps with mobile payment service providers must take place before order can be moved to completion processing. Notification from client is not necessary in this case.</li>
                    </ul>
                </td>
            </tr>
            <tr>
                <td>Order complete</td>
                <td>When the order completion is done, the value of the order has been captured and the order status is then "Complete"</td>
            </tr>
            <tr>
                <td>Order credited</td>
                <td>When an order is manually credited by SPiD or the client, or automatically by SPiD if there was a problem with the payment transaction (e.g. over-charges).</td>
            </tr>
            <tr>
                <td>Page viewed</td>
                <td>Whenever the user loads a SPiD page. The event contains a page id that explains which page the user is seeing.</td>
            </tr>
            <tr>
                <td>Prior access to product</td>
                <td>When the user manually chooses a product that the user already has access to.</td>
            </tr>
            <tr>
                <td>Purchase complete</td>
                <td>When a user ha completed an active purchase process, i.e. gone through all the purchase steps and have confirmed the payment details. This means that the order type will always be "Active purchase", and that the order can either go for authorization or completion afterwards.
                    Use this event as a measure of how many users actually complete an active purchase process at a given time, but look to "Order complete" for confirmed completed orders.</td>
            </tr>
            <tr>
                <td>Receipt sent</td>
                <td>When the receipt of a purchase is emailed to the user.</td>
            </tr>
            <tr>
                <td>Signup completed</td>
                <td>When the completes the process of signing up for a new SPiD account after having clicked the email verification link and the status of the email changes to verified. The event will be attributed to whichever client the signup process was initiated.
                    <ul>
                        <li><strong>PS!</strong> This is not the same as signing up to a client.</li>
                        <li><strong>PS2!</strong> For imported users, this event is not triggered. Please see event "Migration completed".</li>
                    </ul>
                </td>
            </tr>
            <tr>
                <td>Signup failed</td>
                <td>When a user attempt to create a new account for credentials (e.g. email) that already exists. The signup process is stopped, and "Signup started" is not triggered.</td>
            </tr>
            <tr>
                <td>Signup started</td>
                <td>When the user starts signing up for a new SPiD account. The event will be attributed to whichever client the signup process was initiated.
                    <ul>
                        <li><strong>PS!</strong> This is not the same as signing up to a new client.</li>
                        <li><strong>PS2!</strong> For imported users, this event is not triggered. Please see event "Migration started".</li>
                    </ul>
                </td>
            </tr>
            <tr>
                <td>Verify email</td>
                <td>When the user completes the verification of an email address by having clicked the email verification link and the status of the email changes to verified.</td>
            </tr>
            <tr>
                <td>Verify email sent</td>
                <td>When the verification email is emailed to the user.</td>
            </tr>
            <tr>
                <td>Verify mobile</td>
                <td>When the user completes the verification of a phone number by having inserted the correct verification code as received by sms, and the status of the phone number changes to verified.</td>
            </tr>
        </tbody>
    </table>
