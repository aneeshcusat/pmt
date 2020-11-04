var accountNameCodetMapping = [
 {name:"Agilent", value:"AGL"},
 {name:"Amazon", value:"AMZ"},
 {name:"Colgate", value:"CPL"},
 {name:"Dell", value:"DEL"},
 {name:"Facebook", value:"FCB"},
 {name:"Fuel Cycle", value:"FCL"},
 {name:"Google", value:"GOO"},
{name:"Grammarly", value:"GRY"},
 {name:"Hyundai Motor", value:"HMR"},
 {name:"IEEE", value:"IEE"},
 {name:"iHeart Media", value:"IHM"},
 {name:"Intel", value:"ITL"},
 {name:"Internal", value:""},
 {name:"Lenovo", value:"LEN"},
{name:"LS", value:"LS"},
{name:"RAI", value:"RAI"},
 {name:"Lululemon", value:"LUL"},
 {name:"MBRDI", value:"MBR"},
 {name:"Microsoft", value:"MSF"},
 {name:"Motorola", value:"MLA"},
 {name:"Mozilla", value:"MOZ"},
 {name:"OnePlus", value:"OPL"},
 {name:"Pepsi", value:"PEP"},
 {name:"PG & E", value:"PGE"},
 {name:"Roofoods Ltd", value:"RFL"},
 {name:"T-mobile", value:"TML"},
 {name:"Unilever", value:"ULR"},
 {name:"YouTube", value:"YTE"},
 {name:"3M", value:"3MM"},
 {name:"Mars", value:"MRS"},
 {name:"HDFC", value:"HFC"},
 {name:"Autodesk", value:"ADK"},
 {name:"Sazerac", value:"SZC"},
 {name:"NBF", value:"NBF"},
 {name:"Airtel", value:"ATL"},
 {name:"Hotel Champs", value:"HCP"}
]

var categoryCodeMapping = [
{name: "LEAVE", value: "101"},
{name: "Client onsite trips and visits", value: "102"},
{name: "Compliance Management", value: "103"},
{name: "Internal team meetings, conferences and offsites", value: "104"},
{name: "Administrative and management", value: "105"},
{name: "Holiday", value: "106"},
{name: "Knowledge development and training", value: "107"},
{name: "Internal product/solution development and support", value: "{division}{accountCode}210"},
{name: "Proposals", value: "{division}{accountCode}211"},
{name: "POC and Pilot Projects", value: "{division}{accountCode}212"},
{name: "Marketing Collateral and Campaigns", value: "{division}{accountCode}213"},
{name: "Additional support on projects post closure", value: "{division}{accountCode}213"}
]

$('input[name = "ubdivision"]').on("change", function(){
	setReferenceNo();
});
$(".ubaccountId").on("change", function(){
	setReferenceNo();
	changeFieldsOnTaskTypeChange($(".ubaccountId").val());
	$(".ubaccountIdDiv").show();
});
//unbilled task start
$("#taskType").on("change", function(){
	$("#taskCreate").show();
	if($("#taskType").val() == "") {
		$("#taskCreate").hide();
	}
	var taskTypeVal = $("#taskType").val();
	if (taskTypeVal != "" && taskTypeVal.startsWith("Internal")){
		$(".ubaccountId option[value != 'Internal']").hide();
		$(".ubaccountId").val("Internal");
		$('.ubaccountId').selectpicker('refresh');
	} else {
		$(".ubaccountId option").show();
		$(".ubaccountId").prop("selectedIndex",0);
		$('.ubaccountId').selectpicker('refresh');
	}
	
	$(".unbilledClientNameDiv").show();
	if(disableClientAdditionalFileds(taskTypeVal)) {
		$(".unbilledClientNameDiv").hide();
	}

	changeFieldsOnTaskTypeChange(taskTypeVal);
	
	setReferenceNo();
	checkFutureTimeCaptureRestriction($("#taskType").val());
});

