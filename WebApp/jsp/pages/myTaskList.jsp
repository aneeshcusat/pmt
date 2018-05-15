<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>           
<style>
#unbillableTaskCreationModal .modal-dialog {
	width: 50%;
}

.event-list,
.icon-w-list,
.minified-task-list {
  font-family: 'Karla', sans-serif;
  margin-bottom: -1px !important;
  -webkit-border-bottom-right-radius: 3px;
  -moz-border-radius-bottomright: 3px;
  border-bottom-right-radius: 3px;
  -webkit-border-bottom-left-radius: 3px;
  -moz-border-radius-bottomleft: 3px;
  border-bottom-left-radius: 3px;
}
.event-list .row,
.icon-w-list .row,
.minified-task-list .row {
  margin-right: -15px;
  margin-left: -15px;
}
.event-list .event-icon,
.icon-w-list .event-icon,
.minified-task-list .event-icon {
  padding: 0 12px;
  color: #7f8c8d;
}
.event-list .event-item,
.icon-w-list .event-item,
.minified-task-list .event-item,
.event-list .icon-w-list-item,
.icon-w-list .icon-w-list-item,
.minified-task-list .icon-w-list-item,
.event-list .task-item,
.icon-w-list .task-item,
.minified-task-list .task-item {
  padding-left: 0;
}
.event-list .event-item h5,
.icon-w-list .event-item h5,
.minified-task-list .event-item h5,
.event-list .icon-w-list-item h5,
.icon-w-list .icon-w-list-item h5,
.minified-task-list .icon-w-list-item h5,
.event-list .task-item h5,
.icon-w-list .task-item h5,
.minified-task-list .task-item h5 {
  margin: 2px 0 3px 0;
  font-size: 14px;
}
.event-list .event-item .event-date,
.icon-w-list .event-item .event-date,
.minified-task-list .event-item .event-date,
.event-list .icon-w-list-item .event-date,
.icon-w-list .icon-w-list-item .event-date,
.minified-task-list .icon-w-list-item .event-date,
.event-list .task-item .event-date,
.icon-w-list .task-item .event-date,
.minified-task-list .task-item .event-date,
.event-list .event-item .icon-w-list-date,
.icon-w-list .event-item .icon-w-list-date,
.minified-task-list .event-item .icon-w-list-date,
.event-list .icon-w-list-item .icon-w-list-date,
.icon-w-list .icon-w-list-item .icon-w-list-date,
.minified-task-list .icon-w-list-item .icon-w-list-date,
.event-list .task-item .icon-w-list-date,
.icon-w-list .task-item .icon-w-list-date,
.minified-task-list .task-item .icon-w-list-date,
.event-list .event-item .task-date,
.icon-w-list .event-item .task-date,
.minified-task-list .event-item .task-date,
.event-list .icon-w-list-item .task-date,
.icon-w-list .icon-w-list-item .task-date,
.minified-task-list .icon-w-list-item .task-date,
.event-list .task-item .task-date,
.icon-w-list .task-item .task-date,
.minified-task-list .task-item .task-date {
  display: block;
  font-size: 13px;
  opacity: .7;
}
.event-list i,
.icon-w-list i,
.minified-task-list i {
  font-size: 23px;
  line-height: 40px;
}
.event-list span.indicator,
.icon-w-list span.indicator,
.minified-task-list span.indicator {
  position: absolute;
  top: 6px;
  left: -4px;
  width: 8px;
  height: 8px;
}
.list-group.task-list {
  margin-bottom: 0;
}
.list-group.task-list li {
  padding: 0 15px;
  display: table;
  width: 100%;
  border-color: #dfe7ea;
  margin-bottom: 3px;
  -webkit-border-radius: 3px;
  -moz-border-radius: 3px;
  border-radius: 3px;
}
.list-group.task-list li .checkbox input[type="checkbox"],
.list-group.task-list li .checkbox-inline input[type="checkbox"] {
  position: relative;
  margin: 0;
}
.list-group.task-list li .checkbox {
  margin: 0;
}
.list-group.task-list li .checkbox label {
  padding-left: 0;
}
.list-group.task-list li .checkbox .bootstrap-checkbox {
  display: block;
}
.list-group.task-list li .checkbox .bootstrap-checkbox .btn {
  font-size: 18px;
  color: #dddddd;
  padding: 10px 0;
}
.list-group.task-list li .checkbox .bootstrap-checkbox .btn .fa {
  position: relative;
  top: 1px;
}
.list-group.task-list li .checkbox .bootstrap-checkbox .btn .fa.fa-check-square {
  color: #00a89c;
}
.list-group.task-list li:hover .checkbox .bootstrap-checkbox .btn {
  color: #919191;
}
.list-group.task-list li.completed .task-item {
  text-decoration: line-through;
}
.list-group.task-list .row {
  margin: 0px;
}
.list-group.task-list .task-check {
  display: table-cell;
  width: 3%;
}
.list-group.task-list .task-item {
  display: table-cell;
  width: 70%;
  padding: 13px 0 12px;
}
.list-group.task-list .task-item .task-today {
  margin-right: 11px;
  width: 14px;
  height: 15px;
}
.list-group.task-list .task-item .task-today a {
  display: block;
  font-size: 15px;
  position: relative;
  top: 2px;
  opacity: .1;
}
.list-group.task-list .task-item .task-today a:hover {
  opacity: 1;
  color: #f1c40f;
}
.list-group.task-list .task-item .task-today a.marked {
  color: #f1c40f;
  opacity: 1;
}
.list-group.task-list .task-item .task-title {
  margin: 0;
  font-size: 14px;
  font-weight: 400;
  line-height: 20px;
}
.list-group.task-list .task-date {
  font-family: 'Karla', sans-serif;
  text-transform: uppercase;
  font-size: 13px;
}
.list-group.task-list .task-meta {
  display: table-cell;
  text-align: right;
  padding-right: 10px;
}
.list-group.task-list .task-meta .label {
  margin: 0 0 0 10px;
  position: relative;
  top: -1px;
  text-transform: uppercase;
  font-size: 9px;
  padding: 4px 6px;
}
.list-group.task-list .task-assignee {
  display: table-cell;
  width: 5%;
}
.list-group.task-list .task-assignee a {
  display: block;
  width: 30px;
  height: 30px;
  overflow: hidden;
  margin-left: 5px;
  -webkit-border-radius: 50px;
  -moz-border-radius: 50px;
  border-radius: 50px;
  background: #ecf0f1;
  position: relative;
}
.list-group.task-list .task-assignee a.no-assignee {
  margin-bottom: -10px;
}
.list-group.task-list .task-assignee a:before {
  content: "\f007";
  font-family: FontAwesome;
  font-style: normal;
  font-weight: normal;
  text-decoration: inherit;
  position: absolute;
  font-size: 16px;
  left: 30%;
  color: #fff;
  z-index: 1;
  top: 16%;
  color: #7f8c8d;
}
.list-group.task-list .task-assignee a:hover:after {
  opacity: .9;
}
.list-group.task-list .task-assignee img {
  width: 30px;
  position: relative;
  z-index: 10;
}
.list-group.task-list .task-edit {
  display: table-cell;
  width: 3%;
  border-left: 1px solid #dfe7ea;
  padding-left: 15px;
  position: relative;
}
.list-group.task-list .task-edit a {
  display: block;
  opacity: .3;
}
.list-group.task-list .task-edit a i {
  display: block;
  position: relative;
  top: 1px;
}
.list-group.task-list .task-edit a:hover {
  opacity: 1;
}
.list-group.task-list .task-edit.dropdown .dropdown-menu {
  right: -10px;
  top: 38px;
}
.list-group.task-list .task-edit.dropdown .dropdown-menu li {
  padding-right: 0;
  padding-left: 0;
}
.list-group.task-list .task-edit.dropdown .dropdown-menu li a {
  opacity: 1;
}
.list-group.task-list .label {
  margin-top: 5px;
}
.list-group .tooltip {
  font-size: 12px;
  font-family: 'Karla', sans-serif;
}
.list-group.minified-task-list .row {
  margin: 0px;
}
.list-group.minified-task-list .task-item {
  float: left;
  padding: 5px 0;
}
.list-group.minified-task-list .task-item .task-title {
  margin: 0;
}
.list-group.minified-task-list .label {
  display: block;
  margin-top: 5px;
}

