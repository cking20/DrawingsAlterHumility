<template>
	<div class = lobbies-vue>
		<H1>Current Lobbies</H1>
		<div>
			<!-- <button v-on:click="datastore.getAvailableLobbies()">Refresh Lobbies</button> -->
		</div>
		<ul>
			<li v-for="lobby in datastore.state.knownLobbies">
				<!-- <button v-on:click="datastore.joinLobby(lobby.id)">Join {{lobby.id}}</button>
				{{JSON.stringify(lobby)}} -->
				<LobbyPreviewComponent v-bind:lobby="lobby"></LobbyPreviewComponent>
			</li>
		</ul>

		<H1>My Lobby Data</H1>
		<p>{{JSON.stringify(datastore.getMyLobbyData())}}</p>
		<!-- <button v-on:click="datastore.refreshMyLobbyData()">Refresh My Lobby</button> -->
		<button v-on:click="datastore.createNewLobby()">Create Lobby</button>
		<!-- <button v-on:click="datastore.joinLobby(0)">Join Lobby</button> -->
		<button v-on:click="datastore.leaveLobby()">Leave Lobby</button>
		
	</div>
</template>

<script>
import store from '../store.js'
import LobbyPreviewComponent from './LobbyPreviewComponent.vue'
export default{
	name: 'LobbyVue',
	props:{
	},
	components:{
		LobbyPreviewComponent
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