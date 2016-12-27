(ns clojurl.handler
  (:require [ring.util.request :as request]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-response]]
            [clojurl.storage :as st]))


(defn handle-get-link
  [stg id]
  (if-let [url (st/get-link stg id)]
    (response/redirect url)
    (response/not-found "Sorry, that's not a valid link.")))

(defn handle-create-link
  [stg id {url :body}]
  (if (st/create-link stg id url)
    (response/response (str "/links/" id))
    (-> (response/response (format "Sorry, link with id %s already taken." id))
        (response/status 422))))

(defn handle-update-link
  [stg id {new-url :body}]
  (if (st/update-link stg id new-url)
    (response/response (str "/links/" id))
    (response/not-found (format "Sorry, no link with id %s found." id)) ))

(defn handle-delete-link
  [stg id]
  (st/delete-link stg id)
  (-> (response/response "")
      (response/status 204)))

(defn handle-list-links
  [stg]
  (wrap-json-response
    (fn [_]
      (response/response (st/list-links stg)))))

