<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@include file="includes/header.jsp"%>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="prjectCategoryEnabled" value="${applicationScope.applicationConfiguraion.prjectCategoryEnabled}"/>
<c:set var="userSkillHoursMappingEnabled" value="${applicationScope.applicationConfiguraion.userSkillHoursMappingEnabled}"/>

 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>                    
     <li class="active">Projects</li>
 </ul>
 <!-- END BREADCRUMB -->       
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/cron/jquery-cron.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/gentleSelect/jquery-gentleSelect.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/checkbox/styledCheckbox.css"/>

<script type="text/javascript">
var assigneeMandatoryForQuickCloning = ${applicationScope.applicationConfiguraion.assignManForQckClone};
var recurringOriginal = ${applicationScope.applicationConfiguraion.recurringOriginal};
var userSkillHoursMappingEnabled = ${userSkillHoursMappingEnabled};
</script>
<style>
::-webkit-scrollbar {
    width: 7px;
    height: 7px;
}
.userSkillSplitFooter, .monthlySplitTableFooter {
    background-color: #ccc;
    font-size: 14px;
}
.form-group.required .control-label:after {
  content:"*";
  color:red;
}
@media screen and (min-width: 700px) {
.custom-control-label {
    padding-top: 7px;
    margin-bottom: 0;
    text-align: right;
}
	#createprojectmodal .modal-dialog {
		width: 80%;
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
    width: 500px;
    border-radius: 10px;
    box-shadow: 5px 5px 2px #888888;
}
	
#x {
    position: relative;
    float: right;
    top: -5px;
    right: 0px;
    border-radius: 25px;
    font-size: 16pt;
    font-weight: bold;
    color: gray;
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
.archived{
	background-color: #FEF8F8;
}
.durationTxt{
    font-weight: bold;
    width: 22px;
    margin-right: 1px;
    height: 22px;
    line-height: 18px;
    box-shadow: none;
    -webkit-appearance: none;
    border: 1px solid #D5D5D5;
    background: #F9F9F9;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    border-radius: 4px;
    color: #555;
    font-size: 9px;
    padding: 0px 2px 2px 2px;
}

.gentleinputstyle {
    padding: 1px 5px 1px;
    background-color: #eee;
    border: 2px solid #ddd;
    -moz-border-radius: 5px;
    -webkit-border-radius: 5px;
    border-radius: 5px;
    background-repeat: no-repeat;
    background-position: center right;
    cursor: pointer;
    color: #555;
    white-space: nowrap;
}

.radioButton:hover, .radioButton:focus, .radioButton:active, .radioButton.active {
    background-color: #c6c3c3;
    border-color: #DDD;
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
							<div class="col-md-4">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="projectSearchBoxId"
										placeholder="Search for a project.." />
									<div class="input-group-btn">
										<button type="button" class="hide btn btn-primary projectSearchBtn"  onclick="searchAllProjectDetails($('#projectSearchBoxId').val());">Search</button>
									</div>
								</div>
							</div>
								<div class="col-md-1">
        							<p style="text-align:center;margin :0 0 0px;">Show Archived</p>
									<p style="text-align: center;margin :0 0 0px">
									<div class="slideThree">	
										<input type="checkbox" class="styledCheckBox includeArchive" value="None" id="slideThree" name="check" />
										<label for="slideThree"></label>
									</div>
																		<!-- <input id="includeArchive" type="checkbox" class=""/> -->
									
									</p>
							</div>
			                <div class="col-md-3" >
					             	 <span id="reportrange" class="dtrange dateFilterDiv">                                            
            							<span>${dateRange}</span><b class="caret"></b>
        							</span>
        								<input style="margin-left:10px" class="btn btn-default dateFilterDiv" type="button" value="Filter" onclick="loadAllProjectDetails($('#daterangeText').val());"></input>
        								<input type="hidden" id="daterangeText" value="hello" /> 
        					</div>
        					<div class="col-md-2" >
        						<c:if test="${currentUser.userRole == 'SUPERADMIN'}">
       							   <select class="form-control" data-live-search="true" id="userGroupSelection">
						               <option value="">- select -</option>
						               <c:forEach var="userGroup" items="${userGroupMap}" varStatus="userGroupIndex"> 
						                <option <c:if test="${currentUserGroupId == userGroup.value.userGroupId}">selected="selected"</c:if> value="${userGroup.value.userGroupId}">${userGroup.value.name}</option>
						               </c:forEach>
						           </select>
       							</c:if>
        					</div>
							<div class="col-md-2">
								  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
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
	 <div class="row" id="projectDashBoardData">
	 	Loading data....
	 </div>
</div>
<!-- END CONTENT FRAME -->


<!-- project create modal start -->
<div class="modal fade" id="createprojectmodal" tabindex="-1"
	role="dialog" aria-labelledby="createprojectmodal" aria-hidden="true" data-backdrop="static">
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
				<div class="modal-body" style="padding: 10px 10px 10px 5px">
					<c:if test="${userSkillHoursMappingEnabled}">
						<%@include file="fagments/newCreateProjectModal.jspf"%>
					</c:if>
					<c:if test="${!userSkillHoursMappingEnabled}">
						<%@include file="fagments/createProjectModal.jspf"%>
					</c:if>
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


<!-- recurring project create modal start -->
<div class="modal fade" id="recurringprojectmodal" tabindex="-1"
	role="dialog" aria-labelledby="recurringprojectmodal" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Recurring Project Settings</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/recurringProjectModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="recurringProjectActionButton" onclick=""
						class="btn btn-primary">
						<span id="RPABCreatOrUpdate">Create</span>
					</button>
				</div>
			</div>
		</div>
</div>
<!-- recurring project create modal end -->


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
			 <input type="hidden" class="availabilityTime"></input>
		</div>
		<div class="col-md-2 col-xs-12">
			<input type="text" class="form-control cloneInput tskDuration"
				value="1"/>
				
		</div>
		<div class="col-md-2 col-xs-12">
			<select class="form-control cloneInput tskType">
				<option value="PRODUCTIVE"  selected="selected">P</option>
				<option value="REVIEW">R</option>
				<option value="ITERATION">I</option>
			</select>
		</div>
		<div class="col-md-1 col-xs-12">
			<a href="javascript:void(0)" class="deleteCloneTaskLink"  onclick="deleteCloneTaskItem('')"><span class="fa fa-times" style="color: red"></span></a>
		</div>
	</div>
</div>

<%@include file="includes/footer.jsp"%>

 <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min.js?v=${fsVersionNumber}"></script> 
<script type='text/javascript'
	src='${js}/plugins/jquery-validation/jquery.validate.min.js'></script>
<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type="text/javascript"
	src="${js}/plugins/tagsinput/jquery.tagsinput.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript"
	src="${js}/plugins/fileinput/fileinput.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript"
	src="${js}/plugins/autocomplete/jquery.autocomplete.js?v=${fsVersionNumber}"></script>
 <script type="text/javascript" src="${js}/plugins/typeahead/typeahead.bundle.js?v=${fsVersionNumber}"></script>
 <script type="text/javascript" src="${js}/plugins/tagsinput/mab-jquery-taginput.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/gentleSelect/jquery-gentleSelect-min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/cron/jquery-cron.js?v=${fsVersionNumber}"></script>
<script>

$(function() {
    $('input[name="daterange"]').daterangepicker();
   
});
/* 
var createTaskDurationList = function(duration){
	 $("#duration").html("");
	for (var index = 1; index <= duration; index++) {
	     $("#duration").append('<option value="'+index+'">'+index+'</option>');
	 }
} */

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
	}
	
	<c:if test="${prjectCategoryEnabled}">
	,
	category: {
	   	 required: true
	}
	,
	newCategory: {
	   	 required: true
	}
	</c:if>	
 }
});


