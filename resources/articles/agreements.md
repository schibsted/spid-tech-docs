:title Agreements
:frontpage
:category self-service
:body

We here explain the usage and creation of agreements in SPiD.

## Overview
The user agreement is divided in three parts. Part I is an introduction, and Part II states the usage of SPiD. Part III is service specific (also called client terms). The agreement addresses the legal terms the user agrees to and a description of which services SPiD provides. The terms of use must be accepted by the end user in order to create a SPiD account.

Part I and Part II only need to be read and accepted once (and when changes are made to the agreement) when the user creates a SPiD account. Part III which is client specific will be generated for each new client that the user choses connect with.

An end user can create a SPiD account and accept Part I and Part II without connecting the SPiD account to a service. However, an end user may not access a service connected to SPiD without having a SPiD account and thus accepting the SPiD specific terms of use.

The standard terms can be found here:

Norway: [https://payment.schibsted.no/terms](https://payment.schibsted.no/terms).

Sweden: [https://payment.schibsted.se/terms](https://payment.schibsted.se/terms).

If you append your client_id to the term urls above you will also see PART III, which contains the provided client's terms section.

## When implementing SPiD
Each user facing API client must have terms of use in place before going into production. Here are the main steps you need to take into account when working with your SPiD implementation.

1. Write your terms of use.
2. Send them to support@spid.no. We will review the draft and make eventual adjustments.
3. Publish your terms when approved by SPiD.