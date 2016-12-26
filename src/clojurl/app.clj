(ns clojurl.app
 (:require [clojurl.routes :as routes]
           [clojurl.middleware :as middleware]
           [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
           [ring.middleware.params :as params]
           [ring.mock.request :as mock]
           [clojurl.handler :as handler]))

(def app
  (wrap-defaults clojurl.routes/app-routes api-defaults))


