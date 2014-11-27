:title Troubleshooting

:frontpage
:category api-integration
:aside

## On this page

<spid-toc></spid-toc>

:body

This article offers potential solutions for common problems as well as pointers
for some common gotchas.

## Empty objects

In some cases, empty JSON objects (e.g. objects with no properties) returned
from the API will be represented as an empty array. To illustrate, let's say an
endpoint returns this:

```js
{
  "name": "Jane Doe",
  "addresses": {
    "home": { ... },
    "delivery": { ... }
  }
}
```

The `addresses` property uses a common SPiD data structure, the "object of
things". This is really a list of items represented as an object with special
keys (typically one of the properties from the nested object, in the above case
`type`) instead of an array with numeric indices.

If you request a user with no addresses, you may get this response:

```js
{
  "name": "John Doe",
  "addresses": []
}
```

Depending on your language of choice and how you handle JSON, this inconsistency
may need to be worked around.

## Searches with no results

Some endpoints that search data will return with HTTP response code 404 when
there are no data, instead of returning an empty result set with a successful
HTTP response code. Any code performing searches with the API should handle
404s. Refer to each individual endpoint for documentation on possible failure
states.

## Logging in a user with /oauth/token

When attempting to obtain a user token by `POST`-ing to `/oauth/token`, email
addresses using an alias (e.g. the username part has a `+` in it) will result in
a failure. SPiD is generally email address aliases (e.g.
`myaddress+myalias@domain.tld`) aware, and this will be fixed in the future.

## Paylinks in forms

When using [paylinks](/paylink-api/) to sell products, you should **not** use
the paylink as the `action` in an HTML form. The HTML5 spec specifies that the
URL in `action` will not preserve it's query string, thus rendering the paylink
broken. To send the user to SPiD to pay through a paylink, either use a regular
`<a href="...">...</a>` element (style as a button if desired), or simply use a
redirect right after creating the paylink.


## Error code: invalid_client_id

When logging in with a redirect_uri for the first time you may encounter an error message that say **invalid_client_id**.  If your client id is correct, your client is most likely missing a redirect_uri. Login to [Norwegian stage](https://stage.payment.schibsted.no) or [Swedish stage](https://stage.payment.schibsted.se) to access the self-service (if you want change a production client, please remove the "stage" sub-domain).


