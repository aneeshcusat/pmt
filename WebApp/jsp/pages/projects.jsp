<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>       
<style>
 @media screen and (min-width: 700px) {
 #createprojectmodal .modal-dialog  {width:75%;}
 
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
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">    
	<div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-bars"></span> Projects </h2>
        </div>  
	</div>        
	<div class="row">
	      <div class="col-md-12">
	          
	          <div class="panel panel-default">
	              <div class="panel-body">
	                  <form class="form-horizontal">
	                      <div class="form-group">
	                          <div class="col-md-8">
	                              <div class="input-group">
	                                  <div class="input-group-addon">
	                                      <span class="fa fa-search"></span>
	                                  </div>
	                                  <input type="text" class="form-control" placeholder="Search for a project.."/>
	                                  <div class="input-group-btn">
	                                      <button class="btn btn-primary">Search</button>
	                                  </div>
	                              </div>
	                          </div>
	                          <div class="col-md-4">
	                             <a data-toggle="modal" data-target="#createprojectmodal" class="btn btn-success btn-block">
	                             <span class="fa fa-plus"></span> Create a new Project</a>
	                          </div>
	                      </div>
	                  </form>                                    
	              </div>
	          </div>
	          
	      </div>
	  </div>
	<div class="page-title">                    
          <h4><span class="fa fa-clock-o"></span>Unassinged </h4>
    </div>
      <table class="table table-hover p-table">
          <thead>
          <tr>
              <th>Project Name</th>
              <th>Assignees</th>
              <th>Project Progress</th>
              <th>Project Status</th>
              <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr>
              <td class="project_name">
                  <a href="projectdetails.jsp">Project 1</a>
                  <br>
                  <small>Created 27.11.2014</small>
              </td>
              <td class="project_team">
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user2.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user3.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user4.jpg"></a>
              </td>
              <td class="project_progress">
                  <div class="progress progress-xs">
                      <div style="width: 87%;" class="progress-bar progress-bar-success"></div>
                  </div>
                  <small>87% Complete </small>
              </td>
              <td>
                 <span class="label label-info">Inprogress</span>
              </td>
              <td>
               	   <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-folder-open-o"></span></button>
                  <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-pencil"></span></button>
                  <button class="btn btn-danger btn-rounded btn-sm" onclick="delete_row('trow_1');"><span class="fa fa-times"></span></button>
              </td>
          </tr>
            <tr>
              <td class="project_name">
                  <a href="projectdetails.jsp">Project 1</a>
                  <br>
                  <small>Created 27.11.2014</small>
              </td>
              <td class="project_team">
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user2.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user3.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user4.jpg"></a>
              </td>
              <td class="project_progress">
                  <div class="progress progress-xs">
                      <div style="width: 10%;" class="progress-bar progress-bar-danger"></div>
                  </div>
                  <small>10% Complete </small>
              </td>
              <td>
                 <span class="label label-success">New</span>
              </td>
              <td>
               	   <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-folder-open-o"></span></button>
                  <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-pencil"></span></button>
                  <button class="btn btn-danger btn-rounded btn-sm" onclick="delete_row('trow_1');"><span class="fa fa-times"></span></button>
              </td>
          </tr>
            <tr>
              <td class="project_name">
                  <a href="projectdetails.jsp">Project 1</a>
                  <br>
                  <small>Created 27.11.2014</small>
              </td>
              <td class="project_team">
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user2.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user3.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user4.jpg"></a>
              </td>
              <td class="project_progress">
                  <div class="progress progress-xs">
                      <div style="width: 50%;" class="progress-bar progress-bar-warning"></div>
                  </div>
                  <small>50% Complete </small>
              </td>
              <td>
                 <span class="label label-success">New</span>
              </td>
              <td>
               	   <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-folder-open-o"></span></button>
                  <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-pencil"></span></button>
                  <button class="btn btn-danger btn-rounded btn-sm" onclick="delete_row('trow_1');"><span class="fa fa-times"></span></button>
              </td>
          </tr>
          </tbody>
      </table>
      
      
      <div class="page-title">                    
          <h4><span class="fa fa-clock-o"></span>Unassinged </h4>
    </div>
      <table class="table table-hover p-table">
          <thead>
          <tr class="">
              <th>Project Name</th>
              <th>Assignees</th>
              <th>Project Progress</th>
              <th>Project Status</th>
              <th>Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr>
              <td class="project_name">
                  <a href="projectdetails.jsp">Project 1</a>
                  <br>
                  <small>Created 27.11.2014</small>
              </td>
              <td class="project_team">
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user2.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user3.jpg"></a>
                  <a href="#"><img alt="image" class="" src="${assets}/images/users/user4.jpg"></a>
              </td>
              <td class="project_progress">
                  <div class="progress progress-xs">
                      <div style="width: 87%;" class="progress-bar progress-bar-success"></div>
                  </div>
                  <small>87% Complete </small>
              </td>
              <td>
                 <span class="label label-info">Inprogress</span>
              </td>
              <td>
               	   <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-folder-open-o"></span></button>
                  <button class="btn btn-default btn-rounded btn-sm"><span class="fa fa-pencil"></span></button>
                  <button class="btn btn-danger btn-rounded btn-sm" onclick="delete_row('trow_1');"><span class="fa fa-times"></span></button>
              </td>
          </tr>
           
          </tbody>
      </table>
</div>               
<!-- END CONTENT FRAME -->     

<!-- project create modal start -->
<div class="modal fade" id="createprojectmodal" tabindex="-1"
role="dialog" aria-labelledby="createprojectmodal"
aria-hidden="true">
<form:form id="createProjectFormId" action="createProject" method="POST" role="form" class="form-horizontal">
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
			<%@include file="fagments/createprojectmodal.jsp" %>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-secondary"
			data-dismiss="modal">Cancel</button>
		<button type="button" onclick="doAjaxCreateProjectForm();" class="btn btn-primary"><span id="userButton">Save</span></button>
		</div>
	</div>
</div>
</form:form>
</div>
<!-- project create modal end -->
                           
<%@include file="includes/footer.jsp" %>            
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js"></script>
<script type="text/javascript" src="${js}/plugins/dropzone/dropzone.min.js"></script>
<script type="text/javascript" src="${js}/plugins/autocomplete/jquery.autocomplete.js"></script>

<script>
var countries = [
                 { value: 'Andorra', data: 'AD' },
                 // ...
                 { value: 'Zimbabwe', data: 'ZZ' }
             ];

$('.autocomplete').autocomplete({
    //serviceUrl: '/autocomplete/countries',
     lookup: countries,
    onSelect: function (suggestion) {
        alert('You selected: ' + suggestion.value + ', ' + suggestion.data);
    }
});
$(document).ready(function () {
    Dropzone.autoDiscover = false;
    $("#my-dropzone").dropzone({
        url: "/upload",
        addRemoveLinks: true,
        success: function (file, response) {
            var imgName = response;
            file.previewElement.classList.add("dz-success");
            console.log("Successfully uploaded :" + imgName);
        },
        error: function (file, response) {
            file.previewElement.classList.add("dz-error");
        }
    });
});
</script>
       





