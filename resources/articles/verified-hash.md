:title Verified hash
:frontpage
:body

Some endpoints require a `hash` query parameter that contains a hash verifying
the `POST` request body data. The hash is comprised of the query parameters
sorted by name, concatenated (recursively) into one string, hashed (sha256) with
SHthe client signature secret and finally base64 URL encoded. The verification
hash ensures that the data being `POST`-ed has not been tampered with by a
middleman.

The sort algorithm is [natsort](http://sourcefrog.net/projects/natsort/)
("Natural order string comparison").

## :tabs Hashing example

The following are examples of how to hash the `POST` body data so its
authenticity can be validated by SPiD.

### :tab PHP

```php
<?php
function recursiveArrayToString($data) {
    if (!is_array($data)) {
        return "$data";
    }
    $ret = "";
    uksort($data, 'strnatcmp');
    foreach ($data as $v) {
        $ret .= recursiveArrayToString($v);
    }
    return $ret;
}

function base64UrlEncode($input) {
    return rtrim(strtr(base64_encode($input), '+/', '-_'), '=');
}

function createHash($data, $secret) {
    $string = recursiveArrayToString($data);
    return base64UrlEncode(hash_hmac("sha256", $string, $secret, true));
}

$sign_secret = 'foobar';

$data = array(
    'requestReference' => $ref, // unique to reqest
    'clientReference' => $localOrderId, // freely useable by client
    'paymentOptions' => 2,
    'items' => array(
        array('productId' => 100002, 'clientItemReference' => 'first item'),
        array('name' => 'A magazine', 'description' => 'It is really great', 'price' => 2000, 'vat' => 2500),
        array('productId' => 100002, 'name' => 'Banana',  'description' => 'One', 'price' => 1500, 'vat' => 2500, 'quantity' => 1, 'clientItemReference' => 'itemRef4'),
    )
);

$data['hash'] = createHash($data, $sign_secret);
$client->api('/user/123/charge', 'POST', $data);
```

### :tab PHP SDK

```php
<?php
$sign_secret = 'foobar';

$path = "/payment":
$method = "POST";

$data = array(
    'action' => 'sale',
    'productId' => 10001,
    'userId' => 123,
    'price' => 9900
);

$data['hash'] = $client->createHash($data);

try {
    $result = $client->api($path, $method, $data);
} catch (VGS_Client_Exception $e) {
    echo $e->getMessage();
    var_dump($client->container);
}
```

### :tab Java SDK

```java
// no.spid.api.security.SpidSecurityHelper
String signatureSecret = "your secret here";
SpidSecurityHelper securityHelper = new SpidSecurityHelper(signatureSecret);

HashMap<String,String> params = new HashMap<String, String>();
params.put("action", "sale");
params.put("productId", "10001");
params.put("userId", "123");
params.put("price", "9900");

securityHelper.addHash(params);

// Now use params to make a request
```

## :/tabs
