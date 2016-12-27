(defproject clojurl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.5.0"]
                 [compojure "1.4.0"]
                 [cheshire "5.6.3"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [environ "1.0.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler clojurl.app/app
         :port 3084}
  :uberjar-name "clojurl-0.1.0-SNAPSHOT-standalone.jar"
  :profiles
  {:dev {:env {:dev true}
         :dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}
   :production {:env {:production true
                      :port 3084}}
   :uberjar {:main clojurl.app, :aot :all}})
