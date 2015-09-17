:introduction

List and search all orders.

## Examples

Fetch all orders that changed within the past 7 days:

```sh
curl http://identity-pre.schibsted.com/api/2/users?oauth_token=TOKEN&\
  since=last week&\
  filters=updated
```

Fetch all orders created between yesterday and today:

```sh
curl http://identity-pre.schibsted.com/api/2/users?oauth_token=TOKEN&\
  since=yesterday&\
  until=today
```

Fetch 10 orders created today (since midnight):

```sh
curl http://identity-pre.schibsted.com/api/2/users?oauth_token=TOKEN&\
  since=today&\
  limit=10&\
  offset=20
```

Fetch 10 orders created since february 22nd 2013:

```sh
curl http://identity-pre.schibsted.com/api/2/users?oauth_token=TOKEN&\
  since=2013-02-22&\
  limit=10&\
  offset=20
```

:relevant-types order

:relevant-endpoints

GET /order/{orderId}
GET /order/{orderId}/items
GET /order/{orderId}/transactions
