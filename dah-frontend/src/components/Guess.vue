<template>
	<div class = guess-vue>
	<div v-if="!this.datastore.getHaveISubmitted()">
		<H1>Guess what this is</H1>
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
	    	if(this.guess != null)
    			this.datastore.submitGuess(this.guess);
    	},
	}
}
</script>
<style lang="scss">
h1{
	margin: 0;
}
</style>