.title-separator .separator-heading {
  text-transform: uppercase;
  font-size: 15px;
}
.title-separator .separator-heading {
  text-transform: uppercase;
  font-size: 15px;
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
.list-group-item.active, .list-group-item.active:hover, .list-group-item.active:focus {
    background: lightblue;
    border-color: #1b1e24;
}
.list-group-horizontal .list-group-item {
	margin-bottom: 0;
	margin-left:-4px;
	margin-right: 0;
}
.list-group-horizontal .list-group-item:first-child {
	border-top-right-radius:0;
}
.list-group-horizontal .list-group-item:last-child {
	border-bottom-left-radius:0;
}
</style>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Task Activities</li>
 </ul>
 <!-- END BREADCRUMB -->  
<div class="content-frame" ng-app="mytasks">     
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-tasks"></span> Task Activities</h2>
        </div>                                                
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                
    </div> 
<div class="col-md-12">

			<div class="panel panel-default">
				<div class="panel-body">
					<form class="form-horizontal">
						<div class="form-group">
							<div class="col-md-6">
								<div class="input-group">
									<div class="input-group-addon">
										<span class="fa fa-search"></span>
									</div>
									<input type="text" class="form-control" id="taskActivitySearchId" placeholder="Search for a task activity">
								</div>
							</div>
							 <div class="col-md-3">
							  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
								<select id="taskAssigneeId" name="taskAssigneeId" class="form-control select" data-live-search="true">
								<option value="">All</option>
									 <c:if test="${not empty userMap}">
										<c:forEach var="user" items="${userMap}">
											<c:if test="${user.role != 'SUPERADMIN'}">
												<option value="userId${user.id}">${user.firstName}</option>
											</c:if>
						  				</c:forEach>
						  			</c:if>
								</select>
								</c:if>
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
    
		    <div class="row">
		        <div class="col-md-12">
				<div class="inner no-pad-t">
						
			<div class="task-list-container">
		
			<div class="title-separator">
				<h2 class="separator-heading">Todays</h2>
			</div>
			<ul class="list-group task-list" id="todaysTask">
			
			</ul>
			
			<div class="title-separator">
				<h2 class="separator-heading">Upcoming</h2>
			</div>
			<ul class="list-group task-list" id="upcomingTask">
			</ul>
		
			<div class="title-separator">
				<h2 class="separator-heading">Last months</h2>
			</div>
			<ul class="list-group task-list" id="oldTask">
			</ul>	
			
			</div>
		</div>
    </div>
</div>

<!-- task completion modal start -->
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
</div>
<ul class="hide"  id="taskTemplate">
	<li class="list-group-item taskact-item">
			<div class="task-item">
				<div class="task-today pull-left">
					<a href="#" class="ttip" data-toggle="tooltip" data-placement="top" title="" data-original-title="Mark Today">
						<i class="fa fa-star"></i>
					</a>
				</div>
				<h3 class="task-title"></h3>
			</div>

			<div class="task-meta">
				<span class="task-date color-danger"></span>
				<span class="label label-danger"></span>
			</div>

			<div class="task-assignee">
				<a href="#" class="ttip" data-toggle="tooltip" data-placement="top" title="">
					<img alt="image" class="assignee" src="" onerror="this.src='/bops/jsp/assets/images/users/no-image.jpg'">
				</a>
			</div>
			
			<div class="task-edit">
					<a href="#" data-box="#confirmationbox" class="deleteTask hide mb-control1 profile-control-right btn btn-danger btn-rounded btn-sm">
						<i class="fa fa-trash-o fa-1" style="color:red" aria-hidden="true"></i>
					</a>
			</div>
		</li>

</ul>

<!-- project create modal end -->  

<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>     
 <script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js"></script>       
<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>

<script>
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
    
	var dataString = {userId:$("#userId").val(),type:$("#taskType").val(),startDate:startDate,endDate:endDate,comments:$("#taskStartComments").val()};
	doAjaxRequest("POST", "${applicationHome}/createNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
        	getAssignJsonData();
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
}
//unbilled task end

var getAssignJsonData = function(){
	doAjaxRequest("GET", "${applicationHome}/userTaskActivityJson", {},  function(jsonData) {
		fillTableFromJson(jsonData);
	   }, function(e) {
	   });
};


var fillTableFromJson = function(jsonAssignData) {
	$("#todaysTask").html("");
	$("#oldTask").html("");
	$("#upcomingTask").html("");
	$.each(JSON.parse(jsonAssignData), function(idx, elem){
		
		var taskData = $("#taskTemplate").clone();
		$(taskData).find(".taskact-item").addClass("userId" + elem.userId);
		$(taskData).find(".task-title").html(elem.taskId + " : " +elem.taskName);
		$(taskData).find(".task-date").html(elem.dateId);
		$(taskData).find(".deleteTask").attr("onclick", "deleteTaskActivity('" +elem.taskActivityId + "','"+elem.taskName+"' )" );
		$(taskData).find(".assignee").attr("src", "/bops/dashboard/image/" + elem.userId );
		
		if (elem.userTaskType == "LEAVE" || elem.userTaskType == "MEETING" || elem.userTaskType == "TRAINING" || elem.userTaskType == "ADMIN" || elem.userTaskType == "BD") {
			 $(taskData).find(".deleteTask").removeClass("hide");
			 $(taskData).find(".task-title").html(elem.taskName);
  		}
		
		if (getTodayDate(new Date()) == elem.dateId) {
			$("#todaysTask").append($(taskData).html());
		} else if (getTodayDate(new Date()) > elem.dateId) {
			$("#oldTask").append($(taskData).html());
		} else if (getTodayDate(new Date()) < elem.dateId) {
			$("#upcomingTask").append($(taskData).html());
		}
	});
}

$(document).ready(function(){
	getAssignJsonData();
});

var deleteTaskActivityAjax = function(activityId) {
	doAjaxRequest("POST", "${applicationHome}/deleteTaskActivity", {activityId:activityId},  function() {
		getAssignJsonData();
		$(".mb-control-close").click();
	}, function(e) {
	});
}

function deleteTaskActivity(activityId, taskName){
	$(".msgConfirmText").html("Delete task activity");
	$(".msgConfirmText1").html(taskName);
	$("#confirmYesId").prop("href","javascript:deleteTaskActivityAjax('"+activityId+"')");
}


var activeUserId = "";

$(".taskOwnersList").on("click", function(){
	var hasClass = $("#" + this.id).hasClass("active");
	$(".taskOwnersList").removeClass("active");
	if (!hasClass) {
		$("#" + this.id).addClass("active");
		$(".taskact-item").hide();
		$(".taskact-item." + this.id).show();
		activeUserId=this.id;
	} else {
		$(".taskact-item").show();
		activeUserId="";
	}
	performSearch();
});


$("#taskAssigneeId").on("change", function(){
	var assigneeSelectionId = $(this).val();
	if (assigneeSelectionId != "") {
		$(".taskact-item").hide();
		$(".taskact-item." + assigneeSelectionId).show();
		activeUserId=assigneeSelectionId;
	} else {
		$(".taskact-item").show();
		activeUserId="";
	}
	performSearch();
});


function performSearch(){
	var serarchText = $('#taskActivitySearchId').val();
	famstacklog(serarchText);
	var searchId = ".taskact-item";

	if (activeUserId != "") {
		searchId+="."+activeUserId;
	}
	
	if (serarchText != "") {
		$('.taskact-item').hide();
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

$(document).ready(function(){
	/* MESSAGE BOX */
	$(document).on("click",".deleteTask",function(){
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
