<template>
  <div>
    <a class="download" :href="downloadURL()" download="drawing.png">Download {{this.datastore.state.myLobby.playerData[bookletData.owner].name}}s game <img src="../assets/download.png" alt="Download"></a>

        <canvas id="downLoader" ref="dlCanvas" 
        v-bind:style="{width: w + 'px', height: h + 'px', display: 'none'}" 
        :width="w" :height="h">
          
        </canvas>
        <img id="saveImg" ref="saveImg"style="display: none">
   
    </div>
</template>

<script>
  import store from '../store.js'
export default {
  name: 'Downloader',
  props:{
    bookletData: {
        type: Object,
        required: true,
        default: function () {
          return {  
            owner: "",
              user: "",
              pages: []
      }
        }
      }
  },
  components: {
  },
  data () {
    return {
      canvas: null,
      context: null,
      saveImg: null,
      datastore: store.store,
      w: 350,
      backgroundColor: '#e6e6e6'
    }
  },
  computed:{
    h: function(){
      if(this.bookletData.pages != null)
        return (this.bookletData.pages.length/2 * 370) - 75;
      return 370;
    }
  },
  mounted: function(){
    this.canvas = this.$refs['dlCanvas'];
    this.saveImg = this.$refs['saveImg'];
    this.context = this.canvas.getContext('2d'); 
  },
  created: function(){
  },
  beforeDestroy: function(){
  },
  methods:{
    downloadURL: function(){
      if(this.canvas == null)
        return 0;
      else
        this.gen();
        return this.saveImg.src;//this.canvas.toDataURL("image/png").replace("image/png", "image/octet-stream");
    },
    gen: function(){
      //fill background
      this.context.clearRect(0, 0, this.w, this.h); 
      this.context.fillStyle = this.backgroundColor; 
      this.context.fillRect(0,0,this.w, this.h);  
      this.context.fillStyle = "black"; 

      //draw
      var ittH = 370;//itterator height
      var i = 0;
      for (i = 0; i < this.bookletData.pages.length-1; i+=2) {
        var img = new Image;
        img.src = this.datastore.host + "/images" +this.bookletData.pages[i+1].content;
        this.context.drawImage(img, 0,ittH*(i/2) + 50);

        this.context.textAlign="center";
        this.context.font = "30px Arial"; 
        this.context.fillText(this.bookletData.pages[i].content,this.w/2,ittH*(i/2) + 40);
        this.context.textAlign="end"; 
        this.context.font = "16px Arial"; 
        this.context.fillText("By "+this.datastore.state.myLobby.playerData[this.bookletData.pages[i].creator].name,this.w-25,ittH*(i/2 + 1)); //300+50+16+4

        //mark up canvas center
        this.context.beginPath();
        this.context.strokeStyle="red";
        this.context.moveTo(this.w/2, 0);
        this.context.lineTo(this.w/2, this.h);
        this.context.stroke();
        //mark up Title
        this.context.moveTo(0, ittH*(i/2) + 40);
        this.context.lineTo(this.w, ittH*(i/2) + 40);
        this.context.stroke();
        this.context.moveTo(0, ittH*(i/2) + 50);
        this.context.lineTo(this.w, ittH*(i/2) + 50);
        this.context.stroke();
        this.context.closePath();
        //ittH*(i/2 + 1)
        this.context.beginPath();
        this.context.strokeStyle="green";
        this.context.moveTo(0, ittH*(i/2 + 1));
        this.context.lineTo(this.w, ittH*(i/2 + 1));
        this.context.stroke();
        this.context.closePath();

        
      }
      this.context.textAlign="center";
      this.context.font = "30px Arial"; 
      this.context.fillText(this.bookletData.pages[i].content,this.w/2,ittH*(i/2) + 40);
      this.context.fillText("Drawings Alter Humility",this.w/2,ittH*(i/2) + 80);
      this.context.font = "12px Arial"; 
      this.context.fillText(this.datastore.host,this.w/2,ittH*(i/2) + 100);
      // this.context.textAlign="center";
      // this.context.font = "30px Arial"; 
      // this.context.fillText(this.bookletData.pages[this.bookletData.pages.length-1].content,this.w/2,ittH*(this.bookletData.pages.length/2-1)+40);
      this.context.stroke();
      this.context.closePath();
      this.saveImg.src = this.canvas.toDataURL();

    }

  }
}
</script>

<style lang="scss">

</style>
