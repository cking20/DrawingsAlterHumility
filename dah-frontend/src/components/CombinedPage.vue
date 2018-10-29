<template>
	<div class="page">
		<h2 class="titleContent">Title: {{title.content}}</h2>
		<div class="imageContent">
			<img :src="imgURL" style="width: 100%;">	
		</div>
		<p class="attribution">By {{datastore.state.myLobby.playerData[drawing.creator].name}}</p>
		<h2 class="guessContent">Guess: {{guess.content}}</h2>
		<p class="attribution">By {{datastore.state.myLobby.playerData[guess.creator].name}}</p>
		
	</div>
</template>

<script>
import store from '../store.js'
export default{
	name: 'CombinedPage',
	props:{
		title: {
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
	    drawing: {
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
	    guess: {
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
	    // bookletCreator:{
	    // 	type: String,
	    // 	required: true
	    // },
	    // index: {
	    //   type: Number,
	    //   required: true
	    // }
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
		this.imgURL = this.datastore.host + "/images" +this.drawing.content;
		// this.loadImageToElement(document.getElementById(this.pageData.id));
		     
	},
	beforeDestroy: function(){
	},
	methods:{	
		// vote: function(){
		// 	//TODO hit vote with id param
		// 	console.log(this.pageData.creator+", "+ this.index);
		// 	this.datastore.vote(this.pageData.creator, this.index);
		// }
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
	text-align: right;
	padding-right: 5%;
}
</style>