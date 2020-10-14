<%@include file="includes/header.jsp"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="sameDayOnlyTaskEnabled" value="${applicationScope.applicationConfiguraion.sameDayOnlyTaskEnabled}"/>
<c:set var="futureHourCaptureDisabled" value="${applicationScope.applicationConfiguraion.futureHourCaptureDisabled}"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/tasks.css?version=3.3&v=${fsVersionNumber}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userDetailsMap"
	value="${applicationScope.applicationConfiguraion.userMap}" />
<ul class="breadcrumb">
	<li><a href="${applicationHome}/index">Home</a></li>
	<li class="active">Tasks</li>
</ul>
<style>

<c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
.task-item {
	display: none;
  }
</c:if>
</style>
<script>
 var taskTimerMap = {};
  
 function getCompletedTaskTime(taskId){
	 if (!$(".taskPlayPause"+taskId).hasClass("fa-pause")){
		 return;
	 }
	 
	 $("."+taskId+".durationDiv").remove();
	 $("."+taskId+".taskRemainingDiv").show();
	 
	 var actualTaskStartTime = new Date($("."+taskId+".taskRemainingDiv .actualTaskStartTime").html()).getTime();
	 var currentTime = new Date().getTime();
	 
	 var timeDiff =  Math.abs(currentTime - actualTaskStartTime);
	 
	 var hour = parseInt(timeDiff/(3600*1000));
	 var minutes =  parseInt((timeDiff/(60*1000))%60);
	 var second = parseInt((timeDiff/(1000))%60);
	 
	 if (minutes < 10) {
		minutes = "0" + minutes;
	}
	
	if (second < 10) {
		second = "0" + second;
	}
	
	if (hour <10) {
		hour = "0" + hour;
	}
	 
	$("."+taskId+".taskRemainingDiv .taskHour").html(hour);
	$("."+taskId+".taskRemainingDiv .taskMinutes").html(minutes);
	$("."+taskId+".taskRemainingDiv .taskSeconds").html(second);
	 	
 }
 </script>
<!-- START CONTENT FRAME -->
<div class="content-frame" ng-app="mytasks">
	<!-- START CONTENT FRAME TOP -->
	<div class="content-frame-top">
		<div class="page-title">
			<h2>
				<span class="fa fa-tasks"></span> Tasks
			</h2>
		</div>
	</div>
	<!-- END CONTENT FRAME TOP -->
	
		<div class="row ">
			<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body">
					<form class="form-horizontal">
						<div class="form-group">
							<div class="col-md-4">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="taskActivitySearchId" placeholder="Search for a task">
								</div>
							</div>
							
							<div class="col-md-2">
								 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
								<select id="taskAssigneeId" name="taskAssigneeId" class="form-control select" data-live-search="true">
									<option value="">All</option>
										 <c:if test="${not empty userMap}">
											<c:forEach var="user" items="${userMap}">
												 <c:if test="${currentUser.id eq user.id}">
												 	<option selected="selected" value="userId${user.id}">${user.firstName}</option>
												 </c:if>
												  <c:if test="${currentUser.id ne user.id}">
												  <option value="userId${user.id}">${user.firstName}</option>
												  </c:if>
							  				</c:forEach>
							  			</c:if>
									</select>
								</c:if>
							</div>
							
							<div class="col-md-2"> 
								<div class="form-group">
                                <div class="col-md-10">                                            
                                       <select id="taskFilterDayId" name="taskFilterDayId" class="form-control select" data-live-search="true">
                                      		<c:if test="${empty param.dayfilter}">
												<c:set var="dayfilterParam" value="15"/>
											</c:if>
											<c:if test="${not empty param.dayfilter}">
												<c:set var="dayfilterParam" value="${param.dayfilter}"/>
											</c:if>
											<c:forEach begin="1" end="365" varStatus="loop" step="1">
												<c:if test="${dayfilterParam eq loop.current}">
                                        		<option value="${loop.current}" selected="selected">Last ${loop.current} Days Tasks</option>
                                        		</c:if>
                                        		<c:if test="${dayfilterParam ne loop.current}">
                                        		<option value="${loop.current}">Last ${loop.current} Days Tasks</option>
                                        		</c:if>
                                 			</c:forEach>
										</select>
                                </div>
                            </div>
							</div>
							<div class="col-md-1"> 
				            <a href="javascript:showGridEmployeeDetails();" id="employeesDetailsGridLink"  style="margin-right: 10px;" class="blueColor hide"><span class="fa fa-th-large fa-3x"></span></a>
                            <a href="javascript:showListEmployeeDetails();" id="employeesDetailsListLink" class="hide"><span class="fa fa-tasks fa-3x"></span></a>
							</div>
							<div class="col-md-3">
								  
								  <a data-toggle="modal" data-target="#unbillableTaskCreationModal" onclick="clearUnbillableFormForCreate(${currentUser.id})"
									class="btn btn-success btn-block"> <span class="fa fa-plus"></span>
									Record Non-billable Time.
									</a>
								
							</div>
						</div>
					</form>
				</div>
			</div>

		</div>

		</div>

	<!-- START CONTENT FRAME BODY -->
	    <div class="row ">
			<%@include file="fagments/tasksInfoDetailsGrid.jspf"%>
		</div>
		 <div class="row ">
			<%@include file="fagments/tasksInfoDetailsList.jspf"%>
		</div>
	<!-- END CONTENT FRAME BODY -->
