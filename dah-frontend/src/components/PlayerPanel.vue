<template>
  <div id="player-panel" class="panel">
    <H2>Player List</H2>
    <ul>
      <li v-for="player in datastore.state.myLobby.playerData">
        <p>{{player.name}}</p>
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
li p{
  text-align: left;
}
</style>
