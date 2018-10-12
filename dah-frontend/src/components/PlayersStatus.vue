<template>
  <div id="player-status-panel" class="panel">
    <H2>Players' Statuses</H2>
    <ul>
      <li v-for="player in datastore.state.myLobby.playerData">
        <div v-if="player.submitted" class="submitted">
          {{player.name}}
        </div>
        <div v-else class="playing">
          {{player.name}}
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'PlayerStatus',
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
.submitted{
  background-color: green;
}
.playing{
  background-color: red;
}
li p{
  text-align: left;
}
</style>
