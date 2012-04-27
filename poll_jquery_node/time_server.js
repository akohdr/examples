var http = require('http');
var fs = require('fs');

var host = require('os').hostname();
var port = 1337;

var clientHTML = fs.readFileSync('client.html'); //cached
var jquery = fs.readFileSync('jquery.min.js');

http.createServer(function (req, res) {
  if (req.url.search('time')>0) {
    console.log('time request from '+ req.connection.remoteAddress)
    res.writeHead(200, {'Content-Type': 'application/json'});
    res.end('{"time": "'+new Date()+'"}');
  }
  else if (req.url.search('jquery')>0) {
    res.writeHead(200, {'Content-Type': 'text/javascript'});
    res.end(jquery);
  }
  else {
    console.log('Client request:\n\n');
    console.log(req);
    res.writeHead(200, {'Content-Type': 'text/html'});
    var clientHTML = fs.readFileSync('client.html'); // non-cached for dev
    res.end(clientHTML);
  }
}).listen(port, host);

console.log('Server running at http://'+host+':'+port+'/');

