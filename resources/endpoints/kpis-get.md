:introduction

Get <abbr title="Key Performance Indicator">KPI</abbr>s in this client.

The KPI tallies are generated at 5:00 AM every morning. A KPI created 2012-12-12
contains all data up to 2012-12-12 05:00:00. We cannot guarantee there is a KPI
generated for every date, since the cronjob might be stopped due to deployment.

### Available KPIs

<table class="sectioned mbl">
  <tr>
    <th>Name</th>
    <th>Description</th>
  </tr>
  <tr>
    <th><code>users_registered</code></th>
    <td>Number of registered user with connection to client</td>
  </tr>
  <tr>
    <th><code>users_verified</code></th>
    <td>Number of verified users with connection to client</td>
  </tr>
  <tr>
    <th><code>users_using_facebook</code></th>
    <td>Number of users with Facebook connection and connection to client</td>
  </tr>
  <tr>
    <th><code>users_using_google</code></th>
    <td>Number of users with Google connection and connection to client</td>
  </tr>
  <tr>
    <th><code>users_using_live</code></th>
    <td>Number of users with Windows Live connection and connection to client</td>
  </tr>
  <tr>
    <th><code>logins</code></th>
    <td>Number of logins</td>
  </tr>
  <tr>
    <th><code>completed_orders</code></th>
    <td>Number of orders with status complete</td>
  </tr>
  <tr>
    <th><code>completed_orders_with_creditcard</code></th>
    <td>Number of orders with status complete, performed with credit card</td>
  </tr>
  <tr>
    <th><code>completed_orders_with_invoice</code></th>
    <td>Number of orders with status complete, performed with invoice</td>
  </tr>
  <tr>
    <th><code>completed_orders_with_sms</code></th>
    <td>Number of orders with status complete, performed with SMS</td>
  </tr>
  <tr>
    <th><code>sum_completed_orders</code></th>
    <td>Sum of price of orders, matching same critera as above</td>
  </tr>
  <tr>
    <th><code>sum_completed_orders_with_creditcard</code></th>
    <td>Sum of price of orders, matching same critera as above</td>
  </tr>
  <tr>
    <th><code>sum_completed_orders_with_invoice</code></th>
    <td>Sum of price of orders, matching same critera as above</td>
  </tr>
  <tr>
    <th><code>sum_completed_orders_with_sms</code></th>
    <td>Sum of price of orders, matching same critera as above</td>
  </tr>
  <tr>
    <th><code>total_active_subscriptions</code></th>
    <td>Number of active subscriptions</td>
  </tr>
  <tr>
    <th><code>total_active_subscriptions_autorenew_on</code></th>
    <td>Number of active subscriptions, auto renew turned on</td>
  </tr>
  <tr>
    <th><code>total_active_subscriptions_autorenew_off</code></th>
    <td>Number of active subscriptions, auto renew turned off</td>
  </tr>
  <tr>
    <th><code>total_churned_subscriptions</code></th>
    <td>Number of churned subscriptions</td>
  </tr>
  <tr>
    <th><code>actively_churned_subscriptions</code></th>
    <td>Number of actively churned subscriptions</td>
  </tr>
  <tr>
    <th><code>passively_churned_subscriptions</code></th>
    <td>Number of passively churned subscriptions</td>
  </tr>
  <tr>
    <th><code>api_churned_subscriptions</code></th>
    <td>Number of churned subscriptions through API (ie <a href="/ambassador/">Ambassador</a>)</td>
  </tr>
  <tr>
    <th><code>force_churned_subscriptions</code></th>
    <td>Number of forced churned subscriptions</td>
  </tr>
  <tr>
    <th><code>deleted_account_churned_subscriptions</code></th>
    <td>Number of churned subscriptions, due to account deletion</td>
  </tr>
  <tr>
    <th><code>final_churned_subscriptions</code></th>
    <td>Number of churned subscriptions, due to final end date reached</td>
  </tr>
  <tr>
    <th><code>renewed_subscriptions</code></th>
    <td>Number of renewed subscriptions</td>
  </tr>
  <tr>
    <th><code>paused_subscriptions</code></th>
    <td>Number of paused subscriptions</td>
  </tr>
  <tr>
    <th><code>expiration_changed_subscriptions</code></th>
    <td>Number of subscriptions where expiration date changed</td>
  </tr>
</table>

:relevant-endpoints

GET /anonymous/users
