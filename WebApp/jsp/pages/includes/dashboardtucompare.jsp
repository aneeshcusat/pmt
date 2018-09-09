<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- PAGE CONTENT WRAPPER -->
<div class="dashboard col-md-12 dashboadcompare" style="padding-right: 5px;display: none">
<div class="col-md-12" style="background-color: #e0e0e0;padding-left: 5px;">
<div class="col-md-3 dashboadaccountcompare" style="padding-right: 0px;height: 150px;padding-left: 0px;">
	<div class="col-md-12" style="margin-bottom: 5px;"><span class="tucompareheading">Accounts</span>
	</div>
	<div class="col-md-12" style="margin: 0px;margin-botton:5px;padding-right: 0px;height: 130px; overflow: auto;">
		<div class="col-md-12" style="margin: 0px;padding: 0px;">
			<c:if test="${not empty dashboardData.accountData}">  
		        <c:forEach var="account" items="${dashboardData.accountData}"  varStatus="accountStatus">
		 			<div class="col-md-6 dbcheckbox dbcheckboxug <c:if test="${currentUserGroupId != account.value.userGoupId}">hide</c:if> AC${account.key} UG${account.value.userGoupId}" style="margin: 0px;padding: 0px;">
					  <div class="checkbox checkbox-info checkbox-circle">
                        <input class="acccbutfilter" id="ACCB${account.key}" type="checkbox"  value="${account.key}">
                        <label for="ACCB${account.key}">
                            ${account.value.name}
                        </label>
                    </div>
		 			</div>
				</c:forEach>
			</c:if>
		</div>
	</div>	
</div>

<div class="col-md-3 dbptcompare" style="padding-right: 0px;height: 150px;padding-left: 0px;">
	<div class="col-md-12" style="margin-bottom: 5px;"><span class="tucompareheading">Teams</span>
	</div>
	<div class="col-md-12" style="margin: 0px;margin-botton:5px;padding-right: 0px;height: 130px; overflow: auto;">
		<div class="col-md-12" style="margin: 0px;padding: 0px;">
			<c:if test="${not empty dashboardData.accountData}">  
	        <c:forEach var="account" items="${dashboardData.accountData}"  varStatus="accountStatus">
	        	 <c:if test="${not empty account.value.projectTeams}"> 
	        	 	 <c:forEach var="projectTeam" items="${account.value.projectTeams}">
                    	<div class="col-md-6 dbcheckbox dbcheckboxug hide ACCB${account.key} PTCB${projectTeam.teamId}" style="margin: 0px;padding: 0px;">
						  <div class="checkbox checkbox-info checkbox-circle">
	                        <input class="ptcbutfilter" id="PTCB${projectTeam.teamId}" type="checkbox"  value="${projectTeam.teamId}">
	                        <label for="PTCB${projectTeam.teamId}">
	                           ${projectTeam.name}
	                        </label>
	                    </div>
			 			</div>
					</c:forEach>
				</c:if>
			</c:forEach>
			</c:if>
		</div>
	</div>	
</div>

<div class="col-md-3 dbstcompare" style="padding-right: 0px;height: 150px;padding-left: 0px;">
	<div class="col-md-12" style="margin-bottom: 5px;"><span class="tucompareheading">Sub Teams</span>
	</div>
	<div class="col-md-12" style="margin: 0px;margin-botton:5px;padding-right: 0px;height: 130px; overflow: auto;">
		<div class="col-md-12" style="margin: 0px;padding: 0px;">
			<c:if test="${not empty dashboardData.accountData}">  
	        <c:forEach var="account" items="${dashboardData.accountData}"  varStatus="accountStatus">
	        	 <c:if test="${not empty account.value.projectTeams}"> 
	        	 	 <c:forEach var="projectTeam" items="${account.value.projectTeams}">
	        	 	 	 <c:if test="${not empty projectTeam.projectSubTeams}">  
		                    <c:forEach var="projectSubTeam" items="${projectTeam.projectSubTeams}">		 			
		                    	<div class="col-md-6 dbcheckbox dbcheckboxug hide ACCB${account.key} PTCB${projectTeam.teamId} PSTCB${projectSubTeam.subTeamId}" style="margin: 0px;padding: 0px;">
								  <div class="checkbox checkbox-info checkbox-circle">
			                        <input class="stcbutfilter" id="PSTCB${projectSubTeam.subTeamId}" type="checkbox" value="${projectSubTeam.subTeamId}">
			                        <label for="PSTCB${projectSubTeam.subTeamId}">
			                           ${projectSubTeam.name}
			                        </label>
			                    </div>
					 			</div>
							</c:forEach>
						</c:if>
					</c:forEach>
				</c:if>
			</c:forEach>
			</c:if>
		</div>
	</div>	
</div>

<div class="col-md-3 dbrescompare" style="padding-right: 0px;height: 150px;padding-left: 0px;">
	<div class="col-md-12" style="margin-bottom: 5px;"><span class="tucompareheading">Resources</span>
	</div>
	<div class="col-md-12" style="margin: 0px;margin-botton:5px;padding-right: 0px;height: 130px; overflow: auto;">
		<div class="col-md-12" style="margin: 0px;padding: 0px;">
			 <c:if test="${not empty employeeMap}">
	    		<c:forEach var="employeeItem" items="${employeeMap}">		 			
	    			<div class="col-md-6 dbcheckbox <c:if test="${currentUserGroupId != employeeItem.value.userGroupId}">hide</c:if> UG${employeeItem.value.userGroupId}"	 style="margin: 0px;padding: 0px;">
					  <div class="checkbox checkbox-info checkbox-circle">
                        <input class="rescbutfilter" id="${employeeItem.key}" type="checkbox" value="${employeeItem.key}">
                        <label for="${employeeItem.key}">
                            ${employeeItem.value.firstName}
                        </label>
                    </div>
		 			</div>
				</c:forEach>
			</c:if>
		</div>
	</div>	
</div>
<div class="col-md-12">
	<select style="float: right" class="dbCompareUtilization">
		<option value="All">Over All</option>
		<option value="BILLABLE">Billable</option>
		<option value="NON_BILLABLE">Non Billable</option>
	</select>
	<span style="float: right;font-size: 11px;font-weight: 900;margin-right: 5px;">Show By</span>
 </div>
</div>
<div class="col-md-3 dbwidget">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title-box">
			<span style="float: left;margin-top: 3px"><a href="javascript:showHome();"><span class="fa fa-chevron-left fa-3x"></span></a></span>
				<span class="dbheading">Total Utilization</span>
			</div>
		</div>
		<div class="panel-body padding-0">
			<div class="dbcontentbox totalUtilizationComparediv autiloutercontent" id="chartTeamUtilizationContainer" style="height: 390px;">
			</div>
		</div>
	</div>
</div>
<div class="col-md-5 dbwidget">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title-box">
				<span class="dbheading">Teams</span>
			</div>
		</div>
		<div class="panel-body padding-0">
			<div class="dbcontentbox chartTeamContainer" id="chartTeamContainer"  style="height: 390px;width: 100%;">
			</div>
		</div>
	</div>
</div>
<div class="col-md-4 dbwidget">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title-box">
				<span class="dbheading">Resources</span>
			</div>
		</div>
		<div class="panel-body padding-0">
			<div class="dbcontentbox chartResourceContainer" id="chartResourceContainer"  style="height: 390px;">
				</div>
			</div>
		</div>
	</div>
</div>