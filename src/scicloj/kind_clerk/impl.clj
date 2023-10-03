(ns scicloj.kind-clerk.impl
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly-advice.v1.api :as kindly-advice]
            [nextjournal.clerk :as clerk]
            [nextjournal.clerk.viewer :as clerk.viewer]
            [clojure.string :as string]
            [clojure.pprint :as pprint]
            [scicloj.kind-clerk.viewers :as viewers]))

(defn value->kind [v]
  (-> {:value v}
      kindly-advice/advise
      :kind))

(def *kinds-to-ignore (atom #{}))

(defn add-kind-to-ignore! [kind]
  (swap! *kinds-to-ignore conj kind))

(def *kind->transform
  (atom {}))

(defn add-kind-transform! [kind transform]
  (swap! *kind->transform assoc kind transform))

(let [pred (fn [v]
             (when-let [k (value->kind v)]
               (-> k
                   (@*kinds-to-ignore)
                   not)))]
  (pred (+ 1 2)))

(defn extract-kindly-context [clerk-context]
  (kindly-advice/advise
   {:form (:form clerk-context)
    :value (-> clerk-context
               :nextjournal.clerk.viewer/result
               :nextjournal/value)}))

(defn add-relevant-viewer! []
  (clerk/add-viewers!
   [{:pred (fn [clerk-context]
             (-> clerk-context
                 :nextjournal.clerk.viewer/result
                 :nextjournal/value
                 :nextjournal.clerk/var-from-def))
     :transform-fn (clerk/update-val
                    (fn [_]
                      (clerk/md "")))}
    {:pred (fn [clerk-context]
             (when-let [k (-> clerk-context
                              extract-kindly-context
                              :kind)]
               (-> k
                   (@*kinds-to-ignore)
                   not)))
     :transform-fn (clerk/update-val
                    (fn [clerk-context]
                      (let [{:keys [kind value]} (extract-kindly-context clerk-context)]
                        (if-let [transform (@*kind->transform kind)]
                          (transform value)
                          (clerk/html [:p "Unsupported kind "
                                       [:code (pr-str kind)]
                                       "."])))))}]))


(add-kind-to-ignore! :kind/image)
(add-kind-to-ignore! :kind/vector)
(add-kind-to-ignore! :kind/seq)
(add-kind-to-ignore! :kind/map)
(add-kind-to-ignore! :kind/set)

(add-kind-transform! :kind/vega-lite clerk/vl)
(add-kind-transform! :kind/vega clerk/vl)

(add-kind-transform! :kind/hiccup clerk/html)

(add-kind-transform!
 :kind/md (fn [v]
            (->> v
                 (string/join "\n")
                 clerk/md)))

(add-kind-transform!
 :kind/dataset (fn [v]
                 (clerk/html
                  [:code
                   {:class :scicloj-dataset}
                   [:style "
.scicloj-dataset {
  background-color: #fff;
}
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

;; (add-kind-transform!
;;  :kind/cytoscape (fn [v]
;;                    (clerk.viewer/with-viewer
;;                      viewers/cytoscape-viewer
;;                      v)))

;; (add-kind-transform!
;;  :kind/echarts (fn [v]
;;                  (clerk.viewer/with-viewer
;;                    viewers/echarts-viewer
;;                    v)))
