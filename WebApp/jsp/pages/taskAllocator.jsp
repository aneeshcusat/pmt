<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Task Allocator</li>
 </ul>
<style>

.dataTables_length {
width: 40%;
}

.dataTables_filter{
width: 60%;
}
.markable{
	height: 100%;
	padding:0 0 0 0 !important;
}
#employeeListForTaskTable{
height: 100%;
}
table tr td.markable span {
  	float: left;
    height: 100%;
    display: table-cell;
    z-index: 1000;
    text-align: center;
    vertical-align: middle;
    font-size: 15px;
    font-weight: bold;
    color: wheat;
    vertical-align: middle;
 }
 
.legend span {
    width: 23px;
    height: 23px;
    border: 1px solid lightgray;
    padding-top: 3px;
    display: table-cell;
    z-index: 1000;
    text-align: center;
    font-size: 9px;
    font-weight: bold;
    color: wheat;
    vertical-align: middle;
}

.legend table td{
	font-weight: bold;
	padding-left: 5px;
}
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-list"></span> Task Scheduler</h2>
        </div>  
    </div>
    <!-- END CONTENT FRAME TOP -->
    <!-- body-->
    <form:form id="createTaskFormId" action="${applicationHome}/updateTask" method="POST"
		role="form" class="form-horizontal">
    <div class="row">
	<div class="col-md-12"  style="margin-top:10px">
		<div class="row">
			<div class="col-md-4">
				<div class="form-group">
					<label class="col-md-2 control-label">Projects</label>
					<div class="col-md-9">
						<select class="form-control select" name="projectId" id="projectId"  data-live-search="true">
                         <option value="">-select-</option>
                         <c:if test="${not empty unAssignedProjects}">
	                        <c:forEach var="project" items="${unAssignedProjects}">
	                        	<option value="${project.id}">${project.name}</option>
	                        </c:forEach>
                         </c:if>
                     	 </select>
                     	 <c:if test="${not empty unAssignedProjects}">
	                        <c:forEach var="project" items="${unAssignedProjects}">
	                        	<input type="hidden" id="${project.id}unAssignedDuration" value="${project.unAssignedDuration}"/>
	                        	<input type="hidden" id="${project.id}startTime" value="${project.startTime}"/>
	                        	<input type="hidden" id="${project.id}completionTime" value="${project.completionTime}"/>
	                        	<input type="hidden" id="${project.id}duration" value="${project.durationHrs}"/>
	                        </c:forEach>
                         </c:if>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-md-2 control-label">Task</label>
					<div class="col-md-9">
						<form:input type="hidden" value="" path="taskId" id="taskId"/>
						<form:input type="hidden" value="" path="startTime" id="startTime"/>
						<%-- <form:input type="hidden" value="" path="assignee" id="assignee"/>
						<form:input type="hidden" value="" path="helper" id="helper"/> --%>
						<form:input type="hidden" value="" path="name" id="name"/>
						
						
						<select class="form-control" id="taskSelectId" name="taskSelectId"  data-live-search="true">
                         <option value="">-select-</option>
                         <c:if test="${not empty unAssignedProjects}">
	                        <c:forEach var="project" items="${unAssignedProjects}">
	                          	<c:if test="${not empty project.projectTaskDeatils}">
	                         		<c:forEach var="task" items="${project.projectTaskDeatils}">
	                         			<c:if test="${task.status != 'INPROGRESS' && task.status != 'COMPLETED'}">
	                         				<option id="${task.taskId}" projectId="${project.id}" class="taskIdOption" style="display:none" filter="${project.id}-${task.taskId}" value="${task.taskId}">${task.name}</option>
	                         			</c:if>
	                         		</c:forEach>
	                         	</c:if>
	                        	
	                        </c:forEach>
                         </c:if>
                     	 </select>
                     	 <c:if test="${not empty unAssignedProjects}">
	                        <c:forEach var="project" items="${unAssignedProjects}">
	                          	<c:if test="${not empty project.projectTaskDeatils}">
	                         		<c:forEach var="task" items="${project.projectTaskDeatils}">
	                         			<c:if test="${task.status != 'INPROGRESS' && task.status != 'COMPLETED'}">
	                         			<input type="hidden" id="${task.taskId}startTime" value="${task.startTime}"/>
	                        			<input type="hidden" id="${task.taskId}duration" value="${task.duration}"/>
	                        			<input type="hidden" id="${task.taskId}description" value="${task.description}"/>
	                        			<input type="hidden" id="${task.taskId}assignee" value="${task.assignee}"/>
	                        			<input type="hidden" id="${task.taskId}helper" value="${task.helper}"/>
	                        			<input type="hidden" id="${task.taskId}name" value="${task.name}"/>
	                         			</c:if>
	                         		</c:forEach>
	                         	</c:if>
	                        </c:forEach>
                         </c:if>
					</div>
				</div>
			</div>
			<div class="col-md-4">
			<div class="form-group">
				<label class="col-md-2 control-label">Description</label>
                     <div class="col-md-9">
                            <form:textarea class="form-control" id="description" path="description" name="description"></form:textarea> 
                         <span class="help-block">required description</span>
                     </div>
				</div>
				<div class="form-group">
				<label class="col-md-2 control-label" >Duration :</label>
				<label class="col-md-2 control-label text-left">Unassigned</label>
                     <div class="col-md-2" style="margin-top: 7px;">
                     <b id="unassignedDuration">0</b> Hrs
                     </div>
                     <label class="col-md-1 control-label">Project</label>
                     <div class="col-md-2" style="margin-top: 7px;">
                     <b id="projectDuration">0</b> Hrs
                     </div>
                <label class="col-md-1 control-label">Task</label>
                     <div class="col-md-2" style="margin-top: 7px;">
                     <b id="taskDuration">0</b> Hrs
                     </div>
				</div>
			</div>
			
			<div class="col-md-4">
				
				<div class="form-group">
				<label class="col-md-3 control-label">Est Start Time</label>
                     <div class="col-md-4">
                            <form:input type="text" class="form-control dateTimePicker" id="estStartTime" path="startTime"/> 
                         <span class="help-block">required estimated start time</span>
                     </div>
					<label class="col-md-2 control-label">Duration</label>
                     <div class="col-md-3">
                     <form:input class="form-control" path="duration" name="duration" id="duration"/>
                         <%-- <form:select class="form-control select" path="duration" name="duration" id="duration"  data-live-search="true">
                         <option>0</option>
                       	 </form:select> --%>
                         <span class="help-block">required duration</span>
                     </div>
				</div>
				
				<div class="form-group">
				 <div class="col-md-4">
				 </div>
				<!--<label class="col-md-3 control-label">Est Completion Time</label>
                      <div class="col-md-4">
                           <h5 id="estCompleteTime">000/00/00 00:00</h5>
                     </div> -->
                     <label class="col-md-2 control-label"></label>
                     <div class="col-md-5">
					<button type="button" onclick=""
						class="btn btn-primary  pull-right">
						<span id="taskStart" onclick="doAjaxCreateTaskForm()">Update</span>
					</button>
                     </div>
				</div>
				
			</div>

		</div>
		
		<div class="row" id="assignTableId"  style="margin-top:10px">
		<table id="employeeListForTaskTable" class="table table-striped table-bordered" cellspacing="0" width="100%" height="100%">
        <thead>
            <tr style="height: 20px">
            	<th>Select</th>
                <th>Employee</th>
                <th>08:00 - 09:00</th>
                <th>09:00 - 10:00</th>
                <th>10:01 - 11:00</th>
                <th>11:01 - 12:00</th>
                <th>12:01 - 13:00</th>
                <th>13:01 - 14:00</th>
                <!-- <th style="background-color: gray; width: 25px"></th> -->
                <th>14:01 - 15:00</th>
                <th>15:01 - 16:00</th>
                <th>16:01 - 17:00</th>
                <th>17:01 - 18:00</th>
                <th>18:01 - 19:00</th>
                <th>19:01 - 20:00</th>
                <th>20:01 - 21:00</th>
                <th style="width: 50px;font-weight:bold">Total Hrs</th>
                <th style="width: 50px;font-weight:bold">Avlbl Hrs</th>
                 <th style="width: 10px">Collaborators</th>
            </tr>
        </thead>
        <tbody>
        
         	 <c:if test="${not empty userMap}">
             <c:forEach var="user" items="${userMap}" varStatus="userIndex"> 
             	<c:if test="${user.role != 'SUPERADMIN'}">
		             <tr class="editable" id="${user.id}-row"">
		            	<td><input name="assignee" type="radio" id="${user.id}-select" value="${user.id}" path="assignee"></input></td>
		                <td class="nonmarkable" id="${user.id}-name">${user.firstName} ${user.lastName}</td>
		                <td class="markable" id="${user.id}-8" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-9" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-10" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-11" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-12" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <!-- <td class="nonmarkable" style="background-color: gray; width: 25px" cellmarked="false" celleditable="false" isassigned="false"></td> -->
		                <td class="markable" id="${user.id}-13" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-14" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-15" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-16" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-17" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-18" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-19" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="markable" id="${user.id}-20" cellcolor="" cellmarked="false" celleditable="true" modified="false" isassigned="false"></td>
		                <td class="nonmarkable" dynamicvalue="0" style="font-weight: bold; font-size: 16px;" id="${user.id}-totalHours">0</td>
		                <td class="nonmarkable" dynamicvalue="8" style="font-weight: bold; font-size: 18px; color: green" id="${user.id}-availabeHours">8</td>
		                <td class="nonmarkable"><input path="helper" name="helper" id="${user.id}-helper" type="checkbox" value="${user.id}"></input></td>
		            </tr>
            	</c:if>
             </c:forEach>
             </c:if>
        </tbody>
    </table>
		</div>
	</div>
