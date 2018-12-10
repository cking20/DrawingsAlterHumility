<template>
  <div class="modal">
    <div class="modal-content">
    <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Modal',
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
.modal {
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgb(0,0,0); 
    background-color: rgba(0,0,0,0.4);
}
.modal-content {
    background-color: lightgrey;
    margin: 10% auto;
    padding: 20px;
    
    width: 40%; /* Could be more or less, depending on screen size */
    height: 60%;
}
</style>
