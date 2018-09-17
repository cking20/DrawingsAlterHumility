<template>
  <div id="app">

    <h1>Test shared image</h1>
    <img id="testImage" ref="testImage">
    <button @click="reloadImage()">Refresh</button>

    <LobbyVue></LobbyVue>
    <DrawingApp></DrawingApp>
    <!-- <img src="./assets/logo.png">
    <h1>{{ msg }}</h1>
    <h2>Essential Links</h2>
    <ul>
      <li><a href="https://vuejs.org" target="_blank">Core Docs</a></li>
      <li><a href="https://forum.vuejs.org" target="_blank">Forum</a></li>
      <li><a href="https://chat.vuejs.org" target="_blank">Community Chat</a></li>
      <li><a href="https://twitter.com/vuejs" target="_blank">Twitter</a></li>
    </ul>
    <h2>Ecosystem</h2>
    <ul>
      <li><a href="http://router.vuejs.org/" target="_blank">vue-router</a></li>
      <li><a href="http://vuex.vuejs.org/" target="_blank">vuex</a></li>
      <li><a href="http://vue-loader.vuejs.org/" target="_blank">vue-loader</a></li>
      <li><a href="https://github.com/vuejs/awesome-vue" target="_blank">awesome-vue</a></li>
    </ul> -->
  </div>
</template>

<script>
  import store from './store.js'
  import DrawingApp from './components/DrawingApp.vue'
  import LobbyVue from './components/LobbyVue.vue'
export default {
  name: 'app',
  props:{
  },
  components: {
    DrawingApp,
    LobbyVue 
  },
  data () {
    return {
      datastore: store.store,
      imgRef: {}
    }
  },
  mounted: function(){
    this.imgRef = this.$refs['testImage']; 
  },
  methods:{
    reloadImage: function () {
      // this.imgRef.src = "";
      // this.imgRef.src = this.datastore.host+'/images/testimage';
      this.datastore.refreshImage(function(url){
        document.getElementById("testImage").src = url;
      });
      
    }
  }
}
</script>

<style lang="scss">
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
