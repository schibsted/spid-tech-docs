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
    [:link {:rel "shortcut icon" :href (link/file-path request "/favicon.ico")}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])]
   [:body
    [:script "document.body.className = 'js';"]
    [:div#head
     [:div.center.pll
      [:a {:href "http://techdocs.spid.no/"} "SPiD" [:span.frill " Documentation"]] " > "
      [:a {:href "/"} "API"]
      [:form.right.prl {:action "https://www.google.com/search"}
       [:input {:type "search", :name "q", :placeholder "Search"}]
       [:input {:type "hidden", :name "sitesearch", :value "techdocs.spid.no"}]]]]
    [:div#main
     [:div.center
      [:div.bd
       [:div#logo
        [:a {:href "/"} [:img {:src (link/file-path request "/images/spid.png")}]]]
       [:div#menu
        [:a {:href "/#guides"} "Guides"] " "
        [:a.nowrap {:href "/#working-examples"} "Working examples"] " "
        [:a.nowrap {:href "/#api-reference"} "API reference"]
        [:a.nowrap {:href "https://docs.spid.no/pages/viewpage.action?pageId=2523213"} "Merchants site"]]]
      [:div#body (when split-page? {:class "split"})
       [:div.bd body]]]]
    [:div#footer
     [:div.center
      "You're reading the technical documentation for the "
      [:a.nowrap {:href "http://www.schibstedpayment.no/spid/"} "SPiD platform"]
      " by "
      [:a.nowrap {:href "http://www.schibstedpayment.no/about/"} "Schibsted Payment"]
      ". "
      [:span.nowrap "Way to go!"]]]
    (optimus.hiccup/link-to-js-bundles request ["app.js"])]))
