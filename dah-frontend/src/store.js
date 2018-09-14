const store = {
	// host: 'http://localhost:4567/game',
	host: 'https://drawings-alter-humility.herokuapp.com/game',
	state:{
		myData:{},
		myLobby:null,
		knownLobbies:[],
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
		hit('GET', this.host + '/current', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
	},

	getAvailableLobbies: function(){
		hit('GET', this.host + '/lobbies', function(xhttp){
			store.state.knownLobbies = JSON.parse(xhttp.responseText);
		});
	},
	joinLobby: function(lobbyID){
		hit('POST', this.host + '/lobbies/'+ lobbyID + '/join', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
	},
	leaveLobby: function(){
		hit('POST', this.host + '/lobbies/'+ store.state.myLobby.id + '/leave', function(xhttp){

			// Vue.set(store.state.myLobby, null);
			store.state.myLobby = null;
			// delete store.state.myLobby;
			// console.log(store.state.myLobby);

		});
	},
	createNewLobby: function(){
		hit('POST', this.host + '/lobbies', function(xhttp){
			store.state.myLobby = JSON.parse(xhttp.responseText);
		});
	},
	getMyLobbyData: function(){
		return store.state.myLobby;
	}
}

function hit(method, path, callbackFunction){
	var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
		    if (this.readyState == 4 && this.status == 200) {
		    	//could be a callback function passed as a parameter?
		    	callbackFunction(this);
			}
		};
		xhttp.withCredentials = true;
		xhttp.open(method, path, true);
		xhttp.send();
}
export default{store}