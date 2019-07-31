
$(document).ready(function(){
	cloneProjectUpdateTimeRow();
	moveToCurrentWeek();
});

function clearWeeklyTimeLogTable(){
	$(".projectDetailsUpdateRow.active").remove();
	cloneProjectUpdateTimeRow();
	weeklyTotalCalculate();
}

function saveCurrentProjectWeekData(clearProjects){
	var dataString = "";
	var hasError = false;
	$(".projectDetailsUpdateRow.active").each(function(projectItemRowIndex, projectItemRow){
		var userId = $(projectItemRow).find(".userIdSelector").val();
		var projectType = $(projectItemRow).find(".projectTypeSelector").val();
		var projectId = $(projectItemRow).find(".projectNameSelectorIdHidden").val();
		var billableTaskId = $(projectItemRow).find(".billableTaskSelector").val();
		var nonBillableTaskId  =$(projectItemRow).find(".nonBillableTaskSelector").val();
		
		$(projectItemRow).find(".userIdSelector").removeClass("error");
		$(projectItemRow).find(".projectNameSelector").removeClass("error");
		$(projectItemRow).find(".billableTaskSelector").removeClass("error");
		$(projectItemRow).find(".nonBillableTaskSelector").removeClass("error");
		$(projectItemRow).find(".weekday").removeClass("error");
		
		if (userId == "") {
			hasError = true;
			$(projectItemRow).find(".userIdSelector").addClass("error");
		}
		if (projectType == "BILLABLE") {
			if (projectId == "") {
				hasError = true;
				$(projectItemRow).find(".projectNameSelector").addClass("error");
			}
			if (billableTaskId == "") {
				hasError = true;
				$(projectItemRow).find(".billableTaskSelector").addClass("error");
			}
		} else {
			if (nonBillableTaskId == "") {
				hasError = true;
				$(projectItemRow).find(".nonBillableTaskSelector").addClass("error");
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
		}
		
		dataString+="#PID#"+$(".weekSelector").val()+"#PD#";
	});

	if (!hasError){
		console.log(dataString);
		
		doAjaxRequest("POST", fsApplicationHome + "/weeklyTimeLog",{"projectDetails":dataString,"weekStartDate":$(".weekSelector").val()} , function(data) {
			famstacklog(data);
			var responseJson = JSON.parse(data);
			if (responseJson.status){
				if(clearProjects) {
					clearWeeklyTimeLogTable();
				}
			}
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	    });
	}

}

function saveCurrentProjectWeekDataAndMove(){
	saveCurrentProjectWeekData(false);
	moveToNextWeek();
}

function moveToPreviousWeek(){
	 var date =  new Date($(".weekSelector").val());
	 date.setDate(date.getDate() - 7);
	 $('.weekSelector').val(formatDate(date));
  	fillWeeklyDates(date);
}

function moveToNextWeek(){
	 var date =  new Date($(".weekSelector").val());
	 date.setDate(date.getDate() + 7);
	 $('.weekSelector').val(formatDate(date));
  	fillWeeklyDates(date);
}

function moveToCurrentWeek(){
	var date = new Date(getLastMonday(new Date()));
	$('.weekSelector').val(formatDate(date));
	fillWeeklyDates(date);
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
	fillTaskDetails(projectDetailsUpdateRowClone, project.tasks, project.projectCategory);
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


function weeklyTotalCalculate(){
    
    var weeklyTotal = 0;
    $(".projectDetailsUpdateRow .weekdayTotal").each(function() {
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
    
    $(".projectDetailsFooterRow .weeklyTotal").html((parseInt(weeklyTotal/60)) + ":" + parseInt(weeklyTotal%60));

}

function getLastMonday(date) {
	  var t = new Date(date);
	  t.setDate(t.getDate() - t.getDay() + 1);
	  return t;
	}
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
	var date = new Date($(".weekSelector").val());
	fillWeeklyDates(date);
});;

function formatDate(date) {
	var monthNames = [
	            	    "Jan", "Feb", "Mar",
	            	    "Apr", "May", "Jun", "Jul",
	            	    "Aug", "Sep", "Oct",
	            	    "Nov", "Dec"
	            	  ];
	  var day = date.getDate();
	  var monthIndex = date.getMonth();
	  var year = date.getFullYear();
	  return day + '-' + monthNames[monthIndex] + '-' + year;
	}
	
function formatDayDate(currentDate) {
	var monthNames = [
	            	    "Jan", "Feb", "Mar",
	            	    "Apr", "May", "Jun", "Jul",
	            	    "Aug", "Sep", "Oct",
	            	    "Nov", 
	            	    "Dec"
	            	  ];
	var weekNames = [
	            	    "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
	            	  ];
	  var day = currentDate.getDay();
	  var date = currentDate.getDate();
	  var monthIndex = currentDate.getMonth();
	  return weekNames[day] + ', ' + monthNames[monthIndex] + " " + date;
	}


function fillWeeklyDates(currentDate){
	for (i =1; i < 8; i++) {
		$(".day"+i).html(formatDayDate(currentDate));
		currentDate.setDate(currentDate.getDate() + 1);
	}
}
