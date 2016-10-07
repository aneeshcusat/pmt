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
		height: 8px;
	}
}

</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">
	<div class="content-frame-top">
		<div class="page-title">
			<h2>
				<span class="fa fa-bars"></span> Projects
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
								<a data-toggle="modal" data-target="#createprojectmodal"
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
			<span class="fa fa-clock-o"></span>ASSIGNED
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
				<td class="project_name"><a href="${applicationHome}/project/${project.id}">${project.name}</a> <br> <small>Created ${project.createdDate}</small></td>
				<td><h5>${project.code}</h5></td>
				<td class="project_team">
                   <img alt="" src=""  onerror="this.src='${assets}/images/users/no-image.jpg'">
                </td>
				<td class="project_progress">
					<div class="progress progress-xs">
						<div style="width: 87%;" class="progress-bar progress-bar-success"></div>
					</div> <small>87% Complete </small>
				</td>
				<td><span class="label label-info">${project.status}</span></td>
				<td>
					<button class="btn btn-default btn-rounded btn-sm">
						<span class="fa fa-folder-open-o"></span>
					</button>
					<%-- <button class="btn btn-default btn-rounded btn-sm" data-toggle="modal" data-target="#createprojectmodal"
						onclick="loadProject('${project.id}')">
						<span class="fa fa-pencil"></span>
					</button>--%>
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
					<button type="button" onclick="doAjaxCreateProjectForm();"
						class="btn btn-primary">
						<span id="userButton">Save</span>
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

<script>


	
	$( document ).ready(function(){
		$("input[type='search']").parent().hide();
	});
	
	var projectNames = [ {
		value : 'Project 1',
		data : 'P100'
	},
	{
		value : 'Project 2',
		data : 'P102'
	} ];

	$('.autocomplete').autocomplete(
		{
			//serviceUrl: '/autocomplete/projects',
			lookup : projectNames,
			onSelect : function(suggestion) {
				alert('You selected: ' + suggestion.value + ', '
						+ suggestion.data);
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
	allowTimes:['08:00','09:00','10:00','11:00','12:00','13:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00','21:00'],
	beforeShowDay: function(date) {
		if (date.getMonth() < new Date().getMonth() && date.getDate() < new Date().getDate()) {
			return [false, ""]
		}
		return [true, ""];
	}
	
	});
</script>