<template>
  <div id="name-vue">
    <div id="rules" style="color: black;">
      <h2>How to play</h2>
      <p style="">
        DAH is the combination of the old fashioned game of telephone, and Pictionary.
        Choose a prompt to start off your booklet.
        Your booklet then gets handed off to player 2.
        Player 2 attempts to draw your prompt.
        Your booklet then gets handed off again. Player 3 can only see the drawing that was just created and must try to guess what player 2 was attempting to draw. This continues until every player's booklet is full.

      </p>
      <h3>DONT DRAW OUT TEXT</h3>
      <p>(It's funnier this way)</p>
    </div>
    <input v-model="name" placeholder="Set Name">
    <!-- <p>Name is: {{ name }}</p> -->
    <button class="submit" :disabled="!isValid" @click="submitNewName()">Play</button>
  </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'Name',
  props:{
  },
  components: {
  },
  data () {
    return {
      datastore: store.store,
      name: '',
      validName: false
    }
  },
  computed: {
    isValid: function() {
     return this.name != '';
    }
  },
  mounted: function(){
  },
  created: function(){
  },
  beforeDestroy: function(){
  },
  methods:{
    submitNewName: function(){
      this.name = this.name.trim().replace(/[&\/\\#,+()$~%.'":;*?<>{}]/g, '');
      if(this.name!= '')
        this.datastore.changeName(this.name);
    }
  }
}
</script>

<style lang="scss">
@media only screen and (orientation: portrait) and (max-width: 800px){
  #rules{
    h1{
      margin-top: 25%;
      font-size: 5em;
    }
    h2{
      font-size: 2em;
    }
    p{
      font-size: 1em;
    }
  }

}
@media only screen and (orientation: portrait) and (min-width: 800px) {
  #rules{
    h1{
      margin-top: 25%;
      font-size: 10em;
    }
    h2{
      font-size: 4em;
    }
    p{
      font-size: .5em;
    }
  }
}
</style>
