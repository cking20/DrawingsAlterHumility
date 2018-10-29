<template>
	<div class="horizontal-container">
		
		<div v-if="currentPage != null">
			<button class="cancel horizontal-element" @click="next(0)">:|<br>&#x2715</button>
			<div class="booklet horizontal-element">
				<ul>
					<li v-for="(page, index) in pages">
						<CombinedPage v-if="index == viewIndex" v-bind="currentPage"></CombinedPage>
					</li>
				</ul>
			</div>
			<button class="submit horizontal-element" @click="next(1)">;D<br>&#x2714</button>
		</div>
		<!-- <div class=booklet>
			<H1>{{datastore.state.myLobby.playerData[bookletData.owner].name}}</H1>
			
			<ul class="">
				<li v-for="(page, index) in bookletData.pages">
					<Page v-bind:pageData="page" v-bind:index="index" v-bind:bookletCreator="bookletData.owner"></Page>
				</li>
			</ul>
			<button class="download" >Download Booklet</button>
		</div> -->
		
		<button @click="DEV_UNDO">DEV BACK</button>
	</div>
</template>

<script>
import store from '../store.js'
//import Page from './Page.vue'
import CombinedPage from './CombinedPage.vue'
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
		//Page,
		CombinedPage
	},
	data: function() {
		return {
			datastore: store.store,
			viewIndex: 0
		}
	},
	computed:{
		pages: function(){
			var thePages = new Array();
			for (var i = 0; i < this.bookletData.pages.length-2; i+=2) {
				var page = new Object();
				page.title = this.bookletData.pages[i];
				page.drawing = this.bookletData.pages[i+1];
				page.guess = this.bookletData.pages[i+2];
				thePages.push(page);
			}
			return thePages;
		},
		currentPage: function(){
			return this.pages[this.viewIndex];
		}
	},
	mounted: function(){
		
	},
	created: function(){
	},
	beforeDestroy: function(){
	},
	methods:{	
		next: function(vote){
			if(vote > 0){
				//vote
				console.log(this.bookletData.owner+", "+ (this.viewIndex*2+1)+", "+(this.viewIndex*2+2));
				//TBD TODO make seconds param not 0
				this.datastore.vote(this.bookletData.owner, this.viewIndex*2+1);
				this.datastore.vote(this.bookletData.owner, this.viewIndex*2+2);
			

			}
			this.viewIndex++;
			if(this.viewIndex >= this.pages.length){
				console.log("nextBook?");
				this.datastore.reviewNextBooklet();
			}
		},
		DEV_UNDO: function(){
			this.viewIndex--;
		}
	}
}
</script>
<style lang="scss">
.horizontal-container{

}
.horizontal-element{
	padding-top: 49%;
	padding-bottom: 45%;
	float: left;
	width: 10%;
}
.booklet{
	padding-top: 0;
	padding-bottom: 0;
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