var clearProjectFormForCreate = function(projectId) {
	$("#createProjectFormId").prop("action","createProject");
	$("#myModalLabel").html("Create a Project");
	$("#projectActionButton span").html("Save");
	clearCreateProjectForm();
	$("#projectcode").val("PRJ" + Math.floor(Date.now() / 1000));
	//$("#PONumber").val("PO" + Math.floor(Date.now() / 1000));
	$('#estStartTime').val(getTodayDate(new Date()) + " 08:00");
	$('#estCompleteTime').val(getTodayDate(new Date()) + " 18:00");
	if(userSkillHoursMappingEnabled) {
		initializeCreateFormModel();
	}
	
}
var isProjectUpdate = false;
var isProjectDuplicate = false;

var loadProjectForUpdate = function(projectId) {
	$("#createProjectFormId").prop("action","updateProject");
	$("#myModalLabel").html("Update a Project");
	$("#projectActionButton span").html("Update");
	isProjectUpdate = true;
	isProjectDuplicate = false;
	loadProject(projectId);
}

var loadProjectForClone = function(projectId) {
	$("#createProjectFormId").prop("action","createProject");
	$("#myModalLabel").html("Duplicate a Project");
	$("#projectActionButton span").html("Duplicate");
	isProjectUpdate = false;
	isProjectDuplicate = true;
	loadProject(projectId);
	
	$('#estStartTime').val(getTodayDate(new Date()) + " 08:00");
	$('#estCompleteTime').val(getTodayDate(new Date()) + " 18:00");
}

