:title Getting started with Paylinks

:aside

## Prerequisites

In order to complete this guide, you need to know your:

- client ID
- client secret

You should also have gone through the [Getting Started](/getting-started/)
guide. In particular, you should have downloaded and installed the appropriate
SDK for your platform.

:body

When you have completed this guide, you can sell products by allowing users to
check out and pay via Schibsted account.

## Overview

This is the flow:

- The user chooses product(s) on your website.
- You send the list of items to Schibsted account, and get a unique URL back. Redirect the
  user to this Paylink URL.
- The user completes payment on the Schibsted account website, and is redirected back to your site with an order ID.
- You refresh your user info, and track the status of payment with the order ID.

It's illustrated in [this sequence diagram](/paylink-api/#purchase-flows).

We'll look at these steps in detail in the rest of the guide.

## Configure your application

These variables change between production and staging environments:

- Your client ID
- Your client secret
- Your client signature secret
- The base URL to Schibsted account
- Your base URL

How you choose to configure your application is up to you, but
these variables should not be hard-coded.

## The user chooses products on your website

Here are some things to keep in mind when implementing paylinks on your site:

- Before you create the Paylink, you need to know what items the user wants to purchase,
  as well as the price, VAT and quantities of each.
- Schibsted account will present the user with a summary of the items, and a total price.
  Quantities can not be changed at this point.

In the example, we're keeping it simple with three products - each with an input
field to specify quantity:

```html
<form action="/checkout" method="post">
 <div><input class="amount" type="number" name="sw4" value="1"> Star Wars IV</div>
 <div><input class="amount" type="number" name="sw5" value="1"> Star Wars V</div>
 <div><input class="amount" type="number" name="sw6" value="1"> Star Wars VI</div>
 <input type="submit" value="Buy these excellent movies">
</form>
```

## Send the list of items to Schibsted account

We prepare the purchase by creating a Paylink - which has a URL to which we can
redirect the user for payment.

To [create a Paylink](/endpoints/POST/paylink/), Schibsted account expects a few parameters:

- `items` A list of items as a JSON-string, each item with: `description`, `price`, `vat` and `quantity`
- `title` A description shown to the user on the payment page.
- `redirectUri` Where Schibsted account should send the user after a successful payment
- `cancelUri` Where Schibsted account should send the user after a cancelled payment
- `clientReference` Your reference, for tracking and processing the order generated by this PayLink

We'll take the quantities posted by the user, and create the data.

**NB!** If you store products in Schibsted account, you can pass on `productId` when creating
paylinks. Refer to the [reference documentation](/endpoints/POST/paylink/) for
all options.

```java
public class Product {
    final String description;
    final int price;
    final int vat;

    public Product(String description, int price, int vat) {
        this.description = description;
        this.price = price;
        this.vat = vat;
    }
}

public class PaylinkItem {
    final String description;
    final int price;
    final int vat;
    final int quantity;

    public PaylinkItem(Product product, int quantity) {
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.vat = product.getVat();
        this.quantity = quantity;
    }
}
```

### Create paylink

With our data in hand, we can create the Paylink by POSTing to
[the /paylinks endpoint](/endpoints/POST/paylink/).

```java
private static Map<String, Product> products = new HashMap<String, Product>(){{
        put("sw4", new Product("Star Wars IV", 9900, 2400));
        put("sw5", new Product("Star Wars V", 9900, 2400));
        put("sw6", new Product("Star Wars VI", 9900, 2400));
    }};

private JSONObject createPaylink(List<PaylinkItem> items) {
    String serverAccessToken = getServerAccessToken(clientId, clientSecret);
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + serverAccessToken);
    Response response = httpClient.POST("/paylink", createPaylinkData(items), headers);
    return response.getJsonData();
}

private Map<String, String> createPaylinkData(List<PaylinkItem> items) {
    Map<String, String> data = new HashMap<>();
    data.put("title", "Quality movies");
    data.put("redirectUri", appBaseUrl + "/callback");
    data.put("cancelUri", appBaseUrl + "/cancel");
    data.put("clientReference", "Order number " + System.currentTimeMillis());
    data.put("items", gson.toJson(items));
    return data;
}

private List<PaylinkItem> getPaylinkItems(Map<String, String> params) {
    List<PaylinkItem> items = new ArrayList<PaylinkItem>();
    for (Map.Entry<String, String> entry : params.entrySet()) {
        int quantity = Integer.parseInt(entry.getValue());
        Product p = products.get(entry.getKey());
        if (quantity > 0 && p != null) {
            items.add(new PaylinkItem(p, quantity));
        }
    }
    return items;
}
```

## Redirect the user to Schibsted account for payment

Our new Paylink has [quite a few fields](/types/paylink/), but the one we're
interested in is the `shortUrl`. We'll use this to send the user to Schibsted account for
payment.

```java
@RequestMapping("/checkout")
String checkout(@RequestParam Map<String, String> params) {
    JSONObject paylink = createPaylink(getPaylinkItems(params));
    return "redirect:" + paylink.get("shortUrl");
}
```

The user will be presented with a summary of the order, along with payment options.

## The user returns after completing the purchase

Schibsted account will redirect the user to your given `redirectUri` after a successful
purchase. It is a GET request, which includes two query parameters: `order_id` and `code`. We'll start with the latter:

### Make sure we've got the right user

The user might have logged in with a different user since we sent them to Schibsted account
(like on a shared computer). We need to ensure we're giving the purchased
product to the right user.

So, like in [the Implementing Single Sign-On guide](/implementing-sso/), we
create a session for the user with information from Schibsted account:

```java
@RequestMapping("/callback")
String callback(@RequestParam String code,
                @RequestParam String order_id,
                HttpServletRequest request) {
    // Retrieve this user's access token
    String userAccessToken = getUserAccessToken(clientId, clientSecret, code);
    
    // Use the access token to get info about the user
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + userAccessToken);
    Response response = spidClient.GET("/oauth/userinfo", headers);
    JSONObject userData = response.getJsonData();

    // Save token and info in session
    request.getSession().setAttribute("userToken", token);
    request.getSession().setAttribute("userInfo", user);

    return "redirect:/success?orderId=" + order_id;
}
```

Note that the code has a short lifespan, so it is prudent to create the user
token with the code first, and then 302 redirect to another success landing page
with the code removed from the URL.

### Check the status of the order

Once we've got the right user, we can use the `order_id` to look up information
about the order.

```java
private static Map<String, String> orderStatus = new HashMap<String, String>(){{
    put("-3", "Expired");
    put("-2", "Cancelled");
    put("-1", "Failed");
    put("0", "Created");
    put("1", "Pending");
    put("2", "Complete");
    put("3", "Credited");
    put("4", "Authorized");
}};

private JSONObject getOrder(String orderId) {
    String serverAccessToken = getServerAccessToken(clientId, clientSecret);
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + serverAccessToken);
    Response response = httpClient.GET("/order/" + orderId + "/status", headers);
    return response.getJsonData();
}
```

The order status is likely **Complete** (status `"2"`), meaning everything is
proceeding according to our plans. Time to give the user what they payed for.

#### Cancel

The user might also cancel the purchase, in which case they'll be redirected to
your given `cancelUri`. It will include a `spid_page` query parameter that
describes where in the process the user left.

## Further reading

- [Callbacks](/callbacks/) notifies you about changes to your orders.
