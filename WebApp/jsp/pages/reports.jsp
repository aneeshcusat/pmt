<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="includes/header.jsp" %>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>

<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/reports.css?version=4.1&v=${fsVersionNumber}"/>

<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-table"></span>&nbsp;Reports</h2>
        </div>  
        
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
        <div class="row">
        <div class="col-md-12" style="padding-top: 10px;padding-bottom: 10px">
        	<div class="col-md-1">
        	 <c:if test="${currentUser.userRole == 'SUPERADMIN'}">
        	 
    		<select class="form-control" data-live-search="true" id="userGroupSelection">
			    <option value="">- select -</option>
			    <c:forEach var="userGroup" items="${userGroupMap}" varStatus="userGroupIndex"> 
			      <option <c:if test="${currentUserGroupId == userGroup.value.userGroupId}">selected="selected"</c:if> value="${userGroup.value.userGroupId}">${userGroup.value.name}</option>
			    </c:forEach>
			</select>
			
    		</c:if>
    		</div>
    		<div class="col-md-2">
        	<select class="form-control autoReportType">
                <option value="USER_SITE_ACTIVITY">User activity</option>
                <option value="USER_UTILIZATION">User Utilization</option>

                <option value="WEEKLY_PO_ESTIMATION">PO Estimation</option>
                <option value="WEEKLY_PROJECT_HOURS">Weekly Project Hours</option>
                
                <option value="TEAM_UTILIZATION_CHART">Team Utilization Chart</option>
          	</select>
          	</div>
          	<div class="col-md-1">
          	<select class="form-control autoReportDuration">
          		<option value="DAILY">Daily</option>
          		<option value="WEEKLY">Weekly</option>
          		<option value="MONTHLY">Monthly</option>
          	</select>
          	</div>
        	<div class="col-md-2">
        		<span class="fa fa-angle-down fa-lg" style="font-weight: bold"></span>
        		<input readonly="readonly" type='text' class="dailySelector"/>
        		<input readonly="readonly" type='text' class="weekSelector hide"/>
        		<input readonly="readonly" type='text' class="monthSelector hide"/>
        	</div>
        	<div class="col-md-1 " style="text-align: center;">
	        	<a href="javascript:moveToPrevious();" style="float: left" title="Previous"><span class="fa fa-angle-left fa-2x" style="font-weight: bold"></span></a>
	        	<a href="javascript:moveToCurrent();" title="Current"><span style="padding-left: 15px;" class="fa fa-home fa-2x"></span></a>
	        	<a href="javascript:moveToNext();" style="float: right" title="Next"><span style="padding-left: 15px;font-weight: bold" class="fa fa-angle-right  fa-2x"></span></a>
        	</div>
        	
        	<div class="col-md-3" style="text-align: center">
        		<input class="btn btn-info refreshButton hide" type="button" value="Refresh"/>
        		<button class="btn btn-warning clearfix exportButton hide"><span class="fa fa-file-excel-o"></span> Export</button>
        	</div>
        	<div class="col-md-2" style="text-align: center">
        	<input type="text" class="form-control hide" id="reportSearchBoxId"
												placeholder="Type here to filter.." />	
        	</div>
        </div>
        <div class="col-md-12 teamUtilizationChartDiv hide">
        	
        	<c:if test="${currentUserGroupId == '1001' || currentUserGroupId == '1004' || currentUserGroupId == '1016' || currentUserGroupId == '1007' || currentUserGroupId == '1003' || currentUserGroupId == '1010' 
        	|| currentUserGroupId == '1006' || currentUserGroupId == '1002' || currentUserGroupId == '1009' || currentUserGroupId == '1005'}">
        		<input id="teamUtilizationChartGroupIds" value="1001,1004,1016,1007,1003,1010,1006,1002,1009,1005" type="hidden"/>
			</c:if>
			
			<c:if test="${not (currentUserGroupId == '1001' || currentUserGroupId == '1004' || currentUserGroupId == '1016' || currentUserGroupId == '1007' || currentUserGroupId == '1003' || currentUserGroupId == '1010' 
        	|| currentUserGroupId == '1006' || currentUserGroupId == '1002' || currentUserGroupId == '1009' || currentUserGroupId == '1005')}">
        		<input id="teamUtilizationChartGroupIds" value="${currentUserGroupId}" type="hidden"/>
			</c:if>
			
			<div class="col-md-12" id="teamUtilizationChart" style="height: 400px;width: 100%;box-shadow: 5px 5px 23px grey;margin-bottom: 20px;"></div>
			
			
			 <div class="col-md-12 chartHeader">
			 	<div class="col-md-9"><span style="font-size: 19px;font-weight: bold;" class="teamUtlDataText"></span></div>
			 </div>
			 <div class="col-md-12" style="margin-bottom:20px">
           		<table style="margin-bottom: 0px;" class="table table-responsive table-hover table-bordered">
				     <thead>
				      <tr>
				      	 <th>Sl No</th>
				       	 <th>Team Name</th>
				       	 <th>Billable Hours (B)</th>
				       	 <th>Non Billable Hours (NB)</th>
				       	 <th>Leave Hours (L)</th>
				       	 <th>Holiday Hours (H)</th>
				       	 <th>Total Task Hours (B+NB)</th>
				       	 <th>No Of Work Days (NW)</th>
				       	 <th>No of Employees (NE)</th>
				       	 <th>Total Hours (NW*8*NE-H-L)</th>
				       	 <th>Utilization (%)</th>
				       </tr>
				    </thead>
				    <tbody class="teamUtilizationChartDataBody">
					    
				    </tbody>
					<tfoot>
					</tfoot>
				</table>
            </div>
            <div class="col-md-12 chartHeader">
			 	<div class="col-md-9"></div>
			 	<div class="col-md-1">
			 		<select class="form-control yearSelect">
			 			<option value="2020">2020</option>
                		<option value="2019">2019</option>
                		<option value="2018">2018</option>
                		<option value="2017">2017</option>
                		<option value="2016">2016</option>
          			</select>
          		</div>
			 	<div class="col-md-1">
			 		<select class="form-control displayWiseSelect">
			 			<option value="MonthWise">Month wise</option>
			 			<option value="DateWise">Date wise</option>
          			</select>
			 	</div>
			 	<div class="col-md-1">
			 		<input class="btn btn-info refreshTeamUtilizationButton" type="button" value="Refresh"/>
			 	</div>
			 </div>
             <div class="col-md-12" id="teamUtilizationComparisonChart" style="height: 400px;width: 100%;box-shadow: 5px 5px 23px grey;margin-bottom: 20px;"></div>
        </div>
		</div>
        <div class="col-md-12" style="overflow-x:auto;">
            <table style="margin-bottom: 0px;" class="table table-responsive table-hover table-bordered reportDataTable">
			     <thead class="retportDataHeader">
			       
			    </thead>
			    <tbody class="reportDataBody">
				    
			    </tbody>
				<tfoot>
				</tfoot>
			</table>
            </div>
        </div>
    </div>                    
    <!-- END CONTENT FRAME BODY -->
    
    <table class="hide reportDataTemplate" >
    	<tr class="reportDataHeader-activity">
       	<th width="50px">Sl No</th>
       	<th>Employee Name</th>
       	<th>Reporting Manager</th>
       </tr>
       
       	<tr class="reportDataHeader-utilization">
       	<th width="50px">Sl No</th>
       	<th>Employee Name</th>
       	<th>Reporting Manager</th>
       	<th>Billable Hours</th>
       	<th>Non Billable Hours</th>
       	<th>Leave or Holiday</th>
       	<th>Utilization</th>
       	<th>Total Hours</th>
       </tr>
       
       	<tr class="reportDataHeader-projecthours">
       	<th width="50px">Sl No</th>
       	<th>Team Name</th>
       	<th>Account Name</th>
       	<th>Year</th>
       	<th>Month</th>
       	<th>Week NO</th>
       	<th>Client Name</th>
       	<th>PO NO</th>
       	<th>Project Name</th>
       	<th>Start Date</th>
       	<th>End Date</th>
       	<th>Team Members</th>
       	<th>Funded/Non Funded Resource</th>
       	<th>Owner Name</th>
       	<th>Estimated Hours</th>
       	<th>Actual Hours</th>
       	<th>Leave/Holiday</th>
       </tr>
       
       <tr class="reportDataHeader-utilization-monthly">
       	<th width="50px">Sl No</th>
       	<th>Employee Name</th>
       	<th>Reporting Manager</th>
       </tr>
       
        <tr class="reportDataHeader-poestimation">
        <th><strong>Sl No</strong></th>
       	<th><strong>Team Name</strong></th>
		<th><strong>Account</strong></th>
		<th><strong>Project Name</strong></th>
		<th><strong>Project Id</strong></th>
		<th><strong>Project Code</strong></th>
		<th><strong>PO No</strong></th>
		<th><strong>SOW</strong></th>
		<th><strong>Start Date</strong></th>
		<th><strong>End Date</strong></th>
		<th><strong>Current Status</strong></th>
		<th><strong>Total Estimation Hours</strong></th>
		<th><strong>Consumed Hours</strong></th>
		<th><strong>% of Utilization</strong></th>
       </tr>
    </table>
    
    
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>   
 <script type='text/javascript' src="${js}/plugins/bootstrap/bootstrap-datepicker.js?v=${fsVersionNumber}"></script>  
 <script type='text/javascript' src="${js}/plugins/tableexport/xlsx.core.js"></script>
<script type='text/javascript' src="${js}/plugins/tableexport/FileSaver.js"></script>
<script type='text/javascript' src="${js}/plugins/tableexport/tableexport.js"></script>
 <script type="text/javascript"
	src="${js}/famstack.reports.js?version=4.3&v=${fsVersionNumber}"></script> 
  <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min_v1.js?v=${fsVersionNumber}"></script> 
<script type="text/javascript" src="${js}/plugins/datatables/dataTables.buttons.min.js?v=${fsVersionNumber}"></script>   
<script type="text/javascript" src="${js}/plugins/canvasjs/canvasjs.js"></script> 