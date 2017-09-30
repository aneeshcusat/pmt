<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Tasks</li>
 </ul>
 <style>
 	.changeBackground{
 		border: 1px dotted #FF0000;
 	}
 	#taskCompletionModal .modal-dialog {
		width: 75%;
	}
	
	#taskStartModal  .modal-dialog {
		width: 75%;
	}
	
	#taskDetailsModal  .modal-dialog {
		width: 30%;
	}
	.taskName{
		color: #2BD5D0;
	}
	.taskLabel {
		float: right; 
		border: 1px solid blue; 
		width: 30px; 
		height: 28px; 
		text-align: center; 
		font-weight: bold; 
		font-size: 20px;color: wheat; 
	}
	
	.list-group-horizontal .list-group-item {
    display: inline-block;
}
.list-group-item {
    position: relative;
    padding: 5px 12px;
    margin-bottom: -1px;
    background-color: #f5f5f5;
    border: 0px solid #ddd;
    font-size: 13px;
    font-weight: bold;
}
.list-group-item.active, .list-group-item.active:hover, .list-group-item.active:focus {
    background: lightblue;
    border-color: #1b1e24;
}
.list-group-horizontal .list-group-item {
	margin-bottom: 0;
	margin-left:-4px;
	margin-right: 0;
}
.list-group-horizontal .list-group-item:first-child {
	border-top-right-radius:0;
}
.list-group-horizontal .list-group-item:last-child {
	border-bottom-left-radius:0;
}
 </style>
 <script>
 var taskTimerMap = {};

 var getRemaining = function(taskId) {
	 $("."+taskId+".durationDiv").remove();
	 $("."+taskId+".taskRemainingDiv").show();
	 
	 var hour = parseInt($("."+taskId+".taskRemainingDiv .taskHour").html());
	 var minutes =  parseInt($("."+taskId+".taskRemainingDiv .taskMinutes").html());
	 var second =  parseInt($("."+taskId+".taskRemainingDiv .taskSeconds").html());
	 
	 if (second <= 0){
		second =59;
		minutes-=1;
	}
	second = second-1;
	if (minutes <=0){
		minutes =59;
		hour-=1;
	}
	
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
            <h2><span class="fa fa-tasks"></span> Tasks</h2>
        </div>                                                
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                
    </div>                    
    <!-- END CONTENT FRAME TOP -->
    <c:if test="${not empty modelViewMap.taskOwners}">
        <div class="row ">
            <div class="col-md-12" style="background-color: #f5f5f5">
            <div class="col-md-1" style="background-color: #f5f5f5">
            	<span style="vertical-align: middle;text-align: left;float:left; margin-top: 5px;font-weight: bold; font-size: 15px;line-height: 20px;">Task Filters :</span>
            </div>
            <div class="col-md-11" style="background-color: #f5f5f5">
			<div class="list-group list-group-horizontal">
			  <c:forEach var="taskOwner" items="${modelViewMap.taskOwners}" varStatus="taskOwnerIndex">
			  		<a href="#" id="userId${taskOwner}" class="taskOwnersList list-group-item">${userDetailsMap[taskOwner].firstName}</a>
			  </c:forEach>
            </div>
            </div>
   		</div>
    </div>
    </c:if>
    <!-- START CONTENT FRAME BODY -->
        <div class="row ">
            <div class="col-md-3">
			    <h5>Backlog</h5>
                <div class="tasks" id="tasks">
                	<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['BACKLOG']}" varStatus="taskIndex">
			        <div class='task-item task-danger task-primary userId${tasks.taskActivityDetails[0].userId} task-item${tasks.taskId}'>                                    
                        <div class="task-text col-md-12">
                        <div class="col-md-10">
                        <span class="taskName clearfix"><b>${tasks.name}</b></span>
                        </div>
                        <div class="col-md-2">
                        <span style="background-color: blue " class="taskLabel">
                        <c:if test="${tasks.reviewTask}">
                        R
                        </c:if>
                        <c:if test="${!tasks.reviewTask}">
                        P
                        </c:if>
                        </span>
                        </div>
                        <div class="col-md-12">
                        <span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
                        </div>
                        </div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}" style="float: left">Show project</a>
								<a href="#" style="float: right"onclick="openTaskDetails('${tasks.taskId}');">View task</a>
						</div>
						<div class="task-footer">
                            <div class="pull-right"><span class="fa fa-clock-o"></span><span class="durationDiv ${tasks.taskId}">${tasks.duration} Hours</span>
                            	<span class="${tasks.taskId} taskRemainingDiv" style="display: none"><span class="taskHour">${tasks.taskActivityDetails[0].timeTakenToCompleteHour}</span>:<span class="taskMinutes">${tasks.taskActivityDetails[0].timeTakenToCompleteMinute}</span>:<span class="taskSeconds">${tasks.taskActivityDetails[0].timeTakenToCompleteSecond}</span></span>
                            </div>     
                            <div class="pull-left"><span class="startDateTimeDiv">Est Start Time : ${tasks.startTime}</span></div> 
                        </div>    
                         <input type="hidden" class="estStartTime" value="${tasks.startTime}"/>                                
						<input type="hidden" class="startTime" value='<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${tasks.taskActivityDetails[0].actualStartTime}"/>'/>
                        <input type="hidden" class="duration" value="${tasks.duration}"/>
                        <input type="hidden" class="assignee" value="${tasks.taskActivityDetails[0].userId}"/> 
                         <input type="hidden" class="taskStatus" value="${tasks.status}"/>         
                        <input type="hidden" class="taskId" value="${tasks.taskId}"/>  
                        <input type="hidden" class="projectId" value="${tasks.projectId}"/>       
                        <input type="hidden" class="taskActivityId" value="${tasks.taskActivityDetails[0].taskActivityId}"/>  
                    </div>
                    </c:forEach>
                    </c:if>
                </div>                            
            </div>
             <div class="col-md-3">
			    <h5>Today's To Do</h5>
                <div class="tasks" id="tasks">
			       <c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['ASSIGNED']}" varStatus="taskIndex">
			        <div class='task-item task-info task-primary userId${tasks.taskActivityDetails[0].userId} task-item${tasks.taskId}'>                                    
                        <div class="task-text col-md-12">
                        <div class="col-md-10">
                        <span class="taskName clearfix"><b>${tasks.name}</b></span>
                        </div>
                        <div class="col-md-2">
                        <span style="background-color: blue " class="taskLabel">
                        <c:if test="${tasks.reviewTask}">
                        R
                        </c:if>
                        <c:if test="${!tasks.reviewTask}">
                        P
                        </c:if>
                        </span>
                        </div>
                        <div class="col-md-12">
                        <span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
                        </div>
                        </div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}" style="float: left">Show project</a>
								<a href="#" style="float: right"onclick="openTaskDetails('${tasks.taskId}');">View task</a>
						</div>
                        <div class="task-footer">
                            <div class="pull-right"><span class="fa fa-clock-o"></span><span class="durationDiv ${tasks.taskId}"> ${tasks.duration} Hours</span>
                              <span class="${tasks.taskId} taskRemainingDiv" style="display: none"><span class="taskHour">${tasks.taskActivityDetails[0].timeTakenToCompleteHour}</span>:<span class="taskMinutes">${tasks.taskActivityDetails[0].timeTakenToCompleteMinute}</span>:<span class="taskSeconds">${tasks.taskActivityDetails[0].timeTakenToCompleteSecond}</span></span>
                            </div>     
                            <div class="pull-left"><span class="startDateTimeDiv">Est Start Time : ${tasks.startTime}</span></div> 
                         <input type="hidden" class="estStartTime" value="${tasks.startTime}"/>
                        <input type="hidden" class="startTime" value=""/>
                        <input type="hidden" class="duration" value="${tasks.duration}"/>     
                        <input type="hidden" class="assignee" value="${tasks.taskActivityDetails[0].userId}"/>       
						<input type="hidden" class="projectId" value="${tasks.projectId}"/>    
						 <input type="hidden" class="taskStatus" value="${tasks.status}"/>        
                        <input type="hidden" class="taskId" value="${tasks.taskId}"/>      
                        <input type="hidden" class="taskActivityId" value="${tasks.taskActivityDetails[0].taskActivityId}"/>  
                    </div>
                    </div>
                    </c:forEach>
                    </c:if>
                </div>          
            </div>
            <div class="col-md-3">
                <h5>In Progress</h5>
                <div class="tasks" id="tasks_progreess">
			         <c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['INPROGRESS']}" varStatus="taskIndex">
			        <div class='task-item task-warning task-primary userId${tasks.taskActivityDetails[0].userId} task-item${tasks.taskId}'>                                    
                        <div class="task-text col-md-12">
                        <div class="col-md-10">
                        <span class="taskName clearfix"><b>${tasks.name}</b></span>
                        </div>
                        <div class="col-md-2">
                        <span style="background-color: blue " class="taskLabel">
                        <c:if test="${tasks.reviewTask}">
                        R
                        </c:if>
                        <c:if test="${!tasks.reviewTask}">
                        P
                        </c:if>
                        </span>
                        </div>
                        <div class="col-md-12">
                        <span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
                        </div>
                        </div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}" style="float: left">Show project</a>
								<a href="#" style="float: right"onclick="openTaskDetails('${tasks.taskId}');">View task</a>
						</div>
						<div class="task-footer">
                            <div class="pull-right"><span class="fa fa-pause"></span><span class="durationDiv ${tasks.taskId}"></span>
                            <span class="${tasks.taskId} taskRemainingDiv" style="display: none"><span class="taskHour">${tasks.taskActivityDetails[0].timeTakenToCompleteHour}</span>:<span class="taskMinutes">${tasks.taskActivityDetails[0].timeTakenToCompleteMinute}</span>:<span class="taskSeconds">${tasks.taskActivityDetails[0].timeTakenToCompleteSecond}</span></span>
                            </div>     
                            <div class="pull-left"><span class="startDateTimeDiv">Started at : <fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${tasks.taskActivityDetails[0].actualStartTime}" /></span></div> 
                        </div>   
                         <input type="hidden" class="estStartTime" value="${tasks.startTime}"/>
                        <input type="hidden" class="startTime" value="<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${tasks.taskActivityDetails[0].actualStartTime}" />"/>
                        <input type="hidden" class="duration" value="${tasks.duration}"/> 
                        <input type="hidden" class="assignee" value="${tasks.taskActivityDetails[0].userId}"/>        
                        <input type="hidden" class="taskId" value="${tasks.taskId}"/>
                         <input type="hidden" class="taskStatus" value="${tasks.status}"/>     
                        <input type="hidden" class="projectId" value="${tasks.projectId}"/>       
                        <input type="hidden" class="taskActivityId" value="${tasks.taskActivityDetails[0].taskActivityId}"/> 
                        <script>
                        taskTimerMap["${tasks.taskId}"] = window.setInterval(function(){
                			getRemaining("${tasks.taskId}");
                		}, 1000);
                        </script>                                      
                    </div>
                    </c:forEach>
                    </c:if>
                    <div class="task-drop push-down-10 inprogressDropDown" style="display: none;">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to start tracking time
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <h5>Completed</h5>
                <div class="tasks" id="tasks_completed">
                <div class="task-drop completeDropDown"  style="display: none;">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to finish it
                 </div>  
			       <c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['COMPLETED']}" varStatus="taskIndex">
			        <div class='task-item task-complete task-success userId${tasks.taskActivityDetails[0].userId}'>                                    
                        <div class="task-text col-md-12">
                        <div class="col-md-10">
                        <span class="taskName clearfix"><b>${tasks.name}</b></span>
                        </div>
                        <div class="col-md-2">
                        <span style="background-color: blue " class="taskLabel">
                        <c:if test="${tasks.reviewTask}">
                        R
                        </c:if>
                        <c:if test="${!tasks.reviewTask}">
                        P
                        </c:if>
                        </span>
                        </div>
                        <div class="col-md-12">
                        <span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
                        </div>
                        </div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}" style="float: left">Show project</a>
						</div>
						<div class="task-footer">
                           <div class="pull-left"><span class="fa fa-clock-o"></span>${tasks.taskActivityDetails[0].timeTakenToComplete}</div> 
                        </div>                                    
                    </div>
                    </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>                        
    <!-- END CONTENT FRAME BODY -->
