(ns conduit.core
  (:require [rum.core :as rum]
            [conduit.components.header :refer [Header]]
            [conduit.components.footer :refer [Footer]]
            [conduit.components.home :as home]))

(rum/defc App []
  [:div
    (Header :home)
    (home/Layout)
    (Footer)])

(rum/mount (App)
           (. js/document (getElementById "app")))
