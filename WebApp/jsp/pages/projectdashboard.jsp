<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@include file="includes/header.jsp"%>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>                    
     <li class="active">Projects</li>
 </ul>
 <!-- END BREADCRUMB -->       
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
@media screen and (min-width: 700px) {
	#createprojectmodal .modal-dialog {
		width: 75%;
	}
	.disabled .fc-day-content {
		background-color: #123959;
		color: #FFFFFF;
		cursor: default;
	}
	.project_team img {
		width: 25px;
		border: 2px solid #FFF;
		border-radius: 20%;
	}
	.project_progress .progress {
		margin-bottom: 5px;
		height: 15px;
	}
	
	div.tagsinputWatchers{
	background-color: #fff;
	}
	
	.table thead tr th {
	   font-size:10pt;
	}
	
	.margin5 {
	   margin: 0px 5px 0px 5px;
	}
	
	table th {
		font-size: 7pt !important;
	}
	
	
div#taskDetailsDiv {
    top: 278px;
    left: 756px;
    position: absolute;
    border: 3px solid lightgrey;
    padding: 5px;
    display: block;
    background-color: white;
    width: 400px;
    border-radius: 10px;
    box-shadow: 5px 5px 2px #888888;
}
	
#x {
    position: relative;
    float: right;
    top: -10px;
    right: -10px;
    border-radius: 25px;
    font-size: 16pt;
    font-weight: bold;
    color: red;
    background-color: white;
    border-color: gray;
    z-index: 1000
}

.duplicateTxt {
    font-size: 10px;
    font-weight: 700;
    color: #959595;
    letter-spacing: 1px;
}

.cloneInput{
	height: 25px;
	line-height: 10px;
}

