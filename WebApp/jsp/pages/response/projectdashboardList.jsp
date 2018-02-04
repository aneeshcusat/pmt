<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	
<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="applicationHome" value="${contextPath}/dashboard"/>
 <div class="col-xs-12">
			<table class="table table-responsive table-hover">
			  <thead>
			        <tr>
			        	<th width="1%"></th>
			        	<th width="10%">Delivery Date</th>
			        	<th width="20%">Project Name</th>
			        	<th width="10%">Team</th>
			        	<th width="10%">Client</th>
			        	<th width="10%">Assignees</th>
			        	<th width="5%">Tasks</th>
			        	<th width="12%">Comments</th>
			        	<th width="10%">Status</th>
			        	<th width="20%">Actions</th>
			        </tr>
			    </thead>
			    <c:if test="${not empty modelViewMap.projectDetailsData}">
			    <tbody>
			    	<c:forEach var="project" items="${modelViewMap.projectDetailsData}">
				      <c:set var="projectState" value="info"/>
	                  <c:if test="${project.status == 'COMPLETED' }">
	                   		<c:set var="projectState" value="success"/>
	                  </c:if>
	                  <c:if test="${project.status == 'NEW' }">
	                  </c:if>
	                  <c:if test="${project.projectMissedTimeLine == true }">
	                  		<c:set var="projectState" value="danger"/>
	                  </c:if>
			        <tr class="clickable projectData ${project.id}" id="projectData${project.id}">
			            <td  onclick="loadDuplicateProjects(${project.id}, '${project.code}', false)" data-toggle="collapse" data-target=".projectData${project.id}"><i id="projectOpenLink${project.id}" style="color: blue" class="fa fa-chevron-right 2x"></i></td>
			            <td>${project.completionTime}</td>
			            <td><a href="${applicationHome}/project/${project.id}">${project.name}</a></td>
			            <td>${project.teamName}</td>
			            <td>${project.clientName}</td>
			           <td class="project_team">
						<c:if test="${not empty project.contributers}">
							<c:forEach var="contributer" items="${project.contributers}" varStatus="taskIndex"> 
									<img alt="image" src="${applicationHome}/image/${contributer}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
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
			            
			            <td>${project.description}</td>
			          	<td><span class="label label-${projectState}">${project.status}</span></td>  
			            <td>
							<a href="#" style="margin-right: 7px;color:darkgreen"  title="Edit this project"  data-toggle="modal" data-target="#createprojectmodal" 
								onclick="loadProjectForUpdate('${project.id}')">
								<span class="fa fa-pencil  fa-2x"></span>
							</a>
							<a href="#" data-box="#confirmationbox" style="color:red""  title="Delete this project" class="deleteProject mb-control profile-control-right" 
								onclick="deleteProject('${project.id}','${project.name}');">
								<span class="fa fa-times  fa-2x"></span>
							</a>
							
							<a href="#" data-box="#confirmationbox" style="margin-left:7px; color:orange;"  title="Archive this project" class="deleteProject mb-control profile-control-right" 
									onclick="archiveProject('${project.id}','${project.name}');">
									<i class="fa fa-ban fa-2x" aria-hidden="true"></i>
							</a>
							
							<a href="#" style="margin-left: 7px;color:blue"  title="Recurring Project"  data-toggle="modal" data-target="#recurringprojectmodal" 
								onclick="recurringProjectModel('${project.code}', '${project.id}')">
								<span class="fa fa-recycle fa-2x recurringSpin${project.code}" ></span>
							</a>
							
							
						</td>
			        </tr>
					<tr class="collapse projectDataHidden projectData${project.id}">
					<td colspan="11" style="width: 100%; border-bottom:1px dotted gray">
						<span class="duplicateTxt">Quick Project Cloning</span>
					</td>
					</tr>
					
					 <tr class=" collapse projectDataHidden projectData${project.id}" style="background-color: white">
						<td colspan="11">
							<table style="width: 100%" class="table" id="projectDetails${project.id}">
							<tbody>
							<tr class="collapse projectDataHidden projectData${project.id}">
	        			<td colspan="11">
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
													<input type="text" class="form-control cloneInput"  value="${project.duration}"
														id="prjDuration${project.id}"/>
														<span class="help-block" id="projectDurationMsg${project.id}"></span>
												</div>
											</div>
										</div>
								
										<div class="col-md-3">
											<div class="form-group">
												<label class="col-md-4 control-label">Start Date</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput estStartTime"
														id="prjStartTime${project.id}" /> <span class="help-block"></span>
												</div>
											</div>
											<div class="form-group">
												<label class="col-md-4 control-label">Due Date</label>
												<div class="col-md-8 col-xs-12">
													<input type="text" class="form-control cloneInput estCompleteTime"
														id="prjEndTime${project.id}"/>
												</div>
											</div>
										</div>
										<div class="col-md-5">
											<!-- task assign start -->
											
											<%@include file="quicktaskreassign.jsp"%>
											
											<!-- task assign start -->
										</div>
										<div class="col-md-1">
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
												<a href="javascript:void(0)" onclick="createDuplicateProjectWithTask('${project.id}','${project.code}');" style="color:green">
													<span class="fa fa-save fa-3x"></span>
												</a>
											</div>
								
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
