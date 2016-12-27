(ns clojurl.middleware-test
  (:require [clojurl.middleware :refer :all]
            [ring.mock.request :as mock]
            [clojure.test :refer :all]))

(deftest wrap-slurp-body-test
  (let [body "Hello World"
        req (mock/request :post "/foo" body)
        expected-request (assoc req :body body)
        fake-handler (wrap-slurp-body identity)]

    (testing "When request has a ByteArrayInputStream as the body"
      (let [prepared-request (fake-handler req)]

        (is (= java.io.ByteArrayInputStream (class (:body req))))

        (testing "the body is converted to a string"
          (is (= body (:body prepared-request))))

        (testing "the rest of the request is unchanged"
          (is (= expected-request prepared-request))) ))

    (testing "When request has no body"
      (let [no-body (mock/request :get "/")]
        (testing "nothing happens"
          (is (= no-body (fake-handler no-body))))))))
