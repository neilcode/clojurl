(ns clojurl.storage-test
 (:require
  [clojure.test :refer :all]
  [clojurl.storage :refer :all]
  [clojurl.storage.in-memory :as in-memory]))

(defn is-valid-storage
  [stg]
  (let [url "https://www.google.com"
        id "goog"]

    (testing "create-link"
      (testing "returns id when successful"
        (is (= id (create-link stg id url)))

        (testing "won't override an id that's already in use"
          (is (nil? (create-link stg id "some other url")))
          (is (= url (get-link stg id))))))

    (testing "update-link"
      (testing "returns id if successful"
        (is (= id (update-link stg id "http://new-url.com"))))

      (testing "returns nil if id is not currently used"
        (is (nil? (update-link stg "bogus" url)))))

    (testing "get-link"
      (create-link stg "foo" "bar")
      (is (= "bar" (get-link stg "foo"))))

    (testing "delete-link"
      (delete-link stg "foo")
      (is (nil? (get-link stg "foo"))))

    (testing "list-links"
      (is (= {"goog" "http://new-url.com"}
             (list-links stg))))))

(deftest in-memory-storage-test
  (let [store (in-memory/in-memory-storage)]
    (is-valid-storage store)))
