<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="futureHourCaptureDisabled" value="${applicationScope.applicationConfiguraion.futureHourCaptureDisabled}"/>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="staticUnbilledEnabled" value="${applicationScope.applicationConfiguraion.staticNonBillableEnabled}"/>

 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Calender</li>
 </ul>
 <script>
 var futureHourCaptureDisabled = ${futureHourCaptureDisabled};
 var newFieldsEnabled = ${staticUnbilledEnabled};
 </script>
<style>

.colorRed {
	background-color: red !important;
}
.calenderEventPopOver{
	font-size: 10pt;
}
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
					<h4 class="modal-title" id="unbilledModelTitle">Create Non-billable time</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/unbillableTaskCreationModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary createUnbillableCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="taskCreate" onclick="createUnbillableTask()"
						class="btn btn-primary taskActCreateBtn" style="display: none">
						<span class="nonBillableTaskCreateText" >Create</span>
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
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
 
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js?v=${fsVersionNumber}"></script>
<script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/unbilledtask.js?version=2.24&v=${fsVersionNumber}"></script>
<script>
var refreshCalendar = function() {
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
}

$("#taskAssigneeId").on("change",function(){
	refreshCalendar();
});

</script>
