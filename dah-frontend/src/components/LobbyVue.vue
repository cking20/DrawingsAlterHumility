<template>
	<div class = lobbies-vue>
		<H1>Current Lobbies</H1>
		<button v-on:click="datastore.getAvailableLobbies()">Refresh Lobbies</button>
		<ul>
			<li v-for="lobby in datastore.state.knownLobbies">{{JSON.stringify(lobby)}}</li>
		</ul>

		<H1>My Lobby Data</H1>
		<p>{{JSON.stringify(datastore.getMyLobbyData())}}</p>
		<button v-on:click="datastore.refreshMyLobbyData()">Refresh My Lobby</button>
		<button v-on:click="datastore.createNewLobby()">Create Lobby</button>
		<button v-on:click="datastore.joinLobby(0)">Join Lobby</button>
		<button v-on:click="datastore.leaveLobby()">Leave Lobby</button>
		
	</div>
</template>

<script>
import store from '../store.js'
export default{
	name: 'LobbyVue',
	props:{
	},
	components:{
	},
	data: function() {
		return {
			datastore: store.store
		}
	},
	mounted: function(){
		
	},
	created(){
		
	},
	methods:{	
		
	}
}
</script>