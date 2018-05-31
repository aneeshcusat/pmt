<%@include file="includes/header.jsp"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="userDetailsMap"
	value="${applicationScope.applicationConfiguraion.userMap}" />
<ul class="breadcrumb">
	<li><a href="${applicationHome}/index">Home</a></li>
	<li class="active">Tasks</li>
</ul>
<style>

#unbillableTaskCreationModal .modal-dialog {
	width: 50%;
}

.fa-clock-o,.fa-pause,.fa-play {
	margin-right: 6px;
}
.fa-pause{
	color: red;
}
.fa-play{
	color: green;
}
.changeBackground {
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

.taskName {
	color: #2BD5D0;
}

.taskLabel {
	float: right;
	border: 1px solid blue;
	width: 30px;
	height: 28px;
	text-align: center;
	font-weight: bold;
	font-size: 20px;
	color: wheat;
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

.list-group-item.active,.list-group-item.active:hover,.list-group-item.active:focus
	{
	background: lightblue;
	border-color: #1b1e24;
}

.list-group-horizontal .list-group-item {
	margin-bottom: 0;
	margin-left: -4px;
	margin-right: 0;
}

.list-group-horizontal .list-group-item:first-child {
	border-top-right-radius: 0;
}

.list-group-horizontal .list-group-item:last-child {
	border-bottom-left-radius: 0;
}

.blink_text {

    animation:1s blinker linear infinite;
    -webkit-animation:1s blinker linear infinite;
    -moz-animation:1s blinker linear infinite;
	font-weight:bold;
     color: red;
    }

    @-moz-keyframes blinker {  
     0% { opacity: 1.0; }
     50% { opacity: 0.0; }
     100% { opacity: 1.0; }
     }

    @-webkit-keyframes blinker {  
     0% { opacity: 1.0; }
     50% { opacity: 0.0; }
     100% { opacity: 1.0; }
     }

    @keyframes blinker {  
     0% { opacity: 1.0; }
     50% { opacity: 0.0; }
     100% { opacity: 1.0; }
     }
.task-item{
	display: none;
}
</style>
<script>
 var taskTimerMap = {};
 
 var getRemaining = function(taskId) {
	 
	 if (!$(".taskPlayPause"+taskId).hasClass("fa-pause")){
		 return;
	 }
	 
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
			<h2>
				<span class="fa fa-tasks"></span> Tasks
			</h2>
		</div>
		<div class="pull-right">
			<button class="btn btn-default content-frame-left-toggle">
				<span class="fa fa-bars"></span>
			</button>
		</div>
	</div>
	<!-- END CONTENT FRAME TOP -->
	
		<div class="row ">
			<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body">
					<form class="form-horizontal">
						<div class="form-group">
							<div class="col-md-5">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="taskActivitySearchId" placeholder="Search for a task">
								</div>
							</div>
							
							<div class="col-md-2">
								<c:if test="${not empty modelViewMap.taskOwners}">
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
											<c:forEach begin="15" end="100" varStatus="loop" step="5">
												<c:if test="${param.dayfilter eq loop.current}">
                                        		<option value="${loop.current}" selected="selected">Last ${loop.current} Days Tasks</option>
                                        		</c:if>
                                        		<c:if test="${param.dayfilter ne loop.current}">
                                        		<option value="${loop.current}">Last ${loop.current} Days Tasks</option>
                                        		</c:if>
                                 			</c:forEach>
										</select>
                                </div>
                            </div>
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
		<div class="col-md-3">
			<h5>Backlog</h5>
			<div class="tasks" id="tasks">
				<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
					<c:forEach var="tasks"
						items="${modelViewMap.projectTaskDetailsData['BACKLOG']}"
						varStatus="taskIndex">
						<div
							class='task-item task-danger task-primary userId${tasks.taskActivityDetails[0].userId} task-item${tasks.taskId}'>
							<div class="task-text col-md-12">
								<div class="col-md-10">
									<span class="taskName clearfix"><b>${tasks.name}</b></span>
								</div>
								<div class="col-md-2">
									<span style="background-color: blue" class="taskLabel">
										<c:if test="${tasks.projectTaskType == 'PRODUCTIVE' }">
											P
										</c:if>
										<c:if test="${tasks.projectTaskType == 'REVIEW' }">
											R
										</c:if>
										<c:if test="${tasks.projectTaskType == 'ITERATION' }">
											I
										</c:if>
									</span>
								</div>
								<div class="col-md-12">
									<span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
								</div>
							</div>
							<div class="task-text">
								<a target="_new"
									href="${applicationHome}/project/${tasks.projectId}"
									style="float: left">Show project</a> <a href="#"
									style="float: right"
									onclick="openTaskDetails('${tasks.taskId}');">View task</a>
							</div>
							<div class="task-footer">
								<div class="pull-right">
									<span class="blink_text blink${tasks.taskId}"></span><a
										href="javascript:void(0)"><span
										class="fa fa-clock-o taskPlayPause${tasks.taskId}"
										data-task-state="notstarted" 
										onclick="taskPlayOrPause(${tasks.taskId});"></span></a><span
										class="durationDiv ${tasks.taskId}">${tasks.duration}
										Hours</span> <span class="${tasks.taskId} taskRemainingDiv"
										style="display: none"><span class="taskHour">${tasks.taskActivityDetails[0].timeTakenToCompleteHour}</span>:<span
										class="taskMinutes">${tasks.taskActivityDetails[0].timeTakenToCompleteMinute}</span>:<span
										class="taskSeconds">${tasks.taskActivityDetails[0].timeTakenToCompleteSecond}</span></span>
								</div>
								<div class="pull-left">
									<span class="startDateTimeDiv">Est Start Time :
										${tasks.startTime}</span>
								</div>
							</div>
							<input type="hidden" class="estStartTime"
								value="${tasks.startTime}" /> <input type="hidden"
								class="startTime"
								value='<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${tasks.taskActivityDetails[0].actualStartTime}"/>' />
							<input type="hidden" class="duration" value="${tasks.duration}" />
							<input type="hidden" class="taskPriority" value="${tasks.priority}" />
							<input type="hidden" class="assignee"
								value="${tasks.taskActivityDetails[0].userId}" /> <input
								type="hidden" class="taskStatus" value="${tasks.status}" /> <input
								type="hidden" class="taskId" value="${tasks.taskId}" /> <input
								type="hidden" class="projectId" value="${tasks.projectId}" /> <input
								type="hidden" class="taskActivityId"
								value="${tasks.taskActivityDetails[0].taskActivityId}" />
						</div>
					</c:forEach>
				</c:if>
			</div>
		</div>
		<div class="col-md-3">
			<h5>Today's To Do</h5>
			<div class="tasks" id="tasks">
				<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
					<c:forEach var="tasks"
						items="${modelViewMap.projectTaskDetailsData['ASSIGNED']}"
						varStatus="taskIndex">
						<div
							class='task-item task-info task-primary userId${tasks.taskActivityDetails[0].userId} task-item${tasks.taskId}'>
							<div class="task-text col-md-12">
								<div class="col-md-10">
									<span class="taskName clearfix"><b>${tasks.name}</b></span>
								</div>
								<div class="col-md-2">
									<span style="background-color: blue" class="taskLabel">
										<c:if test="${tasks.projectTaskType == 'PRODUCTIVE' }">
											P
										</c:if>
										<c:if test="${tasks.projectTaskType == 'REVIEW' }">
											R
										</c:if>
										<c:if test="${tasks.projectTaskType == 'ITERATION' }">
											I
										</c:if>
									</span>
								</div>
								<div class="col-md-12">
									<span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
								</div>
							</div>
							<div class="task-text">
								<a target="_new"
									href="${applicationHome}/project/${tasks.projectId}"
									style="float: left">Show project</a> <a href="#"
									style="float: right"
									onclick="openTaskDetails('${tasks.taskId}');">View task</a>
							</div>
							<div class="task-footer">
								<div class="pull-right">
									<span class="blink_text blink${tasks.taskId}"></span><a
										href="javascript:void(0)"><span
										class="fa fa-clock-o taskPlayPause${tasks.taskId}"
										data-task-state="notstarted"
										onclick="taskPlayOrPause(${tasks.taskId});"></span></a><span
										class="durationDiv ${tasks.taskId}"> ${tasks.duration}
										Hours</span> <span class="${tasks.taskId} taskRemainingDiv"
										style="display: none"><span class="taskHour">${tasks.taskActivityDetails[0].timeTakenToCompleteHour}</span>:<span
										class="taskMinutes">${tasks.taskActivityDetails[0].timeTakenToCompleteMinute}</span>:<span
										class="taskSeconds">${tasks.taskActivityDetails[0].timeTakenToCompleteSecond}</span></span>
								</div>
								<div class="pull-left">
									<span class="startDateTimeDiv">Est Start Time :
										${tasks.startTime}</span>
								</div>
								<input type="hidden" class="estStartTime"
									value="${tasks.startTime}" /> <input type="hidden"
									class="startTime" value="" /> <input type="hidden"
									class="duration" value="${tasks.duration}" /> 
									<input type="hidden" class="taskPriority" value="${tasks.priority}" />
									<input
									type="hidden" class="assignee"
									value="${tasks.taskActivityDetails[0].userId}" /> <input
									type="hidden" class="projectId" value="${tasks.projectId}" /> <input
									type="hidden" class="taskStatus" value="${tasks.status}" /> <input
									type="hidden" class="taskId" value="${tasks.taskId}" /> <input
									type="hidden" class="taskActivityId"
									value="${tasks.taskActivityDetails[0].taskActivityId}" />
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
					<c:forEach var="tasks"
						items="${modelViewMap.projectTaskDetailsData['INPROGRESS']}"
						varStatus="taskIndex">
						<div
							class='task-item task-warning task-primary userId${tasks.taskActivityDetails[0].userId} task-item${tasks.taskId}'>
							<div class="task-text col-md-12">
								<div class="col-md-10">
									<span class="taskName clearfix"><b>${tasks.name}</b></span>
								</div>
								<div class="col-md-2">
									<span style="background-color: blue" class="taskLabel">
										<c:if test="${tasks.projectTaskType == 'PRODUCTIVE' }">
											P
										</c:if>
										<c:if test="${tasks.projectTaskType == 'REVIEW' }">
											R
										</c:if>
										<c:if test="${tasks.projectTaskType == 'ITERATION' }">
											I
										</c:if>
									</span>
								</div>
								<div class="col-md-12">
									<span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
								</div>
							</div>
							<div class="task-text">
								<a target="_new"
									href="${applicationHome}/project/${tasks.projectId}"
									style="float: left">Show project</a> <a href="#"
									style="float: right"
									onclick="openTaskDetails('${tasks.taskId}');">View task</a>
							</div>
							<div class="task-footer">
								<div class="pull-right">
									<c:if test="${not empty tasks.taskPausedTime}">
										<span class="blink_text blink${tasks.taskId}">PAUSED</span>
									</c:if>
									<c:if test="${empty tasks.taskPausedTime}">
										<span class="blink_text blink${tasks.taskId}"></span>
									</c:if>
									<a href="javascript:void(0)"><span
										class='fa <c:if test="${not empty tasks.taskPausedTime}">fa-play</c:if>
										<c:if test="${empty tasks.taskPausedTime}">fa-pause</c:if> taskPlayPause${tasks.taskId}'
										<c:if test="${not empty tasks.taskPausedTime}"> data-task-state="paused"</c:if>
										<c:if test="${empty tasks.taskPausedTime}"> data-task-state="running"</c:if>
										onclick="taskPlayOrPause(${tasks.taskId});"></span></a><span
										class="durationDiv ${tasks.taskId}">
										<c:if test="${not empty tasks.taskPausedTime}">
										<fmt:formatDate pattern="yyyy/MM/dd HH:mm"	value="${tasks.taskPausedTime}" />
										</c:if>
										</span> <span
										class="${tasks.taskId} taskRemainingDiv" style="display: none"><span
										class="taskHour">${tasks.taskActivityDetails[0].timeTakenToCompleteHour}</span>:<span
										class="taskMinutes">${tasks.taskActivityDetails[0].timeTakenToCompleteMinute}</span>:<span
										class="taskSeconds">${tasks.taskActivityDetails[0].timeTakenToCompleteSecond}</span></span>
								</div>
								<div class="pull-left">
									<span class="startDateTimeDiv">Started at : <fmt:formatDate
											pattern="yyyy/MM/dd HH:mm"
											value="${tasks.taskActivityDetails[0].actualStartTime}" /></span>
								</div>
							</div>
							<input type="hidden" class="estStartTime"
								value="${tasks.startTime}" /> <input type="hidden"
								class="startTime"
								value="<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${tasks.taskActivityDetails[0].actualStartTime}" />" />
							<input type="hidden" class="duration" value="${tasks.duration}" />
							<input type="hidden" class="taskPriority" value="${tasks.priority}" />
							<input type="hidden" class="assignee"
								value="${tasks.taskActivityDetails[0].userId}" /> <input
								type="hidden" class="taskId" value="${tasks.taskId}" /> <input
								type="hidden" class="taskStatus" value="${tasks.status}" /> <input
								type="hidden" class="projectId" value="${tasks.projectId}" /> <input
								type="hidden" class="taskActivityId"
								value="${tasks.taskActivityDetails[0].taskActivityId}" />
							<c:if test="${empty tasks.taskPausedTime}">
								<script>
                        taskTimerMap["${tasks.taskId}"] = window.setInterval(function(){
                			getRemaining("${tasks.taskId}");
                		}, 1000);
                        </script>
							</c:if>
							<c:if test="${not empty tasks.taskPausedTime}">
							<script>
								//updatePausedTaskTime('${tasks.taskId}', '<fmt:formatDate pattern="yyyy/MM/dd HH:mm"	value="${tasks.taskPausedTime}" />', '<fmt:formatDate pattern="yyyy/MM/dd HH:mm"	value="${tasks.taskActivityDetails[0].actualStartTime}" />');
							</script>
							</c:if>
						</div>
					</c:forEach>
				</c:if>
				<div class="task-drop push-down-10 inprogressDropDown"
					style="display: none;">
					<span class="fa fa-cloud"></span> Drag your task here to start
					tracking time
				</div>
			</div>
		</div>
		<div class="col-md-3">
			<h5>Completed</h5>
			<div class="tasks" id="tasks_completed">
				<div class="task-drop completeDropDown" style="display: none;">
					<span class="fa fa-cloud"></span> Drag your task here to finish it
				</div>
				<c:if test="${not empty modelViewMap.projectTaskDetailsData}">
					<c:forEach var="tasks"
						items="${modelViewMap.projectTaskDetailsData['COMPLETED']}"
						varStatus="taskIndex">
						<div
							class='task-item task-complete task-success userId${tasks.taskActivityDetails[0].userId}'>
							<div class="task-text col-md-12">
								<div class="col-md-10">
									<span class="taskName clearfix"><b>${tasks.name}</b></span>
								</div>
								<div class="col-md-2">
									<span style="background-color: blue" class="taskLabel">
										<c:if test="${tasks.projectTaskType == 'PRODUCTIVE' }">
											P
										</c:if>
										<c:if test="${tasks.projectTaskType == 'REVIEW' }">
											R
										</c:if>
										<c:if test="${tasks.projectTaskType == 'ITERATION' }">
											I
										</c:if>
									</span>
								</div>
								<div class="col-md-12">
									<span class="taskDescription  clearfix"><i>${tasks.description}</i></span>
								</div>
							</div>
							<div class="task-text">
								<a target="_new"
									href="${applicationHome}/project/${tasks.projectId}"
									style="float: left">Show project</a>
							</div>
							<div class="task-footer">
								<div class="pull-left">
									<span class="fa fa-clock-o"></span>${tasks.taskActivityDetails[0].timeTakenToComplete}</div>
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
					<button type="button" onclick="" class="btn btn-primary">
						<span id="taskComplete" onclick="taskComplete()">Complete</span>
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
					<button type="button" onclick="" class="btn btn-primary">
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
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<button id="taskReassign" type="button" class="btn btn-primary hide">
					<span onclick="reAssignTask()">Re Assign Task</span>
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
					<h4 class="modal-title" id="myModalLabel">Create Non-billable time</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/unbillableTaskCreationModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary createUnbillableCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="taskCreate" onclick="createUnbillableTask()"
						class="btn btn-primary" style="display: none">
						<span >Create</span>
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
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script type='text/javascript'
	src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js"></script>
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
	 $(".completedTime").html(completedTime);
  	 $(".recordedStartTime").html(completedTime);
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
		getRemaining(taskId);
	}, 1000, taskId);
	$(".task-item" + taskId).find("input.taskStatus").val("INPROGRESS");
	$(".taskPlayPause"+taskId).removeClass("fa-clock-o");
	$(".taskPlayPause"+taskId).addClass("fa-pause");
	$(".taskPlayPause"+taskId).attr("data-task-state", "running");
}

