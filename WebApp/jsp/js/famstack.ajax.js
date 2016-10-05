var doAjaxRequest = function(requestType, requestUrl, requestJsonData, successCallBackMethod, errorCallBackMethod) {
   $.ajax({
       type : requestType,
       url : requestUrl ,
       data: requestJsonData,
       timeout : 1000,
       success : function(data) {
        	   successCallBackMethod(data);
       },
       error : function(error) {
           console.log("ERROR: ", error);
           errorCallBackMethod(error);
       },
       done : function(e) {
           console.log("DONE");
       }
   });
}

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