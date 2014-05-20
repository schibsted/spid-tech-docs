:title Getting started with the JavaScript client

:aside

## See also

- [Getting started with the server-side API](/getting-started/)

:body

The JavaScript SDK is different from the other SDKs in that it only provides
information about the user's authentication/authoriation status. It is not a
general purpose SDK to make arbitrary requests against the API. A Node SDK
is being developed to fill this role for server-side JavaScript.

Download the JavaScript SDK by downloading the
[latest version from GitHub](https://github.com/schibsted/sdk-js/tree/master/dist).
Note that both the unminified and minified versions bundle the JSON2.js script.
If you do not need it, download the source code and build it yourself. Refer to
the [JS SDK docs](https://github.com/schibsted/sdk-js) for how to do this.

The user's logged in status may change while on your site due to inactivity,
logging out elsewhere or other reasons. For this reason, it is recommended
to subscribe to changes in the user's status.

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
    server: "stage.payment.schibsted.no",
    prod: false
});
```

The `prod` property should be set to `true` (or omitted) when running against
the production server.

Take a look at
[this example](https://github.com/schibsted/spid-js-examples/tree/master/getting-started).
Open index.html in your browser, and it will reveal your own login status.
