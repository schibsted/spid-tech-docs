:title Verified hash
:frontpage
:category api-integration
:body

Some endpoints require a `hash` query parameter that contains a hash verifying
the `POST` request body data. The hash is comprised of the query parameters
sorted by name, concatenated (recursively) into one string, hashed (sha256) with
SHthe client signature secret and finally base64 URL encoded. The verification
hash ensures that the data being `POST`-ed has not been tampered with by a
middleman.

**Note: After the string is base64 encoded you should replace `+` with `-` and `/` with `_`. Also you should remove trailing `=`.**

The sort algorithm is [natsort](http://sourcefrog.net/projects/natsort/)
("Natural order string comparison").

```json
{
    "a" : "zebra",
    "x" : "banana",
    "c" : {
        "b" : "orange",
        "c" : "monkey",
        "a" : "sun"
    },
    "b" : "tree"
}    
```

Should give the following string after concat:
`zebratreesunorangemonkeybanana`

## Hashing example

# :tabs

The following are examples of how to hash the `POST` body data so its
authenticity can be validated by Schibsted account.

## :tab PHP

```php
<?php
function recursiveArrayToString($data)
{
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

function base64UrlEncode($input)
{
    return rtrim(strtr(base64_encode($input), '+/', '-_'), '=');
}

function createHash($data, $secret)
{
    $string = recursiveArrayToString($data);
    return base64UrlEncode(hash_hmac("sha256", $string, $secret, true));
}

$sign_secret = 'foobar';

$data = [
    'requestReference' => $ref, // unique to reqest
    'clientReference' => $localOrderId, // freely useable by client
    'paymentOptions' => 2,
    'items' => [
        ['productId' => 100002, 'clientItemReference' => 'first item'],
        ['name' => 'A magazine', 'description' => 'It is really great', 'price' => 2000, 'vat' => 2500],
        ['productId' => 100002, 'name' => 'Banana',  'description' => 'One', 'price' => 1500, 'vat' => 2500, 'quantity' => 1, 'clientItemReference' => 'itemRef4'],
    ]
];

$data['hash'] = createHash($data, $sign_secret);
$client->api('/user/123/charge', 'POST', $data);
```

# :/tabs
