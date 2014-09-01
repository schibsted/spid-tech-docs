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

Include the javascript pulse sdk from SPiD. Copy the file to your local resource folder and 
include it into the document header.
 
```html
<script id="spid-jssdk" src="https://www.my-domain.no/js/spid-sdk-pulse-1.7.6.min.js"></script>
```

Initialize the spid-js-sdk in the regular way. Remember to add the options for pulse tracking.  
Read more about this in our [tech docs](http://techdocs.spid.no/sdks/javascript/)

__Example for staging:__ 
```js
VGS.init({
    client_id : "your-spid-client-id",
    server    : "stage.payment.schibsted.(no|se)",
    prod      : false,
    logging   : true,
    status    : true,
    track_anon_opt_out : true,
    track_custom_data  : '{ "articleId" : 12345, "section" : "football" }'
});
```

__Example for production:__
```js
VGS.init({
    client_id : "your-spid-client-id",
    server    : "payemnt.schibsted.(no|se)",
    prod      : true,
    status    : true,
    track_anon_opt_out : true,
    track_custom_data  : '{ "articleId" : 12345, "section" : "football" }'
});
```

#### SPiD pulse tracking options

##### track_anon_opt_out

By default the SPiD pulse part of the sdk will track anonymous users as well as logged in users. If you want to disable tracking of anonymous users set this to true. This setting requires the pulse module.

##### track_custom_data

The SPiD pulse accepts custom data as a stringified JSON object. This data is sent to the pulse server and stored into the database in raw format. Example value: '{ "articleId": 1234, "section": "sport/football" }' Default is null. This setting requires the pulse module.


### HOWTO setup developer environment
```bash
    # To watch files while developing.
    $ grunt check

    # To run the server in develop mode and automatically reload when files changes.
    $ grunt run-local
```