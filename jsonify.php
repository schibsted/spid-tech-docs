<?php
require("routes.api.2.php");

echo json_encode(array(
    "name" => "SPP Container",
    "version" => "0.2",
    "api" => 2,
    "object" => "Utility",
    "type" => "collection",
    "code" => 200,
    "request" => array(
        "reset" => 3154,
        "limit" => 360000,
        "remaining" => 359997
    ),
    "debug" => array(
        "route" => array(
            "name" => "List API endpoints",
            "url" => "\/api\/2\/endpoints",
            "controller" => "Api\/2\/Utility.endpoints"
        ),
        "params" => array(
            "options" => array(),
            "where" => array()
        )
    ),
    "meta" => array(
        "count" => 98,
        "offset" => 0
    ),
    "error" => null,
    "data" => $_CORE_ROUTES
));


/* Test that EmptyAssocArray works just like arrays

$obj = new EmptyAssocArray();

foreach ($obj as $k => $v) {
    print($k);
}

print("Count: " . count($obj));
print("Access: " . $obj["something"]);

print(json_encode($obj));
*/