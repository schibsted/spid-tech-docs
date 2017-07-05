:title External payment methods
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

`Available since SPiD version 2.111.0`

In case when Client wants to give the user the possibility to pay with payment method 
that is not supported in SPiD (Payment Platform), Client is able to provide all 
new payment method details via Paylink.

If user choose this payment method, SPiD redirects him to URL provided by Client in Paylink details.

Such payment methods are called external.

<img alt="Choose payment method step" style="display: block; max-width: 100%; height: auto; margin: auto; float: none!important;" src="/images/choose-payment.png">


## Basic functionality

In order to allow external payment methods, payment method details has to be 
provided in create Paylink request, in `paymentOptions` field, as a JSON [array of Payment Option objects](/types/payment-options-array/).

There is a possibility to setup the Paylink to be paid:

- only with payment methods supported in SPiD
- both with payment methods supported in SPiD and with external payment methods
- only with external payment methods

***

#### Payment with payment methods supported in SPiD

In order to give the user the possibility to pay with payment methods supported in SPiD,
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

#### Payment with external payment methods

In order to give the user the possibility to pay with external payment methods,
additional parameters defining Payment Option have to be provided - `externalType`, `url`, `name`, `imageUrl` and `description`. 

Parameter `externalType` is used by SPiD to distinguish between all external payment 
methods. It is important to agree this value with SPiD support before use.

After user chooses to pay with external payment method, SPiD redirects him 
to `url`.

Parameters `name`, `imageUrl` and `description` are used to display external payment method in SPiD checkout.

<img alt="Choose payment external details" style="display: block; max-width: 100%; height: auto; margin: auto; float: none!important;" src="/images/choose-payment-external-details.png">


See the [Payment Option object specification](/types/payment-options-array/) for further details.

Passed array can contain one or more Payment Option objects of type `EXTERNAL`. 


```
curl https://payment.schibsted.no/api/2/paylink \
   -X POST \
   -d "oauth_token=[access token]" \
   -d "title=Freebies for all" \
   -d "items=[{\"name\":\"Episode\",\"description\":\"Star Wars IV\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars V\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars VI\",\"price\":9900,\"vat\":2500}]" \
   -d "paymentOptions=[{\"type\":\"EXTERNAL\",\"externalType\":\"Vouchers\",\"url\":\"https://external-url.com\",\"name\":\"Vouchers\",\"imageUrl\":\"https://external-url.com/vouchers-icon.jpeg\",\"description\":\"Vouchers allow you to pay with voucher code.\"},{\"type\":\"EXTERNAL\",\"externalType\":\"Wallet\",\"url\":\"https://external-wallet-url.com\",\"name\":\"Wallet\",\"imageUrl\":\"https://external-wallet-url.com/wallet-icon.jpeg\",\"description\":\"You can pay with money from your Wallet.\"}]" \
```

***

#### Payment with payment methods supported in SPiD and with external payment methods

```
curl https://payment.schibsted.no/api/2/paylink \
   -X POST \
   -d "oauth_token=[access token]" \
   -d "title=Freebies for all" \
   -d "items=[{\"name\":\"Episode\",\"description\":\"Star Wars IV\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars V\",\"price\":9900,\"vat\":2500},{\"description\":\"Star Wars VI\",\"price\":9900,\"vat\":2500}]" \
   -d "paymentOptions=[{\"type\":\"PAYEX_CC\"},{\"type\":\"KLARNA\"},{\"type\":\"EXTERNAL\",\"externalType\":\"Vouchers\",\"url\":\"https://external-url.com\",\"name\":\"Vouchers\",\"imageUrl\":\"https://external-url.com/vouchers-icon.jpeg\",\"description\":\"Vouchers allow you to pay with voucher code.\"}]" \
```


## Preconditions and requirements

* Access to the paylink endpoints. Only given to vetted implementations and use cases.
* External payment method type (`externalType`, see [Payment Option object specification](/types/payment-options-array/)) has to be determined in cooperation with SPiD support.
* External payment methods URLs have to be whitelisted in client configuration.


