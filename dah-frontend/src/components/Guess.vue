<template>
	<div class = guess-vue>
		
		<H1>Guess</H1>
		
		

		

		
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
			datastore: store.store
		}
	},
	mounted: function(){
		
	},
	created: function(){
		
	},
	beforeDestroy: function(){
	},
	methods:{
    	
	}
}
</script>