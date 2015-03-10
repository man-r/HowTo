var crypto = require('crypto');
var shasum = crypto.createHash('sha256');
shasum.update("This is some String");
console.log(shasum.digest('HEX'));