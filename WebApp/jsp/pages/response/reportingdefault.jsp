
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<spring:url value="/jsp/assets" var="assets" htmlEscape="true" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="applicationHome" value="${contextPath}/dashboard" />
<table id="projectsTable" class="table table-striped">
	<thead>
		<tr>
			<th>Project code</th>
			<th>ID</th>
			<th>PO ID</th>
			<th>Project Name</th>
			<th>Type</th>
			<th>Category</th>
			<th>Team</th>
			<th>Sub Team</th>
			<th>Client</th>
			<th>Est Duration (Hrs)</th>
			<th>Project Duration (Hrs)</th>
			<th>Task Duration (Mins)</th>
			<th>Assignee</th>
			<th>Contributers</th>
			<th>Status</th>
		</tr>
	</thead>
	<c:if test="${not empty projectData}">
		<tbody>
			<c:forEach var="project" items="${projectData}">
				 <c:set var="projectState" value="info"/>
				      <c:set var="statusColor" value=""/>
	                  <c:if test="${project.status == 'NEW' }">
							<c:set var="statusColor" value="background-color:lightblue"/>
	                  </c:if>
	                  <c:if test="${project.status == 'UNASSIGNED' }">
							<c:set var="statusColor" value="background-color:darkviolet"/>
	                  </c:if>
	                  <c:if test="${project.status == 'ASSIGNED' }">
	                  		<c:set var="statusColor" value="background-color:orange"/>
	                  </c:if>
   					  <c:if test="${project.status == 'INPROGRESS' }">
   					  	<c:set var="statusColor" value="background-color: brown;"/>
	                  </c:if>
	                  <c:if test="${project.projectMissedTimeLine == true }">
	                  		<c:set var="projectState" value="danger"/>
	                  </c:if>
	                  <c:if test="${project.status == 'COMPLETED' }">
	                   		<c:set var="projectState" value="success"/>
	                  </c:if>
				<c:if test="${not empty project.projectTaskDeatils}">
					<c:forEach var="projectTaskDetails"
						items="${project.projectTaskDeatils}"
						varStatus="projectTaskDetailsIndex">
						<tr
							<c:if test="${projectTaskDetailsIndex.index > 0}">
				                              </c:if>>
							<td><a href="${applicationHome}/project/${project.id}"
								target="_new">${project.code}</a></td>
							<td>${project.id}</td>
							<td>${project.PONumber}</td>
							<td>${project.name}</td>
							<td>${project.type}</td>
							<td>${project.category}</td>
							<td>${project.teamName}</td>
							<td>${project.subTeamName}</td>
							<td>${project.clientName}</td>
							<td>${project.duration}</td>
							<td>${project.actualDurationInHrs}</td>
							<td>${projectTaskDetails.actualTimeTaken}</td>
							<td>${userDetailsMap[projectTaskDetails.assignee].firstName}
							</td>
							<td><c:forEach var="contributer"
									items="${project.contributers}" varStatus="contributerIndex">${userDetailsMap[contributer].firstName}<c:if
										test="${contributerIndex.index < project.contributers.size() - 1}">,</c:if>
								</c:forEach></td>

							<td><span class="label label-${projectState}" style="${statusColor}">${project.status}</span></td>
						</tr>

					</c:forEach>
				</c:if>
				<c:if test="${empty project.projectTaskDeatils}">
					<tr>
						<td><a href="${applicationHome}/project/${project.id}"
							target="_new">${project.code}</a></td>
						<td>${project.id}</td>
						<td>${project.PONumber}</td>
						<td>${project.name}</td>
						<td>${project.type}</td>
						<td>${project.category}</td>
						<td>${project.teamName}</td>
						<td>${project.subTeamName}</td>
						<td>${project.clientName}</td>
						<td>${project.duration}</td>
						<td></td>
						<td>${project.actualDurationInHrs}</td>
						<td></td>
						<td></td>
						<td><span class="label label-${projectState}">${project.status}</span></td>
					</tr>
				</c:if>
			</c:forEach>
		</tbody>
	</c:if>
</table>
