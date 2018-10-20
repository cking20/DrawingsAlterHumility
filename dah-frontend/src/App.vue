<template>
  <div id="app">
    <div v-if="datastore.state.mustRefresh">
      <Loader></Loader>
    </div>
    <div :class="{hidden: datastore.state.mustRefresh, center: true}">
      <component v-bind:is="datastore.state.currentVue"> </component>
    </div>
    

    <!-- <h1>Test shared image</h1>
    <img id="testImage" ref="testImage"> -->

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
  </div>
</template>

<script>
  import socialConnector from './socialConnector.js'
  import store from './store.js'
  import LandingVue from './components/LandingVue.vue'
  import Loader from './components/Loader.vue'
  import BrowserVue from './components/BrowserVue.vue'
  import DrawVue from './components/Draw.vue'
  import LobbyVue from './components/LobbyVue.vue'

export default {
  name: 'app',
  props:{
  },
  components: {
    DrawVue,
    LobbyVue,
    BrowserVue,
    LandingVue,
    Loader 
  },
  data: function() {
    return {
      sapi : socialConnector.socialConnector,
      datastore: store.store,
      currentVue: 'BrowserVue',
      // imgRef: {},
      refresher: ''
    }
  },
  mounted: function(){
    // this.imgRef = this.$refs['testImage']; 
  },
  created: function(){
    this.refresher = setInterval(this.refreshState,5000);
  },
  methods:{
    refreshState: function(){
      //todo: only update the part that need updateing TBD when Vue Swither is built
      // this.reloadImage();
      // this.datastore.refreshMyLobbyData();
      // this.datastore.getAvailableLobbies();
    },
    reloadImage: function () {
      // this.imgRef.src = "";
      // this.imgRef.src = this.datastore.host+'/images/testimage';
      // this.datastore.refreshImage(function(url){
      //   document.getElementById("testImage").src = url;
      // }); 
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
  color: #ffffff;
  margin-top: 0px;
}


body{
  background-color: #252525;
  white-space: no-wrap; 
}

button{
    border: 2px solid lightgrey;
    border-radius: 4px;
    width: 50%;
    padding: 12px 20px;
    margin: 8px 0;
    color: white;
    background: none;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    -webkit-transition-duration: 0.4s; /* Safari */
    transition-duration: 0.4s;
}
button:hover{
  background-color: #acacac;
}

input{
    text-align: center;
    border: 2px solid white;
    border-radius: 4px;
    width: 50%;
    padding: 12px 20px;
    margin: 8px 0;
    box-sizing: border-box;
}
input:focus {
    background-color: lightgrey;
    border-color: lightgrey;
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

.submit{
  background-color: #569818;
  border-color: #569818;
}
.submit:hover{
  background-color: #69dd01;
  border-color: #69dd01;
}

.cancel{
  background-color: #9a1842;
  border-color: #9a1842;
}
.cancel:hover{
  background-color: #db0147;
  border-color: #db0147;
}

:disabled{
  background-color: lightgrey;
  border-color: lightgrey;
}
:disabled:hover{
  background-color: lightgrey;
  border-color: lightgrey;
}

.center{
  margin: auto;
  width: 50%;
  padding: 10px;
}
@media only screen and (max-width: 700px) {
    .center {
        width: 100%;
        padding: 0px;
    }
}

.panel{
  color: #0a0a0a;
  background-color: #acacac;
  border-radius: 4px;
  padding: 12px 20px;
  margin: 8px 0;
}
.hidden{
  display: none;
}

.card{
  padding: 0;
  margin: 10px;
  color: #0a0a0a;
  background-color: #acacac;
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 1), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
@media only screen and (max-width: 700px) {
    .card {
        margin: 0px;
    }
}
</style>
