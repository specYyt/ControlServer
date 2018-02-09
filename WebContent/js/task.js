function send(){
	     $.ajax({
	          type: "POST",
	          url: "generate.action",
	          dataType: "json",
	          success: function(data){
	        	  $("#token").val(data.token);
	          },
	          error: function(jqXHR){
	        	  
	          }
	     });
}