function disableAdditionalFileds(taskTypeVal){
	if(taskTypeVal == 'LEAVE' 
		|| taskTypeVal == 'Leave' 
		|| taskTypeVal == 'Holiday' 
		|| taskTypeVal == 'LS' 
		|| taskTypeVal == 'RAI' 
		|| taskTypeVal == 'Grammarly' 
		|| (taskTypeVal.startsWith('Internal') && !taskTypeVal.startsWith("Internal product"))
		|| taskTypeVal.startsWith("Compliance Management")
		|| taskTypeVal.startsWith("Client onsite trips")
		|| taskTypeVal.startsWith("Administrative and management")
		|| taskTypeVal.startsWith("Knowledge development")
		
		) {
		return true;
	}
	return false;
}

function disableClientAdditionalFileds(taskTypeVal){
	if(taskTypeVal == 'LEAVE' 
		|| taskTypeVal == 'Leave' 
		|| taskTypeVal == 'Holiday' 
		|| taskTypeVal.startsWith("Internal product")
		|| taskTypeVal.startsWith("Internal team meetings")
		|| taskTypeVal.startsWith("Compliance Management")
		|| taskTypeVal.startsWith("Administrative and management")
		|| taskTypeVal.startsWith("Knowledge development")
		) {
		return true;
	}
	return false;
}
function changeFieldsOnTaskTypeChange(taskTypeVal) {
	$(".divisionGroupDiv").show();
	$(".ubaccountIdDiv").show();
	$(".divisionGroupDiv").show();
	$(".unbilledTeamDiv").show();
	$(".unbilledClientPartnerDiv").show();
	$(".ubreferenceNoDiv").show();
	
	if (disableAdditionalFileds(taskTypeVal)) {
			$(".divisionGroupDiv").hide();
			$(".ubaccountIdDiv").hide();
			$(".unbilledTeamDiv").hide();
			$(".unbilledClientPartnerDiv").hide();
			//$(".ubreferenceNoDiv").hide();
			//$(".unbilledClientName").addClass("hide");
			$(".uborderbooknodiv").addClass("hide");
	} else {
		if (taskTypeVal != "" && taskTypeVal.startsWith("Additional support")){
			//$(".unbilledClientName").removeClass("hide");
			$(".uborderbooknodiv").removeClass("hide");
		} else {
			//$(".unbilledClientName").addClass("hide");
			$(".uborderbooknodiv").addClass("hide");
		}
	}
}

function setReferenceNo(){
	$(".ubreferenceNo").val("");
	var taskTypeVal = $("#taskType").val();
	var accountVal = $("#ubaccountId").val();
	var divisionVal = $('input[name = "ubdivision"]:checked').val()
	var catCode = "";
	var accountCode ="";
	if (taskTypeVal != ""){
		$.each(categoryCodeMapping, function(i,categoryItem) {
		 if (categoryItem.name == taskTypeVal) {
			catCode = categoryItem.value;
			}
		}); 
	} 
	if (taskTypeVal != ""){
		$.each(accountNameCodetMapping, function(index,accountItem) {
		 if (accountItem.name == accountVal) {
			accountCode = accountItem.value;
			}
		}); 
	}

	if (catCode != "" && catCode.includes("{accountCode}") && !disableAdditionalFileds(taskTypeVal)) {
		catCode = catCode.replace("{accountCode}", accountCode);
	}
	
	if (catCode != "" && catCode.includes("{division}") && divisionVal != undefined && !disableAdditionalFileds(taskTypeVal) && !disableAdditionalFileds(accountVal)) {
		catCode = catCode.replace("{division}", divisionVal);
	}
	if (catCode != "") {
		catCode = catCode.replace("{accountCode}", "");
		catCode = catCode.replace("{division}", "");
		$(".ubreferenceNo").val(catCode);
	}
}
var startDateRange = $('#startDateRange').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm',useCurrent: false,defaultDate:getTodayDate(new Date()) + " 9:00"}).on('dp.change', function(e) {
   adjustStartNBTimeChanged();
});;
var completionDateRange = $('#completionDateRange').datetimepicker({ sideBySide: true, format: 'YYYY/MM/DD HH:mm',useCurrent: false,defaultDate:getTodayDate(new Date()) + " 17:00"});

