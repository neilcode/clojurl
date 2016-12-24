(ns clojurl.handler
  (:require [ring.util.request :as request]
            [ring.util.response :as response]
            [clojurl.storage :as st]))

(defn get-link
  [stg id]
  (if-let [url (st/get-link stg id)]
    (response/redirect url)
    (response/not-found "Sorry, that's not a valid link")))


