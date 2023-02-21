(ns user
  (:require [nextjournal.clerk :as clerk]
            [scicloj.kind-clerk.api :as kind-clerk]
            [scicloj.kindly-default.v1.api :as kindly-default]))

;; Initialize Kindly's [default](https://github.com/scicloj/kindly-default).
(kindly-default/setup!)

;; Start Clerk.
(clerk/serve! {:browse? true})