</div>
<!-- END CONTENT FRAME -->

<!-- task completion modal start -->
<div class="modal fade" id="taskCompletionModal" tabindex="-1"
	role="dialog" aria-labelledby="taskCompletionModal" aria-hidden="true">
	<form:form id="taskCompletionModal" action="taskCompletion" method="POST"
		role="form" class="form-horizontal">
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
					<button type="button" onclick=""
						class="btn btn-primary">
						<span id="taskComplete" onclick="taskComplete()">Complete</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- project create modal end -->  

<!-- task completion modal start -->
<div class="modal fade" id="taskStartModal" tabindex="-1"
	role="dialog" aria-labelledby="taskStartModal" aria-hidden="true">
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
					<button type="button" onclick=""
						class="btn btn-primary">
						<span id="taskStart" onclick="taskStart()">Start</span>
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
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button id="taskReassign" type="button"
						class="btn btn-primary hide">
						<span  onclick="reAssignTask()">Re Assign Task</span>
					</button>
				</div>
			</div>
		</div>
</div>

<!-- project create modal end -->  
<a data-toggle="modal" data-target="#taskCompletionModal" class="hide taskCompletionLink" data-backdrop="static">complete</a>
<a data-toggle="modal" data-target="#taskStartModal" class="hide taskStartLink" data-backdrop="static">start</a>
<a data-toggle="modal" data-target="#taskDetailsModal" class="hide taskDetailsLink" data-backdrop="static">taskdetails</a>
<!-- END MODALS -->
<%@include file="includes/footer.jsp" %>  
<script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js"></script>
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
<script>

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
function reloadTime() {
	 var completedTime = getTodayDateTime(new Date());
	 $(".completedTime").html(completedTime);
  	 $(".recordedStartTime").html(completedTime);
}
window.setInterval(reloadTime, 1000);

