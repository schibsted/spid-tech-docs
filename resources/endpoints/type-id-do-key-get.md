:description

The Simple Object Data API, or SODA, is an API extension enabling storage of
simple JSON objects where a unique key can be associated with a client-provided
JSON object.

```text
KEY: 21345678390291876534
VALUE: {"lastpage":"summary","pageviews":54}
```

The key must be a unique string of alphanumeric characters. Subsequent posts
with the same key will overwrite any existing data associated with this key.

Simple data objects can be attached to one of the main base API type user,
product, order, discount or client. You only need to append /do/{key} to each
supported endpoint and do an HTTP `POST` to attach the object and an HTTP `GET`
to retrieve the object. An HTTP `DELETE` will remove the object from storage.
