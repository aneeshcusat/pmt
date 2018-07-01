//unbilled task start
$("#taskType").on("change", function(){
	$("#taskCreate").show();
	if($("#taskType").val() == "") {
		$("#taskCreate").hide();
	}
	
});

$('#startDateRange').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm',useCurrent: false,defaultDate:getTodayDate(new Date()) + " 9:00"}).on('dp.change', function(e) {
   adjustStartNBTimeChanged();
});;
$('#completionDateRange').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm',useCurrent: false,defaultDate:getTodayDate(new Date()) + " 17:00"});

var adjustStartNBTimeChanged=function(){
	var startTime = $('#startDateRange').val();
    var newCompletionDate = getTodayDate(new Date(startTime)) + " 17:00";
    $('#completionDateRange').val(newCompletionDate);
}

var validateStartAndEndUBTtime = function(){
	var startDate = $('#startDateRange').val();
	var endDate = $('#completionDateRange').val();
	$('#startDateRange').removeClass("error");
	$('#completionDateRange').removeClass("error");
	if (startDate != "" && endDate != "") {
		if (new Date(startDate) < new Date(endDate)) {
			var diffTime = (new Date(endDate) - new Date(startDate)) /(60*1000);
			fillNBTaskCompletionTime(diffTime);
			return true;
		}
	}
	$('#startDateRange').addClass("error");
	$('#completionDateRange').addClass("error");
	
	$(".nonBillableTaskCreateText").html("Create");
	return false;
}

function reloadNBTime() {
	 var completedTime = getTodayDateTime(new Date());
	 validateStartAndEndUBTtime();
}

window.setInterval(reloadNBTime, 1000);

var fillNBTaskCompletionTime =function(diffTime){
	var timeMsg = "";
	if (diffTime > 59) {
		var diffTimeHrs = diffTime/60;
		var diffTimeMins = diffTime%60;
		timeMsg = "More than 8 hours of";//parseInt(diffTimeHrs) + " Hrs " + parseInt(diffTimeMins) + " Mins";
		//$("#completedTimeHrs").html(timeMsg);
	} else {
		//$("#completedTimeHrs").html(Math.round(diffTime) + " Mins");
	}
	$("#taskCreate").parent().removeClass("colorRed");
	if (diffTime > 480){
		$(".nonBillableTaskCreateText").parent().addClass("colorRed");
		$(".nonBillableTaskCreateText").html("Confirm & Create " + timeMsg + " " + $("#taskType").val());
	} else {
		$(".nonBillableTaskCreateText").html("Create");
	}
}

var createUnbillableTask = function(){
	var endDate = "";
	var startDate = "";
	$("#userId").removeClass("error");
	if ($("#taskType").val() != "") {
		startDate = $("#startDateRange").val();
		endDate = $("#completionDateRange").val();
		if (!validateStartAndEndUBTtime()){
			return;
		}
	}
    
	if ($("#userId").val() == "" || $("#taskType").val() == "") {
		$("#userId").addClass("error");
		return;
	}
    
	var taskType = $("#taskType").val();
	var taskActCategory = "";
	
	if (taskType == "LEAVE"){
		taskActCategory = "Leave";
		
	} else if (taskType == "MEETING"){
		taskActCategory = "Meeting";
	} else {
		taskActCategory = taskType;
		taskType = "OTHER";
	}
	
	var isSkipWeekEnds = $("#skipWeekEnds").prop("checked") == true;
	
	var dataString = {skipWeekEnd:isSkipWeekEnds,userId:$("#userId").val(),type:taskType,taskActCategory:taskActCategory,startDate:startDate,endDate:endDate,comments:$("#taskStartComments").val()};
	doAjaxRequest("POST", "/bops/dashboard//createNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
			refreshCalendar();
        } else {
        	return false;
        }
       
    }, function(e) {
    });
}

var clearUnbillableFormForCreate = function(currentUserId) {
	$("#userId").val(currentUserId);
	$("#taskType").prop("selectedIndex",0);
	$("#startDateRange").val(getTodayDate(new Date()) + " 9:00");
	$("#completionDateRange").val(getTodayDate(new Date()) + " 17:00");
	$("#skipWeekEnds").prop("checked", true);
	$("#taskStartComments").val("");
}


var deleteTaskActivityAjax = function(activityId) {
	doAjaxRequest("POST", "/bops/dashboard//deleteTaskActivity", {activityId:activityId},  function() {
		getAssignJsonData();
		$(".mb-control-close").click();
	}, function(e) {
	});
}

function deleteTaskActivity(activityId, taskName){
	$(".msgConfirmText").html("Delete task activity");
	$(".msgConfirmText1").html(taskName);
	$("#confirmYesId").prop("href","javascript:deleteTaskActivityAjax('"+activityId+"')");
}


//unbilled task end