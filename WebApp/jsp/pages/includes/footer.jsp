<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="userAccessedSystemToday" value="${applicationScope.applicationConfiguraion.userAccessedSystemToday}"/>
<c:set var="currentUserId" value="${applicationScope.applicationConfiguraion.currentUserId}"/>
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
                     	<p class="hide taskInProgressWarningMsg" ><span style="color: red;font-size: 13pt;font-weight: bold;">Tasks are In-Progress make sure to pause or complete it!</span><a href="${applicationHome}/tasks"> click here</a></p>
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

if(${userAccessedSystemToday}){
	invokeUserActivityTracker(${currentUserId});
}

function invokeUserActivityTracker(userId){
	doAjaxRequestWithGlobal("GET", "${applicationHome}/trackUserActivity",  {"userId":userId},function(data) {
	},function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}

function updateUserActivity(userId, activityDateString, status){
	doAjaxRequestWithGlobal("GET", "${applicationHome}/trackUserActivity",  {"userId":userId,"activityDateString":activityDateString,"status":status},function(data) {
	},function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}


function userPingCheck(){
	doAjaxRequestWithGlobal("POST", "${applicationHome}/userPingCheck",  {},function(data) {
    	var userStatus = JSON.parse(data);
    	
    	$.each(userStatus, function(idx, elem){
    		$(".userOnline"+elem.userId).removeClass("status-online");
    		$(".userOnline"+elem.userId).removeClass("status-away");
    		$(".userOnline"+elem.userId).removeClass("status-offline");
    		$("#sortDivData"+elem.userId).attr("data-sort", elem.status);
    		if (elem.status == 0) {
    			$(".userOnline"+elem.userId).addClass("status-offline");
    		} else if (elem.status == 1) {
    			$(".userOnline"+elem.userId).addClass("status-away");
    		} else if (elem.status == 5) {
    			$(".userOnline"+elem.userId).addClass("status-online");
    		}
    		$("#availableAfter"+elem.userId).html(elem.userAvailableMsg);
    		changeDashBoardOnlineStatus(elem.userId, elem.userAvailableMsg, elem.status);
    	});
    	sortOnlineStatus();
    },function(error) {
    	famstackLoginCheck("ERROR: ", error);
    },false);
}

function changeDashBoardOnlineStatus(userId, userAvailableMsg, status){
	
	$(".onlineStatus"+userId).removeClass("red");
	$(".onlineStatus"+userId).removeClass("green");
	
	$(".availabilityStatus"+userId).removeClass("red");
	$(".availabilityStatus"+userId).removeClass("green");
	
	if ("Available" == userAvailableMsg){
		$(".onlineStatus"+userId).addClass("green");
	} else {
		$(".onlineStatus"+userId).addClass("red");
	}
	
	if (status == 0) {
		$(".availabilityStatus"+userId).addClass("gray");
	} else if (status == 1) {
		$(".availabilityStatus"+userId).addClass("yellow");
	} else if (status == 5) {
		$(".availabilityStatus"+userId).addClass("green");
	}
	
	$(".onlineStatus"+userId).attr("title", userAvailableMsg);	
}

function userNotifications(){
	doAjaxRequestWithGlobal("GET", "${applicationHome}/getNotifications",  {},function(data) {
    	var notifications = JSON.parse(data);
    	processNotification(notifications);
		famstacklog("notifications: ", notifications);
    },function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}

function userMessageNotifications(){
	doAjaxRequestWithGlobal("GET", "${applicationHome}/getMessageNotifications",  {},function(data) {
    	var msgNotifications = JSON.parse(data);
    	processMessageNotification(msgNotifications);
		famstacklog("notifications: ", msgNotifications);
    },function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}

var tasksCount = 0;

function updateTaskNotification(){
	tasksCount = 0
	$("#mCSB_4_container").html("");
	doAjaxRequestWithGlobal("GET", "${applicationHome}/listTaskListJson",  {},function(data) {
    	var notifications = JSON.parse(data);
    	processTaskNotification(notifications.COMPLETED);
    	processTaskNotification(notifications.ASSIGNED);
    	processTaskNotification(notifications.INPROGRESS);
    	
    	checkInprogressTasks(notifications.INPROGRESS);
		famstacklog("task notification: ", notifications);
		$(".taskNotification").html(tasksCount);
    },function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}

function checkInprogressTasks(notifications){
	$(".taskInProgressWarningMsg").addClass("hide");
	$.each(notifications, function(idx, elem){
		if(elem.taskPausedTime == null) {
			$(".taskInProgressWarningMsg").removeClass("hide");
			return;
		}
	});
}

function processTaskNotification(notifications){
	$.each(notifications, function(idx, elem){
		famstacklog(tasksCount);
		var taskName = elem.name;
		var taskPercentage=elem.percentageOfTaskCompleted;
		var taskDate = null;
		var actualStartTime=elem.taskActivityDetails.actualStartTime; 
		if (actualStartTime != null) {
			taskDate=getTodayDateTime(new Date(actualStartTime));
		}
		
		if (taskDate == null) {
			taskDate=elem.startTime;
		}
		 var taskHealth = "info";
         if (elem.taskActivityDetails < 1) {
        	 taskHealth ="danger";
         } else if (taskPercentage == 100){
        	 taskHealth ="success";
         }
         
         
		var notificationMessage =  '<a class="list-group-item"  target="_new" href="${applicationHome}/tasks"><strong>'+taskName+'</strong><div class="progress progress-small progress-striped active"><div class="progress-bar progress-bar-'+taskHealth+'" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: '+taskPercentage+'%;">'+taskPercentage+'%</div></div><small class="text-muted">'+taskDate+' / '+taskPercentage+'%</small></a>';
		$("#mCSB_4_container").prepend(notificationMessage);
		tasksCount++;
	});
}


var messagesMap = [
{key:"default",value:"famstack notification!"},
{key:"userRegistraion",value:"You have been registered in famstack"},
{key:"resetPassword",value:"Your account password has been reset!"},
{key:"forgotPassword",value:"Your account password has been reset!"},
{key:"userDetailsUpdate",value:"Your account details have been updated!"},
{key:"projectCreated",value:"Project has been created!"},
{key:"projectDetailsUpdate",value:"Project has been updated!"},
{key:"projectDelete",value:"Project has been deleted!"},
{key:"projectTaskCreated",value:"Project task has been created!"},
{key:"projectTaskDetailsUpdate",value:"Project task has been updated!"},
{key:"projectTaskDelete",value:"Project task has been deleted!"},
{key:"projectTaskCreateAssigned",value:"Project task has been created and assigned!"},
{key:"projectTaskAssigned",value:"Project task has been assigned!"},
{key:"projectTaskClosed",value:"Project task has been closed!"},
{key:"projectTaskInProgress",value:"Project task has been in progress!"},
{key:"projectTaskCompleted",value:"Project task has been completed!"},
{key:"projectCommentAdded",value:"Project comments added!"},
{key:"projectEndReminder",value:"Project should be completed in 5 mins!"},
{key:"projectStartReminder",value:"Project should start in 5 mins!"},
{key:"projectDeadLineMissed",value:"Project has missed the deadline!"},
{key:"projectTaskDeadLineMissed",value:"Project task has missed the deadline!"},
{key:"projectTaskEndReminder",value:"Project task should be completed in 5 mins!"},
{key:"projectTaskStartReminder",value:"Project task should start in 5 mins!"},
];

function getMessageFromKey(key){
	var message ="";
	$.each(messagesMap, function(idx, elem){

		if(""+elem.key === key) {
			message = elem.value;
		}
	});
	
	return message;
}

function processNotification(notification){
	$("#mCSB_3_container").html("");
	$("#notificationPageDiv").html("");
	var newMessages = parseInt($("#newNotification").html());
	$.each(notification, function(idx, elem){
		famstacklog(newMessages);
		var noficationType = elem.notificationType;
		var message = elem.data.name ;
		var titileMsg = getMessageFromKey(elem.data.notificationKey);
		if (typeof message == 'undefined') {
			message = "Notification";
		}
		if (elem.read == false) {
			newMessages++;
			 $.notify(message, {
			        title: titileMsg,
			        icon:'${fn:escapeXml(image)}/favicon.ico'
			 }).click(function(){
			        location.href = "${applicationHome}/project/"+elem.data.id;
		      });
		}
		var notificationMessage = '<a id="projectLink" target="_new" href="${applicationHome}/project/'+elem.data.id+'" class="list-group-item"><span class="contacts-title">'+titileMsg+'</span><p>'+message+'</p></a>'
		$("#mCSB_3_container").prepend(notificationMessage);
		
		if (typeof fillNotificationPage !== 'undefined' && $.isFunction(fillNotificationPage)) {
			fillNotificationPage(elem);
		}
	});
	$(".newNotification").html(newMessages);
}

function processMessageNotification(notification){
	$("#mCSB_2_container").html("");
	var newMessages = parseInt($("#newMessageNotification").html());
	$.each(notification, function(idx, groupElem){
		famstacklog(groupElem);
		$.each(groupElem, function(idx, elem){
			famstacklog(elem);
			var groupId = elem.group;
			var groupName = elem.groupName;
			var userName = elem.userFullName;
			var message = userName + " says : " + elem.description;
			if (elem.read == false) {
				newMessages++;
				 $.notify(message, {
				        title: "Message from " + groupName,
				        icon:'${fn:escapeXml(image)}/favicon.ico'
				 }).click(function(){
				        location.href = "${applicationHome}/messages";
			      });
			}
			var notificationMessage = '<a id="projectLink" target="_new" href="${applicationHome}/messages" class="list-group-item"><span class="contacts-title">'+groupName+'</span><p>'+message+'</p></a>'
			$("#mCSB_2_container").prepend(notificationMessage);
		});
	});
	$(".newMessageNotification").html(newMessages);
}

userPingCheck();
userNotifications();
updateTaskNotification();
userMessageNotifications()

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
    	userMessageNotifications();
    	updateTaskNotification();
    }
    idleTime = idleTime + 1;
}

function sortOnlineStatus(){
	$('.sortDiv').html($('.sortDiv .sortDivData').sort(function (a, b) {

	      var contentA =parseInt( $(a).attr('data-sort'));
	      var contentB =parseInt( $(b).attr('data-sort'));
	      return (contentA > contentB) ? -1 : (contentA < contentB) ? 1 : 0;
	   }));
}


</script>
