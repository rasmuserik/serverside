console.log('starting phantomjs');
var process = require('child_process').spawn("phantomjs", ["./dist.js", "7007"]);
process.stdout.on('data', function(s) { console.log(String(s).replace(/\n?$/, ""));});
process.stderr.on('data', function(s) { console.error(String(s).replace(/\n?$/, ""));});
process.on('exit', function(code) { process.exit(code); });