var taskStart = function(){
	var comments =$("#taskStartComments").val();
	var adjustStartTime =$("#adjustStartTime").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var dataString = {"taskId":taskId,"taskStatus": "INPROGRESS","comments":comments, "adjustStartTime":adjustStartTime, "adjustCompletionTime":""}
	updateTaskStatus(dataString, false);
	taskTimerMap[taskId] = window.setInterval(function(taskId){
		getRemaining(taskId);
	}, 1000, taskId);
}

var taskComplete = function(){
	var comments =$("#taskCompletionComments").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
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

$("#taskComplete").click(function(){
	$(".modal").hide();
});


$("#adjustCompletionTime").on("change", function(){
	var adjustStartTime = $(lastMovedItem.item).find("input.startTime").val();
	var adjustStartTimeDate = new Date(adjustStartTime);
	var diffTime = (new Date() - adjustStartTimeDate) /(60*1000);
	$(".completedTimeHrs").val(diffTime);
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
            	
            	resetFileUploadUrl(projectId+"-completed");
            	$(".unassignedDuration").html($(lastMovedItem.item).find("input.duration").val());
            	
            	if(this.id == "tasks_completed"){
            		var adjustStartTime = $(lastMovedItem.item).find("input.startTime").val();
                	$("#adjustStartTime1").val(adjustStartTime);
                	var adjustStartTimeDate = new Date(adjustStartTime);
                	var diffTime = (new Date() - adjustStartTimeDate) /(60*1000);
                	console.log(diffTime);
                	$("#completedTimeHrs").html(Math.round(diffTime));
                	$(".taskCompletionLink").click();
                }
                if(this.id == "tasks_progreess"){
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

$("#taskAssignee").on("change", function(){
		if ($("#taskAssignee").val() != $("#taskAssigneeHidden").val()) {
			$("#taskReassign").removeClass("hide");
		} else {
			$("#taskReassign").addClass("hide");
		}
});

var openTaskDetails = function (taskId){
	;
	$(".taskDetailsTaskName").html($(".task-item" + taskId).find(".taskName").html());
	$(".taskDetailsDuration").html($(".task-item" + taskId).find("input.duration").val());
	$(".taskDetailsStartedAt").html($(".task-item" + taskId).find("input.startedAt").val());
	$(".taskDetailsEstStartTimet").html($(".task-item" + taskId).find("input.estStartTime").val());
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
	success : function(file, response) {
		file.previewElement.classList.add("dz-success");
		console.log(file.name);
	},
	error : function(file, response) {
		file.previewElement.classList.add("dz-error");
	},init: function() {
	    this.on("processing", function(file) {
		      this.options.url = "${applicationHome}/uploadfile/"+fileLocation;
		      console.log("new file upload location");
		    });
		  }
});

function resetFileUploadUrl(location){
	fileLocation = location;
}

$('#adjustCompletionTime').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm'});
$('#adjustStartTime').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm'});
</script>
