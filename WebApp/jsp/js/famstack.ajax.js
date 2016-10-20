var doAjaxRequest = function(requestType, requestUrl, requestJsonData, successCallBackMethod, errorCallBackMethod) {
   $.ajax({
       type : requestType,
       url : requestUrl ,
       data: requestJsonData,
       timeout : 6000,
       success : function(data) {
        	   successCallBackMethod(data);
        	   $(".page-container").waitMe('hide');
       },
       error : function(error) {
           console.log("ERROR: ", error);
           errorCallBackMethod(error);
           $(".page-container").waitMe('hide');
       },
       done : function(e) {
           console.log("DONE");
           $(".page-container").waitMe('hide');
       }
   });
}


$( document ).ajaxStart(function() {
	 $(".page-container").waitMe({
			effect : "bounce",
			text : "",
			bg : 'rgba(255,255,255,0.7)',
			color : "#000"
			});
	}).ajaxStop(function() {
		$(".page-container").waitMe('hide');
	});

Date.prototype.addHours = function(h) {    
	   this.setTime(this.getTime() + (h*60*60*1000)); 
	   return this;   
}

var getTodayDate = function(today){
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){
	    dd='0'+dd
	} 
	if(mm<10){
	    mm='0'+mm
	} 
	return yyyy+'/'+mm+'/'+dd;
}