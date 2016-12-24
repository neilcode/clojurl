(ns clojurl.routes
 (:require [compojure.route :as route]
           [compojure.core :refer :all]))


(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))
