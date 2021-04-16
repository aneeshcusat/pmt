
$(document).ready(function(){
	cloneProjectUpdateTimeRow();
	moveToCurrentWeek();
});


function getDateObject(dateString) {
	var dateArray = dateString.split("-");
	var year = 2020;
	var month = 0;
	var date = 1;
	if (dateArray.length == 3) {
		year = dateArray[2];
		month = monthNames.indexOf(dateArray[1]);
		date = dateArray[0];
	} else {
		year = dateArray[1];
		month = monthNames.indexOf(dateArray[0]);
	}
	return new Date(year, month, date);
}

function clearWeeklyTimeLogTable(){
	$(".projectDetailsUpdateRow.active").remove();
	cloneProjectUpdateTimeRow();
	weeklyTotalCalculate();
}

function saveCurrentProjectWeekData(clearProjects){
	$("#weeklyTimeLogSaveButton").attr("disabled", true);
	$("#weeklyTimeLogSaveNextButton").attr("disabled", true);
	var dataString = "";
	var hasError = false;
	var errorMsg = "";
	$(".projectDetailsUpdateRow.active").each(function(projectItemRowIndex, projectItemRow){
		var userId = $(projectItemRow).find(".userIdSelector").val();
		var projectType = $(projectItemRow).find(".projectTypeSelector").val();
		var projectId = $(projectItemRow).find(".projectNameSelectorIdHidden").val();
		var billableTaskId = $(projectItemRow).find(".billableTaskSelector").val();
		var nonBillableTaskId  =$(projectItemRow).find(".nonBillableTaskSelector").val();
		var taskComments  =$(projectItemRow).find(".taskcomments").val();
		
		$(projectItemRow).find(".userIdSelector").removeClass("error");
		$(projectItemRow).find(".projectNameSelector").removeClass("error");
		$(projectItemRow).find(".billableTaskSelector").removeClass("error");
		$(projectItemRow).find(".nonBillableTaskSelector").removeClass("error");
		$(projectItemRow).find(".weekday").removeClass("error");
		
		
		
		if (userId == "") {
			hasError = true;
			$(projectItemRow).find(".userIdSelector").addClass("error");
			errorMsg ="Invalid user selection";
		}
		
		var currentSelectedWeekDate = $(".weekSelector").val();
		
		if (currentSelectedWeekDate.includes('NaN') || currentSelectedWeekDate.includes('undefined')) {
			hasError = true;
			errorMsg ="Invalid date selection";
		}
		
		if (projectType == "BILLABLE") {
			if (projectId == "") {
				hasError = true;
				$(projectItemRow).find(".projectNameSelector").addClass("error");
				errorMsg ="Invalid project selection";
			}
			if (billableTaskId == "") {
				hasError = true;
				$(projectItemRow).find(".billableTaskSelector").addClass("error");
				errorMsg ="Invalid task selection";
			}
		} else {
			if (nonBillableTaskId == "") {
				hasError = true;
				$(projectItemRow).find(".nonBillableTaskSelector").addClass("error");
				errorMsg ="Invalid task selection";
			}
		}
		
		dataString+=userId+"#PID#"+projectType+"#PID#"+projectId+"#PID#"+billableTaskId+"#PID#"+nonBillableTaskId;
		var hasDayValue = false;
		for (var index=1;index<=7;index++) {
			var dayTime = $(projectItemRow).find(".day"+index).val();
			if (dayTime != "") {
				hasDayValue=true;
			}
			dataString+="#PID#"+dayTime;
		}
		
		if(!hasDayValue){
			$(projectItemRow).find(".weekday").addClass("error");
			hasError = true;
			errorMsg ="Invalid task time";
		}
		
		dataString+="#PID#"+taskComments;
		dataString+="#PID#"+$(".weekSelector").val()+"#PD#";
	});
	if (hasError){
		showErrorMessage(errorMsg);
		$("#weeklyTimeLogSaveButton").removeAttr("disabled");
		$("#weeklyTimeLogSaveNextButton").removeAttr("disabled");
	}
	
	if (!hasError){
		console.log(dataString);
		
		doAjaxRequest("POST", fsApplicationHome + "/weeklyTimeLog",{"projectDetails":dataString,"weekStartDate":$(".weekSelector").val()} , function(data) {
			famstacklog(data);
			var responseJson = JSON.parse(data);
			if (responseJson.status){
				if(clearProjects) {
					clearWeeklyTimeLogTable();
					getSelectedWeekLoggedData();
					showSuccessMessage("Task details saved Successfully!!!");
				}
			} else {
				 showErrorMessage("Unable to save data, please refresh the page and try again");
				 getSelectedWeekLoggedData();
			}
			$("#weeklyTimeLogSaveButton").removeAttr("disabled");
			$("#weeklyTimeLogSaveNextButton").removeAttr("disabled");
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	        getSelectedWeekLoggedData();
	        showErrorMessage("Unable to save data, please refresh the page and try again");
	        $("#weeklyTimeLogSaveButton").removeAttr("disabled");
			$("#weeklyTimeLogSaveNextButton").removeAttr("disabled");
	    });
	}

}
function showErrorMessage(errorMsg) {
	$(".taskLogInfo").removeClass("hide");
	$(".taskLogInfo .taskLogInfoMsg").html("Error has occured!!!  " + errorMsg);
	$(".taskLogInfo").removeClass("greenColor");
	$(".taskLogInfo").addClass("redColor");
	$(".taskLogInfo .errorMsg").removeClass("hide");
	$(".taskLogInfo .successMsg").addClass("hide");
}