</div>
</form:form>
    <!-- body end -->
     <div class="row">
	<div class="col-md-12"  style="margin-top:10px;margin-bottom: 15px;">
	<table class="legend">
		<tr>
			<td>
				<table>
					<tr>
						<td><span style="background-color: rgb(250, 128, 114);">P</span></td>
						<td>Project Task</td>
					</tr>
					<tr>
						<td><span style="background-color: rgb(250, 128, 114);">PH</span></td>
						<td>Project Collaborator Task</td>
					</tr>
				</table>
			</td>
			<td>
				<table>
					<tr>
						<td><span style="background-color: rgb(250, 128, 114);">PR</span></td>
						<td>Project Review Task</td>
					</tr>
					<tr>
						<td><span style="background-color: rgb(250, 128, 114);">PRH</span></td>
						<td>Project Review Task Collaborator</td>
					</tr>
					
				</table>
			</td>

			<td>
				<table>
					<tr>
						<td><span style="background-color: rgb(250, 128, 114);">PET</span></td>
						<td>Project Extra Time</td>
					</tr>
					<tr>
						<td><span style="background-color: rgb(128, 128, 128);">L</span></td>
						<td>Leave</td>
					</tr>
				</table>
			</td>
			<td>
				<table>
					<tr>
						<td><span style="background-color: rgb(0, 139, 139);">M</span></td>
						<td>Meeting</td>
					</tr>
					<tr>
						<td><span style="background-color: rgb(0, 139, 139);">OTH</span></td>
						<td>Other Non Billable</td>
					</tr>
					
				</table>
			</td>
			
			<td>
				<table>
					<tr>
						<td><span style="background-color: green;"></span></td>
						<td>Completed</td>
					</tr>
					<tr>
						<td><span style="background-color: red;"></span></td>
						<td>Task Overlapping</td>
					</tr>
				</table>
			</td>
			<td style="vertical-align: top">
				<table>
					<tr>
						<td><span style="background-color: blue;"></span></td>
						<td>In Progress</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</div>
    </div>
