<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
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

.well {
    margin-top:-20px;
    background-color:#007FBD;
    border:2px solid #0077B2;
    text-align:center;
    cursor:pointer;
    font-size: 25px;
    padding: 15px;
    border-radius: 0px !important;
}

.well:hover {
    margin-top:-20px;
    background-color:#0077B2;
    border:2px solid #0077B2;
    text-align:center;
    cursor:pointer;
    font-size: 25px;
    padding: 15px;
    border-radius: 0px !important;
    border-bottom : 2px solid rgba(97, 203, 255, 0.65);
}

.bg_blur
{
    background-image:url('http://data2.whicdn.com/images/139218968/large.jpg');
    height: 300px;
    background-size: cover;
}

.follow_btn {
    text-decoration: none;
    position: absolute;
    left: 35%;
    top: 42.5%;
    width: 35%;
    height: 15%;
    background-color: #007FBE;
    padding: 10px;
    padding-top: 6px;
    color: #fff;
    text-align: center;
    font-size: 20px;
    border: 4px solid #007FBE;
}

.follow_btn:hover {
    text-decoration: none;
    position: absolute;
    left: 35%;
    top: 42.5%;
    width: 35%;
    height: 15%;
    background-color: #007FBE;
    padding: 10px;
    padding-top: 6px;
    color: #fff;
    text-align: center;
    font-size: 20px;
    border: 4px solid rgba(255, 255, 255, 0.8);
}

.header{
    color : #808080;
    margin-left:12%;
    margin-top:23px;
}

.picture{
    height:150px;
    width:150px;
    position:absolute;
    top: 41px;
    left:-70px;
}

.picture_mob{
    position: absolute;
    width: 35%;
    left: 35%;
    bottom: 70%;
}

.btn-style{
    color: #fff;
    background-color: #007FBE;
    border-color: #adadad;
    width: 33.3%;
}

.btn-style:hover {
    color: #333;
    background-color: #3D5DE0;
    border-color: #adadad;
    width: 33.3%;
   
}


</style>
  <div class="content-frame">                                    
       <!-- START CONTENT FRAME TOP -->
       <div class="content-frame-top">                        
           <div class="page-title">                    
               <h2><span class="fa fa-comments"></span> Profile</h2>
           </div>                                                    
       </div>
   <div class="container" style="margin-top: 20px; margin-bottom: 20px;">
	<div class="row panel">
        <div class="col-md-8  col-xs-10">
               <img src="${applicationHome}/image/${userProile.id}" class="img-thumbnail picture hidden-xs" alt="${userProile.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
           <div class="header">
                <h2>${userProile.firstName} ${userProile.lastName}</h2><span>
                 <a data-toggle="modal" class="profile-control-left" data-target="#registerusermodal" onclick="javascript:loadUser('${userProile.id}')">
	     			<span class="fa fa-edit fa-2x" style="color: blue"></span>
	      		</a> 			
	     		</span>
                 <table class="table table-user-information">
                    <tbody>
                      <tr>
                        <td>Designation:</td>
                        <td>${userProile.designation}</td>
                      </tr>
                       <tr>
                        <td>Gender</td>
                        <td>${userProile.gender}</td>
                      </tr>
                      <tr>
                        <td>Mobile Number</td>
                        <td>${userProile.mobileNumber}</td>
                      </tr>
                      <tr>
                        <td>Email</td>
                        <td>${userProile.userId}</td>
                      </tr>
                      <tr>
                        <td>Reporting Manager</td>
                        <td>${userProile.reportertingManager.firstName}</td>
                      </tr>
                    </tbody>
                  </table>
           </div>
        
        </div>
           <div  class="col-md-2  col-xs-2">
             <%--   <img src='${assets}/images/empofmonth.png'/> --%>
           </div>
    </div>   
    
	<div class="row nav">    
        <div class="col-md-1"></div>
        <div class="col-md-11 col-xs-12" style="margin: 0px;padding: 0px;">
            <div class="col-md-4 col-xs-4 well"><i class="fa fa-star fa-lg" style="color: gold;"></i> 0</div>
            <div class="col-md-4 col-xs-4 well"><i class="fa fa-heart-o fa-lg" style="color: red;"></i> 0</div>
            <div class="col-md-4 col-xs-4 well"><i class="fa fa-thumbs-o-up fa-lg" style="color: blue;"></i> 0</div>
        </div>
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
				<h4 class="modal-title" id="myModalLabel">Update My details</h4>
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

<%@include file="includes/footer.jsp" %>    
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js"></script>
<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js"></script> 

<script>
jQuery.validator.addMethod("validEmail", function(value, element) {
	  return this.optional( element ) || /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test( value );
}, 'Please enter a valid email address.');

var jvalidate = $("#createUserFormId").validate({
	 ignore: ".ignorevalidation",
    rules: {                                            
   	 email: {
        required: true,
        validEmail: true
     },
     firstName: {
         required: true
     },
     dateOfBirth: {
         required: false
 	 },
	team: {
	       required: false
	},userGroupId: {
	       required: true
	},
	role: {
	       required: true
	},
	file: {
	       required: false
	},
	range: {
	       required: false
	 }   
   }
});

function doAjaxUpdateUserForm(){
	var imageData = $('.image-editor').cropit('export');
	$('#filePhoto').val(imageData);
	famstacklog(imageData);
	$('#createUserFormId').prop("action", "${applicationHome}/updateEmployee");
    $('#createUserFormId').submit();
}

$('#createUserFormId').ajaxForm(function(response) { 
	famstacklog(response);
	var responseJson = JSON.parse(response);
    if (responseJson.status){
        window.location.reload(true);
    }
}); 

function loadUser(userId) {
	$.ajax({
        type : "GET",
        contentType : "application/json",
        url : "${applicationHome}/editEmployee",
        data: "userId="+userId,
        timeout : 1000,
        success : function(data) {
            famstacklog("SUCCESS: ", data);
            processUserResponseData(data);
            $("#createOrUpdateEmployeeId span").html("Update");
            $("#createOrUpdateEmployeeId").prop("href","javascript:doAjaxUpdateUserForm()");
        },
        error : function(e) {
            famstacklog("ERROR: ", e);
        },
        done : function(e) {
            famstacklog("DONE");
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
	$('#userGroupId').val(response.userGroupId);
	$('#designation').val(response.designation);
	
	$('#role').selectpicker('refresh');
	$('#reportingManger').selectpicker('refresh');
	$('#qualification').selectpicker('refresh');
	$('#team').selectpicker('refresh');
	$('#userGroupId').selectpicker('refresh');
	$('#designation').selectpicker('refresh');
	
	$('#temporaryEmployee').attr("checked", response.temporaryEmployee);
	$('#empCode').val(response.empCode);
}


$(function() {
    $('.image-editor').cropit();
  });

</script> 