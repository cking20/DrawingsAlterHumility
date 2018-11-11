<template>
  <div id="admin-panel" class="panel">
    <div v-if="!starting">
      <H2>Settings</H2>
      <p>Name<br><input v-model="tempLobbyDataCopy.name"></p>
      <p>Password<br><input v-model="tempLobbyDataCopy.password"></p>
      
      <!-- <p>Round limit: {{tempLobbyDataCopy.maxRounds}} <br>
        <input type="range" v-model.number="tempLobbyDataCopy.maxRounds" min=2 max=20 step=2>
      </p> -->
      <p>Player limit: {{tempLobbyDataCopy.maxPlayers}} <br>
        <input type="range" v-model.number="tempLobbyDataCopy.maxPlayers" min=4 max=12>
      </p>

      <button class="submit" v-on:click="updateSettings()">Update</button>
      <button class="submit" v-on:click="start()">START</button>
    </div>
    <div v-else>
      <h1>Starting the Game...</h1>
    </div>


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
      starting: false
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
      if(this.tempLobbyDataCopy.name != null)
        this.tempLobbyDataCopy.name = this.tempLobbyDataCopy.name.trim().replace(/[&\/\\#,+()$~%.'":;*?<>{}]/g, '');
      if(this.tempLobbyDataCopy.password != null)
        this.tempLobbyDataCopy.password = this.tempLobbyDataCopy.password.trim().replace(/[&\/\\#,+()$~%.'":;*?<>{}]/g, '');
      this.datastore.triggerLoadingScreen();
      this.datastore.updateLobbySettings(this.tempLobbyDataCopy);
      //todo
    },
    start: function(){
      this.starting = true;
      this.datastore.triggerLoadingScreen();
      this.datastore.beginGame();
    }
  }
}
</script>

<style lang="scss">
button{
  width: 100%;
}
input{
  width: 100%;
}
@media only screen and (min-width: 10em) and (max-width: 60em) {
  input{
    font-size: .5em;
    width: 100%;
  }
}

</style>