.availabilityStatus{
    font-size: 9px;
    color: green
  }
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame margin5" style="min-height: 500px">
	<div class="content-frame-top">
		<div class="page-title">
			<h2>
				<span class="fa fa-list-alt"></span> Projects - ${userGroupMap[currentUserGroupId].name}
			</h2>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body">
					<form class="form-horizontal">
						<div class="form-group">
							<div class="col-md-8">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="projectSearchBoxId"
										placeholder="Search for a project.." />
									<div class="input-group-btn">
										<button class="btn btn-primary">Search</button>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'MANAGER'}">
								<a data-toggle="modal" data-target="#createprojectmodal" onclick="clearProjectFormForCreate()"
									class="btn btn-success btn-block"> <span class="fa fa-plus"></span>
									Create a new Project
								</a>
								</c:if>
							</div>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
	 <div class="row">
	 	 <div class="col-xs-12">
			<table class="table table-responsive table-hover">
			  <thead>
			        <tr>
			        	<th width="1%"></th>
			        	<th width="10%">Delivery Date</th>
			        	<th width="20%">Project Name</th>
			        	<th width="10%">Team</th>
			        	<th width="10%">Client</th>
			        	<th width="10%">Assignees</th>
			        	<th width="5%">Tasks</th>
			        	<th width="12%">Comments</th>
			        	<th width="10%">Status</th>
			        	<th width="20%">Actions</th>
			        </tr>
			    </thead>
			    <c:if test="${not empty modelViewMap.projectDetailsData}">
			    <tbody>
			    	<c:forEach var="project" items="${modelViewMap.projectDetailsData}">
				      <c:set var="projectState" value="info"/>
	                  <c:if test="${project.status == 'COMPLETED' }">
	                   		<c:set var="projectState" value="success"/>
	                  </c:if>
	                  <c:if test="${project.status == 'NEW' }">
	                  </c:if>
	                  <c:if test="${project.projectMissedTimeLine == true }">
	                  		<c:set var="projectState" value="danger"/>
	                  </c:if>
			        <tr class="clickable projectData ${project.id}" id="projectData${project.id}">
			            <td  onclick="loadDuplicateProjects(${project.id}, '${project.code}', this)" data-toggle="collapse" data-target=".projectData${project.id}"><i id="projectOpenLink${project.id}" style="color: blue" class="fa fa-chevron-right 2x"></i></td>
			            <td>${project.completionTime}</td>
			            <td><a href="${applicationHome}/project/${project.id}">${project.name}</a></td>
			            <td>${project.teamName}</td>
			            <td>${project.clientName}</td>
			           <td class="project_team">
						<c:if test="${not empty project.contributers}">
							<c:forEach var="contributer" items="${project.contributers}" varStatus="taskIndex"> 
									<img alt="image" src="${applicationHome}/image/${contributer}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
							</c:forEach>
						</c:if>
						</td>
			            <td>
			            <c:if test="${not empty project.projectTaskDeatils}">
			            	<a  href="javascrip:void(0);" id="${taskDetails.taskId}" class="taskLink" onclick="taskLinkclick(${project.id}, event);" style="color:blue">${fn:length(project.projectTaskDeatils)} Tasks</a>
			            </c:if>
			            <c:if test="${empty project.projectTaskDeatils}">
			            		None
			            </c:if>
			            </td>
			            
			            <td>${project.description}</td>
			          	<td><span class="label label-${projectState}">${project.status}</span></td>  
			            <td>
							<a href="#" style="margin-right: 7px;color:darkgreen"  title="Edit this project"  data-toggle="modal" data-target="#createprojectmodal" 
								onclick="loadProjectForUpdate('${project.id}')">
								<span class="fa fa-pencil  fa-2x"></span>
							</a>
							<a href="#" data-box="#confirmationbox" style="color:red""  title="Delete this project" class="deleteProject mb-control profile-control-right" 
								onclick="deleteProject('${project.id}','${project.name}');">
								<span class="fa fa-times  fa-2x"></span>
							</a>
						</td>
			        </tr>
					<tr class="collapse projectDataHidden projectData${project.id}">
					<td colspan="11" style="width: 100%; border-bottom:1px dotted gray">
						<span class="duplicateTxt">Quick Project Cloning</span>
					</td>
					</tr>
					
					 <tr class=" collapse projectDataHidden projectData${project.id}" style="background-color: white">
						<td colspan="11">
							<table style="width: 100%" class="table" id="projectDetails${project.id}">
							<tbody>
							<tr class="collapse projectDataHidden projectData${project.id}">
	        			<td colspan="11">
	        				<!-- task creator start -->
	        				<div class="col-xs-12">
									<div class="row">
										<div class="col-md-3">
											<div class="form-group">
												<label class="col-md-4 control-label">Name</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput" value="${project.name}"
														id="prjName${project.id}" /> <span class="help-block"></span>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">Duration</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput"  value="${project.duration}"
														id="prjDuration${project.id}"/>
												</div>
											</div>
										</div>
								
										<div class="col-md-3">
											<div class="form-group">
												<label class="col-md-4 control-label">Start Date</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput estStartTime"
														id="prjStartTime${project.id}" /> <span class="help-block"></span>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">Due Date</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput estCompleteTime"
														id="prjEndTime${project.id}"/>
												</div>
											</div>
										</div>
										<div class="col-md-5">
											<!-- task assign start -->
											
											<%@include file="response/quicktaskreassign.jsp"%>
											
											<!-- task assign start -->
										</div>
										<div class="col-md-1">
											<div class="col-md-12 col-xs-12">
												<a href="javascript:void(0)" style="color:blue"
													title="Clone this project" data-toggle="modal"
													data-target="#createprojectmodal"
													onclick="loadProjectForClone('${project.id}')">
													<span class="fa fa-external-link fa-3x"></span>
												</a>
												<span class="help-block"></span>
											</div>
											<div class="col-md-12 col-xs-12">
												<a href="javascript:void(0)"  style="color:green">
													<span class="fa fa-save fa-3x"></span>
												</a>
											</div>
								
										</div>
									</div>
								</div>
	        				<!--  task creator end -->
	        			</td>
        			</tr>
							</tbody>
							</table>
				    	</td>
				    </tr>
					    	
			        
			      </c:forEach>
			    </tbody>
			</c:if>
			</table>
	 	 </div>
	 </div>
	
	
