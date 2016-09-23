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
                        <img src="${user.filePhoto}" alt="${user.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                    </div>
                    <div class="profile-data">
                        <div class="profile-data-name">${user.firstName}</div>
                        <div class="profile-data-title">${user.designation}</div>
                    </div>
                    <div class="profile-controls">
                        <a data-toggle="modal" class="profile-control-left" data-target="#registerusermodal" onclick="javascript:loadUser('${user.id}')"><span class="fa fa-edit"></span></a>
                        <a href="#" data-box="#confirmationbox" class="mb-control profile-control-right" onclick="javascript:deleteUser('${user.id}')"><span class="fa fa-times"></span></a>
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
        
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <ul class="pagination pagination-sm pull-right push-down-10 push-up-10">
                            <li class="disabled"><a href="#">&lt;&lt;</a></li>
                            <li class="active"><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>                                    
                            <li><a href="#">.</a></li>
                            <li><a href="#">.</a></li>
                            <li><a href="#">&gt;&gt;</a></li>
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
    	//$.growlUI('User has been created!', 'Have a nice day!'); 
        window.location.reload(true);
    }
   //$.unblockUI();
}); 

function doAjaxCreateUserForm(){
	//$.blockUI();
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

function deleteUser(userId){
	$(".msgConfirmText").html("Delete user");
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteUser('"+userId+"')");
}

function doAjaxDeleteUser(userId) {
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "${home}deleteEmployee",
        data: "userId="+userId,
        timeout : 1000,
        success : function(data) {
            console.log("SUCCESS: ", data);
            var responseJson = JSON.parse(data);
            if (responseJson.status){
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