<template>
	<div class = "choose-vue">
		<div v-if="!this.datastore.getHaveISubmitted()">
			<H2>Choose A Starting Phrase</H2>
			<div class="card">
				<ul>
					<li v-for="prompt in datastore.state.prompts" class="item">
						<p :id="prompt"  @click="select(prompt)">{{prompt}}</p><br>
						<!-- <button>{{prompt}}</button> -->
					</li>
				</ul>
			</div>
	<!-- 
			<select name="cars" size=10>
			  <option v-for="prompt in datastore.state.prompts" :value="prompt">{{prompt}}</option>
			</select>
	 -->
			<button class="submit" :disabled="!isValid" @click="submit()">SUBMIT</button>
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
	name: 'ChooseVue',
	props:{
	},
	components:{
		PlayersStatus
	},
	data: function() {
		return {
			datastore: store.store,
			selected: null
		}
	},
	computed:{
		isValid: function() {
	    	return this.selected != null;
		}
	},
	mounted: function(){
		
	},
	created: function(){
		this.datastore.refreshMyLobbyData();
		this.datastore.triggerLoadingScreen();
		this.datastore.getPrompts();		
	},
	beforeDestroy: function(){
	},
	methods:{
    	submit: function(){
    		this.datastore.triggerLoadingScreen();
    		this.datastore.submitGuess(this.selected);
    	},
    	select: function(item){
    		if(this.selected != null)
    			document.getElementById(this.selected).classList.remove('selected');
    		this.selected = item;
    		document.getElementById(item).classList.add('selected');
    		console.log(item);
    	},
	}
}
</script>
<style lang="scss">
	.selected{
		color: white;
	}
	.item{
		display: block;
		margin-bottom: 0;
	}
	.item p{
		margin-top : 0;
		padding-top: 20px;
		margin-bottom: 0px;
		text-align: center;
	}
	@media only screen and (orientation: portrait) {
		.item p{
			padding-top: .3em;
			font-size: 4em;
		}
	}
</style>