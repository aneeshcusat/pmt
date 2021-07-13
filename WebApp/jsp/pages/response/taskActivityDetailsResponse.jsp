	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>	
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="applicationHome" value="${contextPath}/dashboard"/>
	<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<input id="${taskDetails.taskId}name" type="hidden" value="${taskDetails.name}"/>
<input id="${taskDetails.taskId}taskClient" type="hidden" value="${taskDetails.client}"/>
<input id="${taskDetails.taskId}projectTaskType" type="hidden"
	value="${taskDetails.projectTaskType}" />
	<input id="${taskDetails.taskId}priority" type="hidden"
	value="${taskDetails.priority}" />
<input id="${taskDetails.taskId}timeTaken" type="hidden"
	value="${taskDetails.actualTimeTakenInHrs}" />
	<input id="${taskDetails.taskId}timeTakenInMins" type="hidden"
	value="${taskDetails.actualTimeTaken}" />
<input id="${taskDetails.taskId}description" type="hidden"
	value="${taskDetails.description}" />
<input id="${taskDetails.taskId}startTime" type="hidden"
	value="${taskDetails.startTime}" />
<input id="${taskDetails.taskId}completionTime" type="hidden"
	value="${taskDetails.completionTime}" />
<input id="${taskDetails.taskId}duration" type="hidden"
	value="${taskDetails.duration}" />
<input id="${taskDetails.taskId}priority" type="hidden"
	value="${taskDetails.priority}" />
<input id="${taskDetails.taskId}canRecure" type="hidden"
	value="${taskDetails.canRecure}" />
<input id="${taskDetails.taskId}assignee" type="hidden"
	value="${taskDetails.assignee}" />
<input id="${taskDetails.taskId}assigneeName" type="hidden"
	value="${userDetailsMap[taskDetails.assignee].firstName}" />
<input id="${taskDetails.taskId}helper" type="hidden"
	value="${taskDetails.helpersList}" />
<c:set var="helpersNames" value="" />
<input id="${taskDetails.taskId}helperNames" type="hidden"
	value="${taskDetails.helperNames}" />
<input id="${taskDetails.taskId}taskitemcategory" type="hidden"
	value="${taskDetails.taskCategory}" />
<input id="${taskDetails.taskId}taskProjectCategory" type="hidden"
	value="${taskDetails.projectCategory}" />

