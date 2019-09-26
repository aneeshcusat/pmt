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
		<c:forEach var="staticNonBillableCategoriesConf" items="${staticNonBillableCategories}">
			<option value="${staticNonBillableCategoriesConf.value}">${staticNonBillableCategoriesConf.name}</option>
		</c:forEach>
	</c:if>
</c:if>
