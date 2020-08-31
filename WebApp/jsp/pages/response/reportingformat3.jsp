
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true" />
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard" />
<table id="projectsTable" class="table table-responsive table-hover">
	<thead>
		<tr>
			<th>Date</th>
			<th>Project Code</th>
			<th>ID</th>
			<th>PO ID</th>
			<th>Resource Name</th>
			<th>Resource Code</th>
			<th>Designation</th>
			<th>Client</th>
			<th>Project</th>
			<th>Stakeholder</th>
			<th>Project Type</th>
			<th>Tasks</th>
			<th>Status</th>
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
					<td>${userDetailsMap[project.userId].firstName}</td>
					<td>${userDetailsMap[project.userId].empCode}</td>
					<td>${userDetailsMap[project.userId].designation}</td>
					<td>${project.accountName}</td>
					<td>${project.projectName}</td>
					<td>${project.clientName}</td>
					<td>${project.projectType}</td>
					<td>${project.taskName}</td>
					<td><span class="label label-${projectState}" style="${statusColor}">${project.projectStatus}</span></td>
					<c:if test="${empty project.childs}">
						<td><span class="${project.taskActivityId}taskTimeEditLink ${project.taskActivityId}taskTimeEditLinkHrs">${project.durationInHours}</span>
						<span class="${project.taskActivityId}taskTimeEdit" style="display: none">
							<input type="text" placeholder="hh" class="durationTxt taskActHHTimeEdit${project.taskActivityId}"/>
							<input type="text" value="0" placeholder="mm" class="durationTxt taskActMMTimeEdit${project.taskActivityId}"/> 
						</span>
						<span style="text-align:right; display:none" class="${project.taskActivityId}taskActTimeEdit">
							<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmit(${project.taskId},'${project.taskActivityId}')" value="Save"><i class="fa fa-check" style="color: green" aria-hidden="true"></i></button>
							<button style="background-color: transparent; border: 0px;" onclick="hideTaskActActualTimeEdit('${project.taskActivityId}')" value="Cancel"><i class="fa fa-undo" style="color: gray" aria-hidden="true"></i></button>
						</span>
						<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && not empty project.taskActivityEndTime}">
							<a href="javascript:void(0)" class=" ${project.taskActivityId}taskTimeEditLink" onclick="showTaskActActualTimeEdit('${project.taskActivityId}')"  style="margin-left: 10px">
							<span class="fa fa-pencil"></span></a>
						</c:if>
						</td>
					</c:if>
					
					<c:if test="${not empty project.childs}">
						<td>
						<input type="hidden" class="taskOriginalTime${project.taksUserDateKey}" value="${project.taskActivityDuration}"/>
						<span class="${project.taksUserDateKey}taskActTaskTimeHrs" >${project.durationInHours}</span>
						<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
						<a data-toggle="popover" data-container="body" data-placement="left" type="button" data-html="true" href="javascript:initPopOver(this);" id="taskActPopOver${project.taksUserDateKey}" style="margin-left: 10px"><span class="fa fa-pencil-square-o fa-lg"></span></a>
						</c:if>
						 <div id="popover-taskActPopOver${project.taksUserDateKey}" class="hide">
					        <div class="form-group"> 
					        <table style="width: 250px;font-size: 8pt">
					        <tr>
					        		<td colspan="3">
					       			</td>
					       			<td style="width: 5px"><a href="javascript:$('#taskActPopOver${project.taksUserDateKey}').popover('hide');" style="margin-top: -10px;" class="pull-right"><i class="fa fa-times fa-lg" style="color: red" aria-hidden="true"></i></a>
					       			</td>
					        </tr>
					        
					       	<tr>
					        		<td style="width: 25%"><fmt:formatDate pattern = "yyyy/MM/dd" value = "${project.taskActivityStartTime}"/></td>
					        		<td style="width: 25%x">${userDetailsMap[project.userId].firstName}</td>
					        		<td style="width: 18%">
					        		<span class="${project.taskActivityId}taskTimeEditLink ${project.taskActivityId}taskTimeEditLinkHrs">${project.actDurationInHours}</span>
					        		</td>
					        		<td  style="width: 32%">
					        		<span>
					        		<input type="hidden" class="taskActOriginalTime${project.taskActivityId}" value="${project.taskActActivityDuration}"/>
									<input type="text" placeholder="hh" class="durationTxt taskActHHTimeEdit${project.taskActivityId}"/>
									<input type="text" value="0" placeholder="mm" class="durationTxt taskActMMTimeEdit${project.taskActivityId}"/> 
								</span>
								<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && not empty project.taskActivityEndTime}">
								<span style="text-align:right;" class="${project.taskActivityId}taskActTimeEdit">
									<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmitPop(${project.taskId},'${project.taskActivityId}','${project.taksUserDateKey}',$(this))" value="Save"><i class="fa fa-check  fa-lg" style="color: green" aria-hidden="true"></i></button>
								</span>
								</c:if>
					        		</td>
					        	</tr>
					        	
					        <c:forEach var="taskActivity" items="${project.childs}">
					        	<tr>
					        		<td style="width: 25%"><fmt:formatDate pattern = "yyyy/MM/dd" value = "${taskActivity.taskActivityStartTime}"/></td>
					        		<td style="width: 25%x">${userDetailsMap[taskActivity.userId].firstName}</td>
					        		<td style="width: 18%"><span class="${taskActivity.taskActivityId}taskTimeEditLink ${taskActivity.taskActivityId}taskTimeEditLinkHrs">${taskActivity.durationInHours}</span>
					        		</td>
					        		<td  style="width: 32%">
					        		<span>
					        		<input type="hidden" class="taskActOriginalTime${taskActivity.taskActivityId}" value="${taskActivity.taskActivityDuration}"/>
									<input type="text" placeholder="hh" class="durationTxt taskActHHTimeEdit${taskActivity.taskActivityId}"/>
									<input type="text" value="0" placeholder="mm" class="durationTxt taskActMMTimeEdit${taskActivity.taskActivityId}"/> 
								</span>
								<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && not empty taskActivity.taskActivityEndTime}">
								<span style="text-align:right;" class="${taskActivity.taskActivityId}taskActTimeEdit">
									<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmitPop(${taskActivity.taskId},'${taskActivity.taskActivityId}','${project.taksUserDateKey}',$(this))" value="Save"><i class="fa fa-check fa-lg" style="color: green" aria-hidden="true"></i></button>
								</span>
								</c:if>
					        		</td>
					        	</tr>
					          </c:forEach>
					        </table>
					        </div>
					    </div>
 					 </td>
					</c:if>
					
				</tr>
			</c:forEach>
		</tbody>
	</c:if>
</table>
