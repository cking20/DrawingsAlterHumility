<template>
	<div class = guess-vue>
		
		<H1>Guess what this is</H1>
		<img id="testImage" ref="testImage">
		<input v-model="guess" placeholder="Your Guess Here">
		<button @click="submit()">SUBMIT</button>

		

		
	</div>
</template>

<script>
import store from '../store.js'

export default{
	name: 'GuessVue',
	props:{
	},
	components:{
	},
	data: function() {
		return {
			datastore: store.store,
			imgRef: {},
			guess: null
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