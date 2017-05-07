(ns clj-scraper.core-test
  (:require [clojure.test :refer :all]
            [clj-scraper.core :refer :all]
            [net.cgrand.enlive-html :as html]))

(defn get-testdom
    []
    (html/html-resource "example_page.htm"))

(deftest test-extract-imgs
  (testing "extract-imgs"
    (is (= 15 (count (extract-imgs (get-testdom)))))))

(deftest test-links
  (testing "extract-links and filter links"
    (is (= 15 (count (filter filter-links (extract-links (get-testdom))))))))

(deftest test-arrows
  (testing "extract-arrows"
    (is (= 2 (count (extract-arrows (get-testdom)))))))

(deftest test-get-content
  (testing "get-content"
    (is (= 15 (count (get-content (get-testdom)))))))