function showSuccessMessage(successMsg) {
	$(".taskLogInfo").removeClass("hide");
	$(".taskLogInfo .taskLogInfoMsg").html(successMsg);
	$(".taskLogInfo").addClass("greenColor");
	$(".taskLogInfo").removeClass("redColor");
	$(".taskLogInfo .errorMsg").addClass("hide");
	$(".taskLogInfo .successMsg").removeClass("hide");
}

function saveCurrentProjectWeekDataAndMove(){
	saveCurrentProjectWeekData(false);
	moveToNextWeek();
}

function checkValidDate() {
	var currentSelectedWeekDate = $(".weekSelector").val();
	if (currentSelectedWeekDate == "" || currentSelectedWeekDate.includes('NaN') || currentSelectedWeekDate.includes('undefined')) {
		$('.weekSelector').val(formatDate(getLastMonday(new Date())));
	}
}

function moveToPreviousWeek(){
	checkValidDate();
	 var date =  getDateObject($(".weekSelector").val());
	 date.setDate(date.getDate() - 7);
	 var proposedDate = new Date(date);
	 $('.weekSelector').val(formatDate(date));
	fillWeeklyDates(date);
	canMoveToPreviousAndEdit(proposedDate);
}

function moveToNextWeek(){
	checkValidDate();
	 var date =  getDateObject($(".weekSelector").val());
	 date.setDate(date.getDate() + 7);
	 $('.weekSelector').val(formatDate(date));
	 var proposedDate = new Date(date);
  	fillWeeklyDates(date);
  	canMoveToPreviousAndEdit(proposedDate);
}

function moveToCurrentWeek(){
	checkValidDate();
	var date = new Date(getLastMonday(new Date()));
	$('.weekSelector').val(formatDate(date));
	fillWeeklyDates(date);
}


$(".loggedUserIdSelector").on("change", function(){
	var userId = $(this).val();
	$(".loggedData.trActive").addClass("hide");
	$(".loggedData.trActive").removeClass("visibleTask");
	if (userId == "") {
		$(".loggedData.trActive").removeClass("hide");
		$(".loggedData.trActive").addClass("visibleTask");
	} else {
		$(".loggedData.trActive.userId"+userId).removeClass("hide");
		$(".loggedData.trActive.userId"+userId).addClass("visibleTask");
	}
	
	weeklyLoggedTotalCalculate();
});


function getSelectedWeekLoggedData(){
	$(".taskLogInfo").addClass("hide");
	checkValidDate();
	doAjaxRequestWithGlobal("GET", fsApplicationHome + "/getWeeklyLogggedTime",{"weekStartDate":$(".weekSelector").val(),"userId":$(".currentUserId").val()} , function(data) {
		famstacklog(data);
		var responseJson = JSON.parse(data);
		fillLoggedData(responseJson);
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    }, false);
}

