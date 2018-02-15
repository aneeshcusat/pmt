	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>	
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
						        	<th width="15%">Est Time (Hrs)</th>
						        	<th width="30%">Actual Time  (Hrs)</th>
						        	<th width="5%">Activity</th>
						        	<th width="10%">Status</th>
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
								<td><span class="${taskDetails.taskId}taskTimeEditLink ${taskDetails.taskId}taskActTimeHrs">${taskDetails.actualTimeTakenInHrs}</span>
								<span class="${taskDetails.taskId}taskTimeEdit"  style="display:none" >
									<input type="text" placeholder="hh" class="durationTxt" id="taskHHTimeEdit${taskDetails.taskId}"/>
									<input type="text" value="0" placeholder="mm" class="durationTxt" id="taskMMTimeEdit${taskDetails.taskId}"/>
								</span>
								<span style="text-align:right; display:none" class="${taskDetails.taskId}taskTimeEdit">
									<button style="background-color: transparent;border: 0px;margin: 0 0;padding: 0 0;" onclick="taskActualTimeSubmit('${taskDetails.taskId}')" value="Save"><i class="fa fa-check fa-lg" style="color: green" aria-hidden="true"></i></button>
									<button style="background-color: transparent;border: 0px;margin: 0 0;padding: 0 0;" onclick="hideTaskActualTimeEdit('${taskDetails.taskId}')" value="Cancel"><i class="fa fa-undo fa-lg" style="color: gray" aria-hidden="true"></i></button>
								</span>
								<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && taskDetails.status == 'COMPLETED'}">
									<a href="javascript:void(0)" onclick="showTaskActualTimeEdit('${taskDetails.taskId}')" style="margin-left: 10px" class="${taskDetails.taskId}taskTimeEditLink"><span class="fa fa-pencil"></span></a>
								</c:if>	
								</td>
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