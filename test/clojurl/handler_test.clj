(ns clojurl.handler-test
  (:require [clojure.test :refer :all]
            [clojurl.storage :as st]
            [clojurl.storage.in-memory :refer [in-memory-storage]]
            [clojurl.handler :refer :all]))


(deftest get-link-test
  (let [stg (in-memory-storage)
        id "test"
        url "http://www.test.com"]
    (st/create-link stg id url)

    (testing "when ID exists"
      (let [response (handle-get-link stg id)]
        (is (= 302 (:status response)))
        (is (= url (get-in response [:headers "Location"])))))

    (testing "when ID does not exist"
      (let [response (get-link stg "foo")]
        (is (= 404 (:status response)))))))

(deftest create-link-test
  (let [stg (in-memory-storage)
        id "goog"
        url "http://www.google.com"
        req {:params {:id id :url url}}]
    (testing "returns the id in the response"
      (let [response (handle-create-link stg req)]
        (is (= 201 (:status response)))
        (is (= id (:body response)))))))

(deftest update-link-test
  (let [stg (in-memory-storage)
        id "goog"
        url "http://www.google.com"
        new-url "https://www.googs.com"
        req {:params {:id id :new-url new-url}}
        bad-req {:params {:id "bogus" :new-url new-url}}
        ]
    (st/create-link stg id url)
    (testing "returns 200 when successful"
      (let [response (handle-update-link stg req)]
        (is (= 200 (:status response)))
        (is (= id (:body response)))))
    (testing "returns a 400 when updating a link that doesn't exist"
      (let [response (handle-update-link stg bad-req)]
        (is (= 404 (:status response)))
        (is (= "No stored link with this id." (:body response)))) )))

(deftest delete-link-test
  (let [stg (in-memory-storage)
        id "goog"
        url "http://www.google.com"
        req {:params {:id id}}]
    (st/create-link stg id url)

    (testing "deletes a link if it exists"
      (let [response (handle-delete-link stg req)]
        (is (= 200 (:status response)))
        (is (= 404 (:status (handle-get-link stg req))))))

    (testing "returns 404 if there's no link stored with given id"
      (let [response (handle-delete-link stg {:params {:id "bogus"}})]
        (is (= 404 (:status response)))
        (is (= "No stored link with this id." (:body response)))))))

(deftest list-links-test
  (let [stg (in-memory-storage)
        links {"foo" "www.foobar.com"
               "gmail" "www.gmail.com"
               "yahoo" "www.isyahoodeadyet.com"}]

    (st/create-link stg "foo" "www.foobar.com")
    (st/create-link stg "gmail" "www.gmail.com")
    (st/create-link stg "yahoo" "www.isyahoodeadyet.com")

    (testing "returns a list of stored links"
      (let [response (handle-list-links stg)]
        (is (= 200 (:status response)))
        (is (= links (:body response)))))))

