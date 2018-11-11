<template>
  <div id="landing">
    <H1>;Drawings Alter Humility</H1>
    <H2>Guess. ;Draw. Laugh.</H2>
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
      
    }
  },
  computed: {
    
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
#landing{
  h1{
    margin-top: 25%;
    font-size: 60px;
  }
}
@media only screen and (min-width: 20em) and (max-width: 60em) {
  #landing{
  h1{
    margin-top: 25%;
    font-size: 5em;
  }
  h2{
    font-size: 2em;
  }
}
}
</style>
