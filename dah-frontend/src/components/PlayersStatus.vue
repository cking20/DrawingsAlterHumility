<template>
  <div id="player-status-panel" class="panel">
    <H2>Waiting for other players</H2>
    <ul>
      <li class="row" v-for="player in datastore.state.myLobby.playerData">
        <div :class="{submitted: player.submitted, playing: !player.submitted}">
          <p style="color: white">{{player.name}}</p>
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
.verticle{
  display: block;
  margin: auto;
}
.submitted{
  background-color: #569818;
}
.playing{
  background-color: #9a1842;
}
li p{
  text-align: left;
}
</style>
