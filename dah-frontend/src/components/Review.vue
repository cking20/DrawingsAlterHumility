<template>
	<div class = review-vue>
		<div v-if="datastore.state.reviewingBooklet >= datastore.state.myLobby.booklets.length">
			<h1>Results</h1>
			<div class="card">
				<ul>
					<li v-for="(player, index) in results">
						<div class="row" :class="{winner: index==0}">
							<p class="leaderboard-name">{{player.name}}</p>
							<p class="leaderboard-result" style="float: right;">{{player.votes}}/{{maxVotes}} upvotes!</p>
						</div>
					</li>
				</ul>			
			</div>
			<ul>
				<li v-for="(booklet, index) in datastore.state.myLobby.booklets">
					<Downloader :bookletData="booklet"></Downloader>	
				</li>
			</ul>
			<button v-if="isAdmin()" class="submit" @click="restart()">Restart Lobby</button>
			<button class="cancel" v-on:click="datastore.triggerLoadingScreen();datastore.leaveLobby()">Leave Lobby</button>
		</div>
		<div v-else>
			<H1>Vote</H1>
			<ul>
				<li v-for="(booklet, index) in datastore.state.myLobby.booklets">
					<Booklet v-if="index == datastore.state.reviewingBooklet" v-bind:bookletData="booklet"></Booklet>
				</li>
			</ul>
		</div>
	</div>
</template>

<script>
import store from '../store.js'
import Booklet from './Booklet.vue'
import Downloader from './Downloader.vue'
export default{
	name: 'Review',
	props:{
	},
	components:{
		Booklet,
		Downloader
	},
	data: function() {
		return {
			datastore: store.store
		}
	},
	computed:{
		results: function(){
			var resultArray = new Array();
			for (var i = 0; i < this.datastore.state.myLobby.players.length; i++) {
				var voteData = new Object();
				voteData.name = this.datastore.state.myLobby.playerData[this.datastore.state.myLobby.players[i]].name;
				voteData.votes = this.datastore.state.myLobby.playerData[this.datastore.state.myLobby.players[i]].votes;
				resultArray.push(voteData);
			}
			return resultArray.sort(
				function(a,b){return b.votes - a.votes});
		},
		maxVotes: function(){
			return this.datastore.state.myLobby.booklets.length * this.datastore.state.myLobby.maxRounds;
		}
	},
	mounted: function(){
		
	},
	created: function(){
	},
	beforeDestroy: function(){
	},
	methods:{	
		isAdmin: function(){
    		return this.datastore.state.myLobby.adminUuid == this.datastore.state.myData.id;
    	},
    	restart: function(){
    		store.store.restartLobby();
    	}
	}
}
</script>
<style lang="scss">
	.winner{
		color: green;
		font-size: 3em;
	}
	@media only screen and (orientation: portrait) {
		p{
			font-size: 2em;
		}
		
	}
</style>