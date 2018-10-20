<template>
	<div class = review-vue>
		
		<H1>Review Vue</H1>
		<ul>
			<li v-for="booklet in datastore.state.myLobby.booklets">
				<Booklet v-bind:bookletData="booklet"></Booklet>
			</li>
		</ul>
	
	</div>
</template>

<script>
import store from '../store.js'
import Booklet from './Booklet.vue'
export default{
	name: 'Review',
	props:{
	},
	components:{
		Booklet
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