</div>
<!-- END CONTENT FRAME -->

<!-- task completion modal start -->
<div class="modal fade" id="taskCompletionModal" tabindex="-1"
	role="dialog" aria-labelledby="taskCompletionModal" aria-hidden="true">
	<form:form id="taskCompletionModal" action="taskCompletion"
		method="POST" role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Task Completion</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/taskCompletionModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary taskWindowCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick="taskComplete()" class="btn btn-primary">
						<span id="taskComplete" >Complete</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- project create modal end -->

<!-- task completion modal start -->
<div class="modal fade" id="taskStartModal" tabindex="-1" role="dialog"
	aria-labelledby="taskStartModal" aria-hidden="true">
	<form:form id="taskCompletionModal" action="taskStart" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Start this task</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/taskStartModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary taskWindowCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick="taskStart()" class="btn btn-primary">
						<span id="taskStart" >Start</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>

<div class="modal fade" id="taskDetailsModal" tabindex="-1"
	role="dialog" aria-labelledby="#taskDetailsModal" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Task Details</h4>
			</div>
			<div class="modal-body">
				<%@include file="fagments/taskDetailsModal.jspf"%>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<button id="taskReassign" onclick="reAssignTask()" type="button" class="btn btn-primary hide">
					<span >Re Assign Task</span>
				</button>
			</div>
		</div>
	</div>
</div>
<!-- project create modal end -->


<!-- unbilled task modal start -->
<div class="modal fade" id="unbillableTaskCreationModal" tabindex="-1" data-backdrop="static"
	role="dialog" aria-labelledby=""createUnbillableModal"" aria-hidden="true">
	<form:form id="unbillableTaskCreationModal" action="unbillableTaskCreationModal" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="unbilledModelTitle">Create Non-billable time</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/unbillableTaskCreationModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary createUnbillableCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="taskCreate" onclick="createUnbillableTask()"
						class="btn btn-primary taskActCreateBtn" style="display: none">
						<span class="nonBillableTaskCreateText" >Create</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- unbilled task create modal end -->  



<a data-toggle="modal" data-target="#taskCompletionModal"
	class="hide taskCompletionLink" data-backdrop="static">complete</a>
<a data-toggle="modal" data-target="#taskStartModal"
	class="hide taskStartLink" data-backdrop="static">start</a>
<a data-toggle="modal" data-target="#taskDetailsModal"
	class="hide taskDetailsLink" data-backdrop="static">taskdetails</a>
<!-- END MODALS -->
<%@include file="includes/footer.jsp"%>
<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type='text/javascript'
	src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/unbilledtask.js?version=2.10&v=${fsVersionNumber}"></script>
<script>

<c:if test="${sameDayOnlyTaskEnabled}">	
	var currentTaskDate = new Date();
	currentTaskDate.setHours(00);
	currentTaskDate.setMinutes(00);
	var minDate = new Date(currentTaskDate);
	currentTaskDate.setHours(23);
	currentTaskDate.setMinutes(59);
	var maxDate = new Date(currentTaskDate);
</c:if>
<c:if test="${!sameDayOnlyTaskEnabled}">
	var currentTaskDate = new Date();
	currentTaskDate.setMonth(currentTaskDate.getMonth()-3);
	var minDate = new Date(currentTaskDate);
	currentTaskDate.setMonth(currentTaskDate.getMonth()+6)
	var maxDate = new Date(currentTaskDate);
