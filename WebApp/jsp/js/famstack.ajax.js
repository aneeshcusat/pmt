function doAjaxRequest(requestType, requestUrl, requestJsonData, successCallBackMethod, errorCallBackMethod) {
   $.ajax({
       type : requestType,
       contentType : "application/json",
       url : requestUrl ,
       data: requestJsonData,
       timeout : 1000,
       beforeSend: function(xhr) {
           xhr.setRequestHeader("Accept", "application/json");
           xhr.setRequestHeader("Content-Type", "application/json");
       },
       success : function(data) {
           if (data.status){
        	   successCallBackMethod();
           }
       },
       error : function(e) {
           console.log("ERROR: ", e);
           errorCallBackMethod();
       },
       done : function(e) {
           console.log("DONE");
       }
   });
}