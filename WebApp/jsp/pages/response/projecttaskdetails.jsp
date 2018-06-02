	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>	
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
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
									<img alt="image" src="${applicationHome}/image/${taskDetails.assignee}"  title="${employeeMap[taskDetails.assignee].firstName}"   onerror="this.src='${assets}/images/users/no-image.jpg'">
								</c:if>
								</td>
								<td>${taskDetails.duration }</td>
								<c:if test="${empty taskDetails.taskActivityDetails}">
								<td>00:00
								</td>
								</c:if>
								
					<c:if test="${not empty taskDetails.taskActivityDetails && fn:length(taskDetails.taskActivityDetails) eq 1}">
						<td>
						<c:if test="${not empty taskDetails.taskActivityDetails[0].recordedEndTime }">
						<span class="${taskDetails.taskActivityDetails[0].taskActivityId}taskTimeEditLink ${taskDetails.taskActivityDetails[0].taskActivityId}taskTimeEditLinkHrs">${taskDetails.taskActivityDetails[0].actualTimeTakenInHrs}</span>
						
						<span class="${taskDetails.taskActivityDetails[0].taskActivityId}taskTimeEdit" style="display: none">
							<input type="text" placeholder="hh" class="durationTxt taskActHHTimeEdit${taskDetails.taskActivityDetails[0].taskActivityId}"/>
							<input type="text" value="0" placeholder="mm" class="durationTxt taskActMMTimeEdit${taskDetails.taskActivityDetails[0].taskActivityId}"/> 
						</span>
						<span style="text-align:right; display:none" class="${taskDetails.taskActivityDetails[0].taskActivityId}taskActTimeEdit">
							<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmit(${taskDetails.taskActivityDetails[0].taskId},'${taskDetails.taskActivityDetails[0].taskActivityId}')" value="Save"><i class="fa fa-check" style="color: green" aria-hidden="true"></i></button>
							<button style="background-color: transparent; border: 0px;" onclick="hideTaskActActualTimeEdit('${taskDetails.taskActivityDetails[0].taskActivityId}')" value="Cancel"><i class="fa fa-undo" style="color: gray" aria-hidden="true"></i></button>
						</span>
						<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
							<a href="javascript:void(0)" class=" ${taskDetails.taskActivityDetails[0].taskActivityId}taskTimeEditLink" onclick="showTaskActActualTimeEdit('${taskDetails.taskActivityDetails[0].taskActivityId}')"  style="margin-left: 10px">
							<span class="fa fa-pencil"></span></a>
						</c:if>
						</c:if>
						<c:if test="${empty taskDetails.taskActivityDetails[0].recordedEndTime }">
						00:00
						</c:if>
						</td>
					</c:if>

					<c:if test="${not empty taskDetails.taskActivityDetails &&  fn:length(taskDetails.taskActivityDetails) gt 1}">
						<td>
						<input type="hidden" class="taskOriginalTime${taskDetails.taskId}" value="${taskDetails.actualTimeTaken}"/>
						<span class="${taskDetails.taskId}taskActTaskTimeHrs" >${taskDetails.actualTimeTakenInHrs}</span>
						<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
						<a data-toggle="popover" data-container="body" data-placement="left" type="button" data-html="true" href="javascript:initPopOver(this);" id="taskActPopOver${taskDetails.taskId}" style="margin-left: 10px"><span class="fa fa-pencil-square-o fa-lg"></span></a>
						</c:if>
						 <div id="popover-taskActPopOver${taskDetails.taskId}" class="hide">
					        <div class="form-group"> 
					        <table style="width: 250px;font-size: 8pt">
					        <tr>
					        		<td colspan="3">
					       			</td>
					       			<td style="width: 5px"><a href="javascript:$('#taskActPopOver${taskDetails.taskId}').popover('hide');" style="margin-top: -10px;" class="pull-right"><i class="fa fa-times fa-lg" style="color: red" aria-hidden="true"></i></a>
					       			</td>
					        </tr>
					        	
					        <c:forEach var="taskActivity" items="${taskDetails.taskActivityDetails}">
					        	<tr>
					        		<td style="width: 25%"><fmt:formatDate pattern = "yyyy/MM/dd" value = "${taskActivity.actualStartTime}"/></td>
					        		<td style="width: 25%x">${employeeMap[taskActivity.userId].firstName}</td>
					        		
					        		<td style="width: 18%">
					        		<c:if test="${empty taskActivity.recordedEndTime }">
									00:00
									</c:if>
									<c:if test="${not empty taskActivity.recordedEndTime }">
					        		<span class="${taskActivity.taskActivityId}taskTimeEditLink ${taskActivity.taskActivityId}taskTimeEditLinkHrs">${taskActivity.actualTimeTakenInHrs}</span>
					        		</c:if>
					        		</td>
					        		<td  style="width: 32%">
					        		<c:if test="${not empty taskActivity.recordedEndTime }">
					        		<span>
					        		<input type="hidden" class="taskActOriginalTime${taskActivity.taskActivityId}" value="${taskActivity.durationInMinutes}"/>
									<input type="text" placeholder="hh" class="durationTxt taskActHHTimeEdit${taskActivity.taskActivityId}"/>
									<input type="text" value="0" placeholder="mm" class="durationTxt taskActMMTimeEdit${taskActivity.taskActivityId}"/> 
									</span>
									</c:if>
								<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && not empty taskActivity.recordedEndTime}">
								<span style="text-align:right;" class="${taskActivity.taskActivityId}taskActTimeEdit">
									<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmitPop(${taskActivity.taskId},'${taskActivity.taskActivityId}',$(this))" value="Save"><i class="fa fa-check fa-lg" style="color: green" aria-hidden="true"></i></button>
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