const store = {
	// host: 'http://localhost:4567/game',
	host: 'https://drawings-alter-humility.herokuapp.com/game',
	state:{
		currentVue: 'BrowserVue',
		tempImageData:null,
		myData:{},
		myLobby:null,
		knownLobbies:[],
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
		post(this.host + '/images/testimage', formdata, function(xhttp){
			callbackFunction();
		});
	},
	test: function(){
		var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	//JSON.parse(this.responseText);
				console.log(this.responseText);
			}
		};
		xhttp.open('GET', this.host + '/hello', true);
		xhttp.send();
	},
	refreshMyLobbyData: function(){
		hit('GET', this.host + '/me/lobby', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		}, function(xhttp){
			store.state.myLobby = null;
		});
		vueStateTransition();
	},

	getAvailableLobbies: function(){
		hit('GET', this.host + '/lobbies', function(xhttp){
			store.state.knownLobbies = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	},
	getPlayerData: function(){
		hit('GET', this.host + '/me', function(xhttp){
			store.state.myData = JSON.parse(xhttp.responseText);
		}, function(xhttp){
			store.state.myData = null;
		});
		vueStateTransition();
	},
	changeName: function(name){
		var nameJSON = '{"name": ' + name + '}';
		post(this.host + '/me/name', nameJSON, function(xhttp){
			store.state.myData = JSON.parse(xhttp.responseText);
		});
	},
	joinLobby: function(lobbyID){
		hit('POST', this.host + '/lobbies/'+ lobbyID + '/join', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	},
	leaveLobby: function(){
		hit('POST', this.host + '/lobbies/'+ store.state.myLobby.id + '/leave', function(xhttp){

			// Vue.set(store.state.myLobby, null);
			store.state.myLobby = null;
			// delete store.state.myLobby;
			// console.log(store.state.myLobby);

		});
		vueStateTransition();
	},
	updateLobbySettings: function(lobbySettings){
		console.log(JSON.stringify(lobbySettings));
		post(this.host + '/lobbies/'+lobbySettings.id, JSON.stringify(lobbySettings), function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
	},
	createNewLobby: function(){
		hit('POST', this.host + '/lobbies', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	},
	getMyLobbyData: function(){
		return store.state.myLobby;
	},
	beginGame: function(){
		hit('POST', this.host + '/lobbies/'+store.state.myLobby.id+'/begin', function(xhttp){
			// store.state.myLobby = JSON.parse(xhttp.responseText);
		});
		vueStateTransition();
	}
}

function vueStateTransition(){
	if(store.state.myLobby == null){
		store.state.currentVue = 'BrowserVue';
	}
	else{
		//todo: check the gamedata and see if it's in 
		store.state.currentVue = 'LobbyVue';
	}
}

function hit(method, path, callbackFunctionSuccess, callbackFunctionFailure){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	callbackFunctionSuccess(this);
			}else{
				callbackFunctionFailure(this);
			}
		};
		xhttp.withCredentials = true;
		xhttp.open(method, path, true);
		xhttp.send();
}

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

//This should change to auto detect the game state and hit the correct endpoint
function post(path, data, callbackFunction){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	callbackFunction(this);
			}
		};
		// uploaded_file

		xhttp.withCredentials = true;
		xhttp.open('POST', path, true);
		// xhttp.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
		xhttp.send(data);

}

export default{store}