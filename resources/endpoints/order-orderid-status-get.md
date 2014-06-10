:introduction

Returns the order object with an updated status. Will also cancel any pending
[callback](/callbacks/#user-status-callback) requests for the order.

:relevant-types order

:relevant-endpoints

GET /order/{orderId}
GET /order/{orderId}/items
GET /order/{orderId}/transactions
