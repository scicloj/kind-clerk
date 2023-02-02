(ns scicloj.kind-clerk.viewers
  (:require [nextjournal.clerk :as clerk]))


(def cytoscape-viewer
  {:pred string?
   :transform-fn clerk/mark-presented
   :render-fn '(fn [value]
                 (v/html
                  (when value
                    [v/with-d3-require {:package ["cytoscape@3.21.0"]}
                     (fn [cytoscape]
                       [:div {:style {:height "500px"}
                              :ref
                              (fn [el]
                                (when el
                                  (-> value
                                      (assoc :container el)
                                      clj->js
                                      cytoscape)))}])])))})

(def echarts-viewer
  {:pred string?
   :transform-fn clerk/mark-presented
   :render-fn
   '(fn [value]
      (v/html
       (when value
         [v/with-d3-require {:package ["echarts@5.3.2"]}
          (fn [echarts]
            [:div {:style {:height "500px"}
                   :ref (fn [el]
                          (when el
                            (let [chart (.init echarts el)]
                              (-> chart
                                  (.setOption (clj->js
                                               value))))))}])])))})
