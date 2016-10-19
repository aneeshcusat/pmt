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
		width: 40px;
		border: 2px solid #FFF;
		border-radius: 20%;
	}
	.project_progress .progress {
		margin-bottom: 5px;
		height: 10px;
	}
	
	div.tagsinputWatchers{
	background-color: #fff;
	}
}

</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">
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
								<a data-toggle="modal" data-target="#createprojectmodal" onclick="clearProjectFormForCreate()"
									class="btn btn-success btn-block"> <span class="fa fa-plus"></span>
									Create a new Project
								</a>
							</div>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
	<div class="page-title">
		<h4>
			
		</h4>
	</div>
	<table class="table table-hover p-table datatable">
		<thead>
			<tr>
				<th>Project Name</th>
				<th>Project Code</th>
				<th>Assignees</th>
				<th>Project Progress</th>
				<th>Project Status</th>
				<th>Actions</th>
			</tr>
		</thead> 

		<c:if test="${not empty modelViewMap.projectDetailsData}">
		<tbody>
        <c:forEach var="project" items="${modelViewMap.projectDetailsData}">
        

			<tr>
				<td class="project_name"><a href="${applicationHome}/project/${project.id}">${project.name}</a> <br> <small>created on ${project.createdDate}</small></td>
				<td><h5>${project.code}</h5></td>
				<td class="project_team">
				<c:if test="${not empty project.projectTaskDeatils}">
					<c:forEach var="taskDetails" items="${project.projectTaskDeatils}" varStatus="taskIndex"> 
							<img alt="image" src="${applicationHome}/image/${taskDetails.assignee}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
					</c:forEach>
				</c:if>
				</td>
				<td class="project_progress">
				
				 <c:set var="projectState" value="info"/>
                  <c:if test="${project.status == 'COMPLETED' }">
                   	<c:set var="projectState" value="success"/>
                  </c:if>
                  <c:if test="${project.status == 'NEW' }">
                  </c:if>
                  <c:if test="${project.projectMissedTimeLine == true }">
                  	<c:set var="projectState" value="danger"/>
                  </c:if>
                      <div class="progress progress-small">
                          <div class="progress-bar progress-bar-${projectState}" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: ${project.projectCompletionPercentage}%;"></div>
                      		 <small>${project.projectCompletionPercentage}% Complete</small>
                      </div>
	                                                        
				</td>
				<td><span class="label label-${projectState}">${project.status}</span></td>
				<td>
					<button class="btn btn-default btn-rounded btn-sm" data-toggle="modal" data-target="#createprojectmodal"
						onclick="loadProjectForClone('${project.id}')">
						<span class="fa fa-clipboard" ></span>
					</button>
					<button class="btn btn-default btn-rounded btn-sm" data-toggle="modal" data-target="#createprojectmodal" 
					<c:if test="${project.status != 'NEW' }">
						disabled="true"
                  	</c:if>
						onclick="loadProjectForUpdate('${project.id}')">
						<span class="fa fa-pencil"></span>
					</button>
					<a href="#" data-box="#confirmationbox" class="mb-control profile-control-right btn btn-danger btn-rounded btn-sm" 
						onclick="deleteProject('${project.id}','${project.name}');">
						<span class="fa fa-times"></span>
					</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
        </c:if>
		<c:if test="${empty modelViewMap.projectDetailsData}">
		  There are no assigned projects
		</c:if>
	</table>


	
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
<script type='text/javascript'
	src='${js}/plugins/jquery-validation/jquery.validate.js'></script>
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


var clearProjectFormForCreate = function(projectId) {
	$("#createProjectFormId").prop("action","createProject");
	$("#myModalLabel").html("Create a Project");
	$("#projectActionButton span").html("Save");
	clearCreateProjectForm();
	$('#estStartTime').val(getTodayDate(new Date()) + " 08:00");
	$('#estCompleteTime').val(getTodayDate(new Date()) + " 05:00");
	
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
	$('#estCompleteTime').val(getTodayDate(new Date()) + " 05:00");
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

function clearCreateProjectForm(){
	$("#projectName").val("");
	$("#summary").val("");
	
	$("#billable").attr("checked", false);
	$("#nonbillable").attr("checked", false);
	$('#billable').parent().removeClass("active");
	$('#nonbillable').parent().removeClass("active");
	
	$("#priority").prop("selectedIndex",0);
	$('#priority').selectpicker('refresh');
	
	$("#duration").prop("selectedIndex",0);
	$('#duration').selectpicker('refresh');
	
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
	
	$("#priority").val(project.priority);
	$('#priority').selectpicker('refresh');
	
	$("#duration").val(project.duration);
	$('#duration').selectpicker('refresh');
	
	$("#complexity").val(project.complexity);
	$('#complexity').selectpicker('refresh');

	$("#projectcode").val(project.code);
	
	$("#projectId").val(project.id);
	
	$("#PONumber").val(project.PONumber);
	
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
		$('#createProjectFormId').submit();
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
	
	$("#billable").on("click",function(){
		resetAccount();
		resetTeam();
		resetClient();
		$('.accountOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		$('#clientId').selectpicker('refresh');
	});
	
	$("#nonbillable").on("click",function(){
		resetAccount();
		resetTeam();
		resetClient();
		
		$('.accountOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		$('#clientId').selectpicker('refresh');
	});
	
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
		console.log("PONumber" + PONumber);
	});
	
	$("#clientId").on("change",function(){
		var subTeam = $(this).children(":selected").attr("subteam");
		var account = $(this).children(":selected").attr("account");
		console.log("account" + account);
		console.log("subTeam" + subTeam);
		$('#accountId').prop("selectedIndex",$("#"+account).attr("index"));
		$('#teamId').prop("selectedIndex",$("#"+subTeam).attr("index"));
		
		$('#billable').parent().removeClass("active");
		$('#nonbillable').parent().removeClass("active");
		
		if ($(this).children(":selected").attr("accounttype") == 'BILLABLE') {
			$('#billable').attr("checked", true);
			$('#billable').parent().addClass("active");
		} else {
			$('#nonbillable').attr("checked", true);
			$('#nonbillable').parent().addClass("active");
		}
		
		$('#accountId').selectpicker('refresh');
		$('#teamId').selectpicker('refresh');
		
		var PONumber = $( "#teamId option:selected" ).attr("poid");
		$("#PONumber").val(PONumber);
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
</script>