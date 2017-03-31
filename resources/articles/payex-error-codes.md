:title PayEx error codes
:frontpage
:category payment

:aside

## Relevant type definitions

- Error codes
- Error
- Credit card

:body

<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg .tg-9hbo{font-weight:bold;vertical-align:top}
.tg .tg-yw4l{vertical-align:top}
</style>
<table class="tg">
  <tr>
    <th class="tg-9hbo">Error/Return code</th>
    <th class="tg-9hbo">Message shown to customer</th>
    <th class="tg-9hbo">Description</th>
  </tr>
  <tr>
    <td class="tg-yw4l">REJECTED_BY_ACQUIRER</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">Your customers bank declined the transaction, your customer can contact their bank for more information</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Error_Generic</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">A generic error</td>
  </tr>
  <tr>
    <td class="tg-yw4l">3DSecureDirectoryServerError</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process with 3D Secure. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">A problem with Visa or MasterCards directory server, that communicates transactions for 3D-Secure verification</td>
  </tr>
  <tr>
    <td class="tg-yw4l">AcquirerComunicationError</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">Communication error with the acquiring bank</td>
  </tr>
  <tr>
    <td class="tg-yw4l">AmountNotEqualOrderLinesTotal</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The sum of your order lines is not equal to the price set in initialize</td>
  </tr>
  <tr>
    <td class="tg-yw4l">CardNotEligible</td>
    <td class="tg-yw4l">Your card is not valid for this type of transaction with us. No money was deducted from your account. Please try again with another card.</td>
    <td class="tg-yw4l">Your customers card is not eligible for this kind of purchase, your customer can contact their bank for more information</td>
  </tr>
  <tr>
    <td class="tg-yw4l">CreditCard_Error</td>
    <td class="tg-yw4l">Some problem occurred with the credit card, you should contact your bank for more information</td>
    <td class="tg-yw4l">Some problem occurred with the credit card, your customer can contact their bank for more information</td>
  </tr>
  <tr>
    <td class="tg-yw4l">PaymentRefusedByFinancialInstitution</td>
    <td class="tg-yw4l">The payment process was stopped by the card issuer. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">Your customers bank declined the transaction, your customer can contact their bank for more information</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Merchant_InvalidAccountNumber</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The merchant account number sent in on request is invalid</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Merchant_InvalidIpAddress</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The IP address the request comes from is not registered in PayEx, you can set it up in PayEx Admin under Merchant profile</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Access_MissingAccessProperties</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The merchant does not have access to requested functionality</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Access_DuplicateRequest</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">Your customers bank declined the transaction, your customer can contact their bank for more information</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Admin_AccountTerminated</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The merchant account is not active</td>
  </tr>
  <tr>
    <td class="tg-yw4l">Admin_AccountDisabled</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The merchant account is not active</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ValidationError_AccountLockedOut</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The merchant account is locked out</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ValidationError_Generic</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">Generic validation error</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ValidationError_HashNotValid</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">The hash on request is not valid, this might be due to the encryption key being incorrect</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ValidationError_InvalidParameter</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l">One of the input parameters has invalid data. See paramName and description for more information</td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACCOUNTBLOCKED</td>
    <td class="tg-yw4l">Your account is not available for this type of transaction. No money was deducted from your account. Please try again with a different card / account.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">CARDNOTACCEPTEDFORTHISPURCHASE</td>
    <td class="tg-yw4l">Your card was not approved for this type of transaction with us. No money was deducted from your account. Please try again with another card.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">CHARGINGERROR</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">COMMUNICATIONERROR</td>
    <td class="tg-yw4l">Sorry, a communication error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">DUPLICATEORDERID</td>
    <td class="tg-yw4l">We have already registered a purchase from you with the same order number. Duplicate purchase avoided, and no money was deducted from your account.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">FRAUDPATTERNDETECTED</td>
    <td class="tg-yw4l">The card is blocked because of suspected abuse on this card. Please try again with another card.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">INSUFFICIENTFOUNDS</td>
    <td class="tg-yw4l">There are not sufficient funds on the card to complete this transaction. No money was deducted from your account. Please try again with another card.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">INVALIDDATA</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">INVALIDPOSTNUMBER</td>
    <td class="tg-yw4l">The postal code provided is incorrect. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">MISSINGMERCHANTCONFIGURATION</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">NOMONEYONACCOUNT</td>
    <td class="tg-yw4l">There are not sufficient funds on the account to complete this transaction. No money was deducted from your account. Please try again with a different card/account.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">NORECORDFOUND</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">OPERATIONCANCELLEDBYCUSTOMER</td>
    <td class="tg-yw4l">The payment process was interrupted. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">OPERATIONNOTALLOWED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PARTIALCAPTURENOTALLOWED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAYMENTTYPENOTSUPPORTEDBYAGREEMENT</td>
    <td class="tg-yw4l">The payment method was not approved for the current card agreement. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAYPALBUYERRESTRICTED</td>
    <td class="tg-yw4l">Your Paypal account stopped this transaction. No money was deducted from your account. Please try again with a different card/account.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">SUBSCRIBERISBLOCKED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">UDTBALANCEEXCEEDED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">UNEXPECTEDCARDISSUERAUTHENTICATIONRESPONS</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">UNRECOGNIZEDCONSUMER</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">UNSUPPORTEDCURRENCY</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">USAGECHECKFAILED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">VERIFICATIONFAILED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">INVALIDPHONENUMBERCALLINGCODE</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. The calling code you provided is not supported for this payment.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">MERCHANT_INVALIDACCOUNTNUMBER</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">MERCHANT_INVALIDIPADDRESS</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACCESS_MISSINGACCESSPROPERTIES</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACCESS_DUPLICATEREQUEST</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACCESS_METHODDOWNFORMAINTENANCE</td>
    <td class="tg-yw4l">Sorry, the payment provider is not currently available. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACCESS_MISSINGSOURCECONFIGURATION</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ADMIN_ACCOUNTUNDERCONSTRUCTION</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ADMIN_ACCOUNTTERMINATED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ADMIN_ACCOUNTDISABLED</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">VALIDATIONERROR_ACCOUNTLOCKEDOUT</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">VALIDATIONERROR_GENERIC</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">VALIDATIONERROR_HASHNOTVALID</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">VALIDATIONERROR_INVALIDPARAMETER</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">VALIDATIONERROR_INVALIDDATA</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Could you please try again later?</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">AGREEMENTDELETED</td>
    <td class="tg-yw4l">The current card agreement has been deleted. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">AGREEMENTNOTVERIFIED</td>
    <td class="tg-yw4l">Current card agreement is invalid. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">AGREEMENTEXISTS</td>
    <td class="tg-yw4l">An agreement already exists. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">CHARGINGBALANCETOPLOW</td>
    <td class="tg-yw4l">ChargingBalanceTopLow</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">CONSUMERBLOCKED</td>
    <td class="tg-yw4l">Card / account is not valid for this type of transaction. No money was deducted from your account. Please try again with another card.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">CONSUMERBLOCKEDSERVICE</td>
    <td class="tg-yw4l">The payment process was stopped because of a lock on the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">INSUFFICIENTFOUNDS</td>
    <td class="tg-yw4l">There are not sufficient funds on the card to complete this transaction. No money was deducted from your account. Please try again with another card.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">OPERATIONORDERSTATUSMISMATCH</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">OPERATIONPAYMENTSTATUSMISMATCH</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAYMENTOBJECTALREADYEXIST</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">SUBSCRIBERBARRED</td>
    <td class="tg-yw4l">SubscriberBarred</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">SUBSCRIBERUNKNOWN</td>
    <td class="tg-yw4l">SubscriberUnknown</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">INVALIDAMOUNT</td>
    <td class="tg-yw4l">Invalid amount</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAYMENTDECLINEDDOTOUNSPECIFIEDERR</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">PAYMENTDECLINEDDUETOUNSPECIFIEDERR</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">SYSTEM_EXCEPTIONRAISED</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Could you please try again later?</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RECURRINGEXPIRYERROR</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">RECURRINGFREQUENCYERROR</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">UNRECOGNIZEDCONSUMER</td>
    <td class="tg-yw4l">Sorry, an error occurred in communicating with the payment provider. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
  <tr>
    <td class="tg-yw4l">ACQUIRER_HOST_OFFLINE</td>
    <td class="tg-yw4l">Sorry, an error occurred in the payment process. No money was deducted from your account. Please try again.</td>
    <td class="tg-yw4l"></td>
  </tr>
</table>
