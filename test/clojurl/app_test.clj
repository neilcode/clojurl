(ns clojurl.app-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojurl.app :refer [app store]]
            [clojurl.storage :as st]
            [cheshire.core :as json]))

(deftest test-app
  (let [id "foo"
        url "www.foobar.com"]

    (testing "not-found route"
      (let [response (app (mock/request :get "/invalid"))]
        (is (= (:status response) 404))))

    (testing "create-link"
      (app (mock/request :delete (str "/links/" id))) ;; make sure nothing is already stored
      (let [request (mock/request :post (str "/links/" id) url)
            response (app request)]

        (is (= 200 (:status response)))
        (is (= (str "/links/" id) (:body response)))))

    (testing "get-link"
      (let [request (mock/request :get (str "/links/" id))]

        (testing "When link exists"
          (st/create-link store id url)

          (let [response (app request)]
            (testing "redirects to expected url"
              (is (= 302 (:status response)))
              (is (= url (get-in response [:headers "Location"]))))))

        (testing "when link doesn't exist"
          (st/delete-link store id)

          (let [response (app request)]
            (testing "returns 404"
              (is (= 404 (:status response))))))))

    (testing "update-link"
      (let [new-url "www.newbar.com"
            request (mock/request :put (str "/links/" id) new-url)]

        (testing "when the link exists"
          (st/create-link store id url)
          (let [response (app request)]

            (testing "returns a 200"
              (is (= 200 (:status response))))) )

        (testing "when the link does not exist"
          (st/delete-link store id)
          (let [response (app request)]
            (testing "returns a 404"
              (is (= 404 (:status response))))))) )

    (testing "delete-link"
      (st/create-link store id url)

      (let [path (str "/links/" id)
            request (mock/request :delete path)
            response (app request)]

        (testing "Returns a 204"
          (is (= 204 (:status response))))

        (testing "The link is truly deleted"
          (is (= 404 (-> (app (mock/request :get path))
                         :status)))
          )
        )
      )

    (testing "list-links"
      (st/create-link store id url)
      (let [request (mock/request :get "/links")
            response (app request)]
        (testing "returns JSON"
          (is (= "application/json; charset=utf-8"
                 (get-in response [:headers "Content-Type"]) )))

        (testing "lists stored links"
          (is (= {id url} (json/decode (:body response)))))) )))