</div>               
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %> 
  <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min.js?v=${fsVersionNumber}"></script> 
	<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script>

var jvalidate = $("#createTaskFormId").validate({
	 ignore: ".ignorevalidation",
   rules: {                                            
	taskSelectId: {
            required: true,
    },
    projectId: {
        required: true
    },
    description: {
    	 required: true
    }
  }
});

function doAjaxCreateTaskForm() {
	$('#createTaskFormId').submit();
}

$('#createTaskFormId').ajaxForm(function(response) {
	var responseJson = JSON.parse(response);
	if (responseJson.status) {
		window.location.reload(true);
	}
});

$("#projectId").on("change",function(){
	$('.taskIdOption').each(function () { $(this).hide(); });
	$("#taskSelectId").prop("selectedIndex",0);
	famstacklog($(this).val());	
	$('.taskIdOption[filter^='+$(this).val()+']').each(function () { $(this).show(); });
});

$("#taskSelectId").on("change",function(){
	if ($("#taskSelectId").val() != "0") {
		var taskId = $("#taskSelectId").val();
		loadTaskDetails(taskId, $("#"+taskId).attr("projectId"));
	}
});

var startProjectTime = '2015/01/01 09:00';
var completionProjectTime = '2020/01/01 20:00';

var loadTaskDetails = function(taskId, projectId){
	startProjectTime =$("#"+projectId+"startTime").val();
	completionProjectTime = $("#"+projectId+"completionTime").val();
	$("#taskId").val($("#taskSelectId").val());
    $("#estStartTime").val($("#"+taskId+"startTime").val());
 	$("#unassignedDuration").html($("#"+projectId+"unAssignedDuration").val());
 	$("#projectDuration").html($("#"+projectId+"duration").val());
 	$("#taskDuration").html($("#"+taskId+"duration").val());
 	$("#description").val($("#"+taskId+"description").val());
 	var assigneeId = $("#"+taskId+"assignee").val();
	var helpers = $("#"+taskId+"helper").val();
	
	$("#startTime").val($("#"+taskId+"startTime").val());
	$("#assignee").val(assigneeId);
	$("#helper").val(helpers);
	$("#name").val($("#"+taskId+"name").val());

	//createTaskDurationList(parseInt($("#"+projectId+"unAssignedDuration").val())+parseInt($("#"+taskId+"duration").val()));
    //$("#duration").prop('selectedIndex', parseInt($("#"+taskId+"duration").val()));
    $("#duration").val(parseInt($("#"+taskId+"duration").val()));
   // $("#duration").selectpicker('refresh');
    $("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), parseInt($("#"+taskId+"duration").val())));
    $("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));
	$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
	
	if (assigneeId != "" && assigneeId != 0) {
		 $("#"+assigneeId+"-select").click();
		 famstacklog("helpers" + helpers);
		 if (helpers != "") {
			 famstacklog("inside helper" + helpers);
			 var helperIds = helpers.replace("[","").replace("]","").split(",");
			 for (var index = 0; index<helperIds.length; index++) {
				 famstacklog("inside ids" + helperIds[index]);
				 $("#"+helperIds[index].trim()+"-helper").click();
			 }
		 }
	}
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
		fillTableFromJson();
	   }, function(e) {
	   });
}

