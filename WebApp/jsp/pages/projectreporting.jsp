<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@include file="includes/header.jsp" %>
<c:set var="userDetailsMap" value="${applicationScope.applicationConfiguraion.userMap}"/>

<style>
.dataTables_filter,.dataTables_length{
	display: none;
}

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
    </div>
    <!-- END CONTENT FRAME TOP -->
    <!-- body-->
    <div class="row">
	<div class="col-md-12" style="min-height: 700px;">
		<div class="row" id="assignTableId"  style="margin-top:10px">
		<!-- START DEFAULT TABLE EXPORT -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                
                                	<div class="col-md-5" >
                                		<div class="col-md-12">
										<div class="input-group">
											<div class="input-group-addon">
												<span class="fa fa-search"></span>
											</div>
											<input type="text" class="form-control" id="projectSearchBoxId"
												placeholder="Search for a project.." />
											<div class="input-group-btn">
												<button class="btn btn-primary hide">Search</button>
											</div>
										</div>
									</div>
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
        							<div class="col-md-2" >
        								<c:set var="reportFormat" value="${param.format}"/>
        								<c:if test="${not empty param.format  && (param.format eq 'format1' || param.format eq 'default')}">
        									<button onclick="exportReport('${param.format}')" class="btn btn-danger pull-right" aria-expanded="true"><i class="fa fa-bars"></i> Export Data</button>
        								</c:if>
        								<c:if test="${empty param.format || (param.format ne 'format1' && param.format ne 'default')}">
        									<button onclick="exportReport('default')" class="btn btn-danger  pull-right" aria-expanded="true"><i class="fa fa-bars"></i> Export Data</button>
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
        responsive: true,
        "lengthMenu": [[50, 100, 200, -1], [50, 100, 200, "All"]],
        "ordering": true,
        "fnDrawCallback": function( oSettings ) {
            	initializePopOver();
              }
        }).on( 'draw', function () {
        	initializePopOver();
        } );;
        
		       
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

function taskActActualTimeSubmit(taskId, activityId){
	var hours=$(".taskActHHTimeEdit"+activityId).val();
	var mins=$(".taskActMMTimeEdit"+activityId).val();
	
	taskActActualTimeSubmitAction(taskId, activityId, hours, mins, true)
}


function taskActActualTimeSubmitPop(taskId, activityId, taskKey, thisVar){
	var hours= $(thisVar).closest("div").find("input.taskActHHTimeEdit"+activityId).val();
	var mins= $(thisVar).closest("div").find("input.taskActMMTimeEdit"+activityId).val();
	if (taskActActualTimeSubmitAction(taskId, activityId, hours, mins, false) == true) {
		var newDuration = (parseInt(hours) * 60) +parseInt(mins);
		
		var originalActTime = $("input.taskActOriginalTime"+activityId).val();
		var originalTaskTime =  $("input.taskOriginalTime"+taskKey).val();
		
		var newTaskDuration = (newDuration - parseInt(originalActTime)) + parseInt(originalTaskTime);
		var newTaskHr = parseInt(newTaskDuration/60);
		var newTaskMins = (newTaskDuration%60);
		
		$("input.taskOriginalTime"+taskKey).val(newTaskDuration);
		$("input.taskActOriginalTime"+activityId).val(newDuration);
		
		$("." + taskKey +"taskActTaskTimeHrs").html(newTaskHr + ":" + (newTaskMins < 10 ? "0"+ newTaskMins : newTaskMins));
	}
}

var taskActActualTimeSubmitAction = function(taskId, activityId, hours, mins, isHide) {
	
	$(".taskActHHTimeEdit"+activityId).removeClass("error");
	$(".taskActMMTimeEdit"+activityId).removeClass("error");
	
	var error = false;
	if (hours == "" || !$.isNumeric(hours)) {
		$(".taskActHHTimeEdit"+activityId).addClass("error");
		error = true;
	}
	
	if (mins == "" || !$.isNumeric(mins) || parseInt(mins) >= 60) {
		$(".taskActMMTimeEdit"+activityId).addClass("error");
		error = true;
	}
	
	if(error){
		return false;
	}
	
	var newDuration = (parseInt(hours) * 60) +parseInt(mins);
	var startTime ="";
	var endTime ="";
	doAjaxRequest("POST", "${applicationHome}/adjustTaskActivityTime", {"activityId":activityId,"taskId":taskId,"newDuration":newDuration,"startTime":startTime,"endTime":endTime},  function(response) {
		$("."+activityId+"taskTimeEditLinkHrs").html(hours+":"+(mins < 10 ? "0"+ mins : mins));
		if (isHide) {
			hideTaskActActualTimeEdit(activityId);
		}
	}, function(e) {
	});
	
	return true;
}

$(document).ready(function(){
	
});
function initializePopOver() {
	$("[data-toggle=popover]").each(function(i, obj) {
		$(this).popover({
		  html: true,
		  content: function() {
		    var id = $(this).attr('id');
		    return $('#popover-' + id).html();
		  }
		});
	});
}

function initPopOver(thisVar){
	$(thisVar).popover({
		  html: true,
		  content: function() {
		    var id = $(thisVar).attr('id');
		    return $('#popover-' + id).html();
		  }
		});
}

$("#projectSearchBoxId").keydown(function(e){
	$("input[type='search']").val($("#projectSearchBoxId").val());
	$("input[type='search']").trigger(e);
});

$("#projectSearchBoxId").keyup(function(e){
	$("input[type='search']").val($("#projectSearchBoxId").val());
	$("input[type='search']").trigger(e);
});


</script>