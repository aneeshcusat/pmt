<%@include file="includes/header.jsp" %>           
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>                                
<style>
    @media screen and (min-width: 800px) {
    #registerusermodal .modal-dialog  {width:65%;}
    
    
    .cropit-preview {
        background-color: #f8f8f8;
        background-size: cover;
        border: 1px solid #ccc;
        border-radius: 3px;
        margin-top: 7px;
        width: 250px;
        height: 250px;
      }

      .cropit-preview-image-container {
        cursor: move;
      }

      .image-size-label {
        margin-top: 10px;
      }

      input, .export {
        display: block;
      }

      button {
        margin-top: 10px;
      }
}
</style>         
<!-- PAGE TITLE -->
<div class="page-title">                    
    <h2><span class="fa fa-users"></span> Employees <small>139 contacts</small></h2>
</div>
<!-- END PAGE TITLE -->                

<!-- PAGE CONTENT WRAPPER -->
<div class="page-content-wrap">
    
    <div class="row">
        <div class="col-md-12">
            
            <div class="panel panel-default">
                <div class="panel-body">
                    <p>Use search to find contacts. You can search by: name, address, phone. Or use the advanced search.</p>
                    <form class="form-horizontal">
                        <div class="form-group">
                            <div class="col-md-8">
                                <div class="input-group">
                                    <div class="input-group-addon">
                                        <span class="fa fa-search"></span>
                                    </div>
                                    <input type="text" class="form-control" placeholder="Who are you looking for?"/>
                                    <div class="input-group-btn">
                                        <button class="btn btn-primary">Search</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                            <a data-toggle="modal" data-target="#registerusermodal" class="btn btn-success btn-block">
                               <span class="fa fa-plus"></span> Register a new Employee</a>
                            </div>
                        </div>
                    </form>                                    
                </div>
            </div>
            
        </div>
    </div>
    
    <div class="row">
    <c:if test="${not empty employeesData}">
    <c:forEach var="user" items="${employeesData}">
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${user.filePhoto}" alt="Nadia Ali"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">${user.firstName}</div>
                        <div class="profile-data-title">${user.designation}</div>
                    </div>
                    <div class="profile-controls">
                        <a data-toggle="modal" class="profile-control-left" data-target="#registerusermodal" onclick="javascript:loadUser('${user.id}')"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>${user.mobileNumber}</p>
                        <p><small>Email</small><br/>${user.email}</p>
                        <p><small>Group</small><br/>${user.group}</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        </c:forEach>
        </c:if>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/user.jpg" alt="Dmitry Ivaniuk"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">Dmitry Ivaniuk</div>
                        <div class="profile-data-title">Web Developer / UI/UX Designer</div>
                    </div>
                    <div class="profile-controls">
                        <a href="#" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(333) 333-33-22</p>
                        <p><small>Email</small><br/>dmitry@domain.com</p>                                        
                        <p><small>Address</small><br/>123 45 Street San Francisco, CA, USA</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/user2.jpg" alt="John Doe"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">John Doe</div>
                        <div class="profile-data-title">Web Developer/Designer</div>
                    </div>
                    <div class="profile-controls">
                        <a href="#" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(234) 567-89-12</p>
                        <p><small>Email</small><br/>john@domain.com</p>
                        <p><small>Address</small><br/>123 45 Street San Francisco, CA, USA</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/user4.jpg" alt="Brad Pitt"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">Brad Pitt</div>
                        <div class="profile-data-title">Actor and Film Producer</div>
                    </div>
                    <div class="profile-controls">
                        <a href="#" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(321) 777-55-11</p>
                        <p><small>Email</small><br/>brad@domain.com</p>
                        <p><small>Address</small><br/>123 45 Street San Francisco, CA, USA</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/user5.jpg" alt="John Travolta"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">John Travolta</div>
                        <div class="profile-data-title">Actor</div>
                    </div>
                    <div class="profile-controls">
                        <a href="#" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(111) 222-33-78</p>
                        <p><small>Email</small><br/>travolta@domain.com</p>
                        <p><small>Address</small><br/>123 45 Street San Francisco, CA, USA</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/user6.jpg" alt="Darth Vader"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">Darth Vader</div>
                        <div class="profile-data-title">Cyborg</div>
                    </div>
                    <div class="profile-controls">
                        <a href="#" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(000) 000-00-01</p>
                        <p><small>Email</small><br/>vader@domain.com</p>
                        <p><small>Address</small><br/>Somewhere deep in space</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/user7.jpg" alt="Samuel Leroy Jackson"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">Samuel Leroy Jackson</div>
                        <div class="profile-data-title">Actor and film producer</div>
                    </div>
                    <div class="profile-controls">
                        <a href="#" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(552) 221-23-25</p>
                        <p><small>Email</small><br/>samuel@domain.com</p>
                        <p><small>Address</small><br/>123 45 Street San Francisco, CA, USA</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
        <div class="col-md-3">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                    <div class="profile-image">
                        <img src="${assets}/images/users/no-image.jpg" alt="Samuel Leroy Jackson"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">Alex Sonar</div>
                        <div class="profile-data-title">Designer</div>
                    </div>
                    <div class="profile-controls">
                        <a href="pages-profile.html" class="profile-control-left"><span class="fa fa-edit"></span></a>
                        <a href="#" class="profile-control-right"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>(213) 428-74-13</p>
                        <p><small>Email</small><br/>alex@domain.com</p>
                        <p><small>Address</small><br/>123 45 Street San Francisco, CA, USA</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
                    </div>                        
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <ul class="pagination pagination-sm pull-right push-down-10 push-up-10">
                            <li class="disabled"><a href="#">«</a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>                                    
                            <li><a href="#">»</a></li>
                        </ul>                            
                    </div>
                </div>

            </div>
            
            <div class="modal fade" id="registerusermodal" tabindex="-1"
			role="dialog" aria-labelledby="reprocessConfirmation"
			aria-hidden="true">
			<form:form id="createUserFormId" action="createEmployee" method="POST" role="form" class="form-horizontal">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Register Employee</h4>
					</div>
					<div class="modal-body">
						<%@include file="fagments/userregistermodal.jsp" %>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-dismiss="modal">Cancel</button>
							<button type="button" onclick="doAjaxCreateUserForm();" class="btn btn-primary"><span id="userButton">Save</span></button>
						</div>
					</div>
				</div>
				
				</form:form>
			</div>
             <!-- END PAGE CONTENT WRAPPER -->                                                 
