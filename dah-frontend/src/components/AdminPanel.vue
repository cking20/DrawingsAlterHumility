<template>
  <div id="admin-panel">
    <p>Name<br><input v-model="tempLobbyDataCopy.name"></p>
    <p>Password<br><input v-model="tempLobbyDataCopy.password"></p>
    
    <p>Round limit: {{tempLobbyDataCopy.maxRounds}} <br>
      <input type="range" v-model.number="tempLobbyDataCopy.maxRounds" min=2 max=20 step=2>
    </p>
    <p>Player limit: {{tempLobbyDataCopy.maxPlayers}} <br>
      <input type="range" v-model.number="tempLobbyDataCopy.maxPlayers" min=4 max=12>
    </p>

    <button v-on:click="updateSettings()">Update</button>
    <button v-on:click="start()">START</button>
    


  </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'AdminPanel',
  props:{
  },
  components: {
  },
  data () {
    return {
      datastore: store.store,
      tempLobbyDataCopy: '',
    }
  },
  mounted: function(){
  },
  created: function(){
    this.tempLobbyDataCopy = JSON.parse(JSON.stringify(this.datastore.state.myLobby));
  },
  beforeDestroy: function(){
  },
  methods:{
    updateSettings: function(){
      this.datastore.updateLobbySettings(this.tempLobbyDataCopy);
      //todo
    },
    start: function(){
      this.datastore.beginGame();
    }
  }
}
</script>

<style lang="scss">
</style>
