#!/bin/bash -v
killall java
lein clean 
lein cljsbuild auto &
(sleep 1; touch *.js) &
while inotifywait -e modify,close_write,move_self -q *.js
do 
  killall phantomjs
  kill `cat .pid`
  sleep 0.2
  node run-phantomjs.js $@ &
  cat `find src -name "*.cljs"`| sed -e "s/^[^/]/    \0/" | sed -e s'/^ *[;][;] \?//' > README.md
  echo $! > .pid
  sleep 3
done
