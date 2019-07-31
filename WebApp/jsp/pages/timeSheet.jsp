<%@include file="includes/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/gentleSelect/jquery-gentleSelect.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/select2/select2.min.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/timesheet.css?v=${fsVersionNumber}"/>

<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>

<script type="text/javascript">
<c:if test="${currentUserGroupId != 1018}">
	var taskCreateEnabled = true;
</c:if>
<c:if test="${currentUserGroupId == 1018}">
	var taskCreateEnabled = false;
</c:if>
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
        	<div class="col-md-3"><span style="font-size: 12px; font-weight: bold">Log hours on</span><input readonly="true" type='text' class="weekSelector"/></div>
        	<div class="col-md-8  text-right">
        	
        	<a href="javascript:moveToPreviousWeek();" title="Previous Week"><span class="fa fa-angle-left fa-2x" style="font-weight: bold"></span></a>
        	<a href="javascript:moveToCurrentWeek();" title="Current Week"><span style="padding-left: 15px;" class="fa fa-home fa-2x"></span></a>
        	<a href="javascript:moveToNextWeek();" title="Next Week"><span style="padding-left: 15px;font-weight: bold" class="fa fa-angle-right  fa-2x"></span></a>
        	
        	</div>
        	<div class="col-md-1">
        	</div>
        </div>
            <div class="col-md-12">
            
            <table class="table table-responsive table-hover weeklyTimeLogTable">
			  <thead>
			        <tr>
			        	<th width="10%">User</th>
			        	<th width="5%">Type</th>
			        	<th width="15%">Project</th>
			        	<th width="15%">Task</th>
			        	<th width="7%"><span class="day1"></span></th>
			        	<th width="7%"><span class="day2"></span></th>
			        	<th width="7%"><span class="day3"></span></th>
			        	<th width="7%"><span class="day4"></span> </th>
			        	<th width="7%"><span class="day5"></span></th>
			        	<th width="7%"><span class="day6"></span></th>
			        	<th width="7%"><span class="day7"></span></th>
			        	<th width="5%">Total</th>
			        </tr>
			    </thead>
			    <tbody class="projectUpdateTimeBody">
				    <jsp:include page="includes/timeSheetItem.jsp"/>
			    </tbody>
				<tfoot>
					<tr class="projectDetailsFooterRow">
						<td colspan="11" style="background: #f1f5f9;"><a class="addNewRowLink" style="font-size: 12px;color: #000" href="javascript:cloneProjectUpdateTimeRow();">+Add new row</a></td>
						<td style="background: #f1f5f9;"><span class="weeklyTotal" style="font-size: 12px;font-weight:bold;color: #000">00:00</span></td>
					</tr>
					<tr>
						<td colspan="12" style="background: #fff; text-align: center;">
							<button type="button" class="btn btn-secondary" onclick="clearWeeklyTimeLogTable();">Clear</button>
						<button type="button" id="weeklyTimeLogSaveButton" onclick="saveCurrentProjectWeekData(true);"
							class="btn btn-info">
							<span>Save & Clear</span>
						</button>
						
						<button type="button" id="weeklyTimeLogSaveNextButton" onclick="saveCurrentProjectWeekDataAndMove()"
							class="btn btn-info">
							<span>Save & Next Week</span>
						</button>
						</td>
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
	src="${js}/famstack.timesheet.js?version=3.1&v=${fsVersionNumber}"></script> 
