<template>
	<div class="page">
		<div v-bind:class="{ imageContent: pageData.isImage, guessContent: !pageData.isImage }">
			<img v-if="pageData.isImage" :src="imgURL" style="width: 100%;">
			<h2 v-else>{{pageData.content}}</h2>
		</div>
		<p class="attribution">By {{datastore.state.myLobby.playerData[pageData.creator].name}}</p>
		<p>Votes: {{this.pageData.votes.length}}</p>
		<button class="submit" style="width:50%;" @click="vote">+1</button>
	</div>
</template>

<script>
import store from '../store.js'
export default{
	name: 'Page',
	props:{
		pageData: {
	      type: Object,
	      required: true,
	      default: function () {
	        return {  
	        	id: 0,
                creator: "",
                isImage: false,
                content: "",
                votes: []
 			}
	      }
	    },
	    bookletCreator:{
	    	type: String,
	    	required: true
	    },
	    index: {
	      type: Number,
	      required: true
	    }
	},
	components:{
	},
	data: function() {
		return {
			datastore: store.store,
			imgURL: null
		}
	},
	mounted: function(){
		 
	},
	created: function(){
		this.imgURL = this.datastore.host + "/images" +this.pageData.content;
		// this.loadImageToElement(document.getElementById(this.pageData.id));
		     
	},
	beforeDestroy: function(){
	},
	methods:{	
		vote: function(){
			//TODO hit vote with id param
			console.log(this.pageData.creator+", "+ this.index);
			this.datastore.vote(this.pageData.creator, this.index);
		}
	}
}
</script>
<style lang="scss">
.page{
	overflow: hidden;
	display: block;
	width: 100%;
}
.guessContent{
	margin: auto;
	background-color: #e6e6e6;
    width: 90%;
    padding-top: 40%;
    padding-bottom: 40%;
    text-align: center;
    vertical-align: center;
    position: relative; 
}
.imageContent{
	width: 90%;
	margin: auto;
}
.attribution{
	font-size: 12px;
	text-align: center;
}
</style>