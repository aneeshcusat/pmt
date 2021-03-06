<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="currentUserGroupId" value="${applicationScope.applicationConfiguraion.currentUserGroupId}"/>
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>
<c:set var="sameDayOnlyTaskEnabled" value="${applicationScope.applicationConfiguraion.sameDayOnlyTaskEnabled}"/>
<c:set var="taskPrjectCategoryEnabled" value="${applicationScope.applicationConfiguraion.taskPrjectCategoryEnabled}"/>
<c:set var="taskProjectMappings" value="${applicationScope.applicationConfiguraion.taskProjectCategoryMappings}"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/projectdetails.css?v=${fsVersionNumber}"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/cron/jquery-cron.css"/>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/gentleSelect/jquery-gentleSelect.css"/>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li><a href="${applicationHome}/projects">Projects</a></li>                  
     <li class="active">${projectDetails.name}</li>
 </ul>
 <!-- END BREADCRUMB -->  
	
<c:if test="${projectDetails == null or currentUserGroupId != projectDetails.userGroupId}">
<div class="content-frame" style=" min-height: 500px;">    
	<div class="content-frame-top" style="padding:0 0 0 0">  
		<div class="row">
                  <div class="col-md-7">                      
            <h4><span style="padding-top: 10px;" class="fa ">Invalid Project Id</span></h4>
            </div>
       </div>
	</div>     
