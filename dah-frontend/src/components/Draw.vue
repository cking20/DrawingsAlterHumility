<template>
	<div class = draw-vue>
		<div v-if="!this.dataStore.getHaveISubmitted()">
			<H1>Draw: {{this.dataStore.getLastContentOfMyBooklet()}}</H1>
			<div>
				<canvas class="drawing-area card" ref="theCanvas" width="300" height="300" style="width: 90%; height: 100%;"
					@mousemove="mouseMove" 
					@mousedown="mouseDown" 
					@mouseup="mouseUp" 
					@mouseover="mouseOver"

					@touchmove.prevent="touchMove" 
					@touchstart="touchDown" 
					@touchend="touchUp" 

					>Your Browser does not support Fun things		
					</canvas>
			</div>
			<div class="drawing-toolbar">
				<div class="slider">
					<select  type="range" id="lineWidth" ref="lineWidth" min="1" max="50" value="2" v-on:change="updateLineWidth(); ">
						<option value=1>1px</option>
						<option value=2>2px</option>
						<option value=5>5px</option>
						<option value=10>10px</option>
						<option value=20>20px</option>
						<option value=30>30px</option>
						<option value=40>40px</option>
						<option value=50>50px</option>
						<option value=100>100px</option>
					</select>
				</div>
				<ul>
					<li><button class="tool" v-on:click="openColorModal">Color</button></li>
					<li><button class="tool" value="Pen" id="pen" v-on:click="pen">Pen</button></li>
					<li><button class="tool" type="button" value="Erase" id="erase" v-on:click="color = backgroundColor">Erase</button></li>
					<li><button class="tool"
						type="button" value="Save" id="save"  v-on:click="saveImage()">Save</button></li>
					<li><button class="tool" 
						type="button" value="Load" id="load"  v-on:click="loadImage()">Load</button></li>
					<li><button class="cancel" type="button" value="Clear" id="clear"  v-on:click="eraseImage()">Clear</button></li>
					<li><button class="cancel" type="button" value="Undo" id="undo"  v-on:click="loadLast()">Undo</button></li>
					<li><button class="tool" 
						type="button" value="Bg" id="background" v-on:click="showBGModal = !showBGModal">Bg</button>
					</li>
				</ul>
				<button class="submit" value="Submit" id="submit" v-on:click="submit">Submit</button>
				
					
			</div>
			<div class="hidden">
				<ul class="history">
					<li v-for="(undo, index) in undos" v-if="index < 4"><img class="card" width="20%" :ref="'undos'+index" :src="undo" @click="loadLast()"></li>		
				</ul>
			</div>
			
			<div class="modal" v-if="this.showBGModal" ref="bgModal">
			    <div class="modal-content">
			    <input class="tool theme-selector" type="color" ref="bgColor" value="#000000" id="bgColor" v-on:change="fillBackground()">
			    </div>
			</div>
			<div class="modal" v-if="this.showColorModal" ref="colorModal">
			    <div class="modal-content">
			    <input class="tool" type="color" ref="color" value="#000000" id="color" v-on:change="updateColor()" style="background-color: black;">
			    </div>
			</div>
			
			<img id="saveImg" ref="saveImg" width="160px" height="160px" style="display: none">
		</div>
		<div v-else>
			<PlayersStatus></PlayersStatus>
		</div>	
	</div>
</template>

<script>
	import store from '../store.js'
	import PlayersStatus from './PlayersStatus.vue'
	import Modal from './Modal.vue'
