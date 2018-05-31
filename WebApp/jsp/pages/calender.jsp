<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Calender</li>
 </ul>
<style>
.disabled .fc-day-content {
    background-color: #123959;
    color: #FFFFFF;
    cursor: default;
}
#unbillableTaskCreationModal .modal-dialog {
	width: 50%;
}

.autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
.autocomplete-group { padding: 2px 5px; }
.autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-calendar"></span> Calendar</h2>
        </div>  
        <div class="pull-right col-md-10">
        <div class="col-md-6">
        </div>
         <div class="col-md-3">
							  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
								<select id="taskAssigneeId" name="taskAssigneeId" class="form-control select" data-live-search="true">
									<option value="-1">All</option>
										 <c:if test="${not empty userMap}">
											<c:forEach var="user" items="${userMap}">
												 <c:if test="${currentUser.id eq user.id}">
												 	<option selected="selected" value="${user.id}">${user.firstName}</option>
												 </c:if>
												  <c:if test="${currentUser.id ne user.id}">
												  <option value="${user.id}">${user.firstName}</option>
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
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class=" padding-bottom-0">
        
        <div class="row">
            <div class="col-md-12">
                <div id="alert_holder"></div>
                <div class="calendar">                                
                    <div id="calendar"></div>                            
                </div>
            </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
    
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
<!-- project create modal end -->  

<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>            
 <script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
 
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js"></script>
<script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js"></script>
<script>

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
    
	var taskType = $("#taskType").val();
	var taskActCategory = "";
	
	if (taskType == "LEAVE"){
		taskActCategory = "Leave";
		
	} else if (taskType == "MEETING"){
		taskActCategory = "Meeting";
	} else {
		taskActCategory = taskType;
		taskType = "OTHER";
	}
	
	var dataString = {userId:$("#userId").val(),type:taskType,taskActCategory:taskActCategory,startDate:startDate,endDate:endDate,comments:$("#taskStartComments").val()};
	doAjaxRequest("POST", "${applicationHome}/createNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
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

$("#taskAssigneeId").on("change",function(){
	var userId = $("#taskAssigneeId").val();
	var events = {
	        url: "getAjaxFullcalendar",
	        type: 'GET',
	        data: {
	        	userId: userId
	        }
	    }
	$('#calendar').fullCalendar('removeEventSource', events);
    $('#calendar').fullCalendar('addEventSource', events);
    $('#calendar').fullCalendar('refetchEvents');
});

</script>
