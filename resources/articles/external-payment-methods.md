:title Provided payment methods
:frontpage
:category payment

:relevant-endpoints

POST /paylink
GET /paylink/{paylinkId}
DELETE /paylink/{paylinkId}

:aside
## Table of Contents

<spid-toc></spid-toc>

:body

`Available since Schibsted account version 2.111.0`

In case when Client wants to give the user the possibility to pay with payment method 
that is not supported in Schibsted account (Payment Platform), Client is able to provide all 
new payment method details via Paylink.

If user chooses this payment method, Schibsted account redirects him to URL provided by Client in Paylink details.

Such payment methods are called provided (external).

<img alt="Choose payment method step" style="display: block; max-width: 100%; height: auto; margin: auto; float: none!important;" src="/images/choose-payment.png">


## Basic functionality

In order to allow provided payment methods, payment method details has to be 
provided in create Paylink request, in `paymentOptions` field, as a JSON [array of Payment Option objects](/types/payment-options-array/).

There is a possibility to setup the Paylink to be paid:

- only with payment methods supported in Schibsted account
- both with payment methods supported in Schibsted account and with provided payment methods
- only with provided payment methods

***

#### Payment with payment methods supported in Schibsted account

In order to give the user the possibility to pay with payment methods supported in Schibsted account,
the only required parameter in Payment Option object is `type`. Other parameters are ignored.

See the [Payment Option type specification](/types/payment-option-type/) for further details on `type` field.


```
curl https://payment.schibsted.no/api/2/paylink \
   -X POST \
   -d "oauth_token=[access token]" \
   -d "title=Freebies for all" \
   -d "items=[{\"name\":\"Episode\",\"description\":\"Star Wars IV\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars V\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars VI\",\"price\":9900,\"vat\":2500}]" \
   -d "paymentOptions=[{\"type\":\"PAYEX_CC\"},{\"type\":\"KLARNA\"}]" \
```

***

#### Payment with provided payment methods

In order to give the user the possibility to pay with provided payment methods,
additional parameters defining Payment Option have to be provided - `subType`, `url`, `name`, `imageUrl` and `description`. 

Parameter `subType` is used by Schibsted account to distinguish between all provided payment 
methods. It is important to agree this value with Schibsted account support before use.

After user chooses to pay with provided payment method, Schibsted account redirects him 
to `url`.

Parameters `name`, `imageUrl` and `description` are used to display provided payment method in Schibsted account checkout.

<img alt="Choose payment provided details" style="display: block; max-width: 100%; height: auto; margin: auto; float: none!important;" src="/images/choose-payment-external-details.png">


See the [Payment Option object specification](/types/payment-options-array/) for further details.

Passed array can contain one or more Payment Option objects of type `PROVIDED`. 


```
curl https://payment.schibsted.no/api/2/paylink \
   -X POST \
   -d "oauth_token=[access token]" \
   -d "title=Freebies for all" \
   -d "items=[{\"name\":\"Episode\",\"description\":\"Star Wars IV\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars V\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars VI\",\"price\":9900,\"vat\":2500}]" \
   -d "paymentOptions=[{\"type\":\"PROVIDED\",\"subType\":\"Vouchers\",\"url\":\"https://external-url.com\",\"name\":\"Vouchers\",\"imageUrl\":\"https://external-url.com/vouchers-icon.jpeg\",\"description\":\"Vouchers allow you to pay with voucher code.\"},{\"type\":\"PROVIDED\",\"subType\":\"Wallet\",\"url\":\"https://external-wallet-url.com\",\"name\":\"Wallet\",\"imageUrl\":\"https://external-wallet-url.com/wallet-icon.jpeg\",\"description\":\"You can pay with money from your Wallet.\"}]" \
```

***

#### Payment with payment methods supported in Schibsted account and with provided payment methods

```
curl https://payment.schibsted.no/api/2/paylink \
   -X POST \
   -d "oauth_token=[access token]" \
   -d "title=Freebies for all" \
   -d "items=[{\"name\":\"Episode\",\"description\":\"Star Wars IV\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars V\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars VI\",\"price\":9900,\"vat\":2500}]" \
   -d "paymentOptions=[{\"type\":\"PAYEX_CC\"},{\"type\":\"KLARNA\"},{\"type\":\"PROVIDED\",\"subType\":\"Vouchers\",\"url\":\"https://external-url.com\",\"name\":\"Vouchers\",\"imageUrl\":\"https://external-url.com/vouchers-icon.jpeg\",\"description\":\"Vouchers allow you to pay with voucher code.\"}]" \
```


## Preconditions and requirements

* Access to the paylink endpoints. Only given to vetted implementations and use cases.
* Provided payment method type (`subType`, see [Payment Option object specification](/types/payment-options-array/)) has to be determined in cooperation with Schibsted account support.
* Provided payment methods URLs have to be whitelisted in client configuration.


