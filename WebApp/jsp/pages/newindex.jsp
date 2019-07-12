<%@include file="includes/header.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/dashboard.css?v=${fsVersionNumber}"/>
 <div class="col-md-12 dsfilter">
 <div class="col-md-12">
 </div>
 <c:set var="isAdmin" value="false" scope="page"/>
 <c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}"> 
  <c:set var="isAdmin" value="true"/>
 </c:if>
 <div class="col-md-12">
 	<span style="float: right" class="dashboardmainfilter">
 		<select class="searchbox dashboadgroup">
 			<option value="-1" disabled>Select Division</option>
        	<c:forEach var="userGroup" items="${userGroupMap}" varStatus="userGroupIndex">
        	<c:if test="${isAdmin}"> 
                <option <c:if test="${currentUserGroupId == userGroup.value.userGroupId}">selected="selected"</c:if> value="${userGroup.value.userGroupId}">${userGroup.value.name}</option>
        	</c:if>
        	<c:if test="${!isAdmin}">
	        	<c:if test="${currentUserGroupId == userGroup.value.userGroupId}">
	        	  <option selected="selected" value="${userGroup.value.userGroupId}">${userGroup.value.name}</option>
	        	</c:if>
        	</c:if> 
        	</c:forEach>
 		</select>
 	<select class="searchbox accountInfo">
 		<option value="-1" disabled selected>Select Account</option>
 		<option value="0">All</option>
		<c:if test="${not empty dashboardData.accountData}">  
	        <c:forEach var="account" items="${dashboardData.accountData}"  varStatus="accountStatus">
	 			<option class='<c:if test="${currentUserGroupId != account.value.userGoupId}">hide</c:if> AC${account.key} UG${account.value.userGoupId}' value="${account.key}">${account.value.name}</option>
			</c:forEach>
		</c:if>
 	</select>	
 	<select class="searchbox projectTeamInfo">
 		<option value="-1" disabled selected>Select Team</option>
		<option value="0">All</option>
		<c:if test="${not empty dashboardData.accountData}">  
	        <c:forEach var="account" items="${dashboardData.accountData}"  varStatus="accountStatus">
	        	 <c:if test="${not empty account.value.projectTeams}"> 
	        	 	 <c:forEach var="projectTeam" items="${account.value.projectTeams}">
	 					<option class='hide AC${account.key} PT${projectTeam.teamId}' value="${projectTeam.teamId}">${projectTeam.name}</option>
	 				</c:forEach>
	 			</c:if>
			</c:forEach>
		</c:if>
 	</select>
 	<select class="searchbox projectSubTeamInfo">
 		<option value="-1" disabled selected>Select Sub Team</option>
 		<option value="0">All</option>
 		<c:if test="${not empty dashboardData.accountData}">  
	        <c:forEach var="account" items="${dashboardData.accountData}"  varStatus="accountStatus">
	        	 <c:if test="${not empty account.value.projectTeams}"> 
	        	 	 <c:forEach var="projectTeam" items="${account.value.projectTeams}">
	        	 	 	 <c:if test="${not empty projectTeam.projectSubTeams}">  
		                    <c:forEach var="projectSubTeam" items="${projectTeam.projectSubTeams}">
	 							<option class='hide AC${account.key} PT${projectTeam.teamId} PST${projectSubTeam.subTeamId}' value="${projectSubTeam.subTeamId}">${projectSubTeam.name}</option>
	 						</c:forEach>
	 					</c:if>
	 				</c:forEach>
	 			</c:if>
			</c:forEach>
		</c:if>
 	</select>
 	<select class="searchbox resourceInfo">
 		<option value="0">All</option>
 		 <c:if test="${not empty employeeMap}">
	    <c:forEach var="employeeItem" items="${employeeMap}">
	   		<c:if test="${isAdmin}"> 
	    		<option  class='<c:if test="${currentUserGroupId != employeeItem.value.userGroupId}">hide</c:if> UG${employeeItem.value.userGroupId}' <c:if test="${currentUser.id == employeeItem.value.id && currentUserGroupId == employeeItem.value.userGroupId}">selected="selected"</c:if> value="${employeeItem.value.id}">${employeeItem.value.firstName}</option>
	    	</c:if>
	    	<c:if test="${!isAdmin}"> 
	    		<c:if test="${currentUser.id == employeeItem.value.id && currentUserGroupId == employeeItem.value.userGroupId}">
	    			<option  class='UG${employeeItem.value.userGroupId}' selected="selected" value="${employeeItem.value.id}">${employeeItem.value.firstName}</option>
	    		</c:if>
	    	</c:if>
	    </c:forEach>
	    </c:if>
 	</select>
 	 <input type="text" name="daterange" id="daterangeText" style="display: none" value="${dateDashBoardRange}" /> 
 	<span id="dashboarddatepicker" class="dateFilterbox">                                            
    	<span>${dateDashBoardRange}</span><b class="caret"></b>
	</span>
 	<a href="javascript:refreshDashBoard();" title="Filter the dashboard">
 		<i class="fa fa-filter fa-2x"></i>
 	</a>
 	</span>
 	</div>
</div>
<div class="dashboardcontainer">
<%@include file="includes/dashboardhome.jsp" %>
<%@include file="includes/dashboardtu.jsp" %>
<%@include file="includes/dashboardbandwidth.jsp" %> 
<%@include file="includes/dashboardtucompare.jsp" %> 
</div>
<script type="text/javascript">
	ajaxStartLabelDisabled=true;
</script>
<%@include file="includes/footer.jsp" %>
<script type="text/javascript">
	//var fullcaledartuSource = "/bops/dashboard/getEmpUtlAjaxFullcalendar/"+$(".dashboadgroup").val();
	//var fullcaledarBsSource = "/bops/dashboard/getEmpBWAjaxFullcalendar/"+$(".dashboadgroup").val();
</script>
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/famstack.calender.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/famstack.dashboard.js?v=${fsVersionNumber}"></script>