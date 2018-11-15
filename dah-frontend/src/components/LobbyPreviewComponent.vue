<template>
	<div class="lobbypreview">
		<div class="card">
			<h3>{{this.lobby.name}}</h3>
			<p>Admin: {{this.adminName()}}</p>
			<p>Players: {{this.numPlayers}}/{{this.lobby.maxPlayers}}</p>
			<div v-if="this.lobby.isInProgress">
				<p class="warning">Game In Progress</p>
			</div>
			<div v-else>
			<input v-if="lobby.password != null" v-model="inputPassword" placeholder="Enter Password">	
			<button v-if="this.numPlayers < this.lobby.maxPlayers" v-on:click="join()" class="submit">Join</button>
			</div>
			
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
	data: function() {
		return {
			inputPassword: ""
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
			store.store.joinLobby(this.lobby.id, this.inputPassword);
		},
		adminName: function(){
			return this.lobby.playerData[this.lobby.adminUuid].name;
		}
	}
	
}
</script>
<style lang="scss">
.lobbypreview{ 
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
@media only screen and (orientation: portrait) {
	.lobbypreview{ 
		h3{
			font-size: 6em;
		}
		p{
			padding-left: 5%;
			padding-right: 5%;
		}
	}
}
</style>