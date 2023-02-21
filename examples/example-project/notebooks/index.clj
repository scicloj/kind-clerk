(ns index
  (:require [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]
            [scicloj.kind-clerk.api :as kind-clerk]))

;; Setup Clerk for Kindly.
(kind-clerk/setup!)

;; ## Hiccup

(-> [:big 1234]
    (kindly/consider :kind/hiccup))

(-> ["
* a
* b
* c
* d"]
    (kindly/consider :kind/md))


;; ## Vega-Lite

(def vega-lite-example
  {:data {:values [{:x 1 :y 2}
                   {:x 2 :y 4}
                   {:x 3 :y 9}
                   {:x 4 :y 16}]}
   :mark :point
   :encoding {:x {:field :x :type :quantitative}
              :y {:field :y :type :quantitative}}})

(-> vega-lite-example
    (kindly/consider :kind/vega-lite))

;; ## Cytoscape

(def cytoscape-example
  {:elements {:nodes [{:data {:id "a" :parent "b"} :position {:x 215 :y 85}}
                      {:data {:id "b"}}
                      {:data {:id "c" :parent "b"} :position {:x 300 :y 85}}
                      {:data {:id "d"} :position {:x 215 :y 175}}
                      {:data {:id "e"}}
                      {:data {:id "f" :parent "e"} :position {:x 300 :y 175}}]
              :edges [{:data {:id "ad" :source "a" :target "d"}}
                      {:data {:id "eb" :source "e" :target "b"}}]}
   :style [{:selector "node"
            :css {:content "data(id)"
                  :text-valign "center"
                  :text-halign "center"}}
           {:selector "parent"
            :css {:text-valign "top"
                  :text-halign "center"}}
           {:selector "edge"
            :css {:curve-style "bezier"
                  :target-arrow-shape "triangle"}}]
   :layout {:name "preset"
            :padding 5}})

(-> cytoscape-example
    (kindly/consider :kind/cytoscape))

;; ## Echarts

(def echarts-example
  {:xAxis {:data ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]}
   :yAxis {}
   :series [{:type "bar"
             :data [23 24 18 25 27 28 25]}]})

(-> echarts-example
    (kindly/consider :kind/echarts))

;; ## Datasets

;; Currently, datasets are printed and rendered as Markdown.

(require '[tablecloth.api :as tc])

(-> {:x [1 3 5]
     :y [2 4 6]}
    tc/dataset)

;; ## Tables

;; We can also use Clerk's table view:

(-> {:x [1 3 5]
     :y [2 4 6]}
    tc/dataset
    (kindly/consider :kind/table))

(-> {:column-names [:x :y]
     :row-vectors [[1 2]
                   [3 4]
                   [5 6]]}
    (kindly/consider :kind/table))

(-> {:column-names [:x :y]
     :row-maps [{:x 1 :y 2}
                {:x 3 :y 4}
                {:x 5 :y 6}]}
    (kindly/consider :kind/table))
