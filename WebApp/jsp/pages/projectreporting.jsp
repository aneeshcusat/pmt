<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>

<style>
.dt-buttons a{
    width: 65px;
    height: 34px;
    font-size: 13px;
    font-weight: bold;
    background-color: red;
}
.dt-buttons{
    float: right;
    margin-left: 25px;
}

.durationTxt{
	font-weight: bold;
    width: 20px;
    height: 20px;
    line-height: 18px;
    box-shadow: none;
    -webkit-appearance: none;
    border: 1px solid #D5D5D5;
    background: #F9F9F9;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    border-radius: 4px;
    color: #555;
    font-size: 8px;
    padding: 0px 2px 2px 5px;
}
</style>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Project Reporting</li>
 </ul>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-table"></span> Project Reporting - ${userGroupMap[currentUserGroupId].name}</h2>
        </div>  
        <div class="pull-right">
            <button class="btn btn-default content-frame-left-toggle"><span class="fa fa-bars"></span></button>
        </div>                                                                                
    </div>
    <!-- END CONTENT FRAME TOP -->
    <!-- body-->
    <div class="row">
	<div class="col-md-12" >
		<div class="row" id="assignTableId"  style="margin-top:10px">
		<!-- START DEFAULT TABLE EXPORT -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                
                                	<div class="col-md-6" >
                                 	</div>
                                 	<div class="col-md-2" >
					                 <span style="margin-top: 9px;margin-right:  10px;float:right"></>Select a date Range :  <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;</span>
					                 <input type="text" name="daterange" id="daterangeText" style="display: none" value="${dateRange}" /> 
					                </div>
					             	
					             	<div class="col-md-3" >
					             	 <span id="reportrange" class="dtrange">                                            
            							<span>${dateRange}</span><b class="caret"></b>
        							</span>
        								<input style="margin-left:10px" class="btn btn-default" type="button" onclick="getProjectReportingData('${param.format}');" value="Search"></input>
        							</div>
        							<div class="col-md-1" >
        								<c:set var="reportFormat" value="${param.format}"/>
        								<c:if test="${not empty param.format  && (param.format eq 'format1' || param.format eq 'default')}">
        									<button onclick="exportReport('${param.format}')" class="btn btn-danger" aria-expanded="true"><i class="fa fa-bars"></i> Export Data</button>
        								</c:if>
        								<c:if test="${empty param.format || (param.format ne 'format1' && param.format ne 'default')}">
        									<button onclick="exportReport('default')" class="btn btn-danger" aria-expanded="true"><i class="fa fa-bars"></i> Export Data</button>
        									<c:set var="reportFormat" value="default"/>
        								</c:if>
        							</div>
                                </div>
                                <div class="panel-body panel-body-table" id="reportingBodyDiv">
                                    
                                </div>
                            </div>
                            <!-- END DEFAULT TABLE EXPORT -->
		</div>
	</div>
</div>
    <!-- body end -->
    
</div>               
<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>
  <script type="text/javascript" src="${js}/plugins/datatables/jquery.dataTables.min_v1.js"></script> 
<script type="text/javascript" src="${js}/plugins/datatables/buttons.print.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/buttons.html5.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/dataTables.buttons.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/jszip.min.js"></script>    
<script type="text/javascript" src="${js}/plugins/datatables/pdfmake.min.js"></script>  
<script type="text/javascript" src="${js}/plugins/datatables/vfs_fonts.js"></script>    

<script type="text/javascript">
        
        
var getProjectReportingData = function(format){
	 loadAllProjectDetails($('#daterangeText').val(), format);
}
        
var loadAllProjectDetails = function(daterange, format) {
	
	var dataString = {"daterange" : daterange, "format":format};
	doAjaxRequest("GET", "${applicationHome}/projectreportingResponse", dataString, function(data) {
        $("#reportingBodyDiv").html(data);
        document.title = "Export_" + daterange;
        $('#projectsTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                 'copy', 'csv', 'print'
            ],
            "pageLength": 100
        });
        
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    });
}
$(function() {
    $('input[name="daterange"]').daterangepicker();
    loadAllProjectDetails('${dateRange}','${reportFormat}');
});


var showTaskActActualTimeEdit = function(taskActId) {
	$("."+taskActId+"taskTimeEditLink").hide();
	$("."+taskActId+"taskTimeEdit").show();	
	$("."+taskActId+"taskActTimeEdit").show();	
}

var hideTaskActActualTimeEdit = function(taskActId) {
	$("."+taskActId+"taskTimeEditLink").show();
	$("."+taskActId+"taskTimeEdit").hide();	
	$("."+taskActId+"taskActTimeEdit").hide();	
}

var taskActActualTimeSubmit = function(taskId, activityId) {
	var hours=$("#taskActHHTimeEdit"+activityId).val();
	var mins=$("#taskActMMTimeEdit"+activityId).val();
	
	
	$("#taskActHHTimeEdit"+taskId).removeClass("error");
	$("#taskActMMTimeEdit"+taskId).removeClass("error");
	
	var error = false;
	if (hours == "" || !$.isNumeric(hours)) {
		$("#taskActHHTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if (mins == "" || !$.isNumeric(mins) || parseInt(mins) >= 60) {
		$("#taskActMMTimeEdit"+taskId).addClass("error");
		error = true;
	}
	
	if(error){
		return;
	}
	
	var newDuration = (parseInt(hours) * 60) +parseInt(mins);
	var startTime ="";
	var endTime ="";
	doAjaxRequest("POST", "${applicationHome}/adjustTaskActivityTime", {"activityId":activityId,"taskId":taskId,"newDuration":newDuration,"startTime":startTime,"endTime":endTime},  function(response) {
		$("."+activityId+"taskTimeEditLinkHrs").html(hours+":"+mins);
		hideTaskActActualTimeEdit(activityId);
	}, function(e) {
	});
}

$(document).ready(function(){
	
});
</script>