(ns clojurl.app
 (:require [clojurl.routes :as routes]
           [clojurl.storage.in-memory :refer [in-memory-storage]]
           ;[ring.middleware.defaults :refer [wrap-defaults api-defaults secure-api-defaults]]
           ;[ring.middleware.params :as params]
           ))

(def store (in-memory-storage)) ;; TOOD: add config to use
                                ;; db backed storage when in production

(def app  (routes/shortener-routes store))


