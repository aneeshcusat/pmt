var doAjaxRequest = $function(requestType, requestUrl, requestJsonData, successCallBackMethod, errorCallBackMethod) {
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