</div>
<!-- END CONTENT FRAME -->


<!-- project create modal start -->
<div class="modal fade" id="createprojectmodal" tabindex="-1"
	role="dialog" aria-labelledby="createprojectmodal" aria-hidden="true">
	<form:form id="createProjectFormId" action="createProject" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Create a Project</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/createProjectModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="projectActionButton" onclick="doAjaxCreateProjectForm();"
						class="btn btn-primary">
						<span>Save</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- project create modal end -->

<!-- task pop up window start-->
    <div id="taskDetailsDiv" style="display: none;">
	</div>
<!-- task pop up window end -->

	<div class="col-md-12 taskDetailsHidden hide">
	<div class="form-group">
		<div class="col-md-4 col-xs-12">
			<input type="text" class="form-control cloneInput tskName" /> <span class="help-block"></span>
		</div>
		<div class="col-md-3 col-xs-12">
		 	<select class="form-control cloneInput taskCloneAssignee" data-live-search="true">
				<option value="">- select -</option>
				<c:if test="${not empty userMap}">
						<c:forEach var="user" items="${userMap}">
								<option value="${user.id}">${user.firstName}</option>
						</c:forEach>
						</c:if>
			</select>
			 <span class="availabilityStatus" ></span>
		</div>
		<div class="col-md-2 col-xs-12">
			<input type="text" class="form-control cloneInput tskDuration"
				value="0"/>
				
		</div>
		<div class="col-md-2 col-xs-12">
			<select class="form-control cloneInput tskType">
				<option value="0">P</option>
				<option value="1">R</option>
			</select>
		</div>
		<div class="col-md-1 col-xs-12">
			<a href="javascript:void(0)" class="deleteCloneTaskLink"  onclick="deleteCloneTaskItem('')"><span class="fa fa-times" style="color: red"></span></a>
		</div>
	</div>
</div>

<%@include file="includes/footer.jsp"%>

 <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min.js"></script> 
<script type='text/javascript'
	src='${js}/plugins/jquery-validation/jquery.validate.min.js'></script>
<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script type="text/javascript"
	src="${js}/plugins/tagsinput/jquery.tagsinput.min.js"></script>
<script type="text/javascript"
	src="${js}/plugins/fileinput/fileinput.min.js"></script>

<script type="text/javascript"
	src="${js}/plugins/autocomplete/jquery.autocomplete.js"></script>
 <script type="text/javascript" src="${js}/plugins/typeahead/typeahead.bundle.js"></script>
 <script type="text/javascript" src="${js}/plugins/tagsinput/mab-jquery-taginput.js"></script>
<script>

var createTaskDurationList = function(duration){
	 $("#duration").html("");
	for (var index = 0; index <= duration; index++) {
	     $("#duration").append('<option value="'+index+'">'+index+'</option>');
	 }
}

var taskLinkclick = function(projectId, e){
	e.preventDefault();
    $('#taskDetailsDiv').hide();
    
	var dataString = {"projectId" : projectId};
	doAjaxRequestWithGlobal("GET", "${applicationHome}/loadTaskDetailsJSon", dataString, function(data) {
        console.log("SUCCESS: ", data);
        $('#taskDetailsDiv').html(data);
        
        if (data.trim() != "") {
        	$('#taskDetailsDiv').css({'top':e.pageY+10,'left':e.pageX-250, 'position':'absolute', 'border':'1px solid gray', 'padding':'5px'});
        	$('#taskDetailsDiv').show();
        }
    }, function(e) {
        console.log("ERROR: ", e);
        alert(e);
    }, false);
   }


