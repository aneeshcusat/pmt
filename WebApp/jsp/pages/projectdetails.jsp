<%@include file="includes/header.jsp" %>
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li><a href="${applicationHome}/projects">Projects</a></li>                  
     <li class="active">${projectDetails.name}</li>
 </ul>
 <!-- END BREADCRUMB -->  
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top">  
	<div class="row">
                  <div class="col-md-7">                      
            <h4><span class="fa "></span><span class="editableFieldText">${projectDetails.name}</span></h4>
            </div>
            <div class="col-md-5 text-right">               
           <span style="background-color: lightgray;" class="btn  btn-default" disabled=disabled>To Do</span>       
           <span style="background-color: lightblue;"  class="btn  btn-default" disabled=disabled>Assign</span>
           <a href="#" class="btn  btn-default">Reviewed</a>
           <a href="#" class="btn  btn-default">Completed</a>
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
                                  <div class="col-md-6">
                                      <p><span class="bold">Created by </span>: ${projectDetails.reporterName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Status </span>: <span class="label label-primary">${projectDetails.status}</span></p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Created </span>: ${projectDetails.createdDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Last Updated</span>: ${projectDetails.lastModifiedDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Client </span>: ${projectDetails.clientId}</p>
                                  </div>
                                   <div class="col-md-6">
                                   	<ul class="nav nav-pills nav-stacked labels-info ">
                                 	 <li><i class=" fa fa-circle text-danger"></i> ${projectDetails.priority}<p></p></li>
	                              </ul>
	                              </div>
	                              <input type="hidden" id="hdn_project_id" value="${projectDetails.id}">
                                  <div class="col-md-6">
                                      <p><span class="bold">Assignee </span>:
                                      <span class="project_team">
                                         <a href="#"><img alt="image" src=""  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
                                      </span>
                                      </p>
                                  </div>

                                  <div class="col-lg-12">
                                      <dl class="dl-horizontal mtop20 p-progress">
                                          <dt>Project Completed:</dt>
                                          <dd>
                                              <div class="progress progress-striped active ">
                                                  <div style="width: 40%;" class="progress-bar progress-bar-danger"></div>
                                              </div>
                                              <small>Project completed in <strong>80%</strong>. Remaining close the project, sign a contract and invoice.</small>
                                          </dd>
                                      </dl>
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
						<h4>Comments</h4>
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
					         <div class="panel panel-default push-up-10">
                                    <div class="input-group">
                                        <textarea class="form-control" placeholder="Enter your comments" id="comment_textarea"></textarea>
                                        <div class="input-group-btn">
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
                          <div class="panel-body">
                           <div class="row padding-bottom-5" >
                          <div class="col-md-7">
                           <h5 class="bold">Tasks</h5>
                          </div>
                           <div class="col-md-5 text-right">
                            <a data-toggle="modal" data-target="#createtaskmodal" class="btn btn-success line-height-15">
                               <span class="fa fa-plus"></span> Create a Task</a>
                            </div>
                          </div>
                              <table class="table">
                                        <tbody>
                                            <tr>
                                             <td width="5%">1</td>
                                                <td><a href="#">Create slide 1 to 10</a> 
                                                 <div class="progress progress-small progress-striped active">
                                        			<div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: 50%;">50%</div>
                                    			</div>
                                                </td>
                                               <td  width="10%"><span class="label label-warning">Inprogress</span></td>
                                            </tr>
                                            <tr>
                                            <td>2</td>
                                                <td><a href="#">Create slide 11 to 20</a>
                                                 <div class="progress progress-small progress-striped active">
                                        			<div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: 50%;">50%</div>
                                    			</div>
                                                </td>
                                                  <td><span class="label label-success">New</span></td>
                                            </tr>
                                            <tr>
                                            <td>3</td>
                                                <td><a href="#">Review all slides</a>
                                                
                                                 <div class="progress progress-small progress-striped active">
                                        			<div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="50" aria-valuemin="0" aria-valuemax="100" style="width: 50%;">50%</div>
                                    			</div>
                                                </td>
                                                  <td><span class="label label-success">New</span></td>
                                            </tr>
                                       </tbody>
                                  </table>
                               <h5 class="bold">Time Tracking</h5>
                              <ul class="nav nav-pills nav-stacked labels-info ">
                                  <li>Estimated start time:<b> 2014-12-12 12:00AM</b></li>
                                  <li>Estimated start time:<b> 2014-12-12 12:00AM</b></li>
                                  <li>Duration : <b>5 hours</b></li>
                              </ul>

                              <br>
                              
                              <h5 class="bold">Project files</h5>
                              <ul class="list-unstyled p-files">
                                  <li><a href=""><i class="fa fa-file-text"></i> Project-document.docx</a></li>
                                  <li><a href=""><i class="fa fa-picture-o"></i> Logo-company.jpg</a></li>
                                  <li><a href=""><i class="fa fa-mail-forward"></i> Email-from-flatbal.mln</a></li>
                                  <li><a href=""><i class="fa fa-file"></i> Contract-10_12_2014.docx</a></li>
                              </ul>
                              <br>

                              <h5 class="bold">Project Tags</h5>
                             <ul class="list-tags">
	                            <li><a href="#"><span class="fa fa-tag"></span> amet</a></li>
	                            <li><a href="#"><span class="fa fa-tag"></span> rutrum</a></li>
	                            <li><a href="#"><span class="fa fa-tag"></span> nunc</a></li>
	                            <li><a href="#"><span class="fa fa-tag"></span> tempor</a></li>
	                            <li><a href="#"><span class="fa fa-tag"></span> eros</a></li>
	                            <li><a href="#"><span class="fa fa-tag"></span> suspendisse</a></li>
	                            <li><a href="#"><span class="fa fa-tag"></span> dolor</a></li>
                        	</ul>

                              <div class="text-center mtop20">
                                <!--   <a href="#" class="btn  btn-primary">Add files</a>
                                  <a href="#" class="btn btn-warning">Report contact</a> -->
                              </div>
                          </div>

                      </section>
                  </div>
              </div>
    
</div>               
<!-- END CONTENT FRAME -->       

<!-- task create modal start -->
<div class="modal fade" id="createtaskmodal" tabindex="-1"
	role="dialog" aria-labelledby="createtaskmodal" aria-hidden="true">
	<form:form id="createProjectFormId" action="createTask" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Create a Project</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/createTaskModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Cancel</button>
					<button type="button" onclick="doAjaxCreateProjectForm();"
						class="btn btn-primary">
						<span id="userButton">Save</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
                         
 <%@include file="includes/footer.jsp" %>            
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
	<script type="text/javascript"
	src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
       <script>
       
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
  			url : "/upload",
  			addRemoveLinks : true,
  			success : function(file, response) {
  				var imgName = response;
  				file.previewElement.classList.add("dz-success");
  				console.log("Successfully uploaded :" + imgName);
  			},
  			error : function(file, response) {
  				file.previewElement.classList.add("dz-error");
  			}
  		});
});
</script>
