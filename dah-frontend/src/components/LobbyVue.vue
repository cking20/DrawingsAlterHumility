<template>
	<div class = lobby-vue>

		<!-- <p>{{JSON.stringify(datastore.getMyLobbyData())}}</p> -->
		<!-- <button v-on:click="datastore.refreshMyLobbyData()">Refresh My Lobby</button> -->
		<!-- <button v-on:click="datastore.joinLobby(0)">Join Lobby</button> -->
		<div v-if="datastore.state.myLobby != null">
			<Pregame v-if="datastore.state.myLobby.roundNumber == -1"></Pregame>
			<Play v-else-if="datastore.state.myLobby.roundNumber <= datastore.state.myLobby.maxRounds"></Play>
			<Review v-else></Review>
		</div>
		<div v-else>
			<!-- <h1> Preparing Awesomeness...</h1> -->
			<Loader></Loader>
		</div>
		<div v-if="datastore.state.myData.name == null">
	      <Modal style="background-color: lightgrey;"><Name></Name></Modal>
	    </div>

		

		
	</div>
</template>

<script>
import store from '../store.js'
import Pregame from './Pregame.vue'
import Play from './Play.vue'
import Review from './Review.vue'
import Loader from './Loader.vue'
import Name from './Name.vue'
  import Modal from './Modal.vue'
export default{
	name: 'LobbyVue',
	props:{
	},
	components:{
		Pregame,
		Play,
		Review,
		Loader,
		Name,
		Modal
	},
	data: function() {
		return {
			datastore: store.store
		}
	},
	mounted: function(){
		
	},
	created: function(){
		this.datastore.triggerLoadingScreen();
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