getAssignJsonData();

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
	famstacklog("userId :"+userId+"startTimeHour :" + startTimeHour +"duration :"+ duration);
	
	for (var index = 0; index < duration; index++) {
		if(startTimeHour ==  breakTime){
			//startTimeHour++;
		}
		famstacklog("index :" + index);
		famstacklog("startTimeHour :" + startTimeHour);
		var getCell = $("#"+userId+"-"+startTimeHour);
		$("#estStartTime").css("border", "1px solid #D5D5D5");
		if (reset){
			famstacklog("reset");
			if($(getCell).attr("isassigned") == "true"){
				return;
			}
			var cellBackGroundColor =$(getCell).attr("cellcolor");
			$(getCell).css("background-color", cellBackGroundColor);
			$(getCell).attr("cellmarked",false);
			$(getCell).attr("modified",false);
		} else {
			famstacklog("helper" + $(getCell).attr("isassigned"));
			var cellBackGroundColor = $(getCell).css("background-color");
			famstacklog("cellBackGroundColor0 :" + cellBackGroundColor);
			if($(getCell).attr("isassigned") == "true"){
				if (helper){
					famstacklog("error- helper");
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
			famstacklog("cellBackGroundColor1 :" + cellBackGroundColor);
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
	/* $(this).find('.xdsoft_date.xdsoft_weekend')
	.addClass('xdsoft_disabled'); */
	},
	dateFormat: 'yyyy-mm-dd HH:mm',
	defaultDate: new Date(),
	minDate:startProjectTime, // yesterday is minimum date
	maxDate:completionProjectTime,
	allowTimes:['08:00','09:00','10:00','11:00','12:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00'],
	onChangeDateTime:dateDisplayLogic,
	onShow:dateDisplayLogic
});
	
$("#estStartTime").on("change",function(){
	resetAssignTable();
	fillTableFromJson();
	var startProjectDate = new Date(startProjectTime);
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
		famstacklog("select box id" + id);
		fillAssignTabledBasedOnDate(id);
	}
	
	$("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));


});

$("#estStartTime").val(getTodayStartDateTime(new Date()));

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
	$("#currentAssignmentDate").html(getTodayDate(new Date()));
	
	$('#employeeListForTaskTable').on( 'draw.dt', function () {
			resetAssignTable();
			fillTableFromJson();
		} );
	
	});

