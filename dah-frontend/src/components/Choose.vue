<template>
	<div class = "choose-vue">
		<H1>Choose A Starting Phrase</H1>
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
		this.datastore.getPrompts();		
	},
	beforeDestroy: function(){
	},
	methods:{
    	submit: function(){
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
</style>