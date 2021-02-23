<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>    
<c:set var="futureHourCaptureDisabled" value="${applicationScope.applicationConfiguraion.futureHourCaptureDisabled}"/>
<c:set var="fsVersionNumber" value="${applicationScope.applicationConfiguraion.fsVersionNumber}"/>
<c:set var="staticUnbilledEnabled" value="${applicationScope.applicationConfiguraion.staticNonBillableEnabled}"/>

<link rel="stylesheet" type="text/css" href="${fn:escapeXml(css)}/pages/myTaskList.css?v=${fsVersionNumber}"/>
<style>
</style>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Task Activities</li>
 </ul>
  <script>
 var futureHourCaptureDisabled = ${futureHourCaptureDisabled};
 var newFieldsEnabled = ${staticUnbilledEnabled};
 </script>
 <!-- END BREADCRUMB -->  
<div class="content-frame" ng-app="mytasks" style="min-height: 500px; margin-bottom: 50px">     
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-tasks"></span> Task Activities</h2>
        </div>                                                
    </div> 
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
									<input type="text" class="form-control" id="taskActivitySearchId" placeholder="Search for a task activity">
								</div>
							</div>
							 <div class="col-md-2">
							  <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
								<select id="taskActivityAssigneeId" name="taskAssigneeId" class="form-control select" data-live-search="true">
									<option value="0">All</option>
										 <c:if test="${not empty userMap}">
											<c:forEach var="user" items="${userMap}">
												 <c:if test="${currentUser.id eq user.id}">
												 	<option selected="selected" value="userId${user.id}">${user.firstName}</option>
												 </c:if>
												  <c:if test="${currentUser.id ne user.id}">
												  <option value="userId${user.id}">${user.firstName}</option>
												  </c:if>
							  				</c:forEach>
							  			</c:if>
									</select>
								</c:if>
								<c:if test="${!(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
									<input type="hidden" id="taskActivityAssigneeId" value="${currentUser.id}">
								</c:if>
							</div>
							<div class="col-md-1"> 
								<div class="form-group">
                                <%-- <div class="col-md-12">                                            
                                       <select id="taskFilterDayId" name="taskFilterDayId" class="form-control select" data-live-search="true">
											<c:if test="${empty param.dayfilter}">
												<c:set var="dayfilterParam" value="10"/>
											</c:if>
											<c:if test="${not empty param.dayfilter}">
												<c:set var="dayfilterParam" value="${param.dayfilter}"/>
											</c:if>
											<c:forEach begin="1" end="365" varStatus="loop" step="1">
												<c:if test="${dayfilterParam eq loop.current}">
                                        		<option value="${loop.current}" selected="selected">Last ${loop.current} Days Activity</option>
                                        		</c:if>
                                        		<c:if test="${dayfilterParam ne loop.current}">
                                        		<option value="${loop.current}">Last ${loop.current} Days Activity</option>
                                        		</c:if>
                                 			</c:forEach>
										</select>
                                </div> --%>
                     	 		<input type='text' class="form-control taskActivityMonthSelector" id='' autocomplete="off"/>
                            </div>
							</div>
							<div class="col-md-1"> 
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
		<div class="inner no-pad-t userTaskActivites">
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
</div>

<!-- project create modal end -->  

<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>     
 <script type='text/javascript' src="${js}/plugins/datepicker/bootstrap-datetimepicker_new.min.js?v=${fsVersionNumber}"></script>       
<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/unbilledtask.js?version=2.22&v=${fsVersionNumber}"></script>
<script>

$(document).ready(function(){
	reloadTaskActivities();
});

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
	filterTaskActivities(assigneeSelectionId);
});

function filterTaskActivities(assigneeSelectionId){
	if (assigneeSelectionId != "") {
		$(".taskact-item").hide();
		$(".taskact-item." + assigneeSelectionId).show();
		activeUserId=assigneeSelectionId;
	} else {
		$(".taskact-item").show();
		activeUserId="";
	}
	performSearch();
}


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

$("#taskActivityAssigneeId").on("change", function(){
	reloadTaskActivities();
});

$(".taskActivityMonthSelector").on("change", function(){
	reloadTaskActivities();
});
function refreshCalendar(){
	reloadTaskActivities();
}
$('.taskActivityMonthSelector').datetimepicker({ 
	sideBySide: true, format: 'MMM-YYYY',
	useCurrent: true,
	defaultDate:new Date()
	}).on('dp.change', function(e) {
		reloadTaskActivities();
	});
</script>
