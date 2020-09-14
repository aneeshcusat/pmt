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
.deletedbackground{
 <c:if test="${userProile.deleted}">
background-image: url(/bops/jsp/image/deleted.png);
    background-position: right;
    background-repeat: no-repeat;
    opacity: .8;
    background-size: 20%
 </c:if>
}

.table-user-information{
    color: #000;
    font-size: 12px;
    font-weight: bold;
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
	<div class="row panel deletedbackground">
        <div class="col-md-8  col-xs-10">
               <img src="${applicationHome}/image/${userProile.id}" class="img-thumbnail picture hidden-xs" alt="${userProile.firstName}" onerror="this.src='${assets}/images/users/no-image.jpg'"/>
           <div class="header">
                <span>
                <c:if test="${!userProile.deleted && false}">
                 <a data-toggle="modal" class="profile-control-left" data-target="#registerusermodal" onclick="javascript:loadUser('${userProile.id}')">
	     			<span class="fa fa-edit fa-2x" style="color: blue"></span>
	      		</a>
	      		</c:if> 			
	     		</span>
                 <table class="table table-user-information">
                    <tbody>
                     <c:if test="${not empty userProile.userId}">
                      <tr>
                        <td>Email</td>
                        <td>${userProile.userId}</td>
                      </tr>
                      </c:if>
                      <c:if test="${not empty userProile.qualification}">
                      <tr>
                        <td>Qualification</td>
                        <td>${userProile.qualification}</td>
                      </tr>
                      </c:if>
                    
                    
                     <c:if test="${not empty userProile.dob}">
                      <tr>
                        <td>Date of Birth</td>
                        <td>${userProile.dob}</td>
                      </tr>
                      </c:if>
                      
                       <c:if test="${not empty userProile.designation}">
                      <tr>
                        <td>Designation</td>
                        <td>${userProile.designation}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.mobileNumber}">
                      <tr>
                        <td>Mobile Number</td>
                        <td>${userProile.mobileNumber}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.empCode}">
                      <tr>
                        <td>Employee Code</td>
                        <td>${userProile.empCode}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.userId}">
                      <tr>
                        <td>Email</td>
                        <td>${userProile.userId}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.division}">
                      <tr>
                        <td>Division</td>
                        <td>${userProile.division}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.country}">
                      <tr>
                        <td>Country</td>
                        <td>${userProile.country}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.department}">
                      <tr>
                        <td>Department</td>
                        <td>${userProile.department}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.subDepartment}">
                      <tr>
                        <td>Sub Department</td>
                        <td>${userProile.subDepartment}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.location}">
                      <tr>
                        <td>Location</td>
                        <td>${userProile.location}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.userId}">
                      <tr>
                        <td>Email</td>
                        <td>${userProile.userId}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.band}">
                      <tr>
                        <td>Band</td>
                        <td>${userProile.band}</td>
                      </tr>
                      </c:if>
                      
                      
                       <c:if test="${not empty userProile.grade}">
                      <tr>
                        <td>Grade</td>
                        <td>${userProile.grade}</td>
                      </tr>
                      </c:if>
                    
                     <c:if test="${not empty userProile.dateOfJoin}">
                      <tr>
                        <td>Date Of Join</td>
                        <td><fmt:formatDate value="${userProile.dateOfJoin}" pattern="yyyy-MM-dd" /></td>
                      </tr>
                      </c:if>
                      
                     
                      <tr>
                        <td>Exit Date</td>
                        <td>
                        <input type="hidden" id="userId" value="${userProile.id}"/>
                        <input value="<fmt:formatDate value="${userProile.exitDate}" pattern="yyyy-MM-dd" />" type="text" style="width: 130px;float: left;margin-right: 10px;" class="form-control DOJdateTimePicker" id="exitDate" name="exitDate"  autocomplete="off"/>
                         
                        	<button id="updateExitDate" onclick="doAjaxUpdateExitDate();" class="btn btn-danger"><c:if test="${userProile.deleted}">Update</c:if><c:if test="${!userProile.deleted}">Delete</c:if></button>
                       		<c:if test="${userProile.deleted}">
                       			<button id="undoDeleteUser" onclick="doAjaxUndoDeleteUser();" class="btn btn-success">Undo Delete User</button>
                       		</c:if>
                        </td>
                      </tr>
                      
                      
                       <c:if test="${not empty userProile.empType}">
                      <tr>
                        <td>Employee Type</td>
                        <td>${userProile.empType}</td>
                      </tr>
                      </c:if>
                      
                       <c:if test="${not empty userProile.reportertingManagerEmailId}">
                      <tr>
                        <td>Reporting Manager</td>
                        <td>${userProile.reportertingManagerEmailId}</td>
                      </tr>
                      </c:if>
                      
                       <c:if test="${not empty userProile.deptLeadEmailId}">
                      <tr>
                        <td>Department Lead</td>
                        <td>${userProile.deptLeadEmailId}</td>
                      </tr>
                      </c:if>
                    
                     <c:if test="${not empty userProile.lobHeadEmailId}">
                      <tr>
                        <td>LOB Head</td>
                        <td>${userProile.lobHeadEmailId}</td>
                      </tr>
                      </c:if>
                      
                      <c:if test="${not empty userProile.skills}">
                      <tr>
                        <td>Skills</td>
                        <td>${userProile.skills}</td>
                      </tr>
                      </c:if>
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
					<a id="createOrUpdateEmployeeId" href="#" class="btn btn-info"><span id="userButton">Save</span></a>
				</div>
			</div>
		</div>
		
		</form:form>
	</div>

