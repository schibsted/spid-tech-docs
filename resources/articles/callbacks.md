# Callbacks

The SPiD platform supports server-side callbacks to enable your service to
subscribe to changes in data. Your service can then cache data and receive
updates, rather than continuously polling SPiD's servers. Caching data and using
this API can improve the reliability of your app or service and decrease its
load times.

Whenever a subscribed change occurs, SPiD makes an HTTP POST request to a
callback URL you specify, with a list of changes. Your app will generally
receive notification of the change within a couple of minutes of its occurrence.

## Types of Callbacks

It is possible to subscribe to the following types of object updates that will
generate callbacks:

Orders – Get notifications relating to Order status changes
Future version of SPiD will support other types of callbacks:

User – Get notifications about certain user fields
Subscriptions – Get notifications relating to subscriptions
etc

## Callback requests

When an object changes, a callback request notifying clients subscribing to
those changes will be done as soon as possible by a HTTP POST to the client
defined callback URL. Note that the actual post is made in the form of a POST
TEXT that is Base64 URL encoded and signed with a signature secret.

NOTE: The payload in the callback from SPiD made by HTTP POST 'Content-Type:
text/plain'.

NOTE: Signature secret is NOT the client secret used on oAuth2.0 authentication
requests on our API's. It is a different secret used for signing requests and
responses.

Example of signed request:

GTUVPjN1LzdyU1qwHjnMKS2oNx.eyJvYmplY3QiOiJvcmRlciIsImVudHJ5IjpbeyJvcmRlcl9pZCI6IjMwMDAxNCIsI
The PHP SDK includes a way of decoding and verifying this signed request, but for illustration, here is a code snippet from a callback implementation example (see callback.php in the PHP SDK for a full example) :

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
  // decode the data
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
//$data will now be an array with the keys "object" and "entry"
At this point a callback implementation should go through all entries and retrieve a fresh object by their corresponding endpoints. The above implementation does the same as the Client::parseSignedRequest($signed) function in the PHP SDK.

Example of a parsed signed request
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
Note that the callback request does not include the actual data values (either from before or after the update). To obtain those, you must request them as normal, subject to the usual privacy and security restrictions. You may wish to query for that data immediately following this callback so that it is ready for when the user returns to your app or service.

SPiD aggregates changes and sends batched updates every 5 minutes. Your endpoint should be set up to handle this level of load, although of course, actual traffic depends on the callbacks your client subscribes to.

Callback Response
If the callback DOES NOT respond with HTTP CODE 202 ACCEPTED, SPiD will retry again immediately, and then 4 times more, with decreasing frequency, over the subsequent 25 hours, until we will set the callback request as failed. These come first after 5 minutes, then fifteen minutes, then one hour and the last two waits 12 hours each. There is a 30 second timeout before SPiD gives up on the request.


Order Status callback

This callback will inform the client when an order status changes. See order status diagram for when callbacks are made.

Callbacks WILL NOT be made under the following conditions:
The order status does not change (partial capture will change the order, but not status unless the whole order have been captured)
The client has requested it's status on /order/{orderId}/status between an order being changed and the callback attempt would have been made, which all clients with callbacks should always do as part of a payment flow.

User Status callback

This callback will inform the client when a user get his status changed. See user status diagram for when callbacks are made.
