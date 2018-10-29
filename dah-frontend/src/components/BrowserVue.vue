<template>
  <div id="browser-vue">
    <H1>Current Lobbies</H1>
    <p v-if="datastore.state.knownLobbies.length == 0">There are no Lobbies yet. Start the fun, by creating one!</p>
    <button class="submit" v-on:click="datastore.triggerLoadingScreen();datastore.createNewLobby()">Create Lobby</button>
    <div>
      <!-- <button v-on:click="datastore.getAvailableLobbies()">Refresh Lobbies</button> -->
    </div>
    <ul>
      <li class="lobbylist" v-for="lobby in datastore.state.knownLobbies">
        <!-- <button v-on:click="datastore.joinLobby(lobby.id)">Join {{lobby.id}}</button>
        {{JSON.stringify(lobby)}} -->
        <LobbyPreviewComponent v-bind:lobby="lobby"></LobbyPreviewComponent>
      </li>
    </ul>

  </div>
</template>

<script>
  import store from '../store.js'
  import LobbyPreviewComponent from './LobbyPreviewComponent.vue'
export default {
  name: 'BrowserVue',
  props:{
  },
  components: {
    LobbyPreviewComponent
  },
  data () {
    return {
      datastore: store.store,
      refresher: ''
    }
  },
  mounted: function(){
  },
  created: function(){
    this.datastore.triggerLoadingScreen();
    this.refreshState();
    this.refresher = setInterval(this.refreshState,5000);
  },
  beforeDestroy: function(){
    clearTimeout(this.refresher);
  },
  methods:{
    refreshState: function(){
      //todo: only update the part that need updateing TBD when Vue Swither is built
      // this.reloadImage();
      console.log("Browser refesh state");
      // this.datastore.refreshMyLobbyData();
      this.datastore.getAvailableLobbies();
    },
  }
}
</script>

<style lang="scss">
.lobbylist{
  width: 100%;
}
</style>
