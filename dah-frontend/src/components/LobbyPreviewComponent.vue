<template>
	<div class="lobbypreview">
		<div class="card">
			<h3>{{this.lobby.name}}</h3>
			<p>Admin: {{this.adminName()}}</p>
			<div v-if="this.lobby.isInProgress">
				<p class="warning">Game In Progress</p>
			</div>
			<div v-else>
				
			</div>
			<p>Players: {{this.numPlayers}}/{{this.lobby.maxPlayers}}</p>
			<button v-if="this.numPlayers < this.lobby.maxPlayers" v-on:click="join()" class="submit">Join</button>
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
		},
		
	},
	components:{
			
	},
	methods:{
		join: function(){
			console.log("joining Lobby");
			store.store.joinLobby(this.lobby.id);
		},
		adminName: function(){
			return this.lobby.playerData[this.lobby.adminUuid].name;
		}
	}
	
}
</script>
<style lang="scss">
	.lobbypreview{
		width: 100%; 
		h3{
			padding: 5%;
		}
		p{
			padding-left: 5%;
			padding-right: 5%;
		}
	}
	.warning{
		color: red;
	}
</style>