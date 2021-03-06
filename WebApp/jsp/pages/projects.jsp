<%@include file="includes/header.jsp"%>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>                    
     <li class="active">Projects</li>
 </ul>
 <!-- END BREADCRUMB -->       
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
#DataTables_Table_0_filter{
	display: none;
}
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
		width: 40px;
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
	
}

</style>
<!-- START CONTENT FRAME -->
<div class="content-frame margin5">
	<div class="content-frame-top">
		<div class="page-title">
			<h2>
				<span class="fa fa-list-alt"></span> Projects
			</h2>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body">
					<form class="form-horizontal">
						<div class="form-group">
							<div class="col-md-12">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="projectSearchBoxId"
										placeholder="Search for a project.." />
									<div class="input-group-btn">
										<button class="btn btn-primary hide">Search</button>
									</div>
								</div>
							</div>
							<div >
							<%-- 	  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
								<a data-toggle="modal" data-target="#createprojectmodal" onclick="clearProjectFormForCreate()"
									class="btn btn-success btn-block"> <span class="fa fa-plus"></span>
									Create a new Project
								</a>
								</c:if>
							 --%></div>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
	<table class="table table-hover p-table projectDatatable ">
		<thead>
			<tr>
				<th width="30%">Project Name</th>
				<th>Due Date</th>
				<th>Project Code</th>
				<th>Assignees</th>
				<th>Project Progress</th>
				<th>Project Status</th>
				 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
				</c:if>
			</tr>
		</thead> 

		<c:if test="${not empty modelViewMap.projectDetailsData}">
		<tbody>
        <c:forEach var="project" items="${modelViewMap.projectDetailsData}">
        

			<tr>
				<td class="project_name"><a href="${applicationHome}/project/${project.id}"><span style="font-size: 10pt">${project.name}</span></a> <br> 
				<small>created at <fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${project.createdDate}"/></small>
				</td>
				<td><h5>${project.completionTime}</h5></td>
				<td><h5>${project.code}</h5></td>
				<td class="project_team">
				<c:if test="${not empty project.contributers}">
					<c:forEach var="contributer" items="${project.contributers}" varStatus="taskIndex"> 
							<img alt="image" src="${applicationHome}/image/${contributer}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
					</c:forEach>
				</c:if>
				</td>
				<td class="project_progress">
				
				  <c:set var="projectState" value="info"/>
				      <c:set var="statusColor" value=""/>
	                  <c:if test="${project.status == 'NEW' }">
							<c:set var="statusColor" value="background-color:lightblue"/>
	                  </c:if>
	                  <c:if test="${project.status == 'UNASSIGNED' }">
							<c:set var="statusColor" value="background-color:darkviolet"/>
	                  </c:if>
	                  <c:if test="${project.status == 'ASSIGNED' }">
	                  		<c:set var="statusColor" value="background-color:orange"/>
	                  </c:if>
   					  <c:if test="${project.status == 'INPROGRESS' }">
   					  	<c:set var="statusColor" value="background-color: brown;"/>
	                  </c:if>
	                  <c:if test="${project.projectMissedTimeLine == true }">
	                  		<c:set var="projectState" value="danger"/>
	                  </c:if>
	                  <c:if test="${project.status == 'COMPLETED' }">
	                   		<c:set var="projectState" value="success"/>
	                  </c:if>
                      <div class="progress progress-small">
                          <div class="progress-bar progress-bar-${projectState}" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: ${project.projectCompletionPercentage}%;"></div>
                      		 <small>${project.projectCompletionPercentage}% Complete</small>
                      </div>
	                                                        
				</td>
				<td><span class="label label-${projectState}" style="${statusColor}">${project.status}</span></td>
				  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
				
				<%-- <td>
					<button class="btn btn-default btn-rounded btn-sm" data-toggle="modal" data-target="#createprojectmodal"
						onclick="loadProjectForClone('${project.id}')">
						<span class="fa fa-clipboard" ></span>
					</button>
					<button class="btn btn-default btn-rounded btn-sm" data-toggle="modal" data-target="#createprojectmodal" 
						onclick="loadProjectForUpdate('${project.id}')">
						<span class="fa fa-pencil"></span>
					</button>
					<a href="#" data-box="#confirmationbox" class="mb-control profile-control-right btn btn-danger btn-rounded btn-sm" 
						onclick="deleteProject('${project.id}','${project.name}');">
						<span class="fa fa-times"></span>
					</a></td>
				 --%>
					</c:if>
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
<script>
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
            famstacklog("SUCCESS: ", data);
            var responseJson = JSON.parse(data);
            if (responseJson.status){
                window.location.reload(true);
            }
        }, function(e) {
            famstacklog("ERROR: ", e);
            famstackalert(e);
        });
	}
	
	$("#projectSearchBoxId").keydown(function(e){
		$("input[type='search']").val($("#projectSearchBoxId").val());
		$("input[type='search']").trigger(e);
	});
	
	$("#projectSearchBoxId").keyup(function(e){
		$("input[type='search']").val($("#projectSearchBoxId").val());
		$("input[type='search']").trigger(e);
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
		validateEstimatedStartTime();
		validateEstimatedEndTime();
		validateProjectDuration();
	});
	
	$('#estCompleteTime').on("change", function(){
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
		famstacklog("validating project validateEstimatedStartTime");
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
		famstacklog("validating project validateEstimatedEndTime");
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
		famstacklog("validating project duration");
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
</script>