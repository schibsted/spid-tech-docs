var createSVG = require("./create-svg");

process.stdin.setEncoding('utf8');

var input = "";

process.stdin.on('readable', function() {
  var chunk = process.stdin.read();
  if (chunk !== null) {
    input += chunk;
  }
});

process.stdin.on('end', function() {
  console.log(createSVG(input));
});
