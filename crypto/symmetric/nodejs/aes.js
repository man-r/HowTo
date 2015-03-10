var crypto = require('crypto');

var password = '11111111111111111111111111111111';
function encrypt(text){
  var cipher = crypto.createCipher('aes-128-cbc',password)
  var crypted = cipher.update(text,'utf8','hex')
  crypted += cipher.final('hex');
  return crypted;
}
 
function decrypt(text){
  var decipher = crypto.createDecipher('aes-128-cbc', password)
  var dec = decipher.update(text,'hex','utf8')
  dec += decipher.final('utf8');
  return dec;
}
 
var encrypted = encrypt("This is some String")
var decrypted = decrypt(encrypted)

console.log('encrypted :', encrypted);
console.log('decrypted :', decrypted);