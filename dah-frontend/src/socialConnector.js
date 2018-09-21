const socialConnector = {
	TestLogin: function(){
		FB.login(function(response){
			console.log(response);
		},{
			scope: 'email,user_likes'
		});
		FB.getLoginStatus(function(response) {
			console.log("getLoginStatus");
    		console.log(response);
		});
	},
	TestFBGet: function(){
		console.log('auth response: '+ FB.getAuthResponse());
		FB.api(
		    "/904051693125724",
		    {

		    },
		    function (response) {
		      if (response && !response.error) {
		      	console.log("All good")
		        console.log(response);
		      }else if(response && response.error){
		      	console.log("response error")
		      	console.log(response.error);
		      }else{
		      	console.log("no response");
		      }
		    }
		)
	},
	TestTwitterImagePost:function(){
		// client key: gCboW23uWsEZYmgI91qP3bJf1
	}
}
export default{socialConnector}

