(ns scicloj.kind-clerk.api
  (:require [scicloj.kind-clerk.impl :as impl]))

(defn setup! []
  (impl/add-relevant-viewer!)
  :ok)
