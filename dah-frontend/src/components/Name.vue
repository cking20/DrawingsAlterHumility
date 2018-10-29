<template>
  <div id="name-vue">
    <input v-model="name" placeholder="Set Name">
    <!-- <p>Name is: {{ name }}</p> -->
    <button class="submit" :disabled="!isValid" @click="submitNewName()">Play</button>
  </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'Name',
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
      this.name = this.name.trim();
      if(this.name!= '')
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
