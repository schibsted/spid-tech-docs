:title WyWallet error codes
:frontpage
:category payment

:aside

## Relevant type definitions

- Error codes
- Error

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
    <td class="tg-yw4l">2101</td>
    <td class="tg-yw4l">The confirmation code was invalid. Please try again</td>
    <td class="tg-yw4l">Invalid confirmation code</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2102</td>
    <td class="tg-yw4l">The credit amount does not match transaction amount (visible only for Ambassador)</td>
    <td class="tg-yw4l">Credit amount does not match transaction amount</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2103</td>
    <td class="tg-yw4l">Not visible for end user</td>
    <td class="tg-yw4l">Cannot update WyWallet phone number, because it's already in terminal state: VERIFIED or DELETED</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2104</td>
    <td class="tg-yw4l">Your phone number is already in use, please try a different phone number or choose a different payment method</td>
    <td class="tg-yw4l">Phone number for user already exists</td>
  </tr>
    <tr>
    <td class="tg-yw4l">2105</td>
    <td class="tg-yw4l">NA → end user can retry to resend error code</td>
    <td class="tg-yw4l">Unable to send SMS to end user</td>
  </tr>
  <tr>
    <td class="tg-yw4l">2106</td>
    <td class="tg-yw4l">Something went wrong, please try again or choose a different payment method</td>
    <td class="tg-yw4l">Charge of phone number was unsuccessful</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2108</td>
    <td class="tg-yw4l">Something went wrong, please try again or choose a different payment method</td>
    <td class="tg-yw4l">Error when establishing connection with WyWallet</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2109</td>
    <td class="tg-yw4l">Something went wrong, please try again or choose a different payment method</td>
    <td class="tg-yw4l">Payment methods not verified</td>
  </tr>
    <tr>
    <td class="tg-yw4l">2110</td>
    <td class="tg-yw4l">Something went wrong, please try again or choose a different payment method</td>
    <td class="tg-yw4l">Payment method verification error</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2111</td>
    <td class="tg-yw4l">Sorry your mobile number is not supported, please contact your mobile operator or try a different payment method</td>
    <td class="tg-yw4l">Autopay error</td>
  </tr>
     <tr>
    <td class="tg-yw4l">2112</td>
    <td class="tg-yw4l">Something went wrong, please try again or choose a different payment method</td>
    <td class="tg-yw4l">Processing</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2113</td>
    <td class="tg-yw4l">NA</td>
    <td class="tg-yw4l">Client error - either not found or blocked or expired</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2114</td>
    <td class="tg-yw4l">Sorry you don’t have enough funds to perform this transaction, the maximum amount allowed is 2000 SEK per month, please choose a different payment method</td>
    <td class="tg-yw4l">Insufficient funds</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2115</td>
    <td class="tg-yw4l">Sorry the amount of this purchase exceeds the limit supported by WyWallet, please choose a different payment method</td>
    <td class="tg-yw4l">The limit of transaction amount has been exceeded</td>
  </tr>
    <tr>
    <td class="tg-yw4l">2116</td>
    <td class="tg-yw4l">Sorry you have reached the amount limit supported by WyWallet, please choose a different payment method</td>
    <td class="tg-yw4l">The accumulated limit of transaction amount has been exceeded</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2117</td>
    <td class="tg-yw4l">Sorry your transaction cannot be completed since you need first to create an account at https://konto.wywallet.se/ Note: this error won’t happen since the product ID does not require user registration</td>
    <td class="tg-yw4l">The limit of transaction amount has been exceeded</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2118</td>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l">Unsupported client</td>
  </tr>
  <tr>
    <td class="tg-yw4l">2119</td>
    <td class="tg-yw4l">Sorry your mobile number is not supported, please contact your mobile operator or try a different payment method</td>
    <td class="tg-yw4l">Transaction amount below minimum</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2120</td>
    <td class="tg-yw4l">Sorry your mobile number is not supported, please contact your mobile operator or try a different payment method</td>
    <td class="tg-yw4l">The amount of transaction is below minimum</td>
  </tr>
    <tr>
    <td class="tg-yw4l">2128</td>
    <td class="tg-yw4l">You have a business phone and therefore need to create a WyWallet account {: {link} here: CLOSE_LINK} to pay by SMS or contact {: contact {} WyWallet: close_contact}. You can also choose to pay with another means of payment</td>
    <td class="tg-yw4l">MSISDN status not OK</td>
  </tr>
  <tr>
    <td class="tg-yw4l">2129</td>
    <td class="tg-yw4l">Something went wrong, please try again or choose a different payment method</td>
    <td class="tg-yw4l">Reversal error</td>
  </tr>
 <tr>
    <td class="tg-yw4l">2131</td>
    <td class="tg-yw4l">Sorry your mobile subscription is not valid, please contact your mobile operator or choose a different payment method</td>
    <td class="tg-yw4l">No subscription could be found for the specified phone number</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2132</td>
    <td class="tg-yw4l">Sorry your transaction cannot be completed please contact TeliaSonera for further support, or choose a different payment method</td>
    <td class="tg-yw4l">Telia Sonera user not identified by mobile operator</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2133</td>
    <td class="tg-yw4l">Sorry your transaction cannot be completed, please contact your mobile operator or choose a different payment method</td>
    <td class="tg-yw4l">User not identified by mobile operator</td>
  </tr>
  <tr>
    <td class="tg-yw4l">2134</td>
    <td class="tg-yw4l">Sorry your mobile number is not supported, please contact your mobile operator or try a different payment method</td>
    <td class="tg-yw4l">Phone number is not supported by WyWallet</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2135</td>
    <td class="tg-yw4l">Sorry your mobile number is not supported, please contact your mobile operator or try a different payment method</td>
    <td class="tg-yw4l">Autopay expired</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2136</td>
    <td class="tg-yw4l">Sorry your transaction cannot be completed since you need first to register and accept WyWallet T&Cs visit WyWallet.se for more information
Note: this error won’t happen since the product ID does not require user registration</td>
    <td class="tg-yw4l">The customer needs to register on the portal and accept terms and conditions</td>
  </tr>
    <tr>
    <td class="tg-yw4l">2137</td>
    <td class="tg-yw4l">Your phone number is already in use, please try a different phone number or choose a different payment method</td>
    <td class="tg-yw4l">User had already verified the requested phone number</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2138</td>
    <td class="tg-yw4l">Your phone number is already in use, please try a different phone number or choose a different payment method</td>
    <td class="tg-yw4l">Another user had verified the requested phone number</td>
  </tr>
   <tr>
    <td class="tg-yw4l">2199</td>
    <td class="tg-yw4l">Sorry your transaction cannot be completed, the maximum amount allowed is 700 SEK, please choose a different payment method</td>
    <td class="tg-yw4l">PPA WyWallet internal error</td>
  </tr>
</table>