<%@include file="includes/footer.jsp" %>
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js"></script> 

<script>


$('#createUserFormId').ajaxForm(function(response) { 
	console.log(response);
	var responseJson = JSON.parse(response);
    if (responseJson.status){
        window.location.reload(true);
    }
}); 

function doAjaxCreateUserForm(){
	var imageData = $('.image-editor').cropit('export');
	$('#filePhoto').val(imageData);
    $('#createUserFormId').submit();
}

function loadUser(userId) {
	$.ajax({
        type : "GET",
        contentType : "application/json",
        url : "${home}editEmployee",
        data: "userId="+userId,
        timeout : 1000,
        success : function(data) {
            console.log("SUCCESS: ", data);
            processUserResponseData(data);
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
function processUserResponseData(data) {
	var response = JSON.parse(data);
	$('#firstName').val(response.firstName);
	$('#mobileNumber').val(response.mobileNumber);
	$('#dateOfBirth').val(response.dateOfBirth);
	$('#lastName').val(response.lastName);
	$('#email').val(response.email);
	$('#id').val(response.id);
	$('#'+response.gender).click();
	
	$('#role').val(response.role);
	$('#qualification').val(response.qualification);
	$('#group').val(response.group);
	$('#designation').val(response.designation);
	$('#role').selectpicker('refresh');
	$('#qualification').selectpicker('refresh');
	$('#group').selectpicker('refresh');
	$('#designation').selectpicker('refresh');
	
}

$(function() {
    $('.image-editor').cropit({
      imageState: {
        src: 'http://lorempixel.com/500/400/',
      },
    });

    $('.export').click(function() {
      var imageData = $('.image-editor').cropit('export');
      window.open(imageData);
    });
  });

</script>        