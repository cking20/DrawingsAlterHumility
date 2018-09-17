<template>
	<div class = drawing-app>
		<H1>Draw: Something</H1>
			<div>
				<canvas class="drawing-area" ref="theCanvas" width="640px" height="600px"  
					@mousemove="mouseMove" 
					@mousedown="mouseDown" 
					@mouseup="mouseUp" 
					@mouseover="mouseOver"

					@touchmove.prevent="mouseMove" 
					@touchstart="mouseDown" 
					@touchend="mouseUp" 

					>Your Browser does not support Fun things		
					</canvas>
			</div>
			<div class="horizontal drawing-toolbar">
				<ul>
					<li><input class="tool slider" type="range" id="lineWidth" ref="lineWidth" min="1" max="50" value="2" v-on:change="updateLineWidth()"></li>
					<li><input class="tool" 
						type="color" ref="color" value="#000000" id="color" v-on:change="updateColor()" >
					</li>
					<li><input class="tool" type="button" value="Pen" id="pen" v-on:click="pen"></li>
					<li><input class="tool" type="button" value="Erase" id="erase" v-on:click="color = backgroundColor"></li>
					<li><input class="tool"
						type="button" value="Save" id="save"  v-on:click="saveImage()"></li>
					<li><input class="tool" 
						type="button" value="Load" id="load"  v-on:click="loadImage()"></li>
					<li><input class="tool" type="button" value="Clear" id="clear"  v-on:click="eraseImage()"></li>
					
					<li><input class="tool" 
						type="button" value="Bg" id="background" v-on:click="showBackGroundSelect = !showBackGroundSelect">
						<input v-if="showBackGroundSelect" class="tool theme-selector" type="color" ref="bgColor" value="#000000" id="bgColor" v-on:change="fillBackground()">
					</li>
					<li><input class="tool start" type="button" value="Submit" id="submit" v-on:click="submit" style="width: auto; float: right;">
					</li>
				</ul>
				
					
			</div>
			<div class="horizontal">
				<ul class="history">
					<li v-for="(undo, index) in undos" v-if="index < 4"><img width="160px" height="160px" :ref="'undos'+index" :src="undo" @click="loadLast()"></li>		
				</ul>
			</div>
			
			<img id="saveImg" ref="saveImg" width="160px" height="160px" style="display: none">
	</div>
</template>

<script>
	import store from '../store.js'
export default{
	name: 'DrawingApp',
	props:{
	},
	components:{
	},
	data: function() {
		return {
			dataStore: store.store,
			showBackGroundSelect: false,
			canvas: null, 
			context: null,
			w: 0,
			h: 0,		
			drawFlag: false,
			prevX: 0,
			curX: 0,
			prevY: 0,
			curY: 0,
			color: '#000000',
			backgroundColor: '#e6e6e6',
			startFlag: false,
			lineWidth: 2,
			undos:[]
		}
	},
	mounted: function(){
		this.canvas = this.$refs['theCanvas']; 
		this.context = this.canvas.getContext('2d');
		this.w = this.canvas.width;
		this.h = this.canvas.height;
		this.context.fillStyle=this.backgroundColor;
		this.context.fillRect(0,0,this.w, this.h);
	},
	created(){
		Event.$on('color-selected', (colour)=>{
			this.$refs['color'].value = colour;
			this.updateColor();
		});
		Event.$on('theme-selected', (theme) => {
			this.updateTheme(theme);

		});

		
	},
	methods:{	
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
			this.color = this.$refs['color'].value;
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
			this.prevX = this.curX;
			this.prevY = this.curY;
			this.curX = mouse.clientX - this.canvas.offsetLeft-10;
			this.curY = mouse.clientY - this.canvas.offsetTop +window.pageYOffset-10;
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
		addToUndos: function(){
			this.undos.splice(0,0,this.canvas.toDataURL());
			var maxUndos = 10;
			this.undos = this.undos.slice(0, maxUndos);
		},
		submit: function(){
			if(confirm("Did you finish???")){
				this.dataStore.submitImage(this.canvas.toDataURL(), function(){
					//callback
					console.log('SUBMITED');
				});
				
			}
		}
	}
}
</script>