export default{
	name: 'DrawVue',
	props:{
	},
	components:{
		PlayersStatus,
		Modal
	},
	data: function() {
		return {
			dataStore: store.store,
			showBackGroundSelect: false,
			canvas: null, 
			context: null,
			gl: null,
			w: 0,
			h: 0,		
			drawFlag: false,
			prevX: 0,
			curX: 0,
			prevY: 0,
			curY: 0,
			savedColor: '#000000',
			color: '#000000',
			backgroundColor: '#e6e6e6',
			startFlag: false,
			lineWidth: 2,
			undos:[],
			touch: null,
			showColorModal: false,
			showBGModal: false
		}
	},
	mounted: function(){
		this.canvas = this.$refs['theCanvas']; 
		this.context = this.canvas.getContext('2d');
		this.gl = this.canvas.getContext('webgl');
		this.w = this.canvas.width;
		this.h = this.canvas.height;
		this.context.fillStyle=this.backgroundColor;
		this.context.fillRect(0,0,this.w, this.h);
	},
	created(){
		this.dataStore.triggerLoadingScreen();
		this.dataStore.refreshMyLobbyData();
		Event.$on('color-selected', (colour)=>{
			this.$refs['color'].value = colour;
			this.updateColor();
		});
		Event.$on('theme-selected', (theme) => {
			this.updateTheme(theme);

		});

		
	},
	methods:{
		openColorModal: function(){
			console.log("wtf");
			this.showColorModal = true;
		},
		loadLast: function(){
			//todo
			console.log(this.$refs)
			this.eraseImage();
			var img = this.$refs["undos0"][0];
			this.context.drawImage(img,0,0);
			this.undos.shift();
		},
		updateColor: function(){
			this.color = this.$refs['color'].value;
			this.savedColor = this.color;
			this.$refs['color'].style.backgroundColor = this.color;
			this.showColorModal = false;
		},
		updateTheme: function(theme){
			console.log("themed");
			console.log(this.selectTheme);
			this.selectTheme = false;
			console.log(this.selectTheme);
			this.selectedTheme = theme;
		},
		draw: function(){		
			this.context.beginPath();
			this.context.lineCap = "round";
			this.context.moveTo(this.prevX, this.prevY);
			this.context.lineTo(this.curX, this.curY);
			this.context.strokeStyle = this.color;
			this.context.lineWidth = this.lineWidth;
			this.context.stroke();
			this.context.closePath();
		},
		pen: function(){
			this.color = this.savedColor;
		},
		fillBackground: function(){
			var color = this.$refs['bgColor'].value;
			this.showBackGroundSelect = false;
			console.log(color);
			var r0 = parseInt(this.backgroundColor.substring(1,3), 16);
			var g0 = parseInt(this.backgroundColor.substring(3,5), 16);
			var b0 = parseInt(this.backgroundColor.substring(5,7), 16);

			var rN = parseInt(color.substring(1,3), 16);
			var gN = parseInt(color.substring(3,5), 16);
			var bN = parseInt(color.substring(5,7), 16);

			var a = 255
			console.log(rN+" "+gN+" "+bN);
			console.log(this.context.getImageData(0,0,this.w,this.h).data);
			var imageLayer = this.context.getImageData(0,0,this.w,this.h);
			var data = imageLayer.data;
			for (var i = 0; i < data.length; i+=4) {
				if(data[i] == r0 && data[i+1] == g0 && data[i+2] == b0){
					data[i]   = rN;
					data[i+1] = gN;
					data[i+2] = bN;
				}
			}
			this.context.putImageData(imageLayer, 0, 0);


			//lastly
			this.backgroundColor = color;
			this.showBGModal = false;
		},
		
		drawCap: function(){
			
			this.context.beginPath();
			this.context.lineCap = "round";
			this.context.moveTo(this.curX, this.curY);
			this.context.lineTo(this.curX, this.curY);
			this.context.strokeStyle = this.color;
			this.context.lineWidth = this.lineWidth;
			this.context.stroke();
			this.context.closePath();
		},
		eraseImage: function(){
			this.context.clearRect(0, 0, this.w, this.h); 
			this.context.strokeStyle = this.backgroundColor; 
			this.context.fillRect(0,0,this.w, this.h); 
			this.context.strokeStyle = this.color;
		},
		loadImage: function(){
			this.context.clearRect(0, 0, this.w, this.h);
			var img=this.$refs['saveImg'];
			this.context.drawImage(img,0,0);
		},
		saveImage: function(){
			var dataURL = this.canvas.toDataURL();
			this.$refs['saveImg'].src = dataURL;
		},		
		updatePositionData: function(mouse){
			console.log(mouse.clientX +' , '+ mouse.clientY);
			this.prevX = this.curX;
			this.prevY = this.curY;
			this.curX = mouse.clientX - this.canvas.offsetLeft;
			this.curY = mouse.clientY - this.canvas.offsetTop +window.pageYOffset;

			//scale for window viewport resizing
						//the display width
			this.curX /= (this.canvas.offsetWidth/ this.canvas.width);
			this.curY /= (this.canvas.offsetHeight/ this.canvas.height);
			// this.gl.viewport(0, 0, gl.canvas.width, gl.canvas.height);
		},
		updateLineWidth: function(){
			this.lineWidth = this.$refs['lineWidth'].value;
		},
		mouseDown: function(mouse){
			this.updatePositionData(mouse);
			this.drawFlag = true;
			this.startFlag = true;
			if (this.startFlag){
				//might want to do more here
				//save to undo
				this.addToUndos();
				this.startFlag = false;
				this.drawCap();	
			}
		},
		mouseOver: function(mouse){
			this.updatePositionData(mouse);
		},
		mouseUp: function(mouse){
			this.drawFlag = false;
			
		},
		mouseMove: function(mouse){
			if(this.drawFlag){
				this.updatePositionData(mouse);
				this.draw();
			}
		},


		touchDown: function(mouse){
			console.log("touchdown");
			this.updatePositionData(mouse.touches[0]);
			this.drawFlag = true;
			this.startFlag = true;
			if (this.startFlag){
				//might want to do more here
				//save to undo
				this.addToUndos();
				this.startFlag = false;
				this.drawCap();	
			}
		},
		touchUp: function(mouse){
			console.log("touchup");
			this.drawFlag = false;
			
		},
		touchMove: function(mouse){
			console.log("touchmove");
			if(this.drawFlag){
				this.updatePositionData(mouse.touches[0]);
				this.draw();
			}
		},
		addToUndos: function(){
			this.undos.splice(0,0,this.canvas.toDataURL());
			var maxUndos = 10;
			this.undos = this.undos.slice(0, maxUndos);
		},
		submit: function(){
			if(confirm("Did you finish???")){
				this.dataStore.triggerLoadingScreen();
				this.dataStore.submitImage(this.canvas.toDataURL(), function(){
					//callback
					console.log('SUBMITED');
				});
				
			}
		}

	}
}
</script>
<style lang="scss">
.tool{
	border: 0;
	background-color: lightgrey;
	color: black;
	text-decoration: none;
	-webkit-transition-duration: 0.4s; /* Safari */
    transition-duration: 0.4s;
}

ul{
	margin: 0;
}
h1{
	margin: 0;
}
li{
	margin-left: 0;
    margin-right: 0;
}

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