</c:if>

 <c:if test="${futureHourCaptureDisabled}">
 	var futureHourCaptureDisabled = true;
	var taskTimeMaxDate = new Date();
	taskTimeMaxDate.setHours(23);
	taskTimeMaxDate.setMinutes(59);
	maxDate = new Date(taskTimeMaxDate);
 </c:if>
 <c:if test="${!futureHourCaptureDisabled}">
 var futureHourCaptureDisabled = false;
 </c:if>

$(".taskOwnersList").on("click", function(){
	var hasClass = $("#" + this.id).hasClass("active");
	$(".taskOwnersList").removeClass("active");
	if (!hasClass) {
		$("#" + this.id).addClass("active");
		$(".task-item").hide();
		$(".task-item." + this.id).show();
	} else {
		$(".task-item").show();
	}
});

var activeUserId = "";

$("#taskAssigneeId").on("change", function(){
	var assigneeSelectionId = $(this).val();
	if (assigneeSelectionId != "") {
		$(".task-item").hide();
		$(".task-item." + assigneeSelectionId).show();
		activeUserId=assigneeSelectionId;
	} else {
		$(".task-item").show();
		activeUserId="";
	}
	performSearch();
});

function performSearch(){
	var serarchText = $('#taskActivitySearchId').val();
	famstacklog(serarchText);
	var searchId = ".task-item";

	if (activeUserId != "") {
		searchId+="."+activeUserId;
	}
	
	if (serarchText != "") {
		$('.task-item').hide();
	    $(searchId).each(function(){
	       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
	           $(this).show();
	       }
	    });
	} else {
		$(searchId).show();
	}
}

$('#taskActivitySearchId').keydown(function(){
	performSearch();
});

$('#taskActivitySearchId').keyup(function(){
	performSearch();
});


function reloadTime() {
	 var completedTime = getTodayDateTime(new Date());
	 validateTaskCompletionTime();
  	 $(".recordedStartTime").html(completedTime);
  	$(".completedTime").html(completedTime);
}
window.setInterval(reloadTime, 1000);

var taskStart = function(){
	var comments =$("#taskStartComments").val();
	var adjustStartTime =$("#adjustStartTime").val();
	$(lastMovedItem.item).find("input.startTime").val(adjustStartTime);
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var dataString = {"taskId":taskId,"taskStatus": "INPROGRESS","comments":comments, "adjustStartTime":adjustStartTime, "adjustCompletionTime":""}
	updateTaskStatus(dataString, false);
	taskTimerMap[taskId] = window.setInterval(function(taskId){
		getCompletedTaskTime(taskId);
	}, 1000, taskId);
	$(".task-item" + taskId).find("input.taskStatus").val("INPROGRESS");
	$(".taskPlayPause"+taskId).removeClass("fa-clock-o");
	$(".taskPlayPause"+taskId).addClass("fa-pause");
	$(".taskPlayPause"+taskId).attr("data-task-state", "running");
}

var validateTaskCompletionTime = function(){
	var adjustCompletionTime =$("#adjustCompletionTime").val();
	var adjustStartTime =$("#adjustStartTime1").val();
	$("#adjustCompletionTime").removeClass("error");
	$("#adjustStartTime1").removeClass("error");
	if (adjustStartTime != "" && adjustCompletionTime != "") {
		if (new Date(adjustCompletionTime) > new Date(adjustStartTime)) {
			var diffTime = (new Date(adjustCompletionTime) - new Date(adjustStartTime)) /(60*1000);
			fillTaskCompletionTime(diffTime);
			return true;
		}
	}
	$("#adjustCompletionTime").addClass("error");
	$("#adjustStartTime1").addClass("error");
	$("#completedTimeHrs").html("Invalid");
	$("#taskComplete").html("Complete");
	return false;
}

var fillTaskCompletionTime =function(diffTime){
	var timeMsg = "";
	if (diffTime > 59) {
		var diffTimeHrs = diffTime/60;
		var diffTimeMins = diffTime%60;
		timeMsg = parseInt(diffTimeHrs) + " Hrs " + parseInt(diffTimeMins) + " Mins";
		$("#completedTimeHrs").html(timeMsg);
	} else {
		$("#completedTimeHrs").html(Math.round(diffTime) + " Mins");
	}
	$("#taskComplete").parent().removeClass("colorRed");
	if (diffTime > 480){
		$("#taskComplete").parent().addClass("colorRed");
		$("#taskComplete").html("Confirm & Complete " + timeMsg + " Task");
	} else {
		$("#taskComplete").html("Complete");
	}
}

