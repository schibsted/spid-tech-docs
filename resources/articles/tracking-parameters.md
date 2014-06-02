:title Tracking and tagging parameters
:body

SPiD URLs accept some query parameters that can be used to track and or tag
login sessions, users or orders. Some may also be used in third party analytics
systems as well. You can add these query parameters to URLs when linking the
user to SPiD to log in/out, start a payment process, etc.

#### tracking_ref

A client-provided unique visitor reference. Used for tracking in third party
analytics like Adobe Sitecatalyst. This is also saved to SPiD logins and events
tables.

#### tag

Custom client string used for custom order tracking. Available in order and
events tables.

#### force

Disables autologin and "remember me" functionality, forcing the user to log in
with their email and password. Only works for the login form.

#### webview

Set to `1` to enable some webview-friendly changes to the login page's CSS. In
webview-mode, the login form has more padding, and the global SPiD menu is
hidden. Useful when allowing users to login through a webview in native apps.
(These changes are applied responsively to small screens only).

#### xiti

<a href="http://www.xiti.com/en/">Xiti Analytics</a> tracking string. Used by
SPiD to populate custom client-provided Xiti parameters for analytics in SPiD.
This parameter is encoded differently than the other parameters. It uses a
special encoding technique which is supported by default in the PHP sdk:

```php
<?php
public function encodeSerializedUrlVariable($var) {
   return strtr(base64_encode(addslashes(gzcompress(serialize($var),9))), '+/=', '-_,');
}    

// Usage Example
$xiti = encodeSerializedUrlVariable(array(
    'xitiLevel2' => '9',
    'xitiSite' => '5221071',
    'collectorServer' => 'logc119',
));
```

**Required parameters**

- xitiLevel2
- xitiSite
- collectorServer

**Other supported parameters**

- userCategory
- metadata
- queryString
