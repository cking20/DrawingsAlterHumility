const store = {
	// host: 'http://192.168.254.198:4567/game',
	// host: 'http://localhost:4567/game',
	host: 'https://drawings-alter-humility.herokuapp.com/game', //the address of the server host *with /game appended*
	state:{						//The core state of the application
		currentVue: 'LandingVue',	//the Vue that the application should display
		reviewingBooklet: 0,		//index of booklet being reviewed. for use in review
		tempImageData:null,			//temporary image data 
		prompts: null,				//a list of strings
		myData:{},					//data kept about the player
		myLobby:null,				//object representing the state of the lobby
		knownLobbies:[],			//a list of known available lobby objects
		mustRefresh: false,			//weither the application is in a state requiring a response from the server
	},

	/**
	@Description: sets the state to mustRefresh	 
	*/
	triggerLoadingScreen: function(){
		this.state.mustRefresh =true;
	},

	/**
	JUST FOR TESTING
	
	testImage: function(dataURL, callbackFunction){
		//help found from https://stackoverflow.com/questions/19032406/convert-html5-canvas-into-file-to-be-uploaded
		var blobBin = atob(dataURL.split(',')[1]);
		var array = [];
		for(var i = 0; i < blobBin.length; i++) {
		    array.push(blobBin.charCodeAt(i));
		}
		var file = new Blob([new Uint8Array(array)], {type: 'image/png'});
		var formdata = new FormData();
		formdata.append("uploaded_file", file);
		post(this.host + '/images/0/0/0', formdata, function(xhttp){
			console.log(xhttp);
			callbackFunction();
		});
	},
	//todo This is just for testing, will need to do waaaay more than this
	refreshImage: function(callbackFunction){
		var data;
		hitImage('GET', this.host + '/images/testimage', function(xhttp){
			// console.log(xhttp.responseText);
			// var dataURL="data:image/png;base64,"+xhttp.responseText;
			var arr = new Uint8Array(xhttp.response);
			var raw = String.fromCharCode.apply(null,arr);
			var dataURL=btoa(raw);
			callbackFunction("data:image/png;base64,"+(dataURL));
		});
	},

	*/

	/**
	Loads the previously submitted image in the current booklet
	@param callbackFunction is run after a response is recieved from the server
	*/
	loadImage: function(callbackFunction){
		var data;
		hitImage('GET', this.host + '/images'+ this.getLastContentOfMyBooklet(), function(xhttp){
			var arr = new Uint8Array(xhttp.response);
			var raw = String.fromCharCode.apply(null,arr);
			var dataURL=btoa(raw);
			callbackFunction("data:image/png;base64,"+(dataURL));
		});
	},

	/**
	@Description: Loads a specific image from the host
	@param url: the final part of the path to the image. example: "/0/0/1"
	@param callbackFunction: the function to be called when a response is recieved from the server
		Should take a parameter of a base64 encoded image
	*/
	loadSpecificImage: function(url, callbackFunction){
		var data;
		hitImage('GET', this.host + '/images'+ url, function(xhttp, element){
			var arr = new Uint8Array(xhttp.response);
			var raw = String.fromCharCode.apply(null,arr);
			var dataURL=btoa(raw);
			callbackFunction("data:image/png;base64,"+(dataURL));
		});
	},

	/**
	@Description: Submits, and uploads an image to the server using the '/me/submitdrawing' endpoint
	@param dataURL: the dataURL of the image to be uploaded
	@param callbackFunction: the function to be called when a response is recieved from the server
	*/
	submitImage: function(dataURL, callbackFunction){
		//help found from https://stackoverflow.com/questions/19032406/convert-html5-canvas-into-file-to-be-uploaded
		var blobBin = atob(dataURL.split(',')[1]);
		var array = [];
		for(var i = 0; i < blobBin.length; i++) {
		    array.push(blobBin.charCodeAt(i));
		}
		var file = new Blob([new Uint8Array(array)], {type: 'image/png'});
		var formdata = new FormData();
		formdata.append("uploaded_file", file);
		post(this.host + '/me/submitdrawing', formdata, function(xhttp){
			callbackFunction();
		});
	},

	/**
	@Description: Uploads an image to the server inorder to be shared to Twitter using the '/images/share' endpoint
	@param dataURL: the dataURL of the image to be uploaded
	@param callbackFunction: the function to be called when a response is recieved from the server
	*/
	shareImage: function(dataURL, callbackFunction){
		//help found from https://stackoverflow.com/questions/19032406/convert-html5-canvas-into-file-to-be-uploaded
		var blobBin = atob(dataURL.split(',')[1]);
		var array = [];
		for(var i = 0; i < blobBin.length; i++) {
		    array.push(blobBin.charCodeAt(i));
		}
		var file = new Blob([new Uint8Array(array)], {type: 'image/png'});
		var formdata = new FormData();
		formdata.append("uploaded_file", file);
		post(this.host + '/images/share', formdata, function(xhttp){
			callbackFunction();
		});
	},

	/**
	@Description: Submits, and uploads a guess to the server using the '/me/submitguess' endpoint
	@param guess: the string guess to be uploaded to the server
	*/
	submitGuess: function(guess){
		var guessJSON = '{"guess": "' + guess + '"}';
		post(this.host + '/me/submitguess', guessJSON, function(xhttp){
			// store.state.myData = JSON.parse(xhttp.responseText);
		});
	},

	/**
	@Description: Tests the connection to the backend, and prints the response on success
	*/
	test: function(){
		var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
				console.log(this.responseText);
			}
		};
		xhttp.open('GET', this.host + '/hello', true);
		xhttp.send();
	},

	/**
	@Description: Queries the state of the current lobby from the server using the '/me/lobby' endpoint
	Updates the state of the lobby on success
	Sets the current lobby to null on error
	*/
	refreshMyLobbyData: function(){
		hitWithFail('GET', this.host + '/me/lobby', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
			store.state.mustRefresh = false;
		}, function(xhttp){
			console.log(xhttp);
			console.log("refresh failure");
			store.state.myLobby = null;
			console.log("lobby now: "+ store.state.myLobby);
			//error
		});
		vueStateTransition();
	},

	/**
	@Description: Gets a list of available lobbies from the server using the '/lobbies' endpoint
	Updates the state of knownLobbies 
	*/
	getAvailableLobbies: function(){
		hit('GET', this.host + '/lobbies', function(xhttp){
			store.state.knownLobbies = JSON.parse(xhttp.responseText);
			store.state.mustRefresh = false;
		});
		vueStateTransition();
	},

	/**
	@Description: Requests the player's data from the server using the '/me' endpoint
	Updates the state of the players data  on success
	Sets the players data to null on error
	*/
	getPlayerData: function(){
		hit('GET', this.host + '/me', function(xhttp){
			store.state.myData = JSON.parse(xhttp.responseText);
		}, function(xhttp){
			store.state.myData = null;
		});
		vueStateTransition();
	},

	/**
	@Description: Posts the player's new name the server using the '/me/name' endpoint
	@param name: String name of the player to send to the server
	Updates myData to the server's response
	*/
	changeName: function(name){
		var nameJSON = '{"name": "' + name + '"}';
		console.log("sending: "+nameJSON);
		post(this.host + '/me/name', nameJSON, function(xhttp){
			console.log(xhttp.responseText);
			store.state.myData = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	},

	/**
	@Description: Posts a player's vote to the server using the '/me/vote' endpoint
	@param: booklet: the specific booklet to vote on 
	@param round: the specific round to vote on
	*/
	vote: function(booklet, round){
		var voteJSON = '{"booklet": "' + booklet + '",'+
						'"round": '+ round+'}';
		post(this.host + '/me/vote', voteJSON, function(xhttp){
			//store.state.myData = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	},

	/**
	@Description: Requests that a player be added to a specific lobby from the server 
		using the '/lobbies/{ID}/join' endpoint
	@param: lobbyID: the specific lobby to join 
	@param password: optional string password to allow the user to join a private lobby
	Updates myLobby data on response from the server
	*/
	joinLobby: function(lobbyID, password){
		store.state.mustRefresh = true;
		var passwordJSON = '{"password": "' + password + '"}';
		post(this.host + '/lobbies/'+ lobbyID + '/join', passwordJSON, function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
			store.state.mustRefresh = false;
		});
		vueStateTransition();
	},

	/**
	@Description: Requests that a player be removed from the lobby they are currently in 
		from the server using the '/lobbies/{ID}/leave' endpoint
	Sets myLobby data to null on respose from the server
	*/
	leaveLobby: function(){
		store.state.mustRefresh = true;
		hit('POST', this.host + '/lobbies/'+ store.state.myLobby.id + '/leave', function(xhttp){
			store.state.mustRefresh = false;
			store.state.myLobby = null;
		});
		vueStateTransition();
	},
	kickPlayer: function(name){
		var nameJSON = '{"name": "' + name + '"}';
		console.log(nameJSON);
		post(this.host + '/lobbies/'+ store.state.myLobby.id + '/kick', nameJSON, function(xhttp){
		});
	},
	/**
	@Description: Requests that the current lobby be restarted 
		from the server using the '/lobbies/{ID}/restart' endpoint
	*/
	restartLobby: function(){
		store.state.mustRefresh = true;
		hit('POST', this.host + '/lobbies/'+ store.state.myLobby.id + '/restart', function(xhttp){
			store.state.mustRefresh = false;
		});
		vueStateTransition();

	},

	/**
	@Description: Posts new settings for the current lobby to the server using the '/lobbies/{ID}' endpoint
	@param: lobbySettings: JSON object representing the lobby's settings 
	Updates myLobby with the respose data from the server
	*/
	updateLobbySettings: function(lobbySettings){
		console.log(JSON.stringify(lobbySettings));
		post(this.host + '/lobbies/'+lobbySettings.id, JSON.stringify(lobbySettings), function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
			store.state.mustRefresh = false;
		});
	},

	/**
	@Description: Posts a request that the server release a new lobby with the player as admin using the 
	'/lobbies' endpoint
	Updates myLobby data on response from the server
	*/
	createNewLobby: function(){
		hit('POST', this.host + '/lobbies', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	},

	/**
	@Description: Returns myLobby data
	*/
	getMyLobbyData: function(){
		return store.state.myLobby;
	},

	/**
	@Description: Posts a request that the server start the current lobby's game using the 
	'/lobbies/{ID}/begin' endpoint
	Updates myLobby data on response from the server
	*/
	beginGame: function(){
		store.state.mustRefresh = true;
		hit('POST', this.host + '/lobbies/'+store.state.myLobby.id+'/begin', function(xhttp){
			// store.state.myLobby = JSON.parse(xhttp.responseText);
			store.state.mustRefresh = false;
		});
		vueStateTransition();
	},

	/**
	@Description: Requests a list of prompts from the server using the 
	'/prompts' endpoint
	Updates prompts data on response from the server
	*/
	getPrompts: function(){
		hit('GET', this.host + '/prompts', function(xhttp){
			store.state.prompts = JSON.parse(xhttp.responseText);
			store.state.mustRefresh = false;
		});
	},

	/**
	@Description: Returns the previous submission in the current booklet 
	@return: the content of the last page in the player's current booklet
	*/
	getLastContentOfMyBooklet: function(){
		var currentBooklet = store.state.myLobby.playerData[store.state.myData.id].currentBooklet;
		var lastRoundNum = store.state.myLobby.roundNumber - 1;
		var content = currentBooklet.pages[lastRoundNum].content;
		return content;
	},

	/**
	@Description: Returns whether the player has submitted or not 
	@return: true if the player has submitted this round, false otherwise
	*/
	getHaveISubmitted:function(){
		return store.state.myLobby.playerData[store.state.myData.id].submitted;
	},

	/**
	@Description: Increments the currently being reviewed booklet number 
	*/
	reviewNextBooklet:function(){
		store.state.reviewingBooklet++;
		console.log(store.state.reviewingBooklet);
	}
}

/**
@Description: Updates the current vue based on the state.
*/
function vueStateTransition(){
	if(store.state.myData.name == null){
		store.state.currentVue = 'LandingVue';
	}
	else if(store.state.myData.name != null){
		store.state.currentVue = 'BrowserVue';
	}
	if(store.state.myLobby == null){
		store.state.currentVue = 'BrowserVue';
	}
	else{
		//todo: check the gamedata and see if it's in 
		store.state.currentVue = 'LobbyVue';
	}
}

/**
@Description: Sends an HTTP request 
@param: method: The HTTP method to use
@param: path: The url to send the request to 
@param: callbackFunctionSuccess: the function to call on a successful request
@param: callbackFunctionFailure: the function to call on failure/error 
*/
function hitWithFail(method, path, callbackFunctionSuccess, callbackFunctionFailure){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	callbackFunctionSuccess(this);
			}else if(this.readyState == 4){
				callbackFunctionFailure(this);
			}
		};
		xhttp.withCredentials = true;
		xhttp.open(method, path, true);
		xhttp.send();
}

/**
@Description: Sends an HTTP request
SHOULD NOT BE USED FOR POSTS THAT REQUIRE A DATA BODY
@param: method: The HTTP method to use
@param: path: The url to send the request to 
@param: callbackFunctionSuccess: the function to call on a successful request
*/
function hit(method, path, callbackFunctionSuccess){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	callbackFunctionSuccess(this);
			}
		};
		xhttp.withCredentials = true;
		xhttp.open(method, path, true);
		xhttp.send();
}

/**
@Description: Sends an HTTP request for images
SHOULD NOT BE USED FOR POSTS THAT REQUIRE A DATA BODY 
@param: method: The HTTP method to use
@param: path: The url to send the request to 
@param: callbackFunctionSuccess: the function to call on a successful request 
*/
function hitImage(method, path, callbackFunction){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	callbackFunction(this);
			}
		};
		xhttp.responseType = 'arraybuffer';
		xhttp.withCredentials = true;
		xhttp.open(method, path, true);
		xhttp.send();

}

/**
@Description: Sends an POST HTTP request with data body
@param: path: The url to send the request to 
@param: data: the data to send to the path
@param: callbackFunctionSuccess: the function to call on a successful request 
*/
function post(path, data, callbackFunction){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	callbackFunction(this);
			}
		};
		xhttp.withCredentials = true;
		xhttp.open('POST', path, true);
		// xhttp.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
		xhttp.send(data);

}

export default{store}