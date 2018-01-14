	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
	
	<spring:url value="/jsp/assets" var="assets" htmlEscape="true"/>
	<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
	<c:set var="applicationHome" value="${contextPath}/dashboard"/>
			    <c:if test="${not empty projectDetailsData}">
			    	<tr class="projectDuplicate${projectId}">
	        			<td colspan="11" style="width: 100%; border-bottom:1px dotted gray">
	        				<span class="duplicateTxt">Duplicate Projects</span>
	        			</td>
	        		</tr>
			    	<c:forEach var="project" items="${projectDetailsData}">
				      <c:set var="projectState" value="info"/>
	                  <c:if test="${project.status == 'COMPLETED' }">
	                   		<c:set var="projectState" value="success"/>
	                  </c:if>
	                  <c:if test="${project.status == 'NEW' }">
	                  </c:if>
	                  <c:if test="${project.projectMissedTimeLine == true }">
	                  		<c:set var="projectState" value="danger"/>
	                  </c:if>
	                  
                 		<tr class="projectDuplicate${projectId}">
		        			<td width="1%"></td>
				        	<td width="10%">${project.completionTime}</td>
				        	<td width="20%"><a href="${applicationHome}/project/${project.id}">${project.name}</a></td>
				        	<td width="10%">${project.teamName}</td>
				        	<td width="10%">${project.clientName}</td>
				        	<td width="10%" class="project_team">
							<c:if test="${not empty project.contributers}">
								<c:forEach var="contributer" items="${project.contributers}" varStatus="taskIndex"> 
										<img alt="image" src="${applicationHome}/image/${contributer}"  onerror="this.src='${assets}/images/users/no-image.jpg'">
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
				        	<td  width="12%">${project.description}</td>
				        	<td width="10%"><span class="label label-${projectState}">${project.status}</span></td>
				        	<td width="20%">
								<a href="#" style="margin-right: 7px;color:blue"  title="Clone this project"  data-toggle="modal" data-target="#createprojectmodal"
									onclick="loadProjectForClone('${project.id}')">
									<span class="fa fa-files-o fa-2x" ></span>
								</a>
								<a href="#" style="margin-right: 7px;color:darkgreen"  title="Edit this project"  data-toggle="modal" data-target="#createprojectmodal" 
									onclick="loadProjectForUpdate('${project.id}')">
									<span class="fa fa-pencil  fa-2x"></span>
								</a>
								<a href="#" data-box="#confirmationbox" style="color:red""  title="Delete this project" class="deleteProject mb-control profile-control-right" 
									onclick="deleteProject('${project.id}','${project.name}');">
									<span class="fa fa-times  fa-2x"></span>
								</a>
							</td>
			        	</tr>
					</c:forEach>
					</c:if>