var checkFutureTimeCaptureRestriction = function(taskName){
	if (futureHourCaptureDisabled) {
		if (taskName.toLowerCase() == 'leave' || taskName.toLowerCase() == "holiday" || taskName.toLowerCase() == 'leaveorholiday'){
			var taskMaxDate = new Date();
			taskMaxDate.setMonth(taskMaxDate.getMonth()+6);
			startDateRange.data("DateTimePicker").maxDate(taskMaxDate);
			completionDateRange.data("DateTimePicker").maxDate(taskMaxDate);
			
		} else {
			var taskMaxDate = new Date();
			taskMaxDate.setHours(23);
			taskMaxDate.setMinutes(59);
			startDateRange.data("DateTimePicker").maxDate(taskMaxDate);
			completionDateRange.data("DateTimePicker").maxDate(taskMaxDate);
		}
	}
};


var adjustStartNBTimeChanged = function(){
	var startTime = $('#startDateRange').val();
    var newCompletionDate = getTodayDate(new Date(startTime)) + " 17:00";
    $('#completionDateRange').val(newCompletionDate);
};

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
};

function reloadNBTime() {
	 var completedTime = getTodayDateTime(new Date());
	 validateStartAndEndUBTtime();
}

window.setInterval(reloadNBTime, 1000);
var unBilledButtonAction = "Create";
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
		$(".nonBillableTaskCreateText").html("Confirm & "+unBilledButtonAction+" " + timeMsg + " " + $("#taskType").val());
	} else {
		$(".nonBillableTaskCreateText").html(unBilledButtonAction);
	}
};

var createUnbillableTask = function(){
	var endDate = "";
	var startDate = "";
	$("#userId").removeClass("error");
	
	$(".unbilledClientPartner").removeClass("error");
	$(".unbilledTeam").removeClass("error");
	$(".ubaccountId").removeClass("error");
	
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
	var accountId =$("#ubaccountId").val();
	
	var clientPartner =$("#unbilledClientPartner").val();
	var teamName =$("#unbilledTeam").val();
	var actProjectName =$("#ubactProjectName").val();
	var division =$('input[name = "ubdivision"]:checked').val();

	var validationEnabled = true;
	if(disableAdditionalFileds(taskActCategory) || "Internal" == accountId || "LS" == accountId || "RAI" == accountId || "Grammarly" == accountId) {
		validationEnabled= false;	
		clientPartner ="";
		division ="";
		if ("Internal" != accountId &&  "LS" != accountId && "RAI" != accountId && "Grammarly" != accountId) {
			accountId ="";
		}
		orderBookNumber ="";
		clientName="";
		teamName="";
	}

	if (division == undefined) {
		division = "";
	}
	if (validationEnabled) {
		if (clientPartner == "") {
			$(".unbilledClientPartner").addClass("error");
			return;
		}
		if (teamName == "") {
			$(".unbilledTeam").addClass("error");
			return;
		}
		if (ubaccountId == "") {
			$(".ubaccountId").addClass("error");
			return;
		}
		if (division == "") {
			return;
		}
	}
	var clientName =$("#unbilledClientName").val();
	var orderBookNumber =$("#uborderbookno").val();
	var referenceNo =$("#ubreferenceNo").val();
	var isSkipWeekEnds = $("#skipWeekEnds").prop("checked") == true;
	
	var dataString = {clientName: clientName, skipWeekEnd:isSkipWeekEnds,
			userId:$("#userId").val(),type:taskType,taskActCategory:taskActCategory,
			startDate:startDate,endDate:endDate,comments:$("#taskUBStartComments").val(),
			clientPartner:clientPartner,teamName:teamName,
			account:accountId,orderBookNumber:orderBookNumber,referenceNo:referenceNo,actProjectName:actProjectName,division:division
	};
	doAjaxRequest("POST", "/bops/dashboard/createNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
			refreshCalendar();
        } else {
        	return false;
        }
       
    }, function(e) {
    });
};