<%@include file="includes/footer.jsp" %>    
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js?v=${fsVersionNumber}"></script> 

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

$('.DOJdateTimePicker').datetimepicker({
	lang:'en',
	timepicker:false,
	format:'Y-m-d',
	formatDate:'Y/m/d',
});

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
	$('#reportingManger').val(response.reportertingManagerEmailId);
	$('#deptLeadEmailId').val(response.deptLeadEmailId);
	$('#lobHeadEmailId').val(response.lobHeadEmailId);
	
	$('#qualification').val(response.qualification);
	$('#userGroupId').val(response.userGroupId);
	$('#designation').val(response.designation);
	
	$('#location').val(response.location);
	$('#country').val(response.country);
	$('#division').val(response.division);
	$('#department').val(response.department);
	$('#subDepartment').val(response.subDepartment);
	$('#band').val(response.band);
	$('#grade').val(response.grade);
	$('#empType').val(response.empType);
	
	$('#dateOfJoin').val(response.dateOfJoin);
	
	$('#role').selectpicker('refresh');
	$('#reportingManger').selectpicker('refresh');
	$('#deptLeadEmailId').selectpicker('refresh');
	$('#lobHeadEmailId').selectpicker('refresh');
	
	$('#location').selectpicker('refresh');
	$('#country').selectpicker('refresh');
	$('#division').selectpicker('refresh');
	$('#department').selectpicker('refresh');
	$('#subDepartment').selectpicker('refresh');
	$('#band').selectpicker('refresh');
	$('#grade').selectpicker('refresh');
	$('#empType').selectpicker('refresh');
	
	$('#qualification').selectpicker('refresh');
	$('#userGroupId').selectpicker('refresh');
	$('#designation').selectpicker('refresh');
	
	$('#empCode').val(response.empCode);
}


$(function() {
    $('.image-editor').cropit();
  });
  
function doAjaxUpdateExitDate() {
	var userId = $("#userId").val();
	var exitDate = $("#exitDate").val();
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/deleteEmployee", {"userId":userId, "exitDate":exitDate}, function(data) {
		 famstacklog("SUCCESS: ", data);
         var responseJson = JSON.parse(data);
         if (responseJson.status){
            window.location.reload(true);
         }
	}, function(e) {
	   famstacklog("ERROR: ", e);
	   famstackalert(e);
	}, false);
}


function doAjaxUndoDeleteUser() {
	var userId = $("#userId").val();
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/unblockUser", {"userId":userId}, function(data) {
		 window.location.reload(true);
	}, function(e) {
	   famstacklog("ERROR: ", e);
	   famstackalert(e);
	}, false);
}
</script> 