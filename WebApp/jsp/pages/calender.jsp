<%@include file="includes/header.jsp" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="currentUser" value="${applicationScope.applicationConfiguraion.currentUser}"/>
 <ul class="breadcrumb">
     <li><a href="${applicationHome}/index">Home</a></li>  
     <li class="active">Calender</li>
 </ul>
<style>
.disabled .fc-day-content {
    background-color: #123959;
    color: #FFFFFF;
    cursor: default;
}
#unbillableTaskCreationModal .modal-dialog {
	width: 50%;
}

.autocomplete-suggestions { border: 1px solid #999; background: #FFF; overflow: auto; }
.autocomplete-suggestion { padding: 2px 5px; white-space: nowrap; overflow: hidden; }
.autocomplete-selected { background: #F0F0F0; }
.autocomplete-suggestions strong { font-weight: normal; color: #3399FF; }
.autocomplete-group { padding: 2px 5px; }
.autocomplete-group strong { display: block; border-bottom: 1px solid #000; }
</style>
<!-- START CONTENT FRAME -->
<div class="content-frame">            
    <!-- START CONTENT FRAME TOP -->
    <div class="content-frame-top">                        
        <div class="page-title">                    
            <h2><span class="fa fa-calendar"></span> Calendar</h2>
        </div>  
        <div class="pull-right">
        <a data-toggle="modal" data-target="#unbillableTaskCreationModal" onclick="clearUnbillableFormForCreate()"
									class="btn btn-success btn-block"> <span class="fa fa-plus"></span>
									Record Non-billable Time.
		</a>
        </div>                                                                                
    </div>
    <!-- END CONTENT FRAME TOP -->
    
    <!-- START CONTENT FRAME BODY -->
    <div class=" padding-bottom-0">
        
        <div class="row">
            <div class="col-md-12">
                <div id="alert_holder"></div>
                <div class="calendar">                                
                    <div id="calendar"></div>                            
                </div>
            </div>
        </div>
        
    </div>                    
    <!-- END CONTENT FRAME BODY -->
    
</div>               
<!-- task completion modal start -->
<div class="modal fade" id="unbillableTaskCreationModal" tabindex="-1" data-backdrop="static"
	role="dialog" aria-labelledby=""createUnbillableModal"" aria-hidden="true">
	<form:form id="unbillableTaskCreationModal" action="unbillableTaskCreationModal" method="POST"
		role="form" class="form-horizontal">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Create Non-billable time</h4>
				</div>
				<div class="modal-body">
					<%@include file="fagments/unbillableTaskCreationModal.jspf"%>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary createUnbillableCancel"
						data-dismiss="modal">Cancel</button>
					<button type="button" id="taskCreate" onclick="createUnbillableTask()"
						class="btn btn-primary" style="display: none">
						<span >Create</span>
					</button>
				</div>
			</div>
		</div>
	</form:form>
</div>
<!-- project create modal end -->  

<!-- END CONTENT FRAME -->                                
 <%@include file="includes/footer.jsp" %>            
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js"></script>
<script>

$("#taskType").on("change", function(){
	$("#taskCreate").show();
	if($("#taskType").val() == "LEAVE") {
		$(".dateDuration").show();
		$(".dateRange").hide();
	} else if ($("#taskType").val() == "MEETING"){
		$(".dateRange").show();
		$(".dateDuration").hide();
	} else {
		$(".dateRange").show();
		$(".dateDuration").show();
		$("#taskCreate").hide();
	}
	
});

$.datetimepicker.setLocale('en');

$('.datePicker').datetimepicker({
	lang:'en',
	timepicker:false,
	format:'Y/m/d',
	formatDate:'Y/m/d',
});

$('.dateTimePicker').datetimepicker({onGenerate:function( ct ){
	$(this).find('.xdsoft_date.xdsoft_weekend')
	.addClass('xdsoft_disabled');
	},
	allowTimes:['08:00','09:00','10:00','11:00','12:00','14:00','15:00','16:00','17:00','18:00','19:00','20:00'],
});
var createUnbillableTask = function(){
	
	var endDate = "";
	var startDate = "";
	$("#startDateRange").css("border", "1px solid #D5D5D5");
	$("#completionDateRange").css("border", "1px solid #D5D5D5");
	
	if($("#taskType").val() == "LEAVE") {
		startDate = $("#startDate").val();
		endDate = $("#duration").val();
	} else if ($("#taskType").val() == "MEETING") {
		startDate = $("#startDateRange").val();
		endDate = $("#completionDateRange").val();
		
		if (new Date(startDate) >= new Date(endDate)){
			$("#startDateRange").css("border", "1px solid red");
			$("#completionDateRange").css("border", "1px solid red");
			return;
		}
	}
    
	var dataString = {userId:$("#userId").val(),type:$("#taskType").val(),startDate:startDate,endDate:endDate,comments:$("#taskStartComments").val()};
	doAjaxRequest("POST", "${applicationHome}/createNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
        } else {
        	return false;
        }
       
    }, function(e) {
    });
}

var clearUnbillableFormForCreate = function() {
	$("#userId").prop("selectedIndex",0);
	$("#taskType").prop("selectedIndex",0);
	$("#duration").prop("selectedIndex",0);
	$("#startDate").val("");
	$("#startDateRange").val("");
	$("#completionDateRange").val("");
	$("#taskStartComments").val("");
}
</script>
