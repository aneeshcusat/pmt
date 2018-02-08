	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="applicationHome" value="${contextPath}/dashboard"/>
			    <c:if test="${not empty taskDetailsData}">
			    	<button class="close" id = "x" onclick='$("#taskDetailsDiv").hide();'>
			           	 X
			        	</button>
			 			<table class="table table-responsive table-hover"  style="top: -25px;position: relative;left: -4px;">
						  <thead>
						        <tr>
						        	<th width="35%">Task Name</th>
						        	<th width="10%">Assignee</th>
						        	<th width="15%">Est Time</th>
						        	<th width="15%">Actual Time</th>
						        	<th width="10%">Activity</th>
						        	<th width="15%">Status</th>
								</tr>
							</thead>
							<tbody>
						  <c:forEach var="taskDetails" items="${taskDetailsData}">
						   <c:set var="projectState" value="info"/>
	                  <c:if test="${taskDetails.status == 'COMPLETED' }">
	                   		<c:set var="projectState" value="success"/>
	                  </c:if>
	                  <c:if test="${project.status == 'NEW' }">
	                  </c:if>
							<tr>
								<td>${taskDetails.name }</td>
								<td class="project_team">
								<c:if test="${not empty taskDetails.assignee}">
									<img alt="image" src="${applicationHome}/image/${taskDetails.assignee}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
								</c:if>
								</td>
								<td>${taskDetails.duration }</td>
								<td>${taskDetails.actualTimeTaken }</td>
								<td>
								<c:if test="${taskDetails.projectTaskType == 'PRODUCTIVE' }">
									P
								</c:if>
								<c:if test="${taskDetails.projectTaskType == 'REVIEW' }">
									R
								</c:if>
								<c:if test="${taskDetails.projectTaskType == 'ITERATION' }">
									I
								</c:if>
								</td>
								<td><span class="label label-${projectState}">${taskDetails.status}</span></td>	
							</tr>			
						</c:forEach>
						</tbody>
						</table>
					
					</c:if>