var cellSelectCount = 0;
var breakTime = 13;
var checkNextAndPreviousMarked = function(thisVarId, checkOrUnchek){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	famstacklog("time" + time);
	famstacklog("userId" + userId);
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
	
	famstacklog("cellSelectCount" + cellSelectCount);
	famstacklog("preMarked" + preMarked);
	famstacklog("nextMarked" + nextMarked);
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
			//tmpTime--;
		}
		famstacklog("pre cell id :" + userId+"-"+tmpTime);
		var celleditable = $("#"+userId+"-"+tmpTime).attr("celleditable");
		var cellmarked	= $("#"+userId+"-"+tmpTime).attr("cellmarked");
		if (cellmarked == 'true' && celleditable == 'true') {
			preMarked = true;
		}
	}
	famstacklog("preMarked" + preMarked);
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
			//tmpTime++;
		}
		famstacklog("next cell id :" + userId+"-"+tmpTime);
		var celleditable = $("#"+userId+"-"+tmpTime).attr("celleditable");
		var cellmarked	= $("#"+userId+"-"+tmpTime).attr("cellmarked");
		if (cellmarked == 'true' && celleditable == 'true') {
			nextMarked = true;
		}
	}
	famstacklog("nextMarked" + nextMarked);
	return nextMarked;
}


$("table#employeeListForTaskTable").on("click", "tr.editable td.markable", function(){
	
	var maxDuration = $('#duration > option').length;
	famstacklog("maxDuration" + maxDuration);
	var cellId = this.id;
	var userId  = cellId.split("-")[0];
	var hourId = cellId.split("-")[1];
	
	var cellBackGroundColor = $(this).css("background-color");
	famstacklog(cellBackGroundColor);
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
		famstacklog(cellColor);
		$(this).css("background-color", cellColor);
		decreaseTotalHours(userId);
		cellSelectCount--;
		$(this).attr("cellmarked",false);
		$(this).attr("modified",false);
		
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
		famstacklog("housrid:" + hourId);
		famstacklog(" startProjectDate.getHours():" +  startProjectDate.getHours());
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
		$(this).removeAttr("data-taskId");
		
		$(this).html("");
	});
	
	$('table#employeeListForTaskTable td[dynamicvalue="0"]').each(function () {
		$(this).html("0");
	});
	
	$('table#employeeListForTaskTable td[dynamicvalue="8"]').each(function () {
		famstacklog("reset 8");
		$(this).html("8");
	});
}


var increaseTotalHours = function(userId){
	famstacklog($("#"+userId+"-totalHours").html());
	var totalHours = parseInt($("#"+userId+"-totalHours").html()) + 1;
	var availableHours = parseInt($("#"+userId+"-availabeHours").html()) - 1;
	$("#"+userId+"-totalHours").html(""+totalHours);
	$("#"+userId+"-availabeHours").html(""+availableHours);
	
	if (availableHours < 0){
		$("#"+userId+"-availabeHours").css("color", "red");
		//$('input:radio[name=assignee]').each(function () { $(this).prop('checked', false); });
	} else {
		$("#"+userId+"-availabeHours").css("color", "green");
	}
	famstacklog("increase total hours:" +totalHours);
}

var decreaseTotalHours = function(userId){
	famstacklog($("#"+userId+"-totalHours").html());
	var totalHours = parseInt($("#"+userId+"-totalHours").html()) - 1;
	var availableHours = parseInt($("#"+userId+"-availabeHours").html()) + 1;
	$("#"+userId+"-totalHours").html(""+totalHours);
	$("#"+userId+"-availabeHours").html(""+availableHours);
	
	if (availableHours <= 0){
		$("#"+userId+"-availabeHours").css("color", "red");
	} else {
		$("#"+userId+"-availabeHours").css("color", "green");
	}
	famstacklog("increase total hours:" +totalHours);
}

