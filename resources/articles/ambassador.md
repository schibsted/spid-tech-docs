:title Ambassador / support tool
:frontpage
:category self-service
:body

Ambassador is a web based tool that has two different fields of use. It intends to be the supporting tool for the client support center and it is an administration tool for clients to create, modify and maintain client products and campaigns

### Login

Login to ambassador assume the client-side user is registered with a Schibsted account account. Ambassador requires two-step-authentication with password and a SMS token. Hence, the user must have a registered mobile number on the Schibsted account account prior to logging in to Ambassador. SP must also register these users manually in Schibsted account. The client will need to supply SP with a full list of persons requiring Ambassador access. This list must contain:

* Registered Schibsted account e-mail address
* Role (product admin or support) per service
* Environment (stage/pre or production)

Make sure all persons in this list have registered and verified both e-mail and phone number on the relevant environments’ Schibsted account account **prior** to handing this over to SP.

To access the ambassador web application use the following URLs

<table class="sectioned mbl" width="100%">
  <tr>
    <th>Environment</th>
    <th>Ambassador URL</th>
  </tr>
  <tr>
    <td>Stage/Pre</td>
    <td><a href="https://ambassador.identity-pre.schibsted.com">https://ambassador.identity-pre.schibsted.com</a></td>
  </tr>
  <tr>
    <td>Production Norway</td>
    <td><a href="https://ambassador.payment.schibsted.no">https://ambassador.payment.schibsted.no</a></td>
  </tr>
  <tr>
    <td>Production Sweden/International</td>
    <td><a href="https://ambassador.schibsted.com/">https://ambassador.schibsted.com/</a></td>
  </tr>
</table>

##Support tool

For the client support center, Ambassador will be the main tool for supporting their end-users. Support staff will have access to the user-profile, subscriptions, orders, products and purchase history. Figure below gives an example of the interface from Ambassador.

![Input fields example](/images/support/ambassador.png)

Support staff can help customer change basic profile information and identify necessary subscriptions and purchase information in addition to issuing credits on purchases.  For password issues, support can help a user by resetting the password. The end-user will be issued an email with a link to set the new password.

If the client support center encounters a support case which requires second or third line support, this is logged through the “contact support” button in the top right corner. This generates a direct message into the Zendesk support system which Schibsted Payment uses to handle all support tickets.

##Product and campaign administration

The second function of Ambassador is an administration interface for clients for setting up and maintaining their products and campaigns. Clients get a simple overview of all products and can easily add, change and modify their available products. Similarly, campaigns can be created and linked up to products.

Additionally, Ambassador offer general administration of auto generated email to end-users, import of third-party vouchers and generic order, payment and product reports.

##Zendesk

Zendesk is the customer support system used by Schibsted account. Client support center, requiring second- or third line support, logs their tickets here. Logging tickets can be done through the “contact support”-button when logged into Ambassador, or through login on http://support.schibstedpayment.no.  For ticket status or additional information on a ticket the webpage login must be used. A Zendesk user-profile is created the first time a ticket is registered.

Clients’ support center will have to coordinate their support ticket in Zendesk with their internal support/help desk system.