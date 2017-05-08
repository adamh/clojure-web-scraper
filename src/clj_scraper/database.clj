(ns clj-scraper.database
    (:use clojure.pprint)
    (:require [clojure.java.jdbc :as sql]))

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/database.db"
   })

(defn create-db 
  "Drop the table then recreate it."
  []
  (sql/execute! db ["drop table if exists animals"])
  (try 
    (let [cs (sql/create-table-ddl :animals
                                    [[:id :integer :primary :key :autoincrement]
                                    [:img :text]
                                    [:name :text]
                                    [:link :text]])]
        (sql/execute! db [cs]))
    (catch Exception e (println e))))
    ; We should actual do something with that exception
    ; but this is just for fun. So... ¯\_(ツ)_/¯


(defn output-all-records
  [conn]
  (sql/query conn "select * from animals"))

(defn insert-record
    "Insert a record into the animals table"
    [record]
    (sql/insert! db :animals record))