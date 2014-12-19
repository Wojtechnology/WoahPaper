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
	uuid : String,
	username : String,
	regid : String
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

app.get('/new/:uuid/:username', function(req, res){
	var me;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({'uuid' : req.params.uuid}, function(err, userFound){
		if(err){
			res.end("failure");
			return console.error(err);
		}
		if(userFound){
			res.end("uuid taken");
		}else{
			user.findOne({'username' : req.params.username}, function(err1, userFound1){
				if(err1){
					res.end("failure");
					return console.error(err1);
				}
				if(userFound1){
					res.end("username taken");
				}else{
					me = new user({uuid : req.params.uuid, username : req.params.username, regid : ""});
					me.save(function (err2, me) {
						if (err2){
							res.end("failure");
							return console.error(err2);
						}
						res.end("created user");
					});
				}
			});
		}
	});	
});

app.get('/login/:uuid', function(req, res){
	var me;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({'uuid' : req.params.uuid}, function(err, userFound){
		me = userFound;
		if(err){
			res.end("failure");
			return console.error(err);
		}
		if(!me){
			res.end("no account");
		}else{
			res.end(me.username);
		}
	});
});

app.get('/updateUser/:uuid/:username', function(req, res){
	var me;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({'username' : req.params.username}, function(err, userFound){
		if(userFound){
			res.end("user taken");
		}else{
			user.findOne({'uuid' : req.params.uuid}, function(err1, userFound1){
				if(err1){
					res.end("failure");
					return console.error(err1);
				}	
				me = userFound1;
				if(!me){
					res.end("no user");
				}else{
					me.username = req.params.username;
					me.save(function(err2, me){
						if (err2){
							res.end("failure");
							return console.error(err2);
						}
						res.end("success");
					});
				}
		
			});
		}
	});
});

app.get('/updateReg/:uuid/:regid', function(req, res){
	var me;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({'regid' : req.params.regid}, function(err, userFound){
		if(err){
			res.end("failure");
			return console.error(err);
		}
		if(userFound){
			res.end("regid taken");
		}else{
			user.findOne({'uuid' : req.params.uuid}, function(err1, userFound1){
				if(err1){
					res.end("failure");
					return console.error(err1);
				}
				me = userFound1;
				if(!me){
					res.end("no user");
				}else{
					me.regid = req.params.regid;
					me.save(function(err2, me){
						if(err2){
							res.end("failure");
							return console.error(err2);
						}
						res.end("success");
					});
				}
			});
		}
	});
});

app.get('/send/:username/:word', function(req, res){
	var me;
	res.setHeader('Content-Type', 'application/json');
	user.findOne({'username' : req.params.username}, function(err, userFound){
		if(err){
			res.end("failure");
			return console.error(err);
		}
		me = userFound;
		if(!me || me.regid === ""){
			res.end("no user")
		}else{
			res.end("success");
		}
	});
});
