

var pubKey = 'YH9GsEZnKvm62gSeqqh0OlcFHwY';
var prvKey = '_f2sLcWnkwtECOzF9IRK84uXD2Q';


var express = require('express');
var bodyParser = require('body-parser');
var session = require('express-session');
var cookieParser = require('cookie-parser');


var oauth = require('oauthio');


var app = express();

app.use(express.static('public'));
app.use(bodyParser());
app.use(cookieParser());
app.use(session({secret: 'keyboard cat', key: 'sid'}));


oauth.initialize(pubKey,prvKey);

app.get('/oauth/token', function (req, res) {

	var token = oauth.generateStateToken(req);

	req.json({
		token: token
	});
});

app.post('/oauth/signing', function (req, res) {
	var code = req.body.code;

	oauth.auth('google', req.session, {
		code: code
	})
	.then(function (r) {
		res.send(200, 'Success');
	})
	.fail(function (e) {
		console.log(e);
		res.send(500, 'An error occured');
	})
});

app.get('/me', function (req, res) {
	oauth.auth('google', req.session)
	.then(function (request_object) {
		return request_object.me();
	})
	.then(function (r) {
		res.json(r);
	})
	.fail(function (e) {
		console.log(e);
		res.send(500, 'An error occured');
	})
})