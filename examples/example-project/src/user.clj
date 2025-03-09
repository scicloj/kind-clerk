(ns user
  (:require [nextjournal.clerk :as clerk]))

;; Start Clerk.


(clerk/serve! {:browse? true
               :watch-paths ["kind-compatibility"]
               }
              )
