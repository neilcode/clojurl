(ns clojurl.storage)

(defprotocol Storage
 (create-link [this id url]
  "Stores a url with the given ID, returns nil if ID already taken.")
 (update-link [this id new-url]
  "Updates the link with a new URL if update was successful, or nil if the ID is not in use.")
 (delete-link [this id]
  "Deletes the link stored with the ID")
 (list-links [this]
  "Returns a map of all stored links")
 (get-link [this id]
  "Returns the URL stored with the given ID or nil if the ID does not exist."))