var jvalidate = $("#createProjectFormId").validate({
	 ignore: ".ignorevalidation",
  rules: {                                            
	name: {
           required: true,
   },
   accountId: {
	   	 required: true
	},
	teamId: {
	   	 required: true
	},
	clientId: {
	   	 required: true
	},
	code: {
	   	 required: true
	},
	startTime: {
	   	 required: true
	},
	completionTime: {
	   	 required: true
	},
	category: {
	   	 required: true
	}
 }
});


var clearProjectFormForCreate = function(projectId) {
	$("#createProjectFormId").prop("action","createProject");
	$("#myModalLabel").html("Create a Project");
	$("#projectActionButton span").html("Save");
	clearCreateProjectForm();
	$("#projectcode").val("PRJ" + Math.floor(Date.now() / 1000));
	$('#estStartTime').val(getTodayDate(new Date()) + " 08:00");
	$('#estCompleteTime').val(getTodayDate(new Date()) + " 18:00");
	
}
var loadProjectForUpdate = function(projectId) {
	$("#createProjectFormId").prop("action","updateProject");
	$("#myModalLabel").html("Update a Project");
	$("#projectActionButton span").html("Update");

	loadProject(projectId);
}

var loadProjectForClone = function(projectId) {
	$("#createProjectFormId").prop("action","createProject");
	$("#myModalLabel").html("Duplicate a Project");
	$("#projectActionButton span").html("Duplicate");
	
	loadProject(projectId);
	
	$('#estStartTime').val(getTodayDate(new Date()) + " 08:00");
	$('#estCompleteTime').val(getTodayDate(new Date()) + " 18:00");
}

var loadProject = function(projectId){
	var dataString = {"projectId" : projectId};
	doAjaxRequest("GET", "${applicationHome}/getProjectDetailsJson", dataString, function(data) {
        console.log("SUCCESS: ", data);
        var responseJson = JSON.parse(data);
        clearCreateProjectForm();
        initializeCreateProjectForm(responseJson);
    }, function(e) {
        console.log("ERROR: ", e);
        alert(e);
    });
}

var loadDuplicateProjects = function(projectId, projectCode) {
	
	if ( $(".projectDuplicate"+projectId).length ==0) {
		var dataString = {"projectId" : projectId, "projectCode" : projectCode};
		doAjaxRequestWithGlobal("GET", "${applicationHome}/loadDuplicateProjectsJSon", dataString, function(data) {
	        console.log("SUCCESS: ", data);
	        $(".projectDuplicate"+projectId).remove();
	        $("#projectDetails"+projectId +" tbody").append(data);
	    }, function(e) {
	        console.log("ERROR: ", e);
	        alert(e);
	    }, false);
	}
	
	if ($("#projectOpenLink" + projectId).hasClass("fa-chevron-right")) {
		$("#projectOpenLink" + projectId).removeClass("fa-chevron-right");
		$("#projectOpenLink" + projectId).addClass("fa-chevron-down");
		
		$(".projectData"+projectId).show();	
		
	} else {
		$("#projectOpenLink" + projectId).removeClass("fa-chevron-down");
		$("#projectOpenLink" + projectId).addClass("fa-chevron-right");
		$(".projectData"+projectId).hide();	
	}
}

