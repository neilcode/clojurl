(ns clojurl.storage.in-memory
 (:require [clojurl.storage :refer :all]))

(defn create-link*
  [!stg id url]
  (when-not (contains? @!stg id)
    (swap! !stg assoc id url)
    id))

(defn get-link*
  [!stg id]
  (get @!stg id))

(defn update-link*
  [!stg id new-url]
  (when (contains? @!stg id)
    (swap! !stg assoc id new-url)
    id))

(defn delete-link*
  [!stg id]
  (if (contains? @!stg id)
    (swap! !stg dissoc id)))

(defn list-links*
  [!stg]
  @!stg)

(defn in-memory-storage
  []
  (let [!stg (atom {})]
    (reify Storage
      (create-link [_ id url] (create-link* !stg id url))
      (get-link [_ id] (get-link* !stg id))
      (update-link [_ id new-url] (update-link* !stg id new-url))
      (delete-link [_ id] (delete-link* !stg id))
      (list-links [_] (list-links* !stg)))))



