<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="staticUnbilledEnabled" value="${currentUserGroupId=='1019' ||currentUserGroupId=='1017' || currentUserGroupId=='1005' || currentUserGroupId=='1002' || currentUserGroupId=='1006' || currentUserGroupId=='1010' || currentUserGroupId=='1001' || currentUserGroupId=='1003' || currentUserGroupId=='1009' || currentUserGroupId=='1007' || currentUserGroupId=='1016' || currentUserGroupId=='1004'}"/>
<c:set var="nonBillableCategories" value="${applicationScope.applicationConfiguraion.nonBillableCategories}"/>
	<c:if test="${!staticUnbilledEnabled }">
		<tr>
			<td>Leave</td>
			<td></td>
		</tr>
		<tr>
			<td>Meeting</td>
			<td></td>
		</tr>
		<c:if test="${not empty nonBillableCategories}">
        <c:forEach var="nonBillableCategoryConf" items="${nonBillableCategories}">
		<tr>
			<td>${nonBillableCategoryConf.name}</td>
			<td><a href="#" onclick="deleteApplicationConfigVal('${nonBillableCategoryConf.name}',${nonBillableCategoryConf.appConfValueId},'nonBillableCategory')" data-box="#confirmationbox" class="deleteAppConfValue mb-control" style="float:right"><i class="fa fa-times fa-2x" style="color:red" aria-hidden="true"></i></a></td>
		</tr>
        </c:forEach>
     </c:if>
     </c:if>
       <c:if test="${staticUnbilledEnabled }">
       <tr>
			<td>Admin Work</td>
			<td></td>
		</tr>
		<tr>
			<td>Attending Trainings & Knowledge Development</td>
			<td></td>
		</tr>
		<tr>
			<td>CSR</td>
			<td></td>
		</tr>
		<tr>
			<td>Famstack Management</td>
			<td></td>
		</tr>
		<tr>
			<td>Holiday</td>
			<td></td>
		</tr>
		<tr>
			<td>Imparting Trainings</td>
			<td></td>
		</tr>
		<tr>
			<td>Internal Team Meetings</td>
			<td></td>
		</tr>
		<tr>
			<td>Leave</td>
			<td></td>
		</tr>
		<tr>
			<td>Marketing Collaterals & POC</td>
			<td></td>
		</tr>
		<tr>
			<td>Presales Proposals</td>
			<td></td>
		</tr>
       </c:if>
