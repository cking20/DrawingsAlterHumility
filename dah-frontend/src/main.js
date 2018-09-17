import Vue from 'vue'
import App from './App.vue'
import store from './store.js'

window.Event = new Vue();
window.onbeforeunload = function() {
	store.store.leaveLobby()
};
new Vue({
  el: '#app',
  render: h => h(App)
})
