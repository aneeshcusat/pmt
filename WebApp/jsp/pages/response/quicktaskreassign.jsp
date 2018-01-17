<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="userMap" value="${applicationScope.applicationConfiguraion.userList}"/>
	<div class="row" id="projectTaskCloneDIv${project.id}">
		<div class="col-md-12">
			<div class="form-group">
					<label class="col-md-4 control-label">Task Name</label> <label
					class="col-md-3 control-label">Assignee</label> <label
					class="col-md-2 control-label">Duration</label> <label
					class="col-md-2 control-label">Type</label> <label
					class="col-md-1 control-label"> <a href="javascript:void(0)" onclick="createNewAssignSection(${project.id})"><span
						class="fa fa-plus" style="color: blue"></span></a></label>
			</div>
		</div>
		
		<c:if test="${not empty project.projectTaskDeatils}">
		 <c:forEach var="taskDetails" items="${project.projectTaskDeatils}">
		<div class="col-md-12 taskDetails" id="taskId${taskDetails.taskId}" data-taskId="${taskDetails.taskId}">
			<div class="form-group">
				<div class="col-md-4 col-xs-12">
					<input type="text" class="form-control cloneInput tskName" value="${taskDetails.name}"/> <span class="help-block"></span>
				</div>
				<div class="col-md-3 col-xs-12">
				 	<select  name="taskAssignee" class="form-control cloneInput taskCloneAssignee" data-projectId="${project.id}" data-taskId="${taskDetails.taskId}" data-live-search="true">
					<option value="">- select -</option>
					<c:if test="${not empty userMap}">
  						<c:forEach var="user" items="${userMap}">
  								<c:if test="${taskDetails.assignee eq user.id}">
  								<option selected="selected" value="${user.id}">${user.firstName}</option>
  								</c:if>
  								<c:if test="${taskDetails.assignee ne user.id}">
  								<option value="${user.id}">${user.firstName}</option>
  								</c:if>
  						</c:forEach>
  						</c:if>
					</select>
					 <span class="availabilityStatus avaStatus${taskDetails.taskId}"></span>
					 <input type="hidden" class="availabilityTime avaInputStatus${taskDetails.taskId}"></input>
				</div>
				<div class="col-md-2 col-xs-12">
					<input type="text" class="form-control cloneInput tskDuration"
						value="${taskDetails.duration}"/>
				</div>
				<div class="col-md-2 col-xs-12">
						<select class="form-control cloneInput tskType">
						<c:if test="${not empty taskDetails.reviewTask && taskDetails.reviewTask}">
							<option value="0">P</option>
							<option value="1" selected="selected">R</option>
						</c:if>
						
						<c:if test="${not empty taskDetails.reviewTask && !taskDetails.reviewTask}">
							<option value="0" selected="selected">P</option>
							<option value="1">R</option>
						</c:if>
						
					</select>
				</div>
				<div class="col-md-1 col-xs-12">
					<a href="javascript:void(0)" class="deleteCloneTaskLink" onclick="deleteCloneTaskItem('taskId${taskDetails.taskId}')"><span class="fa fa-times" style="color: red"></span></a>
				</div>
			</div>
		</div>
		 </c:forEach>
		</c:if>
	</div>
	