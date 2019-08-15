<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<c:set var="nonBillableCategories" value="${applicationScope.applicationConfiguraion.nonBillableCategories}"/>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="staticUnbilledEnabled" value="${currentUserGroupId=='1005' || currentUserGroupId=='1002' || currentUserGroupId=='1006' || currentUserGroupId=='1010' || currentUserGroupId=='1001' || currentUserGroupId=='1003' || currentUserGroupId=='1009' || currentUserGroupId=='1007' || currentUserGroupId=='1016' || currentUserGroupId=='1004'}"/>
    
<option value="">- select -</option>
<c:if test="${!staticUnbilledEnabled }">
	<c:if test="${currentUserGroupId =='1018'}">
		<option value="Leave Or Holiday">Leave Or Holiday</option>
<	</c:if>
	<c:if test="${currentUserGroupId !='1018'}">
		<option value="LEAVE">Leave</option>
<	</c:if>
	<option value="MEETING">Meeting</option>
	<c:if test="${not empty nonBillableCategories }">
		<c:forEach var="nonBillableCategoryConf" items="${nonBillableCategories}">
			<option value="${nonBillableCategoryConf.value}">${nonBillableCategoryConf.name}</option>
		</c:forEach>
	</c:if>
</c:if>
<c:if test="${staticUnbilledEnabled }">
	<option value="Admin Work">Admin Work</option>
	<option value="Attending Trainings & Knowledge Development">Attending Trainings &amp; Knowledge Development</option>
	<option value="CSR">CSR</option>
	<option value="Famstack Management">Famstack Management</option>
	<option value="Holiday">Holiday</option>
	<option value="Imparting Trainings">Imparting Trainings</option>
	<option value="Internal Team Meetings">Internal Team Meetings</option>
	<option value="LEAVE">Leave</option>
	<option value="Marketing Collaterals & POC">Marketing Collaterals &amp; POC</option>
	<option value="Presales Proposals">Presales Proposals</option>
</c:if>
