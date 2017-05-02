(ns conduit.components.header
  (:require [rum.core :as rum]))

(def nav-items
  [{:link "#/" 
    :label "Home" 
    :route :home}
   {:label "New Post"
    :route :new-post
    :icon "ion-compose"}
   {:label "Settings"
    :route :settings
    :icon "ion-gear-a"}
   {:label "Sign up"
    :route :sign-up}])

(rum/defc NavItem [current {:keys [link label icon route]}]
  [:li.nav-item 
    (when (= route current)
      {:class "active"})
    [:a.nav-link {:href link}
      (when icon [:i {:class icon}])
      (when icon " ")
      label]])

(rum/defc Header [current]
  [:nav.navbar.navbar-light
    [:div.container
      [:a.navbar-brand { :href "/" } "conduit" ]
        [:ul.nav.navbar-nav.pull-xs-right
          (map #(NavItem current %) nav-items)]]])