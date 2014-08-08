var fs = require('fs');
var vm = require('vm');
var jsdom = require('jsdom');

var libs = [
  fs.readFileSync("lib/raphael.js"),
  "var Raphael = window.Raphael;",
  "window.Raphael.prototype.renderfix = function () {};",
  fs.readFileSync("lib/ubuntu-font.js"),
  fs.readFileSync("lib/underscore.js"),
  "var name;", // undeclared global in js-sequence-diagrams
  fs.readFileSync("lib/sequence-diagram.js"),
  fs.readFileSync("sd-overwrite.js")
];

module.exports = function (input) {
  var window = jsdom.createWindow(jsdom.dom);
  var navigator = window.navigator;

  var document = jsdom.jsdom("<html><body><div id='diagram'></div></body></html>");
  window.document = document;

  document.implementation.addFeature(
    "http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1");

  var escaped = input
        .replace(/'/g, "\'")
        .replace(/\\/g, "\\\\")
        .replace(/\n/g, "\\n");

  var scripts = libs.concat([
    "var diagram = Diagram.parse('" + escaped + "');",
    "diagram.drawSVG('diagram');",
    "window.retval = document.getElementById('diagram').innerHTML;"
  ]);

  var script = vm.createScript(scripts.join("\n"), "test.js");

  window.Raphael = {};

  script.runInNewContext({
    window: window,
    document: document,
    navigator: navigator,
    console: console,
    setTimeout: setTimeout,
    setInterval: setInterval
  });

  var svg = window.retval
        .replace('xmlns="http://www.w3.org/2000/svg"', 'xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"')
        .replace(/href="#raphael/g, 'xlink:href="#raphael');

  return [
    '<?xml version="1.0" encoding="UTF-8" ?>',
    '<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">',
    svg
  ].join("\n");
};
