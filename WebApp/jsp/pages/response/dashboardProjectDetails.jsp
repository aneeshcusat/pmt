<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>		
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
<c:set var="allUsersMap" value="${applicationScope.applicationConfiguraion.allUsersMap}"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:if test="${not empty projectData}">
	  <c:forEach var="project" items="${projectData}">
	    <tr class='projectDetailsRow activeRow filteredRow ${project.projectStatusLabel} <c:if test="${not empty project.assigneeIdList}"><c:forEach var="assigneeId" items="${project.assigneeIdList}" varStatus="assigneeIdIndex"> prjUserId${assigneeId}</c:forEach></c:if> prjAccount${project.projectAccountId} prjTeam${project.teamId} prjSubTeam${project.subTeamId} prjClient${project.projectClientId}' data-duration="${project.taskActivityDuration}">
	        <td class="filterable-cell rtborder"><strong><a href="${applicationHome}/project/${project.projectId}">${project.projectName}</a></strong></td>
	        <td class="panelHideTD" style="display: none">${project.projectNumber}</td>
	        <td class="filterable-cell">${project.teamName}</td>
	        <td class="filterable-cell">${project.subTeamName}</td>
	        <td class="panelHideTD" style="display: none">${project.accountName}</td>
 				    <td class="panelHideTD" style="display: none">${project.clientName}</td>
	        <td class="filterable-cell"><c:if test="${not empty project.projectLead }">${allUsersMap[project.projectLead].firstName}</c:if></td>
	        <td class="filterable-cell">
	        	<c:if test="${not empty project.assigneeIdList}">
          			<c:forEach var="assigneeId" items="${project.assigneeIdList}" varStatus="assigneeIdIndex"> 
	                 <span class="project_team">
	                    <a href="#"><img  style="width:20px" alt="image" title="${allUsersMap[assigneeId].firstName}" src="${applicationHome}/image/${assigneeId}"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
	                 </span>
	               </c:forEach>
               </c:if>
				</td>
	        </td>
 				    <td class="panelHideTD" style="display: none">${project.projectType}</td>
			<td class="panelHideTD" style="display: none">${project.projectCategory}</td>
	        <td class="filterable-cell">${project.durationInHours}</td>
	        <td class="filterable-cell">${project.projectStatus}</td>
	    </tr>
	    </c:forEach>
</c:if>
<c:if test="${empty projectData}">
 <tr>
	        <td colspan="12" style="font-size: 15px">No Projects !!!</td>
 </tr>
</c:if>