</div>
</c:if>
<c:if test="${projectDetails != null and currentUserGroupId == projectDetails.userGroupId}">
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top" style="padding:0 0 0 0">  
		<div class="row">
                  <div class="col-md-7">                      
            <h4><span style="padding-top: 10px;" class="fa ">${projectDetails.code}</span> - <span class="editableFieldText">${projectDetails.name}</span></h4>
            </div>
          <div class="col-md-5 text-right hide">               
           <a href="#" class="btn  btn-default">Complete</a>
            </div>
       </div>
	</div>        
	      <div class="row">
	      
                  <div class="col-md-8">
                      <section class="panel">
                         <!--  <div class="project project-heading">
                              <strong> Sub Tasks</strong>
                              <p>Sometimes the simplest things are the hardest to find.dddddddd dsdsasds Sometimes the simplest things are the hardest to find. 
                              Sometimes the simplest things are the hardest to find. 
                              Sometimes the simplest things are the hardest to find. Sometimes the simplest things are the hardest to find. Sometimes the simplest t
                              hings are the hardest to find.</p>
                          </div> -->
                          <div class="panel-body bio-graph-info">
                              <!--<h1>New Dashboard BS3 </h1>-->
                              <div class="row project_details">
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
                                  <div class="col-md-6">
                                      <p><span class="bold">Created by </span>: ${projectDetails.reporterName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p class="text-capitalize"><span class="bold">Status </span>: <span class="label label-${projectState}" style="${statusColor}">${projectDetails.status}</span></p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Created </span>: ${projectDetails.createdDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Last Updated</span>: ${projectDetails.lastModifiedDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p class="text-capitalize"><span class="bold">Project Type </span>: ${projectDetails.type}</p>
                                  </div>
                                  <div class="col-md-6 hide">
                                      <p class="text-capitalize"><span class="bold text-capitalize">Project Complexity </span>: ${projectDetails.complexity}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Category </span>: ${projectDetails.category}</p>
                                  </div>
                                   <c:if test="${currentUserGroupId != 1018}">
                                   <div class="col-md-6">
                                      <p><span class="bold">New Category </span>: ${projectDetails.newCategory}</p>
                                  </div>
                                   <div class="col-md-6">
                                      <p><span class="bold">SOW Line Item </span>: ${projectDetails.sowLineItem}</p>
                                  </div>
                                   <div class="col-md-6">
                                      <p><span class="bold">Order book Ref No. </span>: ${projectDetails.orderBookRefNo}</p>
                                  </div>
                                  
                                  <div class="col-md-6">
                                      <p><span class="bold">Proposal No.</span>: ${projectDetails.proposalNo}</p>
                                  </div>
                                  
	                              </c:if>

                                  <div class="col-md-6">
                                      <p><span class="bold">Onshore/ Offshore </span>: ${projectDetails.projectLocation}</p>
                                  </div>

                                  <div class="col-md-6">
                                      <p><span class="bold">Account </span>: ${projectDetails.accountName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Team </span>: ${projectDetails.teamName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Sub Team </span>: ${projectDetails.subTeamName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Client </span>: ${projectDetails.clientName}</p>
                                  </div>
                                   <div class="col-md-6 hide">
                                   	<ul class="nav nav-pills nav-stacked labels-info ">
                                 	 <li><span class="bold">Priority </span> : <i class=" fa fa-circle text-danger"></i> ${projectDetails.priority}<p></p></li>
	                              </ul>
	                              </div>
	                              <input type="hidden" id="hdn_project_id" value="${projectDetails.id}">
	                              <c:if test="${projectDetails.status != 'NEW'}">
                                  
									</c:if>
                                  <div class="col-lg-12">
                                     
                                     <div class="panel panel-default panel-stats">
										<div class="panel-body no-padding">
											<div class="row">
												<div class="col-md-12">
													<div class="panel-data panel-progress">
														<div class="progress">
														
														  <c:set var="progressState" value="striped" />
						                  				<c:if test="${projectDetails.status == 'INPROGRESS' }">
						                   					<c:set var="progressState" value="striped active"/>
						                  				</c:if>
						                  				<c:if test="${projectDetails.status == 'COMPLETED' }">
						                   					<c:set var="progressState" value=""/>
						                  				</c:if>
		                  								 <div class="progress progress-${progressState}">
														<div class="progress-bar progress-bar-${projectState}" role="progressbar" aria-valuenow="${projectDetails.projectCompletionPercentage}" aria-valuemin="0" aria-valuemax="100" style="width: ${projectDetails.projectCompletionPercentage}%;">
													    	${projectDetails.projectCompletionPercentage}%
														</div>
														</div>
														</div>
														<div class="progress-meta clearfix">
															<span class="col-md-6 col-sm-6 text-left"><strong>Milestone:</strong> <c:if test="${projectDetails.completionInDays >= 0}">${projectDetails.completionInDays}</c:if><c:if test="${projectDetails.completionInDays < 0}">${projectDetails.completionInDays * -1}</c:if> Days 
															<c:if test="${projectDetails.completionInDays >= 0}"> Left</c:if><c:if test="${projectDetails.completionInDays < 0}"> Ago</c:if>
															</span>
															<span class="col-md-6 col-sm-6 text-right"><strong>Due:</strong>
															<fmt:parseDate value = "${projectDetails.completionTime}" var = "parsedDate" pattern = "yyyy/MM/dd HH:mm"/>
															<fmt:formatDate pattern = "MMM dd YYYY" value = "${parsedDate}"/></span>
														</div>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-12">
													<div class="col-md-4 col-sm-4">
														<div class="panel-data">
															${projectDetails.noOfTasks}	
															<span>Tasks</span>
														</div>
													</div>
													<div class="col-md-4 col-sm-4">
														<div class="panel-data">
															<c:choose>
															<c:when test="${not empty projectDetails.filesNames && not empty projectDetails.completedFilesNames}">
																${projectDetails.filesNames.size() + projectDetails.completedFilesNames.size()}
															</c:when>
															<c:when test="${not empty projectDetails.filesNames}">
																${projectDetails.filesNames.size()}
															</c:when>
															<c:when test="${not empty projectDetails.completedFilesNames}">
																${projectDetails.completedFilesNames.size()}
															</c:when>
															<c:otherwise>
																0
															</c:otherwise>	
															</c:choose>		
															<span>Assets</span>
														</div>
													</div>
													<div class="col-md-4 col-sm-4">
														<div class="panel-data">
															${projectDetails.contributers.size()}
															<span>Contributers</span>
														</div>
													</div>
												</div>
											</div>
									</div>
                                  </div>
                              </div>
                              <div class="row">
	                    <div class="col-md-12">
	                    <h5 class="bold">Description</h5>
	                      <p>
                                  <span class="editableFieldTextArea">${projectDetails.description}</span>
                              </p>
	                    </div>
	                    </div>
                              
						<div class="row">
	                    <div class="col-md-12">
	                    	  <div class="form-group">
                                <label class="col-md-2 control-label">Attachments</label>
                                <div class="col-md-12">
                                   <div class="dropzone dropzone-mini dz-clickable" id="my-dropzone">
							        </div>
								</div>
								</div>
                    	</div>
                    </div>
                          </div>

                      </section>
						<div class="col-md-12">
						<h4>Comments (${projectDetails.projectComments.size()})</h4>
							<div class="row">
							  <div class="messages messages-img">
							  <c:if test="${not empty projectDetails.projectComments}">
                                 <c:forEach var="comment" items="${projectDetails.projectComments}"> 
					              <div class="item">
					                <div class="image">
					                    <img src="${comment.user.filePhoto}" alt="${comment.user.firstName} ${comment.user.lastName}" onerror="this.src='${assets}/images/users/no-image.jpg'">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">${comment.user.firstName} ${comment.user.lastName}</a>
					                        <span class="date">${comment.createdDate}</span>
					                    </div>                                    
					                   ${comment.description}
					                </div>
					            </div>
					         </c:forEach>
					         </c:if>  
                                
                        <div class="panel-body panel-body-search">
                            <div class="input-group">
                                <div class="input-group-btn">
                                    
                                </div>
                               <textarea class="form-control" placeholder="Enter your comments" id="comment_textarea"></textarea>
                                <div class="input-group-btn" style="padding-left: 5px">
                                    <button class="btn btn-primary" onclick="addComment()">Add</button>
                                </div>
                            </div>
                        </div>
					       </div>
							
							</div>
						</div>
                      <section class="panel">
                        <header class="panel-heading">
                          Last Activity
                        </header>
                        <div class="panel-body">
                            <table class="table table-hover p-table">
                          <thead>
                          <tr>
                              <th>Title</th>
                              <th>Updated Time</th>
                              <th>Comments</th>
                              <th>Status</th>
                          </tr>
                          </thead>
                          <tbody>
                           <c:if test="${not empty projectDetails.projectActivityDetails}">
                                 <c:forEach var="activities" items="${projectDetails.projectActivityDetails}"> 
                          <tr>
                              <td>
                                  Project ${activities.projectActivityType}
                              </td>
                              <td>
                                  ${activities.modifiedDate}
                              </td>
                              <td>
                              <c:if test="${activities.projectActivityType == 'CREATED'}">
                              	${activities.userName} has created the project.
                              </c:if>
                              
                               <c:if test="${activities.projectActivityType == 'PROJECT_DETAILS_UPDATED'}">
                              	${activities.userName} has been updated.
                              </c:if>
                              
                              <c:if test="${activities.projectActivityType == 'TASK_ADDED'}">
                              	${activities.userName} has created the task.
                              </c:if>
                              
                               <c:if test="${activities.projectActivityType == 'TASK_UPDTED'}">
                              	${activities.userName} has been updated.
                              </c:if>
                              
                               <c:if test="${activities.projectActivityType == 'COMMENT_ADDED'}">
                              	${activities.userName} has added a comment.
                              </c:if>
                               <c:if test="${activities.projectActivityType == 'FILE_UPLOADED'}">
                              	${activities.userName} has been uploaded.
                              </c:if>
                              </td>
                              <td>
                                  <span class="label label-info"> ${activities.projectStatus}</span>
                              </td>
                          </tr>
                          </c:forEach>
                          </c:if>
                          </tbody>
                          </table>
                        </div>
                      </section>

                  </div>
                  <div class="col-md-4">
                      <section class="panel">
                          <div class="panel-body" style="padding: 5px">
                           <div class="row padding-bottom-5" >
                          <div class="col-md-3">
                           <h5 class="bold">Tasks (${projectDetails.noOfTasks})</h5>
                          </div>
                          
                           <div class="col-md-4 text-right">
                            <c:if test="${!projectDetails.projectDateExceed && (currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD')}">
                            <a data-toggle="modal"  data-backdrop="static" data-target="#createtaskmodal" onclick="clearTaskDetails();" class="btn btn-success line-height-15" 
                            >
                               <span class="fa fa-plus"></span> Create a Task</a>
                               </c:if>
                            </div>
                            
                          <div class="col-md-5">
								<div class="input-group text-right">
									<input type="text" class="form-control" id="taskDetailsSearch" style="height: 25px" placeholder="Search for a task...">
								</div>
							</div>
                          
                          </div>
                              <table class="table taskDetailsTable" >
                                        <tbody>
                                        
                                        <c:if test="${not empty projectDetails.sortProjectTaskDeatils}">
                                 			<c:forEach var="taskDetails" items="${projectDetails.sortProjectTaskDeatils}" varStatus="taskIndex"> 
                                 			<tr class="taskDetailsSection">
                                              <td class="task_progress" width="">
                                              <span style="font-weight: bold;">[${taskDetails.taskId}]</span>
                                              <a href="#" id="${taskDetails.taskId}" class="trigger" 
                                                data-toggle="modal"  data-backdrop="static" data-target="#taskActivityDetailsModal" onclick="loadTaskActivityDetails(${taskDetails.taskId})"
                                                > ${taskDetails.name}</a> 
                                                 
                                                   <c:set var="taskHealth" value="info"/>
                                                 
                                                 <c:set var="progressTaskState" value="striped"/>
					                  				<c:if test="${taskDetails.status == 'INPROGRESS' }">
					                   					<c:set var="progressTaskState" value="striped active"/>
					                   					<c:if test="${taskDetails.taskRemainingTime < 0 }">
		                                                <c:set var="taskHealth" value="danger"/>
		                                                 </c:if>
					                  				</c:if>
					                  				<c:if test="${taskDetails.status == 'COMPLETED' }">
					                   					<c:set var="progressTaskState" value=""/>
					                   					 <c:set var="taskHealth" value="success"/>
					                  				</c:if>
			                  				
                                                 <div class="progress progress-small progress-${progressTaskState}" style="height: 5px">
                                               
                                        			<div title="${taskDetails.percentageOfTaskCompleted}% Complete" class="progress-bar progress-bar-${taskHealth}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: ${taskDetails.percentageOfTaskCompleted}%;"></div>
                                    				 <span style="font-size: 5px;float: left;">${taskDetails.percentageOfTaskCompleted}% Complete</span>
                                    			</div>
                                                </td>
                                               <td  width="10%"><span class="label label-${taskHealth}">${taskDetails.status}</span></td>
                                                <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
                                               <td width="1%">
                                                <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userGroupId == '1018'}">
                                               <a data-box="#confirmationbox" class="mb-control" onclick="deleteTask('',${taskDetails.taskId},${projectDetails.id});">
                                               <i class="fa fa-times fa-lg" style="color:red" aria-hidden="true"></i></a>
                                              </c:if>
                                              <div  class="hide taskSearchData">
                                              	<span>${userDetailsMap[taskDetails.assignee].firstName} ${userDetailsMap[taskDetails.assignee].lastName}</span>
                                             	<span>${taskDetails.name}</span>
                                             	<span>${taskDetails.status}</span>
                                             	<span>${taskDetails.duration}</span>
                                             	<span>${taskDetails.actualTimeTakenInHrs}</span>
                                              </div>
                                              
                                              </td>
                                              
                                               <td width="1%">
                                                <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userGroupId == '1018'}">
                                             
                                               <c:if test="${not taskDetails.extraTimeTask && taskDetails.status != 'INPROGRESS'}">
                                                <a data-toggle="modal" data-backdrop="static" data-target="#createtaskmodal" onclick="loadTaskDetails('${taskDetails.taskId}');"
                                                 href="#"><i class="fa fa-pencil fa-lg" style="" aria-hidden="true"></i></a>
                                               	</c:if>
                                               	</c:if>
                                               </td>
                                                <td width="1%">
                                                 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userGroupId == '1018'}">
                                                	<a href="#" style="color:blue" title="Recurring Task" data-toggle="modal" data-target="#recurringtaskmodal" onclick="recurringTaskModel('${taskDetails.taskId}','${projectDetails.id}')">
														<span class="fa fa-recycle fa-lg recurringSpinTask${taskDetails.taskId}"></span>
													</a>
													</c:if>
                                                </td>
                                            	</c:if>
                                            	<%@include file="response/taskActivityDetailsResponse.jsp"%>
                                            </tr>
                                            
                                 			</c:forEach>
                                 		</c:if>
                                       </tbody>
                                  </table>
                               <h5 class="bold">Time Tracking</h5>
                              <ul class="nav nav-pills nav-stacked labels-info ">
                                  <li>Estimated start time:<b> ${projectDetails.startTime}</b></li>
                                  <li>Estimated completion time:<b> ${projectDetails.completionTime}</b></li>
                                  <li>Project Duration : <b>${projectDetails.durationHrs} hours</b></li>
								         <li>Actual Duration : <b>${projectDetails.actualDurationInHrs} hours</b>
								        <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
									        <a data-toggle="modal"  data-backdrop="static" data-target="#taskAddExtraTimeModal" onclick="addTaskExtraTime(${projectDetails.id},'${projectDetails.durationHrs}', '${projectDetails.actualDurationInHrs}' );" class="btn btn-info btn-rounded" href="#" style="display: inline;padding: 0 0;margin-left: 26px;"> <span class="fa fa-plus"></span><span class="fa fa-clock-o"></span></a>
								        </c:if> 
								         
								         </li>
                                   
                                  <li>Unassinged Duration : <b>${projectDetails.unAssignedDuration} hours</b></li>
                              </ul>

                              <br>
                              <c:if test="${not empty projectDetails.filesNames || not empty projectDetails.completedFilesNames}">
                              <h5 class="bold">Project files</h5>
                              <ul class="list-unstyled p-files" id="upladedFilesList">
                                <c:if test="${not empty projectDetails.filesNames}">
                                 <c:forEach var="fileName" items="${projectDetails.filesNames}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/download/${projectDetails.id}/${fileName}?fileName=${fileName}"><i class="fa fa-file-text"></i>${fileName}</a>
                                 	 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
                                 	<a data-box="#confirmationbox" class="mb-control" style="margin-left:5px;" href="#" onclick="deleteFile('${fileName}');"><i class="fa fa-trash-o" style="color:red" aria-hidden="true"></i></a>
                                 	</c:if>
                                 	</li> 
                                 </c:forEach>
                                 </c:if>
                                  <c:if test="${not empty projectDetails.completedFilesNames}">
                                   <br>
                                  <h6 class="bold">Completed files</h6>
                                 <c:forEach var="fileName" items="${projectDetails.completedFilesNames}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/download/${projectDetails.id}-completed/${fileName}?fileName=${fileName}"><i class="fa fa-file-text"></i>${fileName}</a>
                                 	 <c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userRole == 'TEAMLEAD'}">
                                 	<a data-box="#confirmationbox" class="mb-control" style="margin-left:5px;" href="#" onclick="deleteFile('${fileName}');"><i class="fa fa-trash-o" style="color:red" aria-hidden="true"></i></a>
                                 	</c:if>
                                 	</li> 
                                 </c:forEach>
                                 </c:if>
                              </ul>
                              </c:if>
                              <br>
                               <c:if test="${not empty projectDetails.duplicateProjects}">
                               <h5 class="bold">Duplicate Projects</h5>
                               <ul class="list-unstyled p-files" id="duplicateProjects" style="height: 280px; overflow-y: scroll;">
                                 <c:forEach var="duplicateProject" items="${projectDetails.duplicateProjects}" varStatus="fileNameIndex"> 
                                 	<li><a href="${applicationHome}/project/${duplicateProject.id}">${duplicateProject.name}</a></li> 
                                 </c:forEach>
                              </ul>
                               </c:if>
							 <c:if test="${not empty projectDetails.tags}">
							 	 <br>
	                             <h5 class="bold">Project Tags</h5>
	                             <ul class="list-tags">
		                             <c:set var="tags" value="${fn:split(projectDetails.tags, ',')}" />
									 <c:forEach var="tag" items="${tags}"  varStatus="tagsIndex">
									 	<li><a href="#"><span class="fa fa-tag"></span> ${tag}</a></li>
									 </c:forEach>
	                        	</ul>
                        	</c:if>
                        	 
                        	<c:if test="${not empty projectDetails.watchers}">
                        	 	<br>
	                             <h5 class="bold">Watchers</h5>
	                             <ul class="list-tags">
		                             <c:set var="watchers" value="${fn:split(projectDetails.watchers, ',')}" />
									 <c:forEach var="watcher" items="${watchers}"  varStatus="watcherIndex">
									 	<li><a href="#"><span class="fa fa-tag"></span> ${watcher}</a></li>
									 </c:forEach>
	                        	</ul>
                        	</c:if>

                      </section>
                  </div>
              </div>
    
</div>               
<!-- END CONTENT FRAME -->     


  
<!-- task pop up window start-->
    <div id="task-pop-up">
      <h5><span id="popup-taskname"></span></h5>
      <p>
       <span id="popup-description"></span>
      </p>
      <ul class="nav nav-pills nav-stacked labels-info ">
	        <li>Estimated start time:<b><span id="popup-startTime"></span></b></li>
	        <li>Estimated completion time:<b><span id="popup-completionTime"></span></b></li>
	        <li>Duration : <b><span id="popup-duration"></span> Hours</b></li>
	        <li>Time Taken : <b><span id="popup-timeTaken"></span> Hours</b></li>
	        <li>Assignee : <b><span id="popup-assigneeName"></span></b></li>
	        <li>Collaborators : <b><span id="popup-HelperName"></span></b></li>
	        <li>Task Type : <b><span id="popup-projectTaskType"></span></b></li>
	        <li>Priority : <b><span id="popup-priority"></span></b></li>
	        <li>Recurring : <b><span id="popup-canRecure"></span></b></li>
	    </ul>
    </div>
<!-- task pop up window end -->

<!-- task create modal start -->
<div class="modal fade" id="createtaskmodal" tabindex="-1"
	role="dialog" aria-labelledby="createtaskmodal" aria-hidden="true">
	<form:form id="createTaskFormId" action="${applicationHome}/createTask" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Create a Task</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/createTaskModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick="doAjaxCreateTaskForm();" id="createOrUpdateTaskId"
						class="btn btn-primary">
						<span id="userButton">Save</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<div class="modal fade" id="taskActivityDetailsModal" tabindex="-1"
	role="dialog" aria-labelledby="taskActivityDetailsModal" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Task Activity Details</h4>
				</div>
					<%@include file="fagments/taskActivityDetailsModal.jspf"%>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button id="taskReassign" type="button"
						class="btn btn-primary hide">
						<span  onclick="taskAddExtraTime()">Add extra time</span>
					</button>
				</div>
			</div>
		</div>
</div>          

<div class="modal fade" id="taskAddExtraTimeModal" tabindex="-1"
	role="dialog" aria-labelledby="taskAddExtraTimeModal" aria-hidden="true">
	<form:form id="createExtraTaskFormId" action="${applicationHome}/createExtraTimeTask" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Extra Project Time</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/taskExtraTimeModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick="doAjaxCreateExtraTaskForm();" id="createOrUpdateExtraTaskId"
						class="btn btn-primary">
						<span id="userExtraTaskButton">Add extra time</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>   

<!-- recurring project create modal start -->
<div class="modal fade" id="recurringtaskmodal" tabindex="-1"
	role="dialog" aria-labelledby="recurringtaskmodal" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Recurring Task Settings</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/recurringTaskModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="recurringTaskActionButton" onclick=""
						class="btn btn-primary">
						<span id="RTCreatOrUpdate">Create</span>
					</button>
				</div>
			</div>
		</div>
</div>
<!-- recurring project create modal end -->
                      
</c:if>                        
<c:forEach var="taskProjectMap" items="${taskProjectMappings}"> 
<input type="hidden" data-tskprj="${taskProjectMap.key}" value="${taskProjectMap.value}"/>
</c:forEach>

 <%@include file="includes/footer.jsp" %>  
  <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min.js?v=${fsVersionNumber}"></script> 
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js?v=${fsVersionNumber}"></script>
	<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
	<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/gentleSelect/jquery-gentleSelect-min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/cron/jquery-cron.js?v=${fsVersionNumber}"></script>
       <script>

       <c:if test="${!sameDayOnlyTaskEnabled}">
      	 	var startProjectTime = '${projectDetails.startTime}';
       		var completionProjectTime = '${projectDetails.completionTime}';
       		var sameDayOnlyTaskEnabled = false;
       </c:if>
       
       <c:if test="${sameDayOnlyTaskEnabled}">
 	 	var startProjectTime = new Date();
  		var completionProjectTime = new Date();
  		var sameDayOnlyTaskEnabled = true;
  		</c:if>
       
       var jvalidate = $("#createTaskFormId").validate({
    		 ignore: ".ignorevalidation",
    	   rules: {                                            
    		name: {
    	            required: true,
    	    },
    	    startTime: {
    	    	 required: true
    	    }
    	    <c:if test="${taskPrjectCategoryEnabled}">
    	    , 
    	    projectCategory: {
    	    	 required: true
    	    }
    	    </c:if>
    	  }
    	});
       
     $(function() {
    	   var moveLeft = 0;
    	   var moveDown = 0;

    	   $('a.trigger').hover(function(e) {
    		   
    		 var taskId = this.id;
   		    $("#popup-startTime").html($("#"+taskId+"startTime").val());
   		 	$("#popup-completionTime").html($("#"+taskId+"completionTime").val());
   		 	$("#popup-duration").html($("#"+taskId+"duration").val());
   		    $("#popup-timeTaken").html($("#"+taskId+"timeTaken").val());
   		 	$("#popup-taskname").html($("#"+taskId+"name").val());
   		 	$("#popup-description").html($("#"+taskId+"description").val());
   			$("#popup-assigneeName").html($("#"+taskId+"assigneeName").val());
   			$("#popup-HelperName").html($("#"+taskId+"helperNames").val());
   			$("#popup-projectTaskType").html($("#"+taskId+"projectTaskType").val());
   			$("#popup-priority").html($("#"+taskId+"priority").val());
   			
   			var canRecureValue = $("#"+taskId+"canRecure").val();
   			if (canRecureValue == 'true'){
   				canRecureValue = 'Yes';
   			} else {
   				canRecureValue = 'No';
   			}
   			$("#popup-canRecure").html(canRecureValue);
   			
    	     $('div#task-pop-up').show()
    	     .css('top', e.pageY + moveDown)
    	      .css('left', e.pageX + moveLeft)
    	      .appendTo('body');
    	   }, function() {
    	     $('div#task-pop-up').hide();
    	   });
    	   
    	   $('a#trigger').mousemove(function(e) {
    		    $("div#task-pop-up").css('top', e.pageY + moveDown).css('left', e.pageX + moveLeft);
    		  });
    	 }); 
   	
var clearTaskDetails = function(){
	//getAssignJsonData();
    $("#taskId").val("");
    var taskDate = new Date();
	var projectEndDate = new Date('${projectDetails.completionTime}');
    console.log(" date " + projectEndDate);
	if (taskDate < projectEndDate || sameDayOnlyTaskEnabled){
		$("#estStartTime").val(getTodayDate(new Date()) + " 08:00");
	} else {
		$("#estStartTime").val(getTodayDate(new Date("${projectDetails.completionTime}")) + " 08:00");
	}
	$("#estCompleteTime").html("${projectDetails.completionTime}");
	$("#unassignedDuration").html(${projectDetails.unAssignedDuration});
	$("#projectDuration").html(${projectDetails.durationHrs});
 	$("#taskDuration").html(${projectDetails.unAssignedDuration});
	$("#taskName").val("${projectDetails.category}");
	if($("#taskcategory").length >= 0) {
		$("#taskcategory").val("${projectDetails.category}");
		$('#taskcategory').selectpicker('refresh');
	}
	if($("#taskitemcategory").length >= 0) {
		$("#taskitemcategory").prop("selectedIndex", 0);;
		$('#taskitemcategory').selectpicker('refresh');
		$("#taskProjectCategory").val("");
	}
	$("#description").val("");
	$("#projectTaskType").prop("selectedIndex", 0);
	$("#taskClient").prop("selectedIndex", 0);
	$("#taskClient").selectpicker('refresh');
	
	$("#priority").prop("selectedIndex", 0);
	$("select.canRecure").prop("selectedIndex", 0);
	
	$("#createOrUpdateTaskId span").html("Save");
    $('#createTaskFormId').prop("action", "${applicationHome}/createTask");
    //createTaskDurationList(${projectDetails.unAssignedDuration});
    //$("#duration").prop('selectedIndex', ${projectDetails.unAssignedDuration});
    $("#duration").val('1');
    $("select.assigneeSelectName").val("");
	$("select.assigneeSelectName").selectpicker('refresh');
    
	
	
    $("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), ${projectDetails.unAssignedDuration}));
    $("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));
    $("#assignTableId").hide(1000);
	$("#toggleAssignTask").html("Assign task");
	resetAssignTable();
	$('input:radio[name=assigneeId]').each(function () { $(this).prop('checked', false); });
}    

$("#taskcategory").on("change",function(){
	if($("#taskcategory").prop("selectedIndex") > 0) {
		$("#taskName").val($(this).val());
	}
});

$("#taskitemcategory").on("change",function(){
	if($("#taskitemcategory").prop("selectedIndex") > 0) {
		var taskItemCatValue = $(this).val();
		var taskProjectCategoryInput = $('input[data-tskprj="'+taskItemCatValue+'"]');
		if (taskProjectCategoryInput == undefined || taskProjectCategoryInput.legnth == 0 ) {
			$("#taskProjectCategory").val("");
		} else {
			$("#taskProjectCategory").val(taskProjectCategoryInput.val());
		}
	} else {
		$("#taskProjectCategory").val("");
	}
});

var createTaskDurationList = function(duration){
	 $("#duration").html("");
	for (var index = 0; index <= duration; index++) {
	     $("#duration").append('<option value="'+index+'">'+index+'</option>');
	 }
}

var jsonAssignData = {};

var getAssignJsonData = function(){
	doAjaxRequest("GET", "${applicationHome}/userTaskActivityJson", {},  function(jsonData) {
		jsonAssignData = jsonData;
	   }, function(e) {
	   });
}

//getAssignJsonData();

var loadTaskDetails = function(taskId){
	//getAssignJsonData();
	$("#taskId").val(taskId);
    $("#estStartTime").val($("#"+taskId+"startTime").val());
 	$("#unassignedDuration").html(${projectDetails.unAssignedDuration});
 	$("#taskName").val($("#"+taskId+"name").val());
 	$("#taskClient").val($("#"+taskId+"taskClient").val());
 	$('#taskClient').selectpicker('refresh');
 	var projectTaskType = $("#"+taskId+"projectTaskType").val();
 	$("#projectTaskType").val("PRODUCTIVE");
 	famstacklog("projectTaskType" + projectTaskType);
 	if (projectTaskType == 'REVIEW') {
 		$("#projectTaskType").val("REVIEW");
 	} else if (projectTaskType == 'ITERATION') {
 		$("#projectTaskType").val("ITERATION");
 	}
 	$('#projectTaskType').selectpicker('refresh');
 	$("#projectDuration").html(${projectDetails.durationHrs});
 	$("#taskDuration").html($("#"+taskId+"duration").val());
 	$("#taskName").val($("#"+taskId+"name").val());
 	$("#description").val($("#"+taskId+"description").val());
 	$("#priority").val($("#"+taskId+"priority").val());
 	$('#priority').selectpicker('refresh');
 	
 	if($("#taskitemcategory").length >= 0) {
		$("#taskitemcategory").val($("#"+taskId+"taskitemcategory").val());
		$('#taskitemcategory').selectpicker('refresh');
		$("#taskProjectCategory").val($("#"+taskId+"taskProjectCategory").val());
	}
 	
 	$("select.canRecure").val($("#"+taskId+"canRecure").val());
	$("select.canRecure").selectpicker('refresh');
	
	$("#createOrUpdateTaskId span").html("Update");
    $('#createTaskFormId').prop("action", "${applicationHome}/updateTask");
    //createTaskDurationList(${projectDetails.unAssignedDuration}+parseInt($("#"+taskId+"duration").val()));
   // $("#duration").prop('selectedIndex', parseInt($("#"+taskId+"duration").val()));
   $("#duration").val($("#"+taskId+"duration").val());
    //$("#duration").selectpicker('refresh');
    $("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), parseInt($("#"+taskId+"duration").val())));
    $("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));
	resetAssignTable();
	fillTableFromJson();
	$('input:radio[name=assigneeId]').each(function () { $(this).prop('checked', false); });
	var assigneeId = $("#"+taskId+"assignee").val();
	
	$("#employeeListForTaskTable_filter input").val($("#"+taskId+"assigneeName").val());
	$("#employeeListForTaskTable_filter input").keyup();
	var helpers = $("#"+taskId+"helper").val();
	if (assigneeId != "" && assigneeId != 0) {
		 $("#"+assigneeId+"-select").click();
		 $("#assignTableId").show(500);
		 $("#toggleAssignTask").html("Assign task later");
		 if (helpers != "") {
			 var helperIds = helpers.replace("[","").replace("]","").split(",");
			 for (var index = 0; index<helperIds.length; index++) {
				 $("#"+helperIds[index].trim()+"-helper").click();
			 }
		 }
	} else {
		$("#assignTableId").hide(1000);
		$("#toggleAssignTask").html("Assign task");
	}

 	
}  
       
function addComment() {
   var dataString = {"projectId": $('#hdn_project_id').val() , "projectComments": $('#comment_textarea').val() };
   
   doAjaxRequest("POST", "${applicationHome}/addComment", dataString,  function(data) {
	   var responseJsonObj = JSON.parse(data)
       if (responseJsonObj.status){
           window.location.reload(true);
       }
   }, function(e) {
   });
}

$(document).ready(function() {
    
    $('.editableFieldText').editable('saveProjectDetails', {
        indicator : 'Saving...',
        tooltip   : 'Click to edit...',
        cancel	  : '<i class="fa fa-times" aria-hidden="true"></i>',
        submit	  : '<i class="fa fa-check" aria-hidden="true"></i>',
        height 	  : '25px',
        width     : '100%'
    });
    $('.editableFieldTextArea').editable('saveProjectDetails', {
        type      : 'textarea',
        cancel	  : '<i class="fa fa-times fa-2x" aria-hidden="true"></i>',
        submit	  : '<i class="fa fa-check fa-2x" aria-hidden="true"></i>',
        tooltip   : 'Click to edit...',
        rows	  : 5,
        cols	  : 130
    });
    
	Dropzone.autoDiscover = false;
  		$("#my-dropzone").dropzone({
  			url : "${applicationHome}/uploadfile/${projectDetails.id}",
  			addRemoveLinks : false,
  			acceptedFiles: ".jpeg,.jpg,.png,.gif,.doc,.docx,.xls,.xlsx,.pdf",
  			maxFilesize: 10, 
  			success : function(file, response) {
  		       if (response.status){
  				var imgName = response;
  				file.previewElement.classList.add("dz-success");
  				var fileIcon = "fa-file-text";
  				if (file.type == "text/xml") {
  					fileIcon = "fa-file-excel-o";
  				}
  				
  				$("#upladedFilesList").append('<li><a href="${applicationHome}/download/${projectDetails.id}/'+file.name+'?fileName='+file.name+'"><i class="fa '+fileIcon+'"></i>'+file.name+'</a></li>');
  		       } else {
  		    	 file.previewElement.classList.add("dz-error");
  		       }
  		      },
  			error : function(file, response) {
  				file.previewElement.classList.add("dz-error");
  			}
  		});
  		
  		
  		$('#employeeListForTaskTable').on( 'draw.dt', function () {
  			resetAssignTable();
  			fillTableFromJson();
  		} );
});

var deleteFile = function(fileName){
	$(".msgConfirmText").html("Delete file");
	$(".msgConfirmText1").html(fileName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteFile('"+fileName+"')");
}

var deleteTask = function(taskName, taskId, projectId){
	$(".msgConfirmText").html("Delete task");
	$(".msgConfirmText1").html(taskName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteTask("+taskId+","+projectId+")");
}

var doAjaxDeleteTask = function(taskId, projectId){
	var dataString = {"taskId" : taskId, "projectId":projectId};
	var url = '${applicationHome}/deletetask';
	 doAjaxRequest("POST", url, dataString,  function(data) {
		   var responseJsonObj = JSON.parse(data)
	       if (responseJsonObj.status){
	           window.location.reload(true);
	       }
	   }, function(e) {
	   });
}


var doAjaxDeleteFile = function(fileName){
	var dataString = {"fileName" : fileName};
	var url = '${applicationHome}/deletefile/${projectDetails.id}';
	 doAjaxRequest("POST", url, dataString,  function(data) {
		   var responseJsonObj = JSON.parse(data)
	       if (responseJsonObj.status){
	           window.location.reload(true);
	       }
	   }, function(e) {
	   });
}

var toggleAssignTask = function(){
	if ($("#assignTableId").is(':hidden')) {
		$("#assignTableId").show(1000);
		$("#toggleAssignTask").html("Assign task later");
		resetAssignTable();
		fillTableFromJson();
		$(".assigneeSelectId").hide();
	} else {
		$("#assignTableId").hide(1000);
		$("#toggleAssignTask").html("Assign task");
		resetAssignTable();
		$('input:radio[name=assigneeId]').each(function () { $(this).prop('checked', false); });
		$(".assigneeSelectId").show();
	}
	
	$("select.assigneeSelectName").val("");
	$("select.assigneeSelectName").selectpicker('refresh');
}

var jvalidate = $("#createExtraTaskFormId").validate({
	 ignore: ".ignorevalidation",
 rules: {                                            
	 assignee: {
	       required: true,
	  	},
	  	startTime: {
		   	 required: true
		}
  	}
});


function doAjaxCreateExtraTaskForm() {
	$('#createExtraTaskFormId').submit();
}

$('#createExtraTaskFormId').ajaxForm(function(response) {
	var responseJson = JSON.parse(response);
	if (responseJson.status) {
		window.location.reload(true);
	}
});

function doAjaxCreateTaskForm() {
	$('#createTaskFormId').submit();
}

$('#createTaskFormId').ajaxForm(function(response) {
	var responseJson = JSON.parse(response);
	if (responseJson.status) {
		window.location.reload(true);
	}
});


$('input:radio[name=assigneeId]').on("click", function(){
	resetAssignTable();
	fillTableFromJson();
	fillAssignTabledBasedOnDate(this.id);
	$("select.assigneeSelectName").val($(this).val());
	$("select.assigneeSelectName").selectpicker('refresh');
	
});

$('input:checkbox[name=helper]').on("click", function(){
		fillHelperTabledBasedOnDate(this.id);
});


var fillHelperTabledBasedOnDate =function(id){
	var startTaskTime = $("#estStartTime").val();
	var startTimeHour = new Date(startTaskTime).getHours();
	var duration = $("#duration").val();
	var userId = id.split("-")[0];
	if ($("#"+userId+"-helper").prop("checked") == true) {
		markTableFields(userId, startTimeHour, duration, "lightgray", true, false);
	} else {
		markTableFields(userId, startTimeHour, duration, "lightgray", true, true);
	}
}

var fillAssignTabledBasedOnDate =function(id){
	
	var startTaskTime = $("#estStartTime").val();
	var startTimeHour = new Date(startTaskTime).getHours();
	var duration = $("#duration").val();
	var userId = id.split("-")[0];
	$("#"+userId+"-helper").prop("disabled", true);
	markTableFields(userId, startTimeHour, duration, "yellow", false, false);
}

var markTableFields = function(userId, startTimeHour, duration, color, helper, reset){
	
	for (var index = 0; index < duration; index++) {
		famstacklog("duration :" + duration);
		if(startTimeHour ==  breakTime){
			//startTimeHour++;
		}
		var getCell = $("#"+userId+"-"+startTimeHour);
		$("#estStartTime").css("border", "1px solid #D5D5D5");
		if (reset){
			if($(getCell).attr("isassigned") == "true"){
				return;
			}
			var cellBackGroundColor =$(getCell).attr("cellcolor");
			$(getCell).css("background-color", cellBackGroundColor);
			$(getCell).attr("cellmarked",false);
			$(getCell).attr("modified",false);
			decreaseTotalHours(userId);
		} else {
			var cellBackGroundColor = $(getCell).css("background-color");
			if($(getCell).attr("isassigned") == "true"){
				if (helper){
					$("#"+userId+"-helper").prop("checked", false);
					return;
					//error
					
				} else {
					$('input:radio[name=assigneeId]').each(function () { $(this).prop('checked', false); });
					$("#estStartTime").css("border", "1px solid red");
					return;
					//error
				}
			}
			increaseTotalHours(userId);
			$(getCell).attr("cellcolor", cellBackGroundColor);
			$(getCell).css("background-color", color);
			$(getCell).attr("cellmarked",true);
			$(getCell).attr("modified",true);
		}
		
		if (!helper){
			cellSelectCount++;
		} else {
			$(getCell).attr("celleditable", false);
		}
		
		startTimeHour++;
	}
}

var dateDisplayLogic = function( currentDateTime ){
	
	var startDate = new Date(startProjectTime);
	var startHours = startDate.getHours();
	var dd = startDate.getDate();
	var mm = startDate.getMonth(); //January is 0!
	var yyyy = startDate.getFullYear();
	startHours+=":00";
	if (currentDateTime && currentDateTime.getDate() == dd && currentDateTime.getMonth() == mm && currentDateTime.getFullYear() == yyyy){
		this.setOptions({
			minTime:startHours
		});
	}else
		this.setOptions({
			minTime:'8:00'
		});
};

$.datetimepicker.setLocale('en');
$('.dateTimePicker').datetimepicker({onGenerate:function( ct ){
	/* $(this).find('.xdsoft_date.xdsoft_weekend')
	.addClass('xdsoft_disabled'); */
	},
	minDate:startProjectTime, // yesterday is minimum date
	maxDate:completionProjectTime,
	allowTimes:['08:00','09:00','10:00','11:00','12:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00'],
	onChangeDateTime:dateDisplayLogic,
	onShow:dateDisplayLogic
});
	
$('.dateTimePickerNew').datetimepicker({
	allowTimes:['08:00','09:00','10:00','11:00','12:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00']
});

$("#estStartTime").on("change",function(){
	
	var startProjectDate = new Date(startProjectTime);
	resetAssignTable();
	fillTableFromJson();
	
	var startTaskTime = $("#estStartTime").val();
	var startTaskDate = new Date(startTaskTime);
	if (sameDayOnlyTaskEnabled && isNotSameDate(startTaskDate, new Date())) {
		$("#estStartTime").val(getTodayDate(new Date()) + " 08:00");		
	}
	
	if(startTaskDate < startProjectDate && !sameDayOnlyTaskEnabled) {
		//$("#estStartTime").css("border", "1px solid red");
		$("#estStartTime").val(getTodayDate(new Date()) + " 08:00");
		return;
	} else {
		$("#estStartTime").css("border", "1px solid #D5D5D5");
	}
	$("#estCompleteTime").html(getEstimatedCompletionTime( $("#estStartTime").val(), $("#duration").val()));
	$("#taskDuration").html($("#duration").val());
	
	if (!$("#assignTableId").is(':hidden') && $('input:radio[name=assigneeId]:checked').length > 0) {
		var id = $('input:radio[name=assigneeId]:checked').attr('id');
		fillAssignTabledBasedOnDate(id);
	}
	
	$("#currentAssignmentDate").html("Date : " + getTodayDate(new Date($("#estStartTime").val())));
});

function isNotSameDate(date1, date2){
	if (date1.getDate() != date2.getDate() || date1.getMonth() != date2.getMonth() ||  date1.getFullYear() != date2.getFullYear()) {
		return true;
	}
	return false;
}

$("#duration").on("change",function(){
	resetAssignTable();
	fillTableFromJson();
	$("#estCompleteTime").html(getEstimatedCompletionTime($("#estStartTime").val(), $("#duration").val()));
	$("#taskDuration").html($("#duration").val());
	if (!$("#assignTableId").is(':hidden') && $('input:radio[name=assigneeId]:checked').length > 0) {
		var id = $('input:radio[name=assigneeId]:checked').attr('id');
		fillAssignTabledBasedOnDate(id);
	}
});

var getEstimatedCompletionTime = function(startTime, duration){
	var estimatedCompletionTime = new Date(startTime); 
	
	estimatedCompletionTime.addHours(duration);
	
	var completionTimeString = getTodayDate(estimatedCompletionTime);
	var completionHour = estimatedCompletionTime.getHours();
	var startTimeHours =  new Date(startTime).getHours();
	if(completionHour > 13 && startTimeHours < 13){
		estimatedCompletionTime.addHours(1);
	}
	completionTimeString +=(" " +estimatedCompletionTime.getHours()+":00");
	return completionTimeString;
}

$(document).ready(function() {
    $('#employeeListForTaskTable').DataTable({ 
    	responsive: true,
        "lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
        "ordering": false,
    
    });
    
    $("#employeeListForTaskTable_filter").append('<span style="float:left;font-weight: bold;margin-top: 7px;"><a hre="#"><i class="fa fa-angle1-double-left fa-x" aria-hidden="true"></i></a> <span style="margin-left: 10px;margin-right: 10px;" id="currentAssignmentDate"></span> <a hre="#"><i class="fa fa-angle1-double-right fa-x" aria-hidden="true"></i></a></span>');
} );

var cellSelectCount = 0;
var breakTime = 13;
var checkNextAndPreviousMarked = function(thisVarId, checkOrUnchek){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	var nextMarked = false;
	var preMarked = false;
	var sameMarked = false;
	var celleditable = $("#"+userId+"-"+time).attr("celleditable");
	var cellmarked	= $("#"+userId+"-"+time).attr("cellmarked");
	if (cellmarked == 'true' && celleditable == 'true') {
		sameMarked = true;
	}
	preMarked = isPreMarked(thisVarId);
	nextMarked = isNextMarked(thisVarId);
	
	if (checkOrUnchek) {
		return (cellSelectCount == 0 || preMarked || nextMarked) && !sameMarked;
	}
	return (!preMarked || !nextMarked) && sameMarked;
}

var isPreMarked = function(thisVarId){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	var preMarked = false;
	
	if (time <= 21 && 8 < time) {
		var tmpTime = time - 1;
		if (time == breakTime+1) {
			//tmpTime--;
		}
		var celleditable = $("#"+userId+"-"+tmpTime).attr("celleditable");
		var cellmarked	= $("#"+userId+"-"+tmpTime).attr("cellmarked");
		if (cellmarked == 'true' && celleditable == 'true') {
			preMarked = true;
		}
	}
	return preMarked;
}


var isNextMarked = function(thisVarId){
	var cellIds  = thisVarId.split("-");
	var userId = cellIds[0];
	var time = parseInt(cellIds[1]);
	var nextMarked = false;
	
	if (time >= 8 && 21 > time) {
		var tmpTime = time + 1;
		if (time == breakTime-1) {
			//tmpTime++;
		}
		var celleditable = $("#"+userId+"-"+tmpTime).attr("celleditable");
		var cellmarked	= $("#"+userId+"-"+tmpTime).attr("cellmarked");
		if (cellmarked == 'true' && celleditable == 'true') {
			nextMarked = true;
		}
	}
	return nextMarked;
}


$("table#employeeListForTaskTable").on("click", "tr.editable td.markable", function(){
	
	var maxDuration = $('#duration > option').length;
	
	var cellId = this.id;
	var userId  = cellId.split("-")[0];
	var hourId = cellId.split("-")[1];
	
	var cellBackGroundColor = $(this).css("background-color");
	var celleditable = $("#"+userId+"-"+hourId).attr("celleditable");
	if (cellBackGroundColor == "rgb(0, 0, 255)" || celleditable == "false") {
		return;
	}
	if(cellSelectCount == 0) {
		$("#"+userId+"-select").prop('checked', true);
		$("#"+userId+"-helper").prop('disabled', true);
	}
	//$('input:radio[name=assigneeId]').each(function () { $(this).prop('disabled', true); });
	//$("#"+userId+"-select").prop('disabled', false);
	
	if (cellBackGroundColor == "rgb(255, 255, 0)" && checkNextAndPreviousMarked(this.id, false)) {
		var cellColor = $(this).attr("cellcolor");
		$(this).css("background-color", cellColor);
		decreaseTotalHours(userId);
		cellSelectCount--;
		$(this).attr("cellmarked",false);
		$(this).attr("modified",false);
		$('input:checkbox[name=helper]:checked').each(function () { $(this).click();});
		if (isPreMarked(this.id) && cellSelectCount == 1) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(hourId-1);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		}
		
		if (isNextMarked(this.id)) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(parseInt(hourId)+1);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		}
		
	} else if (checkNextAndPreviousMarked(this.id, true)){
		var startProjectDate = new Date(startProjectTime);
		if(hourId < startProjectDate.getHours()) {
			return;
		} 
		
		if (maxDuration - 1 == cellSelectCount) {
			return;
		}
		$(this).attr("cellcolor", cellBackGroundColor);
		$(this).css("background-color", "yellow");
		increaseTotalHours();
		cellSelectCount++;
		increaseTotalHours(userId);
		$(this).attr("cellmarked",true);
		$(this).attr("modified",true);
		$('input:checkbox[name=helper]:checked').each(function () { $(this).click();});
		if (cellSelectCount == 1) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(hourId);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		} else if (!isPreMarked(this.id)) {
			var estimatedStartDate = new Date($("#estStartTime").val());
			estimatedStartDate.setHours(hourId);
			$("#estStartTime").val(getTodayDate(estimatedStartDate)+" "+estimatedStartDate.getHours()+":00");
		}
	}
	
	if (cellSelectCount == 0) {
		$("#employeeListForTaskTable tr").addClass("editable");
		$('input:radio[name=assigneeId]').each(function () { $(this).prop('disabled', false); });
		$('input:radio[name=assigneeId]').each(function () { $(this).prop('checked', false); });
		
		$('input:checkbox[name=helper]').each(function () { $(this).prop('disabled', false); });
		$('input:checkbox[name=helper]').each(function () { $(this).prop('checked', false); });
	}
	
	$("#estCompleteTime").html(getEstimatedCompletionTime( $("#estStartTime").val(), cellSelectCount));
	$("#duration").val(cellSelectCount);
	$("#duration").selectpicker("refresh");
	
});

var resetAssignTable = function(){
	
	$('table#employeeListForTaskTable td[cellmarked="true"]').each(function () {
		
		$("#employeeListForTaskTable tr").addClass("editable");
		$('input:checkbox[name=helper]').each(function () { $(this).prop('disabled', false); });
		$('input:checkbox[name=helper]').each(function () { $(this).prop('checked', false); });
		var cellColor = $(this).attr("cellcolor");
		$(this).css("background-color", cellColor);
		$(this).html("");
		cellSelectCount=0;
		$(this).attr("cellmarked",false);
		$(this).attr("modified",false);
		$(this).attr("celleditable", true);
		$(this).attr("isassigned",false);
		$(this).removeAttr("data-taskId");
		
	});
	
	$('table#employeeListForTaskTable td[dynamicvalue="0"]').each(function () {
		$(this).html("0");
	});
	
	$('table#employeeListForTaskTable td[dynamicvalue="8"]').each(function () {
		$(this).html("8");
	});
}


var increaseTotalHours = function(userId){
	var totalHours = parseInt($("#"+userId+"-totalHours").html()) + 1;
	var availableHours = parseInt($("#"+userId+"-availabeHours").html()) - 1;
	$("#"+userId+"-totalHours").html(""+totalHours);
	$("#"+userId+"-availabeHours").html(""+availableHours);
	
	if (availableHours < 0){
		$("#"+userId+"-availabeHours").css("color", "red");
		//$('input:radio[name=assigneeId]').each(function () { $(this).prop('checked', false); });
	} else {
		$("#"+userId+"-availabeHours").css("color", "green");
	}
}

var decreaseTotalHours = function(userId){
	var totalHours = parseInt($("#"+userId+"-totalHours").html()) - 1;
	var availableHours = parseInt($("#"+userId+"-availabeHours").html()) + 1;
	$("#"+userId+"-totalHours").html(""+totalHours);
	$("#"+userId+"-availabeHours").html(""+availableHours);
	
	if (availableHours <= 0){
		$("#"+userId+"-availabeHours").css("color", "red");
	} else {
		$("#"+userId+"-availabeHours").css("color", "green");
	}
}

var fillTableFromJson = function(){
	famstacklog("jsonAssignData :" + jsonAssignData);
	try{	
	$.each(JSON.parse(jsonAssignData), function(idx, elem){
		if (getTodayDate(new Date($("#estStartTime").val())) == elem.dateId) {
			
			var isCompleted = elem.actualEndTime == null ?false:true;
			var isInprogress = elem.actualStartTime == null?false:true;

			var starthour = parseInt(elem.startHour);
			var durationmins = parseInt(elem.durationInMinutes);
			var startmins = parseInt(elem.startMins);
	
			var iteration = 0;
			var duration = (durationmins * 100) /60;
			
			famstacklog("duration :" + duration);
			famstacklog("starthour :" + starthour);
			famstacklog("startmins :" + startmins);
			while (duration > 0 ) {
				var marginLeft = 0;
				var style = "float:left;";
				if (iteration == 0){
					marginLeft = (startmins*100)/60;
					if ((duration + marginLeft) >= 100) {
						firstWidth = 100 - marginLeft;
						duration-=firstWidth;
					} else {
						firstWidth = duration;
						duration =0;
					}
					
					style +="width:"+firstWidth+"%;margin-left:"+marginLeft+"%;";
				} else {
					
					if (duration >= 100) {
						style +="width:100%;margin-left:0px;";
						duration-=100;
					} else {
						style +="width:"+duration+"%;";
						duration =0;
					}
					
				}
				
				famstacklog("style :" + style);
				
				if (starthour == breakTime) {
					//starthour++;
				}
				var cellId = $("#"+elem.userId+"-"+starthour);
				famstacklog(cellId);
				
				if($("#taskId").val() != "" && elem.taskId == $("#taskId").val() &&  elem.taskId != 0) {
					famstacklog(elem.taskId);
					famstacklog($("#taskId").val());
				} else {
					var isOverLapping = false;
					if ($(cellId).attr("cellmarked") == 'true') {
						isOverLapping = true;
					}
					
					$(cellId).attr("celleditable", false);
					$(cellId).attr("isassigned",true);
					$(cellId).attr("cellmarked",true);
					$(cellId).attr("modified",false);
					$(cellId).css("text-align", "center");
					

					var cellStausColor = isInprogress ? "rgb(250, 128, 114)" :  "rgb(0, 0, 255)"; 
					cellStausColor = isCompleted ? "rgb(0, 128, 0)" :cellStausColor;
					
					var cellTitleTaskName = "Task Id :" + elem.taskId + ", Activity Id:" + elem.taskActivityId +", Task Name :" +  elem.taskName;
					var cellTaskType = "";
					if (elem.userTaskType == "PROJECT") {
						cellTaskType = "P";
					} else if (elem.userTaskType == "PROJECT_HELPER") {
						cellTaskType = "PH";
					} else if (elem.userTaskType == "LEAVE") {
						cellStausColor = "rgb(128, 128, 128)";
						cellTaskType = "L";
					} else if (elem.userTaskType == "MEETING") {
						cellStausColor = "rgb(0, 139, 139)";
						cellTaskType = "M";
					}else if (elem.userTaskType == "PROJECT_HELPER_REVIEW") {
						cellTaskType = "PRH";
					}else if (elem.userTaskType == "PROJECT_REVIEW") {
						cellTaskType = "PR";
					}else if (elem.userTaskType == "PROJECT_PARTIAL") {
						cellTaskType = "PRA";
					}else if (elem.userTaskType == "EXTRATIME") {
						cellTaskType = "PET";
					} else if (elem.userTaskType == "OTHER") {
						cellStausColor = "rgb(0, 139, 139)";
						cellTaskType = "OTH";
					}
					if ($(cellId).attr("data-taskId") == elem.taskId) {
						$(cellId).find("span").css("margin-left","");
						$(cellId).append('<span title="'+cellTitleTaskName+'" style="'+style+'height:41px;padding-top:10px;background-color:'+cellStausColor+'">'+cellTaskType+'</span>');
					} else {
						if (isOverLapping) {
							cellStausColor = "rgb(235, 0, 0)";
							$(cellId).css("background-color","red");
						}
						$(cellId).html('<span title="'+cellTitleTaskName+'" style="'+style+'height:41px;padding-top:10px;background-color:'+cellStausColor+'">'+cellTaskType+'</span>');
					}
					$(cellId).attr("data-taskId",elem.taskId);
					increaseTotalHours(elem.userId);
				}
				
				iteration++;
				starthour++;
			}
			
		}
	});
	}catch(err){
		
	}
}

var addTaskExtraTime = function(projectId, estDuration, actualDuration){
	$(".taskDetailsDuration").html(estDuration);
	$(".taskDetailsActualDuration").html(actualDuration);
}

var loadTaskActivityDetails = function(taskId) {
	$("#projectActivityModalContent").html($("#taskDetailsContent"+taskId).html());
}

function deleteTaskActivity(activityId, activityName){
	$(".msgConfirmText").html("Delete task activity");
	$(".msgConfirmText1").html(activityName);
	$("#confirmYesId").prop("href","javascript:deleteTaskActivityAjax('"+activityId+"')");
}


var deleteTaskActivityAjax = function(activityId) {
	doAjaxRequest("POST", "${applicationHome}/deleteTaskActivity", {activityId:activityId},  function() {
		$(".mb-control-close").click();
		$(".taskActivityTime" + activityId).remove();
	}, function(e) {
	});
}

$(document).ready(function(){
	/* MESSAGE BOX */
	$(document).on("click",".deleteTaskActivity",function(){
	    var box = $($(this).data("box"));
	    if(box.length > 0){
	        box.toggleClass("open");
	        
	        var sound = box.data("sound");
	        
	        if(sound === 'alert')
	            playAudio('alert');
	        
	        if(sound === 'fail')
	            playAudio('fail');
	        
	    }        
	    return false;
	});
	$(document).on("click",".mb-control-close",function(){
	   $(this).parents(".message-box").removeClass("open");
	   return false;
	});    
	/* END MESSAGE BOX */
});


var showTaskActualTimeEdit = function(taskId){
	$("."+taskId+"taskTimeEdit").show();
	$("."+taskId+"taskTimeEditLink").hide();
}

var hideTaskActualTimeEdit = function(taskId){
	$("."+taskId+"taskTimeEdit").hide();
	$("."+taskId+"taskTimeEditLink").show();
}

var showTaskActualTimeEditDate = function(taskActId) {
	$("."+taskActId+"taskTimeEditDateLink").hide();
	$("."+taskActId+"taskTimeEdit").hide();
	$("."+taskActId+"taskTimeEditLink").show();
	$("."+taskActId+"taskTimeEditDate").show();
	$("."+taskActId+"taskActTimeEdit").show();	
	
	$("."+taskActId+"taskTimeEditDate").datetimepicker({onGenerate:function( ct ){
		/* $(this).find('.xdsoft_date.xdsoft_weekend')
		.addClass('xdsoft_disabled'); */
		},
		minDate:startProjectTime, // yesterday is minimum date
		maxDate:completionProjectTime,
		allowTimes:['08:00','09:00','10:00','11:00','12:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00']
	});
}

var showTaskActActualTimeEdit = function(taskActId) {
	$("."+taskActId+"taskTimeEditDateLink").show();
	$("."+taskActId+"taskTimeEditLink").hide();
	$("."+taskActId+"taskTimeEditDate").hide();
	$("."+taskActId+"taskTimeEdit").show();	
	$("."+taskActId+"taskActTimeEdit").show();	
}

var hideTaskActActualTimeEdit = function(taskActId) {
	$("."+taskActId+"taskTimeEditDateLink").show();
	$("."+taskActId+"taskTimeEditLink").show();
	$("."+taskActId+"taskTimeEditDate").hide();
	$("."+taskActId+"taskTimeEdit").hide();	
	$("."+taskActId+"taskActTimeEdit").hide();	
}

var taskActualTimeSubmit = function(taskId) {
	var hours=$("#projectActivityModalContent #taskHHTimeEdit"+taskId).val();
	var mins=$("#projectActivityModalContent #taskMMTimeEdit"+taskId).val();
	
	$("#projectActivityModalContent #taskHHTimeEdit"+taskId).removeClass("error");
	$("#projectActivityModalContent #taskMMTimeEdit"+taskId).removeClass("error");
	var error = false;
	if (hours == "" || !$.isNumeric(hours)) {
		$("#projectActivityModalContent #taskHHTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if (mins == "" || !$.isNumeric(mins) || parseInt(mins) >= 60) {
		$("#projectActivityModalContent #taskMMTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if(error){
		return;
	}
	
	var newDuration = (parseInt(hours) * 60) +parseInt(mins);
	
	doAjaxRequest("POST", "${applicationHome}/adjustTaskTime", {"taskId":taskId,"newDuration":newDuration},  function(taskId) {
		 window.location.reload(true);
	}, function(e) {
	});
}

var taskActActualTimeSubmit = function(taskId, activityId) {
	var hours=$("#projectActivityModalContent #taskActHHTimeEdit"+activityId).val();
	var mins=$("#projectActivityModalContent #taskActMMTimeEdit"+activityId).val();
	
	
	$("#projectActivityModalContent #taskActHHTimeEdit"+taskId).removeClass("error");
	$("#projectActivityModalContent #taskActMMTimeEdit"+taskId).removeClass("error");
	
	var error = false;
	if (hours == "" || !$.isNumeric(hours)) {
		$("#projectActivityModalContent #taskActHHTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if (mins == "" || !$.isNumeric(mins) || parseInt(mins) >= 60) {
		$("#projectActivityModalContent #taskActMMTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if(error){
		return;
	}
	var activityIdTmp = activityId;
	var newDuration = (parseInt(hours) * 60) +parseInt(mins);
	var startTime ="";
	var endTime ="";
	doAjaxRequest("POST", "${applicationHome}/adjustTaskActivityTime", {"activityId":activityId,"taskId":taskId,"newDuration":newDuration,"startTime":startTime,"endTime":endTime},  function(activityId) {
		$("."+activityIdTmp+"taskTimeEditLinkHours").html(hours+":"+mins+" hours");
		
		/* var activityOriDuration = $("."+activityIdTmp+"tskActTimeTakenInMins").val();
		var taskOriDuration = $("#"+taskId+"timeTakenInMins").val();
		
		var newActDurationDiff = parseInt(newDuration) - parseInt(activityOriDuration); 
		var taskNewDuration = parseInt(newActDurationDiff) + parseInt(taskOriDuration);
		
		$("."+activityIdTmp+"tskActTimeTakenInMins").val(newDuration);
		$("."+taskId+"timeTakenInMins").val(taskNewDuration);
		
		var taskHrs = parseInt(taskNewDuration)/60;
		var taskMins = parseInt(taskNewDuration)%60;
		
		$("."+taskId+"actualTimeTakenInHrs").html(taskHrs+":"+taskMins);
		 */
		hideTaskActActualTimeEdit(activityIdTmp);
	}, function(e) {
	});
}


function performProjectSearch(){
	var searchText = $('#taskDetailsSearch').val();
	famstacklog(searchText);
	if (searchText != "") {
		$('.taskDetailsSection').hide();
	    $('.taskDetailsSection .taskSearchData').each(function(){
	       if($(this).text().toUpperCase().indexOf(searchText.toUpperCase()) != -1){
	           $(this).closest('.taskDetailsSection').show();
	       }
	    });
	} else {
		$('.taskDetailsSection').show();
	}
}

$("#taskDetailsSearch").keydown(function(e){
	performProjectSearch();
});

$("#taskDetailsSearch").keyup(function(e){
	performProjectSearch();
});



/******************Recurring PrjTsk model************/
 function recurringTaskModel(taskId, projectId) {
	var dataString = {"taskId": taskId};
	doAjaxRequest("GET", "${applicationHome}/getRecurringTaskDetails", dataString, function(data) {
			famstacklog(data);
			if (data != ""){
				var responseJson = JSON.parse(data);
				loadRecurringTaskDetails(taskId, responseJson);
			} else {
				$(".recurringTimeDiv").addClass("hide");
				$(".recurringDelete").addClass("hide");
				$(".recurringPrjTskDiv").addClass("hide");
				$(".recurringPrjTskName").html("");
				$(".recurringEndTime").val("");
				$("#RTCreatOrUpdate").html("Create");
				initializeCron("0 5 0 * * ?");
			}
			$("#recurringTaskActionButton").attr("onclick", "createRecurringTask('"+projectId+"',"+taskId+")");
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}
 
function initializeCron(cronExpression) {
	$('#recurringCronExpressionDiv').html('<div id="recurringCronExpression"></div>');
	$('#recurringCronExpression').cron({
 	 initial: cronExpression,
      onChange: function() {
          $('#recurringCronExpressionValue').val($(this).cron("value"));
      },
      customValues: {
         // "5 Minutes" : "*/5 * * * *",
         // "2 Hours on Weekends" : "0 */2 * * 5,6"
      },
      useGentleSelect: true
 });
}
 
function loadRecurringTaskDetails(taskId, responseJson){
	famstacklog(responseJson);
	$("#RTCreatOrUpdate").html("Update");
	$(".recurringTimeDiv").removeClass("hide");
	$(".recurringDelete").removeClass("hide");
	$(".recurringEndTime").val(responseJson.endDateString);
	$("#recurringCronExpressionValue").val(responseJson.cronExpression);
	$(".recurringTaskDiv").removeClass("hide");

	if ( responseJson.recurreOriginal) {
		$(".recurreOriginal").prop("checked", "checked");
	} else {
		$(".recurreOriginal").removeAttr("checked");
	}
	
	initializeCron(responseJson.cronExpression);
	$("#recurringNET").html(responseJson.nextRun);
	if (responseJson.lastRun != null){
		$("#recurringLET").html(responseJson.lastRun);
	} else {
		$("#recurringLET").html("Not yet run");
	}
	$(".recurringDelete").attr("onclick", "deleteRecurringTask("+responseJson.id+","+responseJson.taskId+")");
}

function createRecurringTask(projectId, taskId) {
	var cronExp = $("#recurringCronExpressionValue").val();
	var recurringEndDate = $("#recurringEndDate").val();
	var recurreOriginal = $(".recurreOriginal").prop("checked");
	
	$("#recurringEndDate").css("border", " 0px solid red");
	
	if (new Date(recurringEndDate) < new Date()) {
		$("#recurringEndDate").css("border", " 1px solid red");
		return;
	}
	
	var dataString = {"projectId": projectId, "taskId": taskId, "cronExp":cronExp, "recurringEndDate":recurringEndDate, "recurreOriginal" : recurreOriginal};
	doAjaxRequest("POST", "${applicationHome}/createRecurringTask", dataString, function(data) {
			famstacklog(data);
			if (data != ""){
				var responseJson = JSON.parse(data);
				loadRecurringTaskDetails(taskId, responseJson);
				$(".recurringSpinTask"+taskId).addClass("fa-spin");
				$(".recurringSpinTask"+taskId).html('');
			}
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}

function deleteRecurringTask(recurringId, taskId) {
	$(".msgConfirmText").html("Deleting recurring project task schedule");
	$(".msgConfirmText1").html(recurringId);
	$("#confirmYesId").prop("href","javascript:deleteRecuringTaskDetails('"+recurringId+"','"+taskId+"')");
}

function deleteRecuringTaskDetails(recurringId, taskId) {
	var dataString = {"recurringId": recurringId};
	doAjaxRequest("POST", "${applicationHome}/deleteRecuringTaskDetails", dataString, function(data) {
			famstacklog(data);
			$(".recurringSpinTask"+taskId).removeClass("fa-spin");
			$(".recurringSpinTask"+taskId).html('');
			initializeCron("0 5 0 * * ?");
			$(".message-box").removeClass("open");
			$("#RTCreatOrUpdate").html("Create");
			$(".recurringTimeDiv").addClass("hide");
			$(".recurringDelete").addClass("hide");
			$(".recurringEndTime").val("");
			$(".recurreOriginal").prop("checked", "checked");
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}

function refreshRecurringSpin(projectId){
	doAjaxRequest("GET", "${applicationHome}/getAllRecuringTaskByProjectId", {projectId:projectId}, function(data) {
			famstacklog(data);
			if (data != ""){
				var responseJson = JSON.parse(data);
				$.each(responseJson, function(idx, elem){
					$(".recurringSpinTask"+elem).addClass("fa-spin");
					$(".recurringSpinTask"+elem).html('<span class="hide">Recurring</span>');
				});
			} 
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
}

$('.recurringEndTime').datetimepicker({
	timepicker:false,
	formatDate:'Y/m/d',
	format:'Y/m/d',
	minDate:new Date()
});

<c:if test="${projectDetails != null}">
$( document ).ready(function(){
refreshRecurringSpin(${projectDetails.id});
});
</c:if>
/*********recurring ends*********/
</script>


