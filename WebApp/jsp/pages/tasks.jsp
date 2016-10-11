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
			          <c:if test="${taskIndex.index == 0 && empty modelViewMap.projectTaskDetailsData['INPROGRESS']}">
			        	task-primary
			         </c:if>
			        '>                                    
                        <div class="task-text">${tasks.name}</div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                            <div class="pull-right"><span class="fa fa-clock-o"></span> ${tasks.duration} Hours</div>     
                            <div class="pull-left">Start Time : ${tasks.startTime}</div> 
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
                        <div class="task-text">${tasks.name}</div>
                        <div class="task-text"><a target="_new" href="${applicationHome}/project/${tasks.projectId}">Show project</a></div>
                        <div class="task-footer">
                            <script>
                            	hour = ${tasks.taskActivityDetails.timeTakenToCompleteHour};
                            	minutes = ${tasks.taskActivityDetails.timeTakenToCompleteMinute};
                            	second = ${tasks.taskActivityDetails.timeTakenToCompleteSecond};
                            </script>
                            <div class="pull-left">Start Time : ${tasks.taskActivityDetails.actualStartTime}</div> 
                            <div class="pull-right"><span class="fa fa-pause"></span> Time remaining : <span id="remainingTime">2h 55min</span></div>
                           
                            <input type="hidden" class="taskId" value="${tasks.taskId}"/>      
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
                        Drag your task here to start it tracking time
                    </div>
                    
                    
                </div>
            </div>
            <div class="col-md-4">
                <h3>Completed</h3>
                <div class="tasks" id="tasks_completed">
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
                    <div class="task-drop">
                        <span class="fa fa-cloud"></span>
                        Drag your task here to finish it
                    </div>  
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
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
<script>
var inProgress = true;
function reloadTime() {
	if (!inProgress) {
		return;
	}
	second-=1;
	if (second ==0){
		second =59;
		minutes-=1;
	}
	if (minutes ==0){
		minutes =59;
		hour-=1;
	}
    $("#remainingTime").html(hour+":"+minutes+":"+second);
}
window.setInterval(reloadTime, 1000);

var taskStart = function(){
	var comments =$("#taskStartComments").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var duration = $(lastMovedItem.item).find("input.duration").val();
	hour = parseInt(duration);
	second =0;
	mininute =0;
	var taskActivityId = $(lastMovedItem.item).find("input.taskActivityId").val();
	var dataString = {"taskActivityId":taskActivityId, "taskId":taskId,"taskStatus": "INPROGRESS","comments":comments}
	updateTaskStatus(dataString, false);
}


var taskComplete = function(){
	var comments =$("#taskCompletionComments").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var taskActivityId = $(lastMovedItem.item).find("input.taskActivityId").val();
	var dataString = {"taskActivityId":taskActivityId, "taskId":taskId,"taskStatus": "COMPLETED","comments":comments}
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
        		var completedTime = getTodayDate(new Date()) + " " + new Date().getHours()+":"+new Date().getMinutes();
        		lastMovedItem.item.addClass("task-complete").find(".task-footer > .pull-right").remove();
        		lastMovedItem.item.find(".task-footer").append('<div class="pull-right">completed Time '+completedTime+'</div>');
        		
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

Dropzone.autoDiscover = false;
$("#my-dropzone").dropzone({
	url : "${applicationHome}/uploadfile/${projectDetails.code}",
	addRemoveLinks : false,
	success : function(file, response) {
		var imgName = response;
		file.previewElement.classList.add("dz-success");
		var fileIcon = "fa-file-text";
		if (file.type == "text/xml") {
			fileIcon = "fa-file-excel-o";
		}
		
		$("#upladedFilesList").append('<li><a href="${applicationHome}/download/${projectDetails.code}/'+file.name+'?fileName='+file.name+'"><i class="fa '+fileIcon+'"></i>'+file.name+'</a></li>');
		console.log(file.name);
	},
	error : function(file, response) {
		file.previewElement.classList.add("dz-error");
	}
});
</script>
