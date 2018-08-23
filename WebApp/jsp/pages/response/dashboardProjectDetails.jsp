<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:if test="${not empty projectData}">
	  <c:forEach var="project" items="${projectData}">
	    <tr class="projectDetailsRow activeRow ${project.projectStatus}">
	        <td class="filterable-cell rtborder">${project.projectName}</td>
	        <td class="panelHideTD" style="display: none">${project.projectNumber}</td>
	        <td class="filterable-cell">${project.teamName}</td>
	        <td class="filterable-cell">${project.subTeamName}</td>
	        <td class="panelHideTD" style="display: none">${project.accountName}</td>
 				    <td class="panelHideTD" style="display: none">${project.clientName}</td>
	        <td class="filterable-cell">Project Lead</td>
	        <td class="filterable-cell">Assigned To</td>
 				    <td class="panelHideTD" style="display: none">${project.projectType}</td>
			<td class="panelHideTD" style="display: none">${project.projectCategory}</td>
	        <td class="filterable-cell">10:00</td>
	        <td class="filterable-cell">${project.projectStatus}</td>
	    </tr>
	    </c:forEach>
</c:if>
<c:if test="${empty projectData}">
 <tr>
	        <td colspan="12" style="font-size: 15px">No Projects !!!</td>
 </tr>
</c:if>