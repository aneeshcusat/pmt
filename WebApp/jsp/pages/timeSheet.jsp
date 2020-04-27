<%@include file="includes/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/gentleSelect/jquery-gentleSelect.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/select2/select2.min.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/timesheet.css?version=3.5&v=${fsVersionNumber}"/>

<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<c:set var="hasFullPermissionVar" value="${(((currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && currentUserGroupId != 1018) || currentUser.userRole == 'SUPERADMIN')}"/>
<c:set var="currentUserId" value="${applicationScope.applicationConfiguraion.currentUserId}"/>
<c:set var="appConfigMap" value="${applicationScope.applicationConfiguraion.appConfigMap}"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="weeklyLogTaskEnabled" value='${applicationScope.applicationConfiguraion.weeklyTimeLogNewTaskEnabled}'/>
<c:set var="weekTLDisableMonthEnabled" value='${applicationScope.applicationConfiguraion.weekTLDisableMonthEnabled}'/>

<script type="text/javascript">
var taskCreateEnabled = ${weeklyLogTaskEnabled};
var weekTLDisableMonthEnabled = ${weekTLDisableMonthEnabled};
</script>

 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Time sheet</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-clock-o"></span> Weekly Time Log</h2>
        </div>  
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
        <div class="row">
        <div class="col-md-12" style="padding-top: 10px;padding-bottom: 10px">
        	<div class="col-md-3"><span style="font-size: 12px; font-weight: bold">Log hours on</span><input readonly="readonly" type='text' class="weekSelector"/></div>
        	<div class="col-md-5"><span class="taskLogInfo"><i class="fa fa-times-circle hide errorMsg" aria-hidden="true"></i><i class="fa fa-check-circle-o  hide successMsg" aria-hidden="true"></i> <span class="taskLogInfoMsg"></span></span></div>
        	<div class="col-md-3  text-right">
        	
        	<a href="javascript:moveToPreviousWeek();" title="Previous Week"><span class="fa fa-angle-left fa-2x" style="font-weight: bold"></span></a>
        	<a href="javascript:moveToCurrentWeek();" title="Current Week"><span style="padding-left: 15px;" class="fa fa-home fa-2x"></span></a>
        	<a href="javascript:moveToNextWeek();" title="Next Week"><span style="padding-left: 15px;font-weight: bold" class="fa fa-angle-right  fa-2x"></span></a>
        	
        	</div>
        	<div class="col-md-1">
        		<c:if  test="${hasFullPermissionVar eq 'true'}">
        			<input class="currentUserId" type="hidden" value="0"/>
        		</c:if>
        		
        		<c:if  test="${hasFullPermissionVar eq 'false'}">
        			<input class="currentUserId" type="hidden" value="${currentUserId}"/>
        		</c:if>
        	</div>
        </div>
            <div class="col-md-12">
            
            <table style="margin-bottom: 0px;" class="table table-responsive table-hover weeklyTimeLogTable">
			  <thead>
			        <tr>
			        	<th width="10%">User</th>
			        	<th width="6%">Type</th>
			        	<th width="15%">Project</th>
			        	<th width="12%">Task</th>
			        	<th width="9%">Comments</th>
			        	<th width="6%"><span class="day1"></span></th>
			        	<th width="6%"><span class="day2"></span></th>
			        	<th width="6%"><span class="day3"></span></th>
			        	<th width="6%"><span class="day4"></span> </th>
			        	<th width="6%"><span class="day5"></span></th>
			        	<th width="6%"><span class="day6"></span></th>
			        	<th width="6%"><span class="day7"></span></th>
			        	<th width="8%">Total</th>
			        </tr>
			    </thead>
			    <tbody class="projectUpdateTimeBody">
				    <jsp:include page="includes/timeSheetItem.jsp">
				    	<jsp:param name="hasFullPermission" value="${hasFullPermissionVar}"/>
				    </jsp:include>
			    </tbody>
				<tfoot>
					<tr class="projectDetailsFooterRow">
						<td colspan="12" style="background: #f1f5f9;"><a class="addNewRowLink" style="font-size: 12px;color: #000" href="javascript:cloneProjectUpdateTimeRow();">+Add new row</a></td>
						<td style="background: #f1f5f9;"><span class="weeklyTotal" style="font-size: 12px;font-weight:bold;color: #000;padding-left: 5px">00:00</span></td>
					</tr>
					<tr>
						<td colspan="13" style="background: #fff; text-align: center;">
							<button type="button" class="btn btn-secondary" onclick="clearWeeklyTimeLogTable();">Clear</button>
						<button type="button" id="weeklyTimeLogSaveButton" onclick="saveCurrentProjectWeekData(true);"
							class="btn btn-info hide">
							<span>Save &amp; Clear</span>
						</button>
						
						<button type="button" id="weeklyTimeLogSaveNextButton" onclick="saveCurrentProjectWeekDataAndMove()"
							class="btn btn-info hide">
							<span>Save &amp; Next Week</span>
						</button>
						</td>
					</tr>
				</tfoot>
			</table>
            </div>
        </div>
         <div class="row loggedTimeDiv hide">
        <div class="col-md-12">
        	<div class="col-md-10"><span style="font-size: 12px; font-weight: bold">Logged hours for this week</span></div>
        	<div class="col-md-2">
				<c:if  test="${hasFullPermissionVar eq 'true'}">
					<select class="form-control loggedUserIdSelector">
					<option value="">All</option>
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
        </div>
        <div class="col-md-12" style="padding-top: 10px;padding-bottom: 10px">
        	<table class="table table-responsive">
			  <thead>
			        <tr>
			        	<th width="10%">User</th>
			        	<th width="6%">Type</th>
			        	<th width="15%">Project</th>
			        	<th width="12%">Task</th>
			        	<th width="9%">Comments</th>
			        	<th width="6%"><span class="day1"></span></th>
			        	<th width="6%"><span class="day2"></span></th>
			        	<th width="6%"><span class="day3"></span></th>
			        	<th width="6%"><span class="day4"></span> </th>
			        	<th width="6%"><span class="day5"></span></th>
			        	<th width="6%"><span class="day6"></span></th>
			        	<th width="6%"><span class="day7"></span></th>
			        	<th width="8%">Total</th>
			        </tr>
			    </thead>
			    <tbody class="loggedDataTableBody">
					<tr class="loggedData hide visibleTask">
						<td class="userName"></td>
						<td class="type"></td>
						<td class="project"></td>
						<td class="task"></td>
						<td class="taskComments"></td>
						<td><span class="loggedDay1 bold"></span></td>
						<td><span class="loggedDay2 bold"></span></td>
						<td><span class="loggedDay3 bold"></span></td>
						<td><span class="loggedDay4 bold"></span> </td>
						<td><span class="loggedDay5 bold"></span></td>
						<td><span class="loggedDay6 bold"></span></td>
						<td><span class="loggedDay7 bold"></span></td>
						<td><span class="weekdayTotal bold"></span></td>
					</tr>
				</tbody>
				<tfoot>
					<tr class="projectLoggedDetailsFooterRow">
						<td colspan="12" style="background: #f1f5f9;"></td>
						<td style="background: #f1f5f9;"><span class="weeklyTotal">00:00</span></td>
					</tr>
				</tfoot>
			  </table>
        </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>   
 <script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>         
 <script type='text/javascript' src="${js}/plugins/bootstrap/bootstrap-datepicker.js?v=${fsVersionNumber}"></script>  
 <script type="text/javascript"
	src="${js}/plugins/autocomplete/jquery.autocomplete.js?v=${fsVersionNumber}"></script> 
<script type="text/javascript"
	src="${js}/plugins/select2/select2.full.min.js?v=${fsVersionNumber}"></script> 
<script type="text/javascript"
	src="${js}/famstack.timesheet.js?version=4.0&v=${fsVersionNumber}"></script> 
