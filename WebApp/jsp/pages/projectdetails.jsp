<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li><a href="${applicationHome}/projects">Projects</a></li>                  
     <li class="active">${projectDetails.name}</li>
 </ul>
 <!-- END BREADCRUMB -->  
 <style>
#createtaskmodal .modal-dialog {
	width: 70%;
}

#employeeListForTaskTable th{
font-weight: normal;
font-size: 8pt;
}

div#task-pop-up {
  display: none;
  position: absolute;
  width: 280px;
  padding: 10px;
  background: #eeeeee;
  color: #000000;
  border: 1px solid #1a1a1a;
  font-size: 90%;
}

.dataTables_length {
width: 40%;
}

.dataTables_filter{
width: 60%;
}
.task_progress .progress {
		height: 10px;
	}
	</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top">  
	<div class="row">
                  <div class="col-md-7">                      
            <h4><span class="fa ">${projectDetails.code}</span> - <span class="editableFieldText">${projectDetails.name}</span></h4>
            </div>
          <!--   <div class="col-md-5 text-right">               
           <span style="background-color: lightgray;" class="btn  btn-default" disabled=disabled>To Do</span>       
           <span style="background-color: lightblue;"  class="btn  btn-default" disabled=disabled>Assign</span>
           <a href="#" class="btn  btn-default">Reviewed</a>
           <a href="#" class="btn  btn-default">Completed</a>
            </div> -->
            </div>
	</div>        
	      <div class="row">
	      
                  <div class="col-md-8">
                      <section class="panel">
                         <!--  <div class="project project-heading">
                              <strong> Sub Tasks</strong>
                              <p>Sometimes the simplest things are the hardest to find.dddddddd dsdsasds Sometimes the simplest things are the hardest to find. 
                              Sometimes the simplest things are the hardest to find. 
                              Sometimes the simplest things are the hardest to find. Sometimes the simplest things are the hardest to find. Sometimes the simplest t
                              hings are the hardest to find.</p>
                          </div> -->
                          <div class="panel-body bio-graph-info">
                              <!--<h1>New Dashboard BS3 </h1>-->
                              <div class="row project_details">
                              <c:set var="projectState" value="info"/>
			                  <c:if test="${projectDetails.status == 'COMPLETED' }">
			                   	<c:set var="projectState" value="success"/>
			                  </c:if>
			                  <c:if test="${projectDetails.projectMissedTimeLine == true }">
			                  	<c:set var="projectState" value="danger"/>
			                  </c:if>
                                  <div class="col-md-6">
                                      <p><span class="bold">Created by </span>: ${projectDetails.reporterName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p class="text-capitalize"><span class="bold">Status </span>: <span class="label label-${projectState}">${projectDetails.status}</span></p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Created </span>: ${projectDetails.createdDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Last Updated</span>: ${projectDetails.lastModifiedDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p class="text-capitalize"><span class="bold">Project Type </span>: ${projectDetails.type}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p class="text-capitalize"><span class="bold text-capitalize">Project Complexity </span>: ${projectDetails.complexity}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Category </span>: ${projectDetails.category}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Account </span>: ${projectDetails.accountName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Team </span>: ${projectDetails.teamName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Sub Team </span>: ${projectDetails.subTeamName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Client </span>: ${projectDetails.clientName}</p>
                                  </div>
                                   <div class="col-md-6">
                                   	<ul class="nav nav-pills nav-stacked labels-info ">
                                 	 <li><span class="bold">Priority </span> : <i class=" fa fa-circle text-danger"></i> ${projectDetails.priority}<p></p></li>
	                              </ul>
	                              </div>
	                              <input type="hidden" id="hdn_project_id" value="${projectDetails.id}">
	                              <c:if test="${projectDetails.status != 'NEW'}">
                                  <div class="col-md-6">
                                      <p><span class="bold">Assignee </span>:
                                       
                                        <c:if test="${not empty projectDetails.projectTaskDeatils}">
                                 			<c:forEach var="taskDetails" items="${projectDetails.projectTaskDeatils}" varStatus="taskIndex"> 
		                                      <span class="project_team">
		                                         <a href="#"><img alt="image" src="${applicationHome}/image/${taskDetails.assignee}"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
		                                      </span>
                                      </c:forEach>
                                      </c:if>
                                      </p>
                                  </div>
									</c:if>
                                  <div class="col-lg-12">
                                      <dl class="dl-horizontal mtop20 p-progress">
                                          <dt>Project Completed:</dt>
                                          <dd>
                                          <c:set var="progressState" value="striped"/>
			                  				<c:if test="${projectDetails.status == 'INPROGRESS' }">
			                   					<c:set var="progressState" value="striped active"/>
			                  				</c:if>
			                  				<c:if test="${projectDetails.status == 'COMPLETED' }">
			                   					<c:set var="progressState" value=""/>
			                  				</c:if>
                                              <div class="progress progress-${progressState}">
                                                  <div style="width: ${projectDetails.projectCompletionPercentage}%;" class="progress-bar progress-bar-${projectState}"></div>
                                                   <small>${projectDetails.projectCompletionPercentage}% Complete</small>
                                              </div>
                                              <small>Project complition <strong>${100 - projectDetails.projectCompletionPercentage}%</strong> Remaining</small>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>
                              <div class="row">
	                    <div class="col-md-12">
	                    <h5 class="bold">Description</h5>
	                      <p>
                                  <span class="editableFieldTextArea">${projectDetails.description}</span>
                              </p>
	                    </div>
	                    </div>
                              
						<div class="row">
	                    <div class="col-md-12">
	                    	  <div class="form-group">
                                <label class="col-md-2 control-label">Attachments</label>
                                <div class="col-md-12">
                                   <div class="dropzone dropzone-mini dz-clickable" id="my-dropzone">
							        </div>
								</div>
								</div>
                    	</div>
                    </div>
                          </div>

                      </section>
						<div class="col-md-12">
						<h4>Comments (${projectDetails.projectComments.size()})</h4>
							<div class="row">
							  <div class="messages messages-img">
							  <c:if test="${not empty projectDetails.projectComments}">
                                 <c:forEach var="comment" items="${projectDetails.projectComments}"> 
					              <div class="item">
					                <div class="image">
					                    <img src="${comment.user.filePhoto}" alt="${comment.user.firstName} ${comment.user.lastName}" onerror="this.src='${assets}/images/users/no-image.jpg'">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">${comment.user.firstName} ${comment.user.lastName}</a>
					                        <span class="date">${comment.createdDate}</span>
					                    </div>                                    
					                   ${comment.description}
					                </div>
					            </div>
					         </c:forEach>
					         </c:if>  
                                
                        <div class="panel-body panel-body-search">
                            <div class="input-group">
                                <div class="input-group-btn">
                                    
                                </div>
                               <textarea class="form-control" placeholder="Enter your comments" id="comment_textarea"></textarea>
                                <div class="input-group-btn" style="padding-left: 5px">
                                    <button class="btn btn-primary" onclick="addComment()">Add</button>
                                </div>
                            </div>
                        </div>
					       </div>
							
							</div>
						</div>
                      <section class="panel">
                        <header class="panel-heading">
                          Last Activity
                        </header>
                        <div class="panel-body">
                            <table class="table table-hover p-table">
                          <thead>
                          <tr>
                              <th>Title</th>
                              <th>Updated Time</th>
                              <th>Comments</th>
                              <th>Status</th>
                          </tr>
                          </thead>
                          <tbody>
                           <c:if test="${not empty projectDetails.projectActivityDetails}">
                                 <c:forEach var="activities" items="${projectDetails.projectActivityDetails}"> 
                          <tr>
                              <td>
                                  Project ${activities.projectActivityType}
                              </td>
                              <td>
                                  ${activities.modifiedDate}
                              </td>
                              <td>
                              <c:if test="${activities.projectActivityType == 'CREATED'}">
                              	${activities.userName} has created the project.
                              </c:if>
                              
                              <c:if test="${activities.projectActivityType == 'TASK_ADDED'}">
                              	${activities.userName} has created the task.
                              </c:if>
                              
                               <c:if test="${activities.projectActivityType == 'COMMENT_ADDED'}">
                              	${activities.userName} has added a comment.
                              </c:if>
                              
                              </td>
                              <td>
                                  <span class="label label-info"> ${activities.projectStatus}</span>
                              </td>
                          </tr>
                          </c:forEach>
                          </c:if>
                          </tbody>
                          </table>
                        </div>
                      </section>

                  </div>
                  <div class="col-md-4">
                      <section class="panel">
                          <div class="panel-body">
                           <div class="row padding-bottom-5" >
                          <div class="col-md-7">
                           <h5 class="bold">Tasks</h5>
                          </div>
                           <div class="col-md-5 text-right">
                            <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
                            <a data-toggle="modal" data-target="#createtaskmodal" onclick="clearTaskDetails();" class="btn btn-success line-height-15" 
                            <c:if test="${projectDetails.unAssignedDuration == 0}">
                            disabled="true"
                            </c:if>
                            >
                               <span class="fa fa-plus"></span> Create a Task</a>
                               </c:if>
                            </div>
                          </div>
                              <table class="table">
                                        <tbody>
                                        
                                        <c:if test="${not empty projectDetails.projectTaskDeatils}">
                                 			<c:forEach var="taskDetails" items="${projectDetails.projectTaskDeatils}" varStatus="taskIndex"> 
                                 			<tr>
                                             <td width="5%">1</td>
                                                <td class="task_progress">${taskDetails.name}<a href="#" id="${taskDetails.taskId}" class="trigger" 
                                                 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
                                                data-toggle="modal" data-target="#createtaskmodal" onclick="loadTaskDetails('${taskDetails.taskId}');"
                                                </c:if>
                                                >  Details</a> 
                                                 
                                                 <c:set var="progressTaskState" value="striped"/>
					                  				<c:if test="${taskDetails.status == 'INPROGRESS' }">
					                   					<c:set var="progressTaskState" value="striped active"/>
					                  				</c:if>
					                  				<c:if test="${taskDetails.status == 'COMPLETED' }">
					                   					<c:set var="progressTaskState" value=""/>
					                  				</c:if>
			                  				
                                                 <div class="progress progress-small progress-${progressTaskState}">
                                                 <c:set var="taskHealth" value="info"/>
                                                 <c:if test="${taskDetails.taskRemainingTime < 1 }">
                                                  <c:set var="taskHealth" value="danger"/>
                                                 </c:if>
                                        			<div class="progress-bar progress-bar-${taskHealth}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: ${taskDetails.percentageOfTaskCompleted}%;"></div>
                                    				 <small>${taskDetails.percentageOfTaskCompleted}% Complete</small>
                                    			</div>
                                                </td>
                                               <td  width="10%"><span class="label label-${taskHealth}">${taskDetails.status}</span></td>
                                                <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
                                               <td width="10%"><a data-box="#confirmationbox" class="mb-control" style="margin-left:5px;" onclick="deleteTask('${taskDetails.name}',${taskDetails.taskId},${projectDetails.id});"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a></td>
                                            	</c:if>
                                            </tr>
                                            <input id="${taskDetails.taskId}name" type="hidden" value="${taskDetails.name}"/>
                                             <input id="${taskDetails.taskId}isReviewTask" type="hidden" value="${taskDetails.reviewTask}"/>
                                            <input id="${taskDetails.taskId}description" type="hidden" value="${taskDetails.description}"/>
                                            <input id="${taskDetails.taskId}startTime" type="hidden" value="${taskDetails.startTime}"/>
                                            <input id="${taskDetails.taskId}completionTime" type="hidden" value="${taskDetails.completionTime}"/>
                                            <input id="${taskDetails.taskId}duration" type="hidden" value="${taskDetails.duration}"/>
                                            <input id="${taskDetails.taskId}priority" type="hidden" value="${taskDetails.priority}"/>
                                            <input id="${taskDetails.taskId}assignee" type="hidden" value="${taskDetails.assignee}"/>
                                            <input id="${taskDetails.taskId}assigneeName" type="hidden" value="${userDetailsMap[taskDetails.assignee].firstName}"/>
                                            <input id="${taskDetails.taskId}helper" type="hidden" value="${taskDetails.helpersList}"/>
                                            <c:set var="helpersNames" value=""/>
                                            <input id="${taskDetails.taskId}helperNames" type="hidden" value="${taskDetails.helperNames}"/>
                                 			</c:forEach>
                                 		</c:if>
                                       </tbody>
                                  </table>
                               <h5 class="bold">Time Tracking</h5>
                              <ul class="nav nav-pills nav-stacked labels-info ">
                                  <li>Estimated start time:<b> ${projectDetails.startTime}</b></li>
                                  <li>Estimated completion time:<b> ${projectDetails.completionTime}</b></li>
                                  <li>Project Duration : <b>${projectDetails.duration} hours</b></li>
                                  <li>Unassinged Duration : <b>${projectDetails.unAssignedDuration} hours</b></li>
                              </ul>

                              <br>
                              
                              <h5 class="bold">Project files</h5>
                              <ul class="list-unstyled p-files" id="upladedFilesList">
                                <c:if test="${not empty projectDetails.filesNames}">
                                 <c:forEach var="fileName" items="${projectDetails.filesNames}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/download/${projectDetails.id}/${fileName}?fileName=${fileName}"><i class="fa fa-file-text"></i>${fileName}</a>
                                 	 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
                                 	<a data-box="#confirmationbox" class="mb-control" style="margin-left:5px;" href="#" onclick="deleteFile('${fileName}');"><i class="fa fa-trash-o" style="color:red" aria-hidden="true"></i></a>
                                 	</c:if>
                                 	</li> 
                                 </c:forEach>
                                 </c:if>
                                  <c:if test="${not empty projectDetails.completedFilesNames}">
                                   <br>
                                  <h6 class="bold">Completed files</h6>
                                 <c:forEach var="fileName" items="${projectDetails.completedFilesNames}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/download/${projectDetails.id}-completed/${fileName}?fileName=${fileName}"><i class="fa fa-file-text"></i>${fileName}</a>
                                 	 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
                                 	<a data-box="#confirmationbox" class="mb-control" style="margin-left:5px;" href="#" onclick="deleteFile('${fileName}');"><i class="fa fa-trash-o" style="color:red" aria-hidden="true"></i></a>
                                 	</c:if>
                                 	</li> 
                                 </c:forEach>
                                 </c:if>
                              </ul>
                              <br>
                               <c:if test="${not empty projectDetails.duplicateProjects}">
                               <h5 class="bold">Duplicate Project</h5>
                               <ul class="list-unstyled p-files" id="duplicateProjects">
                                 <c:forEach var="duplicateProject" items="${projectDetails.duplicateProjects}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/project/${duplicateProject.id}">${duplicateProject.name}</a></li> 
                                 </c:forEach>
                              </ul>
                              <br>
                               </c:if>
							 <c:if test="${not empty projectDetails.tags}">
	                             <h5 class="bold">Project Tags</h5>
	                             <ul class="list-tags">
		                             <c:set var="tags" value="${fn:split(projectDetails.tags, ',')}" />
									 <c:forEach var="tag" items="${tags}"  varStatus="tagsIndex">
									 	<li><a href="#"><span class="fa fa-tag"></span> ${tag}</a></li>
									 </c:forEach>
	                        	</ul>
                        	</c:if>
                        	  <br>
                        	<c:if test="${not empty projectDetails.watchers}">
	                             <h5 class="bold">Watchers</h5>
	                             <ul class="list-tags">
		                             <c:set var="watchers" value="${fn:split(projectDetails.watchers, ',')}" />
									 <c:forEach var="watcher" items="${watchers}"  varStatus="watcherIndex">
									 	<li><a href="#"><span class="fa fa-tag"></span> ${watcher}</a></li>
									 </c:forEach>
	                        	</ul>
                        	</c:if>

                      </section>
                  </div>
              </div>
    
</div>               
<!-- END CONTENT FRAME -->       
<!-- task pop up window start-->
    <div id="task-pop-up">
      <h5><span id="popup-taskname"></span></h5>
      <p>
       <span id="popup-description"></span>
      </p>
      <ul class="nav nav-pills nav-stacked labels-info ">
	        <li>Estimated start time:<b><span id="popup-startTime"></span></b></li>
	        <li>Estimated completion time:<b><span id="popup-completionTime"></span></b></li>
	        <li>Duration : <b><span id="popup-duration"></span> hours</b></li>
	        <li>Assignee : <b><span id="popup-assigneeName"></span></b></li>
	        <li>Helpers : <b><span id="popup-HelperName"></span></b></li>
	        <li>Is review task : <b><span id="popup-isReviewTask"></span></b></li>
	    </ul>
    </div>
<!-- task pop up window end -->

<!-- task create modal start -->
<div class="modal fade" id="createtaskmodal" tabindex="-1"
	role="dialog" aria-labelledby="createtaskmodal" aria-hidden="true">
	<form:form id="createTaskFormId" action="${applicationHome}/createTask" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Create a Task</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/createTaskModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick="doAjaxCreateTaskForm();" id="createOrUpdateTaskId"
						class="btn btn-primary">
						<span id="userButton">Save</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
                         
 <%@include file="includes/footer.jsp" %>  
  <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min.js"></script> 
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
	<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
	<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
       <script>
   	
       var jvalidate = $("#createTaskFormId").validate({
    		 ignore: ".ignorevalidation",
    	   rules: {                                            
    		name: {
    	            required: true,
    	    },
    	    startTime: {
    	    	 required: true
    	    }
    	  }
    	});
       
     $(function() {
    	   var moveLeft = 0;
    	   var moveDown = 0;

    	   $('a.trigger').hover(function(e) {
    		   
    		 var taskId = this.id;
   		    $("#popup-startTime").html($("#"+taskId+"startTime").val());
   		 	$("#popup-completionTime").html($("#"+taskId+"completionTime").val());
   		 	$("#popup-duration").html($("#"+taskId+"duration").val());
   		 	$("#popup-taskname").html($("#"+taskId+"name").val());
   		 	$("#popup-description").html($("#"+taskId+"description").val());
   			$("#popup-assigneeName").html($("#"+taskId+"assigneeName").val());
   			$("#popup-HelperName").html($("#"+taskId+"helperNames").val());
   			$("#popup-isReviewTask").html($("#"+taskId+"isReviewTask").val());
    		 	
    	     $('div#task-pop-up').show()
    	     .css('top', e.pageY + moveDown)
    	      .css('left', e.pageX + moveLeft)
    	      .appendTo('body');
    	   }, function() {
    	     $('div#task-pop-up').hide();
    	   });
    	   
    	   $('a#trigger').mousemove(function(e) {
    		    $("div#task-pop-up").css('top', e.pageY + moveDown).css('left', e.pageX + moveLeft);
    		  });
    	 }); 
   	
var clearTaskDetails = function(){
	getAssignJsonData();
    $("#taskId").val("");
	$("#estStartTime").val("${projectDetails.startTime}");
	$("#estCompleteTime").html("${projectDetails.completionTime}");
	$("#unassignedDuration").html(${projectDetails.unAssignedDuration});
	$("#projectDuration").html(${projectDetails.duration});
 	$("#taskDuration").html(${projectDetails.unAssignedDuration});
	$("#taskName").val("");
	$("#description").val("");
	$("#reviewTask").prop("selectedIndex", 0);
	$("#priority").prop("selectedIndex", 0);
	$("#createOrUpdateTaskId span").html("Save");
    $('#createTaskFormId').prop("action", "${applicationHome}/createTask");
    createTaskDurationList(${projectDetails.unAssignedDuration});
    $("#duration").prop('selectedIndex', ${projectDetails.unAssignedDuration});
    $("#duration").selectpicker('refresh');
    $("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), ${projectDetails.unAssignedDuration}));
    $("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));
    $("#assignTableId").hide(1000);
	$("#toggleAssignTask").html("Assign task");
	resetAssignTable();
	$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
}    

var createTaskDurationList = function(duration){
	 $("#duration").html("");
	for (var index = 0; index <= duration; index++) {
	     $("#duration").append('<option value="'+index+'">'+index+'</option>');
	 }
}

var jsonAssignData = {};

var getAssignJsonData = function(){
	doAjaxRequest("GET", "${applicationHome}/userTaskActivityJson", {},  function(jsonData) {
		jsonAssignData = jsonData;
	   }, function(e) {
	   });
}

getAssignJsonData();

var loadTaskDetails = function(taskId){
	getAssignJsonData();
	$("#taskId").val(taskId);
    $("#estStartTime").val($("#"+taskId+"startTime").val());
 	$("#unassignedDuration").html(${projectDetails.unAssignedDuration});
 	$("#taskName").val($("#"+taskId+"name").val());
 	var isReviewTask = $("#"+taskId+"isReviewTask").val();
 	$("#reviewTask").val("0");
 	console.log("isReviewTask" + isReviewTask);
 	if (isReviewTask == 'true') {
 		$("#reviewTask").val("1");
 	}
 	$('#reviewTask').selectpicker('refresh');
 	$("#projectDuration").html(${projectDetails.duration});
 	$("#taskDuration").html($("#"+taskId+"duration").val());
 	$("#taskName").val($("#"+taskId+"name").val());
 	$("#description").val($("#"+taskId+"description").val());
 	$("#priority").val($("#"+taskId+"priority").val());
 	$('#priority').selectpicker('refresh');
	$("#createOrUpdateTaskId span").html("Update");
    $('#createTaskFormId').prop("action", "${applicationHome}/updateTask");
    createTaskDurationList(${projectDetails.unAssignedDuration}+parseInt($("#"+taskId+"duration").val()));
    $("#duration").prop('selectedIndex', parseInt($("#"+taskId+"duration").val()));
    $("#duration").selectpicker('refresh');
    $("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), parseInt($("#"+taskId+"duration").val())));
    $("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));
	resetAssignTable();
	$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
	
	var assigneeId = $("#"+taskId+"assignee").val();
	var helpers = $("#"+taskId+"helper").val();
	if (assigneeId != "" && assigneeId != 0) {
		 $("#"+assigneeId+"-select").click();
		 $("#assignTableId").show(500);
		 $("#toggleAssignTask").html("Assign task later");
		 if (helpers != "") {
			 var helperIds = helpers.replace("[","").replace("]","").split(",");
			 for (var index = 0; index<helperIds.length; index++) {
				 $("#"+helperIds[index].trim()+"-helper").click();
			 }
		 }
	} else {
		$("#assignTableId").hide(1000);
		$("#toggleAssignTask").html("Assign task");
	}

 	
}  
       
function addComment() {
   var dataString = {"projectId": $('#hdn_project_id').val() , "projectComments": $('#comment_textarea').val() };
   
   doAjaxRequest("POST", "${applicationHome}/addComment", dataString,  function(data) {
	   var responseJsonObj = JSON.parse(data)
       if (responseJsonObj.status){
           window.location.reload(true);
       }
   }, function(e) {
   });
}

$(document).ready(function() {
    
    $('.editableFieldText').editable('saveProjectDetails', {
        indicator : 'Saving...',
        tooltip   : 'Click to edit...',
        cancel	  : '<i class="fa fa-times" aria-hidden="true"></i>',
        submit	  : '<i class="fa fa-check" aria-hidden="true"></i>',
        height 	  : '25px',
        width     : '100%'
    });
    $('.editableFieldTextArea').editable('saveProjectDetails', {
        type      : 'textarea',
        cancel	  : '<i class="fa fa-times fa-2x" aria-hidden="true"></i>',
        submit	  : '<i class="fa fa-check fa-2x" aria-hidden="true"></i>',
        tooltip   : 'Click to edit...',
        rows	  : 5,
        cols	  : 130
    });
    
	Dropzone.autoDiscover = false;
  		$("#my-dropzone").dropzone({
  			url : "${applicationHome}/uploadfile/${projectDetails.id}",
  			addRemoveLinks : false,
  			success : function(file, response) {
  				var imgName = response;
  				file.previewElement.classList.add("dz-success");
  				var fileIcon = "fa-file-text";
  				if (file.type == "text/xml") {
  					fileIcon = "fa-file-excel-o";
  				}
  				
  				$("#upladedFilesList").append('<li><a href="${applicationHome}/download/${projectDetails.id}/'+file.name+'?fileName='+file.name+'"><i class="fa '+fileIcon+'"></i>'+file.name+'</a></li>');
  			},
  			error : function(file, response) {
  				file.previewElement.classList.add("dz-error");
  			}
  		});
});

var deleteFile = function(fileName){
	$(".msgConfirmText").html("Delete file");
	$(".msgConfirmText1").html(fileName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteFile('"+fileName+"')");
}

var deleteTask = function(taskName, taskId, projectId){
	$(".msgConfirmText").html("Delete task");
	$(".msgConfirmText1").html(taskName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteTask("+taskId+","+projectId+")");
}

var doAjaxDeleteTask = function(taskId, projectId){
	var dataString = {"taskId" : taskId, "projectId":projectId};
	var url = '${applicationHome}/deletetask';
	 doAjaxRequest("POST", url, dataString,  function(data) {
		   var responseJsonObj = JSON.parse(data)
	       if (responseJsonObj.status){
	           window.location.reload(true);
	       }
	   }, function(e) {
	   });
}


var doAjaxDeleteFile = function(fileName){
	var dataString = {"fileName" : fileName};
	var url = '${applicationHome}/deletefile/${projectDetails.id}';
	 doAjaxRequest("POST", url, dataString,  function(data) {
		   var responseJsonObj = JSON.parse(data)
	       if (responseJsonObj.status){
	           window.location.reload(true);
	       }
	   }, function(e) {
	   });
}

var toggleAssignTask = function(){
	if ($("#assignTableId").is(':hidden')) {
		$("#assignTableId").show(1000);
		$("#toggleAssignTask").html("Assign task later");
		fillTableFromJson();
	} else {
		$("#assignTableId").hide(1000);
		$("#toggleAssignTask").html("Assign task");
		resetAssignTable();
		$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
	}
}

function doAjaxCreateTaskForm() {
	$('#createTaskFormId').submit();
}

$('#createTaskFormId').ajaxForm(function(response) {
	var responseJson = JSON.parse(response);
	if (responseJson.status) {
		window.location.reload(true);
	}
});
var startProjectTime = '${projectDetails.startTime}';
var completionProjectTime = '${projectDetails.completionTime}';

$('input:radio[name=assignee]').on("click", function(){
	resetAssignTable();
	fillTableFromJson();
	fillAssignTabledBasedOnDate(this.id);
	
});

$('input:checkbox[name=helper]').on("click", function(){
		fillHelperTabledBasedOnDate(this.id);
});


var fillHelperTabledBasedOnDate =function(id){
	var startTaskTime = $("#estStartTime").val();
	var startTimeHour = new Date(startTaskTime).getHours();
	var duration = $("#duration").val();
	var userId = id.split("-")[0];
	if ($("#"+userId+"-helper").prop("checked") == true) {
		markTableFields(userId, startTimeHour, duration, "lightgray", true, false);
	} else {
		markTableFields(userId, startTimeHour, duration, "lightgray", true, true);
	}
}

var fillAssignTabledBasedOnDate =function(id){
	
	var startTaskTime = $("#estStartTime").val();
	var startTimeHour = new Date(startTaskTime).getHours();
	var duration = $("#duration").val();
	var userId = id.split("-")[0];
	$("#"+userId+"-helper").prop("disabled", true);
	markTableFields(userId, startTimeHour, duration, "yellow", false, false);
}

var markTableFields = function(userId, startTimeHour, duration, color, helper, reset){
	
	for (var index = 0; index < duration; index++) {
		console.log("duration :" + duration);
		if(startTimeHour ==  breakTime){
			startTimeHour++;
		}
		var getCell = $("#"+userId+"-"+startTimeHour);
		$("#estStartTime").css("border", "1px solid #D5D5D5");
		if (reset){
			if($(getCell).attr("isassigned") == "true"){
				return;
			}
			var cellBackGroundColor =$(getCell).attr("cellcolor");
			$(getCell).css("background-color", cellBackGroundColor);
			$(getCell).attr("cellmarked",false);
			$(getCell).attr("modified",false);
			decreaseTotalHours(userId);
		} else {
			var cellBackGroundColor = $(getCell).css("background-color");
			if($(getCell).attr("isassigned") == "true"){
				if (helper){
					$("#"+userId+"-helper").prop("checked", false);
					return;
					//error
					
				} else {
					$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
					$("#estStartTime").css("border", "1px solid red");
					return;
					//error
				}
			}
			increaseTotalHours(userId);
			$(getCell).attr("cellcolor", cellBackGroundColor);
			$(getCell).css("background-color", color);
			$(getCell).attr("cellmarked",true);
			$(getCell).attr("modified",true);
		}
		
		if (!helper){
			cellSelectCount++;
		} else {
			$(getCell).attr("celleditable", false);
		}
		
		startTimeHour++;
	}
}

var dateDisplayLogic = function( currentDateTime ){
	
	var startDate = new Date(startProjectTime);
	var startHours = startDate.getHours();
	var dd = startDate.getDate();
	var mm = startDate.getMonth(); //January is 0!
	var yyyy = startDate.getFullYear();
	startHours+=":00";
	if (currentDateTime && currentDateTime.getDate() == dd && currentDateTime.getMonth() == mm && currentDateTime.getFullYear() == yyyy){
		this.setOptions({
			minTime:startHours
		});
	}else
		this.setOptions({
			minTime:'8:00'
		});
};

$.datetimepicker.setLocale('en');
$('.dateTimePicker').datetimepicker({onGenerate:function( ct ){
	$(this).find('.xdsoft_date.xdsoft_weekend')
	.addClass('xdsoft_disabled');
	},
	minDate:startProjectTime, // yesterday is minimum date
	maxDate:completionProjectTime,
	allowTimes:['08:00','09:00','10:00','11:00','12:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00'],
	onChangeDateTime:dateDisplayLogic,
	onShow:dateDisplayLogic
});
	
$("#estStartTime").on("change",function(){
	
	var startProjectDate = new Date(startProjectTime);
	resetAssignTable();
	fillTableFromJson();
	
	var startTaskTime = $("#estStartTime").val();
	var startTaskDate = new Date(startTaskTime);
	
	if(startTaskDate < startProjectDate) {
		$("#estStartTime").css("border", "1px solid red");
		return;
	} else {
		$("#estStartTime").css("border", "1px solid #D5D5D5");
	}
	$("#estCompleteTime").html(getEstimatedCompletionTime( $("#estStartTime").val(), $("#duration").val()));
	$("#taskDuration").html($("#duration").val());
	
	if (!$("#assignTableId").is(':hidden') && $('input:radio[name=assignee]:checked').length > 0) {
		var id = $('input:radio[name=assignee]:checked').attr('id');
		fillAssignTabledBasedOnDate(id);
	}
	
	$("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));


});

$("#duration").on("change",function(){
	resetAssignTable();
	fillTableFromJson();
	$("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), $("#duration").val()));
	$("#taskDuration").html($("#duration").val());
	if (!$("#assignTableId").is(':hidden') && $('input:radio[name=assignee]:checked').length > 0) {
		var id = $('input:radio[name=assignee]:checked').attr('id');
		fillAssignTabledBasedOnDate(id);
	}
});

var getEstimatedCompletionTime = function(startTime, duration){
	var estimatedCompletionTime = new Date(startTime); 
	estimatedCompletionTime.addHours(duration);
	var completionTimeString = getTodayDate(estimatedCompletionTime);
	var completionHour = estimatedCompletionTime.getHours();
	var startTimeHours =  new Date(startTime).getHours();
	if(completionHour > 13 && startTimeHours < 13){
		estimatedCompletionTime.addHours(1);
	}
	completionTimeString +=(" " +estimatedCompletionTime.getHours()+":00");
	return completionTimeString;
}

$(document).ready(function() {
    $('#employeeListForTaskTable').DataTable({ 
    	responsive: true,
        "lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
        "ordering": false,
    
    });
    
    $("#employeeListForTaskTable_filter").append('<span style="float:left;font-weight: bold;margin-top: 7px;"><a hre="#"><i class="fa fa-angle1-double-left fa-x" aria-hidden="true"></i></a> <span style="margin-left: 10px;margin-right: 10px;" id="currentAssignmentDate"></span> <a hre="#"><i class="fa fa-angle1-double-right fa-x" aria-hidden="true"></i></a></span>');
} );

var cellSelectCount = 0;
var breakTime = 13;
var checkNextAndPreviousMarked = function(thisVarId, checkOrUnchek){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	var nextMarked = false;
	var preMarked = false;
	var sameMarked = false;
	var celleditable = $("#"+userId+"-"+time).attr("celleditable");
	var cellmarked	= $("#"+userId+"-"+time).attr("cellmarked");
	if (cellmarked == 'true' && celleditable == 'true') {
		sameMarked = true;
	}
	preMarked = isPreMarked(thisVarId);
	nextMarked = isNextMarked(thisVarId);
	
	if (checkOrUnchek) {
		return (cellSelectCount == 0 || preMarked || nextMarked) && !sameMarked;
	}
	return (!preMarked || !nextMarked) && sameMarked;
}

var isPreMarked = function(thisVarId){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	var preMarked = false;
	
	if (time <= 21 && 8 < time) {
		var tmpTime = time - 1;
		if (time == breakTime+1) {
			tmpTime--;
		}
		var celleditable = $("#"+userId+"-"+tmpTime).attr("celleditable");
		var cellmarked	= $("#"+userId+"-"+tmpTime).attr("cellmarked");
		if (cellmarked == 'true' && celleditable == 'true') {
			preMarked = true;
		}
	}
	return preMarked;
}


var isNextMarked = function(thisVarId){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	var nextMarked = false;
	
	if (time >= 8 && 21 > time) {
		var tmpTime = time + 1;
		if (time == breakTime-1) {
			tmpTime++;
		}
		var celleditable = $("#"+userId+"-"+tmpTime).attr("celleditable");
		var cellmarked	= $("#"+userId+"-"+tmpTime).attr("cellmarked");
		if (cellmarked == 'true' && celleditable == 'true') {
			nextMarked = true;
		}
	}
	return nextMarked;
}


$("table#employeeListForTaskTable").on("click", "tr.editable td.markable", function(){
	
	var maxDuration = $('#duration > option').length;
	
	var cellId = this.id;
	var userId  = cellId.split("-")[0];
	var hourId = cellId.split("-")[1];
	
	var cellBackGroundColor = $(this).css("background-color");
	var celleditable = $("#"+userId+"-"+hourId).attr("celleditable");
	if (cellBackGroundColor == "rgb(0, 0, 255)" || celleditable == "false") {
		return;
	}
	if(cellSelectCount == 0) {
		$("#"+userId+"-select").prop('checked', true);
		$("#"+userId+"-helper").prop('disabled', true);
	}
	//$('input:radio[name=assignee]').each(function () { $(this).prop('disabled', true); });
	//$("#"+userId+"-select").prop('disabled', false);
	
	if (cellBackGroundColor == "rgb(255, 255, 0)" && checkNextAndPreviousMarked(this.id, false)) {
		var cellColor = $(this).attr("cellcolor");
		$(this).css("background-color", cellColor);
		decreaseTotalHours(userId);
		cellSelectCount--;
		$(this).attr("cellmarked",false);
		$(this).attr("modified",false);
		$('input:checkbox[name=helper]:checked').each(function () { $(this).click();});
		if (isPreMarked(this.id) && cellSelectCount == 1) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(hourId-1);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		}
		
		if (isNextMarked(this.id)) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(parseInt(hourId)+1);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		}
		
	} else if (checkNextAndPreviousMarked(this.id, true)){
		var startProjectDate = new Date(startProjectTime);
		if(hourId < startProjectDate.getHours()) {
			return;
		} 
		
		if (maxDuration - 1 == cellSelectCount) {
			return;
		}
		$(this).attr("cellcolor", cellBackGroundColor);
		$(this).css("background-color", "yellow");
		increaseTotalHours();
		cellSelectCount++;
		increaseTotalHours(userId);
		$(this).attr("cellmarked",true);
		$(this).attr("modified",true);
		$('input:checkbox[name=helper]:checked').each(function () { $(this).click();});
		if (cellSelectCount == 1) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(hourId);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		} else if (!isPreMarked(this.id)) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(hourId);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		}
	}
	
	if (cellSelectCount == 0) {
		$("#employeeListForTaskTable tr").addClass("editable");
		$('input:radio[name=assignee]').each(function () { $(this).prop('disabled', false); });
		$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
		
		$('input:checkbox[name=helper]').each(function () { $(this).prop('disabled', false); });
		$('input:checkbox[name=helper]').each(function () { $(this).prop('checked', false); });
	}
	
	$("#estCompleteTime").html(getEstimatedCompletionTime( $("#estStartTime").val(), cellSelectCount));
	$("#duration").val(cellSelectCount);
	$("#duration").selectpicker("refresh");
	
});

var resetAssignTable = function(){
	
	$('table#employeeListForTaskTable td[cellmarked="true"]').each(function () {
		
		$("#employeeListForTaskTable tr").addClass("editable");
		$('input:checkbox[name=helper]').each(function () { $(this).prop('disabled', false); });
		$('input:checkbox[name=helper]').each(function () { $(this).prop('checked', false); });
		var cellColor = $(this).attr("cellcolor");
		$(this).css("background-color", cellColor);
		cellSelectCount=0;
		$(this).attr("cellmarked",false);
		$(this).attr("modified",false);
		$(this).attr("celleditable", true);
		$(this).attr("isassigned",false);
		
	});
	
	$('table#employeeListForTaskTable td[dynamicvalue="0"]').each(function () {
		$(this).html("0");
	});
	
	$('table#employeeListForTaskTable td[dynamicvalue="8"]').each(function () {
		$(this).html("8");
	});
}


var increaseTotalHours = function(userId){
	var totalHours = parseInt($("#"+userId+"-totalHours").html()) + 1;
	var availableHours = parseInt($("#"+userId+"-availabeHours").html()) - 1;
	$("#"+userId+"-totalHours").html(""+totalHours);
	$("#"+userId+"-availabeHours").html(""+availableHours);
	
	if (availableHours < 0){
		$("#"+userId+"-availabeHours").css("color", "red");
		$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
	} else {
		$("#"+userId+"-availabeHours").css("color", "green");
	}
}

var decreaseTotalHours = function(userId){
	var totalHours = parseInt($("#"+userId+"-totalHours").html()) - 1;
	var availableHours = parseInt($("#"+userId+"-availabeHours").html()) + 1;
	$("#"+userId+"-totalHours").html(""+totalHours);
	$("#"+userId+"-availabeHours").html(""+availableHours);
	
	if (availableHours <= 0){
		$("#"+userId+"-availabeHours").css("color", "red");
	} else {
		$("#"+userId+"-availabeHours").css("color", "green");
	}
}

var fillTableFromJson = function(){
	
	$.each(JSON.parse(jsonAssignData), function(idx, elem){
		if (getTodayDate(new Date($("#estStartTime").val())) == elem.dateId) {
			var hour = parseInt(elem.startHour);
			var duration = elem.durationInMinutes/60;
			var isCompleted = elem.actualEndTime == null ?false:true;
			var isInprogress = elem.actualStartTime == null?false:true;
	
			for (var index = 0; index < duration; index++) {
				
				if (hour == breakTime) {
					hour++;
				}
				var cellId = $("#"+elem.userId+"-"+hour);
				hour++;
				if($("#taskId").val() != "" && elem.taskId == $("#taskId").val()  &&  elem.taskId != 0) {
				} else {
					$(cellId).attr("celleditable", false);
					$(cellId).attr("isassigned",true);
					$(cellId).attr("cellmarked",true);
					$(cellId).attr("modified",false);

					var cellStausColor = isInprogress ? "rgb(250, 128, 114)" :  "rgb(0, 0, 255)"; 
					cellStausColor = isCompleted ? "rgb(0, 128, 0)" :cellStausColor;
					
					var cellTitleTaskName = "Task Id :"elem.taskId + ", Activity Id:" + elem.taskActivityId+", Task Name :" +  elem.taskName;
					if (elem.userTaskType == "PROJECT") {
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="font-size: 18px;font-weight: bold;text-align: center;color: wheat;">P</span>');
					} else if (elem.userTaskType == "PROJECT_HELPER") {
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="font-size: 18px;font-weight: bold;text-align: center;color: wheat;">PH</span>');
					} else if (elem.userTaskType == "LEAVE") {
						cellStausColor = "rgb(128, 128, 128)";
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="font-size: 18px;font-weight: bold;text-align: center;color: wheat;">L</span>');
					} else if (elem.userTaskType == "MEETING") {
						cellStausColor = "rgb(0, 139, 139)";
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="font-size: 18px;font-weight: bold;text-align: center;color: wheat;">M</span>');
					}else if (elem.userTaskType == "PROJECT_HELPER_REVIEW") {
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="font-size: 18px;font-weight: bold;text-align: center;color: wheat;">PRH</span>');
					}else if (elem.userTaskType == "PROJECT_REVIEW") {
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="font-size: 18px;font-weight: bold;text-align: center;color: wheat;">PR</span>');
					}

					if ($(cellId).css("background-color") == cellStausColor) {
						$(cellId).css("background-color", "rgb(255, 0, 0)");
					} else {
						$(cellId).attr("cellcolor", $(cellId).css("background-color"));
						$(cellId).css("background-color", cellStausColor);
					}
	
					increaseTotalHours(elem.userId);
				}
				
			}
		}
	});
}

</script>
