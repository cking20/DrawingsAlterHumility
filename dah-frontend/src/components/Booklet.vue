<template>
	<div class=booklet>
		
		<H1>{{datastore.state.myLobby.playerData[bookletData.owner].name}}</H1>
		<ul>
			<li v-for="page in bookletData.pages">
				<Page v-bind:pageData="page"></Page>
			</li>
		</ul>
		<button class="download">Download</button>
	</div>
</template>

<script>
import store from '../store.js'
import Page from './Page.vue'
export default{
	name: 'Booklet',
	props:{
		bookletData: {
	      type: Object,
	      required: true,
	      default: function () {
	        return {  
	        	owner: "",
            	user: "",
            	pages: []
 			}
	      }
	    }
	},
	components:{
		Page
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
<style lang="scss">
.booklet{
	width:75%;
	margin: auto;
	background-color: #e6e6e6;
	color: black;
	overflow: hidden;

    border-radius: 12px;
}
.download{
	width: 75%;
	color: black;
	background-color: #ccc;
}
ul li{
	width: 100%;
}
</style>