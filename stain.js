/**
 * Created by parth.mudgal on 15/07/14.
 */
var jsdom = require('jsdom');
var fs = require("fs");

fs.readdir(".", function (e, files) {
    console.log("callback", files);
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        jsdom.env({file: file, scripts: ["http://code.jquery.com/jquery.js"], done: function (err, window) {
            console.log(window.$(".whitebox").find("h2"))
        }});
    }
});