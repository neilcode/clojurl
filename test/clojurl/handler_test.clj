(ns clojurl.handler-test
  (:require [clojure.test :refer :all]
            [clojurl.storage :as st]
            [clojurl.storage.in-memory :refer [in-memory-storage]]
            [clojurl.handler :refer :all]
            [ring.mock.request :as mock]
            [cheshire.core :as json]))


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
      (let [response (handle-get-link stg "foo")]
        (is (= 404 (:status response)))))))

(deftest create-link-test
  (let [stg (in-memory-storage)
        id "goog"
        url "http://www.google.com"
        req (-> (mock/request :post "/links/goog" url)
                (update :body slurp))]

    (testing "when the id does not already exist"
      (let [response (handle-create-link stg id req)]
        (testing "returns a 200 response"
          (is (= 200 (:status response))))
        (testing "returns a URI of the shortened url"
          (is (= (str "/links/" id) (:body response))))
        (testing "actually stores the url"
          (is (= url (st/get-link stg id))))))


    (testing "when id is already taken"
      (st/create-link stg id url)
      (let [response (handle-create-link stg id req)]
        (testing "Returns a 422 status"
          (is (= 422 (:status response))))
        (testing "Returns an error message in the body"
          (is (= (str "Sorry, link with id " id " already taken.")
               (:body response))))))))

(deftest update-link-test
  (let [stg (in-memory-storage)
        id "goog"
        url "http://www.google.com"
        new-url "https://www.googs.com"
        req (-> (mock/request :put (str "/links/" id) new-url)
                (update :body slurp))]

    (testing "When a URL is stored under the given id"
      (st/create-link stg id url)
      (let [response (handle-update-link stg id req)]
        (testing "Returns 200 status"
          (is (= 200 (:status response))))
        (testing "Returns the id in the body"
          (is (= (str "/links/" id) (:body response))))
        (testing "Link is actually updated"
          (is (= new-url (st/get-link stg id))))))
    (testing "When there is no URL stored under the given id"
      (let [response (handle-update-link stg "bogus-id" req)]
        (testing "Returns a 404 status"
          (is (= 404 (:status response))))
        (testing "Body contains an error message with the unfound id"
          (is (= (str "Sorry, no link with id bogus-id found.")
               (:body response))))))))

(deftest delete-link-test
  (let [stg (in-memory-storage)
        id "goog"
        url "http://www.google.com"]

    (testing "Link exists"
      (st/create-link stg id url)
      (let [response (handle-delete-link stg id)]
        (testing "Returns a 204 status"
          (is (= 204 (:status response))))
        (testing "Link is actually deleted"
          (is (nil? (st/get-link stg id))))))

    (testing "Link does not exist"
      (let [response (handle-delete-link stg "bogus")]
        (testing "Returns 204 status"
          (is (= 204 (:status response))))))))

(deftest list-links-test
  (let [stg (in-memory-storage)
        links {"foo" "www.foobar.com"
               "gmail" "www.gmail.com"
               "yahoo" "www.isyahoodeadyet.com"}]

    (doseq [[id url] links]
      (st/create-link stg id url))

    (let [handler (handle-list-links stg)
          response (handler (mock/request :get "/links"))]

      (testing "returns a 200 HTTP status"
        (is (= 200 (:status response))))

      (testing "returns result of list-links encoded in JSON"
        (is (= links (json/decode (:body response))))))))
