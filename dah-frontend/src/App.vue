<template>
  <div id="app">
    <div v-if="datastore.state.mustRefresh">
      <Loader></Loader>
    </div>
    <div :class="{hidden: datastore.state.mustRefresh, center: true}">
      <component v-bind:is="datastore.state.currentVue"> </component>
    </div>
    <div>
      <button v-if="datastore.state.currentVue == 'LandingVue'" class="submit" @click="toBrowse()">Play</button>
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
      href="https://twitter.com/intent/tweet?text=Made%20on%20"
      data-size="large">
      Tweet
      </a>
    </div>
    <a href="privacy.html">Privacy Policy </a><a href="about.html"> About</a>
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
      currentVue: 'LandingVue',
      // imgRef: {},
      refresher: ''
    }
  },
  mounted: function(){
    // this.imgRef = this.$refs['testImage']; 
  },
  created: function(){
    // this.refresher = setInterval(this.refreshState,5000);
  },
  methods:{ 
    toBrowse: function(){
      this.datastore.state.currentVue =  'BrowserVue';
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
  width: 100%;
}


body{
  background-color: #252525;
  white-space: no-wrap; 
}

button{
  border: 2px solid lightgrey;
  border-radius: .25em;
  width: 50%;
  padding: .5em .5em;
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
  border-radius: .25em;
  width: 50%;
  padding: .5em .5em;
  margin: 8px 0;
  box-sizing: border-box;
}

.download{
  overflow: hidden;
  width: 100%;
  color: white;
  background: none;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  -webkit-transition-duration: 0.4s; /* Safari */
  transition-duration: 0.4s;
}
.download:hover{
  background-color: #acacac;
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
  margin: 0;
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
.row{
  display: block;
  margin: auto;
}
.row p{
  display: inline-block;
}
.leaderboard-name{
  padding: 5%;
  float:left;
  text-align: left;    
}
.leaderboard-result{
  padding: 5%;
  text-align: right;
}

.card{
  padding: 0;
  margin: 10px;
  color: #0a0a0a;
  background-color: #acacac;
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 1), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
@media only screen and (orientation: portrait) and (max-width: 800px){
  h1{
    font-size: 5em;
  }
  h2{
    font-size: 3em;
  }
  p{
    font-size: 3em;
  }
  .center {
    width: 100%;
    padding: 0px;
  }
  .card {
    margin: 0px;
  }
  input {
    width: 100%;
    font-size: 2em;
  }
  button{
    width: 100%;
    font-size: 2em;
  }
  #name-vue{
    z-index: 1000;
    position: fixed;
    bottom: 2%;
    left: 5%;
    margin: auto;
    width: 90%;
  }
}
@media only screen and (orientation: portrait) and (min-width: 800px) {
  h1{
    font-size: 10em;
  }
  h2{
    font-size: 6em;
  }
  p{
    font-size: 6em;
  }
  .center {
    width: 100%;
    padding: 0px;
  }
  .card {
    margin: 0px;
  }
  input {
    width: 100%;
    font-size: 4em;
  }
  button{
    width: 100%;
    font-size: 4em;
  }
  #name-vue{
    z-index: 1000;
    position: fixed;
    bottom: 2%;
    left: 5%;
    margin: auto;
    width: 90%;
  }
}


/*generated from https://www.cssportal.com/style-input-range/*/
input[type=range] {
  height: 47px;
  -webkit-appearance: none;
  margin: 10px 0;
  width: 100%;
  padding: 0;
  background-color: #acacac;
  border: 0px;
}
input[type=range]:focus {
  outline: none;
}
input[type=range]::-webkit-slider-runnable-track {
  width: 100%;
  height: 30px;
  cursor: pointer;
  animate: 0.2s;
  box-shadow: 0px 0px 0px #000000;
  background: #595959;
  border-radius: 50px;
  border: 0px solid #000000;
}
input[type=range]::-webkit-slider-thumb {
  box-shadow: 1px 1px 10px #000000;
  border: 0px solid #000000;
  height: 40px;
  width: 40px;
  border-radius: 50px;
  background: #D4D4D4;
  cursor: pointer;
  -webkit-appearance: none;
  margin-top: -5px;
}
input[type=range]:focus::-webkit-slider-runnable-track {
  background: #595959;
}
input[type=range]::-moz-range-track {
  width: 100%;
  height: 30px;
  cursor: pointer;
  animate: 0.2s;
  box-shadow: 0px 0px 0px #000000;
  background: #595959;
  border-radius: 50px;
  border: 0px solid #000000;
}
input[type=range]::-moz-range-thumb {
  box-shadow: 1px 1px 10px #000000;
  border: 0px solid #000000;
  height: 40px;
  width: 40px;
  border-radius: 50px;
  background: #D4D4D4;
  cursor: pointer;
}
input[type=range]::-ms-track {
  width: 100%;
  height: 30px;
  cursor: pointer;
  animate: 0.2s;
  background: transparent;
  border-color: transparent;
  color: transparent;
}
input[type=range]::-ms-fill-lower {
  background: #595959;
  border: 0px solid #000000;
  border-radius: 100px;
  box-shadow: 0px 0px 0px #000000;
}
input[type=range]::-ms-fill-upper {
  background: #595959;
  border: 0px solid #000000;
  border-radius: 100px;
  box-shadow: 0px 0px 0px #000000;
}
input[type=range]::-ms-thumb {
  margin-top: 1px;
  box-shadow: 1px 1px 10px #000000;
  border: 0px solid #000000;
  height: 40px;
  width: 40px;
  border-radius: 50px;
  background: #D4D4D4;
  cursor: pointer;
}
input[type=range]:focus::-ms-fill-lower {
  background: #595959;
}
input[type=range]:focus::-ms-fill-upper {
  background: #595959;
}
</style>
