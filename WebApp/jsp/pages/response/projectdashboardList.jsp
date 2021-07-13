<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>	
<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="allowProjectCreationOnlyForSuperAdmin" value="${applicationScope.applicationConfiguraion.allowProjectCreationOnlyForSuperAdmin}"/>
<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
 <div class="col-xs-12">
			<table class="table table-responsive table-hover">
			  <thead>
			        <tr>
			        	<th width="1%"></th>
			        	<th width="7%">Created Date</th>
			        	<th width="10%">Delivery Time</th>
			        	<th width="15%">Project Name</th>
			        	<th width="10%">Account</th>
			        	<th width="8%">Team</th>
			        	<th width="8%">Sub Team</th>
			        	<th width="10%">Assignees</th>
			        	<th width="5%">Tasks</th>
			        	<th width="5%">Status</th>
			        	<th width="6%">Time Taken</th>
			        	<th width="5%">Est Hrs</th>
			        	<th width="18%">
			        	  <c:if test="${currentUser.userRole == 'SUPERADMIN' || (currentUser.userRole == 'ADMIN' && !allowProjectCreationOnlyForSuperAdmin)}">
							
			        	<span class="dropdown">
					          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Actions <span class="caret"></span></a>
					          <ul class="dropdown-menu deleteProjectDropDown hide">
					            <li class="deleteProjectDropDownLink hide"> 
					            	<a href="#" data-box="#confirmationbox"  title="Delete this project" class="deleteProject mb-control profile-control-right" 
									onclick="deleteProjects();">
									<span class="fa fa-trash-o"  style="color:red" aria-hidden="true"></span>Delete Projects
								</a>
								</li>
								
								 <li class="archiveProjectDropDownLink hide">
								 	<a href="#" data-box="#confirmationbox"   title="Archive this project" class="deleteProject mb-control profile-control-right" 
										onclick="archiveProjects();">
										<span class="fa fa-ban" aria-hidden="true" style="color:orange;"></span>Archive Projects
									</a>
								</li>
								
							 <li class="undoArchiveProjectDropDownLink hide">
							 	<a href="#" data-box="#confirmationbox"   title="Undo Archive this project" class="undoarchive deleteProject mb-control profile-control-right" 
									onclick="undoArchiveProject();">
									<span class="fa fa-ban" aria-hidden="true" style="color:blue;"></span>Undo Archive Projects
								</a>
							</li>
							
			        		</ul>
			        	</span>
			        	</c:if>
			        	<c:if test="${currentUser.userRole != 'SUPERADMIN' && currentUser.userRole != 'ADMIN'}">
			        		Actions
			        	</c:if>
			        	</th>
			        </tr>
			    </thead>
			    <c:if test="${not empty modelViewMap.projectDetailsData}">
			    <tbody>
			    	<c:forEach var="project" items="${modelViewMap.projectDetailsData}">
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
			        <tr class="clickable projectData ${project.id} <c:if test='${project.deleted == true }'>archived</c:if>" id="projectData${project.id}" data-projectId="${project.id}"  style="">
			            <td  onclick="loadDuplicateProjects(${project.id}, '${project.code}', false)" data-toggle="collapse" data-target=".projectData${project.id}"><i id="projectOpenLink${project.id}" style="color: blue" class="fa fa-chevron-right 2x"></i></td>
			            <td>${project.createdTime}</td>
			            <td>${project.completionTime}</td>
			            <td style=" word-wrap:break-word"><a href="${applicationHome}/project/${project.id}">${project.name}</a></td>
			            <td>${project.accountName}</td>
			            <td title="${project.teamName} - ${project.subTeamName} - ${project.clientName}">${project.teamName}</td>
			            <td title="${project.teamName} - ${project.subTeamName} - ${project.clientName}">${project.subTeamName}</td>
			           <td class="project_team">
						<c:if test="${not empty project.contributers}">
							<c:forEach var="contributer" items="${project.contributers}" varStatus="taskIndex"> 
									<img alt="image" src="${applicationHome}/image/${contributer}"  data-toggle="tooltip" data-html="true"  title="${employeeMap[contributer].firstName}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
									<span class="hide">${employeeMap[contributer].firstName}</span>
							</c:forEach>
						</c:if>
						</td>
			            <td>
			            <c:if test="${not empty project.projectTaskDeatils}">
			            	<a  href="javascrip:void(0);" id="${taskDetails.taskId}" class="taskLink" onclick="taskLinkclick(${project.id}, event);" style="color:blue">${fn:length(project.projectTaskDeatils)} Tasks</a>
			            </c:if>
			            <c:if test="${empty project.projectTaskDeatils}">
			            		None
			            </c:if>
			            </td>
			            
			          	<td><span class="label label-${projectState}" style="${statusColor}">${project.status}</span></td>  
			            <td>${project.actualDurationInHrs}</td>
			            <td>${project.durationHrs}</td>
			            <td>
			              <c:if test="${currentUser.userRole == 'SUPERADMIN' || (currentUser.userRole == 'ADMIN' && !allowProjectCreationOnlyForSuperAdmin)}">
							
							<a href="#" style="margin-right: 7px;color:darkgreen"  title="Edit this project"  data-toggle="modal" data-target="#createprojectmodal" 
								onclick="loadProjectForUpdate('${project.id}')">
								<span class="fa fa-pencil  fa-2x"></span>
							</a>
							
							<a href="#" style="margin-left: 7px;color:blue"  title="Recurring Project"  data-toggle="modal" data-target="#recurringprojectmodal" 
								onclick="recurringProjectModel('${project.code}', '${project.id}')">
								<span class="fa fa-recycle fa-2x recurringSpin${project.code}" ></span>
							</a>
								
							<c:if test='${project.deleted == true }'>
								<span class="hide archivedProject${project.id}">Archived</span>
								<input type="checkbox" class="prjectDeleteArchive archivedProject prjectDeleteArchive${project.id}"  style="margin-left: 7px" data-projectId="${project.id}"/>
							</c:if>
							<c:if test='${project.deleted != true }'>
								<input type="checkbox" class="prjectDeleteArchive prjectDeleteArchive${project.id}"  style="margin-left: 7px" data-projectId="${project.id}"/>
							</c:if>
							</c:if>
						</td>
			        </tr>
					<tr class="collapse projectDataHidden projectData${project.id}">
					<td colspan="13" style="width: 100%; border-bottom:1px dotted gray">
						<span class="duplicateTxt">Quick Project Cloning</span>
					</td>
					</tr>
					
					 <tr class=" collapse projectDataHidden projectData${project.id}" style="background-color: white">
						<td colspan="13">
							<table style="width: 100%" class="table" id="projectDetails${project.id}">
							<tbody>
							<tr class="collapse projectDataHidden projectData${project.id}">
	        			<td colspan="13">
	        				<!-- task creator start -->
	        				<div class="col-xs-12">
									<div class="row">
										<div class="col-md-3">
											<div class="form-group">
												<label class="col-md-4 control-label">Name</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput" value="${project.name}"
														id="prjName${project.id}" /> <span class="help-block"></span>
													<input type="hidden" value="${project.category}" id="projectCategory${project.id}"/>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">Duration</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput durationHrs"  value="${project.durationHrs}" data-projectId="${project.id}"
														id="prjDuration${project.id}"/>
														<span class="help-block" id="projectDurationMsg${project.id}"></span>
												</div>
											</div>
										</div>
								
										<div class="col-md-3">
											<div class="form-group">
												<label class="col-md-4 control-label">Start Date</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput estStartTime" data-projectId="${project.id}"
														id="prjStartTime${project.id}" /> <span class="help-block"></span>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">Due Date</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput estCompleteTime" data-projectId="${project.id}"
														id="prjEndTime${project.id}"/>
												</div>
											</div>
										</div>
										<div class="col-md-5">
											<!-- task assign start -->
											<div class="row" id="projectTaskCloneDIv${project.id}">
											<%-- <%@include file="quicktaskreassign.jsp"%> --%>
											</div>
											<!-- task assign start -->
										</div>
										<div class="col-md-1">
										 <c:if test="${currentUser.userRole == 'SUPERADMIN' || (currentUser.userRole == 'ADMIN' && !allowProjectCreationOnlyForSuperAdmin)}">
						
											<div class="col-md-12 col-xs-12">
												<a href="javascript:void(0)" style="color:blue"
													title="Clone this project" data-toggle="modal"
													data-target="#createprojectmodal"
													onclick="loadProjectForClone('${project.id}')">
													<span class="fa fa-external-link fa-3x"></span>
												</a>
												<span class="help-block"></span>
											</div>
											<div class="col-md-12 col-xs-12">
												<a href="javascript:void(0)" class="mb-control quickProjectDuplicateMsg" data-box="#confirmationbox"  onclick="createDuplicateProjectWithTask('${project.id}','${project.code}');" style="color:green">
													<span class="fa fa-save fa-3x"></span>
												</a>
											</div>
								</c:if>
										</div>
									</div>
								</div>
	        				<!--  task creator end -->
	        			</td>
        			</tr>
							</tbody>
							</table>
				    	</td>
				    </tr>
					    	
			        
			      </c:forEach>
			    </tbody>
			</c:if>
			</table>
	 	 </div>
