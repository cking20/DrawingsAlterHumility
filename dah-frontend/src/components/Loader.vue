<template>
  <div class="loading-container">
  <div id="loading-popup" class = "loader">   
  </div> 
  <p>Loading...</p> 
</div>
</template>

<script>
export default {
  name: 'Loader',
  props:{
  },
  components: {
  },
  data () {
    return {
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
.loading-container{
  padding-top: 20%;
}
.loader {
    margin: auto;
    border: 16px solid #f3f3f3; /* Light grey */
    border-top: 16px solid #569818; /* Green */
    border-radius: 50%;
    width: 120px;
    height: 120px;
    animation: spin 1.25s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
</style>
