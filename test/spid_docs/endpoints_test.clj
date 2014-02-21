(ns spid-docs.endpoints-test
  (:require [spid-docs.endpoints :refer :all]
            [midje.sweet :refer :all]))

(fact "It calculates endpoint paths and urls"
      (endpoint-url {:path "logins"}) => "/logins"
      (endpoint-path {:path "logins"}) => "/endpoints/logins")

(fact "It renders endpoint page"
      (let [page (render-page {:method "GET"
                    :name "Logins search"
                    :httpMethods
                    {:GET
                     {:name "GET"
                      :required []
                      :optional
                      ["limit" "since" "offset" "until" "ip" "status" "filters"]}}
                    :parameters
                    {:ip "Show only login attempts from this IP address"
                     :status "Filter by successful (`true`) or failed (`false`) logins"}
                    :url "/api/2/logins"
                    :pathParameters []
                    :controller "Api/2/User.logins"
                    :default_output_format "json"
                    :description "List all (or search for) logins for a client."
                    :auth-required true
                    :path "logins"
                    :valid_output_formats ["json" "jsonp"]
                    :filters ["merchant"]
                    :types
                    [{:id :login
                      :name "Login attempt object"
                      :description "This object contains details about a login attempt"
                      :type :object
                      :fields
                      [{:field :id :type :string}
                       {:field :merchantId :type :integer}
                       {:field :created :type :datetime}
                       {:field :type :type :login-type}
                       {:field :ip :type :string :description "Originating IP address"}]}
                     {:id :login-type
                      :name "Login type"
                      :description
                      "A string describing the type of login attempt. The default value is `\"normal\"`."
                      :type :string
                      :values
                      [{:value "normal"
                        :description "Login with credentials (email and password)"}
                       {:value "auto"
                        :description "Automatic login caused by a 'remember me' cookie"}
                       {:value "api"
                        :description "Login via the login API (used in native apps)"}]}]}
                   [{:id :string}
                    {:id :integer}
                    {:id :datetime
                     :description "Something"}])]
        (:title page) => "/logins"))

