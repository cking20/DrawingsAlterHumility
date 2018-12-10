<template>
  <div id="player-panel" class="panel">
    <H2>Player List</H2>
    <ul>
      <li class="row" v-for="(player, index) in datastore.state.myLobby.playerData">
        <div class="playerItem">
          <button v-if="isAdmin() && isNotMe(player)" class="remove" v-on:click="kickPlayer(index)">X</button>
          
            <p class="playername">{{player.name}}</p>
          
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'PlayerPanel',
  props:{
  },
  components: {
  },
  data () {
    return {
      datastore: store.store,
    }
  },
  mounted: function(){
  },
  created: function(){
  },
  beforeDestroy: function(){
  },
  methods:{
    isAdmin: function(){
      return this.datastore.state.myLobby.adminUuid == this.datastore.state.myData.id;
    },
    isNotMe: function(player){
      return player.name != this.datastore.state.myData.name;
    },
    kickPlayer: function(index){
      console.log("index "+index);
      this.datastore.kickPlayer(index);
    }
  }
}
</script>

<style lang="scss">
.playerItem{
  width: 95%;
  
  margin: 2%;
}
.playername{
  display: inline;
  overflow: hidden;
  width: 70%;
  margin: 0;
}
li p{
  text-align: left;
}

.remove{
  display: inline-block;
  width: 20%;
  margin:0%;
  background-color: #9a1842;
  border-color: #9a1842;
}
.remove:hover{
  background-color: #db0147;
  border-color: #db0147;
}
@media only screen and (orientation: portrait) {
  .row p{
    margin: 0;
  }
}
</style>
