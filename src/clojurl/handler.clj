(ns clojurl.handler
  (:require clojurl.routes
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))
