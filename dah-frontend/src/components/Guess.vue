<template>
	<div class = guess-vue>
	<div v-if="!this.datastore.getHaveISubmitted()">
		<H2>Guess what this is</H2>
		<img id="testImage" ref="testImage" width="100%">
		<input v-model="guess" placeholder="Your Guess Here">
		<button @click="submit()" class="submit" :disabled="!isValid">SUBMIT</button>
	</div>
	<div v-else>
		<PlayersStatus></PlayersStatus>
	</div>	
		

		
	</div>
</template>

<script>
import store from '../store.js'
import PlayersStatus from './PlayersStatus.vue'

export default{
	name: 'GuessVue',
	props:{
	},
	components:{
		PlayersStatus
	},
	data: function() {
		return {
			datastore: store.store,
			imgRef: {},
			guess: null
		}
	},
	computed:{
		isValid: function() {
	    	return this.guess != null;
		}
	},
	mounted: function(){
		this.imgRef = this.$refs['testImage']; 
	},
	created: function(){
		this.datastore.triggerLoadingScreen();
		this.datastore.refreshMyLobbyData();
		this.loadImage();
	},
	beforeDestroy: function(){
	},
	methods:{
		loadImage: function () {
	      this.datastore.loadImage(function(url){
	        document.getElementById("testImage").src = url;
	      }); 
	    },
	    submit: function(){
	    	if(this.guess != null){
	    		this.datastore.triggerLoadingScreen();
    			this.datastore.submitGuess(this.guess.trim().replace(/[&\/\\#,+()$~%.'":;*?<>{}]/g, ''));

	    	}
    	},
	}
}
</script>
<style lang="scss">
@media only screen and (min-width: 10em) and (max-width: 60em) {
	h2{
		margin-bottom: 0;
	}
	input{
		font-size: 1em;
	}
}
</style>