var taskComplete = function(){
	var comments =$("#taskCompletionComments").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	
	if (!validateTaskCompletionTime()) {
		return;
	}
	var adjustCompletionTime =$("#adjustCompletionTime").val();
	var adjustStartTime =$("#adjustStartTime1").val();

	var dataString = {"taskId":taskId,"taskStatus": "COMPLETED","comments":comments, "adjustStartTime":adjustStartTime, "adjustCompletionTime":adjustCompletionTime}
	updateTaskStatus(dataString, true);
	window.clearInterval(taskTimerMap[taskId]);
}

var reAssignTask = function(){
	var dataString = {taskId:$("#taskIdHidden").val(),newUserId:$("#taskAssignee").val(),taskActivityId:$("#taskActivityIdHidden").val(),taskStatus:$("#taskStatusHidden").val()};
	doAjaxRequest("POST", "${applicationHome}/reAssignTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
        	 window.location.reload(true);
        } else {
        	return false;
        }
       
    }, function(e) {
    });
}

var updateTaskStatus = function(dataString, isComplete){
	doAjaxRequest("POST", "${applicationHome}/updateTaskStatus", dataString, function(data, taskId) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
        	var labelDate = getTodayDateTime(new Date());
        	var label ="Started";
        	if (!isComplete) {
        		$(".inprogressDropDown").hide();
        		if ($("#adjustStartTime").val() != "") {
        			labelDate = $("#adjustStartTime").val();
        		}
        	} else {
        		label ="Completed"
        		lastMovedItem.item.find(".task-footer").find(".pull-right").remove();
        		$(".inprogressDropDown").show();
        		if ($("#adjustCompletionTime").val() != "") {
        			labelDate = $("#adjustCompletionTime").val();
        		}
        	}
        	lastMovedItem.item.find(".task-footer").find(".actualTaskStartTime").html(labelDate);
        	lastMovedItem.item.find("a.taskCompleted").remove();
    		lastMovedItem.item.find(".task-footer").find(".startDateTimeDiv").html(label + " at : " + labelDate);
    		lastMovedItem.item.find(".task-footer").find("input.startTime").val(labelDate);
        	return true;
        } else {
        	$(lastMovedItem.sender).sortable('cancel');
        	return false;
        }
       
    }, function(e) {
    	$(lastMovedItem.sender).sortable('cancel');
    });
	
}

$(document).ready(function () {
	$("#adjustCompletionTime").on("change", function(){
		validateTaskCompletionTime();
	});
	
	$("#adjustStartTime1").on("change", function(){
		validateTaskCompletionTime();
	});
});

var lastMovedItem ;
$(function(){
    var tasks = function(){
        $("#tasks,#tasks_progreess,#tasks_completed").sortable({
            items: "> .task-primary",
            connectWith: "#tasks_progreess,#tasks_completed",
            handle: ".task-text",            
            receive: function(event, ui) {
            	lastMovedItem=ui;
            	var taskId = $(lastMovedItem.item).find("input.taskId").val();
            	var projectId = $(lastMovedItem.item).find("input.projectId").val();
            	var taskState = $(".taskPlayPause"+taskId).attr("data-task-state");
            	if (taskState == 'paused') {
            		//$(lastMovedItem.sender).sortable('cancel');
            		//return;
            	}
            	resetFileUploadUrl(projectId+"-completed");
            	$(".unassignedDuration").html($(lastMovedItem.item).find("input.duration").val());
            	
            	if(this.id == "tasks_completed"){
            		var adjustStartTime = $(lastMovedItem.item).find("input.startTime").val();
            		//var estStartTime = $(lastMovedItem.item).find("input.estStartTime").val();
            		if (adjustStartTime == "") {
            			adjustStartTime = getTodayDateTime(new Date());//estStartTime
            		}
                	$("#adjustStartTime1").val(adjustStartTime);  
                	
                	var pausedTime = $(lastMovedItem.item).find("input.pausedTime").val();
                	if (pausedTime != "") {
                		$('#adjustCompletionTime').val(pausedTime);
                	} else {
                		$('#adjustCompletionTime').val(getTodayDateTime(new Date())); 
                	}
					adjustStartTimeChanged();                	
                	var adjustStartTimeDate = new Date(adjustStartTime);
                	var completionDate = new Date($("#adjustCompletionTime").val());
                	var diffTime = (completionDate - adjustStartTimeDate) /(60*1000);
                	famstacklog(diffTime);
                	fillTaskCompletionTime(diffTime);
                	$("#adjustCompletionTime").removeClass("error");
                	$("#adjustStartTime1").removeClass("error");
                	$(".taskCompletionLink").click();
                }
                if(this.id == "tasks_progreess"){
                	//var estStartTime = $(lastMovedItem.item).find("input.estStartTime").val();
                	$("#adjustStartTime").val(getTodayDateTime(new Date()));
                	$(".taskStartLink").click();
                }                
                page_content_onresize();
            },
            start:function(){
            	if(this.id == "tasks"){
            		$("#tasks_progreess").addClass("changeBackground");
            		$(".inprogressDropDown").show();
            	}
            	$(".completeDropDown").show();
            	$("#tasks_completed").addClass("changeBackground");
            },
            stop:function(){
            	$("#tasks_completed").removeClass("changeBackground");
            	$("#tasks_progreess").removeClass("changeBackground");
            	$(".inprogressDropDown").hide();
            	$(".completeDropDown").hide();
            }
        }).disableSelection();
        
    }();
    
    $(".taskWindowCancel").click(function(){
    	$(lastMovedItem.sender).sortable('cancel');
    });
});


