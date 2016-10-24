<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<spring:url value="/jsp/css" var="css" htmlEscape="true"/>
<spring:url value="/jsp/js" var="js" htmlEscape="true"/>
<!DOCTYPE html>
<html lang="en" class="body-full-height">
    <head>        
        <!-- META SECTION -->
        <title>Famstack - Project Scheduler</title>            
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        
        <link rel="icon" href="favicon.ico" type="image/x-icon" />
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
                           <a class="btn btn-info btn-block" href="javascript:invokeForgotPasswordAjax()">Reset</a>
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
 
    
    function invokeForgotPasswordAjax(){
    	console.log("jello");
    	var dataString = {"email": $('#emailId').val()};
    	
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
        
    </body>
</html>





