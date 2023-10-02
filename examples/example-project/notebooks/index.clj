;; # Kind-Clerk demo

(ns index
  (:require [scicloj.kindly.v4.kind :as kind]
            [scicloj.kind-clerk.api :as kind-clerk]))

(kind-clerk/setup!)

;; ## Plain data

[1 1 3]

{:x "A"
 :y "B"}


;; ## Hiccup

(kind/hiccup
 [:big 1234])

^:kind/hiccup
[:big 1234]

;; ## Markdown

(kind/md
 "
* a
* b
* c
* d")


;; ## Vega-Lite

(def vega-lite-example
  {:data {:values [{:x 1 :y 2}
                   {:x 2 :y 4}
                   {:x 3 :y 9}
                   {:x 4 :y 16}]}
   :mark :point
   :encoding {:x {:field :x :type :quantitative}
              :y {:field :y :type :quantitative}}})

(kind/vega-lite vega-lite-example)

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

(kind/cytoscape cytoscape-example)

;; ## Echarts

(def echarts-example
  {:xAxis {:data ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]}
   :yAxis {}
   :series [{:type "bar"
             :data [23 24 18 25 27 28 25]}]})

(kind/echarts echarts-example)

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
    kind/table)

(-> {:column-names [:x :y]
     :row-vectors (for [i (range 100)]
                    [i (inc i)])}
    kind/table)

(-> {:column-names [:x :y]
     :row-maps (for [i (range 100)]
                 {:x i :y (inc i)})}
    kind/table)
