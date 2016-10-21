<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>           
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Employees</li>
 </ul>
 <!-- END BREADCRUMB -->  
<style>
    @media screen and (min-width: 800px) {
    #registerusermodal .modal-dialog  {width:65%;}
    .cropit-preview {
        background-color: #f8f8f8;
        background-size: cover;
        border: 1px solid #ccc;
        border-radius: 3px;
        margin-top: 7px;
        width: 175px;
        height: 175px;
        cursor: move;
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
      
      
      .cropit-image-background {
        opacity: .2;
        cursor: auto;
      }

      .image-size-label {
        margin-top: 10px;
      }

      input {
        display: block;
      }

      .export {
        margin-top: 10px;
      }

}

</style>         
<!-- PAGE TITLE -->
<div class="page-title">                    
    <h2><span class="fa fa-users"></span> Employees <small>${fn:length(userMap)} contacts</small></h2>
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
                                    <input type="text" class="form-control" placeholder="Who are you looking for?" id="employeeSearch"/>
                                    <div class="input-group-btn">
                                        <button class="btn btn-primary">Search</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                            <a data-toggle="modal" data-target="#registerusermodal" class="btn btn-success btn-block" onclick="createEmployeeDetails()">
                               <span class="fa fa-plus"></span> Register a new Employee</a>
                            </div>
                        </div>
                    </form>                                    
                </div>
            </div>
            
        </div>
    </div>
    
    <div class="row">
    <c:if test="${not empty userMap}">
    <c:forEach var="user" items="${userMap}">
    <c:if test="${user.role != 'SUPERADMIN' || currentUser.userRole == 'SUPERADMIN'}">
        <div class="col-md-3 contact-name">
            <!-- CONTACT ITEM -->
            <div class="panel panel-default">
                <div class="panel-body profile">
                 	<a href="${applicationHome}/profile/${user.id}">
                    <div class="profile-image">
                        <img src="${applicationHome}/image/${user.id}" alt="${user.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
                    </div>
                    </a>
                    <div class="profile-data">
                        <div class="profile-data-name">${user.firstName}</div>
                        <div class="profile-data-title">${user.designation}</div>
                    </div>
                    <div class="profile-controls">
                        <a data-toggle="modal" class="profile-control-left" data-target="#registerusermodal" onclick="javascript:loadUser('${user.id}')"><span class="fa fa-edit"></span></a>
                        <a href="#" data-box="#confirmationbox" class="mb-control profile-control-right" onclick="javascript:deleteUser('${user.id}','${user.lastName} ${user.firstName}')"><span class="fa fa-times"></span></a>
                    </div>
                </div>                                
                <div class="panel-body">                                    
                    <div class="contact-info">
                        <p><small>Mobile</small><br/>${user.mobileNumber}</p>
                        <p><small>Email</small><br/>${user.email}</p>
                        <p><small>Team</small><br/>${user.team}</p>                                   
                    </div>
                </div>                                
            </div>
            <!-- END CONTACT ITEM -->
        </div>
         </c:if>
        </c:forEach>
        </c:if>
        
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
						<%@include file="fagments/userRegisterModal.jspf" %>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-dismiss="modal">Cancel</button>
							<a id="createOrUpdateEmployeeId" href="#" class="btn btn-primary"><span id="userButton">Save</span></a>
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

function createEmployeeDetails(){
	clearUserData();
	$("#createOrUpdateEmployeeId span").html("Save");
	$("#createOrUpdateEmployeeId").prop("href","javascript:doAjaxCreateUserForm()");
}

function doAjaxUpdateUserForm(){
	//$.blockUI();
	var imageData = $('.image-editor').cropit('export');
	$('#filePhoto').val(imageData);
	console.log(imageData);
	$('#createUserFormId').prop("action", "updateEmployee");
    $('#createUserFormId').submit();
}

function doAjaxCreateUserForm(){
	//$.blockUI();
	$('#createUserFormId').prop("action", "createEmployee");
	var imageData = $('.image-editor').cropit('export');
	$('#filePhoto').val(imageData);
    $('#createUserFormId').submit();
}

function loadUser(userId) {
	$.ajax({
        type : "GET",
        contentType : "application/json",
        url : "${applicationHome}/editEmployee",
        data: "userId="+userId,
        timeout : 1000,
        success : function(data) {
            console.log("SUCCESS: ", data);
            processUserResponseData(data);
            $("#createOrUpdateEmployeeId span").html("Update");
            $("#createOrUpdateEmployeeId").prop("href","javascript:doAjaxUpdateUserForm()");
        },
        error : function(e) {
            console.log("ERROR: ", e);
        },
        done : function(e) {
            console.log("DONE");
        }
    });

}

function deleteUser(userId, userName){
	$(".msgConfirmText").html("Delete user");
	$(".msgConfirmText1").html(userName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteUser('"+userId+"')");
}

function doAjaxDeleteUser(userId) {
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "${applicationHome}/deleteEmployee",
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
		$(".cropit-preview").show();
		$(".cropit-preview-image").attr("onerror","this.src='${assets}/images/users/no-image.jpg'");
		$(".cropit-preview-image").prop("src", "${applicationHome}/image/" + response.id);
		
	$('#role').val(response.role);
	$('#reportingManger').val(response.reportingManger);
	$('#qualification').val(response.qualification);
	$('#team').val(response.team);
	$('#designation').val(response.designation);
	
	$('#role').selectpicker('refresh');
	$('#reportingManger').selectpicker('refresh');
	$('#qualification').selectpicker('refresh');
	$('#team').selectpicker('refresh');
	$('#designation').selectpicker('refresh');
	
}

function clearUserData() {
	$('#firstName').val("");
	$('#mobileNumber').val("");
	$('#dateOfBirth').val("");
	$('#lastName').val("");
	$('#email').val("");
	$('#id').val("");
	$(".cropit-preview").hide();
	$(".cropit-preview-image").prop("src", "");
	$('#role').val("");
	$('#qualification').val("");
	$('#team').val("");
	$('#designation').val("");
	$(".genderGroup label").removeClass("active");
	$('#role').prop('selectedIndex', 0);
	$('#qualification').prop('selectedIndex', 0);
	$('#group').prop('selectedIndex', 0);
	$('#designation').prop('selectedIndex', 0);
	$('#reportingManger').val("");
	$('#role').selectpicker('refresh');
	$('#reportingManger').selectpicker('refresh');
	$('#qualification').selectpicker('refresh');
	$('#team').selectpicker('refresh');
	$('#designation').selectpicker('refresh');
	
}

$(function() {
    $('.image-editor').cropit();
  });

function performSearch(){
	var serarchText = $('#employeeSearch').val();
	console.log(serarchText);
	if (serarchText != "") {
	 $('.contact-name').hide();
    $('.contact-name').each(function(){
       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
           $(this).show();
       }
    });
	} else {
		$('.contact-name').show();
	}
}

$('#employeeSearch').keydown(function(){
	performSearch();
});

$('#employeeSearch').keyup(function(){
	performSearch();
});

$.datetimepicker.setLocale('en');

$('.dateTimePicker').datetimepicker({
	yearOffset:-30,
	lang:'en',
	timepicker:false,
	format:'Y-m-d',
	formatDate:'Y/m/d',
});
</script>        