function fillLoggedData(data){
	$('.loggedData.trActive').remove();
	 if ( data.length == 0 ) {
	     if(!$(".loggedTimeDiv").hasClass("hide")){
	    	 $(".loggedTimeDiv").addClass("hide");
	     }
	  } else {
		  $(".loggedTimeDiv").removeClass("hide");
		$.each(data, function(key,value) {
			var loggedDataClone = $(".loggedData:first").clone();
			$(loggedDataClone).removeClass("hide").appendTo(".loggedDataTableBody");
			$(loggedDataClone).addClass("trActive");
			
			 var date =  getDateObject($(".weekSelector").val());
			 date.setDate(date.getDate() - 1);
			 for (var i = 0 ; i < 7 ;i++) {
				 date.setDate(date.getDate() + 1);
				 $(loggedDataClone).find('.loggedDay' + (i+1)).addClass(formatDate(date));
			 }
			
			$(loggedDataClone).find(".userName").html(value.userName);
			$(loggedDataClone).find(".type").html(value.type);
			$(loggedDataClone).find(".project").html(value.project);
			$(loggedDataClone).find(".task").html(value.task);
			$(loggedDataClone).find(".taskComments").html(value.comments);
			
			$(loggedDataClone).addClass("userId" + value.userId );
			$(loggedDataClone).find(".project").addClass("projectId" + value.projectId );
			$(loggedDataClone).find(".task").addClass("taskId" + value.taskId );
			
			$.each(value.time, function(dateString,timeInHours) {
				$(loggedDataClone).find("."+dateString).html(timeInHours);
			});
			
		}); 
		if ($(".loggedUserIdSelector").length > 0){
			$(".loggedUserIdSelector").trigger("change");
		}
		weeklyLoggedTotalCalculate();
	}
}

function cloneProjectUpdateTimeRow(){
	var projectDetailsUpdateRowClone = $(".projectDetailsUpdateRow:first").clone();
	$(projectDetailsUpdateRowClone).removeClass("hide").appendTo(".projectUpdateTimeBody");
	$(projectDetailsUpdateRowClone).addClass("active");
	
	initializeSelectBox($(projectDetailsUpdateRowClone).find(".userIdSelector"), function(){$(projectDetailsUpdateRowClone).find(".userIdSelector").removeClass("error");});
	initializeSelectBox($(projectDetailsUpdateRowClone).find(".projectTypeSelector"), projectTypeChange);

	if (taskCreateEnabled) {
		initializeSelect2Box($(projectDetailsUpdateRowClone).find(".billableTaskSelector"), function(){$(projectDetailsUpdateRowClone).find(".billableTaskSelector").removeClass("error");});
	} else {
		initializeSelectBox($(projectDetailsUpdateRowClone).find(".billableTaskSelector"), function(){$(projectDetailsUpdateRowClone).find(".billableTaskSelector").removeClass("error");});
	}
	initializeSelectBox($(projectDetailsUpdateRowClone).find(".nonBillableTaskSelector"), function(){$(projectDetailsUpdateRowClone).find(".nonBillableTaskSelector").removeClass("error");});
	
	$(projectDetailsUpdateRowClone).find('.projectNameSelector').on('change', function(){
		if ($(this).val().length < 4) {
			projectNameBoxError(projectDetailsUpdateRowClone, true);
			$(projectDetailsUpdateRowClone).find('.popoverContainer').addClass('hide');
		}
	});
	projectNameBoxError(projectDetailsUpdateRowClone, true);
	$(projectDetailsUpdateRowClone).find('.projectNameSelector').autocomplete(
	{
		width:'300px',
		minChars:4,
		serviceUrl: fsApplicationHome+'/getProjectNamesCodePoIdJson',
		onSearchStart:function(query){
			projectNameBoxError(projectDetailsUpdateRowClone, true);
			$(projectDetailsUpdateRowClone).find('.popoverContainer').addClass('hide');
		},
		onSelect : function(suggestion) {
			fillProjectDetails(projectDetailsUpdateRowClone,suggestion);
			projectNameBoxError(projectDetailsUpdateRowClone, false);
		}
	});
	
	if($(".projectDetailsUpdateRow").length > 10) {
		$(".addNewRowLink").addClass("hide");
	}
}

