(defproject serverside "0.1.0-SNAPSHOT"
  :dependencies 
  [[org.clojure/clojure "1.8.0"]
   [org.clojure/clojurescript "1.7.170"]
   [org.clojure/core.async "0.2.374"]]
  :plugins 
  [[lein-cljsbuild "1.1.2"]
   [lein-ancient "0.6.8"]
   [lein-figwheel "0.5.0-2"]
   [lein-kibit "0.1.2"]]

  :source-paths ["src"]

  :clean-targets ["target"]

  :cljsbuild 
  { :builds 
   [{:id "dist" 
     :source-paths 
     ["src"]
     :compiler 
     {:output-to "dist.js"
      :main solsort.serverside
      :optimizations :simple}}]})
