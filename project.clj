(defproject clj-scraper "0.1.0-SNAPSHOT"
  :description "Scrape the caspca"
  :url "http://adamh.io"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [enlive "1.1.6"]
                 [http-kit "2.1.18"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.xerial/sqlite-jdbc "3.7.2"]]
  :main ^:skip-aot clj-scraper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
