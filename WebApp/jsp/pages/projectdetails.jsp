<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
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
	</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top">  
	<div class="row">
                  <div class="col-md-7">                      
            <h4><span class="fa ">${projectDetails.code}</span> - <span class="editableFieldText">${projectDetails.name}</span></h4>
            </div>
            <div class="col-md-5 text-right">               
           <span style="background-color: lightgray;" class="btn  btn-default" disabled=disabled>To Do</span>       
           <span style="background-color: lightblue;"  class="btn  btn-default" disabled=disabled>Assign</span>
           <a href="#" class="btn  btn-default">Reviewed</a>
           <a href="#" class="btn  btn-default">Completed</a>
            </div>
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
                                  <div class="col-md-6">
                                      <p><span class="bold">Created by </span>: ${projectDetails.reporterName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p class="text-capitalize"><span class="bold">Status </span>: <span class="label label-primary">${projectDetails.status}</span></p>
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
                                      <p><span class="bold">Account </span>: ${projectDetails.accountId}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Team </span>: ${projectDetails.teamId}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Sub Team </span>: ${projectDetails.teamId}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Client </span>: ${projectDetails.clientId}</p>
                                  </div>
                                   <div class="col-md-6">
                                   	<ul class="nav nav-pills nav-stacked labels-info ">
                                 	 <li><span class="bold">Priority </span> : <i class=" fa fa-circle text-danger"></i> ${projectDetails.priority}<p></p></li>
	                              </ul>
	                              </div>
	                              <input type="hidden" id="hdn_project_id" value="${projectDetails.id}">
                                  <div class="col-md-6">
                                      <p><span class="bold">Assignee </span>:
                                      <span class="project_team">
                                         <a href="#"><img alt="image" src=""  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
                                      </span>
                                      </p>
                                  </div>

                                  <div class="col-lg-12">
                                      <dl class="dl-horizontal mtop20 p-progress">
                                          <dt>Project Completed:</dt>
                                          <dd>
                                              <div class="progress progress-striped active ">
                                                  <div style="width: 40%;" class="progress-bar progress-bar-danger"></div>
                                              </div>
                                              <small>Project completed in <strong>80%</strong>. Remaining close the project, sign a contract and invoice.</small>
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
						<h4>Comments</h4>
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
					         <div class="panel panel-default push-up-10">
                                    <div class="input-group">
                                        <textarea class="form-control" placeholder="Enter your comments" id="comment_textarea"></textarea>
                                        <div class="input-group-btn">
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
                            <a data-toggle="modal" data-target="#createtaskmodal" onclick="clearTaskDetails();" class="btn btn-success line-height-15">
                               <span class="fa fa-plus"></span> Create a Task</a>
                            </div>
                          </div>
                              <table class="table">
                                        <tbody>
                                        
                                        <c:if test="${not empty projectDetails.projectTaskDeatils}">
                                 			<c:forEach var="taskDetails" items="${projectDetails.projectTaskDeatils}" varStatus="taskIndex"> 
                                 			<tr>
                                             <td width="5%">1</td>
                                                <td><a data-toggle="modal" data-target="#createtaskmodal" onclick="loadTaskDetails('${taskDetails.taskId}');">${taskDetails.name}</a> 
                                                 <div class="progress progress-small progress-striped active">
                                        			<div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
                                    			</div>
                                                </td>
                                               <td  width="10%"><span class="label label-warning">${taskDetails.status}</span></td>
                                            </tr>
                                            <input id="${taskDetails.taskId}name" type="hidden" value="${taskDetails.name}"/>
                                            <input id="${taskDetails.taskId}description" type="hidden" value="${taskDetails.description}"/>
                                            <input id="${taskDetails.taskId}startTime" type="hidden" value="${taskDetails.startTime}"/>
                                            <input id="${taskDetails.taskId}completionTime" type="hidden" value="${taskDetails.completionTime}"/>
                                            <input id="${taskDetails.taskId}duration" type="hidden" value="${taskDetails.duration}"/>
                                             <input id="${taskDetails.taskId}priority" type="hidden" value="${taskDetails.priority}"/>
                                 			</c:forEach>
                                 		</c:if>
                                       </tbody>
                                  </table>
                               <h5 class="bold">Time Tracking</h5>
                              <ul class="nav nav-pills nav-stacked labels-info ">
                                  <li>Estimated start time:<b> ${projectDetails.startTime}</b></li>
                                  <li>Estimated completion time:<b> ${projectDetails.completionTime}</b></li>
                                  <li>Duration : <b>${projectDetails.duration} hours</b></li>
                              </ul>

                              <br>
                              
                              <h5 class="bold">Project files</h5>
                              <ul class="list-unstyled p-files" id="upladedFilesList">
                                <c:if test="${not empty projectDetails.filesNames}">
                                 <c:forEach var="fileName" items="${projectDetails.filesNames}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/download/${projectDetails.code}/${fileName}?fileName=${fileName}"><i class="fa fa-file-text"></i>${fileName}</a><a data-box="#confirmationbox" class="mb-control" style="margin-left:5px;" href="#" onclick="deleteFile('${fileName}');"><i class="fa fa-times" style="color:red" aria-hidden="true"></i></a></li> 
                                 </c:forEach>
                                 </c:if>
                              </ul>
                              <br>
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
      <h3>Pop-up div Successfully Displayed</h3>
      <p>
        This div only appears when the trigger link is hovered over.
        Otherwise it is hidden from view.
      </p>
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
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
	<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
       <script>
   	
/*        $(function() {
    	   var moveLeft = 0;
    	   var moveDown = 0;

    	   $('a.trigger').hover(function(e) {
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
    	 }); */
   	
var clearTaskDetails = function(){
    $("#taskId").val("");
	$("#estStartTime").val("${projectDetails.startTime}");
	$("#estCompleteTime").val("${projectDetails.completionTime}");
	$("#unassignedDuration").html("5");
	$("#taskName").val("");
	$("#description").val("");
	$("#priority").prop("selectedIndex", 0);
	$("#createOrUpdateTaskId span").html("Save");
    $('#createTaskFormId').prop("action", "${applicationHome}/createTask");
}    

var loadTaskDetails = function(taskId){
	$("#taskId").val(taskId);
    $("#estStartTime").val($("#"+taskId+"startTime").val());
 	$("#estCompleteTime").val($("#"+taskId+"completionTime").val());
 	$("#unassignedDuration").html($("#"+taskId+"duration").val());
 	$("#taskName").val($("#"+taskId+"name").val());
 	$("#description").val($("#"+taskId+"description").val());
 	$("#priority").val($("#"+taskId+"priority").val());
 	$('#priority').selectpicker('refresh');
	$("#createOrUpdateTaskId span").html("Update");
    $('#createTaskFormId').prop("action", "${applicationHome}/updateTask");
 	
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

$.datetimepicker.setLocale('en');
$('.dateTimePicker').datetimepicker({value:new Date(),onGenerate:function( ct ){
	$(this).find('.xdsoft_date.xdsoft_weekend')
	.addClass('xdsoft_disabled');
	},
	minDate:'${projectDetails.startTime}', // yesterday is minimum date
	maxDate:'${projectDetails.completionTime}',
	allowTimes:['08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00'],
	});

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
  			url : "${applicationHome}/uploadfile/${projectDetails.code}",
  			addRemoveLinks : false,
  			success : function(file, response) {
  				var imgName = response;
  				file.previewElement.classList.add("dz-success");
  				var fileIcon = "fa-file-text";
  				if (file.type == "text/xml") {
  					fileIcon = "fa-file-excel-o";
  					//fa-file-archive-o
  					//fa-file-audio-o
  					//fa-file-code-o
  					//fa-file-excel-o
  					//fa-file-image-o
  					//fa-file-movie-o
  					//fa-file-pdf-o
  					//fa-file-video-o
  					//fa-file-powerpoint-o
  					//fa-file-word-o
  				}
  				
  				$("#upladedFilesList").append('<li><a href="${applicationHome}/download/${projectDetails.code}/'+file.name+'?fileName='+file.name+'"><i class="fa '+fileIcon+'"></i>'+file.name+'</a></li>');
  				console.log(file.name);
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

var doAjaxDeleteFile = function(fileName){
	var dataString = {"fileName" : fileName};
	var url = '${applicationHome}/deletefile/${projectDetails.code}';
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
		$("#toggleAssignTask").html("Hide Assign task");
	} else {
		$("#assignTableId").hide(1000);
		$("#toggleAssignTask").html("Assign task");
		$('input:radio[name=selection]').each(function () { $(this).prop('checked', false); });
	}
}

$(document).ready(function() {
    $('#employeeListForTaskTable').DataTable({ 
    	responsive: true,
        "lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
        "ordering": false,
    
    });
} );

function doAjaxCreateTaskForm() {
	$('#createTaskFormId').submit();
}

$('#createTaskFormId').ajaxForm(function(response) {
	console.log(response);
	var responseJson = JSON.parse(response);
	if (responseJson.status) {
		window.location.reload(true);
	}
});

</script>