function moveToCompleteTask(taskId){
	var taskState = $(".taskPlayPause"+taskId).attr("data-task-state");
	if (taskState == 'paused') {
		//return;
	}
	lastMovedItem = new Object();
	lastMovedItem.item = $(".task-item"+ taskId);
	var adjustStartTime = $(lastMovedItem.item).find("input.startTime").val();
	var pausedTime = $(lastMovedItem.item).find("input.pausedTime").val();
	//var estStartTime = $(lastMovedItem.item).find("input.estStartTime").val();
	if (adjustStartTime == "") {
		adjustStartTime = getTodayDateTime(new Date());
	}
	$("#adjustStartTime1").val(adjustStartTime);   
	if (pausedTime != "") {
		$('#adjustCompletionTime').val(pausedTime);
	} else {
		$('#adjustCompletionTime').val(getTodayDateTime(new Date())); 
	}
	adjustStartTimeChanged();                	
	var adjustStartTimeDate = new Date(adjustStartTime);
	var completionDate = new Date($("#adjustCompletionTime").val());
	var diffTime = (completionDate - adjustStartTimeDate) /(60*1000);
	famstacklog(diffTime);
	fillTaskCompletionTime(diffTime);
	$("#adjustCompletionTime").removeClass("error");
	$("#adjustStartTime1").removeClass("error");
	$(".taskCompletionLink").click();
}

$("#taskAssignee").on("change", function(){
		if ($("#taskAssignee").val() != $("#taskAssigneeHidden").val()) {
			$("#taskReassign").removeClass("hide");
		} else {
			$("#taskReassign").addClass("hide");
		}
});

var openTaskDetails = function (taskId){
	
	$(".taskDetailsTaskName").html($(".task-item" + taskId).find(".taskName").html());
	$(".taskDetailsDuration").html($(".task-item" + taskId).find("input.duration").val());
	$(".taskDetailsStartedAt").html($(".task-item" + taskId).find("input.startedAt").val());
	$(".taskDetailsEstStartTimet").html($(".task-item" + taskId).find("input.estStartTime").val());
	/*$(".taskPriority").html($(".task-item" + taskId).find("input.taskPriority").val());*/
	
	$(".taskDetailsTaskDescription").html($(".task-item" + taskId).find(".taskDescription").html());
	$("#taskAssigneeHidden").val($(".task-item" + taskId).find("input.assignee").val());
	
	$("#taskIdHidden").val($(".task-item" + taskId).find("input.taskId").val());
	$("#taskActivityIdHidden").val($(".task-item" + taskId).find("input.taskActivityId").val());
	$("#taskStatusHidden").val($(".task-item" + taskId).find("input.taskStatus").val());
	$("#taskAssignee").val($(".task-item" + taskId).find("input.assignee").val()).change();

	$(".taskDetailsLink").click();
}

var fileLocation = "";
Dropzone.autoDiscover = false;
var myDropZone = $("#my-dropzone").dropzone({
	url : "${applicationHome}/uploadfile/completed",
	addRemoveLinks : false,
	acceptedFiles: ".jpeg,.jpg,.png,.gif,.doc,.docx,.xls,.xlsx,.pdf",
	maxFilesize: 10, 
	success : function(file, response) {
	if (response.status){
		file.previewElement.classList.add("dz-success");
		famstacklog(file.name);
	 } else {
	    	 file.previewElement.classList.add("dz-error");
	       }
	},
	error : function(file, response) {
		file.previewElement.classList.add("dz-error");
	},init: function() {
	    this.on("processing", function(file) {
		      this.options.url = "${applicationHome}/uploadfile/"+fileLocation;
		      famstacklog("new file upload location");
		    });
		  }
});

