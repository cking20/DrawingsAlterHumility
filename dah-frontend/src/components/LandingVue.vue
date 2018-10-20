<template>
  <div id="landing-vue">
    <H1>;Drawings Alter Humility</H1>
    <H2>Guess. ;Draw. Laugh.</H2>
    <input v-model="name" placeholder="Set Name">
    <!-- <p>Name is: {{ name }}</p> -->
    <button class="submit" :disabled="!isValid" @click="submitNewName()">Play</button>

  </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'LandingVue',
  props:{
  },
  components: {
  },
  data () {
    return {
      datastore: store.store,
      name: '',
      validName: false
    }
  },
  computed: {
    isValid: function() {
     return this.name != '';
    }
  },
  mounted: function(){
  },
  created: function(){
  },
  beforeDestroy: function(){
  },
  methods:{
    submitNewName: function(){
      if(this.name != '')
        this.datastore.changeName(this.name);
    }
  }
}
</script>

<style lang="scss">
#landing-vue{
  h1{
    margin-top: 25%;
    font-size: 50px;
  }
}
</style>
