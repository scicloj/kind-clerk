(ns scicloj.kind-clerk.impl
  (:require [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]
            [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as clerk.viewer]
            [clojure.string :as string]
            [clojure.pprint :as pprint]
            [scicloj.kind-clerk.viewers :as viewers]))


(defn value->kind [v]
  (-> {:value v}
      kindly/advice
      last
      :kind))

(def *kinds-to-ignore (atom #{}))

(defn add-kind-to-ignore! [kind]
  (swap! *kinds-to-ignore conj kind))

(def *kind->transform
  (atom {}))

(defn add-kind-transform! [kind transform]
  (swap! *kind->transform assoc kind transform))

(defn add-relevant-viewer! []
  (clerk/add-viewers!
   [{:pred (fn [v]
             (when-let [k (value->kind v)]
               (-> k
                   (@*kinds-to-ignore)
                   not)))
     :transform-fn (clerk/update-val
                    (fn [v]
                      (let [kind (value->kind v)]
                        (if-let [transform (@*kind->transform kind)]
                          (transform v)
                          (clerk/html [:p "Unsupported kind "
                                       [:code (pr-str kind)]
                                       "."])))))}]))


(add-kind-to-ignore! :kind/buffered-image)

(add-kind-transform!
 :kind/vega-lite clerk/vl)

(add-kind-transform!
 :kind/vega clerk/vl)

(add-kind-transform!
 :kind/hiccup clerk/html)

(add-kind-transform!
 :kind/md (fn [v]
            (if (sequential? v)
              (->> v
                   (string/join "\n")
                   clerk/md)
              (-> v
                  str
                  clerk/md))))

(add-kind-transform!
 :kind/dataset (fn [v]
                 (clerk/html
                  [:code {:class :scicloj-dataset}
                   [:style "
.scicloj-dataset th {
  padding: 2px;
}
.scicloj-dataset td {
  padding: 2px;
}
.scicloj-dataset th {
  background-color: #ddd;
}
.scicloj-dataset tr:nth-child(even) {
  background-color: #f6f6f6;
}
"]
                   (-> v
                       pprint/pprint
                       with-out-str
                       clerk/md)])))

(add-kind-transform!
 :kind/table
 (fn [dataset-or-options]
   (if (-> dataset-or-options
           class
           str
           (= "class tech.v3.dataset.impl.dataset.Dataset"))
     (nextjournal.clerk/table
      {:head (-> dataset-or-options keys)
       :rows (->> dataset-or-options
                  vals
                  (apply map vector))})
     (let [{:keys [row-maps row-vectors column-names]} dataset-or-options]
       (assert column-names)
       (if row-vectors
         (nextjournal.clerk/table
          {:head column-names
           :rows row-vectors})
         (do
           (assert row-maps)
           (nextjournal.clerk/table
            {:head column-names
             :rows (->> row-maps
                        (map #(->> column-names
                                   (map %))))})))))))

(add-kind-transform!
 :kind/cytoscape (fn [v]
                   (clerk.viewer/with-viewer
                     viewers/cytoscape-viewer
                     v)))

(add-kind-transform!
 :kind/echarts (fn [v]
                 (clerk.viewer/with-viewer
                   viewers/echarts-viewer
                   v)))
