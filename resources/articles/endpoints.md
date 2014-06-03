:title Endpoints

:frontpage

:aside

## Table of Contents

- [Response container](#response-container)
- [Response formats](#response-formats)
- [Signed responses](#signed-responses)
- [Encoding](#encoding)
- [Pagination](#pagination)
- [Selection](#selection)
- [Sorting](#sorting)
- [Filters](#filters)
- [Locales](#locales)
- [Parameters](#parameters)
- [Java request example](#java-request-example)
- [PHP request example](#php-request-example)

Looking for the list of all endpoints? See the [API reference](/#api-reference).

:body

This page collects some general workings of endpoints in SPiD.

## Response container

All API responses come with a common container. The container object (when the
response format is JSON) contains some information about the API itself, and a
standard way of retrieving the endpoint return type or information about API
failures.

More details are available from the
[response container type specification](/types/response-container/). Below is a
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

## Signed responses

Some API endpoints return encoded and signed data. This can be enabled upon
request, and is enabled by default on the payment API endpoints.

There are 2 fields in the API container that indicate a signed response. The
`sig` parameter is a signature string, while `algorithm` is the algorithm used
to encrypt the signature string.

The signature ensures that the data you are receiving is actually sent by SPiD.
It is signed using your Client signature secret which is only known to you and
SPiD. Without this secret, third parties cannot modify the `sig` parameter
without also invalidating the data in the provided response.

The response data must be Base64 decoded to JSON before it can be used.

### :tabs Decoding responses

#### :tab PHP

```php
<?php
$data = json_decode(base64UrlDecode($container['data']), true);
$sig = base64UrlDecode($encoded_sig);
```

To verify the data content, recreate the payload with the signature using the
specified algorithm and your client signature secret. SHA256 is the default
hashing algorithm.

```php
<?php
$expected_sig = hash_hmac('sha256', $container['data'], $client_signature_secret, true);
$verified = ($sig === $expected_sig);
```

### :/tabs

## Encoding

**The SPiD API expects all data to be [UTF-8](http://www.utf-8.com/) encoded.**

All incoming data is actively checked for valid UTF-8 sequences. For
historical reasons, if an invalid sequence is found, the data is
presumed to be ISO-8859-1 and converted accordingly to UTF-8. You
should not rely on this feature, but instead always use UTF-8.

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
GET /user/johndoe@example.com?fields=firstName,displayName,photo
```

## Sorting

For endpoints that return collections, you can sort the results using the `sort`
request parameter in a map-like manner. You may specify multiple sorting
properties, and for each one whether to sort ascending (`asc`) or descending
(`desc`).

Example:

```text
GET http://payment.schibsted.no/api/2/orders?sort[status]=desc&sort[updated]=asc
```

## Filters

Many endpoints accept the `filter` request parameter. It can be used to provide
one or more filters, which will affect how the endpoint behaves. The two main
types of filters are:

1. **Status filters** - these filters (such as `active`, `verified`, `deleted`
   etc) allow you to control the status of returned items. If you specify
   multiple status filters, they will be `OR`-ed, e.g. `filters=active,deleted`
   will return all items that are active and all items that are deleted.

2. **Type filters** - these filters (such as `bundle`, `subscription` etc) allow
   you to control what kind of items are returned. Multiple type filters are
   `OR`-ed, e.g. `filters=bundle,subscription` will return every product that is
   either a bundle or a subscription.

Combined status and type filters are `AND`-ed together:
`filters=active,bundle,subscription` will return all active products that are
either bundles or subscriptions.

See individual endpoints for supported filters.

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

## Java request example

Java examples throughout the endpoint API docs are excerpts, to spare you from
wading through repeated boilerplate. To put it all in context, the following is
a full example, including import statements.

<spid-example lang="java" src="/getting-started/src/main/java/no/spid/examples/GettingStarted.java" title="Getting started"/>

## PHP request example

PHP examples throughout the endpoint API docs are excerpts, to spare you from
wading through repeated boilerplate. To put it all in context, the following is
a full example.

<spid-example lang="php" src="/getting-started/getting-started.php" title="Getting started"/>