function clearCreateProjectForm(){
	$("#projectName").val("");
	$("#summary").val("");
	
	$("#billable").attr("checked", false);
	$("#projectType").val("");
	$("#nonbillable").attr("checked", false);
	$('#billable').parent().removeClass("active");
	$('#nonbillable').parent().removeClass("active");
	
	$("#priority").prop("selectedIndex",0);
	$('#priority').selectpicker('refresh');
	
	$("#duration").prop("selectedIndex",0);
	$('#duration').selectpicker('refresh');
	
	$("#projectSubType").prop("selectedIndex",0);
	$('#projectSubType').selectpicker('refresh');
	
	$("#projectLead").prop("selectedIndex",0);
	$('#projectLead').selectpicker('refresh');
	
	$("#complexity").prop("selectedIndex",0);
	$('#complexity').selectpicker('refresh');
	$("#projectcode").val("");
	$("#projectId").val("");
	
	$("#clientId").prop("selectedIndex",0);
	$('#clientId').selectpicker('refresh');
	
	$("#teamId").prop("selectedIndex",0);
	$('#teamId').selectpicker('refresh');
	
	$("#accountId").prop("selectedIndex",0);
	$('#accountId').selectpicker('refresh');
	
	$("#category").prop("selectedIndex",0);
	$('#category').selectpicker('refresh');
	$("#PONumber").val("");
	$("#POidSpan").html("");
	
	$("#watchers").val("");
	
	$("#tags").val("");
	
	$(".tagsinputWatchers span[data-tag]").each(function(){
		$(this).remove();
	});
	
	$(".tagsinput span.tag").each(function(){
		$(this).remove();
	});
}