function fillProjectDetails(projectDetailsUpdateRowClone,project) {
	var projectDetailsHtml = '<span>Project Name : <b>'+project.name+'</b></span>\
	<span>Name : <b>'+project.name+'</b></span>\
	<span>Id : <b>'+project.data+'</b></span>\
	<span>Code : <b>'+project.projectCode+'</b></span>\
	<span>Category : <b>'+project.projectCategory+'</b></span>\
	<span>POId : <b>'+project.projectPOId+'</b></span>\
	<span>Date : <b>'+project.projectDate+'</b></span>';
	
	//$(projectDetailsUpdateRowClone).find('.projectDetailsLink').attr("href","project/"+project.data);
	$(projectDetailsUpdateRowClone).find('.popoverContainer').removeClass("hide");
	$(projectDetailsUpdateRowClone).find('.projectNameSelectorIdHidden').val(project.data);
	
	$(projectDetailsUpdateRowClone).find('.popoverContainer').html('<a class="popoverProject" data-toggle="popover" type="button" href="#"><i style="color:#2509fb" class="fa fa-info-circle" aria-hidden="true"></i></a><a class="projectDetailsLink" href="project\\'+project.data+'" target="_new" style="padding-left: 2px"><i style="color: #0b670c;" class="fa fa-angle-double-right fa-lg" aria-hidden="true"></i></a>');
	$(projectDetailsUpdateRowClone).find('.popoverProject').popover({
		trigger: 'focus',
		container: 'body',
		html:true,
		placement:'bottom',
		content:'content',
		template:'<div class="popover" role="tooltip"><div class="arrow"></div><div class="popover-body">'+projectDetailsHtml+'</div></div>'
	});
	
	doAjaxRequestWithGlobal("GET", fsApplicationHome + "/getAllTasksJson",{"projectId":project.data, "howManyOldData":howManyOldData} , function(data) {
		famstacklog(data);
		var responseJson = JSON.parse(data);
		fillTaskDetails(projectDetailsUpdateRowClone, responseJson.tasks, project.projectCategory);
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
    }, false);
	
}

function fillTaskDetails(projectDetailsUpdateRowClone, tasks, projectCategory){
	//var taskName = 'Week '+ moment($(".weekSelector").val(), "DD/MMM/YYYY").week() +', Task '+projectCategory;
	var taskName = 'Weekly Task '+projectCategory;
	var billableTasksList ="";
	if (taskCreateEnabled) {
	 billableTasksList = '<option value="'+taskName+'">[NEW] '+taskName+'</option>';
	} 
	if (tasks.length > 0) {
		for (i in tasks) {
			billableTasksList += '<option data-status="'+tasks[i].status+'" value="'+tasks[i].id+'">'+tasks[i].name+'</option>';
		}
	}
	$(projectDetailsUpdateRowClone).find('select.billableTaskSelector').html(billableTasksList);
	$(projectDetailsUpdateRowClone).find('select.billableTaskSelector').selectpicker('refresh');
}

function projectNameBoxError(projectDetailsUpdateRowClone,isError){
	if (isError) {
   	 $(projectDetailsUpdateRowClone).find('.projectNameSelector').addClass("error");
   	 $(projectDetailsUpdateRowClone).find('.projectNameSelectorIdHidden').val("");
   	 //$("#weeklyTimeLogSaveButton").addClass("hide");
   	 //$("#weeklyTimeLogSaveNextButton").addClass("hide");
   	 $(projectDetailsUpdateRowClone).find('select.billableTaskSelector').html('');
   	 $(projectDetailsUpdateRowClone).find('select.billableTaskSelector').selectpicker('refresh');
    } else {
   	 $(projectDetailsUpdateRowClone).find('.projectNameSelector').removeClass("error"); 
   	//$("#weeklyTimeLogSaveButton").removeClass("hide");
   	 //$("#weeklyTimeLogSaveNextButton").removeClass("hide");
    }
}

function initializeSelectBox(thisVar, callBackfunction){
	$(thisVar).selectpicker({
	    liveSearch: true,
	    showSubtext: true
	}).on('change', callBackfunction);
}

