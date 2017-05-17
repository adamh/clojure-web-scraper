(ns clj-scraper.core
    (:use clojure.pprint)
    (:require [net.cgrand.enlive-html :as html]
              [org.httpkit.client :as http]
              [clojure.string :as string]
              [clj-scraper.database :refer :all])
    (:gen-class))

(def base-url "http://caspcapets.shelterbuddy.com")
(def website "http://caspcapets.shelterbuddy.com/search/searchResults.asp?s=adoption&searchType=4&searchTypeId=4&animalType=2%2C3%2C93%2C85%2C15%2C16%2C86%2C73&datelostfoundmonth=5&datelostfoundday=2&datelostfoundyear=2016&tpage=1&submitbtn=Find+Animals&pagesize=45&task=view")

(defn get-dom
  "Get the DOM for a website"
  [site]
  (html/html-snippet
    (:body @(http/get site {:insecure? true}))))

(defn find-imglocs
  "If it's an image tag then get the location of the image"  
  [m]
  (if (= (:tag m) :img)
    (:src (:attrs m))
    ""))

(defn filter-links
  "Only get the anchor tags that are the animal's name"
  [m]
  (if (or (string/blank? (first (:content m))) (= (string/join (:content m)) "View Details"))
      false
      true))

(defn combine-imgslinks
  "Given an img and link data form a map containing the img, name, and details url of the animal"
  [img, link]
  { :img (if (string/blank? img) nil (str base-url img)),
    :name (nth link 0),
    :link (str base-url (nth link 1))})

(defn find-linkvalues
  "Given a map of the anchor tag extract the name and link into a vector"  
  [m]
  [(string/join (:content m)), (:href (:attrs m))])

(defn extract-imgs 
  "Get all img tags or lack of images"
  [dom]
  (html/select dom [#{:img.pic :div.no-photo-search-results}]))

(defn extract-links
  "Get all anchor tags" 
  [dom]
  (html/select dom [:td.searchResultsCell :a]))

(defn filter-arrows
  "Filter out any page links that are not >>"
  [m]
  (if (= (string/join (:content m)) ">>")
      true
      false))

(defn get-content
  "Given a dom returns a vector of maps containing details of each animal"
  [dom]
  (let [imgs (map find-imglocs (extract-imgs dom))]
     (let [links (map find-linkvalues (filter filter-links (extract-links dom)))] 
        (map combine-imgslinks imgs links))))

(defn extract-arrows
  "Extract all << or >> from the DOM"
  [dom]
  (filter filter-arrows (html/select dom [:a.SearchResultsPageLink])))

(defn get-linkfromarrows
  "Given a vector of arrows extract the href from the first on and prepend the base url"
  [arrows]
  (str base-url (:href (:attrs (nth arrows 0))))
  )

(defn do-scrape
  "Recursively scrape all search pages"
  [dom]
  (let [arrows (vec (extract-arrows dom))]
    (if (= (count arrows) 2)
    (concat (do-scrape (get-dom (get-linkfromarrows arrows))) (get-content dom))
    (get-content dom))))


(defn -main
  [& args]
    (create-db)
    (let [data (vec (do-scrape (get-dom website)))]
      (doall (map insert-record data)))
    )