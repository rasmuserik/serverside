;; # Serverside renderer
;;
;; Status: system is now set up for executing clojurescript within phantomjs, and has nodejs runner for deploy with pm2
;;
(ns solsort.serverside
  (:require [cljs.core.async :refer  [>! <! chan put! take! timeout close! pipe]]))

(def page-cache (atom '()))

;; ## JavaScript function running within page in phantomjs
;;
;; This just sets a callback which calls back to the server
(def phantom-function
  "function () {
  function done() {
  if (window.onSolsortReady) { 
  window.onSolsortReady = undefined;
  window.callPhantom ({
  head: document.head.innerHTML,
  body: document.body.innerHTML});
  }}
  window.onSolsortReady = done;
  setTimeout(done, 3000); 
  }")


;; ## Actual server
(-> (js/require "webserver")
    (.create)
    (.listen 
      "127.0.0.1:2345"
      (fn [req res]
        ;; Handle request, phantom-api is:
        ;; 
        ;; - req: method, url, httpVersion, headers, post, postRaw
        ;; - res: headers setHeader(name, value) header(name) statusCode, setCnoding(binary/utf-8), write(), writeHead(statusCode, headers), close(), closeGracefully()
        (let [url (-> (.-url req)
                      (.slice 1)
                      (.replace "/" "://")
                      (.replace "?" "#"))
              page (peek @page-cache)
              page (if page
                     (do (swap! page-cache pop) page)
                     (.create (js/require "webpage")))
              sent (atom false)
              t0 (js/Date.now)
              ]
          (.open 
            page url
            (fn [status]
              (aset 
                page "onCallback"
                (fn [data]
                  (when-not @sent
                    (reset! sent true)
                    (doto res
                      (.setHeader "Content-Type" "text/html")
                      (aset "statusCode" 200)
                      (.write 
                        (str
                          "<!DOCTYPE html><html><head>"
                          (aget data "head")
                          "</head><body>"
                          (aget data "body")
                          "</body></html>" ))

                      (.close))
                    (aset page "onCallback" nil)
                    (swap! page-cache conj page))))
              (.evaluateJavaScript page phantom-function)
              ))))))

