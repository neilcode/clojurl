(ns clojurl.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojurl.storage :as st]
            [clojurl.storage.in-memory :refer [in-memory-storage]]
            [clojurl.handler :refer :all]))

(deftest get-link-test
  (let [stg (in-memory-storage)
        id "test"
        url "http://www.test.com"]
    (st/create-link stg id url)
    (testing "when ID exists"
      (let [response (get-link stg id)]
        (is (= 302 (:status response)))))
    (testing "when ID does not exist"
      (let [response (get-link stg "foo")]
        (is (= 404 (:status response)))))))
