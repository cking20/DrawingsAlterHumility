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
/*found from https://www.w3schools.com/howto/howto_css_modals.asp*/
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
    margin: 40% auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}
</style>
