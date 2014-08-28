:title Implementing Mixpanel tracking

:aside

## Read more about Mixpanel

- [Mixpanel Analytics](/mixpanel/analytics/)
- [Events tracked by SPiD](/mixpanel/events-tracked/)
- [Managing User-Specific Properties and Traits](/mixpanel/managing-properties-and-traits/)
- [Mixpanel Page Viewed Event](/mixpanel/page-viewed-event/)

:body

To implement Mixpanel tracking, you need a token, an API key and a secret. These
are provided by SPiD when new clients are created. There are three steps to
enable Mixpanel tracking for your site:

1. Install the Mixpanel library
2. Identify the visitor via the SPiD SDK
3. Track events using the provided SPiD visitor unique id

It is recommended to read through the
[Mixpanel documentation](https://mixpanel.com/docs/) and research which events
to track, and which funnels to implement before pushing events to Mixpanel.

### 1. Installing the Mixpanel library

We recommend using the Mixpanel JavaScript library although there are
[other implementations](https://mixpanel.com/docs/integration-libraries) too.

Paste this snippet just before the `</head>` end tag of your page:

```html
<!-- start Mixpanel --><script>(function(e,b){if(!b.__SV){var a,f,i,g;window.mixpanel=b;a=e.createElement("script");a.type="text/javascript";a.async=!0;a.src=("https:"===e.location.protocol?"https:":"http:")+'//cdn.mxpnl.com/libs/mixpanel-2.2.min.js';f=e.getElementsByTagName("script")[0];f.parentNode.insertBefore(a,f);b._i=[];b.init=function(a,e,d){function f(b,h){var a=h.split(".");2==a.length&&(b=b[a[0]],h=a[1]);b[h]=function(){b.push([h].concat(Array.prototype.slice.call(arguments,0)))}}var c=b;"undefined"!==
typeof d?c=b[d]=[]:d="mixpanel";c.people=c.people||[];c.toString=function(b){var a="mixpanel";"mixpanel"!==d&&(a+="."+d);b||(a+=" (stub)");return a};c.people.toString=function(){return c.toString(1)+".people (stub)"};i="disable track track_pageview track_links track_forms register register_once alias unregister identify name_tag set_config people.set people.set_once people.increment people.append people.track_charge people.clear_charges people.delete_user".split(" ");for(g=0;g<i.length;g++)f(c,i[g]);
b._i.push([a,e,d])};b.__SV=1.2}})(document,window.mixpanel||[]);
mixpanel.init("{YOUR MIXPANEL TOKEN}");</script><!-- end Mixpanel -->
```

Add the SPiD SDK in the same manner (example uses the Norwegian staging
environment):

```html
<script src="https://stage.payment.schibsted.no/js/spid-sdk-1.7.2.js"></script>
```

[SPiD JavaScript SDK details](/sdks/javascript/).

### 2. Identify the visitor via the SPiD SDK

When the page has loaded, subscribe to the "auth.visitor" event. This event is
triggered when SPiD identifies the current visitor and the SDK is initialized.
When the user is identified, you can identify the user with Mixpanel by calling
`mixpanel.identify(SPID_VISITOR_ID)`, and optionally add custom traits to the
current visitor by calling the SPiD JS SDK function
`VGS.setTraits(SIGNED_JSON_OBJECT)`.

Place the following code in a `script` block right before the closing `</body>`
tag, or in a
[DOMContentLoaded event handler](https://developer.mozilla.org/en-US/docs/Web/Reference/Events/DOMContentLoaded)
(e.g. `$(document).ready(function () { /* Here */ })` if using jQuery).

```js
VGS.Event.subscribe('auth.visitor', function(data) {
    mixpanel.identify(data.uid);
    var signedData = 'UKpcW_4cVs2V2X1jnK-SrwVOWWphFKExv85sLfgA6jw.eyJ0ZXN0IjoiOSIsImFsZ29yaXRobSI6IkhNQUMtU0hBMjU2In0';
    VGS.setTraits(signedData, function(data) {});
});

VGS.init({
    client_id: '{YOUR STAGE CLIENT ID}',
    server: 'stage.payment.schibsted.no',
    prod: false,
    logging: true,
    status:true
});
```

### 3. Track events using the provided SPiD unique visitor id

```js
// your own tracking
mixpanel.track('Viewed campaign page', {'campaign': 'Sommerkampanje'});
```