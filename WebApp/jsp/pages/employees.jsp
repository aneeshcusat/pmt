<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="includes/header.jsp" %>
<c:set var="allSortedUsers" value="${applicationScope.applicationConfiguraion.allSortedUsers}"/>
<c:set var="employeeAccess" value="false"/>
<c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userGroupId == '1012'}">
	<c:set var="employeeAccess" value="true"/> 
</c:if>           
 <!-- START BREADCRUMB -->
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Employees</li>
 </ul>
 <!-- END BREADCRUMB -->  
<style>
.dataTables_filter,.dataTables_length{
	display: none;
}
#DataTables_Table_0_wrapper .dt-buttons{
	display: none;
}
.blueColor{
	color:blue !important;
}
    @media screen and (min-width: 800px) {
    #registerusermodal .modal-dialog  {width:75%;}
    .cropit-preview {
        background-color: #f8f8f8;
        background-size: cover;
        border: 1px solid #ccc;
        border-radius: 3px;
        margin-top: 7px;
        width: 150px;
        height: 150px;
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

.deletedUser{
	background-color: red;
	display: none;
}
</style>         
<!-- PAGE TITLE -->
<div class="page-title">                    
    <h2><span class="fa fa-users"></span> Employees <small>
    
     <c:if test="${currentUser.userGroupId != '1012' && currentUser.userRole != 'SUPERADMIN'}">
    	${fn:length(userMap)} 
	</c:if>
	
	 <c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN'}">
    	${fn:length(employeeMap)} 
	</c:if>    
    
    contacts</small></h2>
</div>
<!-- END PAGE TITLE -->                

<!-- PAGE CONTENT WRAPPER -->
<div class="page-content-wrap"  style="min-height: 500px">
    
    <div class="row">
        <div class="col-md-12">
            
            <div class="panel panel-default">
                <div class="panel-body">
                    <p>Use search to find contacts. You can search by: name, address, phone. Or use the advanced search.</p>
                    <form class="form-horizontal">
                        <div class="form-group">
                            <div class="col-md-4">
                                <div class="input-group">
                                    <div class="input-group-addon">
                                        <span class="fa fa-search"></span>
                                    </div>
                                    <input type="text" class="form-control" placeholder="Who are you looking for?" id="employeeSearch"/>
                                    <div class="input-group-btn hide">
                                        <button class="btn btn-primary">Search</button>
                                    </div>
                                </div>
                            </div>
                           
                            <div class="col-md-2">
                            <c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN'}">
                            <a href="javascript:showGridEmployeeDetails();" id="employeesDetailsGridLink"  style="margin-right: 10px;" class="blueColor"><span class="fa fa-th-large fa-3x"></span></a>
                            <a href="javascript:showListEmployeeDetails();" id="employeesDetailsListLink"><span class="fa fa-tasks fa-3x"></span></a>
                            
                             <a href="javascript:downloadListEmployeeDetails();" id="downloadEmployeesDetailsListLink" class="hide" style="float:right"><span class="fa fa-download fa-3x"></span></a>
                             
							</c:if>
                             <c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN'}">
							<span style="float: left; margin-right: 5px">Deleted<input type="checkbox" class="showDeletedCheckBox" value="show Deleted" style="margin-left: 10px;"/></span>
							</c:if>
                            </div>
                            <div class="col-md-3">
                       		<c:if test="${currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN' || currentUser.userGroupId == '1012'}">
                            	<a id="userSiteActivityLink" href='javascript:$("#userSiteActivityLink").hide();$("#userSiteActivityDiv").show();'>Download Activity</a>
								<span id="userSiteActivityDiv" style="display: none">                            
	                           		<span id="exportDateRange" class="dtrange">                                            
	           							<span></span><b class="caret"></b>
	       							</span>
	       							<input type="hidden" id="daterangeText" value="hello" /> 
	       							<a href="javascript:exportReport('useractivity');" class="btn btn-danger" aria-expanded="true"><i class="fa fa-bars"></i>Download Activity</a>
                          		</span>
                           </c:if>
                            </div>
                            
                             <div class="col-md-3">
                             <c:if test="${employeeAccess == 'true'}">
                           		 <a data-toggle="modal" data-target="#registerusermodal" class="btn btn-success btn-block" onclick="createEmployeeDetails()">
                               <span class="fa fa-plus"></span> Register a new Employee</a>
                             </c:if>
                            </div>
                        </div>
                    </form>                                    
                </div>
            </div>
            
        </div>
    </div>
    
    <div class="row" id="employeesDetailsGridDiv">
   		<%@include file="response/employeeList.jsp" %>
   </div>
   <c:if test="${currentUser.userGroupId == '1012' || currentUser.userRole == 'SUPERADMIN' || currentUser.userRole == 'ADMIN'}">
    <div class="row hide" id="employeesDetailsListDiv">
   	</div>
   </c:if>       
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
							<a id="createOrUpdateEmployeeId" href="#" class="btn btn-info"><span id="userButton">Save</span></a>
						</div>
					</div>
				</div>
				
				</form:form>
			</div>
             <!-- END PAGE CONTENT WRAPPER -->                                                 
<%@include file="includes/footer.jsp" %>
<script type='text/javascript' src='${js}/plugins/jquery-validation/jquery.validate.js'></script>   
<script type="text/javascript" src="${js}/plugins/bootstrap/bootstrap-select.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/tagsinput/jquery.tagsinput.min.js?v=${fsVersionNumber}"></script>
<script type="text/javascript" src="${js}/plugins/fileinput/fileinput.min.js?v=${fsVersionNumber}"></script> 
<script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min_v1.js?v=${fsVersionNumber}"></script> 
<script type="text/javascript" src="${js}/plugins/datatables/dataTables.buttons.min.js?v=${fsVersionNumber}"></script>   
<script type="text/javascript" src="${js}/plugins/datatables/buttons.html5.min.js?v=${fsVersionNumber}"></script>   
<script type='text/javascript' src='${js}/plugins/lazyload/jquery.lazy.min.js'></script>   

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
 	 userGroupId: {
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

$('#createUserFormId').ajaxForm(function(response) { 
	famstacklog(response);
	var responseJson = JSON.parse(response);
	$(".email-error").addClass("hide");
	$("#email").removeClass("error");
    if (responseJson.status){
        window.location.reload(true);
    } else {
    	var errorCode = responseJson.errorCode;
    	
    	if (errorCode == 'Duplicate') {
    		$(".email-error").html("Email id is already registered");
    		$(".email-error").removeClass("hide");
    		$("#email").removeClass("valid");
    		$("#email").addClass("error");
    	}
    }
}); 

function createEmployeeDetails(){
	clearUserData();
	$("#createOrUpdateEmployeeId span").html("Save");
	$("#createOrUpdateEmployeeId").prop("href","javascript:doAjaxCreateUserForm()");
}

function doAjaxUpdateUserForm(){
	var imageData = $('.image-editor').cropit('export');
	$('#filePhoto').val(imageData);
	$('#createUserFormId').prop("action", "updateEmployee");
    $('#createUserFormId').submit();
}

function doAjaxCreateUserForm(){
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

function deleteUser(userId, userName){
	$(".msgConfirmText").html("Delete user");
	$(".msgConfirmText1").html(userName);
	$("#confirmYesId").prop("href","javascript:doAjaxDeleteUser('"+userId+"')");
}

function undoDeleteUser(userId, emailId, userName){
	$(".msgConfirmText").html("Undo deleted user");
	$(".msgConfirmText1").html(userName);
	$("#confirmYesId").prop("href","javascript:doAjaxUndoDeleteUser('"+userId+"','"+emailId+"')");
}

function doAjaxUndoDeleteUser(userId, emailId) {
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/unblockUser", {"userId":userId}, function(data) {
		  $(".userDetails"+userId).removeClass("deletedUser");
          $(".message-box").removeClass("open");
	}, function(e) {
	   famstacklog("ERROR: ", e);
	   famstackalert(e);
	}, false);
}

function doAjaxDeleteUser(userId) {
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "${applicationHome}/deleteEmployee",
        data: "userId="+userId,
        timeout : 1000,
        success : function(data) {
            famstacklog("SUCCESS: ", data);
            var responseJson = JSON.parse(data);
            if (responseJson.status){
            	$(".userDetails"+userId).addClass("deletedUser");
                $(".message-box").removeClass("open");
            }
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
	$('#userGroupId').val("");
	$('#designation').val("");
	$(".genderGroup label").removeClass("active");
	$('#role').prop('selectedIndex', 0);
	$('#qualification').prop('selectedIndex', 0);
	$('#group').prop('selectedIndex', 0);
	$('#designation').prop('selectedIndex', 0);
	$('#reportingManger').val("");
	$('#deptLeadEmailId').val("");
	$('#lobHeadEmailId').val("");
	$('#location').val("");
	$('#country').val("");
	$('#division').val("");
	$('#department').val("");
	$('#subDepartment').val("");
	$('#band').val("");
	$('#grade').val("");
	$('#empType').val("");
	$('#dateOfJoin').val("");
	
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
	$('#role').selectpicker('refresh');
	$('#reportingManger').selectpicker('refresh');
	$('#qualification').selectpicker('refresh');
	$('#userGroupId').selectpicker('refresh');
	$('#team').selectpicker('refresh');
	$('#designation').selectpicker('refresh');
	
}

$(function() {
    $('.image-editor').cropit();
  });

function performSearch(e){
	var serarchText = $('#employeeSearch').val();
	
	if ($("#employeesDetailsListDiv").length > 0 && (!$("#employeesDetailsListDiv").hasClass("hide"))) {
		$("input[type='search']").val(serarchText);
		$("input[type='search']").trigger(e);
		return;
	}
	famstacklog(serarchText);
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

$('#employeeSearch').keydown(function(e){
	performSearch(e);
});

$('#employeeSearch').keyup(function(e){
	performSearch(e);
});

$.datetimepicker.setLocale('en');

$('.dateTimePicker').datetimepicker({
	yearOffset:-30,
	lang:'en',
	timepicker:false,
	format:'Y-m-d',
	formatDate:'Y/m/d',
});

$('.DOJdateTimePicker').datetimepicker({
	lang:'en',
	timepicker:false,
	format:'Y-m-d',
	formatDate:'Y/m/d',
});

function showGridEmployeeDetails() {
	$("#employeesDetailsGridDiv").removeClass("hide");
	$("#employeesDetailsListDiv").addClass("hide");
	$("#downloadEmployeesDetailsListLink").addClass("hide");
	$("#employeesDetailsGridLink").addClass("blueColor");
	$("#employeesDetailsListLink").removeClass("blueColor");
}

function downloadListEmployeeDetails(){
	$(".dt-button.buttons-csv").click();
}

function reloadEmployeeDetails(){
	var pageName = "employeeGridPage";
	if ($("#employeesDetailsListDiv").hasClass("hide")){
		pageName = "employeeGridPage";
	} else if ($("#employeesDetailsGridDiv").hasClass("hide")){
		pageName = "employeeListPage";
	}
	
	doAjaxRequest("GET", "${applicationHome}/" +pageName, {}, function(data) {

		if ($("#employeesDetailsListDiv").hasClass("hide")){
			$("#employeesDetailsGridDiv").html(data);
		} else if ($("#employeesDetailsGridDiv").hasClass("hide")){
			$("#employeesDetailsListDiv").html(data);
			  $('.employeeDataTable').DataTable({ 
				  	dom: 'Bfrtip',
			        buttons: [
			            	 'csv'
			        ],
			    	responsive: false,
			        "pageLength": -1,
			        "ordering": false,
			        "scrollX": true
			    });
		}
		
    }, function(e) {
        famstacklog("ERROR: ", e);
    });
	
}

function showListEmployeeDetails() {
	$("#employeesDetailsGridDiv").addClass("hide");
	$("#employeesDetailsGridLink").removeClass("blueColor");
	$("#employeesDetailsListLink").addClass("blueColor");
	$("#employeesDetailsListDiv").removeClass("hide");
	$("#downloadEmployeesDetailsListLink").removeClass("hide");
	reloadEmployeeDetails();
}

if($("#exportDateRange").length > 0){   
    $("#exportDateRange").daterangepicker({                    
        ranges: filterDateMap,
        opens: 'left',
        buttonClasses: ['btn btn-default'],
        applyClass: 'btn-small btn-primary',
        cancelClass: 'btn-small',
        format: 'MM.DD.YYYY',
        separator: ' to ',
        startDate:moment().subtract(6, 'days'),
        endDate:  moment()         
      },function(start, end) {
    	  $("#daterangeText").val(start.format('MM/DD/YYYY') + ' - ' + end.format('MM/DD/YYYY'));
    	  $("#exportDateRange span").html($("#daterangeText").val());
    });
    $("#daterangeText").val(moment().subtract(6, 'days').format('MM/DD/YYYY') + ' - ' + moment().format('MM/DD/YYYY'));
    $("#exportDateRange span").html($("#daterangeText").val());
}

$(".showDeletedCheckBox").on("change",function(e){
	if($(".showDeletedCheckBox").is(':checked')){
		$(".deletedUser").addClass("contact-name");
		$(".deletedUser").show();
	} else {
		$(".deletedUser").removeClass("contact-name");
		$(".deletedUser").hide();
	}
	performSearch(e);
});

/* $("img[data-src]").each(function(index) {
    $(this).load(function() {
        // code to run after loading
    });
    $(this).attr("src", $(this).attr("data-src"));
});


 */
 $(document).ready(function(){
	 $('.profile-image img').Lazy();
 });
 </script>        