var editUnbillableTask = function(taskActId){
	var endDate = "";
	var startDate = "";
	$("#userId").removeClass("error");
	$(".unbilledClientPartner").removeClass("error");
	$(".unbilledTeam").removeClass("error");
	$(".ubaccountId").removeClass("error");
	
	
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
	
	var accountId =$("#ubaccountId").val();
	
	var clientPartner =$("#unbilledClientPartner").val();
	var teamName =$("#unbilledTeam").val();
	var actProjectName =$("#ubactProjectName").val();
	var division =$('input[name = "ubdivision"]:checked').val();

	var validationEnabled = true;
	if(disableAdditionalFileds(taskActCategory) || "Internal" == accountId  || "LS" == accountId || "RAI" == accountId || "Grammarly" == accountId) {
		validationEnabled= false;	
		clientPartner ="";
		division ="";
		if ("Internal" != accountId &&  "LS" != accountId && "RAI" != accountId && "Grammarly" != accountId) {
			accountId ="";
		}
		orderBookNumber ="";
		clientName="";
		teamName="";
	}	
	if (division == undefined) {
		division = "";
	}
	if (validationEnabled) {
		if (clientPartner == "") {
			$(".unbilledClientPartner").addClass("error");
			return;
		}
		if (teamName == "") {
			$(".unbilledTeam").addClass("error");
			return;
		}
		if (ubaccountId == "") {
			$(".ubaccountId").addClass("error");
			return;
		}
		if (division == "") {
			return;
		}
	}
	var clientName =$("#unbilledClientName").val();
	var orderBookNumber =$("#uborderbookno").val();
	var referenceNo =$("#ubreferenceNo").val();
	var isSkipWeekEnds = $("#skipWeekEnds").prop("checked") == true;
	
	var dataString = {taskActId : taskActId,clientName: clientName, skipWeekEnd:isSkipWeekEnds,
			userId:$("#userId").val(),type:taskType,taskActCategory:taskActCategory,
			startDate:startDate,endDate:endDate,comments:$("#taskUBStartComments").val(),
			clientPartner:clientPartner,teamName:teamName,
			account:accountId,orderBookNumber:orderBookNumber,referenceNo:referenceNo,actProjectName:actProjectName,division:division
	};
	
	doAjaxRequest("POST", "/bops/dashboard/updateNonBillableTask", dataString, function(data) {
        var responseJson = JSON.parse(data);
        if (responseJson.status){
        	$(".modal").modal('hide');
			refreshCalendar();
        } else {
        	return false;
        }
       
    }, function(e) {
    });

};

var clearUnbillableFormForCreate = function(currentUserId) {
	$("#userId").val(currentUserId);
	if ($("select#userId").length >0){
		$(".unbilledOnBehalfOfDiv").show();
	}
	$(".taskActCreateBtn").attr("onclick", "createUnbillableTask()");
	$("#taskType").prop("selectedIndex",0);
	$("#startDateRange").val(getTodayDate(new Date()) + " 9:00");
	$("#completionDateRange").val(getTodayDate(new Date()) + " 17:00");
	$("#skipWeekEnds").prop("checked", true);
	$("#taskUBStartComments").val("");
	$("#unbilledClientName").val("");
	$("#unbilledClientPartner").val("");
	//$("#unbilledTeam").val("");
	$("#ubactProjectName").val("");
	$("#ubreferenceNo").val("");
	$("#uborderbookno").val("");
	
	$("#ubaccountId").val("");
	$('input[name = "ubdivision"]').attr('checked', false);
	$(".divisionGroup label").removeClass("active");
	$(".unbilledClientPartner").prop("selectedIndex",0);
	$(".ubaccountId").prop("selectedIndex",0);
	$('.unbilledClientPartner').selectpicker('refresh');
	$('.ubaccountId').selectpicker('refresh');
	//$(".unbilledClientName").addClass("hide");
	$(".uborderbooknodiv").addClass("hide");
		
	unBilledButtonAction = "Create";
	$(".nonBillableTaskCreateText").html("Create");
	$("#unbilledModelTitle").html("Create Non-billable time");
	$("#taskCreate").hide();
};

