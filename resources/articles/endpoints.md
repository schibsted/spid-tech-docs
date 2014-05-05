:title Endpoints

:aside

## Table of Contents

- [Response container](#response-container)
- [Response formats](#response-formats)
- [Pagination](#pagination)
- [Filters](#filters)
- [Locales](#locales)
- [Parameters](#parameters)

Looking for the list of all endpoints? See the [API reference](/#api-reference).

:body

This page collects some general workings of endpoints in SPiD.

## Response container

All API responses come with a common container. The container object (when the
response format is JSON) contains some information about the API itself, and a
standard way of retrieving the endpoint return type or information about API
failures.

More details are available from the
[response container type specification](/types/response-container). Below is a
sample output:

```js
{
    "name": "SPP Container",
    "version": "0.2",
    "api": 2,
    "object": "Utility",
    "type": "element",
    "code": 200,
    "request": {
        "reset": 3537,
        "limit": 0,
        "remaining": -3
    },
    "debug": {
        "route": {
            "name": "Utility:describe",
            "url": "/api/2/describe/{object}",
            "controller": "Api\/2\/Utility.describe"
        },
        "params": {
            "options": [],
            "where": { "object":"User" }
        }
    },
    "meta": null,
    "error": null,
    "data": { ... }
}
```


## Response formats

Most SPiD endpoints can respond with various response formats. The format used
is determined by the `format` query parameter supported by all endpoints. Refer
to individual endpoint API docs for information on what formats are supported in
each case.

<h3 id="format-json">JSON</h3>

[JSON](http://json.org/) is the most commonly supported response type used in
SPiD. It is also the default format used by most endpoints. To force the use of
JSON, send the following query parameter:

```text
format=json
```

<h3 id="format-jsonp">JSONP</h3>

[JSONP](http://en.wikipedia.org/wiki/JSONP) is supported by most endpoints that
support JSON. JSONP ("JSON with padding") is JSON wrapped in a function call,
served as JavaScript. Because JavaScript can be served from mixed sources on a
web page, JSONP circumvents certain security restrictions and complexity
associated with other forms of
[cross-origin resource sharing](http://en.wikipedia.org/wiki/Cross-origin_resource_sharing),
at the expense of a potentially higher risk.

To force the use of JSONP, send the following query parameter:

```text
format=jsonp
```

The response will be executable JavaScript:

```js
callback({ ... });
```

The callback function will be called `callback` by default. To change its name,
use the `callback` query parameter:

```sh
curl http://stage.payment.schibsted.no/api/2/users?oauth_token=TOKEN& \
     format=jsonp&\
     callback=doit
```

Which will return something like

```js
doit([...]);
```

<span id="format-xml"></span>
<span id="format-tgz"></span>
<span id="format-csv"></span>
<span id="format-png"></span>

### Other formats

A few SPiD endpoints support other response types:

- **XML**: The extensible markup language with pointy brackets
- **TGZ**: Packaged and compressed files
- **CSV**: Comma separated values
- **PNG**: An image file

To force their use, provide the `format` query parameter. Like this
for XML:

```text
format=xml
```


## Pagination

Pagination is supported in many SPiD endpoints. These are the parameters to control paging:

- `limit`: The maximum number of results to return. May not exceed 1000.
- `offset`: How many results to skip over.
- `until`: Entries older than this unix timestamp are excluded from the results.
- `since`: Entries newer than this unix timestamp are excluded from the results.

You might find that using `since` instead of `offset` helps you avoid
overlaps between pages in a rapidly growing result set.

Both `until` and `since` accept several time formats, most notably
relative formats like `yesterday` and `first day of January`. Read
more about
[supported date and time formats](http://www.php.net/manual/en/datetime.formats.php).

Please note that not all parameters are available for all endpoints.
See the individual endpoint pages for details.


## Filters

Several endpoints accept the `filter` argument. It is a generic way to
get different results - usually filtering the result down to a subset,
but not always.

Each endpoint specifies which filters it supports, but here are some common ones:

- `merchant` Show all results within the current merchant rather than the current client
- `updated` Filter by users that have updated their account, order by last updated (requires usage of since and/or until)
- `verified` Filter by users with status = 1 (verified)
- `unverified` Filter by users with status = 0 (unverified)
- `inactive` Filter by users with status = -1 (inactive/disabled account)
- `blocked` Filter by users with status = -2 (blocked from using the service)
- `deleted` Filter by users with status = -3 (deleted from the platform/service)
- `facebook` Filter by users using Facebook as a third party login
- `google` Filter by users using Google as a third party login
- `live` Filter by users using Live as a third party login
- `imported` Filter by users that have been imported

Multiple filters can be active at once. Separate them by a comma:

```
filter=updated,verified
```

## Locales

SPiD supports localized content for some endpoints. Whenever that is the case,
the available locales are:

- `nb_NO` - For Norwegian Bokmål
- `sv_SE` - For Swedish
- `en_US` - For American English (default)

Although rare, you may run into content that has not been localized. In these
cases, you will get English content back.


## Parameters

Some query parameters support multiple values. Whenever this is the case, you
have a few options for providing these values.

You can send a single value:

```text
key=single-value
```

...you can provide multiple values in a comma-separated list:

```text
key=one-value,other-value
```

...and you can provide multiple values in array-like notation, supported by some
server-side environments (PHP, Rails, others):

```text
key[]=one-value&key[]=other-value
```
