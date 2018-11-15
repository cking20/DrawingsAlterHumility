<template>
  <div id="player-panel" class="panel">
    <H2>Player List</H2>
    <ul>
      <li class="row" v-for="player in datastore.state.myLobby.playerData">
        <div>
          <p>{{player.name}}</p>
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
  }
}
</script>

<style lang="scss">
.playername{
  display: inline-block;
  width: 100%;
}
li p{
  text-align: left;
}
@media only screen and (orientation: portrait) {
  .row p{
    margin: 0;
  }
}
</style>
