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
                    <div class="login-title"><strong>Welcome</strong>, Please login</div>
                    <form class="form-horizontal" method="post" id="loginForm">
                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="text" class="form-control" placeholder="Email id" id="emailId" name="email"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="password" class="form-control" placeholder="Password" id="password" name="password"/>
                        </div>
                    </div>
                    <span id="invalidLoginSpan" style="color: red;display: none">Invalid credentials</span>
                    <div class="form-group">
                        <div class="col-md-6">
                            <a href="/bops/dashboard/forgotpassword" class="btn btn-link btn-block">Forgot your password?</a>
                        </div>
                        <div class="col-md-6">
                           <a class="btn btn-info btn-block" href="javascript:invokeLoginAjax()">Log In</a>
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
        <script type="text/javascript" src="${js}/plugins/jquery/jquery.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/jquery/jquery-ui.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src=" ${js}/plugins/waitme/waitMe.min.js?v=${fsVersionNumber}"></script>
        <script type="text/javascript" src="${js}/famstack.ajax.js?v=${fsVersionNumber}"></script>  
        <script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.min.js'></script> 
        <!-- END PLUGINS -->  
 <script type="text/javascript">
 var jvalidate = $("#loginForm").validate({
     ignore: [],
     rules: {                                            
    	 email: {
                     required: true,
                     email: true
             },
             password: {
                     required: true,
                     minlength: 5
             }  
     	}
     });
    
    $('#emailId').keypress(function(e) {
        if(e.which == 10 || e.which == 13) {
        	invokeLoginAjax();
        }
    });
    $('#password').keypress(function(e) {
        if(e.which == 10 || e.which == 13) {
        	invokeLoginAjax();
        }
    });
    
    function invokeLoginAjax(){
    	if(!$("#loginForm").valid()) {
    		return;
    	}
    	var dataString = {"email": $('#emailId').val() , "password": $('#password').val() };
    	
		doAjaxRequest("POST", "/bops/dashboard/loginAjax", dataString,
					function(response) {
						$("#invalidLoginSpan").hide();
						var responseJsonObj = JSON.parse(response);
						if (responseJsonObj.status == true) {
							var currentUrl = window.location.href;
							if (currentUrl.indexOf("/dashboard/index") > 0 || currentUrl.indexOf("/dashboard/logout") > 0 || currentUrl.indexOf("/dashboard/login") > 0){
								window.location = "/bops/dashboard/index";
							} else {
								window.location.reload(true);
							}
						} else if(responseJsonObj.status == false && responseJsonObj.passwordreset == true)  {
							window.location = "/bops/dashboard/resetpassword?key="+encodeURIComponent(responseJsonObj.key)+"&uid="+responseJsonObj.uid;
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





