:title Callbacks

:aside

## Table of Contents

- [Types of callbacks](#types-of-callbacks)
- [Callback requests](#callback-requests)
- [Callback responses](#callback-responses)
- [Order status callback](#order-status-callback)
- [User status callback](#user-status-callback)

## Relevant type definitions

- [Order](/types/order)
- [User](/types/user)

:body

The SPiD platform supports server-side callbacks that enable your service to
subscribe to changes in data. Your service can then cache data and receive
updates, rather than continuously polling SPiD's servers. Caching data and using
this API can improve the reliability of your app or service and decrease its
load times.

Whenever a subscribed change occurs, SPiD makes an HTTP POST request to a
callback URL of your choice, with a list of changes. Your app will generally
receive notification of the change within a couple of minutes of its occurrence.

## Types of Callbacks

The SPiD platform currently supports two kinds of callbacks:

* Order status changes
* User status changes

Further callbacks are planned for future versions of SPiD, such as user and
subscription changes.

## Callback requests

When an object changes, an HTTP POST request will be made to the client-defined
callback URL. The response body will contain Base64 encoded text signed with your
signature secret.

NOTE: The payload in the callback from SPiD made by HTTP POST 'Content-Type: text/plain'.

NOTE: Your signature secret is **not** the same as your client secret (which is
used for authentication). It is a different secret specifically used for signing
requests and decoding signed responses. If you do not have a signing secret,
[contact SPiD](mailto:support@spid.no).

The response body contains an encoded signature and encoded data, separated by a
dot, e.g. `"<signature>.<data>"`. Here is an example:

```
GTUVPjN1LzdyU1qwHjnMKS2oNx.eyJvYmplY3QiOiJvcmRlciIsImVudHJ5IjpbeyJvcmRlcl9pZCI6IjMwMDAxNCIsI
```

The decoded data is a JSON object:

```js
{
  "object": "order",
  "algorithm": "HMAC-SHA256",
  "entry": [
      {
          "orderId": 123,
          "changedFields": "status",
          "time": "2012-10-19 10:10:15"
      },
      {
          "orderId": 456,
          "changedFields": "status",
          "time": "2012-10-19 10:10:19"
      }
   ]
}
```

### :tabs Decoding responses

#### :tab PHP

The following example manually achieves the same effect as the PHP SDK function
`Client::parseSignedRequest($signed)` (see [callback.php](https://github.com/schibsted/sdk-php/blob/4e40c580561fc1d0187dbac0383e8ba0e50de1e3/examples/callback/index.php)
in the [PHP SDK](https://github.com/schibsted/sdk-php) for a full example).

```php
<?php
require_once('src/Client.php');

$SPID_CREDENTIALS = array(
      VGS_Client::CLIENT_ID       => '4cf36fakdk2sj17e030000',
      VGS_Client::CLIENT_SECRET   => 'lsh4nf82f',
      VGS_Client::STAGING_DOMAIN  => 'payment.schibsted.no',
      VGS_Client::HTTPS           => true,
      VGS_Client::REDIRECT_URI    => "http://myapp.example.org",
      VGS_Client::DOMAIN          => 'myapp.example.org',
      VGS_Client::COOKIE          => true,
      VGS_Client::API_VERSION     => 2,
      VGS_Client::PRODUCTION      => true,
      VGS_Client::CLIENT_SIGN_SECRET => 'jsu3f6',
);

$client = new VGS_Client($SPID_CREDENTIALS);
$client->auth();

function parse_signed_request($signed_request, $secret) {
  list($encoded_sig, $payload) = explode('.', $signed_request, 2);
  $sig = base64_url_decode($encoded_sig);
  $data = base64_url_decode($payload);
  $expected_sig = hash_hmac('sha256', $payload, $secret, $raw = true);
  if ($sig !== $expected_sig) {
    return null;
  }
  return $data;
};

function base64_url_decode($input) {
  return base64_decode(strtr($input, '-_', '+/'));
}

$payload = file_get_contents("php://input");
$parsed = parse_signed_request($payload, $SPID_CREDENTIALS[VGS_Client::CLIENT_SIGN_SECRET]);
$data = json_decode($parsed, true);
```

### :/tabs

### Processing callback data

The response only contains a description of what changes occurred - it does not
inline individual resources. Having received, decoded and verified the integrity
of callback data, the client should iterate through all the `entry` objects and
retrieve up-to-date objects through relevant endpoints. These objects can be
safely cached until the next callback request is made.

SPiD aggregates changes and sends batched updates every five minutes. This means
that for every subscription, you will receive at most one request every five
minutes, and possible less often (if there are fewer changes).

## Callback responses

When your client receives callback requests, it should respond with
HTTP code `202 Accepted`. If the callback does not respond with `202`,
SPiD will retry again immediately, and then four more times at
increasing intervals over the subsequent 25 hours. If all of these
requests go un-accepted, the callback request will be marked as
failed.

Retries occur after five minutes, fifteen minutes, one hour, twelve hours and
again twelve hours. There is a thirty second timeout before SPiD quits the
connection and considers the request failed. *Do not synchronously process
callback data before responding to the callback request.*

## Order status callback

This callback will inform the client when an order status changes.

![Order status state machine](/images/order-status.png)

Callbacks **will not** be made under the following conditions:

* The order status does not change (partial captures change the order, but does
  not update the status unless the whole order is captured)
* The client has requested its status through the
  [`/order/{orderId}/status`](/endpoints/GET/order/{orderId}/status) after
  creating the order (thus, there are no further updates to relay). All clients
  should always request the order status as part of the normal payment flow.

## User status callback

This callback will inform the client when a user's status changes.

![User status state machine](/images/user-status.png)
