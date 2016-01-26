# Serverside renderer

Plan: webservice for executing HTML5 webapps serverside

Status: system is now set up for executing clojurescript within phantomjs, and has nodejs runner for deploy with pm2

    (ns solsort.serverside
      (:require [cljs.core.async :refer  [>! <! chan put! take! timeout close! pipe]]))

    (js/console.log "hello world")

    (-> (js/require "webserver")
        (.create)
        (.listen 
          "127.0.0.1:2345"
          (fn [req res]
Handle request, phantom-api is:

- req: method, url, httpVersion, headers, post, postRaw
- res: headers setHeader(name, value) header(name) statusCode, setCnoding(binary/utf-8), write(), writeHead(statusCode, headers), close(), closeGracefully()
            (doto res
              (.setHeader "Content-Type" "text/plain")
              (aset "statusCode" 200)
              (.write "hello")
              (.close))
            )))

