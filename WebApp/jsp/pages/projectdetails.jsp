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
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top">                        
            <h4><span class="fa "></span> Project - Prj 10002 </h4>
	</div>        
	      <div class="row">
                  <div class="col-md-8">
                      <section class="panel">
                          <div class="project project-heading">
                              <strong> Short description </strong>
                          </div>
                          <div class="panel-body bio-graph-info">
                              <!--<h1>New Dashboard BS3 </h1>-->
                              <div class="row project_details">
                                  <div class="col-md-6">
                                      <p><span class="bold">Created by </span>: Jonathan Smith</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Status </span>: <span class="label label-primary">Active</span></p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Created </span>: 13.05.2014 10:16:23</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Last Updated</span>: 22.08.2014 03:11:45</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Client </span>: <a href="#">Themeforest</a></p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Version </span>: v.2.3</p>
                                  </div>
                                  <div class="col-md-6">
                                      <p><span class="bold">Participants </span>:
                                      <span class="project_team">
                                         <a href="#"><img alt="image" class="" src="${assets}/images/users/user2.jpg"></a>
						                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user3.jpg"></a>
						                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user4.jpg"></a>
                                      </span>
                                      </p>
                                  </div>

                                  <div class="col-lg-12">
                                      <dl class="dl-horizontal mtop20 p-progress">
                                          <dt>Project Completed:</dt>
                                          <dd>
                                              <div class="progress progress-striped active ">
                                                  <div style="width: 80%;" class="progress-bar progress-bar-success"></div>
                                              </div>
                                              <small>Project completed in <strong>80%</strong>. Remaining close the project, sign a contract and invoice.</small>
                                          </dd>
                                      </dl>
                                  </div>
                              </div>

                          </div>

                      </section>
						<div class="col-md-12">
						<h4>Comments</h4>
							<div class="row">
							  <div class="messages messages-img">
					              <div class="item">
					                <div class="image">
					                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">Dmitry Ivaniuk</a>
					                        <span class="date">08:39</span>
					                    </div>                                    
					                    Integer et ipsum vitae urna mattis dictum. Sed eu sollicitudin nibh, in luctus velit.
					                </div>
					            </div>
					            
					              <div class="item">
					                <div class="image">
					                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">Dmitry Ivaniuk</a>
					                        <span class="date">08:39</span>
					                    </div>                                    
					                    Integer et ipsum vitae urna mattis dictum. Sed eu sollicitudin nibh, in luctus velit.
					                </div>
					            </div>
					            
					              <div class="item">
					                <div class="image">
					                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">Dmitry Ivaniuk</a>
					                        <span class="date">08:39</span>
					                    </div>                                    
					                    Integer et ipsum vitae urna mattis dictum. Sed eu sollicitudin nibh, in luctus velit.
					                </div>
					            </div>
					            
					              <div class="item">
					                <div class="image">
					                    <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk">
					                </div>                                
					                <div class="text">
					                    <div class="heading">
					                        <a href="#">Dmitry Ivaniuk</a>
					                        <span class="date">08:39</span>
					                    </div>                                    
					                    Integer et ipsum vitae urna mattis dictum. Sed eu sollicitudin nibh, in luctus velit.
					                </div>
					            </div>
					            <div class="panel panel-default push-up-10">
					                <div class="input-group">
					                    <textarea class="form-control" placeholder="Enter your comments"></textarea>
					                    <div class="input-group-btn">
					                        <button class="btn btn-primary">Add</button>
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
                          <header class="panel-heading">
                            <h4> Projects Description</h4>
                          </header>

                          <div class="panel-body">
                              <p>
                                  Sometimes the simplest things are the hardest to find. I imagined a line of my favorite pieces, the things i would live in every day, all year round. So I stopped looking and started designing. Sometimes the simplest things are the hardest to find. Sometimes the simplest things are the hardest to find. I imagined a line of my favorite pieces, the things i would live in every day, all year round. So I stopped looking and started designing. Sometimes the simplest things are the hardest to find.
                              </p>
                              <br>
                               <h5 class="bold">Time Tracking</h5>
                              <ul class="nav nav-pills nav-stacked labels-info ">
                                  <li>Estimated time : 2014-12-12 12:00AM<p></p></li>
                                  <li>Completed time : 2014-12-12 12:00AM<p></p></li>
                              </ul>

                              <br>
                              
                              <h5 class="bold">Priority</h5>
                              <ul class="nav nav-pills nav-stacked labels-info ">
                                  <li><i class=" fa fa-circle text-danger"></i> High Priority<p></p></li>
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
                                  <a href="#" class="btn  btn-primary">Add files</a>
                                  <a href="#" class="btn btn-warning">Report contact</a>
                              </div>
                          </div>

                      </section>
                  </div>
              </div>
    
</div>               
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>            

       





