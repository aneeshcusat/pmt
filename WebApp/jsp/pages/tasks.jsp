<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Tasks</li>
 </ul>
 <style>
 	#taskCompletionModal .modal-dialog {
		width: 75%;
	}
	
	#taskStartModal  .modal-dialog {
		width: 75%;
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
 </style>
 <script>
 var hour = 0;
 var minutes = 0;
 var second =0;
 </script>
<!-- START CONTENT FRAME -->
<div class="content-frame">     
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
    
    <!-- START CONTENT FRAME BODY -->
        <div class="row ">
            <div class="col-md-4">
                
                
	<a data-toggle="modal" data-target="#taskCompletionModal" class="hide taskCompletionLink" data-backdrop="static">complete</a>
<a data-toggle="modal" data-target="#taskStartModal" class="hide taskStartLink" data-backdrop="static">start</a>
                <h3>To-do List</h3>
                
                <div class="tasks" id="tasks">

					<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['ASSIGNED']}" varStatus="taskIndex">
			        <div class='task-item task-danger 
			          <c:if test="${taskIndex.index == 0 && empty modelViewMap.projectTaskDetailsData['INPROGRESS'] && tasks.assignee == currentUser.id}">
			        	task-primary
			         </c:if>
			        '>                                    
                        <div class="task-text">${tasks.name}
                        
                        <span style="background-color: blue " class="taskLabel">
                        <c:if test="${tasks.reviewTask}">
                        R
                        </c:if>
                        <c:if test="${!tasks.reviewTask}">
                        P
                        </c:if>
                        </span>
                        
                        </div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                            <div class="pull-right"><span class="fa fa-clock-o"></span> ${tasks.duration} Hours</div>     
                            <div class="pull-left">Est Start Time : ${tasks.startTime}</div> 
                            <input type="hidden" class="duration" value="${tasks.duration}"/>        
                            <input type="hidden" class="taskId" value="${tasks.taskId}"/>      
                            <input type="hidden" class="taskActivityId" value="${tasks.taskActivityDetails.taskActivityId}"/>  
                        </div>                                    
                    </div>
			        </c:forEach>
			        </c:if>
                </div>                            

            </div>
            <div class="col-md-4">
                <h3>In Progress</h3>
                <div class="tasks" id="tasks_progreess">
					<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['INPROGRESS']}">
			        <c:if test='${tasks.status == "INPROGRESS"}'>
			        <div class="task-item task-primary">                                    
                        <div class="task-text">${tasks.name}
                        <span style="background-color: blue " class="taskLabel">
                        <c:if test="${tasks.reviewTask}">
                        R
                        </c:if>
                        <c:if test="${!tasks.reviewTask}">
                        P
                        </c:if>
                        </span>
                        
                        </div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                            <script>
                            	hour = ${tasks.taskActivityDetails.timeTakenToCompleteHour};
                            	minutes = ${tasks.taskActivityDetails.timeTakenToCompleteMinute};
                            	second = ${tasks.taskActivityDetails.timeTakenToCompleteSecond};
                            </script>
                            
                            <div class="pull-left">Started at : <fmt:formatDate pattern = "yyyy/MM/dd hh:mm" value = "${tasks.taskActivityDetails.actualStartTime}" /></div> 
                            <div class="pull-right"><span class="fa fa-clock-o"></span> Remaining :<span id="remainingTime"></span></div>
                            <input type="hidden" class="duration" value="${tasks.duration}"/>     
                            <input type="hidden" class="taskId" value="${tasks.taskId}"/>
                            <input type="hidden" class="projectId" value="${tasks.projectId}"/>       
                            <input type="hidden" class="taskActivityId" value="${tasks.taskActivityDetails.taskActivityId}"/>         
                        </div>                                    
                    </div>
			        </c:if>
			        </c:forEach>
			        </c:if>
			      
                    <div class="task-drop push-down-10 inprogressDropDown" style="display: <c:if test="${not empty modelViewMap.projectTaskDetailsData['INPROGRESS']}">
                    none;</c:if>
                    ">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to start tracking time
                    </div>
                    
                    
                </div>
            </div>
            <div class="col-md-4">
                <h3>Completed</h3>
                <div class="tasks" id="tasks_completed">
                <div class="task-drop">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to finish it
                 </div>  
                  <c:if test="${not empty modelViewMap.projectTaskDetailsData}">
			        <c:forEach var="tasks" items="${modelViewMap.projectTaskDetailsData['COMPLETED']}">
			        <c:if test='${tasks.status == "COMPLETED"}'>
			        <div class="task-item task-info task-complete">                                    
                        <div class="task-text">${tasks.name}</div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                             <div class="pull-left"><span class="fa fa-clock-o"></span>${tasks.taskActivityDetails.timeTakenToComplete}</div>                                         
                        </div>                                    
                    </div>
			        </c:if>
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
<!-- project create modal end -->  
     