function initializeCreateProjectForm(project){
	$("#projectName").val(project.name);
	$("#summary").val(project.description);
	
	if (project.type == 'BILLABLE') {
		$("#billable").attr("checked", true);
		$('#billable').parent().addClass("active");
	} else {
		$("#nonbillable").attr("checked", true);
		$('#nonbillable').parent().addClass("active");
	}
	
	$("#projectType").val(project.type);
	
	$("#projectSubType").val(project.projectSubType);
	$('#projectSubType').selectpicker('refresh');
	
	$("#projectLead").val(project.projectLead);
	$('#projectLead').selectpicker('refresh');
	
	$("#priority").val(project.priority);
	$('#priority').selectpicker('refresh');
	
	$("#duration").val(project.duration);
	$('#duration').selectpicker('refresh');
	
	$("#complexity").val(project.complexity);
	$('#complexity').selectpicker('refresh');

	$("#projectcode").val(project.code);
	
	$("#projectId").val(project.id);
	
	$("#quantity").val(project.quantity);
	
	$("#PONumber").val(project.PONumber);
	$("#POidSpan").html(project.PONumber);
	
	$('#estStartTime').val(project.startTime);
	$('#estCompleteTime').val(project.completionTime);

	$("#category").val(project.category);
	$('#category').selectpicker('refresh');
	$('.clientOption').each(function () { $(this).show(); });
	$('#clientId').val(project.clientId);
	$('#clientId').selectpicker('refresh');
	$('#clientId').change();
	
	$("#watchers").val(project.watchers);
	
	if (project.watchers != "") {
		var watchersArray = project.watchers.split(",");
		console.log(watchersArray);
		for (var index=0; index < watchersArray.length; index++) {
			$(".tagsinputWatchers").prepend('<span class="label label-primary" data-tag="'+watchersArray[index]+'">'+watchersArray[index]+'<span class="glyphicon glyphicon-remove"></span></span>');
		}
		$(".twitter-typeahead input.mab-jquery-taginput-input").attr("placeholder","");
	}
	
	if (project.tags != "") {
		var tagsArray = project.tags.split(",");
		for (var index=0; index < tagsArray.length; index++) {
			$(".tagsinput").addTag(tagsArray[index]);
		}
	}
}
	
	$( document ).ready(function(){
		$("input[type='search']").parent().hide();
	});
	
	$('.autocomplete').autocomplete(
		{
			serviceUrl: '${applicationHome}/getProjectJson',
			onSelect : function(suggestion) {
				loadProjectForClone(suggestion.data);
			}
		});

	function doAjaxCreateProjectForm() {
		if (validateEstimatedEndTime()) {
			$('#createProjectFormId').submit();
		}
	}

	$('#createProjectFormId').ajaxForm(function(response) {
		console.log(response);
		var responseJson = JSON.parse(response);
		if (responseJson.status) {
			window.location.reload(true);
		}
	});

	function deleteProject(projectId, projectName){
		$(".msgConfirmText").html("Delete project");
		$(".msgConfirmText1").html(projectName);
		$("#confirmYesId").prop("href","javascript:doAjaxDeleteProject('"+projectId+"')");
	}
	
	function doAjaxDeleteProject(projectId) {
		var dataString = {"projectId" : projectId};
		doAjaxRequest("POST", "${applicationHome}/deleteProject", dataString, function(data) {
            console.log("SUCCESS: ", data);
            var responseJson = JSON.parse(data);
            if (responseJson.status){
                window.location.reload(true);
            }
        }, function(e) {
            console.log("ERROR: ", e);
            alert(e);
        });
	}
	
	
	function performProjectSearch(){
		var serarchText = $('#projectSearchBoxId').val();
		console.log(serarchText);
		if (serarchText != "") {
		$('.projectData').hide();
		$('.projectDataHidden').hide();
		
	    $('.projectData').each(function(){
	       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
	           $(this).show();
	       }
	    });
		} else {
			$('.projectData').show();
		}
	}
	
	$("#projectSearchBoxId").keydown(function(e){
		performProjectSearch();
	});
	
	$("#projectSearchBoxId").keyup(function(e){
		//$("input[type='search']").val($("#projectSearchBoxId").val());
		//$("input[type='search']").trigger(e);
		performProjectSearch();
	});

	$.datetimepicker.setLocale('en');
	$('.estStartTime').datetimepicker({value:getTodayDate(new Date()) + " 08:00",
	onGenerate:function( ct ){
		$(this).find('.xdsoft_date.xdsoft_weekend')
		.addClass('xdsoft_disabled');
	},
	allowTimes:['08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00']
	
	});
	
	
	$('.estCompleteTime').datetimepicker({value:getTodayDate(new Date()) + " 18:00",
	onGenerate:function( ct ){
		$(this).find('.xdsoft_date.xdsoft_weekend')
		.addClass('xdsoft_disabled');
	},
	allowTimes:['08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00']
	
	});
	
	$('.dateTimePicker').datetimepicker({value:new Date(),
	onGenerate:function( ct ){
		$(this).find('.xdsoft_date.xdsoft_weekend')
		.addClass('xdsoft_disabled');
	},
	allowTimes:['08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00']
	
	});
	
	
	function resetAccount(){
		$('.accountOption').each(function () { $(this).hide(); });
		$("#accountId").prop("selectedIndex",0);
		$('#accountId').selectpicker('refresh');
	}
	
	function resetTeam(){
		$('.teamOption').each(function () { $(this).hide(); });
		$("#teamId").prop("selectedIndex",0);
		$('#teamId').selectpicker('refresh');
	}
	
	function resetClient(){
		$('.clientOption').each(function () { $(this).hide(); });
		$("#clientId").prop("selectedIndex",0);
		$('#clientId').selectpicker('refresh');
	}
	
	var clickBillableType = function(){
		resetAccount();
		resetTeam();
		resetClient();
		$('.accountOption[filter^=BILLABLE]').each(function () { $(this).show(); });
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		$('#clientId').selectpicker('refresh');
		$("#projectType").val("BILLABLE");
	};
	
	var clickNonBillableType =  function(){
		resetAccount();
		resetTeam();
		resetClient();
		$("#projectType").val("NON_BILLABLE");
		$('.accountOption[filter^=NONBILLABLE]').each(function () { $(this).show(); });
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		$('#clientId').selectpicker('refresh');
	};
	
	$("#accountId").on("change",function(){
		console.log("account id change:" + $(this).val());
		resetTeam();
		resetClient();
		$('.teamOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
		$('#teamId').selectpicker('refresh');
		$('#clientId').selectpicker('refresh');
	});
	
	$("#teamId").on("change",function(){
		console.log("team id change:" + $(this).val());
		resetClient();
		$('.clientOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
		$('#clientId').selectpicker('refresh');
		var PONumber = $( "#teamId option:selected" ).attr("poid");
		$("#PONumber").val(PONumber);
		$("#POidSpan").html(PONumber);
		console.log("PONumber" + PONumber);
	});
	
	$("#clientId").on("change",function(){
		var subTeam = $(this).children(":selected").attr("subteam");
		var account = $(this).children(":selected").attr("account");
		console.log("account" + account);
		console.log("subTeam" + subTeam);
		$('#accountId').prop("selectedIndex",$("#"+account).prop("index"));
		$('#teamId').prop("selectedIndex",$("#"+subTeam).prop("index"));
		
		$('#billable').parent().removeClass("active");
		$('#nonbillable').parent().removeClass("active");
		
		if ($(this).children(":selected").attr("accounttype") == 'BILLABLE') {
			$('#billable').attr("checked", true);
			$('#billable').parent().addClass("active");
			$("#projectType").val("BILLABLE");
		} else {
			$('#nonbillable').attr("checked", true);
			$('#nonbillable').parent().addClass("active");
			$("#projectType").val("NON_BILLABLE");
		}
		
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		
		var PONumber = $( "#teamId option:selected" ).attr("poid");
		$("#PONumber").val(PONumber);
		$("#POidSpan").html(PONumber);
		console.log("PONumber" + PONumber);
		
	});
	
	
	var tags = new Bloodhound({
	    datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.tag); },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    local: [
		<c:if test="${not empty userMap}">
		 <c:forEach var="user" items="${userMap}" varStatus="userIndex"> 
	 		<c:if test="${user.role != 'SUPERADMIN'}"> 
	 		{ tag: '${user.email}' },
	 		</c:if>
 		</c:forEach>
 		</c:if>
	    ]
	});

	tags.initialize();
	$('.tagsinputWatchers').tagInput({
		  tagDataSeparator: ',',
		  allowDuplicates: false,
		  typeahead: true,
		  typeaheadOptions: {
		      highlight: true
		  },
		  typeaheadDatasetOptions: {
		    displayKey: 'tag',
		    source: tags.ttAdapter()
		  }
		});
	
	
	$('#estStartTime').on("change", function(){
		validateEstimatedStartTime();
		validateEstimatedEndTime();
		validateProjectDuration();
	});
	
	$('#estCompleteTime').on("change", function(){
		validateEstimatedStartTime();
		validateEstimatedEndTime();
		validateProjectDuration();
	});
	
	$('.estStartTime').on("change", function(){
		validateEstimatedStartTime();
		validateEstimatedEndTime();
		validateProjectDuration();
	});
	
	$('.estCompleteTime').on("change", function(){
		validateEstimatedStartTime();
		validateEstimatedEndTime();
		validateProjectDuration();
	});
	
	
	$('#duration').on("change", function(){
		validateEstimatedStartTime();
		validateEstimatedEndTime();
		validateProjectDuration();
	});
	
	function validateEstimatedStartTime(){
		console.log("validating project validateEstimatedStartTime");
		var estimatedTimeVal = $('#estStartTime').val();
		if (estimatedTimeVal != "") {
			var estimatedStartTimeDate = new Date(estimatedTimeVal);
			if(estimatedStartTimeDate < new Date()) {
				$("#estStartTime").css("border", "1px solid red");
				return false;
			} else {
				$("#estStartTime").css("border", "1px solid #D5D5D5");
			}
		}
		return true;
	}
	
	function validateEstimatedEndTime(){
		console.log("validating project validateEstimatedEndTime");
		var estCompleteTimeVal = $('#estCompleteTime').val();
		if (estCompleteTimeVal != "") {
			var estCompleteTimeDate = new Date(estCompleteTimeVal);
			var estimatedStartTimeDate = new Date($("#estStartTime").val());
			if(estCompleteTimeDate < estimatedStartTimeDate) {
				$("#estCompleteTime").css("border", "1px solid red");
				return false;
			} else {
				$("#estCompleteTime").css("border", "1px solid #D5D5D5");
			}
		}
		return true;
	}
	
	function validateProjectDuration(){
		console.log("validating project duration");
		var estCompleteTimeVal = $('#estCompleteTime').val();
		if (estCompleteTimeVal != "") {
			var estCompleteTimeDate = new Date(estCompleteTimeVal);
			var estimatedStartTimeDate = new Date($("#estStartTime").val());
			
			var diffTime = (estCompleteTimeDate - estimatedStartTimeDate) /(60*60*1000);
			var projectDuration =  $('#duration').val();
			
			if(diffTime < projectDuration) {
				$("#duration").closest('div').find('button').css("border", "1px solid red");
				return false;
			} else {
				$("#duration").closest('div').find('button').css("border", "1px solid #D5D5D5");
			}
		}
		return true;
	}
	
	$(document).ready(function() {
	    $('.projectDatatable').DataTable({ 
	    	responsive: true,
	        "lengthMenu": [[50, 100, 200, -1], [50, 100, 200, "All"]],
	        "ordering": true,
	        "order": [[ 1, 'asc' ]]
	    
	    });
	});
	

	$(document).ready(function(){
		/* MESSAGE BOX */
		$(document).on("click",".deleteProject",function(){
		    var box = $($(this).data("box"));
		    if(box.length > 0){
		        box.toggleClass("open");
		        
		        var sound = box.data("sound");
		        
		        if(sound === 'alert')
		            playAudio('alert');
		        
		        if(sound === 'fail')
		            playAudio('fail');
		        
		    }        
		    return false;
		});
		$(document).on("click",".mb-control-close",function(){
		   $(this).parents(".message-box").removeClass("open");
		   return false;
		});    
		/* END MESSAGE BOX */
	});
	
	
