<template>
	<div class = review-vue>
		
		<H1>Review Vue</H1>
	
	</div>
</template>

<script>
import store from '../store.js'
export default{
	name: 'Review',
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