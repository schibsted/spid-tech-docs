:title Client Teaser Documentation
:frontpage
:category self-service
:body

![Client teaser example](/images/client_teaser/client_teaser_example.png)

# Introduction

During the creation of the new design one of the most requested features was for you, the clients, to be able to style the SPiD pages more and in that step we introduce what we call “*Client Teasers*”.

Client Teasers is a simple way for clients to both style the page making it more relevant for your customers and also creating reasons why users should create an account / sign in to purchase or access your products.

# How it works

When you create a new client teaser, you will write markdown and CSS. The markdown will be converted into HTML together with the CSS and some extra javascript that is required for cross-origin iframes to work.

The HTML-page will then be uploaded to our CDN (Content Delivery Network) in order for us to serve your users in the most efficient way. Each upload will be an unique file so each change should be seen immediatly after the upload is complete and served at the very next request.

You may only have one teaser activated for each client and it’s only shown if it is activated.

# Each input field explained

## Teaser description

The description field is a short text to explain what the teaser is about. Given the campaign example above, a good description could be “*Shows our special campaign prices for campaign y*”.

The description is shown in the summary of all your client teasers.

## Teaser CSS

The “Teaser CSS” input field is where all the CSS that is styling your HTML will go. We do not have any specific rules about this one except it is a good idea to style your elements with a thought about the teasers will be shown on different screen sizes and resolutions so you will have to abide to usual styling guidelines that works for different resolutions.

Data-uris are currently not supported, so all images needs to be loaded for external sources via HTTPS.

## Active

In order for you to activate your newly created teaser you must tick this box. If you have another teaser already activated, it will be deactivated and thus making the new one take it’s place. You do not need to deactivate other teasers in order to activate a new one.

A ticked (checked) checkbox means it is activated and an unticked one means it’s deactivated.

## Teaser Markdown

For the client teaser we use markdown that converts into HTML along with the CSS. Markdown is a simple way to write things that will convert into html without actually having to know HTML. If you have ever blogged with [Ghost](http://ghost.org) or commented on [Reddit](http://reddit.com) you probably already know it. Anyway it’s really simple and you will get the gist of it in minutes.

### Why Markdown?

We chose markdown over HTML to solve issues that HTML has such as you can add or make mistakes and easily create incorrect documents. But most importantly we use markdown because of the security concerns that were raised with normal HTML input. Basically we use markdown to provide security for us and our users as we try to provide as much of freedom of expression to you, the clients, as possible.

### How do I use it?

For the markdown we use everything that is “common” to markdown documents with our special sauce on top to add ids and classes to your elements. To try your markdown a good place could be [Parsedown demo page](http://parsedown.org/demo) since that is the library we made use of. Of course, as the CSS classes and ids are an extension of that library it will not work for their demo page.

You may want to write your markdown in a seperate editor and we can recommend [Mou](http://25.io/mou/) which is a native client for the OS X operating system. If you run Windows, Linux or other operating systems there most likely exist similar software for your platform or even web-based ones.

You also have our stage environments and we recommend that you test all your teasers there first before deploying them in production.

### How do I style elements with CSS?

To style elements with CSS you may of course style tags as you are used to. But you may want to use classes or even ids in your HTML to style your document. For that we have, as mentioned above, extended the markdown converter to support that.

For example, you can add an id or class to your markdown by simply adding ```{#my-id}``` or ```{.my-class}``` by the end of the line. You may also use several classes for elements if you want to use more advanced styling like ```{.box yellow}```. If you use several classes, please note that a dot is only needed in the beginning to tell the compiler that it is a CSS class.

If you make use of classes or ids, this is simple examples of the HTML it will produce:

```
#This is a header with an id {#with-an-id}
#This is a header with a class {.with-a-class}
#This is a header with two classes {.first-class second-class}
#This is a header with two classes in one {.box.red}
#Please note the two dots {.first-class .second-class}
```

Which will render to:

```
<h1 id="with-an-id">This is a header with an id</h1>
<h1 class="with-a-class">This is a header with a class</h1>
<h1 class="first-class second-class">This is a header with two classes</h1>
<h1 class=”box.red”>This is a header with two classes in one</h1>
<h1 class=”first-class .second-class”>Please note the two dots</h1>
```

As you may see from the example given above, we tried to make it as straight forward as possible for you to use.


# How to add a new teaser for your client

To begin creating your first client teaser you have to access the self-service. For Norwegian clients go to https://payment.schibsted.no/selfservice and for Swedish clients it is https://payment.schibsted.se/selfservice. There you will have to make sure you are given the correct access before you can start, if you do not have access to self-service, but should have, you will need to contact SPiD support at support@spid.no.

When you have access, follow these steps to begin to create a client teaser:

1. Choose your merchant in the first menu.
2. When the second client menu appears, choose the client you want to add a client teaser to.
3. When the third menu-level is shown, click on “client teasers” and then click on the “new”-button.

Now you should see the display of a page like this:


![Add a new client teaser](/images/client_teaser/new_client_teaser.png)


Now, all you need to do is fill in the fields as described above in “Each input field explained”, activate and press save and it will be compiled, uploaded and served for the very next request.


# The summary of client teasers

![Client teaser summary](/images/client_teaser/client_teaser_summary.png)

As you may see on the figure above the summary of client teasers are quite simple. You may edit an existing client teaser that will create and upload a new file on our CDN so any previous cached version will hence be refreshed with a new page.

If you have a lot of teasers you also have the ability to simply delete a client teaser, note that if you delete a client teaser it is a **hard deletion** and there is no way for us to get it back. Once deleted, it stays deleted and you will have to create a complete new one if you wish to get it back as it was.
