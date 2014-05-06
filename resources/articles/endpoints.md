:title Endpoints

:aside

## Table of Contents

- [Response container](#response-container)
- [Response formats](#response-formats)
- [Encoding](#encoding)
- [Pagination](#pagination)
- [Selection](#selection)
- [Sorting](#sorting)
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
        "limit": 3600,
        "remaining": 3
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

Many SPiD endpoints can respond with various response formats. The format used
is determined by the URL suffix/file name extension used. Refer to individual
endpoint API docs for information on supported formats.

<h3 id="format-json">JSON</h3>

[JSON](http://json.org/) is the most commonly supported response type used in
SPiD. It is also the default format used by most endpoints. To force the use of
JSON, use the `.json` suffix on URLs:

```text
http://staging.payment.schibsted.no/api/2/user/42.json
```

<h3 id="format-jsonp">JSONP</h3>

[JSONP](http://en.wikipedia.org/wiki/JSONP) is supported by most endpoints that
support JSON. JSONP ("JSON with padding") is JSON wrapped in a function call,
served as JavaScript. Because JavaScript can be served from mixed sources on a
web page, JSONP circumvents certain security restrictions and complexity
associated with other forms of
[cross-origin resource sharing](http://en.wikipedia.org/wiki/Cross-origin_resource_sharing),
at the expense of higher security risk.

To force the use of JSONP, use the `.jsonp` suffix on URLs:

```text
http://staging.payment.schibsted.no/api/2/user/42.jsonp
```

The response will be executable JavaScript:

```js
callback({ ... });
```

The callback function will be called `callback` by default. To change its name,
use the `callback` query parameter:

```sh
curl http://stage.payment.schibsted.no/api/2/users.jsonp?oauth_token=TOKEN&\
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

To force their use, provide the lower case abbreviation as the URL suffix, e.g.:

```text
http://staging.payment.schibsted.no/api/2/user/42.xml
```

## Encoding

**The SPiD API expects all data to be UTF-8 encoded.**

All incoming data is actively checked for valid UTF-8 sequences. If an invalid
sequence is found, the data is presumed to be ISO-8859-1 and converted
accordingly to UTF-8. Sending data in any other encoding will result in garbage
in SPiD. It won’t be dangerous garbage (we will always store valid UTF-8) but it
will still be garbage. [More information about UTF-8](http://www.utf-8.com/).

## Pagination

Pagination is supported in many SPiD endpoints. There are four parameters that
control paging:

- `limit`: The maximum number of results to return. May not exceed 1000.
- `offset`: How many results to skip over.
- `until`: Entries older than this are excluded from the results.
- `since`: Entries newer than this are excluded from the results.

There is a hard limit of 1000 results. Larger result sets will be truncated.

In rapidly growing result sets, using `since` instead of `offset` is recommended
to avoid overlaps between pages.

Both `until` and `since` accept several time formats; timestamps, dates and even
fuzzy formats like `yesterday` and `first day of January`. Read more about
[supported date and time formats](http://www.php.net/manual/en/datetime.formats.php).

Not all parameters are available for all endpoints. See individual endpoint
API docs for details.

## Selection

By default, responses will contain full object descriptions, with most or all
object properties. If you only need certain properties, you can trim the result
by using the `fields` query parameter. As an example, this is how you would
request a certain user with only the `firstName`, `displayName` and `photo`
properties included in the response:

```text
GET https://payment.schibsted.no/api/2/user/daniel.bentes@vg.no?fields=firstName,displayName,photo
```

## Sorting

For endpoints that return collections, you can sort the results using the `sort`
request parameter in a map-like manner. You may specify multiple sorting
properties, and for each one whether to sort ascending (`asc`) or descending
(`desc`).

Example:

```text
GET https://payment.schibsted.no/api/2/orders?sort[status]=desc&sort[updated]=asc
```

Endpoints that support sorting:

* [/orders](/endpoints/GET/orders)
* [/subscriptions](/endpoints/GET/subscriptions)
* [/digitalcontents](/endpoints/GET/digitalcontents)
* [/campaigns](/endpoints/GET/campaigns)

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
