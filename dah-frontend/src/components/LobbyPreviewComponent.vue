<template>
	<div class="lobby-preview">
		<div>
			<h3>{{this.lobby.name}}</h3>
			<p>Admin: {{this.lobby.adminUuid}}</p>
			<p>In Progress: {{this.lobby.isInProgress}}</p>
			<p>Players: {{this.numPlayers}}/{{this.lobby.maxPlayers}}</p>
			<button v-if="this.numPlayers < this.lobby.maxPlayers" v-on:click="join()">Join</button>
		</div>	
	</div>
</template>

<script>
	import store from '../store.js'
export default{
	name: 'LobbyPreviewComponent',
	props:{
		lobby:{
			type: Object,
			default: function(){
				return {};}
		}
	},
	computed:{
		numPlayers: function(){
			return this.lobby.players.length;
		}
	},
	components:{
			
	},
	methods:{
		join: function(){
			console.log("joining Lobby");
			store.store.joinLobby(this.lobby.id);
		}
	}
	
}
</script>