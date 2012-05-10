"use strict";

// Port where we'll run the websocket server
var webSocketsServerPort = 7777;
//var webSocketsServerPort = 80; doesn't look like can have websocket on port 80 :(

// websocket and http servers
var webSocketServer = require('websocket').server;
var http = require('http');
var fs = require('fs');

var client = fs.readFileSync('html/socket-client.html');
var jquery = fs.readFileSync('html/jquery.min.js');
var taglib = fs.readFileSync('html/taglib.js');
//var taglib = fs.readFileSync('html/taglib2.js');

var log = function(s) { 
    var time = (new Date()).toString().split(' ')[4];
    console.log('\n' + time + ' SERVER: ' + s);
}

// HTTP server
var server = http.createServer(function(request, response) {
    // we serve up the client HTML and any javascript libraries used in browser context
    if(request.url.search('jquery')>=0) {
        response.writeHead(200, {'Content-Type': 'application/javascript'});
        response.end(jquery);
    }
    else if(request.url.search('taglib')>=0) {
        response.writeHead(200, {'Content-Type': 'application/javascript'});
        response.end(taglib);
    }
    else {
        response.writeHead(200, {'Content-Type': 'text/html'});
        response.end(client);
    }
});

server.listen(webSocketsServerPort, function() {
    log(" Server is listening on port " + webSocketsServerPort);
});

// WebSocket server
var wsServer = new webSocketServer({
    // WebSocket server is tied to a HTTP server. To be honest I don't understand why.
    httpServer: server
});

// This callback function is called every time someone
// tries to connect to the WebSocket server
wsServer.on('request', function(request) {
    log(' Connection from origin ' + request.origin + '.');

    // accept connection - you should check 'request.origin' to make sure that
    // client is connecting from your website
    // (http://en.wikipedia.org/wiki/Same_origin_policy)
    var connection = request.accept(null, request.origin); 
    exports.con = connection;

    log(' Connection accepted.');

    // Send ACK message to ensure client is up
    exports.send(ACK_MSG);

    // user disconnected
    connection.on('close', function(connection) {
        if (userName !== false && userColor !== false) {
            log(" Peer " + connection.remoteAddress + " disconnected.");
        }
    });

});

var cmd = function(o) { 
    var cmd = {_ME: 'EC87DB8A678F29BD'};
    for (var a in o) {
        cmd[a] = o[a]; // TODO should we deep copy ??
    }
    return cmd;
}
var ACK_MSG = cmd({html:'<B>Hello</B>', js: 'console.log("Server is talking, client is listening")'});

var RE_reduceWS = /\s{2,}/g
var RE_removeLF = /\\n/g
var send = function(o) {
    var msg = JSON.stringify(o);
    msg = msg.replace(RE_removeLF,' ');
    msg = msg.replace(RE_reduceWS, ' ');
    log('send: '+msg);
    if(exports.con) {
        exports.con.sendUTF(msg);
    }
    else {
        log('ERROR: no connections are clients listening');
    }
}

var send_js = function(s) {
    send(cmd({js: s}));
}

var send_html = function(s) {
    send(cmd({html: s}));
}

// exports
exports.cmd = cmd
exports.send = send
exports.send_js = send_js
exports.send_html = send_html

// aliases
global.send_js = send_js
global.body = function(s) { send_js('$(\'body\').html(\'' + s + '\')') }
global.client_render = function (o) { send_js("$('body').html(toTagUsing(full)("+JSON.stringify(o)+"))") }

global.h = exports.send_html
global.j = exports.send_js
global.hj = function (h,j) { send(cmd({html: h, js: j})); }



