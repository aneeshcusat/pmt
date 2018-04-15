<%@page import="com.fasterxml.jackson.annotation.JsonInclude.Include"%>
<%@include file="includes/header.jsp"%>
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
							<div class="col-md-5">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="projectSearchBoxId"
										placeholder="Search for a project.." />
									<div class="input-group-btn">
										<button class="hide btn btn-primary">Search</button>
									</div>
								</div>
							</div>
								<div class="col-md-1">
        							<p style="text-align:center;margin :0 0 0px;font-weight: bold">Show Archived</p>
									<p style="text-align: center;margin :0 0 0px">
									<div class="slideThree">	
										<input type="checkbox" class="styledCheckBox includeArchive" value="None" id="slideThree" name="check" />
										<label for="slideThree"></label>
									</div>
																		<!-- <input id="includeArchive" type="checkbox" class=""/> -->
									
									</p>
							</div>
			                <div class="col-md-3" >
					             	 <span id="reportrange" class="dtrange">                                            
            							<span>${dateRange}</span><b class="caret"></b>
        							</span>
        								<input style="margin-left:10px" class="btn btn-default" type="button" value="Filter" onclick="loadAllProjectDetails($('#daterangeText').val());"></input>
        								<input type="hidden" id="daterangeText" value="hello" /> 
        					</div>
        					
							<div class="col-md-3">
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
<script type="text/javascript" src="${js}/plugins/gentleSelect/jquery-gentleSelect-min.js"></script>
<script type="text/javascript" src="${js}/plugins/cron/jquery-cron.js"></script>
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
        famstacklog("SUCCESS: ", data);
        var responseJson = JSON.parse(data);
        clearCreateProjectForm();
        initializeCreateProjectForm(responseJson);
    }, function(e) {
        famstacklog("ERROR: ", e);
        alert(e);
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
	//$('#duration').selectpicker('refresh');
	
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
		famstacklog(watchersArray);
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
		famstacklog(response);
		var responseJson = JSON.parse(response);
		if (responseJson.status) {
			showNotification($("#projectName").val(),$("#summary").val(),"${applicationHome}/project/"+responseJson.projectId);
			refreshProjectDetails();
			$('#createprojectmodal').modal('hide');
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
            alert(e);
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
            alert(e);
        });
	}
	
	function performProjectSearch(){
		var serarchText = $('#projectSearchBoxId').val();
		famstacklog(serarchText);
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
		$("#PONumber").val(PONumber);
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
		famstacklog("PONumber" + PONumber);
		
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
		validateEstimatedStartTime($('#estStartTime'));
		validateEstimatedEndTime($('#estStartTime'),$('#estCompleteTime'));
		//validateProjectDuration($('#estStartTime'), $('#estCompleteTime'), $('#duration'));
	});
	
	$('#estCompleteTime').on("change", function(){
		validateEstimatedStartTime($('#estStartTime'));
		validateEstimatedEndTime($('#estStartTime'),$('#estCompleteTime'));
		//validateProjectDuration($('#estStartTime'), $('#estCompleteTime'), $('#duration'));
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
        	$('#taskDetailsDiv').css({'top':e.pageY+10,'left':e.pageX-350, 'position':'absolute', 'border':'1px solid gray', 'padding':'5px'});
        	$('#taskDetailsDiv').show();
        }
    }, function(e) {
        famstacklog("ERROR: ", e);
        alert(e);
    }, false);
}

var loadAllProjectDetails = function(daterange) {
	
	var dataString = {"daterange" : daterange, includeArchive:$("input.includeArchive").is(":checked")};
	doAjaxRequest("GET", "${applicationHome}/projectdashboardList", dataString, function(data) {
        $("#projectDashBoardData").html(data);
        refreshRecurringSpin();
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
       	
       	performProjectSearch();
        	
    }, function(e) {
        famstacklog("ERROR: ", e);
        alert(e);
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
	        alert(e);
	    }, false);
	}
	if(!isForce) {
		if ($("#projectOpenLink" + projectId).hasClass("fa-chevron-right")) {
			$("#projectOpenLink" + projectId).removeClass("fa-chevron-right");
			$("#projectOpenLink" + projectId).addClass("fa-chevron-down");
			loadInitialTaskAvailabilityTime(projectId);
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
        alert(e);
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
	
var createDuplicateProjectWithTask = function(projectId, projectCode) {
	
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
		if (availabilityTime == "") {
			$(this).find(".taskCloneAssignee").addClass("error");
			isError = true;
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
        alert(e);
    });
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
				$("#RPABCreatOrUpdate").html("Create");
				initializeCron("0 5 0 * * ?");
			}
			$("#recurringProjectActionButton").attr("onclick", "createRecurringProject('"+projectCode+"',"+projectId+")");
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        alert(e);
	    });
}
 
function initializeCron(cronExpression) {
	$('#recurringCronExpressionDiv').html('<div id="recurringCronExpression"></div>');
	$('#recurringCronExpression').cron({
 	 initial: cronExpression.replace("?","*").substring(2,13),
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
	$("#RPABCreatOrUpdate").html("Upate");
	$(".recurringTimeDiv").removeClass("hide");
	$(".recurringDelete").removeClass("hide");
	$("#recurringCronExpressionValue").val(responseJson.cronExpression);
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
	var dataString = {"projectCode": projectCode, "projectId": projectId, "cronExp":cronExp};
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
	        alert(e);
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
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        alert(e);
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
	        alert(e);
	    });
}

$( document ).ready(function(){
refreshProjectDetails();
refreshRecurringSpin();
});


var showTaskActualTimeEdit = function(taskId){
	$("."+taskId+"taskTimeEdit").show();
	$("."+taskId+"taskTimeEditLink").hide();
}

var hideTaskActualTimeEdit = function(taskId){
	$("."+taskId+"taskTimeEdit").hide();
	$("."+taskId+"taskTimeEditLink").show();
}

var taskActualTimeSubmit = function(taskId) {
	var hours=$("#taskDetailsDiv #taskHHTimeEdit"+taskId).val();
	var mins=$("#taskDetailsDiv #taskMMTimeEdit"+taskId).val();
	
	$("#taskDetailsDiv #taskHHTimeEdit"+taskId).removeClass("error");
	$("#taskDetailsDiv #taskMMTimeEdit"+taskId).removeClass("error");
	var error = false;
	if (hours == "" || !$.isNumeric(hours)) {
		$("#taskDetailsDiv #taskHHTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if (mins == "" || !$.isNumeric(mins) || parseInt(mins) >= 60) {
		$("#taskDetailsDiv #taskMMTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if(error){
		return;
	}
	
	var newDuration = (parseInt(hours) * 60) +parseInt(mins);
	
	doAjaxRequest("POST", "${applicationHome}/adjustTaskTime", {"taskId":taskId,"newDuration":newDuration},  function() {
		hideTaskActualTimeEdit(taskId);
		$("."+taskId+"taskActTimeHrs").html((hours.length > 1 ? hours : "0" + hours) + ":" + (mins.length > 1 ? mins : "0" + mins));
	}, function(e) {
	});
}

</script>