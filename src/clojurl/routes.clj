(ns clojurl.routes
 (:require [compojure.route :as route]
           [compojure.core :refer :all]
           [clojurl.middleware :as mw]
           [clojurl.handler :as h]))


(defn shortener-routes
  [stg]
  (-> (routes
        (GET "/links/:id" [id :as request] (h/handle-get-link stg id))
        (POST "/links/:id" [id :as request] (h/handle-create-link stg id request))
        (PUT "/links/:id" [id :as request] (h/handle-update-link stg id request))
        (DELETE "/links/:id" [id :as request] (h/handle-delete-link stg id))
        (GET "/links" [:as request] ((h/handle-list-links stg) request))
        (route/not-found "Not Found"))
      (wrap-routes mw/wrap-slurp-body)))
