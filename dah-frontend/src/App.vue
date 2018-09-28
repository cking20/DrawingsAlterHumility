<template>
  <div id="app">

    <input v-model="name" placeholder="Set Name">
    <p>Name is: {{ name }}</p>
    <button @click="submitNewName()">Set</button>

    <h1>Test shared image</h1>
    <img id="testImage" ref="testImage">

    <div class="fb-like" data-href="https://developers.facebook.com/docs/plugins/" data-layout="button" data-action="like" data-size="large" data-show-faces="false" data-share="false">
    </div>

    <div class="fb-share-button" 
      data-href="https://drawings-alter-humility.herokuapp.com/" 
      data-layout="button" data-size="large" data-mobile-iframe="true">
      <a target="_blank" 
      href="https://www.facebook.com/sharer/sharer.php?u=https%3A%2F%2Fdrawings-alter-humility.herokuapp.com%2F&amp;src=sdkpreparse" class="fb-xfbml-parse-ignore">Share
      </a>
    </div>

    <div>
      <a class="twitter-share-button"
      href="https://twitter.com/intent/tweet?text=
      Hello%20world"
      data-size="large">
      Tweet
      </a>
    </div>

    <!-- <button @click="reloadImage()">Refresh</button> -->

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
  import socialConnector from './socialConnector.js'
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
      sapi : socialConnector.socialConnector,
      datastore: store.store,
      imgRef: {},
      name: '',
      refresher: ''
    }
  },
  mounted: function(){
    this.imgRef = this.$refs['testImage']; 
  },
  created: function(){
    this.refresher = setInterval(this.refreshState,5000);
  },
  methods:{
    refreshState: function(){
      //todo: only update the part that need updateing TBD when Vue Swither is built
      this.reloadImage();
      this.datastore.refreshMyLobbyData();
      this.datastore.getAvailableLobbies();
    },
    reloadImage: function () {
      // this.imgRef.src = "";
      // this.imgRef.src = this.datastore.host+'/images/testimage';
      this.datastore.refreshImage(function(url){
        document.getElementById("testImage").src = url;
      }); 
    },
    submitNewName: function(){
      this.datastore.changeName(this.name);
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
