(ns clojurl.app
 (:require [clojurl.routes :as routes]
           [clojurl.middleware :as middleware]
           [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
           [clojurl.handler :as handler]))

(def app
  (wrap-defaults clojurl.routes/app-routes site-defaults))
