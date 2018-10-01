<template>
	<div class = lobby-vue>
		
		<div v-if="isAdmin()">
			<H2>Admin Panel Vue</H2>
			<AdminPanel></AdminPanel>
		</div>

		<H1>My Lobby Data</H1>
		<p>{{JSON.stringify(datastore.getMyLobbyData())}}</p>
		<!-- <button v-on:click="datastore.refreshMyLobbyData()">Refresh My Lobby</button> -->
		<!-- <button v-on:click="datastore.joinLobby(0)">Join Lobby</button> -->
		<button v-on:click="datastore.leaveLobby()">Leave Lobby</button>

		
	</div>
</template>

<script>
import store from '../store.js'
import AdminPanel from './AdminPanel.vue'
export default{
	name: 'LobbyVue',
	props:{
	},
	components:{
		AdminPanel
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