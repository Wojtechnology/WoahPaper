// Module dependencies.
var application_root = __dirname,
	express = require( 'express' ), //Web framework
	path = require( 'path' ), //Utilities for dealing with file paths
	mongoose = require( 'mongoose' ); //MongoDB integration

//Add database
mongoose.connect('mongodb://localhost/woah');

var users = mongoose.connection;
users.on('error', console.error.bind(console, 'connection error:'));
users.once('open', function callback () {
	console.log('Connected to users database using mongodb');
});

//Create user model
var userSchema = mongoose.Schema({
	username : String,
	password : Number
})

var user = mongoose.model('user', userSchema);

//Create server
var app = express();

//Where to serve static content
app.use( express.static( path.join( application_root, 'site') ) );

//Start server
var port = 4711;

app.listen( port, function() {
	console.log( 'Express server listening on port %d in %s mode', port, app.settings.env );
});

app.get('/user/:username/:password', function(req, res){
	var me;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({ 'username' : req.params.username }, function(err, userFound){
		me = userFound;
		if(!me){
			me = new user({username : req.params.username, password : Number(req.params.password)});
			me.save(function (err, me) {
				if (err) return console.error(err);
				res.end("created user");
			});
		}else{
			if(me.password === Number(req.params.password)) res.end("correct password");
			else res.end("incorrect password");
		}
	});
	
});

app.get('/send/:username/:word', function(req, res){
	var you;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({ 'username' : req.params.username }, function(err, userFound){
		me = userFound;
		if(!me){
			res.end("User not found")
		}else{
			res.end("Sent word " + req.params.word);
		}
	});
});
