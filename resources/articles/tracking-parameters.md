:title Tracking and tagging parameters
:body

SchAcc URLs accept some query parameters that can be used to track and or tag
login sessions, users or orders. Some may also be used in third party analytics
systems as well. You can add these query parameters to URLs when linking the
user to SchAcc to log in/out, start a payment process, etc.

#### tracking_ref

Used as a grouping reference. For example a client-provided unique visitor reference used for tracking in third party
analytics like Adobe Sitecatalyst. It may be anything you want to track across client domains and SchAcc. 
This is also saved to SchAcc logins and events tables. 

#### tag

Custom client string used for custom order tracking. It may be anything you want to track across client domains and SchAcc. 
Available in order and events tables.

#### webview

Set to `1` to enable some webview-friendly changes to the login page's CSS. In
webview-mode, the login form has more padding, and the global SchAcc menu is
hidden. Useful when allowing users to login through a webview in native apps.
(These changes are applied responsively to small screens only).

#### xiti_json

<a href="http://www.xiti.com/en/">Xiti Analytics</a> tracking string. Used by
SchAcc to populate custom client-provided Xiti parameters for analytics in SchAcc.

```php
<?php
public function encodeXitiVariable($var) {
   return urlencode(json_encode($var));
}    

// Usage Example
$xiti = encodeXitiVariable(array(
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
