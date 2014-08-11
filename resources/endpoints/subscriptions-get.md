:introduction

Subscriptions are products with time constraints and, optionally, renewal
mechanisms.

<h2 id="standard">Standard subscriptions</h2>

Standard subscriptions are basically products with an expiry date, and can be
used to give customers access to some content for a limited duration. Examples
include 24 hour movie rental, 1 month access to a collection of articles, etc.

<h2 id="auto-renewable">Auto-renewable subscriptions</h2>

Auto-renewable subscriptions are like standard subscriptions, with the
difference that they are automatically renewed. The initial subscription period
may differ from the renewal period. This enables you to sell a 6 month
subscription that will automatically renew once a month after the first
expiration date.

<h2 id="limited-time">Limited time subscription</h2>

Limited time subscriptions have start and stop dates, in addition to
auto-renewal. The subscription will automatically renew until the subscription
end date is reached.

As an example, episode-based digital content is a good fit for limited time
subscriptions: If new episodes launch every week, create a subscription that is
valid between the start and end date of a season, say January 1st and March
31st. Set auto-renewal to 1 week to match the episode frequency.

:relevant-endpoints

POST /product
GET /product/{id}
