	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="employeeMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
	<c:set var="allowProjectCreationOnlyForSuperAdmin" value="${applicationScope.applicationConfiguraion.allowProjectCreationOnlyForSuperAdmin}"/>
	<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
	
	<c:set var="applicationHome" value="${contextPath}/dashboard"/>
			    <c:if test="${not empty projectDetailsData}">
			    	<tr class="projectDuplicate${projectId}">
	        			<td colspan="11" style="width: 100%; border-bottom:1px dotted gray">
	        				<span class="duplicateTxt">Duplicate Projects</span>
	        			</td>
	        		</tr>
			    	<c:forEach var="project" items="${projectDetailsData}">
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
	                  
                 		<tr class="projectDuplicate${projectId} <c:if test='${project.deleted == true }'>archived</c:if>" id="projectData${project.id}">
		        			<td width="1%"></td>
		        			<td width="7%">${project.createdTime}</td>
				        	<td width="10%">${project.completionTime}</td>
				        	<td width="15%"  style="word-wrap:break-word"><a href="${applicationHome}/project/${project.id}">${project.name}</a></td>
				        	<td width="10%">${project.accountName}</td>
				        	<td width="8%" title="${project.teamName} - ${project.subTeamName} - ${project.clientName}">${project.teamName}</td>
				        	<td width="8%" title="${project.teamName} - ${project.subTeamName} - ${project.clientName}">${project.subTeamName}</td>
				        	<td width="10%" class="project_team">
							<c:if test="${not empty project.contributers}">
								<c:forEach var="contributer" items="${project.contributers}" varStatus="taskIndex"> 
										<img alt="image" src="${applicationHome}/image/${contributer}" title="${employeeMap[contributer].firstName}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
										<span class="hide">${employeeMap[contributer].firstName}</span>
								</c:forEach>
							</c:if>
							</td>
				        	<td width="5%">
				        	
				        	<c:if test="${not empty project.projectTaskDeatils}">
			            	<a  href="javascrip:void(0);" id="${taskDetails.taskId}" class="taskLink" onclick="taskLinkclick(${project.id}, event);" style="color:blue">${fn:length(project.projectTaskDeatils)} Tasks</a>
				            </c:if>
				            <c:if test="${empty project.projectTaskDeatils}">
				            	None
				            </c:if>
				        	</td>
				        	<td width="5%"><span class="label label-${projectState}" style="${statusColor}">${project.status}</span></td>
				        	<td width="6%">${project.actualDurationInHrs}</td>
				        	<td width="5%">${project.durationHrs}</td>
				        	<td width="18%">
				        	  <c:if test="${currentUser.userRole == 'SUPERADMIN' || (currentUser.userRole == 'ADMIN' && !allowProjectCreationOnlyForSuperAdmin)}">
						
								<a href="#" style="margin-right: 7px;color:blue"  title="Clone this project"  data-toggle="modal" data-target="#createprojectmodal"
									onclick="loadProjectForClone('${project.id}')">
									<span class="fa fa-files-o fa-2x" ></span>
								</a>
								<a href="#" style="margin-right: 7px;color:darkgreen"  title="Edit this project"  data-toggle="modal" data-target="#createprojectmodal" 
									onclick="loadProjectForUpdate('${project.id}')">
									<span class="fa fa-pencil  fa-2x"></span>
								</a>
								<%-- <a href="#" data-box="#confirmationbox" style="color:red""  title="Delete this project" class="deleteProject mb-control profile-control-right" 
									onclick="deleteProject('${project.id}','${project.name}');">
									<span class="fa fa-times  fa-2x"></span>
								</a>
								<a href="#" data-box="#confirmationbox" style="margin-left:7px; color:orange;"  title="Archive this project" class="deleteProject mb-control profile-control-right" 
									onclick="archiveProject('${project.id}','${project.name}');">
									<i class="fa fa-ban fa-2x" aria-hidden="true"></i>
								</a> --%>
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
					</c:forEach>
					</c:if>