<!-- END MODALS -->
<%@include file="includes/footer.jsp" %>  
<script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js"></script>
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
<script>
var inProgress = true;
function reloadTime() {
	if (!inProgress) {
		return;
	}
	if (second <= 0){
		second =59;
		minutes-=1;
	}
	second = second-1;
	
	if (minutes <=0){
		minutes =59;
		hour-=1;
	}
    $("#remainingTime").html(hour+":"+minutes+":"+second);
    
    var completedTime = getTodayDate(new Date()) + " " + new Date().getHours()+":"+new Date().getMinutes();
	$(".completedTime").html(completedTime);
	$(".recordedStartTime").html(completedTime);
	try{
	var duration = $(lastMovedItem.item).find("input.duration").val();
	}catch(err){}
	var durationInHrs = parseInt(duration);
	var completedTimeInHours = durationInHrs - hour - 1;
	var completedTimeInHoursMins = 60 - minutes;
	$(".completedTimeHrs").html(completedTimeInHours +" Hrs "+completedTimeInHoursMins + " Mins");
}
window.setInterval(reloadTime, 1000);

var taskStart = function(){
	var comments =$("#taskStartComments").val();
	var adjustStartTime =$("#adjustStartTime").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var duration = $(lastMovedItem.item).find("input.duration").val();
	hour = parseInt(duration);
	second =0;
	minutes =0;
	var dataString = {"taskId":taskId,"taskStatus": "INPROGRESS","comments":comments, "adjustTime":adjustStartTime}
	updateTaskStatus(dataString, false);
}


var taskComplete = function(){
	var comments =$("#taskCompletionComments").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var adjustCompletionTime =$("#adjustCompletionTime").val();
	var dataString = {"taskId":taskId,"taskStatus": "COMPLETED","comments":comments, "adjustTime":adjustCompletionTime}
	/* var completedTime = getTodayDate(new Date()) + " " + new Date().getHours()+":"+new Date().getMinutes();
	if (adjustCompletionTime != "") {
		completedTime = adjustCompletionTime;
	}
	lastMovedItem.item.find(".task-footer").append('<div class="pull-right">completed Time '+completedTime+'</div>'); */
	updateTaskStatus(dataString, true);
}


var updateTaskStatus = function(dataString, isComplete){
	doAjaxRequest("POST", "${applicationHome}/updateTaskStatus", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").hide();
        	if (!isComplete) {
        		lastMovedItem.item.find(".task-footer").find(".pull-right").remove();
        		lastMovedItem.item.find(".task-footer").append('<div class="pull-right"><span class="fa fa-pause"></span><span id="remainingTime">00:00</span></div>');
        		$(".inprogressDropDown").hide();
        	} else {
        		inProgress = false;
        		lastMovedItem.item.addClass("task-complete").find(".task-footer > .pull-right").remove();
        		$(".inprogressDropDown").show();
        	}
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
                	$(".taskCompletionLink").click();
                }
                if(this.id == "tasks_progreess"){
                	$(".taskStartLink").click();
                }                
                page_content_onresize();
            }
        }).disableSelection();
        
    }();
    
    $(".taskWindowCancel").click(function(){
    	$(lastMovedItem.sender).sortable('cancel');
    });
});
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
