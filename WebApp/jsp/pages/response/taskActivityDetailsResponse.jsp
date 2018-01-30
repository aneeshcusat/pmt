	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="applicationHome" value="${contextPath}/dashboard"/>
<input id="${taskDetails.taskId}name" type="hidden" value="${taskDetails.name}"/>
<input id="${taskDetails.taskId}isReviewTask" type="hidden"
	value="${taskDetails.reviewTask}" />
<input id="${taskDetails.taskId}timeTaken" type="hidden"
	value="${taskDetails.actualTimeTakenInHrs}" />
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
<input id="${taskDetails.taskId}assignee" type="hidden"
	value="${taskDetails.assignee}" />
<input id="${taskDetails.taskId}assigneeName" type="hidden"
	value="${userDetailsMap[taskDetails.assignee].firstName}" />
<input id="${taskDetails.taskId}helper" type="hidden"
	value="${taskDetails.helpersList}" />
<c:set var="helpersNames" value="" />
<input id="${taskDetails.taskId}helperNames" type="hidden"
	value="${taskDetails.helperNames}" />

<div id="taskDetailsContent${taskDetails.taskId}" class="hide">
 		<ul href="#" class="list-group-item">  
									         
		<span class="contacts-title">${taskDetails.name}</span>
		<p>${taskDetails.description}</p>
		<p>Est Duration : <b>${taskDetails.duration}</b> Hours</p>
		<p>
			Time Taken : <span  class="${taskDetails.taskId}taskTimeEditLink"><b>${taskDetails.actualTimeTakenInHrs}</b> Hours </span>
			<input type="text" placeholder="hh" value="" class="durationTxt ${taskDetails.taskId}taskTimeEdit"/>
			<input type="text" placeholder="mm" class="durationTxt ${taskDetails.taskId}taskTimeEdit" value=""/>
			<span style="text-align:right; display:none" class="${taskDetails.taskId}taskTimeEdit">
				<button style="background-color: transparent; border: 0px;" onclick="taskActualTimeSubmit('${taskDetails.taskId}')" value="Save"><i class="fa fa-check fa-2x" style="color: green" aria-hidden="true"></i></button>
				<button style="background-color: transparent; border: 0px;" onclick="hideTaskActualTimeEdit('${taskDetails.taskId}')" value="Cancel"><i class="fa fa-undo fa-2x" style="color: gray" aria-hidden="true"></i></button>
			</span>
			<a href="javascript:void(0)" onclick="showTaskActualTimeEdit('${taskDetails.taskId}')" style="margin-left: 10px" class="${taskDetails.taskId}taskTimeEditLink"><span class="fa fa-pencil"></span></a></p>
		<c:if test="${not taskDetails.extraTimeTask}">
			<p>	Est Start Time : <b>${taskDetails.startTime}</b></p>
			<p>Est Complete Time : ${taskDetails.completionTime}</p>
			<p>Review : ${taskDetails.reviewTask}</p>
		</c:if>
	</ul>

	<c:if test="${not empty taskDetails.taskActivityDetails}">
		<c:forEach var="taskActivityDetail"
			items="${taskDetails.taskActivityDetails}" varStatus="taskIndex">
			<ul
				class="list-group-item taskActivityTime${taskActivityDetail.taskActivityId}">
				<img alt="image"
					src="${applicationHome}/image/${taskActivityDetail.userId}"
					class="pull-left"
					onerror="this.src='${assets}/images/users/no-image.jpg'">
				<span class="contacts-title">${taskActivityDetail.userTaskType}
					Task Activity</span>
				<c:choose>
					<c:when test="${not empty taskActivityDetail.actualEndTime}">
						<p>Status : COMPLETED</p>
					</c:when>
					<c:when test="${not empty taskActivityDetail.actualStartTime}">
						<p>Status : INPROGRESS</p>
					</c:when>
					<c:when test="${empty taskActivityDetail.actualStartTime}">
						<p>Status : NEW</p>
					</c:when>
				</c:choose>
				<c:if test="${not taskDetails.extraTimeTask}">
				
					<c:if test="${not empty taskActivityDetail.recordedStartTime}">
						<p>Recorded start time
							:${taskActivityDetail.recordedStartTime}</p>
					</c:if>
					<c:if test="${not empty taskActivityDetail.recordedEndTime}">
						<p>Recorded end time :${taskActivityDetail.recordedEndTime}</p>
					</c:if>
					
					<c:if test="${not empty taskActivityDetail.actualStartTime}">
						<p>Actual start time :<span class="${taskActivityDetail.taskActivityId}taskTimeEditDateLink ${taskActivityDetail.taskActivityId}taskTimeEditDateStartLink">${taskActivityDetail.actualStartTime}</span>
						<input type="text" value="" style="display:none" class="dateTimeTaskEditPicker ${taskActivityDetail.taskActivityId}taskTimeEditDate ${taskActivityDetail.taskActivityId}taskTimeEditDateStart"/>
						
						<a href="javascript:void(0)"  onclick="showTaskActualTimeEditDate('${taskActivityDetail.taskActivityId}')"   class="${taskActivityDetail.taskActivityId}taskTimeEditDateLink"  style="margin-left: 10px">
						<span class="fa fa-pencil"></span></a></p>
					</c:if>
					<c:if test="${not empty taskActivityDetail.actualEndTime}">
						<p>Actual end time : <span class="${taskActivityDetail.taskActivityId}taskTimeEditDateLink ${taskActivityDetail.taskActivityId}taskTimeEditDateEndLink">${taskActivityDetail.actualEndTime}</span> 
						<input type="text" value="" style="display:none; margin-left: 3px" class="dateTimeTaskEditPicker ${taskActivityDetail.taskActivityId}taskTimeEditDate ${taskActivityDetail.taskActivityId}taskTimeEditDateEnd"/></p>
						
					</c:if>
					<c:if test="${not empty taskActivityDetail.recordedStartTime}">
						<p>In progress Comment
							:${taskActivityDetail.inprogressComment}</p>
					</c:if>
				</c:if>
				<c:if test="${not empty taskActivityDetail.actualEndTime}">
					<p>Time Taken : <span class="${taskActivityDetail.taskActivityId}taskTimeEditLink"> ${taskActivityDetail.actualTimeTakenInHrs} Hours </span>
					<input type="text" value="" placeholder="hh" class="durationTxt ${taskActivityDetail.taskActivityId}taskTimeEdit"/>
					<input type="text" placeholder="mm" class="durationTxt ${taskActivityDetail.taskActivityId}taskTimeEdit" value=""/> 
					<a href="javascript:void(0)" class=" ${taskActivityDetail.taskActivityId}taskTimeEditLink" onclick="showTaskActActualTimeEdit('${taskActivityDetail.taskActivityId}')"  style="margin-left: 10px">
					<span class="fa fa-pencil"></span></a></p>
				</c:if>

				<c:if test="${not empty taskActivityDetail.recordedEndTime}">
					<p>Completion Comment :${taskActivityDetail.completionComment}</p>
				</c:if>
				<div class="list-group-controls">
					<button style="background-color: transparent; border: 0px;"
						data-box="#confirmationbox" class="mb-control deleteTaskActivity"
						onclick="deleteTaskActivity('${taskActivityDetail.taskActivityId}', '${taskActivityDetail.taskName}');">
						<i class="fa fa-times fa-2x" style="color: red" aria-hidden="true"></i>
					</button>
				</div>
				<p style="text-align:right; display:none" class="${taskActivityDetail.taskActivityId}taskActTimeEdit">
				<button style="background-color: transparent; border: 0px;" onclick="taskActActualTimeSubmit('${taskActivityDetail.taskActivityId}')" value="Save"><i class="fa fa-check fa-2x" style="color: green" aria-hidden="true"></i></button>
				<button style="background-color: transparent; border: 0px;" onclick="hideTaskActActualTimeEdit('${taskActivityDetail.taskActivityId}')" value="Cancel"><i class="fa fa-undo fa-2x" style="color: gray" aria-hidden="true"></i></button>
				</p>
			</ul>
		</c:forEach>
	</c:if>
</div>