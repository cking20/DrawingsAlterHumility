<template>
	<div class = play-vue>
		
		<H1>Play Vue IN DEV</H1>
		
		<div>
			<ChooseVue v-if="datastore.state.myLobby.roundNumber == 0"></ChooseVue>
			<DrawVue v-else-if="datastore.state.myLobby.roundNumber % 2 == 1"></DrawVue>
			<GuessVue v-else></GuessVue>
		</div>

		

		
	</div>
</template>

<script>
import store from '../store.js'
import DrawVue from './Draw.vue'
import ChooseVue from './Choose.vue'
import GuessVue from './Guess.vue'

export default{
	name: 'Play',
	props:{
	},
	components:{
		DrawVue,
		GuessVue,
		ChooseVue
	},
	data: function() {
		return {
			datastore: store.store
		}
	},
	mounted: function(){
		
	},
	created: function(){
		this.datastore.getPlayerData();
	    this.refresher = setInterval(this.refreshState,5000);
	},
	beforeDestroy: function(){
		clearTimeout(this.refresher);
	},
	methods:{	
		refreshState: function(){
	    	
    	},
    	
	}
}
</script>