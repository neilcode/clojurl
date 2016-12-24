(ns clojurl.handler
  (:require [ring.util.request :as request]
            [ring.util.response :as response]
            [clojurl.storage :as st]))

(defn handle-get-link
  [stg id]
  (if-let [url (st/get-link stg id)]
    (response/redirect url)
    (response/not-found "Sorry, that's not a valid link.")))

(defn handle-create-link
  [stg id url]
  (if (st/create-link stg id url)
    (-> (response/response id)
        (response/status 201))
    (response/not-found "Sorry, link already taken.")))

(defn handle-update-link
  [stg id new-url]
  (if (st/update-link stg id new-url)
    (response/response id)
    (response/not-found "No stored link with this id.") ))

(defn handle-delete-link
  [stg id]
  (if (st/delete-link stg id)
    (response/response id)
    (response/not-found "No stored link with this id.") ))

(defn handle-list-links
  [stg]
  (response/response (st/list-links stg)))