var editUnbillableFormForCreate = function(taskActId,division,account,orderBookNumber,referenceNo,actProjectName,taskActUserId, taskType, startTime, endTime, clientName, teamName,clientPartner) {
	$("#userId").val(taskActUserId);
	$(".unbilledOnBehalfOfDiv").hide();
	
	if (taskType == 'Leave') {
		taskType = "LEAVE";
	}
	
	if (taskType == 'Meeting') {
		taskType = "MEETING";
	}
	$("#unbilledModelTitle").html("Update Non-billable time");
	$("#taskType").val(taskType);
	$(".taskActCreateBtn").attr("onclick", "editUnbillableTask("+taskActId+")");
	$("#startDateRange").val(getTodayDateTime(new Date(startTime)));
	$("#completionDateRange").val(getTodayDateTime(new Date(endTime)));
	$("#skipWeekEnds").prop("checked", false);
	$("#taskUBStartComments").val("");
	$("#unbilledClientName").val(clientName);
	$("#unbilledClientPartner").val(clientPartner);
	$('#unbilledClientPartner').selectpicker('refresh');
	$("#unbilledTeam").val(teamName);
	$('#unbilledTeam').selectpicker('refresh');
	
	$("#division-"+division).trigger("click");
	$("#ubaccountId").val(account);
	$("#uborderbookno").val(orderBookNumber);
	$("#ubreferenceNo").val(referenceNo);
	$("#ubactProjectName").val(actProjectName);

	changeFieldsOnTaskTypeChange(taskType);
	changeFieldsOnTaskTypeChange(account);
	
	if("Internal" == account  || "LS" == account || "RAI" == account || "Grammarly" == account) {
		$(".ubaccountIdDiv").show();
	}
	$(".unbilledClientNameDiv").show();
	if(disableClientAdditionalFileds(taskType)) {
		$(".unbilledClientNameDiv").hide();
	}
	$('#ubaccountId').selectpicker('refresh');
	$(".nonBillableTaskCreateText").html("Update");
	unBilledButtonAction = "Update";
	$("#taskCreate").show();
};


var deleteTaskActivityAjax = function(activityId) {
	doAjaxRequest("POST", "/bops/dashboard/deleteTaskActivity", {activityId:activityId},  function() {
		//getAssignJsonData();
		$(".mb-control-close").click();
		$(".taskact-item"+activityId).remove();
	}, function(e) {
	});
};

function deleteTaskActivity(activityId, taskName){
	$(".msgConfirmText").html("Delete task activity");
	$(".msgConfirmText1").html(taskName);
	$("#confirmYesId").prop("href","javascript:deleteTaskActivityAjax('"+activityId+"')");
}


var reloadTaskActivities = function(){
	var taskFilterMonth = $(".taskActivityMonthSelector").val();
	var assigneeId = "";
	if ($("#taskActivityAssigneeId").length > 0) {
		assigneeId = $("#taskActivityAssigneeId").val();
	}
	$(".userTaskActivites").html("Loading...");
	doAjaxRequest("GET", "/bops/dashboard/userTaskActivity", {"monthFilter":taskFilterMonth, "userId": assigneeId.replace("userId","")},  function(data) {
		$(".userTaskActivites").html(data);
		if (assigneeId != 0) {
			//filterTaskActivities(assigneeId);
		}
	   }, function(e) {
	   });
};

//unbilled task end