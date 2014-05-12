(ns spid-docs.layout
  "Create full web pages as HTML from page maps."
  (:require [hiccup.page :refer [html5]]
            [optimus.hiccup]
            [optimus.link :as link]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(defn layout-page
  "Takes a request and a map with keys title and body (known as 'a page'), and
   returns a full HTML page. The title from the page map is used in the page's
   title tag."
  [request {:keys [title body split-page?]}]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title (str title " | SPiD API Documentation")]
    [:link {:rel "shortcut icon" :href "/favicon.ico"}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])]
   [:body
    [:script "document.body.className = 'js';"]
    [:div#head
     [:div.center.pll
      [:a {:href "https://docs.spid.no/"} "SPiD Documentation"] " > "
      [:a {:href "/"} "API"]
      [:form.right.prl {:action "https://www.google.com/search"}
       [:input {:type "search", :name "q", :placeholder "Search"}]
       [:input {:type "hidden", :name "sitesearch", :value "preview-docs.spid.no"}]]]]
    [:div#main
     [:div.center
      [:div.bd
       [:div#logo
        [:a {:href "/"} [:img {:src (link/file-path request "/images/spid.png")}]]]
       [:div#menu
        [:a {:href "/#guides"} "Guides"] " "
        [:a.nowrap {:href "/#working-examples"} "Working examples"] " "
        [:a.nowrap {:href "/#api-reference"} "API reference"]]]
      [:div#body (when split-page? {:class "split"})
       [:div.bd body]]]]
    [:div#footer
     [:div.center
      "This is the technical documentation for the "
      [:a {:href "http://www.schibstedpayment.no/spid/"} "SPiD platform"]
      " by "
      [:a {:href "http://www.schibstedpayment.no/about/"} "Schibsted Payment"]
      "."]]
    (optimus.hiccup/link-to-js-bundles request ["app.js"])
    [:script
     "(function() {if (!document.getElementById('disqus_thread')) {return}var dsq = document.createElement('script');dsq.type = 'text/javascript';dsq.async = true;
dsq.src = '//spidtechdocs.disqus.com/embed.js';
(document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);}())"]]))