var deleteCloneTaskItem = function(taskId) {
	$("#"+taskId).remove();
}

$(document).on('change','.taskCloneAssignee',function(){
	var assigneeId = $(this).val();
	var projectId = $(this).attr("data-projectId");
	var projectStartTime = $("#prjStartTime" + projectId).val();
	var projectEndTime = $("#prjEndTime" + projectId).val();
	var thisVar = $(this);
	var dataString = {"assigneeId" : assigneeId, "startDateTime":projectStartTime, "endDateTime":projectEndTime};
	
	doAjaxRequestWithGlobal("GET", "${applicationHome}/getAssigneesSlot", dataString, function(data) {
        console.log(data);
        if (data == "Not Available") {
        	 $(thisVar).nextAll('.availabilityStatus:first').html(data);
        	 $(thisVar).nextAll('.availabilityStatus:first').css("color", "red");
        } else {
        	$(thisVar).nextAll('.availabilityStatus:first').html("Available from " + data);
        	 $(thisVar).nextAll('.availabilityStatus:first').css("color", "green");
        }
    }, function(e) {
        console.log("ERROR: ", e);
        alert(e);
    }, false);
});

var createNewAssignSection = function(projectId) {
		var taskId = new Date().getTime();
		var taskDetailsHidden = $(".taskDetailsHidden").clone();
		$(taskDetailsHidden).attr("id", "taskId"+taskId);
		$(taskDetailsHidden).find(".deleteCloneTaskLink").attr("onclick",'deleteCloneTaskItem("taskId'+taskId+'")');
		$(taskDetailsHidden).find(".taskCloneAssignee").attr("data-projectId", projectId);
		
		$(taskDetailsHidden).removeClass("taskDetailsHidden");
		$(taskDetailsHidden).addClass("taskDetails");
		$(taskDetailsHidden).removeClass("hide"); 
		
		$("#projectTaskCloneDIv"+projectId).append(taskDetailsHidden);
	}
</script>