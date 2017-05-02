(ns conduit.components.home
  (:require [rum.core :as rum]))

(def tag-items
  ["programming"
   "javascript"
   "emberjs"
   "angularjs"
   "react"
   "mean"
   "node"
   "rails"])

(def toggle-items
  [{:label "Your feed"
    :link "#/feed"
    :disabled? true}
   {:label "Global feed"
    :link "#/"
    :disabled? false}])

(def article
  {:avatar "http://i.imgur.com/Qr71crq.jpg"
    :author "Eric Simons"
    :date "January 20th"
    :likes 29
    :title "How to build webapps that scale"
    :description "This is the description for the post."
    :slug "article"})

(rum/defc TagItem [tag]
  [:a.tag-pill.tag-default {:href ""}
    tag])

(rum/defc TagList []
  [:div.taglist
    (map TagItem tag-items)])

(rum/defc Sidebar []
  [:div.sidebar
    [:p "Popular tags"]
    (TagList)])

(rum/defc Banner []
  [:div.container
    [:h1.logo-font "conduit"]
    [:p "A place to share your knowledge."]])

(rum/defc PaginationItem [{:keys [on-click active? disabled?]} label]
  [:li.page-item
    {:class
      (cond 
        disabled? "disabled"
        active? "active")}
    [:a.page-link {:on-click #(on-click label)}
      label]])

(rum/defc Pagination [{:keys [curr-page on-nav]} pages-total]
  (let [pages-left (drop (dec curr-page) pages-total)
        ln 6
        hln (/ ln 2)
        small? (<= (count pages-left) ln)]
    [:nav
      [:ul.pagination
        (PaginationItem {:on-click #(on-nav (dec curr-page))
                        :disabled? (= curr-page 1)}
                        "←")
        (map #(PaginationItem {:on-click on-nav 
                              :active? (= curr-page %)} 
                              %)
              (if small?
                (take ln pages-left)
                (take hln pages-left)))
        (when-not small?
          (PaginationItem {:disabled? true} "..."))
        (when-not small?
          (map #(PaginationItem {:on-click on-nav 
                                :active? (= curr-page %)} 
                                %) 
                (take-last hln pages-left)))
        (PaginationItem {:on-click #(on-nav (inc curr-page))
                        :disabled? (= 1 (count pages-left))}
                        "→")]]))

(rum/defc ToggleItem [{:keys [label link disabled?]}]
  [:li.nav-item
    [:a.nav-link 
      {:href link
       :class 
        (case disabled? 
          true "disabled" 
          false "active"
          nil)} 
       label]])

(rum/defc FeedToggler []
  [:div.feed-toggle
    [:ul.nav.nav-pills.outline-active
      (map ToggleItem toggle-items)]])

(rum/defc ArticlePreview 
  [{:keys [:avatar :author :date :likes :title :description :slug]}]
  [:div.article-preview
    [:div.article-meta
      [:a {:href (str "#/profile/" author)}
        [:img {:src avatar}]]
      [:div.info
        [:a.author {:href ""} author]
        [:span.date date]]
      [:button.btn.btn-outline-primary.btn-sm.pull-xs-right
        [:i.ion-heart]
        (str " " likes)]]
    [:a.preview-link {:href (str "/#/article/" slug)}
      [:h1 title]
      [:p description]
      [:span "Read more..."]]])

(rum/defcs Page <
  (rum/local 1 ::curr-page)
  [{curr-page ::curr-page}]
  (let [page-size 10
        articles (->> (repeat 100 article) (into []))
        from (* (dec @curr-page) page-size)
        to (+ from page-size)
        total-pages (range 1 (/ (inc (count articles)) page-size))]
    [:div.container.page
      [:div.row
        [:div.col-md-9
          (FeedToggler)
          (map ArticlePreview (subvec articles from to))
          (Pagination {:curr-page @curr-page
                      :on-nav #(reset! curr-page %)}
                      total-pages)]
        [:div.col-md-3
          [:div.sidebar
            [:p "Popular tags"]
            [:div.taglist
              (map TagItem tag-items)]]]]]))

(rum/defc Layout []
    [:div.home-page
      (Banner)
      (Page)])