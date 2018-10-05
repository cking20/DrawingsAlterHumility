<template>
	<div class = choose-vue>
		
		<H1>Choose A Starting Phrase</H1>

		<p>todo: get phrases</p>

		<ul>
			<li v-for="prompt in datastore.state.prompts">
				<p :id="prompt" @click="select(prompt)">{{prompt}}</p><br>
				<!-- <button>{{prompt}}</button> -->
			</li>
		</ul>

		<select name="cars" size=10>
		  <option v-for="prompt in datastore.state.prompts" :value="prompt">{{prompt}}</option>
		</select>

		<button @click="submit()">SUBMIT</button>
		
		

		

		
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
    			document.getElementById(this.selected).style = "color: black;";
    		this.selected = item;
    		document.getElementById(item).style = "color: blue;";
    		console.log(item);
    	}
	}
}
</script>