
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<spring:url value="/jsp/assets" var="assets" htmlEscape="true" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="applicationHome" value="${contextPath}/dashboard" />
<table id="projectsTable" class="table table-striped">
	<thead>
		<tr>
			<th>Date</th>
			<th>Project Code</th>
			<th>ID</th>
			<th>PO ID</th>
			<th>Analyst</th>
			<th>Lead</th>
			<th>Productivity Type</th>
			<th>Project Type</th>
			<th>Client BU</th>
			<th>Client POC</th>
			<th>Requestor</th>
			<th>Project Name</th>
			<th>Hours Spent</th>
			<th>Utilization</th>
			<th>Analyst Comment</th>
			<th>Status</th>
		</tr>
	</thead>
	<c:if test="${not empty projectData}">
		<tbody>
			<c:forEach var="project" items="${projectData}">
				<c:set var="projectState" value="info" />
				<c:if test="${project.status == 'COMPLETED' }">
					<c:set var="projectState" value="success" />
				</c:if>
				<c:if test="${project.projectMissedTimeLine == true }">
					<c:set var="projectState" value="danger" />
				</c:if>
				<tr>
					<td>${project.startTime}</td>
					<td><a href="${applicationHome}/project/${project.id}"
						target="_new">${project.code}</a></td>
					<td>${project.id}</td>
					<td>${project.PONumber}</td>
					<td><c:forEach var="contributer"
							items="${project.contributers}" varStatus="contributerIndex">${userDetailsMap[contributer].firstName}<c:if
								test="${contributerIndex.index < project.contributers.size() - 1}">,</c:if>
						</c:forEach></td>
					<td>${employeeMap[project.projectLead].firstName}</td>
					<td>${project.type}</td>
					<td>${project.category}</td>
					<td>${project.accountName}</td>
					<td>${project.clientName}</td>
					<td>${project.reporterName}</td>
					<td>${project.name}</td>
					<td>${project.actualDurationInHrs}</td>
					<td></td>
					<td></td>
					<td><span class="label label-${projectState}">${project.status}</span></td>
				</tr>
			</c:forEach>
		</tbody>
	</c:if>
</table>
