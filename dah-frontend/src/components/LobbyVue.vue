<template>
	<div class = lobby-vue>

		<H1>My Lobby Data</H1>
		<p>{{JSON.stringify(datastore.getMyLobbyData())}}</p>
		<!-- <button v-on:click="datastore.refreshMyLobbyData()">Refresh My Lobby</button> -->
		<!-- <button v-on:click="datastore.joinLobby(0)">Join Lobby</button> -->
		<button v-on:click="datastore.leaveLobby()">Leave Lobby</button>

		<div>
			<Pregame v-if="datastore.state.myLobby.roundNumber == -1"></Pregame>
			<Play v-else-if="datastore.state.myLobby.roundNumber <= datastore.state.myLobby.maxRounds"></Play>
			<Review v-else></Review>
		</div>

		

		
	</div>
</template>

<script>
import store from '../store.js'
import Pregame from './Pregame.vue'
import Play from './Play.vue'
import Review from './Review.vue'
export default{
	name: 'LobbyVue',
	props:{
	},
	components:{
		Pregame,
		Play,
		Review
	},
	data: function() {
		return {
			datastore: store.store
		}
	},
	mounted: function(){
		
	},
	created: function(){
		this.datastore.getPlayerData();
	    this.refresher = setInterval(this.refreshState,5000);
	},
	beforeDestroy: function(){
		clearTimeout(this.refresher);
	},
	methods:{	
		refreshState: function(){
	    	//todo: only update the part that need updateing TBD when Vue Swither is built
	      	// this.reloadImage();
	      	console.log("Lobby Refresh State");
	      	this.datastore.refreshMyLobbyData();
	      	// this.datastore.getAvailableLobbies();
    	},
    	isAdmin: function(){
    		return this.datastore.state.myLobby.adminUuid == this.datastore.state.myData.id;
    	}
	}
}
</script>