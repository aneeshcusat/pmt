<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="staticFilesLocation" value="${applicationScope.applicationConfiguraion.staticFilesLocation}"/>
<c:set value="${staticFilesLocation}/css" var="css"/>
<c:set value="${staticFilesLocation}/image" var="image"/>
<c:set value="${staticFilesLocation}/js" var="js"/>
<!DOCTYPE html>
<html lang="en" class="body-full-height">
    <head>        
        <!-- META SECTION -->
        <title>Famstack - Project Scheduler</title>            
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        
        <link rel="icon" href="${fn:escapeXml(image)}/favicon.ico" type="image/x-icon" />
        <!-- END META SECTION -->
        
        <!-- CSS INCLUDE -->        
        <link rel="stylesheet" type="text/css" id="theme" href="${css}/theme-white.css"/>
        <link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/waitme/waitMe.min.css"/>
        <!-- EOF CSS INCLUDE -->                                    
    </head>
    <body>
        
        <div class="login-container">
        
            <div class="login-box animated fadeInDown">
                <div class="login-famstack_logo"><h2 class="text-center">Project scheduler</h2></div>
                <div class="login-body">
                    <div class="login-title"><strong>Reset</strong>, login password</div>
                    <form class="form-horizontal" method="post">
                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="text" class="form-control" placeholder="Email id" id="emailId"/>
                        </div>
                    </div>
                     <div class="form-group">
                        <div class="col-md-12">
                            <input type="password" class="form-control" placeholder="Current Password" id="currentPassword"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="password" class="form-control" placeholder="New Password" id="password"/>
                        </div>
                    </div>
                     <div class="form-group">
                        <div class="col-md-12">
                            <input type="password" class="form-control" placeholder="Confirm Password" id="confPassword"/>
                        </div>
                    </div>
                    <span id="invalidLoginSpan" style="color: red;display: none">Invalid credentials</span>
                    <div class="form-group">
                        <div class="col-md-12">
                           <a class="btn btn-info btn-block" href="javascript:invokeResetAjax()">Reset</a>
                        </div>
                    </div>
                    </form>
                </div>
                <div class="login-footer">
                    <div class="pull-left">
                        &copy; 2016 famstack
                    </div>
                    <div class="pull-right">
                        <a href="#">About</a> |
                        <a href="#">Privacy</a> |
                        <a href="#">Contact Us</a>
                    </div>
                </div>
            </div>
            
        </div>
       <!-- START PLUGINS -->
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery-ui.min.js"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap.min.js"></script>
        <script type="text/javascript" src=" ${js}/plugins/waitme/waitMe.min.js"></script>
        <script type="text/javascript" src="${js}/famstack.ajax.js"></script>   
        <!-- END PLUGINS -->  
 <script type="text/javascript">
 
 var famstackLogEnabled = false;
 function famstacklog(message){
 	famstacklog("", message);
 }
 
	function famstacklog(code, message){
 	if (famstackLogEnabled) {
 		console.log(code, message);
 	}
 }
	
    function makeError(fieldSelector){
    	fieldSelector.css("border", "1px solid red");
    }
    
    function clearInputTextErrors(){
    	$('#emailId').css("border","");
    	$('#password').css("border","");
    	$("#invalidLoginSpan").hide();
    }
    
    function validation(){
    	var status = false;
    	var emailId = $('#emailId').val();
    	var oldPassword = $('#currentPassword').val();
    	var newPassword = $('#password').val();
    	var confPassword = $('#confPassword').val();
    	
    	if (emailId == "") {
    		makeError($('#emailId'));
    		status = true;
    	}
    	if (oldPassword == "") {
    		makeError($('#currentPassword'));
    		status = true;
    	}
    	if (newPassword == "") {
    		makeError($('#emailId'));
    		status = true;
    	}
    	if (confPassword == "") {
    		makeError($('#confPassword'));
    		status = true;
    	}
    	if (confPassword != newPassword) {
    		makeError($('#password'));
    		status = true;
    	}
    	return status;
    }
    
    function invokeResetAjax(){
    	clearInputTextErrors();
    	if(validation()) {
    		return;
    	}
    	famstacklog("jello");
    	var dataString = {"email": $('#emailId').val(), "oldPassword": $('#currentPassword').val() , "password": $('#password').val(), "confPassword": $('#confPassword').val() };
    	
		doAjaxRequest("POST", "/bops/dashboard/changePassword", dataString,
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
        
    </body>
</html>





