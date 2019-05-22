--------------------------------------------------------------------------------
:title Events
:category analytics
:aside

## On this page

- [Available SDK events](#available-sdk-events)
- [Response Object](#response-object)

## Read more about the JavaScript SDK

- [JavaScript SDK](/sdks/javascript-1x/)
- [Response signature and validation](/sdks/js-1x/response-signature-and-validation/)
- [API Docs](/sdks/js-1x/api-docs/)
- [Hosting](/sdks/js-1x/hosting/)
- [Best practices](/sdks/js-1x/best-practices/)

## See also

- [Getting started with the server-side API](/getting-started/)

:body

**Note: this documentation is for the 1.x versions of the JavaScript SDK. The current version can be found
[here](/sdks/javascript/).**

Subscribing to the authentication events fired by the JS SDK means your
application will be notified if the user's session state changes. This is
important because the session state may change due to user interactions beyond
your application's control. The only way your application can be notified of
these changes is through subscribing to these events.

Using `VGS.Event.subscribe("event name", callback)` attaches an event handler
that will be invoked when the event fires. When an event is fired all
subscribers to the event will be notified and given the response object.

[The example repository](https://github.com/schibsted/spid-js-examples/tree/master/getting-started)
contains a fully working example. Open index.html in your browser, and it will
reveal your own login status, and allow you to log in/out. The JavaScript
powering this example looks like the following.

```javascript
VGS.Event.subscribe("auth.login", function (data) { console.log("auth.login", data); });
VGS.Event.subscribe("auth.logout", function (data) { console.log("auth.logout", data); });

VGS.Event.subscribe("auth.sessionChange", function (data) {
    console.log("auth.sessionChange", data);
    var output = document.getElementById("spid");

    if (!data.session) {
        output.innerHTML = "Welcome. Please <a href=\"" + VGS.getLoginURI() + "\">log in</a>";
    } else {
        output.innerHTML = "Welcome <a href=\"" + VGS.getAccountURI() + "\">" +
            data.session.displayName + "</a>" +
            " <a href=\"" + VGS.getLogoutURI() + "\">Log out</a>";
    }
});

VGS.init({
    client_id: "52f8e3d9efd04bb749000000",
    server: "identity-pre.schibsted.com",
    prod: false
});
```

The `prod` property should be set to `true` (or omitted) when running against
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
      <td><code>auth.notLoggedin</code></td>
      <td>There is no session on page load, user is not logged in</td>
    </tr>
    <tr>
      <td><code>auth.statusChange</code></td>
      <td>Page loads, status changes from unknown to Connected or notConnected</td>
    </tr>
    <tr>
      <td><code>auth.login</code></td>
      <td>
        <ul>
          <li>The user logs in somewhere else</li>
          <li>Page loads, user is already logged in</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td><code>auth.logout</code></td>
      <td>User logs out (either by you or another site).</td>
    </tr>
    <tr>
      <td><code>auth.userChange</code></td>
      <td>User in session has changed to another user</td>
    </tr>
    <tr>
      <td><code>auth.sessionInit</code></td>
      <td>Session is successfully initiated for the first time, on page load</td>
    </tr>
    <tr>
      <td><code>auth.sessionChange</code></td>
      <td>Always. This is a wrapping event, which is fired as a result of all other events that changes the session.</td>
    </tr>
    <tr>
      <td><code>auth.visitor</code></td>
      <td>SchAcc identifies the current visitor. Yields a unique visitor id that can be used to track the user even when not logged in. Used in analytics tracking, etc.</td>
    </tr>
    <tr>
      <td><code>VGS.error</code></td>
      <td>An error occurred. Like communication timeouts, invalid responses or abuse (too many requests in a short time period).</td>
    </tr>
  </tbody>
</table>

Depending on the user's actions, multiple events may be fired.

#### Page loads, user logged in

- `auth.login`
- `auth.statusChange`
- `auth.sessionInit`
- `auth.sessionChange`

#### Page loads, user not logged in
- `auth.notLoggedin`
- `auth.sessionChange`

#### Polling, and there is no change

- `auth.sessionChange`

The change in session are the fields `clientTime` and `serverTime` for
synchronization between client and server.

#### User logs out

- `auth.logout`
- `auth.statusChange`
- `auth.sessionChange`

#### User logs out, and logs in with another user account

- `auth.logout`
- `auth.userChange`
- `auth.login`
- `auth.sessionChange`

## Response Object

The response object is different, whether user is logged in or not.

Response, when logged in

```js
{
    session: {
        baseDomain: "sdk.dev"
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
        agreementAccepted: true
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
