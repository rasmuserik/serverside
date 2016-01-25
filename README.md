# Serverside renderer

Plan: webservice for executing HTML5 webapps serverside

Status: system is now set up for executing clojurescript within phantomjs, and has nodejs runner for deploy with pm2

    (ns solsort.serverside
      (:require [cljs.core.async :refer  [>! <! chan put! take! timeout close! pipe]]))

    (js/console.log "hello world")