<div id="taskDetailsContent${taskDetails.taskId}" class="hide">
 		<ul href="#" class="list-group-item">  
									         
		<span class="contacts-title">${taskDetails.name}</span>
		<p>${taskDetails.description}</p>
		<p>Est Duration : <b>${taskDetails.duration}</b> Hours</p>
		<p>
			Time Taken : <span  class="${taskDetails.taskId}taskTimeEditLink"><b class="${taskDetails.taskId}actualTimeTakenInHrs">${taskDetails.actualTimeTakenInHrs}</b> Hours </span>
			<span class="${taskDetails.taskId}taskTimeEdit"  style="display:none" >
				<input type="text" placeholder="hh" class="durationTxt" id="taskHHTimeEdit${taskDetails.taskId}"/>
				<input type="text" value="0" placeholder="mm" class="durationTxt" id="taskMMTimeEdit${taskDetails.taskId}"/>
			</span>
			<span style="text-align:right; display:none" class="${taskDetails.taskId}taskTimeEdit">
				<button style="background-color: transparent; border: 0px;" onclick="taskActualTimeSubmit('${taskDetails.taskId}')" value="Save"><i class="fa fa-check fa-2x" style="color: green" aria-hidden="true"></i></button>
				<button style="background-color: transparent; border: 0px;" onclick="hideTaskActualTimeEdit('${taskDetails.taskId}')" value="Cancel"><i class="fa fa-undo fa-2x" style="color: gray" aria-hidden="true"></i></button>
			</span>
			 <c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && taskDetails.status == 'COMPLETED'}">
				<a href="javascript:void(0)" onclick="showTaskActualTimeEdit('${taskDetails.taskId}')" style="margin-left: 10px" class="${taskDetails.taskId}taskTimeEditLink hide"><span class="fa fa-pencil"></span></a>
			</c:if>			
			</p>
		<c:if test="${not taskDetails.extraTimeTask}">
			<p>	Est Start Time : <b>${taskDetails.startTime}</b></p>
			<p>Est Complete Time : ${taskDetails.completionTime}</p>
			<p>Task Type :<c:if test="${taskDetails.projectTaskType == 'PRODUCTIVE' }">
											Productive
										</c:if>
										<c:if test="${taskDetails.projectTaskType == 'REVIEW' }">
											Review
										</c:if>
										<c:if test="${taskDetails.projectTaskType == 'ITERATION' }">
											Iterative
										</c:if>
										</p>
		</c:if>
		<p>Priority : ${taskDetails.priority}</p>
		<c:if test="${taskDetails.canRecure == 'true' }">
		<p>Recurring : Yes</p>
		</c:if>
		<c:if test="${taskDetails.canRecure == 'false' }">
		<p>Recurring : No</p>
		</c:if>
	</ul>

	<c:if test="${not empty taskDetails.taskActivityDetails}">
		<c:forEach var="taskActivityDetail"
			items="${taskDetails.taskActivityDetails}" varStatus="taskIndex">
			<ul
				class="list-group-item taskActivityTime${taskActivityDetail.taskActivityId}">
				
				<span class="contacts-title" style="font-size: 10px">${taskActivityDetail.userTaskType}
					[${taskDetails.name}] Task Activity</span>
				<c:choose>
					<c:when test="${not empty taskActivityDetail.actualEndTime}">
						<p>Status :<b> COMPLETED</b></p>
					</c:when>
					<c:when test="${not empty taskActivityDetail.actualStartTime}">
						<p>Status : INPROGRESS</p>
					</c:when>
					<c:when test="${empty taskActivityDetail.actualStartTime}">
						<p>Status : NEW</p>
					</c:when>
				</c:choose>
				<p>Assignee : <b>${employeeMap[taskActivityDetail.userId].firstName}</b></p>
				<c:if test="${taskDetails.extraTimeTask}">
					<p>	Recorded start Time : <b>${taskActivityDetail.recordedStartTime}</b></p>
					<p>Recorded end time : ${taskActivityDetail.recordedEndTime}</p>
				</c:if>
				<c:if test="${not taskDetails.extraTimeTask}">
				
					<c:if test="${not empty taskActivityDetail.recordedStartTime}">
						<p>Recorded start time
							:<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${taskActivityDetail.recordedStartTime}"/></p>
					</c:if>
					<c:if test="${not empty taskActivityDetail.recordedEndTime}">
						<p>Recorded end time :<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${taskActivityDetail.recordedEndTime}"/></p>
					</c:if>
					
					<c:if test="${not empty taskActivityDetail.actualStartTime}">
						<p>Actual start time :<span class="${taskActivityDetail.taskActivityId}taskTimeEditDateLink ${taskActivityDetail.taskActivityId}taskTimeEditDateStartLink"><fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${taskActivityDetail.actualStartTime}"/></span>
						<input type="text" value='<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${taskActivityDetail.actualStartTime}"/>' style="display:none" class="hide dateTimeTaskEditPicker ${taskActivityDetail.taskActivityId}taskTimeEditDate ${taskActivityDetail.taskActivityId}taskTimeEditDateStart"/>
						
						<a href="javascript:void(0)"  onclick="hide showTaskActualTimeEditDate('${taskActivityDetail.taskActivityId}')"   class="${taskActivityDetail.taskActivityId}taskTimeEditDateLink hide"  style="margin-left: 10px">
						<span class="fa fa-pencil"></span></a></p>
					</c:if>
					<c:if test="${not empty taskActivityDetail.actualEndTime}">
						<p>Actual end time : <span class="${taskActivityDetail.taskActivityId}taskTimeEditDateLink ${taskActivityDetail.taskActivityId}taskTimeEditDateEndLink"><fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${taskActivityDetail.actualEndTime}"/></span> 
						<input type="text" value='<fmt:formatDate pattern = "yyyy/MM/dd HH:mm" value = "${taskActivityDetail.actualEndTime}"/>' style="display:none; margin-left: 3px" class="hide dateTimeTaskEditPicker ${taskActivityDetail.taskActivityId}taskTimeEditDate ${taskActivityDetail.taskActivityId}taskTimeEditDateEnd"/></p>
						
					</c:if>
					<c:if test="${not empty taskActivityDetail.recordedStartTime}">
						<p>In progress Comment
							:${taskActivityDetail.inprogressComment}</p>
					</c:if>
				</c:if>
				<c:if test="${not empty taskActivityDetail.actualEndTime}">
					<input type="hidden" class="${taskActivityDetail.taskActivityId}tskActTimeTakenInMins" value="${taskActivityDetail.durationInMinutes}"/>
					<p>Time Taken : <span class="${taskActivityDetail.taskActivityId}taskTimeEditLink ${taskActivityDetail.taskActivityId}taskTimeEditLinkHours"> ${taskActivityDetail.actualTimeTakenInHrs} Hours </span>
					<span class="${taskActivityDetail.taskActivityId}taskTimeEdit" style="display: none">
						<input type="text" placeholder="hh" class="durationTxt" id="taskActHHTimeEdit${taskActivityDetail.taskActivityId}"/>
						<input type="text" value="0" placeholder="mm" class="durationTxt" id="taskActMMTimeEdit${taskActivityDetail.taskActivityId}"/> 
					</span>
					<span style="text-align:right; display:none" class="${taskActivityDetail.taskActivityId}taskActTimeEdit">
						<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmit(${taskDetails.taskId},'${taskActivityDetail.taskActivityId}')" value="Save"><i class="fa fa-check fa-2x" style="color: green" aria-hidden="true"></i></button>
						<button style="background-color: transparent; border: 0px;" onclick="hideTaskActActualTimeEdit('${taskActivityDetail.taskActivityId}')" value="Cancel"><i class="fa fa-undo fa-2x" style="color: gray" aria-hidden="true"></i></button>
					</span>
					<c:if test="${(currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD') && not empty taskActivityDetail.recordedEndTime}">
						<a href="javascript:void(0)" class=" ${taskActivityDetail.taskActivityId}taskTimeEditLink" onclick="showTaskActActualTimeEdit('${taskActivityDetail.taskActivityId}')"  style="margin-left: 10px">
						<span class="fa fa-pencil"></span></a>
					</c:if>					
					
					</p>
				</c:if>

				<c:if test="${not empty taskActivityDetail.recordedEndTime}">
					<p>Completion Comment :${taskActivityDetail.completionComment}</p>
				</c:if>
				<div class="list-group-controls">
					<c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userGroupId == '1018'}">
					<button style="background-color: transparent; border: 0px;"
						data-box="#confirmationbox" class="mb-control deleteTaskActivity"
						onclick="deleteTaskActivity('${taskActivityDetail.taskActivityId}', '${taskActivityDetail.taskName}');">
						<i class="fa fa-times fa-2x" style="color: red" aria-hidden="true"></i>
					</button>
					</c:if>
				</div>
							</ul>
		</c:forEach>
	</c:if>
</div>