function resetFileUploadUrl(location){
	fileLocation = location;
}

function pauseTask(taskId){
	
	var taskActivityId = $(".task-item" + taskId).find("input.taskActivityId").val();
	var dataString = {"taskId": taskId, "taskActivityId":taskActivityId};
	doAjaxRequest("POST", "${applicationHome}/pauseTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        famstacklog(responseJson);
        if (responseJson.status){
        	 $(".taskPlayPause"+taskId).removeClass("fa-pause");
    		 $(".taskPlayPause"+taskId).addClass("fa-play");
    		 window.clearInterval(taskTimerMap[taskId]);
    		 $(".taskPlayPause"+taskId).attr("data-task-state", "paused");
    		 $(".blink"+taskId).html("PAUSED");
        } else {
        	return false;
        }
       
    }, function(e) {
    });
}

function playTask(taskId){
	var taskActivityId = $(".task-item" + taskId).find("input.taskActivityId").val();
	var dataString = {"taskId": taskId, "taskActivityId":taskActivityId};
	doAjaxRequest("POST", "${applicationHome}/playTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        famstacklog(responseJson);
        if(!responseJson.hasOwnProperty('ERROR')){
        	$(".taskPlayPause"+taskId).removeClass("fa-play");
     		$(".taskPlayPause"+taskId).addClass("fa-pause");
     		$(".taskPlayPause"+taskId).attr("data-task-state", "running");
     		$(".blink"+taskId).html("");
     		$("."+taskId+".taskRemainingDiv .actualTaskStartTime").html(getTodayDateTime(new Date(responseJson.actualStartTime)));
     		$(".task-item" + taskId).find(".startDateTimeDiv").html("Started" + " at : " + getTodayDateTime(new Date(responseJson.actualStartTime)));
     		/* $("."+taskId+".taskRemainingDiv .taskHour").html(responseJson.startHour);
     		$("."+taskId+".taskRemainingDiv .taskMinutes").html(responseJson.startMins);
     		$("."+taskId+".taskRemainingDiv .taskSeconds").html(responseJson.startSecs); */
     		
     		
     		$(".task-item" + taskId).find("input.taskActivityId").val(responseJson.taskActivityId);
     		$(".task-item" + taskId).find("input.startTime").val(getTodayDateTime(new Date()));
     		window.setInterval(function(taskId){
     			getCompletedTaskTime(taskId);
     		}, 1000, taskId);
        } else {
        	 $(".blink"+taskId).html("ERROR");
        }
    }, function(e) {
    });
}

function taskPlayOrPause(taskId){
	var taskState = $(".taskPlayPause"+taskId).attr("data-task-state");
	if (taskState == 'running') {
		 pauseTask(taskId);
	} else if(taskState == 'paused') {
		playTask(taskId);
	}
}

$('#adjustCompletionTime').datetimepicker({ sideBySide: true,minDate:minDate,maxDate:maxDate, format: 'YYYY/MM/DD HH:mm'});
$('#adjustStartTime').datetimepicker({ sideBySide: true,minDate:minDate,maxDate:new Date(), format: 'YYYY/MM/DD HH:mm'});
$('#adjustStartTime1').datetimepicker({ sideBySide: true,minDate:minDate,maxDate:maxDate, format: 'YYYY/MM/DD HH:mm'}).on('dp.change', function(e) {
   adjustStartTimeChanged();
});;


var adjustStartTimeChanged=function(){
	var adjustTime = $('#adjustStartTime1').val();
    var taskDuration = parseInt($(lastMovedItem.item).find("input.duration").val());
    var newCompletionDate = getTodayDateTime(new Date(adjustTime).addHours(taskDuration));
   // $('#adjustCompletionTime').val(newCompletionDate);
}

$(document).ready(function () {
	try{
	   // sortSelect('#taskAssigneeId', 'text', 'asc');
	   //$("#taskAssigneeId").val("userId${currentUser.id}");
	   $("#taskAssigneeId").selectpicker('refresh');
	   $('#taskAssigneeId').trigger('change');
	} catch(err){
		
	}
	
	setInterval(function() {
       window.location.reload();
    }, 300000); 
	
}); 


 $("#taskFilterDayId").on("change", function(){
	 window.location = "/bops/dashboard/tasks?dayfilter="+$("#taskFilterDayId").val();
 });
function refreshCalendar(){}
</script>
