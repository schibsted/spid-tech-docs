:title Troubleshooting

:body

This article offers potential solutions for common problems as well as pointers
for some common gotchas.

## Empty objects

In some cases, empty JSON objects (e.g. objects with no properties) returned
from the API will be represented as and empty array. To illustrate, let's say an
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