var fillTableFromJson = function(){
	famstacklog("jsonAssignData :" + jsonAssignData);
	try{	
	$.each(JSON.parse(jsonAssignData), function(idx, elem){
		if (getTodayDate(new Date($("#estStartTime").val())) == elem.dateId) {
			
			var isCompleted = elem.actualEndTime == null ?false:true;
			var isInprogress = elem.actualStartTime == null?false:true;

			var starthour = parseInt(elem.startHour);
			var durationmins = parseInt(elem.durationInMinutes);
			var startmins = parseInt(elem.startMins);
	
			var iteration = 0;
			var duration = (durationmins * 100) /60;
			
			famstacklog("duration :" + duration);
			famstacklog("starthour :" + starthour);
			famstacklog("startmins :" + startmins);
			while (duration > 0 ) {
				var marginLeft = 0;
				var style = "float:left;";
				if (iteration == 0){
					marginLeft = (startmins*100)/60;
					if ((duration + marginLeft) >= 100) {
						firstWidth = 100 - marginLeft;
						duration-=firstWidth;
					} else {
						firstWidth = duration;
						duration =0;
					}
					
					style +="width:"+firstWidth+"%;margin-left:"+marginLeft+"%;";
				} else {
					
					if (duration >= 100) {
						style +="width:100%;margin-left:0px;";
						duration-=100;
					} else {
						style +="width:"+duration+"%;";
						duration =0;
					}
					
				}
				
				famstacklog("style :" + style);
				
				if (starthour == breakTime) {
					//starthour++;
				}
				var cellId = $("#"+elem.userId+"-"+starthour);
				famstacklog(cellId);
				
				if($("#taskId").val() != "" && elem.taskId == $("#taskId").val() &&  elem.taskId != 0) {
					famstacklog(elem.taskId);
					famstacklog($("#taskId").val());
				} else {
					var isOverLapping = false;
					if ($(cellId).attr("cellmarked") == 'true') {
						isOverLapping = true;
					}
					
					$(cellId).attr("celleditable", false);
					$(cellId).attr("isassigned",true);
					$(cellId).attr("cellmarked",true);
					$(cellId).attr("modified",false);
					$(cellId).css("text-align", "center");
					

					var cellStausColor = isInprogress ? "rgb(250, 128, 114)" :  "rgb(0, 0, 255)"; 
					cellStausColor = isCompleted ? "rgb(0, 128, 0)" :cellStausColor;
					
					var cellTitleTaskName = "Task Id :" + elem.taskId + ", Activity Id:" + elem.taskActivityId +", Task Name :" +  elem.taskName;
					var cellTaskType = "";
					if (elem.userTaskType == "PROJECT") {
						cellTaskType = "P";
					} else if (elem.userTaskType == "PROJECT_HELPER") {
						cellTaskType = "PH";
					} else if (elem.userTaskType == "LEAVE") {
						cellStausColor = "rgb(128, 128, 128)";
						cellTaskType = "L";
					} else if (elem.userTaskType == "MEETING") {
						cellStausColor = "rgb(0, 139, 139)";
						cellTaskType = "M";
					}else if (elem.userTaskType == "PROJECT_HELPER_REVIEW") {
						cellTaskType = "PRH";
					}else if (elem.userTaskType == "PROJECT_REVIEW") {
						cellTaskType = "PR";
					}else if (elem.userTaskType == "PROJECT_PARTIAL") {
						cellTaskType = "PRA";
					}else if (elem.userTaskType == "EXTRATIME") {
						cellTaskType = "PET";
					} else if (elem.userTaskType == "OTHER") {
						cellStausColor = "rgb(0, 139, 139)";
						cellTaskType = "OTH";
					}
					
					if ($(cellId).attr("data-taskId") == elem.taskId) {
						$(cellId).find("span").css("margin-left","");
						$(cellId).append('<span title="'+cellTitleTaskName+'" style="'+style+'height:41px;padding-top:10px;background-color:'+cellStausColor+'">'+cellTaskType+'</span>');
					} else {
						if (isOverLapping) {
							cellStausColor = "rgb(235, 0, 0)";
							$(cellId).css("background-color","red");
						}
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="'+style+'height:41px;padding-top:10px;background-color:'+cellStausColor+'">'+cellTaskType+'</span>');
					}
					$(cellId).attr("data-taskId",elem.taskId);
					increaseTotalHours(elem.userId);
				}
				
				iteration++;
				starthour++;
			}
			
		}
	});
	}catch(err){
		
	}
}
</script>
