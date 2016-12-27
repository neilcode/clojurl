(ns clojurl.middleware
  (:import [java.io InputStream]))

(defn wrap-slurp-body
  [handler]
  (fn [req]
    (if (instance? InputStream (:body req))
      (handler (update req :body slurp))
      (handler req))))
