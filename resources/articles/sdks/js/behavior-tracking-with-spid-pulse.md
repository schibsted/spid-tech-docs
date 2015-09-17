:title Behavior tracking with SPiD Pulse
:frontpage
:category analytics
:aside


## Read more about behavior tracking

- [Pulse data](/sdks/js/pulse-data/)

## See also

- [JavaScript SDK](/sdks/javascript/)
- [Getting started with the server-side API](/getting-started/)
- [Mixpanel analytics](/mixpanel/analytics/)

:body

Initialize the JavaScript SDK like explained [here](/sdks/javascript/). Remember to use the JavaScript Pulse SDK instead of the normal JavaScript SDK. For example:
```
<script src="https://login.schibsted.com/js/spid-sdk-pulse-1.7.9.min.js"></script>
```
 
 Go to [GitHub](https://github.com/schibsted/sdk-js/) to check what is the latest version. 

#### Example for staging:

```js
VGS.init({
    client_id : "your-spid-client-id",
    server    : "identity-pre.schibsted.com",
    prod      : false,
    logging   : true,
    status    : true,
    track_anon_opt_out : true,
    track_custom_data  : '{ "articleId" : 12345, "section" : "football" }'
});
```

#### Example for production:

```js
VGS.init({
    client_id : "your-spid-client-id",
    server    : "login.schibsted.com",
    prod      : true,
    status    : true,
    track_anon_opt_out : true,
    track_custom_data  : '{ "articleId" : 12345, "section" : "football" }'
});
```

### SPiD Pulse tracking options

##### track_anon_opt_out (boolean)

By default the SPiD pulse part of the sdk will track anonymous users as well as logged in users. If you want to disable tracking of anonymous users set this to true. This setting requires the pulse module.

##### track_custom_data (string)

The SPiD pulse accepts custom data as a stringified JSON object. This data is sent to the pulse server and stored into the database in raw format. Example value: '{ "articleId": 1234, "section": "sport/football" }' Default is null. This setting requires the pulse module.

### Manual event tracking

Use the Event.track function to record your own user events, this is especially usable for one page applications. (Available only from version 1.7.9)

##### name (string)

Unique name for the event, should say something about what the event is tracking.

##### properties (object)

Accepts a JSON object as custom data, works otherwise exactly the same as track_custom_data.

##### Example:

```js
VGS.Event.track('mySuperEvent', {
    section: 'football',
    articleId: 12345
});
```