function initializeSelect2Box(thisVar, callBackfunction){
	$(thisVar).select2({
		  tags: true,
		  placeholder: 'Select an option'
		}).on('change', callBackfunction);
}

$(document).on('click', ".projectDetailsUpdateRowDelLink" , function() {
    $(this).closest(".projectDetailsUpdateRow").remove();
    weeklyTotalCalculate();
    if($(".projectDetailsUpdateRow").length <= 9) {
    	$(".addNewRowLink").removeClass("hide");
	}
});

$(document).on('change', ".projectDetailsUpdateRow .weekday" , function() {
	var weekdayTotal = 0;
    $(this).closest(".projectDetailsUpdateRow").find(".weekday").each(function() {
    	var time = $(this).val();
    	var timeArray = time.split('.');
    	
    	if (timeArray.length > 1) {
    		var mins = parseInt(timeArray[1]);
    		if (mins < 10) {
    			mins*=10;
    		}
    		weekdayTotal= weekdayTotal + parseInt(timeArray[0] * 60)  + mins;
    	} else {
    		weekdayTotal+=parseInt((timeArray[0] * 60));
    	}
    });
    
    $(this).closest(".projectDetailsUpdateRow").find(".weekdayTotal").html((parseInt(weekdayTotal/60)) + ":" + parseInt(weekdayTotal%60));
    weeklyTotalCalculate();
});

function projectTypeChange(){
	var nonBillableSelector = $(this).closest(".projectDetailsUpdateRow").find(".nonBillableTaskSelector");
	var billableSelector = $(this).closest(".projectDetailsUpdateRow").find(".billableTaskSelector");
	var projectNames = $(this).closest(".projectDetailsUpdateRow").find(".projectNameSelector");
	var billableSelectorContainer = $(this).closest(".projectDetailsUpdateRow").find(".select2-container");
	
	var popoverContainer = $(this).closest(".projectDetailsUpdateRow").find(".popoverContainer");
	
	if($(this).val() == 'BILLABLE') {
		$(billableSelector).removeClass('hide');
		$(billableSelectorContainer).removeClass('hide');
		$(nonBillableSelector).addClass('hide');
		$(projectNames).removeAttr("disabled");
		$(popoverContainer).removeClass("hide");
	} else {
		$(billableSelectorContainer).addClass('hide');
		$(nonBillableSelector).removeClass('hide');
		$(billableSelector).addClass('hide');
		$(projectNames).attr("disabled","disabled");
		projectNameBoxError($(this).closest(".projectDetailsUpdateRow"), false);
		$(popoverContainer).addClass("hide");
	}
}
function weeklyLoggedTotalCalculate(){
	weeklyTotalTimeCalculate("loggedData.visibleTask","projectLoggedDetailsFooterRow");
}

function weeklyTotalCalculate(){
	weeklyTotalTimeCalculate('projectDetailsUpdateRow', 'projectDetailsFooterRow');
}
function weeklyTotalTimeCalculate(source, destination){
    
    var weeklyTotal = 0;
    $("."+source+" .weekdayTotal").each(function() {
    	var time = $(this).html();
    	var timeArray = time.split(':');
    	if (timeArray.length > 1) {
    		var mins = parseInt(timeArray[1]);
    		if (mins < 10) {
    			mins*=10;
    		}
    		weeklyTotal= weeklyTotal + parseInt(timeArray[0] * 60)  + mins;
    	}
    	
    });
    
    $("."+destination+" .weeklyTotal").html((parseInt(weeklyTotal/60)) + ":" + parseInt(weeklyTotal%60));

}

function getLastMonday(date) {
	  var t = new Date(date);
	  t.setDate(t.getDate() - t.getDay() + 1);
	  return t;
}

