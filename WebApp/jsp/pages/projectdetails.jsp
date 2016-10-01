<%@include file="includes/header.jsp" %>
<style>
.disabled .fc-day-content {
    background-color: #123959;
    color: #FFFFFF;
    cursor: default;
  }
.project_team img {
	width:40px;
	border: 2px solid #FFF;
	border-radius: 20%;
}
.project_progress .progress{
	margin-bottom: 5px;
    height: 8px;
    }
.project_details .bold{
	font-weight: bold;

}
.project strong{
margin-top: 5px;
margin-left: 15px;
}
.panel .panel-heading{
border-bottom: 1px solid lightgray;
}
/*
.dropzone {
width: 100%;
height: 80px;
min-height: 0px !important;
}   
.dropzone .dz-default.dz-message{
/*height: 80px;*/
}
*/

</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top">  
	<div class="row">
                  <div class="col-md-7">                      
            <h4><span class="fa "></span>${projectDetails.name}</h4>
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
                                      <p><span class="bold">Created by </span>: ${projectDetails.createdByName}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Status </span>: <span class="label label-primary">${projectDetails.status}</span></p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Created </span>: ${projectDetails.createdDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Last Updated</span>: ${projectDetails.modifiedDate}</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Client </span>: ${projectDetails.clientName}</p>
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
                                         <a href="#"><img alt="image" src="${projectDetails.assigneePhoto}"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
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
                                  ${projectDetails.description}
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
					                    <img src="${comment.user.filePhoto}" alt="${comment.user.firstName}&nbsp;${comment.user.lastName}">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">${comment.user.firstName}&nbsp;${comment.user.lastName}</a>
					                        <span class="date">${comment.createdDate}</span>
					                    </div>                                    
					                   ${comment.description}
					                </div>
					            </div>
					         </c:forEach>
					         </c:if>   
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
                          <tr>
                              <td>
                                  Project analysis
                              </td>
                              <td>
                                  28/11/2014 12:23:03
                              </td>
                              <td>
                                   Ipsum is that it has a as opposed to using Lorem Ipsum is that it has a as opposed to using
                              </td>
                              <td>
                                  <span class="label label-info">Completed</span>
                              </td>
                          </tr>
                          <tr>
                              <td>
                                  Requirement Collection
                              </td>
                              <td>
                                  22/11/2014 12:23:03
                              </td>
                              <td>
                                  Tawseef Ipsum is that it has a as opposed to using Lorem Ipsum is that it has a as opposed to using
                              </td>
                              <td>
                                  <span class="label label-info">Reported</span>
                              </td>
                          </tr>
                          <tr>
                              <td>
                                  Design Implement
                              </td>
                              <td>
                                  28/11/2014 12:23:03
                              </td>
                              <td>
                                  Dism Ipsum is that it has a as opposed to using Lorem Ipsum is that it has a as opposed to using
                              </td>
                              <td>
                                  <span class="label label-info">Accepted</span>
                              </td>
                          </tr>
                          </tbody>
                          </table>
                        </div>
                      </section>

                  </div>
                  <div class="col-md-4">
                      <section class="panel">
                          <div class="panel-body">
                           <h5 class="bold">Tasks</h5>
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
                                             <tr >
                                            <td colspan="3" class="text-right"><a class="btn btn-info">Create a task</a></td>
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
 <%@include file="includes/footer.jsp" %>            
<script type="text/javascript"
	src="${js}/plugins/dropzone/dropzone.min.js"></script>
       <script>
       
       $(document).ready(function() {
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
       
function addComment() {
   var projectId = $('#hdn_project_id').val();
   var commentData = $('#comment_textarea').val();
   var jsonData = '{"id":0,"projectId":0,"description":""}';
   var json = JSON.parse(jsonData);
   json.description = commentData;
   json.projectId = projectId;
   $.ajax({
       type : "POST",
       contentType : "application/json",
       url : "${home}saveComment",
       data: JSON.stringify(json),
       timeout : 1000,
       beforeSend: function(xhr) {
           xhr.setRequestHeader("Accept", "application/json");
           xhr.setRequestHeader("Content-Type", "application/json");
       },
       success : function(data) {
           if (data.status){
               window.location.reload(true);
           }
       },
       error : function(e) {
           console.log("ERROR: ", e);
           alert(e);
       },
       done : function(e) {
           console.log("DONE");
       }
   });
}
</script>
