<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
            </div>            
            <!-- END PAGE CONTENT -->
               
        </div>
        <!-- END PAGE CONTAINER -->
        
        <!-- MESSAGE BOX-->
        <div class="message-box animated fadeIn" data-sound="alert" id="mb-signout">
            <div class="mb-container">
                <div class="mb-middle">
                    <div class="mb-title"><span class="fa fa-sign-out"></span> Log <strong>Out</strong> ?</div>
                    <div class="mb-content">
                        <p>Are you sure you want to log out?</p>                    
                        <p>Press No if you want to continue work. Press Yes to logout current user.</p>
                    </div>
                    <div class="mb-footer">
                        <div class="pull-right">
                            <a href="${applicationHome}/logout" class="btn btn-success btn-lg">Yes</a>
                            <button class="btn btn-default btn-lg mb-control-close">No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END MESSAGE BOX-->
        
         <!-- MESSAGE BOX-->
        <div class="message-box message-box-danger animated fadeIn" data-sound="alert" id="confirmationbox">
            <div class="mb-container">
                <div class="mb-middle">
                    <div class="mb-title"><span class="fa fa-times"></span> <span class="msgConfirmText"></span> <strong  class="msgConfirmText1"></strong> ?</div>
                    <div class="mb-content">
                        <p>Do you really want to <span class="msgConfirmText"></span>?</p>                    
                        <p>Press No if you want to continue work. Press Yes to <span class="msgConfirmText"></span> <strong  class="msgConfirmText1"></strong>.</p>
                    </div>
                    <div class="mb-footer">
                        <div class="pull-right">
                            <a href="#" class="btn btn-success btn-lg" id="confirmYesId">Yes</a>
                            <button class="btn btn-default btn-lg mb-control-close">No</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END MESSAGE BOX-->
          <%@include file="scripts.jsp" %>  
    </body>
</html>
<script>
function userPingCheck(){
	doAjaxRequestWithGlobal("POST", "${applicationHome}/userPingCheck",  {},function(data) {
    	console.log("userPingCheck: ", data);
    },function(error) {
    	console.log("ERROR: ", error);
    },false);
}

function userNotifications(){
	doAjaxRequestWithGlobal("GET", "${applicationHome}/getNotifications",  {},function(data) {
    	var notifications = JSON.parse(data);
    	processNotification(notifications);
		console.log("notifications: ", notifications);
    },function(error) {
    	console.log("ERROR: ", error);
    },false);
}

function processNotification(notification){
	$("#mCSB_2_container").html("");
	$("#notificationPageDiv").html("");
	var newMessages = parseInt($("#newNotification").html());
	$.each(notification, function(idx, elem){
		if (elem.read == false) {
			newMessages++;
		}
		console.log(newMessages);
		var noficationType = elem.notificationType;
		var message = elem.data.name + "has been created";
		var notificationMessage = '<a id="projectLink" target="_new" href="${applicationHome}/project/'+elem.data.id+'" class="list-group-item"><span class="contacts-title">'+noficationType+'</span><p>'+message+'</p></a>'
		$("#mCSB_2_container").prepend(notificationMessage);
		$(".newNotification").html(newMessages);
		
		if ($.isFunction(fillNotificationPage)) {
			fillNotificationPage(elem);
		}
	});
}

userNotifications();

var idleTime = 1;
$(document).ready(function () {
    var idleInterval = setInterval(timerIncrement, 30000); // 30 seconds

    $(this).mousemove(function (e) {
        idleTime = 0;
    });
    $(this).keypress(function (e) {
        idleTime = 0;
    });
});

function timerIncrement() {
    if (idleTime < 2) { // 1 minutes
    	userPingCheck();
    	userNotifications();
    }
    idleTime = idleTime + 1;
}

</script>
