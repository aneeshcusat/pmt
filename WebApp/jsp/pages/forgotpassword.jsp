<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="tracopusConfigEnabled" value="${applicationScope.applicationConfiguraion.tracopusConfigEnabled}"/>
<c:set var="staticFilesLocation" value="${applicationScope.applicationConfiguraion.staticFilesLocation}"/>
<c:set value="${staticFilesLocation}/css" var="css"/>
<c:set value="${staticFilesLocation}/image" var="image"/>
<c:set value="${staticFilesLocation}/js" var="js"/>
<!DOCTYPE html>
<html lang="en">
    <head>        
        <!-- META SECTION -->
        <title><c:if test="${tracopusConfigEnabled == false}">Famstack</c:if><c:if test="${tracopusConfigEnabled == true}">Tracopus</c:if> - Project Scheduler</title>            
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
           <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <link rel="icon" href="${fn:escapeXml(image)}/favicon.ico" type="image/x-icon" />
        <!-- END META SECTION -->
        
        <!-- CSS INCLUDE -->        
       <link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/bootstrap/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/waitme/waitMe.min.css"/>
        <link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/login.css"/>
        <!-- EOF CSS INCLUDE -->                
       <style type="text/css">
		.login_content .btn:hover, .login_content a {
    		text-decoration: none;
		}
		.login_content h1 {
		    font-size:18px;
		    opacity: .5;
		}
		.login_content form input[type=text], .login_content form input[type=email], .login_content form input[type=password] {
			margin: 20px 0 0px;	
		}
        </style>
    </head>
  <body class="login">
    <div>
      <div class="login_wrapper">
        <div class="animate form login_form">
          <section class="login_content">
          	   <div>
                 <c:if test="${tracopusConfigEnabled == false}"><img alt="" src="${fn:escapeXml(image)}/favicon.ico" style="width: 25px;height: 25px;margin-top: 0;margin-right: 5px;padding-top: 0;vertical-align: unset"/><span style="font-size: 35px;color: gray;font-weight: 200;opacity:.7">famstack</span></c:if>
                 <c:if test="${tracopusConfigEnabled == true}"><img style="height: 40px" alt="" src="${fn:escapeXml(image)}/tracopus_logo_trans.png"/></c:if>
               </div>
              <form class="form-horizontal" method="post" id="forgotForm">
              <h1>Reset your password</h1>
             <div>
                <input type="email" class="form-control" placeholder="Email id" id="emailId" name="email"/>
              </div>
              <span id="invalidLoginSpan" style="color: red;display: none">Unable to recognize email id</span>
              <div>
                <a class="btn btn-info btn-block submit" href="javascript:invokeForgotPasswordAjax()">Reset</a>
                 <a class="reset_pass" href="/bops/dashboard/login">Remember your password?</a>
              </div>

              <div class="clearfix"></div>

              <div class="separator">
                <div class="clearfix"></div>
                <br>

                <div>
                  <h1><span  style="opacity:.5;font-size: 18px"> Project Scheduler</span></h1>
                  <p style="opacity:.7">©2016 All Rights Reserved. Powered by <c:if test="${tracopusConfigEnabled == true}">Infleca Innovation  Pvt Ltd</c:if><c:if test="${tracopusConfigEnabled == false}">Credencia Business Solutions LLP</c:if>, Privacy and Terms</p>
                </div>
              </div>
            </form>
          </section>
        </div>
    </div>
  </div>
       <!-- START PLUGINS -->
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery-ui.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap.min.js"></script>
        <script type="text/javascript" src=" ${js}/plugins/waitme/waitMe.min.js"></script>
        <script type="text/javascript" src="${js}/famstack.ajax.js"></script>  
        <script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.min.js'></script> 
        <!-- END PLUGINS -->  
  <script type="text/javascript">
  
  var jvalidate = $("#forgotForm").validate({
	     ignore: [],
	     rules: {                                            
	    	 email: {
	                     required: true,
	                     email: true
	             }
	     	}
	     });
	    
	    $('#emailId').keypress(function(e) {
	        if(e.which == 10 || e.which == 13) {
	        	invokeForgotPasswordAjax();
	        }
	    });
	    
 var famstackLogEnabled = false;
 function famstacklog(message){
 	famstacklog("", message);
 }
 
	function famstacklog(code, message){
 	if (famstackLogEnabled) {
 		console.log(code, message);
 	}
 }
    
    function invokeForgotPasswordAjax(){
    	if(!$("#forgotForm").valid()) {
    		return;
    	}
    	var emailId = $('#emailId').val();
    	famstacklog("emailId - " + emailId);
    	var dataString = {"email": emailId.trim()};
    	
		doAjaxRequest("POST", "/bops/dashboard/forgotPassword", dataString,
		function(response) {
			$("#invalidLoginSpan").hide();
			var responseJsonObj = JSON.parse(response);
			if (responseJsonObj.status == true) {
				window.location = "/bops/dashboard/login";
			} else {
				$("#invalidLoginSpan").show();
			}
		}, function(e) {
			$("#invalidLoginSpan").show();
		});
		}
	</script>
</body></html>