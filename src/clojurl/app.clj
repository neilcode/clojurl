(ns clojurl.app
 (:require [clojurl.routes :as routes]
           [clojurl.storage.in-memory :refer [in-memory-storage]]
           [compojure.handler :refer [site]]
           [ring.adapter.jetty :as jetty]
           [environ.core :refer [env]]))

(def store (in-memory-storage)) ;; TOOD: add config to use
                                ;; db backed storage when in production

(def app (routes/shortener-routes store))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))


