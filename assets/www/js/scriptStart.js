

	function clockTick()
	{
		var d=new Date();
		var t=d.toLocaleTimeString();

		
	 	$.ajax("http://myApp.example.org/time").complete(function (data) {
                    // get the resposne object from our request to native
                    var responseObj = $.parseJSON(data.responseText);                    
                    drawActions(responseObj.contacts);
                });
	}
	
	var secound = 0;
	
	function clockTickSecound()
	{
		secound += 1;
		
		var minutes = Math.floor(secound / 60);
		var seconds = secound - minutes * 60;
		
		if (seconds < 10 & minutes < 10 )		
			$("#time").html("0" + minutes + ":0" + seconds);
		else if (minutes < 10 )		
			$("#time").html("0" + minutes + ":" + seconds);	
		else		
			$("#time").html(minutes + ":" + seconds);					
	}
	
	$(document).ready(function () {
		
 		var int=self.setInterval(function(){clockTick();},100); 		
 		var int=self.setInterval(function(){clockTickSecound();},1000);
	
		$.ajax("http://myApp.example.org/start").complete(function (data) { });
			
		
	});

    var drawActions = function (actions) {    	
		
		var action=actions.split(":");
		var volume = action[0] * 300 ;
		var acc = action[1] * 300 ;			
			
		$("#volume").css("width", volume);
		$("#movement").css("width", acc);		
			
	};
       
	function doit (actions) {    	
    	
		var action=actions.split(":"); 
			
		var volume = action[0] * 300 ;
		var acc = action[1] * 300 ;			
			
		$("#volume").css("width", volume);
		$("#movement").css("width", acc);			
			
	};	   
      
        
        
        
        
        
