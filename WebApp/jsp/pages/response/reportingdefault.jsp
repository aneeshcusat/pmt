
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard" />
<table id="projectsTable" class="table table-striped">
	<thead>
		<tr>
			<th>Date</th>
			<th>Project Code</th>
			<th>ID</th>
			<th>PO ID</th>
			<th>Project Name</th>
			<th>Type</th>
			<th>Category</th>
			<th>Team</th>
			<th>Sub Team</th>
			<th>Client</th>
			<th>Status</th>
			<th>Assignee</th>
			<th>Task Name</th>
			<th>Duration</th>
		</tr>
	</thead>
	<c:if test="${not empty projectData}">
		<tbody>
			<c:forEach var="project" items="${projectData}">
				 <c:set var="projectState" value="success"/>
				      <c:set var="statusColor" value=""/>
	                  <c:if test="${project.projectStatus == 'NEW' }">
							<c:set var="statusColor" value="background-color:lightblue"/>
	                  </c:if>
	                  <c:if test="${project.projectStatus == 'UNASSIGNED' }">
							<c:set var="statusColor" value="background-color:darkviolet"/>
	                  </c:if>
	                  <c:if test="${project.projectStatus == 'ASSIGNED' }">
	                  		<c:set var="statusColor" value="background-color:orange"/>
	                  		<c:set var="projectState" value="info"/>
	                  </c:if>
   					  <c:if test="${project.projectStatus == 'INPROGRESS' }">
   					  	<c:set var="statusColor" value="background-color: brown;"/>
   					  	<c:set var="projectState" value="warning"/>
	                  </c:if>
				<tr>
					<td><fmt:formatDate pattern = "yyyy/MM/dd" value = "${project.taskActivityStartTime}"/></td>
					<td><a href="${applicationHome}/project/${project.projectId}"
						target="_new">${project.projectCode}</a></td>
					<td>${project.projectId}</td>
					<td>${project.projectNumber}</td>
					<td>${project.projectName}</td>
					<td>${project.projectType}</td>
					<td>${project.projectCategory}</td>
					<td>${project.teamName}</td>
					<td>${project.subTeamName}</td>
					<td>${project.clientName}</td>
					<td><span class="label label-${projectState}" style="${statusColor}">${project.projectStatus}</span></td>
					<td>${userDetailsMap[project.userId].firstName}</td>
					<td>${project.taskName}</td>
					<td>${project.durationInHours}</td>
				</tr>
			</c:forEach>
		</tbody>
	</c:if>
</table>