function canMoveToPreviousAndEdit(proposedDate){
	if(weekTLDisableMonthEnabled || restrictTimesheetTillNextDay) {
		
		var lastMonthDate = getLastDayOfMonth(new Date());
		var firstDayOfCurrentMonth = new Date(lastMonthDate);
		if (restrictTimesheetTillNextDay) {
			lastMonthDate = new Date();
			lastMonthDate.setHours(0, 0, 0, 0); ;
			if(new Date().getHours() < 15) {
				lastMonthDate.setDate(lastMonthDate.getDate() - 2);
			} else {
				lastMonthDate.setDate(lastMonthDate.getDate() - 1);
			}
			firstDayOfCurrentMonth = lastMonthDate;
		}
		if(lastMonthDate.getTime() >= proposedDate.getTime()) {
			var index = 0;
			var tempDate = new Date(proposedDate);
			firstDayOfCurrentMonth.setDate(lastMonthDate.getDate() + 1);
			
			do {
			$(".weekday.day" + (index + 1)).addClass("hide");
			 tempDate.setDate(tempDate.getDate() + 1);
			 index++;
			}
			while (index < 7 && firstDayOfCurrentMonth.getTime() > tempDate.getTime());
			
			if (index >= 7) {
				$("#weeklyTimeLogSaveButton").addClass("hide");
				$("#weeklyTimeLogSaveNextButton").addClass("hide");
			}
		}
	}
}

function getLastDayOfMonth(date) {
	  var t = new Date(date);
	  if(weekTLDisableMonthEnabled) {
		  t.setDate(t.getDate() - t.getDate());
	  } else {
		  t.setDate(t.getDate() - 90); 
	  }
	  t.setHours(0, 0, 0, 0);
	  return t;
}

$(document).ready(function(){
$('.weekSelector').val(formatDate(getLastMonday(new Date())));
$('.weekSelector').datepicker({ 
	defaultViewDate:getLastMonday(new Date()),
	defaultDate:getLastMonday(new Date()),
	daysOfWeekHighlighted:1,
	format: 'dd-M-yyyy',
	calendarWeeks:true,
	weekStart:1,
	daysOfWeekDisabled: [0,2,3,4,5,6],
	autoclose:true
}).on('changeDate', function(e) {
	checkValidDate();
	var date = getDateObject($(".weekSelector").val());
	fillWeeklyDates(date);
	canMoveToPreviousAndEdit(date);
});


$('input[type="number"].weekday').on('keyup',function(){
    v = parseInt($(this).val());
    console.log(v);
    min = parseInt($(this).attr('min'));
    max = parseInt($(this).attr('max'));

    if (v < min){
        $(this).val(min);
    } else if (v > max){
        $(this).val(max);
    }
})
});
var monthNames = [
            	    "Jan", "Feb", "Mar",
            	    "Apr", "May", "Jun", "Jul",
            	    "Aug", "Sep", "Oct",
            	    "Nov", 
            	    "Dec"
            	  ];

function formatDate(date) {

	  var day = date.getDate();
	  var dayString = ""+day;
	  if (day < 10) {
		  dayString = "0"+day;
	  }
	  var monthIndex = date.getMonth();
	  var year = date.getFullYear();
	  var dateformatedString =  dayString + '-' + monthNames[monthIndex] + '-' + year;
	  
	  if (dateformatedString.indexOf("--") > 0) {
		  dateformatedString = dateformatedString.replace(/\-+/g, '-');
	  }
	  return dateformatedString;
	}

function formatDayDate(currentDate) {
	
	var weekNames = [
	            	    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
	            	  ];
	  var day = currentDate.getDay();
	  var date = currentDate.getDate();
	  var monthIndex = currentDate.getMonth();
	 return weekNames[day] + ', ' + monthNames[monthIndex] + " " + date;
	 
	}


function fillWeeklyDates(currentDate){
	$(".weekday").removeClass("hide");
	$("#weeklyTimeLogSaveButton").removeClass("hide");
	$("#weeklyTimeLogSaveNextButton").removeClass("hide");
	
	for (var i =1; i < 8; i++) {
		$(".day"+i).html(formatDayDate(currentDate));
		currentDate.setDate(currentDate.getDate() + 1);
	}
	$('.loggedData.trActive').remove();
	getSelectedWeekLoggedData();
}

function saveTimeSheetData(dataString){
	doAjaxRequest("POST", fsApplicationHome + "/weeklyTimeLog",{"projectDetails":dataString,"weekStartDate":$(".weekSelector").val()} , function(data) {
		console.log(data);
}, function(e) {
});}