var taskComplete = function(){
	$("#adjustCompletionTime").removeClass("error");
	$("#adjustStartTime1").removeClass("error");
	var comments =$("#taskCompletionComments").val();
	var taskId = $(lastMovedItem.item).find("input.taskId").val();
	var adjustCompletionTime =$("#adjustCompletionTime").val();
	var adjustStartTime =$("#adjustStartTime1").val();
	
	if (new Date(adjustCompletionTime) < new Date(adjustStartTime)) {
		$("#adjustCompletionTime").addClass("error");
		$("#adjustStartTime1").addClass("error");
		return;
	}
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
            	var taskState = $(".taskPlayPause"+taskId).attr("data-task-state");
            	if (taskState == 'paused') {
            		$(lastMovedItem.sender).sortable('cancel');
            		return;
            	}
            	resetFileUploadUrl(projectId+"-completed");
            	$(".unassignedDuration").html($(lastMovedItem.item).find("input.duration").val());
            	
            	if(this.id == "tasks_completed"){
            		var adjustStartTime = $(lastMovedItem.item).find("input.startTime").val();
                	$("#adjustStartTime1").val(adjustStartTime);
                	var adjustStartTimeDate = new Date(adjustStartTime);
                	var diffTime = (new Date() - adjustStartTimeDate) /(60*1000);
                	famstacklog(diffTime);
                	$("#completedTimeHrs").html(Math.round(diffTime));
                	$("#adjustCompletionTime").removeClass("error");
                	$("#adjustStartTime1").removeClass("error");
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
	$(".taskPriority").html($(".task-item" + taskId).find("input.taskPriority").val());
	
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
		famstacklog(file.name);
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
        $(".taskPlayPause"+taskId).removeClass("fa-play");
		$(".taskPlayPause"+taskId).addClass("fa-pause");
		$(".taskPlayPause"+taskId).attr("data-task-state", "running");
		$(".blink"+taskId).html("");
			
		$("."+taskId+".taskRemainingDiv .taskHour").html(responseJson.startHour);
		$("."+taskId+".taskRemainingDiv .taskMinutes").html(responseJson.startMins);
		$("."+taskId+".taskRemainingDiv .taskSeconds").html(responseJson.startSecs);
		
		$(".task-item" + taskId).find("input.taskActivityId").val(responseJson.taskActivityId);
		$(".task-item" + taskId).find("input.startTime").val(getTodayDateTime(new Date()));
		window.setInterval(function(taskId){
			getRemaining(taskId);
		}, 1000, taskId);

       
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

$('#adjustCompletionTime').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm'});
$('#adjustStartTime').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm'});
$('#adjustStartTime1').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm'});

//unbilled task start
$("#taskType").on("change", function(){
	$("#taskCreate").show();
	if($("#taskType").val() == "") {
		$("#taskCreate").hide();
	}
	
});

$('#startDateRange').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm',useCurrent: false,defaultDate:getTodayDate(new Date()) + " 8:00"});
$('#completionDateRange').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm',useCurrent: false,defaultDate:getTodayDate(new Date()) + " 18:00"});

var validateStartAndEndUBTtime = function(){
	var startDate = $('#startDateRange').val();
	var endDate = $('#completionDateRange').val();
	$('#startDateRange').removeClass("error");
	$('#completionDateRange').removeClass("error");
	if (startDate != "" && endDate != "") {
		if (new Date(startDate) >= new Date(endDate)) {
			$('#startDateRange').addClass("error");
			$('#completionDateRange').addClass("error");
			return false;
		}
	}
	return true;
}

var createUnbillableTask = function(){
	var endDate = "";
	var startDate = "";
	$("#userId").removeClass("error");
	if ($("#taskType").val() != "") {
		startDate = $("#startDateRange").val();
		endDate = $("#completionDateRange").val();
		if (!validateStartAndEndUBTtime()){
			return;
		}
	}
    
	if ($("#userId").val() == "" || $("#taskType").val() == "") {
		$("#userId").addClass("error");
		return;
	}
    
	var taskType = $("#taskType").val();
	var taskActCategory = "";
	
	if (taskType == "LEAVE"){
		taskActCategory = "Leave";
		
	} else if (taskType == "MEETING"){
		taskActCategory = "Meeting";
	} else {
		taskActCategory = taskType;
		taskType = "OTHER";
	}
	
	var dataString = {userId:$("#userId").val(),type:taskType,taskActCategory:taskActCategory,startDate:startDate,endDate:endDate,comments:$("#taskStartComments").val()};
	doAjaxRequest("POST", "${applicationHome}/createNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
        } else {
        	return false;
        }
       
    }, function(e) {
    });
}

var clearUnbillableFormForCreate = function(currentUserId) {
	$("#userId").val(currentUserId);
	$("#taskType").prop("selectedIndex",0);
	$("#startDateRange").val("");
	$("#completionDateRange").val("");
	$("#taskStartComments").val("");
}//unbilled task end

 $(document).ready(function () {
	try{
	   // sortSelect('#taskAssigneeId', 'text', 'asc');
	   //$("#taskAssigneeId").val("userId${currentUser.id}");
	   $("#taskAssigneeId").selectpicker('refresh');
	   $('#taskAssigneeId').trigger('change');
	} catch(err){
		
	}
	
}); 


 $("#taskFilterDayId").on("change", function(){
	 window.location = "/bops/dashboard/tasks?dayfilter="+$("#taskFilterDayId").val();
 });

</script>
