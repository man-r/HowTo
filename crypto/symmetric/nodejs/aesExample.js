var crypto = require('crypto');

var iv = new Buffer('0000000000000000');
        // reference to converting between buffers http://nodejs.org/api/buffer.html#buffer_new_buffer_str_encoding
        // reference node crypto api http://nodejs.org/api/crypto.html#crypto_crypto_createcipheriv_algorithm_key_iv
        // reference to ECB vs CBC cipher methods http://crypto.stackexchange.com/questions/225/should-i-use-ecb-or-cbc-encryption-mode-for-my-block-cipher

var encrypt = function(data, key) {
    var decodeKey = crypto.createHash('sha256').update(key, 'utf-8').digest();
    var cipher = crypto.createCipheriv('aes-256-cbc', decodeKey, iv);
    return cipher.update(data, 'utf8', 'hex') + cipher.final('hex');
};

var decrypt = function(data, key) {
    var encodeKey = crypto.createHash('sha256').update(key, 'utf-8').digest();
    var cipher = crypto.createDecipheriv('aes-256-cbc', encodeKey, iv);
    
    return cipher.update(data, 'hex', 'utf8') + cipher.final('utf8');
};

var decrypt128 = function(data, key) {
    var encodeKey = crypto.createHash('sha256').update(key, 'utf-8').digest();
    var cipher = crypto.createDecipheriv('aes-128-cbc', new Buffer(key, 'hex'), new Buffer(iv));

    return cipher.update(data, 'hex', 'utf8') + cipher.final('utf8');
};

var data = 'Here is my string'
var key = '1234567891123456';
var cipher = encrypt(data, key);
var decipher = decrypt(cipher, key);
console.log(cipher);
console.log(decipher);

// the string below was generated from the "main" in the java side
console.log(decrypt("79D78BEFC06827B118A2ABC6BD9D544E83F92930144432F22A6909EF18E0FDD1", key));
console.log(decrypt128("3EB7CF373E108ACA93E85D170C000938A6B3DCCED53A4BFC0F5A18B7DDC02499", "d7900701209d3fbac4e214dfeb5f230f"));