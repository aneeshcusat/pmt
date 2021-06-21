<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<c:set var="nonBillableCategories" value="${applicationScope.applicationConfiguraion.nonBillableCategories}"/>
<c:set var="staticNonBillableCategories" value="${applicationScope.applicationConfiguraion.staticNonBillableCategories}"/>

<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="staticUnbilledEnabled" value="${applicationScope.applicationConfiguraion.staticNonBillableEnabled}"/>
    
<option value="">- select -</option>
<c:if test="${!staticUnbilledEnabled }">
	<c:if test="${currentUserGroupId =='1018'}">
		<option value="Leave Or Holiday">Leave Or Holiday</option>
	</c:if>
	<c:if test="${currentUserGroupId !='1018'}">
		<option value="LEAVE">Leave</option>
	</c:if>
	<option value="MEETING">Meeting</option>
	<c:if test="${not empty nonBillableCategories }">
		<c:forEach var="nonBillableCategoryConf" items="${nonBillableCategories}">
			<option value="${nonBillableCategoryConf.value}">${nonBillableCategoryConf.name}</option>
		</c:forEach>
	</c:if>
</c:if>

<c:if test="${staticUnbilledEnabled }">
	<c:if test="${not empty staticNonBillableCategories }">
		<option value="LEAVE">Leave</option>
		<%-- <c:forEach var="staticNonBillableCategoriesConf" items="${staticNonBillableCategories}">
			<option value="${staticNonBillableCategoriesConf.value}">${staticNonBillableCategoriesConf.name}</option>
		</c:forEach> --%>
		<option value="Additional support on projects post closure">Additional support on projects post closure</option>
		<option value="Administrative and management">Administrative and management</option>
		<option value="Client onsite trips and visits">Client onsite trips and visits</option>
		<option value="Compliance Management">Compliance Management</option>
		<option value="Downtime due to Technical / IT related Issues">Downtime due to Technical / IT related Issues</option>
		<option value="Free Pilot">Free Pilot</option>
		<option value="Holiday">Holiday</option>
		<option value="Internal product/solution development and support">Internal product/solution development and support</option>
		<option value="Internal team meetings, conferences and offsites">Internal team meetings, conferences and offsites</option>
		<option value="Knowledge development and training">Knowledge development and training</option>
		<option value="Marketing Collateral and Campaigns">Marketing Collateral and Campaigns</option>
		<option value="POC">POC</option>
		<option value="Presales">Presales</option>
		<option value="Proposals">Proposals</option>
	</c:if>
</c:if>
