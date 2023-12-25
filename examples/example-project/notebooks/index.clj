;; # Kind-Clerk demo

(ns index
  (:require [scicloj.kindly.v4.kind :as kind]
            [scicloj.kind-clerk.api :as kind-clerk]))

(kind-clerk/setup!)

;; ## Plain data

[1 1 3]

{:x "A"
 :y "B"}

;; ## Images
(import
 java.net.URL)

(def clj-img
  (->  "https://clojure.org/images/clojure-logo-120b.png"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

clj-img

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

(kind/vega-lite
 {:data {:values [{:x 1 :y 2}
                  {:x 2 :y 4}
                  {:x 3 :y 9}
                  {:x 4 :y 16}]}
  :mark :point
  :encoding {:x {:field :x :type :quantitative}
             :y {:field :y :type :quantitative}}})

;; ## Cytoscape

(kind/cytoscape
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

;; ## Echarts

(kind/echarts
 {:xAxis {:data ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]}
  :yAxis {}
  :series [{:type "bar"
            :data [23 24 18 25 27 28 25]}]})

;; ## Datasets

;; Currently, datasets are printed and rendered as Markdown.

(require '[tablecloth.api :as tc])

(-> {:x [1 3 5]
     :y (repeatedly 3 rand)}
    tc/dataset
    (tc/set-dataset-name "my data"))

;; ## Tables

;; We can also use Clerk's table view:

(-> {:x (range 100)
     :y (repeatedly 100 rand)}
    tc/dataset
    kind/table)

(-> {:column-names [:x :y]
     :row-vectors (for [i (range 5)]
                    [i (rand)])}
    kind/table)

(-> {:column-names [:x :y]
     :row-maps (for [i (range 5)]
                 {:x i :y (rand)})}
    kind/table)

;; ### Nesting kinds in tables (WIP)

(-> {:column-names [:x :y]
     :row-vectors [[1 (rand)]
                   [2 [3 4]]
                   [3 {:x 5
                       :y 6}]
                   [4 clj-img]]}
    kind/table)
