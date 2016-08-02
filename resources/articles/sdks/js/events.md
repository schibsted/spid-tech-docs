--------------------------------------------------------------------------------
:title Events
:category analytics
:aside

## On this page

- [Available SDK events](#available-sdk-events)
- [Response Object](#response-object)


## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript/)
- [Response signature and validation](/sdks/js/response-signature-and-validation/)
- [API Docs](/sdks/js/api-docs/)
- [Hosting](/sdks/js/hosting/)
- [Best practices](/sdks/js/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

Subscribing to the authentication events fired by the JS SDK means your
application will be notified if the user's session state changes. This is
important because the session state may change due to user interactions beyond
your application's control. The only way your application can be notified of
these changes is through subscribing to these events.

Using `SPiD.event.subscribe("event name", callback)` attaches an event handler
that will be invoked when the event fires. When an event is fired all
subscribers to the event will be notified and given the response object.

[The example repository](https://github.com/schibsted/spid-js-examples/tree/master/getting-started)
contains a fully working example. Open index.html in your browser, and it will
reveal your own login status, and allow you to log in/out. The JavaScript
powering this example looks like the following.

```javascript
SPiD.event.subscribe("SPiD.login", function (data) { console.log("SPiD.login", data); });
SPiD.event.subscribe("SPiD.logout", function (data) { console.log("SPiD.logout", data); });

SPiD.event.subscribe("SPiD.sessionChange", function (data) {
    console.log("SPiD.sessionChange", data);
    var output = document.getElementById("spid");

    if (!data.session) {
        output.innerHTML = "Welcome. Please <a href=\"" + SPiD_Uri.login() + "\">log in</a>";
    } else {
        output.innerHTML = "Welcome <a href=\"" + SPiD_Uri.account() + "\">" +
            data.session.displayName + "</a>" +
            " <a href=\"" + SPiD_Uri.logout() + "\">Log out</a>";
    }
});
var config = {
    client_id: "52f8e3d9efd04bb749000000",
    server: "identity-pre.schibsted.com",
    useSessionCluster: false
 }
SPiD.init(config);
SPiD_Uri.init(config)
```

The `useSessionCluster` property should be set to `true` (or omitted) when running against
the production server.

## Available SDK events:

<table class="table table-hover">
  <thead>
    <tr>
      <th>Name</th>
      <th>Fired when ...</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><code>SPiD.notLoggedin</code></td>
      <td>There is no session on page load, user is not logged in</td>
    </tr>
    <tr>
      <td><code>SPiD.statusChange</code></td>
      <td>Page loads, status changes from unknown to Connected or notConnected</td>
    </tr>
    <tr>
      <td><code>SPiD.login</code></td>
      <td>
        <ul>
          <li>The user logs in somewhere else</li>
          <li>Page loads, user is already logged in</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td><code>SPiD.logout</code></td>
      <td>User logs out (either by you or another site (SPiD, other client).</td>
    </tr>
    <tr>
      <td><code>SPiD.userChange</code></td>
      <td>User in session has changed to another user</td>
    </tr>
    <tr>
      <td><code>SPiD.sessionInit</code></td>
      <td>Session is successfully initiated for the first time, on page load</td>
    </tr>
    <tr>
      <td><code>SPiD.sessionChange</code></td>
      <td>Always. This is a wrapping event, which is fired as a result of all other events that changes the session.</td>
    </tr>
    <tr>
      <td><code>SPiD.visitor</code></td>
      <td>SPiD identifies the current visitor. Yields a unique visitor id that can be used to track the user even when not logged in. Used in analytics (Mixpanel) tracking, etc</td>
    </tr>
    <tr>
      <td><code>SPiD.error</code></td>
      <td>An error occurred. Like communication timeouts, invalid responses or abuse (too many requests in a short time period).</td>
    </tr>
  </tbody>
</table>

Depending on the user's actions, multiple events may be fired.

#### Page loads, user logged in

- `SPiD.login`
- `SPiD.statusChange`
- `SPiD.sessionInit`
- `SPiD.sessionChange`

#### Page loads, user not logged in
- `SPiD.notLoggedin`
- `SPiD.sessionChange`

#### Polling, and there is no change

- `SPiD.sessionChange`

The change in session are the fields `clientTime` and `serverTime` for
synchronization between client and server.

#### User logs out

- `SPiD.logout`
- `SPiD.statusChange`
- `SPiD.sessionChange`

#### User logs out, and logs in with another user account

- `SPiD.logout`
- `SPiD.userChange`
- `SPiD.login`
- `SPiD.sessionChange`

## Response Object

The response object is different, whether user is logged in or not.

Response, when logged in

```js
{
    session: {
        baseDomain: "sdk.dev"
        defaultAgreementAccepted: true
        clientAgreementAccepted: true
        clientTime: 1360329602301
        displayName: "Anna Andersson"
        expiresIn: 7138
        familyName: "Andersson"
        gender: "undisclosed"
        givenName: "Anna"
        id: "50604a7ddcb114ed0e000004"
        photo: "https://secure.gravatar.com/avatar/ec32937c22d1a4b1474657b776d0f398?s=200"
        result: true
        serverTime: 1360329601
        userId: 270177
        userStatus: "connected"
        sig: "9toJgvXfPgk-5W2162sD8ueHzZ8Ya1ibBWvELv-I-lk.eyJyZXN1bHQiOnRydWUsInNlcnZlclRpbWUiOjEzNjAzMjk2MDEsInVzZXJTdGF0dXMiOiJjb25uZWN0ZWQiLCJ1c2VySWQiOjI3MDE3NywiaWQiOiI1MDYwNGE3ZGRjYjExNGVkMGUwMDAwMDQiLCJkaXNwbGF5TmFtZSI6IkpvYWtpbSBXYW5nZ3JlbiIsImdpdmVuTmFtZSI6IkpvYWtpbSIsImZhbWlseU5hbWUiOiJXYW5nZ3JlbiIsImdlbmRlciI6InVuZGlzY2xvc2VkIiwicGhvdG8iOiJodHRwczpcL1wvc2VjdXJlLmdyYXZhdGFyLmNvbVwvYXZhdGFyXC9lYzMyOTM3YzIyZDFhNGIxNDc0NjU3Yjc3NmQwZjM5OD9zPTIwMCIsImV4cGlyZXNJbiI6NzEzOCwiYmFzZURvbWFpbiI6InNkay5kZXYiLCJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiJ9"
    },
    status: "connected"
}
```

Response, when not logged in

```js
{
    session: null,
    status: "unknown"
}
```
