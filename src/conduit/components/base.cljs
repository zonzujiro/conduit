(ns conduit.components.base
    (:require [rum.core :as rum]))

(rum/defc Icon [type]
    [:i {:class (str "ion-" (name type))}])