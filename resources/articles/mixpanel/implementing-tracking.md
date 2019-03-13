:title Implementing Mixpanel tracking

:aside

## Read more about Mixpanel

- [Mixpanel Analytics](/mixpanel/analytics/)
- [Events tracked by SchAcc](/mixpanel/events-tracked/)
- [Mixpanel Page Viewed Event](/mixpanel/page-viewed-event/)

:body

To implement Mixpanel tracking, you need a token, an API key and a secret. These
are provided by SchAcc when new clients are created. There are four steps to
enable Mixpanel tracking for your site:

1. Install the Mixpanel library
2. Include the JavaScript SDK
3. Identify the visitor via the SchAcc SDK
4. Track events using the provided SchAcc visitor unique id

It is recommended to read through the
[Mixpanel documentation](https://mixpanel.com/docs/) and research which events
to track, and which funnels to implement before pushing events to Mixpanel.

### 1. Installing the Mixpanel library

We recommend using the [Mixpanel JavaScript library](https://mixpanel.com/help/reference/javascript), although there are
[other implementations](https://mixpanel.com/docs/integration-libraries) too.

### 2. Include the JavaScript SDK

Add the JavaScript SDK just before the `</head>` end tag of your page, for example (remember to use the latest version):

```html
<script src="https://identity-pre.schibsted.com/js/spid-sdk-1.7.9.js"></script>
```

Read more [here](/sdks/javascript/).

### 3. Identify the visitor via the SchAcc SDK

When the page has loaded, subscribe to the "auth.visitor" event. This event is
triggered when SchAcc identifies the current visitor and the SDK is initialized.
When the user is identified, you can identify the user with Mixpanel by calling
`mixpanel.identify(SPID_VISITOR_ID)`.

Place the following code in a `script` block right before the closing `</body>`
tag, or in a
[DOMContentLoaded event handler](https://developer.mozilla.org/en-US/docs/Web/Reference/Events/DOMContentLoaded)
(e.g. `$(document).ready(function () { /* Here */ })` if using jQuery).


```js
VGS.Event.subscribe('auth.visitor', function(data) {
    mixpanel.identify(data.uid);
    var signedData = '{YOUR SIGNED JSON OBJECT}';
});

VGS.init({
    client_id: '{YOUR STAGE/PRE CLIENT ID}',
    server: 'identity-pre.schibsted.com',
    prod: false,
    logging: true,
    status:true
});
```

### 4. Track events using the provided SchAcc unique visitor id

```js
// your own tracking
mixpanel.track('Viewed campaign page', {'campaign': 'Sommerkampanje'});
```
