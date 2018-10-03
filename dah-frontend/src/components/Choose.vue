<template>
	<div class = choose-vue>
		
		<H1>Choose A Starting Phrase</H1>

		<p>todo: get phrases</p>
		
		

		

		
	</div>
</template>

<script>
import store from '../store.js'

export default{
	name: 'ChooseVue',
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