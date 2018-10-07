<template>
	<div class = pregame-vue>
		<H1>Lobby {{datastore.getMyLobbyData().name}}</H1>
		<button class="cancel" v-on:click="datastore.leaveLobby()">Leave Lobby</button>
		<div v-if="isAdmin()" class="center horizontal card">
			<AdminPanel></AdminPanel>
		</div>
		<div v-bind:class="{ center: isAdmin(), horizontal: isAdmin(), card: true}">
			<PlayerPanel ></PlayerPanel>
		</div>
	</div>
</template>

<script>
import store from '../store.js'
import AdminPanel from './AdminPanel.vue'
import PlayerPanel from './PlayerPanel.vue'
export default{
	name: 'Pregame',
	props:{
	},
	components:{
		AdminPanel,
		PlayerPanel
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
<style lang="scss">
.cancel{
	width: 100%;
}
.horizontal{
	width:45%;
	display: inline-block;
	overflow: hidden;
	vertical-align: top;
}

</style>