var loadProject = function(projectId){
	var dataString = {"projectId" : projectId};
	doAjaxRequest("GET", "${applicationHome}/getProjectDetailsJson", dataString, function(data) {
        famstacklog("SUCCESS: ", data);
        var responseJson = JSON.parse(data);
        clearCreateProjectForm();
        initializeCreateProjectForm(responseJson);
    }, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    });
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
	
	//$("#duration").prop("selectedIndex",0);
	//$('#duration').selectpicker('refresh');
	$('#duration').val("");
	
	$("#adhocSubType").attr("checked", false);
	$("#fteSubType").attr("checked", false);
	$("#projectSubType").val("");
	$('#adhocSubType').parent().removeClass("active");
	$('#fteSubType').parent().removeClass("active");
	
	$("#projectLead").prop("selectedIndex",0);
	$('#projectLead').selectpicker('refresh');
	$("#clientPartner").val("");
	
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
	
	$("#newCategory").prop("selectedIndex",0);
	$('#newCategory').selectpicker('refresh');
	$("#sowLineItem").val("");
	
	$("#proposalNo").val("");
	$("#orderBookRefNo").val("");
	$("#projectLocation").prop("selectedIndex",0);
	$('#projectLocation').selectpicker('refresh');
	
	$("#watchers").val("");
	
	$("#tags").val("");
	
	$(".tagsinputWatchers span[data-tag]").each(function(){
		$(this).remove();
	});
	
	$(".tagsinput span.tag").each(function(){
		$(this).remove();
	});
	
	$("#hoursUserSkillMonthlySplit").val("");
	$("#fillMonthlySplitDataTotalHours").val("");
	
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
	
	if (project.projectSubType == 'ADHOC') {
		$("#adhocSubType").attr("checked", true);
		$('#adhocSubType').parent().addClass("active");
	} else {
		$("#fteSubType").attr("checked", true);
		$('#fteSubType').parent().addClass("active");
	}
	$("#projectSubType").val(project.projectSubType);
	
	$("#projectLead").val(project.projectLead);
	$('#projectLead').selectpicker('refresh');
	
	$("#clientPartner").val(project.clientPartner);
	
	$("#priority").val(project.priority);
	$('#priority').selectpicker('refresh');
	
	$("#duration").val(project.duration);
	//$('#duration').selectpicker('refresh');
	
	$("#complexity").val(project.complexity);
	$('#complexity').selectpicker('refresh');
	
	$("#projectId").val(project.id);
	
	$("#quantity").val(project.quantity);
	
	$("#PONumber").val(project.PONumber);
	$("#POidSpan").html(project.PONumber);
	
	$('#estStartTime').val(project.startTime);
	$('#estCompleteTime').val(project.completionTime);

	$("#category").val(project.category);
	$('#category').selectpicker('refresh');
	
	
	$("#newCategory").val(project.newCategory);
	$('#newCategory').selectpicker('refresh');
	$("#sowLineItem").val(project.sowLineItem);
	
	$("#proposalNo").val(project.proposalNo);
	$("#orderBookRefNo").val(project.orderBookRefNo);
	
	$("#projectLocation").val(project.projectLocation);
	$('#projectLocation').selectpicker('refresh');
	
	$('.clientOption').each(function () { $(this).show(); });
	$('#clientId').val(project.clientId);
	$('#clientId').selectpicker('refresh');
	$('#clientId').change();
	
	$("#watchers").val(project.watchers);
	
	$("#projectcode").val(project.code);
	
	if (project.watchers != null && project.watchers != "") {
		var watchersArray = project.watchers.split(",");
		famstacklog(watchersArray);
		for (var index=0; index < watchersArray.length; index++) {
			$(".tagsinputWatchers").prepend('<span class="label label-primary" data-tag="'+watchersArray[index]+'">'+watchersArray[index]+'<span class="glyphicon glyphicon-remove"></span></span>');
		}
		$(".twitter-typeahead input.mab-jquery-taginput-input").attr("placeholder","");
	}
	
	if (project.tags != null && project.tags != "") {
		var tagsArray = project.tags.split(",");
		for (var index=0; index < tagsArray.length; index++) {
			$(".tagsinput").addTag(tagsArray[index]);
		}
	}
	

    if(userSkillHoursMappingEnabled) {
    	fillUserSkillSplitData();
    	$(".monthlySplitGrandTotal").html("");
    	fillMonthlySplitData(project.hoursUserSkillMonthlySplitJson);  
    	$("#hoursUserSkillMonthlySplit").val(project.hoursUserSkillMonthlySplitJson);
    	reInitialilzeMonthlySplitTable = false;
    }
}
	
	$( document ).ready(function(){
		$("input[type='search']").parent().hide();
	});
	
	$('.autocomplete').autocomplete(
		{
			serviceUrl: '${applicationHome}/getProjectJson',
			onSelect : function(suggestion) {
				if(!isProjectUpdate && !isProjectDuplicate) {	
					//loadProjectForClone(suggestion.data);
				}
			}
		});

	function doAjaxCreateProjectForm() {
		if (validateEstimatedEndTime() && validateProjectType()) {
			$('#createProjectFormId').submit();
		}
	}
	
	function validateProjectType() {
		$(".projectTypeDiv").css("border", "0px solid red");
		if($("#projectType").val() == "") {
			$(".projectTypeDiv").css("border", "1px solid red");
			return false;
		}
		return true;
	}

	$('#createProjectFormId').ajaxForm(function(response) {
		famstacklog(response);
		var responseJson = JSON.parse(response);
		if (responseJson.status) {
			showNotification($("#projectName").val(),$("#summary").val(),"${applicationHome}/project/"+responseJson.projectId);
			refreshProjectDetails();
			$('#createprojectmodal').modal('hide');
			isProjectUpdate = false;
			isProjectDuplicate = false;
		}
	});

	
	function deleteProjects(){
		$(".msgConfirmText").html("Delete the selected projects");
		$(".msgConfirmText1").html("");
		$("#confirmYesId").prop("href","javascript:doAjaxArchiveDeleteProjects('hard')");
	}
	
	function archiveProjects(){
		$(".msgConfirmText").html("Archive the selected projects");
		$(".msgConfirmText1").html("");
		$("#confirmYesId").prop("href","javascript:doAjaxArchiveDeleteProjects('soft')");
	}
	
	function undoArchiveProject(){
		$(".msgConfirmText").html("Undo Archived selected projects");
		$(".msgConfirmText1").html("");
		$("#confirmYesId").prop("href","javascript:doAjaxArchiveDeleteProjects('undoarchived')");
	}
	
	function deleteProject(projectId, projectName){
		$(".msgConfirmText").html("Delete project");
		$(".msgConfirmText1").html(projectName);
		$("#confirmYesId").prop("href","javascript:doAjaxDeleteProject('"+projectId+"','deleteProject')");
	}
	
	function archiveProject(projectId, projectName){
		$(".msgConfirmText").html("Archive project");
		$(".msgConfirmText1").html(projectName);
		$("#confirmYesId").prop("href","javascript:doAjaxArchiveProject('"+projectId+"','archiveProject')");
	}
	
	$(document).on("change",".prjectDeleteArchive", function(){
		if ($("input.prjectDeleteArchive:checked").length > 0) {
			$(".deleteProjectDropDown").removeClass("hide");
			$(".deleteProjectDropDownLink").removeClass("hide");
			if ($("input.prjectDeleteArchive.archivedProject:checked").length > 0) {
				$(".undoArchiveProjectDropDownLink").removeClass("hide");
			} else {
				$(".undoArchiveProjectDropDownLink").addClass("hide");
			}
			if ($("input.prjectDeleteArchive.archivedProject:checked").length < $("input.prjectDeleteArchive:checked").length){
				$(".archiveProjectDropDownLink").removeClass("hide");
			} else {
				$(".archiveProjectDropDownLink").addClass("hide");
			}
		} else {
			$(".deleteProjectDropDown").addClass("hide");
			$(".deleteProjectDropDownLink").addClass("hide");
			$(".archiveProjectDropDownLink").addClass("hide");
			$(".undoArchiveProjectDropDownLink").addClass("hide");
		}
	});

	
	function getAllSelectedProjectIds(){
		var projectIds= new Array();
		var index = 0;
		$("input.prjectDeleteArchive:checked").each(function(){
			var projectId = $(this).attr("data-projectId");
			projectIds[index++] = projectId;
		});
		return projectIds;
	}
	
	
	function doAjaxArchiveDeleteProjects(type) {
		var projectIds = getAllSelectedProjectIds();
		if (projectIds.length == 0) {
			 $(".message-box").removeClass("open");
			return;
		}
		var dataString = {"projectIds" : projectIds,type:type};
		doAjaxRequest("POST", "${applicationHome}/deleteProjects", dataString, function(data) {
			$.each(projectIds,function(idx, projectId){
				if ("soft" == type && $("input.includeArchive").is(":checked")) {
					$("#projectData"+projectId).addClass("archived");
					$("#projectData"+projectId).append('<span class="hide archivedProject"'+projectId+'">Archived</span>');
					$(".prjectDeleteArchive"+projectId).addClass("archivedProject");
				} else if ("hard" == type) {
					$("#projectData"+projectId).remove();
	             	$(".projectData"+projectId).remove();
				} else if ("undoarchived" == type) {
					$("#projectData"+projectId).removeClass("archived");
					$(".archivedProject"+projectId).remove();
					$(".prjectDeleteArchive"+projectId).removeClass("archivedProject");
				}
				$("input.prjectDeleteArchive:checked").attr("checked", false);
			});
			$(".message-box").removeClass("open");
			
		}, function(e) {
            famstacklog("ERROR: ", e);
            famstackalert(e);
        });
	}
	
	function doAjaxArchiveDeleteProject(projectId, type) {
		var dataString = {"projectId" : projectId};
		doAjaxRequest("POST", "${applicationHome}/"+type, dataString, function(data) {
            famstacklog("SUCCESS: ", data);
            var responseJson = JSON.parse(data);
            if (responseJson.status){
               $("#projectData"+projectId).remove();
               $(".projectData"+projectId).remove();
               $(".message-box").removeClass("open");
            }
        }, function(e) {
            famstacklog("ERROR: ", e);
            famstackalert(e);
        });
	}
	
	function performProjectSearch(){
		var searchText = $('#projectSearchBoxId').val();
		famstacklog(searchText);
		if (searchText != "") {
			$('.projectData').hide();
			$('.projectDataHidden').hide();
			//$('.projectSearchBtn').removeClass('hide');
			//$(".dateFilterDiv").addClass('hide');
		    $('.projectData').each(function(){
		       if($(this).text().toUpperCase().indexOf(searchText.toUpperCase()) != -1){
		           $(this).show();
		          // $(".dateFilterDiv").removeClass('hide');
		          // $('.projectSearchBtn').addClass('hide');
		       }
		    });
		    
		    if (searchText.length > 2) {
		    	$(".projectSearchBtn").removeClass('hide');
		    }
		} else {
			//$(".dateFilterDiv").removeClass('hide');
			$('.projectSearchBtn').addClass('hide');
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
	
	$('.dateTimePicker').datetimepicker({value:new Date(),
	onGenerate:function( ct ){
		/* $(this).find('.xdsoft_date.xdsoft_weekend')
		.addClass('xdsoft_disabled'); */
	},
	allowTimes:['05:00','06:00','07:00','08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00']
	
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
		//resetAccount();
		//resetTeam();
		//resetClient();
		//$('.accountOption[filter^=BILLABLE]').each(function () { $(this).show(); });
		//$('#accountId').selectpicker('refresh');
		//$('#teamId').selectpicker('refresh');
		//$('#clientId').selectpicker('refresh');
		$("#projectType").val("BILLABLE");
	};
	
	var clickNonBillableType =  function(){
		//resetAccount();
		//resetTeam();
		//resetClient();
		$("#projectType").val("NON_BILLABLE");
		//$('.accountOption[filter^=NONBILLABLE]').each(function () { $(this).show(); });
		//$('#accountId').selectpicker('refresh');
		//$('#teamId').selectpicker('refresh');
		//$('#clientId').selectpicker('refresh');
	};
	
	
	var clickAdHocType = function(){
		$("#projectSubType").val("ADHOC");
	};
	
	var clickFTEType = function(){
		$("#projectSubType").val("FTE");
	};
	
	$("#accountId").on("change",function(){
		famstacklog("account id change:" + $(this).val());
		resetTeam();
		resetClient();
		$('.teamOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
		$('#teamId').selectpicker('refresh');
		$('#clientId').selectpicker('refresh');
	});
	
	$("#teamId").on("change",function(){
		famstacklog("team id change:" + $(this).val());
		resetClient();
		$('.clientOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
		$('#clientId').selectpicker('refresh');
		var PONumber = $( "#teamId option:selected" ).attr("poid");
		if (PONumber != "") {
			$("#PONumber").val(PONumber);
		}
		$("#POidSpan").html(PONumber);
		famstacklog("PONumber" + PONumber);
	});
	
	$("#clientId").on("change",function(){
		var subTeam = $(this).children(":selected").attr("subteam");
		var account = $(this).children(":selected").attr("account");
		famstacklog("account" + account);
		famstacklog("subTeam" + subTeam);
		$('#accountId').prop("selectedIndex",$("#"+account).prop("index"));
		$('#teamId').prop("selectedIndex",$("#"+subTeam).prop("index"));
		
		if ($("#projectType").val() == "") {
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
		}
		
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		
		var PONumber = $( "#teamId option:selected" ).attr("poid");
		if (PONumber != "") {
			$("#PONumber").val(PONumber);
		}
		$("#POidSpan").html(PONumber);
		famstacklog("PONumber" + PONumber);
		
	});
	
	
	var tags = new Bloodhound({
	    datumTokenizer: function(d) { return Bloodhound.tokenizers.whitespace(d.tag); },
	    queryTokenizer: Bloodhound.tokenizers.whitespace,
	    local: [
		<c:if test="${not empty userMap}">
		 <c:forEach var="user" items="${userMap}" varStatus="userIndex"> 
	 		{ tag: '${user.email}' },
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
		validateEstimatedStartTime($('#estStartTime'));
		validateEstimatedEndTime($('#estStartTime'),$('#estCompleteTime'));
		//validateProjectDuration($('#estStartTime'), $('#estCompleteTime'), $('#duration'));
		reInitialilzeMonthlySplitTable = true;
	});
	
	$('#estCompleteTime').on("change", function(){
		validateEstimatedStartTime($('#estStartTime'));
		validateEstimatedEndTime($('#estStartTime'),$('#estCompleteTime'));
		//validateProjectDuration($('#estStartTime'), $('#estCompleteTime'), $('#duration'));
		reInitialilzeMonthlySplitTable = true;
	});
	
	/* $('#duration').on("change", function(){
		validateEstimatedStartTime($('#estStartTime'));
		validateEstimatedEndTime($('#estStartTime'),$('#estCompleteTime'));
		validateProjectDuration($('#estStartTime'), $('#estCompleteTime'), $('#duration'));
	});
 */
	
	$(document).on("change",".estStartTime", function(){
		validateStartAndEndTime($(this).attr("data-projectId"));
		});
	
		function validateStartAndEndTime(projectId){
			var isValidStart = true;
			var isValidEnd = true;
			var isValidDuration = true;
			var estStartTime = $('#prjStartTime'+projectId);
			var estCompleteTime = $('#prjEndTime'+projectId);
			var duration = $('#prjDuration'+projectId);
			//isValidStart = validateEstimatedStartTime(estStartTime);
			isValidEnd = validateEstimatedEndTime(estStartTime, estCompleteTime);
			//isValidDuration = validateProjectDuration(estStartTime, estCompleteTime, duration);
			//if (isValidEnd && isValidDuration) {
				loadInitialTaskAvailabilityTime(projectId);
			//}
		}
		
		$(document).on("change",".estCompleteTime", function(){
			validateStartAndEndTime($(this).attr("data-projectId"));
		});
		
		/* $(document).on("change",".duration", function(){
			validateStartAndEndTime($(this).attr("data-projectId"));
		}); */
	
	
	
	function validateEstimatedStartTime(estStartTime){
		famstacklog("validating project validateEstimatedStartTime");
		var estimatedTimeVal = $(estStartTime).val();
		if (estimatedTimeVal != "") {
			var estimatedStartTimeDate = new Date(estimatedTimeVal);
			if(estimatedStartTimeDate < new Date()) {
				$(estStartTime).css("border", "1px solid red");
				return false;
			} else {
				$(estStartTime).css("border", "1px solid #D5D5D5");
			}
		}
		return true;
	}
	
	function validateEstimatedEndTime(estStartTime, estCompleteTime){
		famstacklog("validating project validateEstimatedEndTime");
		var estCompleteTimeVal = $(estCompleteTime).val();
		if (estCompleteTimeVal != "") {
			var estCompleteTimeDate = new Date(estCompleteTimeVal);
			var estimatedStartTimeDate = new Date($(estStartTime).val());
			if(estCompleteTimeDate < estimatedStartTimeDate) {
				$(estCompleteTime).css("border", "1px solid red");
				return false;
			} else {
				$(estCompleteTime).css("border", "1px solid #D5D5D5");
			}
		}
		return true;
	}
	
	function validateProjectDuration(estStartTime, estCompleteTime, duration){
		famstacklog("validating project duration");
		var estCompleteTimeVal = $(estCompleteTime).val();
		if (estCompleteTimeVal != "") {
			var estCompleteTimeDate = new Date(estCompleteTimeVal);
			var estimatedStartTimeDate = new Date($(estStartTime).val());
			
			var diffTime = (estCompleteTimeDate - estimatedStartTimeDate) /(60*60*1000);
			var projectDuration =  $(duration).val();
			
			if(diffTime < projectDuration) {
				$(duration).closest('div').find('button').css("border", "1px solid red");
				$(duration).css("border", "1px solid red");
				return false;
			} else {
				$(duration).closest('div').find('button').css("border", "1px solid #D5D5D5");
				$(duration).css("border", "1px solid #D5D5D5");
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


var taskLinkclick = function(projectId, e){
	e.preventDefault();
    $('#taskDetailsDiv').hide();
    
	var dataString = {"projectId" : projectId};
	doAjaxRequestWithGlobal("GET", "${applicationHome}/loadTaskDetailsJSon", dataString, function(data) {
        famstacklog("SUCCESS: ", data);
        $('#taskDetailsDiv').html(data);
        
        if (data.trim() != "") {
        	$('#taskDetailsDiv').css({'top':e.pageY+10,'left':e.pageX-500, 'position':'absolute', 'border':'1px solid gray', 'padding':'5px'});
        	$('#taskDetailsDiv').show();
        }
        initializePopOver();
    }, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    }, false);
}

var loadAllProjectDetails = function(daterange) {
	
	var dataString = {"daterange" : daterange, includeArchive:$("input.includeArchive").is(":checked")};
	doAjaxRequest("GET", "${applicationHome}/projectdashboardList", dataString, function(data) {
        fillProjectData(data);
       	performProjectSearch();
        	
    }, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        fillProjectData("Unable to find project details..");
    });
}

var searchAllProjectDetails = function(searchString) {
	
	var dataString = {"searchString" : searchString, includeArchive:$("input.includeArchive").is(":checked")};
	doAjaxRequest("GET", "${applicationHome}/searchProjectDetails", dataString, function(data) {
        fillProjectData(data);
    }, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    });
}

var fillProjectData = function(data){
	
	$("#projectDashBoardData").html(data);
    refreshRecurringSpin();
    $('.estStartTime').datetimepicker({value:getTodayDate(new Date()) + " 08:00",
    	onGenerate:function( ct ){
    		/* $(this).find('.xdsoft_date.xdsoft_weekend')
    		.addClass('xdsoft_disabled'); */
   	},
   	allowTimes:['05:00','06:00','07:00','08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00']
   	
   	});
   	
   	
   	$('.estCompleteTime').datetimepicker({value:getTodayDate(new Date()) + " 18:00",
   	onGenerate:function( ct ){
   		/* $(this).find('.xdsoft_date.xdsoft_weekend')
   		.addClass('xdsoft_disabled'); */
   	},
   	allowTimes:['05:00','06:00','07:00','08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00','22:00','23:00']
   	
   	});
   	
}

var loadDuplicateProjects = function(projectId, projectCode, isForce) {
	
	if ( $(".projectDuplicate"+projectId).length ==0 || isForce) {
		var dataString = {"projectId" : projectId, "projectCode" : projectCode, includeArchive:$("input.includeArchive").is(":checked")};
		doAjaxRequestWithGlobal("GET", "${applicationHome}/loadDuplicateProjectsJSon", dataString, function(data) {
	        $(".projectDuplicate"+projectId).remove();
	        $("#projectDetails"+projectId +" tbody").append(data);
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    }, false);
		
		dataString = {"projectId" : projectId};
		doAjaxRequestWithGlobal("GET", "${applicationHome}/projectTaskCloneJson", dataString, function(data) {
	        $("#projectTaskCloneDIv"+projectId).html(data);
	        if(!isForce) {
	        	loadInitialTaskAvailabilityTime(projectId);
	        }
	    }, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    }, false);
	}
	if(!isForce) {
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
}

var loadInitialTaskAvailabilityTime = function(projectId){
	$("#projectTaskCloneDIv"+projectId+" .taskDetails").each(function(){
		var assigneeId = $(this).find(".taskCloneAssignee").val();
		var taskId =$(this).attr("data-taskid");
		loadTaskAvailabilityTime(projectId, assigneeId,taskId);
	});
}

var loadTaskAvailabilityTime =function(projectId, assigneeId, taskId){
	if (assigneeId == "") {
		return;	
	}
	var projectStartTime = $("#prjStartTime" + projectId).val();
	var projectEndTime = $("#prjEndTime" + projectId).val();
	var dataString = {"assigneeId" : assigneeId, "startDateTime":projectStartTime, "endDateTime":projectEndTime};
	famstacklog("taskId" +  taskId);
	doAjaxRequestWithGlobal("GET", "${applicationHome}/getAssigneesSlot", dataString, function(data) {
        famstacklog(data);
        if (data == "Not Available") {
        	 $(".avaStatus"+taskId).html(data);
        	 $(".avaStatus"+taskId).css("color", "red");
        } else {
        	$(".avaStatus"+taskId).html("Available from " + data);
        	$(".avaInputStatus"+taskId).val(data);
        	$(".avaStatus"+taskId).css("color", "green");
        }
    }, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    }, false);

}

$(document).on('change','.taskCloneAssignee',function(){
	var assigneeId = $(this).val();
	var projectId = $(this).attr("data-projectId");
	var taskId =$(this).attr("data-taskid");
	loadTaskAvailabilityTime(projectId, assigneeId, taskId);
});

var createNewAssignSection = function(projectId) {
		var taskId = new Date().getTime();
		var taskDetailsHidden = $(".taskDetailsHidden").clone();
		$(taskDetailsHidden).attr("id", "taskId"+taskId);
		$(taskDetailsHidden).attr("data-taskid", taskId);
		$(taskDetailsHidden).find(".deleteCloneTaskLink").attr("onclick",'deleteCloneTaskItem("taskId'+taskId+'")');
		$(taskDetailsHidden).find(".taskCloneAssignee").attr("data-projectId", projectId);
		$(taskDetailsHidden).find(".taskCloneAssignee").attr("data-taskid", taskId);
		$(taskDetailsHidden).find(".tskName").val($("#projectCategory"+projectId).val());
		$(taskDetailsHidden).find(".availabilityStatus").addClass("avaStatus"+taskId);
		$(taskDetailsHidden).find(".availabilityTime").addClass("avaInputStatus" + taskId);
		
		$(taskDetailsHidden).removeClass("taskDetailsHidden");
		$(taskDetailsHidden).addClass("taskDetails");
		$(taskDetailsHidden).removeClass("hide"); 
		
		$("#projectTaskCloneDIv"+projectId).append(taskDetailsHidden);
	}
	

function createDuplicateProjectWithTask(projectId, projectCode) {
	$(".msgConfirmText").html("Do you really want to duplicate the project and tasks");
	$(".msgConfirmText1").html(projectCode);
	$("#confirmYesId").prop("href","javascript:createDuplicateProjectWithTaskAction('"+projectId+"','"+projectCode+"')");
}


var createDuplicateProjectWithTaskAction = function(projectId, projectCode) {
	
	var taskDetails = "";
	var projectName = $("#prjName"+projectId).val();
	var projectDuration = $("#prjDuration"+projectId).val();
	var projectStartTime = $("#prjStartTime"+projectId).val();
	var projectEndTime = $("#prjEndTime"+projectId).val();
	
	$("#prjName"+projectId).removeClass("error");
	$("#prjDuration"+projectId).removeClass("error");
	$("#prjStartTime"+projectId).removeClass("error");
	$("#prjEndTime"+projectId).removeClass("error");
	var isError = false;
	if (projectName.trim() == "") {
		$("#prjName"+projectId).addClass("error");
		isError = true;
	}
	 
	if (projectStartTime.trim() == "") {
		$("#prjStartTime"+projectId).addClass("error");
		isError = true;
	}
	if (projectEndTime.trim() == "") {
		$("#prjEndTime"+projectId).addClass("error");
		isError = true;
	}
	
	var estCompleteTimeDate = new Date(projectEndTime);
	var estimatedStartTimeDate = new Date(projectStartTime);
	if(estCompleteTimeDate < estimatedStartTimeDate){
		isError = true;
	}
	
	var taskDurationIntValue = 0;
	$("#projectTaskCloneDIv"+projectId+" .taskCloneAssignee").removeClass("error");
	$("#projectTaskCloneDIv"+projectId+" .tskDuration").removeClass("error");
	$("#projectTaskCloneDIv"+projectId+" .tskName").removeClass("error");
	
	$("#projectTaskCloneDIv"+projectId+" .taskDetails").each(function(){
		var taskCloneAssignee = $(this).find(".taskCloneAssignee").val();
		var tskName = $(this).find(".tskName").val();
		var availabilityTime = $(this).find(".availabilityTime").val();
		var tskDuration = $(this).find(".tskDuration").val();
		var tskType = $(this).find(".tskType").val();
		if (taskDetails != "") {
			taskDetails+="#TD#";
		}
		
		if (tskName == "") {
			$(this).find(".tskName").addClass("error");
			isError = true;
		}
		
		if (tskDuration.trim() == "" || parseInt(tskDuration) <1) {
			$(this).find(".tskDuration").addClass("error");
			isError = true;
		}
		if (availabilityTime == "" && assigneeMandatoryForQuickCloning) {
			$(this).find(".taskCloneAssignee").addClass("error");
			isError = true;
		} else {
			taskCloneAssignee = 0;
			availabilityTime= getTodayDate(new Date()) + " 08:00";
		}
		taskDurationIntValue+=parseInt(tskDuration);
		
		taskDetails += taskCloneAssignee+"#TDD#"+tskName+"#TDD#"+availabilityTime+"#TDD#"+tskDuration+"#TDD#"+tskType;
	});

	var projectDurationIntValue = taskDurationIntValue;
	if (projectDuration != "" && projectDuration != '0') {
		/* $("#prjDuration"+projectId).addClass("error");
		isError = true; */
		projectDurationIntValue = parseInt(projectDuration);
	} else {
		projectDuration = 0;
	}
	
	$("#projectDurationMsg"+projectId).html("");
	if (taskDurationIntValue > projectDurationIntValue) {
		$("#prjDuration"+projectId).addClass("error");
		$("#projectDurationMsg"+projectId).html("Invalid time assignment");
		isError=true;
	}
	
	if (taskDurationIntValue < projectDurationIntValue) {
		$("#prjDuration"+projectId).addClass("error");
		$("#projectDurationMsg"+projectId).html("Unassigned project time");
		isError=true;
	}
	
	if (isError){
		$(".message-box").removeClass("open");
		return;
	}
	
	var dataString ={"projectId":projectId,"projectName":projectName,"projectDuration":projectDuration,"projectStartTime":projectStartTime,"projectEndTime":projectEndTime,"taskDetails":taskDetails};
	
	doAjaxRequest("POST", "${applicationHome}/quickDuplicateProject", dataString, function(data) {
		famstacklog(data);
		  var responseJson = JSON.parse(data);
		if (responseJson.status){
			loadDuplicateProjects(projectId, projectCode, true);
		}
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    });
	$(".message-box").removeClass("open");
}


function refreshProjectDetails(){
	loadAllProjectDetails('${dateRange}');
}

/******************Recurring Project model************/
 function recurringProjectModel(projectCode, projectId) {
	var dataString = {"projectCode": projectCode, "projectId": projectId};
	doAjaxRequest("GET", "${applicationHome}/getRecurringProjectDetails", dataString, function(data) {
			famstacklog(data);
			if (data != ""){
				var responseJson = JSON.parse(data);
				loadRecurringProjectDetails(projectId, projectCode, responseJson);
			} else {
				$(".recurringTimeDiv").addClass("hide");
				$(".recurringDelete").addClass("hide");
				$(".recurringProjectDiv").addClass("hide");
				$(".recurringProjectName").html("");
				$(".recurringEndTime").val("");
				$("#RPABCreatOrUpdate").html("Create");
				initializeCron("0 5 0 * * ?");
				if ( recurringOriginal) {
					$(".recurreOriginal").prop("checked", "checked");
				} else {
					$(".recurreOriginal").removeAttr("checked");
				}
			}
			$("#recurringProjectActionButton").attr("onclick", "createRecurringProject('"+projectCode+"',"+projectId+")");
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}
 
function initializeCron(cronExpression) {
	$('#recurringCronExpressionDiv').html('<div id="recurringCronExpression"></div>');
	$('#recurringCronExpression').cron({
 	 initial: cronExpression,
      onChange: function() {
          $('#recurringCronExpressionValue').val($(this).cron("value"));
      },
      customValues: {
         // "5 Minutes" : "*/5 * * * *",
         // "2 Hours on Weekends" : "0 */2 * * 5,6"
      },
      useGentleSelect: true
 });
}
 
function loadRecurringProjectDetails(projectId, projectCode, responseJson){
	famstacklog(responseJson);
	$("#RPABCreatOrUpdate").html("Update");
	$(".recurringTimeDiv").removeClass("hide");
	$(".recurringDelete").removeClass("hide");
	$(".recurringEndTime").val(responseJson.endDateString);
	$("#recurringCronExpressionValue").val(responseJson.cronExpression);
	$(".recurringProjectDiv").removeClass("hide");
	if ((responseJson.recurreOriginal != null && responseJson.recurreOriginal) || (responseJson.recurreOriginal == null && recurringOriginal)) {
		$(".recurringProjectName").html("<a href='project\\"+responseJson.projectId+"' target='_new'>View recurring project</a>");
		$(".recurreOriginal").prop("checked", "checked");
	} else {
		$(".recurringProjectName").html("<a href='project\\"+projectId+"' target='_new'>View recurring project</a>");
		$(".recurreOriginal").removeAttr("checked");
	}
	initializeCron(responseJson.cronExpression);
	$("#recurringNET").html(responseJson.nextRun);
	if (responseJson.lastRun != null){
		$("#recurringLET").html(responseJson.lastRun);
	} else {
		$("#recurringLET").html("Not yet run");
	}
	$(".recurringDelete").attr("onclick", "deleteRecurringProject("+responseJson.id+",'"+projectCode+"')");
}

function createRecurringProject(projectCode, projectId) {
	var cronExp = $("#recurringCronExpressionValue").val();
	var recurringEndDate = $("#recurringEndDate").val();
	var recurreOriginal = $(".recurreOriginal").prop("checked");
	
	$("#recurringEndDate").css("border", " 0px solid red");
	
	if (new Date(recurringEndDate) < new Date()) {
		$("#recurringEndDate").css("border", " 1px solid red");
		return;
	}
	
	var dataString = {"projectCode": projectCode, "projectId": projectId, "cronExp":cronExp, "recurringEndDate":recurringEndDate, "recurreOriginal" : recurreOriginal};
	doAjaxRequest("POST", "${applicationHome}/createRecurringProject", dataString, function(data) {
			famstacklog(data);
			if (data != ""){
				var responseJson = JSON.parse(data);
				loadRecurringProjectDetails(projectId, projectCode, responseJson);
				$(".recurringSpin"+projectCode).addClass("fa-spin");
				$(".recurringSpin"+projectCode).html('');
			}
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}

function deleteRecurringProject(recurringId, projectCode) {
	$(".msgConfirmText").html("Deleting recurring project schedule");
	$(".msgConfirmText1").html(recurringId);
	$("#confirmYesId").prop("href","javascript:deleteRecuringProjectDetails('"+recurringId+"','"+projectCode+"')");
}

function deleteRecuringProjectDetails(recurringId, projectCode) {
	var dataString = {"recurringId": recurringId};
	doAjaxRequest("POST", "${applicationHome}/deleteRecuringProjectDetails", dataString, function(data) {
			famstacklog(data);
			$(".recurringSpin"+projectCode).removeClass("fa-spin");
			$(".recurringSpin"+projectCode).html('');
			initializeCron("0 5 0 * * ?");
			$(".message-box").removeClass("open");
			$("#RPABCreatOrUpdate").html("Create");
			$(".recurringTimeDiv").addClass("hide");
			$(".recurringDelete").addClass("hide");
			$(".recurringEndTime").val("");
			
			if ( recurringOriginal) {
				$(".recurreOriginal").prop("checked", "checked");
			} else {
				$(".recurreOriginal").removeAttr("checked");
			}
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}

function refreshRecurringSpin(){
	doAjaxRequest("GET", "${applicationHome}/getAllRecuringProjectCodes", {}, function(data) {
			famstacklog(data);
			if (data != ""){
				var responseJson = JSON.parse(data);
				$.each(responseJson, function(idx, elem){
					$(".recurringSpin"+elem).addClass("fa-spin");
					$(".recurringSpin"+elem).html('<span class="hide">Recurring</span>');
				});
			} 
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}

$( document ).ready(function(){
refreshProjectDetails();
refreshRecurringSpin();
});
/*********recurring ends*********/

var showTaskActActualTimeEdit = function(taskActId) {
	$("."+taskActId+"taskTimeEditLink").hide();
	$("."+taskActId+"taskTimeEdit").show();	
	$("."+taskActId+"taskActTimeEdit").show();	
}

var hideTaskActActualTimeEdit = function(taskActId) {
	$("."+taskActId+"taskTimeEditLink").show();
	$("."+taskActId+"taskTimeEdit").hide();	
	$("."+taskActId+"taskActTimeEdit").hide();	
}

function taskActActualTimeSubmit(taskId, activityId){
	var hours=$(".taskActHHTimeEdit"+activityId).val();
	var mins=$(".taskActMMTimeEdit"+activityId).val();
	
	taskActActualTimeSubmitAction(taskId, activityId, hours, mins, true)
}


function taskActActualTimeSubmitPop(taskId, activityId, thisVar){
	var hours= $(thisVar).closest("div").find("input.taskActHHTimeEdit"+activityId).val();
	var mins= $(thisVar).closest("div").find("input.taskActMMTimeEdit"+activityId).val();
	if (taskActActualTimeSubmitAction(taskId, activityId, hours, mins, false) == true) {
		var newDuration = (parseInt(hours) * 60) +parseInt(mins);
		
		var originalActTime = $("input.taskActOriginalTime"+activityId).val();
		var originalTaskTime =  $("input.taskOriginalTime"+taskId).val();
		
		var newTaskDuration = (newDuration - parseInt(originalActTime)) + parseInt(originalTaskTime);
		var newTaskHr = parseInt(newTaskDuration/60);
		var newTaskMins = (newTaskDuration%60);
		
		$("input.taskOriginalTime"+taskId).val(newTaskDuration);
		$("input.taskActOriginalTime"+activityId).val(newDuration);
		
		$("." + taskId +"taskActTaskTimeHrs").html(newTaskHr + ":" + (newTaskMins < 10 ? "0"+ newTaskMins : newTaskMins));
	}
}

var taskActActualTimeSubmitAction = function(taskId, activityId, hours, mins, isHide) {
	
	$(".taskActHHTimeEdit"+activityId).removeClass("error");
	$(".taskActMMTimeEdit"+activityId).removeClass("error");
	
	var error = false;
	if (hours == "" || !$.isNumeric(hours)) {
		$(".taskActHHTimeEdit"+activityId).addClass("error");
		error = true;
	}
	
	if (mins == "" || !$.isNumeric(mins) || parseInt(mins) >= 60) {
		$(".taskActMMTimeEdit"+activityId).addClass("error");
		error = true;
	}
	
	if(error){
		return false;
	}
	
	var newDuration = (parseInt(hours) * 60) +parseInt(mins);
	var startTime ="";
	var endTime ="";
	doAjaxRequest("POST", "${applicationHome}/adjustTaskActivityTime", {"activityId":activityId,"taskId":taskId,"newDuration":newDuration,"startTime":startTime,"endTime":endTime},  function(response) {
		$("."+activityId+"taskTimeEditLinkHrs").html(hours+":"+(mins < 10 ? "0"+ mins : mins));
		if (isHide) {
			hideTaskActActualTimeEdit(activityId);
		}
	}, function(e) {
	});
	
	return true;
}

function initializePopOver() {
	$("[data-toggle=popover]").each(function(i, obj) {
		$(this).popover({
		  html: true,
		  content: function() {
		    var id = $(this).attr('id');
		    return $('#popover-' + id).html();
		  }
		});
	});
}
$(".ranges ul li").on("click",function(){
	persistDateFilter($(this).html());
});

var persistDateFilter = function(value){
	var name = ${currentUser.id} + "_filterDate";
	
	if ("Custom Range" != value && filterDateMap.hasOwnProperty(value)) {
		doAjaxEnableSettings(name,value);
	}
	
}

$('.recurringEndTime').datetimepicker({
	timepicker:false,
	formatDate:'Y/m/d',
	format:'Y/m/d',
	minDate:new Date()
});

function initializeCreateFormModel(){
	$(".projectMonthlySplitInfo").hide();
	$(".createProjectInfo").show();
	$("#createprojectmodal .modal-footer").show();
	$(".monthlySplitGrandTotal").html("");
	fillUserSkillSplitData();
	reInitialilzeMonthlySplitTable = true;
}

var reInitialilzeMonthlySplitTable = true;

$(".monthlySplitButton").on("click", function(){
		$(".createProjectInfo").hide(1000);
		$(".projectMonthlySplitInfo").show(1000);
		$("#createprojectmodal .modal-footer").hide();
		if (reInitialilzeMonthlySplitTable) {
			var monthlySplitSavedData = $("#hoursUserSkillMonthlySplit").val();
			$("#fillMonthlySplitDataTotalHours").val("");
			fillMonthlySplitData(monthlySplitSavedData != "" ? monthlySplitSavedData : null);
			reInitialilzeMonthlySplitTable = false;
		}
	});
	

$(".projectMonthlySplitInfoBackButton").on("click", function(){
	$(".projectMonthlySplitInfo").hide(1000);
	$(".createProjectInfo").show(1000);
	$("#createprojectmodal .modal-footer").show();
});

$(".projectMonthlySplitInfoSaveButton").on("click", function(){
	fillUserSkillSplitData();
	$("#hoursUserSkillMonthlySplit").val(JSON.stringify(getMonthlySplitJson()));
	
});
var userSkilsList = ["Primary Research","Secondary Research","Data Analysis","BI Tableau Power Bi etc","Survey Programming","Project Management","Viz","Social Media","Data Engineering","Digital-Web Analytics","Applied AI-Advanced Data Sciences","AI Engineering","Product-Web Development","Others"]
var monthNames = [
          	    "Jan", "Feb", "Mar",
          	    "Apr", "May", "Jun", "Jul",
          	    "Aug", "Sep", "Oct",
          	    "Nov", 
          	    "Dec"
          	  ];
          	  
function getMonthlySplitJson() {
	var monthlySplitDataJsonObject = {};
	var userSkillSplitDataJsonObject = {};
	
	var monthUserSkillTotal = {};
	var userSkillTotal = {};
	var grandTotalHour = 0;
	
	$(".monthSplitData").each(function(monthSplitDataIndex, monthSplitDataRow){
		var monthName = $(monthSplitDataRow).attr("data-month-name");
		monthlySplitDataJsonObject[monthName] = [];
		var totalMonthHour = 0;
		$(monthSplitDataRow).find("input").each(function(monthSkillHourIndex, monthSkillHour) {
			var hour = $(monthSkillHour).val();
			var skillName = $(monthSkillHour).attr("data-name");	
			var hourInt = hour == "" ? 0 : parseInt(hour); 
			if(hourInt > 0) {
				var skillNameHourIntObj = {};
				skillNameHourIntObj[skillName] = hourInt;
				monthlySplitDataJsonObject[monthName].push(skillNameHourIntObj);
				totalMonthHour += hourInt;
				var skillNameHour = userSkillSplitDataJsonObject[skillName];
				if(!skillNameHour) {
					userSkillSplitDataJsonObject[skillName] = hourInt;
				} else {
					userSkillSplitDataJsonObject[skillName] = skillNameHour + hourInt;
				}

			}
		});
		$(".MTUS_"+monthName).html("<b>"+totalMonthHour+"</b>");
		grandTotalHour+=totalMonthHour;
	});
	
	fillUserSkillDataTable(userSkillSplitDataJsonObject, grandTotalHour);
	
	return monthlySplitDataJsonObject;
}

function fillUserSkillDataTable(userSkillSplitDataJsonObject, grandTotal) {
	
	for (var key in userSkillSplitDataJsonObject) {
		$(".USTH_"+key).html("<strong>" + userSkillSplitDataJsonObject[key] + "</strong>");
	}
	$("td.monthlySplitGrandTotal").html("<b>"+grandTotal+"</b>")
	$("#fillMonthlySplitDataTotalHours").val(grandTotal);
	$("#duration").val(grandTotal);
}

function fillMonthlySplitData(monthlySplitDataJsonObject){
	var startDate = $("#estStartTime").val();
	var endDate = $("#estCompleteTime").val();	
	var monthYearList = getMonthsYearBetweenTwoDates(startDate,endDate );
	var monthlySplitHeader = "<th>MONTH</th>";
	var monthlySplitTableFooter = '<td style="width:60px;font-weight:bold">TOTAL</td>';
	var monthlySplitBody = "";
	
	$.each(userSkilsList, function( index, userSkillValue ) {
		monthlySplitHeader += "<th>"+userSkillValue+"</th>";
		monthlySplitTableFooter += '<td style="font-weight: bold;" class="USTH_'+getShortString(userSkillValue)+'"></td>';
	});	
	monthlySplitHeader += '<th style="font-weight:bold">TOTAL</th>';
	monthlySplitTableFooter += '<td class="monthlySplitGrandTotal"></td>';
	
	$(".monthlySplitTableHeader").html(monthlySplitHeader);
	$(".monthlySplitTableFooter").html(monthlySplitTableFooter);
	
	$.each(monthYearList, function( monthYearIndex, monthYearValue ) {
		monthlySplitBody += '<tr style="font-weight: bold;" data-month-name="'+monthYearValue+'" class="monthSplitData '+monthYearValue+'"><td>'+monthYearValue+'</td>';
		$.each(userSkilsList, function( userSkillIndex, userSkillValue ) {
			monthlySplitBody += '<td class="MUS_'+getShortString(userSkillValue)+'"><input type="number" min="1" max="999" data-name="'+getShortString(userSkillValue)+'" type="text" style="width:50px"/></td>';
		});	
		monthlySplitBody += '<td class="MTUS_'+monthYearValue+'" style="font-weight: bold;"></td>'
		monthlySplitBody += '</tr>';
	});	
	$(".monthlySplitTableBody").html(monthlySplitBody);
	
	if(monthlySplitDataJsonObject != null) {
		monthlySplitDataJsonObject = JSON.parse(monthlySplitDataJsonObject);		
		var userSkillSplitDataJson ={};
		var grandUserSkillHoursTotal = 0;
		for(var userSkillMonthkey in monthlySplitDataJsonObject) {
			var totalMonthHour = 0;
			var userSkillMonthColumn = $(".monthlySplitTableBody").find("." + userSkillMonthkey);
			$.each(monthlySplitDataJsonObject[userSkillMonthkey], function(index, userSkillHourObj) {
				for(var userSkillName in userSkillHourObj) {
					$(userSkillMonthColumn).find(".MUS_" + userSkillName  + " input").val(userSkillHourObj[userSkillName]);
					var skillNameHourInt = parseInt(userSkillHourObj[userSkillName]);
					
					var skillNameHour = parseInt(userSkillSplitDataJson[userSkillName]);
					if(!skillNameHour) {
						userSkillSplitDataJson[userSkillName] = skillNameHourInt;
					} else {
						userSkillSplitDataJson[userSkillName] = skillNameHour + skillNameHourInt;
					}
					totalMonthHour += skillNameHourInt;			
				}
				
			});
			$(".MTUS_"+userSkillMonthkey).html("<b>"+totalMonthHour+"</b>");
			grandUserSkillHoursTotal+=totalMonthHour;
		}
		
		fillUserSkillDataTable(userSkillSplitDataJson, grandUserSkillHoursTotal);
	}
}

function getShortString(string){
	return string.replace(/\ /g, "_");
}
function fillUserSkillSplitData(){
	var userSkillSplitBody = "";
	
	$.each(userSkilsList, function( index, userSkillValue ) {
		userSkillSplitBody += '<tr><td>'+userSkillValue+'</td><td class="USTH_'+getShortString(userSkillValue)+'"></td></tr>';
	});	
	
	$(".userSkillSplitBody").html(userSkillSplitBody);
}

function getMonthsYearBetweenTwoDates(startDate, endDate){
	var start      = startDate.split('/');
	  var end        = endDate.split('/');
	  var startYear  = parseInt(start[0]);
	  var endYear    = parseInt(end[0]);
	  var dates      = [];

	  for(var i = startYear; i <= endYear; i++) {
	    var endMonth = i != endYear ? 11 : parseInt(end[1]) - 1;
	    var startMon = i === startYear ? parseInt(start[1])-1 : 0;
	    for(var j = startMon; j <= endMonth; j = j > 12 ? j % 12 || 11 : j+1) {
	      var displayMonth = monthNames[j];
	      
	      dates.push([displayMonth,i].join('-'));
	    }
	  }
	  return dates;
}

$(document).ready(function(){
	/* MESSAGE BOX */
	$(document).on("click",".quickProjectDuplicateMsg",function(){
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
</script>