var monthNames = [
	            	    "Jan", "Feb", "Mar",
	            	    "Apr", "May", "Jun", "Jul",
	            	    "Aug", "Sep", "Oct",
	            	    "Nov", "Dec"
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
function formatCalenderDate(date) {
	  var monthIndex = date.getMonth() + 1;
	  var year = date.getFullYear();
	  var day = date.getDate();
	  
	  return year+"-"+monthIndex+"-"+day;
}

function formatMonthDate(date) {

	  var monthIndex = date.getMonth();
	  var year = date.getFullYear();
	  var dateformatedString =  monthNames[monthIndex] + '-' + year;
	  
	  return dateformatedString;
	}

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

function moveToPrevious(){
	 var date =  getDateObject($(".weekSelector").val());
	 date.setDate(date.getDate() - 7);
	 $('.weekSelector').val(formatDate(date));
	 
	 var date =  getDateObject($(".dailySelector").val());
	 date.setDate(date.getDate() - 1);
	 $('.dailySelector').val(formatDate(date));
	 
	 var date = getDateObject($(".monthSelector").val());
	 date.setMonth(date.getMonth() - 1);
	 $('.monthSelector').val(formatMonthDate(date));
	 
	 checkValidDate();
	 refreshReportDataOrDownload(false);
}

function moveToNext(){
	 var date =  getDateObject($(".weekSelector").val());
	 date.setDate(date.getDate() + 7);
	 $('.weekSelector').val(formatDate(date));
	 
	 var date =  getDateObject($(".dailySelector").val());
	 date.setDate(date.getDate() + 1);
	 $('.dailySelector').val(formatDate(date));
	 
	 var date =  getDateObject($(".monthSelector").val());
	 date.setMonth(date.getMonth() + 1);
	 $('.monthSelector').val(formatMonthDate(date));
	 
	 checkValidDate();
	 refreshReportDataOrDownload(false);
}

function moveToCurrent(){
	var date = new Date(getLastMonday(new Date()));
	$('.weekSelector').val(formatDate(date));
	
	 var date =  new Date();
	 $('.dailySelector').val(formatDate(date));
	 
	 var date =  new Date();
	 $('.monthSelector').val(formatMonthDate(date));
	 
	 checkValidDate();
	 refreshReportDataOrDownload(false);
}

function checkValidDate() {
	
	var currentSelectedDayDate = $(".dailySelector").val();
	if (currentSelectedDayDate == "" || currentSelectedDayDate.includes('NaN') || currentSelectedDayDate.includes('undefined')) {
		 $('.dailySelector').val(formatDate(new Date()));
	}
	
	var currentSelectedWeekDate = $(".weekSelector").val();
	if (currentSelectedWeekDate == "" || currentSelectedWeekDate.includes('NaN') || currentSelectedWeekDate.includes('undefined')) {
		$('.weekSelector').val(formatDate(getLastMonday(new Date())));
	}
	
	var currentSelectedMonthDate = $(".monthSelector").val();
	if (currentSelectedMonthDate == "" || currentSelectedMonthDate.includes('NaN') || currentSelectedMonthDate.includes('undefined')) {
		$('.monthSelector').val(formatMonthDate(new Date()));
	}
}

function getLastMonday(date) {
	  var t = new Date(date);
	  t.setDate(t.getDate() - t.getDay() + 1);
	  return t;
}

function getLastSundayOfMonth(date) {
	  var t = new Date(date);
	  t.setDate((t.getDate() - t.getDay()) + 7);
	  return t;
}

function clearReportDataTableOnDateChange(){
	var autoReportType = $(".autoReportType").val();
	if (autoReportType != 'TEAM_UTILIZATION_CHART') {
		clearReportDataTable();
	} else {
		refreshReportDataOrDownload(false);
	}
}

$(document).ready(function(){
$('.dailySelector').val(formatDate(new Date()));
$('.dailySelector').datepicker({ 
	defaultViewDate:getLastMonday(new Date()),
	defaultDate:getLastMonday(new Date()),
	defaultDate:new Date(),
	daysOfWeekHighlighted:1,
	format: 'dd-M-yyyy',
	autoclose:true
}).on('changeDate', function(e) {
	clearReportDataTableOnDateChange();
});;
	
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
	clearReportDataTableOnDateChange();
});;

$('.monthSelector').val(formatMonthDate(getFirstDayOfMonth(new Date())));
$('.monthSelector').datepicker({ 
	defaultDate:getFirstDayOfMonth(new Date()),
	viewMode:1,
	minViewMode:1,
	daysOfWeekHighlighted:1,
	format: 'M-yyyy',
	autoclose:true
}).on('changeDate', function(e) {
	clearReportDataTableOnDateChange();
});;
});

function getFirstDayOfMonth(date){
	return new Date(date.getFullYear(), date.getMonth(), 1);
}

$(".autoReportType").on("change",function(){
	 $(".autoReportDuration option").remove();
	 $('.autoReportDuration').append($('<option>').val("DAILY").text("Daily"));
	 $('.autoReportDuration').append($('<option>').val("WEEKLY").text("Weekly"));
	 //$('.autoReportDuration').append($('<option>').val("WEEKLY_DAILY").text("Weekly Day wise")).css("hide");
	 $('.autoReportDuration').append($('<option>').val("MONTHLY").text("Monthly"));
	 $('.autoReportDuration').append($('<option>').val("MONTHLY_WTW").text("Monthly Week to Week"));
	 $('.autoReportDuration').append($('<option>').val("MONTHLY_ENE").text("Monthly End to End"));
	 $(".autoReportDuration option[value='DATE_RANGE']").remove();

	 clearReportDataTable();
	 $(".reportHeaderInputs").removeClass("hide");
	$(".projectestvsactualDiv").addClass("hide"); 
	$(".monthRangeSelectorDiv").addClass("hide");
	var selectedValue = $(this).val();
	 if("USER_SITE_ACTIVITY" == selectedValue) {
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
		 $(".autoReportDuration option[value='MONTHLY_WTW']").remove();
	 } else  if("USER_UTILIZATION" == selectedValue) {
		 
	 } else  if("WEEKLY_PO_ESTIMATION" == selectedValue) {
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_WTW']").remove();
		 $('.autoReportDuration').append($('<option>').val("DATE_RANGE").text("Date Range"));
	 } else  if("WEEKLY_PROJECT_HOURS" == selectedValue) {
		 $(".autoReportDuration option[value='DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
		 $(".autoReportDuration option[value='MONTHLY_WTW']").remove();
	 } else  if("TEAM_UTILIZATION_CHART" == selectedValue) {
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
		 $(".autoReportDuration option[value='MONTHLY_WTW']").remove();
	 } else  if("PROJECT_DETAILS_BY_SKILLS" == selectedValue) {
		$(".reportHeaderInputs").addClass("hide");
		$(".projectestvsactualDiv").removeClass("hide");
		$(".monthRangeSelectorDiv").removeClass("hide");
	 } else if ("TIME_SHEET_DUMP" == selectedValue) { 
		 $('.autoReportDuration').append($('<option>').val("DATE_RANGE").text("Date Range"));
		 $(".autoReportDuration option[value='MONTHLY_WTW']").remove();
	 }else  if("UTILIZATION_BY_SKILLS" == selectedValue || "UTILIZATION_BY_EMPLOYEE_BY_SKILLS" == selectedValue || "UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY" == selectedValue) {
		 $(".autoReportDuration option[value='DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
		 $(".autoReportDuration option[value='MONTHLY_WTW']").remove();
		 $('.autoReportDuration').append($('<option>').val("DATE_RANGE").text("Date Range"));
	}
	 $(".autoReportDuration").trigger("change");
});

$(".autoReportDuration").on("change",function(){
	 clearReportDataTableOnDateChange();
	 
	 var selectedValue = $(this).val();
	 $('.monthSelector').addClass("hide");
	 $(".weekSelector").addClass("hide");
	 $(".dailySelector").addClass("hide");
	 $(".monthDateRangeSelector").addClass("hide");
	 if("DAILY" == selectedValue) {
		 $(".dailySelector").removeClass("hide");
	 } else if ("WEEKLY" == selectedValue) {
		 $(".weekSelector").removeClass("hide");
	 } else if ("WEEKLY_DAILY" == selectedValue) {
		 $('.weekSelector').removeClass("hide");
	 } else if ("MONTHLY" == selectedValue) {
		 $('.monthSelector').removeClass("hide");
	 } else if ("MONTHLY_ENE" == selectedValue) {
		 $('.monthSelector').removeClass("hide");
	 } else if ("MONTHLY_WTW" == selectedValue) {
		 $('.monthSelector').removeClass("hide");
	 } else if ("DATE_RANGE" == selectedValue) {
		 $(".monthDateRangeSelector").removeClass("hide");
	 }
});


$(".refreshTeamUtilizationButton").on("click", function(){
	refreshTeamUtilizationComparisonChart();
});

$(".refreshButton").on("click", function(){
	refreshReportDataOrDownload(false);
});

function refreshReportDataOrDownload(isDownload) {
	checkValidDate();
	if (dataTable != null) {
		dataTable.destroy();
		dataTable = null;
	}
	var autoReportDuration = $(".autoReportDuration").val();
	var autoReportType = $(".autoReportType").val();
	var startDate = "";
	var endDate = "";
	 if("DAILY" == autoReportDuration) {
		var dayDate = getDateObject($(".dailySelector").val());
		startDate = dayDate;
		endDate = dayDate;
	 } else if ("WEEKLY" == autoReportDuration) {
		 var weekStartDate =  $(".weekSelector").val();
		 var weekEndDate =  getDateObject(weekStartDate);
		 weekEndDate.setDate(weekEndDate.getDate() + 6);
		 startDate = getDateObject(weekStartDate);
		 endDate = weekEndDate;
	 } else if ("WEEKLY_DAILY" == autoReportDuration) {
		 var weekStartDate =  $(".weekSelector").val();
		 var weekEndDate =  getDateObject(weekStartDate);
		 weekEndDate.setDate(weekEndDate.getDate() + 6);
		 startDate = getDateObject(weekStartDate);
		 endDate = weekEndDate;
	 } else if ("MONTHLY" == autoReportDuration || "MONTHLY_WTW" == autoReportDuration) {
		 var monthDate =  getDateObject("01-" + $('.monthSelector').val());
		 var firstDayOfMonth = new Date(monthDate.getFullYear(), monthDate.getMonth(), 1);
		 var lastDayOfMonth = new Date(monthDate.getFullYear(), monthDate.getMonth() + 1, 0);
		 var firstMondayOfMonth = getLastMonday(firstDayOfMonth);
		 var lastSundayOfMonth = getLastSundayOfMonth(lastDayOfMonth);
		 if ("USER_UTILIZATION" == autoReportType && "MONTHLY_WTW" == autoReportDuration) {
			 startDate = firstMondayOfMonth;
			 endDate = lastSundayOfMonth;
		 } else {
			 startDate = firstDayOfMonth;
			 endDate = lastDayOfMonth;
		 }
	 } else if ("MONTHLY_ENE" == autoReportDuration) {
		 var firstDayOfMonth = getDateObject("01-" + $('.monthSelector').val());
		 var lastDayOfMonth = new Date(firstDayOfMonth.getFullYear(), firstDayOfMonth.getMonth() + 1, 0);
		 startDate = firstDayOfMonth;
		 endDate = lastDayOfMonth;
	 } else if("DATE_RANGE" == autoReportDuration) {
		 var dateString = $(".monthDateRangeSelector").val();
			var dateRange =	dateString.split(" - ");
			 startDate =  getDateObject("01-" +dateRange[0]);
			 endDate =  getDateObject("01-" +dateRange[1]);
			 endDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1, 0);
	 }
	 
	 var reportStartDate = formatCalenderDate(startDate);
	 var reportEndDate = formatCalenderDate(endDate);
	 
	 var reportType = autoReportType;
	 if("USER_SITE_ACTIVITY" == autoReportType) {
	 } else  if("USER_UTILIZATION" == autoReportType) {
		 if ("MONTHLY_WTW" == autoReportDuration) {
			 reportType = "WEEKWISE_USER_UTILIZATION_MONTHLY";
		 } else if ("WEEKLY_DAILY" == autoReportDuration) {
			 reportType = "DAILYWISE_USER_UTILIZATION_WEEKLY";
		 } else if ("MONTHLY_ENE" == autoReportDuration) {
			 reportType = "WEEKWISE_USER_UTILIZATION_MONTHLY_ENE";
		 } 
	 } else  if("WEEKLY_PO_ESTIMATION" == autoReportType) {
	 } else  if("WEEKLY_PROJECT_HOURS" == autoReportType) {
	 }

	 if (autoReportType == "TEAM_UTILIZATION_CHART") {
		$(".teamUtilizationChartDiv").removeClass("hide");
		refreshTeamUtilizationChart(reportStartDate, reportEndDate);
	 } else {
		 if(isDownload) {
			 window.location.href = fsApplicationHome + "/download?reportType=" + reportType + "&reportStartDate=" + reportStartDate + "&reportEndDate=" + reportEndDate +"&fileName=" + downloadXLSFileName;
		 } else {
			 doAjaxRequestWithGlobal("GET", fsApplicationHome + "/getReportData",{"reportType":reportType,"reportStartDate":reportStartDate,"reportEndDate":reportEndDate} , function(data) {
				fillReportTableData(data, reportType, autoReportDuration);
				$(".exportButton").removeClass("hide");
				 $("#reportSearchBoxId").removeClass("hide");
				 $("#reportSearchBoxId").val("");
			 }, function(e) {
			}, false);
		 }
	 }
}

var downloadXLSFileName = "";

function fillReportTableData(data, reportType,autoReportDuration){

	var jsonData = JSON.parse(data);
	 if("USER_SITE_ACTIVITY" == reportType) {
		 fillUserActivityReportData(data);
		 downloadXLSFileName = "User Site Activity Report";
	 } else  if("USER_UTILIZATION" == reportType) {
		 fillUserUtilizationReportData(data);
		 downloadXLSFileName = "User Utilization Report";
	 } else  if("WEEKWISE_USER_UTILIZATION_MONTHLY" == reportType) {
		 fillUserUtilizationMonthlyReportData(data);
		 downloadXLSFileName = "User Monthly Utilization Report";
	 }else  if("WEEKWISE_USER_UTILIZATION_MONTHLY_ENE" == reportType) {
		 fillUserUtilizationMonthlyReportData(data);
		 downloadXLSFileName = "User Monthly Utilization Report";
	 } else  if("DAILYWISE_USER_UTILIZATION_WEEKLY" == reportType) {
		 downloadXLSFileName = "User Weekly Utilization Report";
	 } else  if("WEEKLY_PO_ESTIMATION" == reportType) {
		 fillPOEstimationReportData(data);
		 downloadXLSFileName = "PO Estimation Report";
	 } else  if("WEEKLY_PROJECT_HOURS" == reportType) {
		 fillProjectHoursReportData(data);
		 downloadXLSFileName = "Weekly Project Hours Report";
	 } else  if("UTILIZATION_BY_SKILLS" == reportType || "UTILIZATION_BY_EMPLOYEE_BY_SKILLS" == reportType || "UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY" == reportType) {
		 fillUtilizationBySkillsReportData(data, reportType);
		 downloadXLSFileName = "UTILIZATION_BY_SKILLS" == reportType 
		 	? "Utilisation By Skills Report" : "UTILIZATION_BY_EMPLOYEE_BY_SKILLS" == reportType 
		 			? "Utilisation By User Skills Report" : "UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY" == reportType 
		 					? "Utilisation By Project Category Report" : ''  ;
	 } else if ("TIME_SHEET_DUMP" == reportType) { 
		 fillTimesheetDumpReportData(data);
		 downloadXLSFileName = "Timesheet dump";
	 }
	 
	 initializeExportTable(downloadXLSFileName);
}

function fillUserActivityReportData(data) {
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-activity").clone();
	var headerHtml = $(reportHeader).html();
	$.each(jsonData.DATE_LIST, function( index, value ) {
		headerHtml +="<th>Status ("+value+")</th>";
	});
	
	$(".retportDataHeader").html("<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		reportBodyHtml += "<td>"+value.employeeName+"</td>";
		
		if(value.reportingManager == null) {
			reportBodyHtml += "<td></td>";
		} else {
			reportBodyHtml += "<td>"+value.reportingManager+"</td>";
		}
		
		$.each(value.statusList, function( index, statusValue ) {
			if(statusValue.status == 'Active') {
				reportBodyHtml += "<td>"+statusValue.status+"</td>";
			} else {
				reportBodyHtml += "<td><span style='color:red;font-weight: bold;'>"+statusValue.status+"</span></td>";
			}
			
		});
		reportBodyHtml += "</tr>";
	});
	
	$(".reportDataBody").html( reportBodyHtml );
}

function fillUtilizationBySkillsReportData(data, reportType) {
	var jsonData = JSON.parse(data);
	
	var type = 'utilizationbyskills';
	
	 if("UTILIZATION_BY_EMPLOYEE_BY_SKILLS" == reportType) {
		 type = 'utilizationbyuserskills';
	 }else if("UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY" == reportType){
		 type = 'utilizationbyprojectcategory';
	 }
	
	var reportHeader = $(".reportDataTemplate .reportDataHeader-" + type).clone();
	var headerHtml = $(reportHeader).html();
	
	$(".retportDataHeader").html("<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		
		if("UTILIZATION_BY_SKILLS" != reportType){
			reportBodyHtml += "<td>"+value.employeeCode+"</td>";
			reportBodyHtml += "<td>"+value.employeeName+"</td>";
			reportBodyHtml += "<td>"+value.designation+"</td>";
		}
		
		reportBodyHtml += "<td>"+value.skillOrCategory+"</td>";
		if("UTILIZATION_BY_EMPLOYEE_BY_PROJECT_CATEGORY" == reportType){
			reportBodyHtml += "<td>"+value.projectAccounts.map(function(account) {return account;})+"</td>";
		}
		reportBodyHtml += "<td>"+value.monthYear+"</td>";
		
		reportBodyHtml += "<td>"+value.billableHours+"</td>";
		reportBodyHtml += "<td>"+value.nonBillableHours+"</td>";
		reportBodyHtml += "<td>"+value.totalHrs+"</td>";
	});
	
	$(".reportDataBody").html( reportBodyHtml );
}

function fillTimesheetDumpReportData(data){
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-timesheetdump").clone();
	var headerHtml = $(reportHeader).html();
	
	$(".retportDataHeader").html("<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		reportBodyHtml += "<td>"+value.fullName+"</td>";
		reportBodyHtml += "<td>"+value.employeeeId+"</td>";
		reportBodyHtml += "<td>"+value.deliveryLead+"</td>";
		reportBodyHtml += "<td>"+value.clientName+"</td>";
		reportBodyHtml += "<td>"+value.projectCode+"</td>";
		reportBodyHtml += "<td>"+value.projectId+"</td>";
		reportBodyHtml += "<td>"+value.projectNumber+"</td>";
		reportBodyHtml += "<td>"+value.orderRefNumber+"</td>";
		reportBodyHtml += "<td>"+value.proposalNumber+"</td>";
		reportBodyHtml += "<td>"+value.projectName+"</td>";
		reportBodyHtml += "<td>"+value.projectStatus+"</td>";
		reportBodyHtml += "<td>"+value.projectCategory+"</td>";
		reportBodyHtml += "<td>"+value.newProjectCategory+"</td>";
		reportBodyHtml += "<td>"+value.taskName+"</td>";
		reportBodyHtml += "<td>"+value.accountName+"</td>";
		reportBodyHtml += "<td>"+value.teamName+"</td>";
		reportBodyHtml += "<td>"+value.subTeamName+"</td>";
		reportBodyHtml += "<td>"+value.taskActivityStartTime+"</td>";
		reportBodyHtml += "<td>"+value.durationInHours+"</td>";
		reportBodyHtml += "<td>"+value.taskCompletionComments+"</td>";
		reportBodyHtml += "<td>"+value.taskRecordedActivityStartTime+"</td></tr>";
	});
	
	$(".reportDataBody").html( reportBodyHtml );
}

function fillUserUtilizationReportData(data) {
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-utilization").clone();
	var headerHtml = $(reportHeader).html();
	
	$(".retportDataHeader").html("<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		reportBodyHtml += "<td>"+value.employeeName+"</td>";
		reportBodyHtml += "<td>"+value.empId+"</td>";
		
		if(value.reportingManager == null) {
			reportBodyHtml += "<td></td>";
		} else {
			reportBodyHtml += "<td>"+value.reportingManager+"</td>";
		}
		reportBodyHtml += "<td>"+value.billableHours+"</td>";
		reportBodyHtml += "<td>"+value.nonBillableHours+"</td>";
		if(parseInt(value.leaveOrHolidayMins) > 0) {
			reportBodyHtml += "<td><span style='color: brown;font-weight: bold;'>"+value.leaveOrHolidayHours+"</span></td>";
		} else {
			reportBodyHtml += "<td>"+value.leaveOrHolidayHours+"</td>";
		}
		
		reportBodyHtml += "<td>"+value.noOfWorkingDays+"</td>";
		
		if(value.underOrOverUtilized) {
			reportBodyHtml += "<td><span style='color:red;font-weight: bold;'>"+value.utilization+" %</span></td>";
		} else {
			reportBodyHtml += "<td>"+value.utilization+" %</td>";
		}
		
		reportBodyHtml += "<td>"+value.totalHours+"</td></tr>";
	});
	
	$(".reportDataBody").html( reportBodyHtml );
}

function fillUserUtilizationMonthlyReportData(data) {
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-utilization-monthly").clone();
	var headerHtml = $(reportHeader).html();
	var headerWeekList = "<td colspan='4' data-tableexport-colspan='3'></td>";
	$.each(jsonData.WEEK_LIST, function( index, weekValue ) {
		headerHtml +="<th>B</th>";
		headerHtml +="<th>NB</th>";
		headerHtml +="<th>L/H</th>";
		headerHtml +="<th>Total Hours</th>";
		
		headerWeekList += "<td colspan='4' style='font-weight:bold'>"+jsonData.DATE_LIST[weekValue]+"</td>";
	});
	
	$(".retportDataHeader").html("<tr class='tableexport-ignore'>" + headerWeekList + "</tr>" + "<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		reportBodyHtml += "<td>"+value.employeeName+"</td>";
		reportBodyHtml += "<td>"+value.empId+"</td>";
		if(value.reportingManager == null) {
			reportBodyHtml += "<td></td>";
		} else {
			reportBodyHtml += "<td>"+value.reportingManager+"</td>";
		}
		$.each(jsonData.WEEK_LIST, function( weekIndex, weekValue ) {
			if (value.userUtilizationMap[weekValue] != undefined) {
				if(parseInt(value.userUtilizationMap[weekValue].billableMins) > 0) {
					reportBodyHtml += "<td>"+value.userUtilizationMap[weekValue].billableHours+"</td>";
				} else {
					reportBodyHtml += "<td></td>";
				}
				if(parseInt(value.userUtilizationMap[weekValue].nonBillableMins) > 0) {
					reportBodyHtml += "<td>"+value.userUtilizationMap[weekValue].nonBillableHours+"</td>";
				} else {
					reportBodyHtml += "<td></td>";
				}
				if(parseInt(value.userUtilizationMap[weekValue].leaveOrHolidayMins) > 0) {
					reportBodyHtml += "<td><span style='color: brown;font-weight: bold;'>"+value.userUtilizationMap[weekValue].leaveOrHolidayHours+"</span></td>";
				} else {
					reportBodyHtml += "<td></td>";
				}
				if(parseInt(value.userUtilizationMap[weekValue].totalWithLeaveMins) > 0) {
					if(value.userUtilizationMap[weekValue].notifyUsers) {
						reportBodyHtml += "<td style='text-align: center;background-color: efefef;font-weight:bold'><span style='color: red;font-weight: bold;'>"+value.userUtilizationMap[weekValue].totalWithLeaveHrs+"</span></td>";
					} else {
						reportBodyHtml += "<td style='text-align: center;background-color: efefef;font-weight:bold'>"+value.userUtilizationMap[weekValue].totalWithLeaveHrs+"</td>";	
					}
				} else {
					reportBodyHtml += "<td style='text-align: center;background-color: efefef;font-weight:bold'><span style='color: red;font-weight: bold;'>00.00</span></td>";
				}
			} else {
				reportBodyHtml += "<td></td><td></td><td></td>";
				reportBodyHtml += "<td style='text-align: center;background-color: efefef;font-weight:bold'><span style='color: red;font-weight: bold;'>00.00</span></td>";
			}
		});
		reportBodyHtml += "</tr>";
		
	});
	
	$(".reportDataBody").html( reportBodyHtml );
}

function fillPOEstimationReportData(data) {
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-poestimation").clone();
	var headerHtml = $(reportHeader).html();
	
	$(".retportDataHeader").html("<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.clientName+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.orderBookId+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.proposalNumber+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.location+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.deliveryLeadName+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.teamName+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.accountName+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.projectName+"</td>";
		if(value.projectId != null) {
			reportBodyHtml += "<td style='text-align: center;'>"+value.projectId+"</td>";
		} else {
			reportBodyHtml += "<td style='text-align: center;'></td>";
		}
		
		if(value.projectCode != null) {
			reportBodyHtml += "<td style='text-align: center;'>"+value.projectCode+"</td>";
		} else {
			reportBodyHtml += "<td style='text-align: center;'></td>";
		}
		
		if(value.projectNumber != null) {
			reportBodyHtml += "<td style='text-align: center;'>"+value.projectNumber+"</td>";
		} else {
			reportBodyHtml += "<td style='text-align: center;'></td>";
		}
		
		if(value.sowLineItem != null) {
			reportBodyHtml += "<td style='text-align: center;'>"+value.sowLineItem+"</td>";
		} else {
			reportBodyHtml += "<td style='text-align: center;'></td>";
		}
		
		reportBodyHtml += "<td style='text-align: center;'>"+value.projectStartTimeFormated+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.projectCompletionTimeFormated+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.projectEstimateStatus+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.projectDurationHrs+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.taskActivityDurationHrs+"</td>";
		reportBodyHtml += "<td style='text-align: center;'>"+value.utilizationString+" %</td>";
		reportBodyHtml += "</tr>";
	});
	
	$(".reportDataBody").html( reportBodyHtml );

}

function fillProjectHoursReportData(data) {
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-projecthours").clone();
	var headerHtml = $(reportHeader).html();
	
	$(".retportDataHeader").html("<tr>" + headerHtml + "</tr>");
	
	var reportBodyHtml="";
	$.each(jsonData.DATA, function( index, value ) {
		var teamName = "";
		var accountName = "";
		var year ="";
		var month ="";
		var weekNumber = "";
		var clientName = "";
		var projectNumber ="";
		var projectName ="";
		var startDateString ="";
		var endDateString ="";
		var teamMembers = "";
		reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
		
		$.each(value.utilizationProjectDetailsList, function( projectIndex, projectValue ) {
			if (teamName != "" && projectValue.teamName != null && projectValue.teamName != ""){
				teamName += ", " +projectValue.teamName;
			} else if (projectValue.teamName != null && projectValue.teamName != ""){
				teamName += projectValue.teamName;
			}
			
			if (accountName != "" && projectValue.accountName != null && projectValue.accountName != ""){
				accountName += ", " +projectValue.accountName;
			} else if (projectValue.accountName != null && projectValue.accountName != ""){
				accountName += projectValue.accountName;
			}
			
			if (year != "" && projectValue.year != null && projectValue.year != ""){
				year += ", " +projectValue.year;
			} else {
				year += projectValue.year;
			}
			if (month != "" && projectValue.month != null && projectValue.month != ""){
				month += ", " +projectValue.month;
			} else {
				month += projectValue.month;
			}
			if (weekNumber != "" && projectValue.weekNumber != null && projectValue.weekNumber != ""){
				weekNumber += ", " +projectValue.weekNumber;
			} else {
				weekNumber += projectValue.weekNumber;
			}
			if (clientName != "" && projectValue.clientName != null && projectValue.clientName != ""){
				clientName += ", " +projectValue.clientName;
			} else if (projectValue.clientName != null && projectValue.clientName != ""){
				clientName += projectValue.clientName;
			}
			if (projectNumber != "" && projectValue.projectNumber != null && projectValue.projectNumber != ""){
				projectNumber += ", " +projectValue.projectNumber;
			} else if ( projectValue.projectNumber != null && projectValue.projectNumber != ""){
				projectNumber += projectValue.projectNumber;
			}
			if (projectName != "" && projectValue.projectName != null && projectValue.projectName != ""){
				projectName += ", " +projectValue.projectName;
			} else if (projectValue.projectName != null && projectValue.projectName != ""){
				projectName += projectValue.projectName;
			}
			if (startDateString != "" && projectValue.startDateString != null && projectValue.startDateString != ""){
				startDateString += ", " +projectValue.startDateString;
			} else if (projectValue.startDateString != null && projectValue.startDateString != ""){
				startDateString += projectValue.startDateString;
			}
			if (endDateString != "" && projectValue.endDateString != null && projectValue.endDateString != ""){
				endDateString += ", " +projectValue.endDateString;
			} else if (projectValue.endDateString != null && projectValue.endDateString != ""){
				endDateString += projectValue.endDateString;
			}
		});
		
		reportBodyHtml += "<td>"+teamName+"</td>";
		reportBodyHtml += "<td>"+accountName+"</td>";
		reportBodyHtml += "<td>"+(moment().year())+"</td>";
		reportBodyHtml += "<td>"+(moment().format('MMMM'))+"</td>";
		
		reportBodyHtml += "<td>"+ (moment().week())+"</td>";
		reportBodyHtml += "<td>"+clientName+"</td>";
		reportBodyHtml += "<td>"+projectNumber+"</td>";
		reportBodyHtml += "<td>"+projectName+"</td>";
		reportBodyHtml += "<td>"+startDateString+"</td>";
		reportBodyHtml += "<td>"+endDateString+"</td>";
		
		reportBodyHtml += "<td>"+value.teamMembers+"</td>";

		if (value.funded == null) {
			reportBodyHtml += "<td></td>";
		} else {
			reportBodyHtml += "<td>"+value.funded+"</td>";
		}
		reportBodyHtml += "<td>"+value.employeeName+"</td>";
		
		reportBodyHtml += "<td>"+value.estimatedHours+"</td>";
		reportBodyHtml += "<td>"+value.actualHours+"</td>";
		
		if(parseInt(value.leaveOrHolidayMins) > 0) {
			reportBodyHtml += "<td><span style='color: brown;font-weight: bold;'>"+value.leaveOrHolidayHours+"</span></td>";
		} else {
			reportBodyHtml += "<td>"+value.leaveOrHolidayHours+"</td>";
		}
		reportBodyHtml += "</tr>";
	});
	
	$(".reportDataBody").html( reportBodyHtml );
}

var serverDownload = true;

$(".exportButton").on("click", function(){
	if (serverDownload) {
		refreshReportDataOrDownload(true);
	} else {
		$(".reportDataTable button.xlsx").click();
	}
});

$(".downloadButton").on("click", function(){
	var dateString = $(".monthRangeSelector").val();
	var dateRange =	dateString.split(" - ");
	var startDate =  getDateObject("01-" +dateRange[0]);
	var endDate =  getDateObject("01-" +dateRange[1]);
	var endDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1, 0);
	var reportStartDate = formatCalenderDate(startDate);
	var reportEndDate= formatCalenderDate(endDate);
	var downloadXLSFileName ="Project_Details_Estimate_vs_Actual_" + dateString;
	 var teamIds ="";
	$('.teamIdsCheckbox input[type=checkbox]:checked').each(function () {
       teamIds += (this.checked ? $(this).val() : "") + ",";
  	}); 

	if(teamIds != "") {
		teamIds = teamIds.substring(0,teamIds.length - 1);
		window.location.href = fsApplicationHome + "/downloadProjectReportBySkill?reportStartDate=" + reportStartDate + "&reportEndDate=" + reportEndDate +"&fileName=" + downloadXLSFileName+"&teamIds=" + teamIds;
	}
});

function clearReportDataTable(){
	
	$(".exportButton").addClass("hide");
	 $("#reportSearchBoxId").addClass("hide");
	
	 if (dataTable != null) {
		 dataTable.destroy();
		 dataTable=null;
	 }
	 
	 if (exportTable != null) {
		 exportTable.remove();
		 exportTable = null;
	 }
	 
	 $(".reportDataTable .retportDataHeader").html("");
	 $(".reportDataTable .reportDataBody").html("");
	 
	var autoReportType = $(".autoReportType").val();
	if (autoReportType == "TEAM_UTILIZATION_CHART") {
		
	}
	
	$(".teamUtilizationChartDiv").addClass("hide");
	$(".teamUtilizationChartDataBody").html("");
	
	if (teamUtilizationChart != null) {
		teamUtilizationChart.destroy();
		teamUtilizationChart = null;
	}
	
}

var exportTable = null;
var dataTable = null;
function initializeExportTable(fileName) {
	exportTable = $(".reportDataTable").tableExport({
		headings: true,
	    footers: true,
	    formats: ["xlsx"],
	    fileName: fileName,
	    bootstrap: true,
	    position: "bottom",
	    ignoreRows: null,
	    ignoreCols: null,     
	    ignoreCSS: ".tableexport-ignore",
	    emptyCSS: ".tableexport-empty", 
	    trimWhitespace: false
	});
	
	if($.fn.DataTable.isDataTable( '.reportDataTable' )) {
		dataTable.destroy();
		dataTable =null;
	}	
	dataTable= $('.reportDataTable').DataTable({ 
	    responsive: false,
	    "pageLength": -1,
	    "ordering": false
	 });
}

$(document).ready(function(){
	$(".refreshButton").removeClass("hide");
});

$("#reportSearchBoxId").keydown(function(e){
	$("input[type='search']").val($("#reportSearchBoxId").val());
	$("input[type='search']").trigger(e);
});

$("#reportSearchBoxId").keyup(function(e){
	$("input[type='search']").val($("#reportSearchBoxId").val());
	$("input[type='search']").trigger(e);
});

var teamUtilizationChart = null;
var teamUtilizationComparisonChart = null;
function initializeTeamUtilizationChart(teamUtilizationData, dateRange){
	if (teamUtilizationData.length == 0) {
		return;
	}
	var titleText = "Team Utilization Tracker " + dateRange;
	if (teamUtilizationChart == null) {
		
		teamUtilizationChart = new CanvasJS.Chart("teamUtilizationChart", {
			theme: "light2", // "light2", "dark1", "dark2"
			animationEnabled: true, // change to true	
			exportEnabled: true,
			title:{
				text: titleText,
				fontWeight: "normal",
				fontSize: 20,
				fontFamily:"Arial"
			},
			axisY:{
				suffix: " %"
			},
			axisX:{
				labelFontSize:12,
				labelFontFamily:"Arial",
				labelFontWeight: "lighter"
			},
			data: [
			{
				type: "column",
				yValueFormatString: "# '%'",
				indexLabel: "{y}",
				dataPoints: teamUtilizationData
			}
			]
		});
	} else {
		teamUtilizationChart.options.title.text = titleText;
		teamUtilizationChart.options.data[0].dataPoints = teamUtilizationData; 
	}
	
	$(".teamUtlDataText").html("Team utilization data - " + dateRange);
	teamUtilizationChart.render();
}


function initializeTeamUtilizationComparisonChart(teamUtilizationComparisonChartData, dateRange, xAxisDateFormat){
	if (teamUtilizationComparisonChartData.length == 0) {
		return;
	}
	var titleText = "Team Utilization Yearly Comparison Chart (" + dateRange + ")";
	if (teamUtilizationComparisonChart == null) {
		
		teamUtilizationComparisonChart = new CanvasJS.Chart("teamUtilizationComparisonChart", {
			theme: "light1", // "light2", "dark1", "dark2"
			animationEnabled: true, // change to true	
			zoomEnabled: true,
			exportEnabled: true,
			title:{
				text: titleText,
				fontWeight: "normal",
				fontSize: 20,
				fontFamily:"Arial"	
			},
			axisY:{
				suffix: " %"
			},
			toolTip: {
				shared: true
			},
			legend: {
				cursor: "pointer",
				verticalAlign: "top",
				horizontalAlign: "center",
				dockInsidePlotArea: true,
			},
			axisX:{
				labelFontSize:12,
				labelFontFamily:"Arial",
				labelFontWeight: "lighter",
				valueFormatString: xAxisDateFormat
			},
			data: teamUtilizationComparisonChartData
		});
	} else {
		teamUtilizationComparisonChart.options.title.text = titleText;
		teamUtilizationComparisonChart.options.axisX.valueFormatString = xAxisDateFormat;
		teamUtilizationComparisonChart.options.data = teamUtilizationComparisonChartData; 
	}
	
	teamUtilizationComparisonChart.render();
}

function refreshTeamUtilizationChart(reportStartDate, reportEndDate){
	var groupIds = $("#teamUtilizationChartGroupIds").val();
	doAjaxRequestWithGlobal("GET", fsApplicationHome + "/getTeamUtilizationChartData",{"groupIds": groupIds,"reportStartDate":reportStartDate,"reportEndDate":reportEndDate} , function(data) {
		var dateRange = reportStartDate == reportEndDate ? reportEndDate : reportStartDate +" - " + reportEndDate;
		initializeTeamUtilizationChart(getUtilizationData(data), dateRange);
		fillTeamUtilizationTableData(data);
	 }, function(e) {
	}, false);
	if (teamUtilizationComparisonChart == null){
		refreshTeamUtilizationComparisonChart();
	}
}

function refreshTeamUtilizationComparisonChart(){
	var groupIds = $("#teamUtilizationChartGroupIds").val();
	var displayWiseSelect = $(".displayWiseSelect").val();
	doAjaxRequestWithGlobal("GET", fsApplicationHome + "/getTeamUtilizationComparisonChartData",{"groupIds": groupIds,"year":$(".yearSelect").val(),"displayWise":displayWiseSelect} , function(data) {
		initializeTeamUtilizationComparisonChart(getUtilizationComparisonData(data, displayWiseSelect=="DateWise"?true:false), $(".yearSelect").val(), displayWiseSelect=="DateWise"? "DD MMM YYYY": "MMM YYYY");
	 }, function(e) {
	}, false);
}

function fillTeamUtilizationTableData(data){
	$(".teamUtilizationChartDataBody").html("");
	var jsonData = JSON.parse(data);
	var index = 0;
	var reportBodyHtml = "";
	$.each(jsonData, function(key, value) {
	   var noOfWorkingMins = (value.NUMBEROFWORKINGDAYS * 8 * 60);
		
	   var billableHours = roundToTwo(value.BILLABLE/60);
	   var nonBillableHours = roundToTwo(value.NONBILLABLE/60);
	   var holidayHours = roundToTwo(value.HOLIDAY/60);
	   var leaveHours = roundToTwo(value.LEAVE/60);
	   var noOfWorkingDyas = value.NUMBEROFWORKINGDAYS;
	   var noOfEmps = value.NUMBEROFEMPS;
	   
	   var totalTaskMins = value.BILLABLE + value.NONBILLABLE;
	   var totalMins =(noOfWorkingMins * noOfEmps) - value.HOLIDAY - value.LEAVE;
	   var totalTaskHours = roundToTwo(totalTaskMins/60);
	   var totalHours = roundToTwo(totalMins/60);
	   
	   var utilizationPercentage = 0;
	   if (totalMins > 0) {
		   utilizationPercentage = roundToTwo((totalTaskMins/totalMins) * 100);
	   }
	   
	   reportBodyHtml += "<tr><td>"+(index+1)+"</td>";
	   reportBodyHtml += "<td style='font-weight: bold;'>"+key+"</td>";
	   reportBodyHtml += "<td>"+billableHours+"</td>";
	   reportBodyHtml += "<td>"+nonBillableHours+"</td>";
	   reportBodyHtml += "<td>"+leaveHours+"</td>";
	   reportBodyHtml += "<td>"+holidayHours+"</td>";
	   reportBodyHtml += "<td style='font-weight: bold;background-color: #eee;'>"+totalTaskHours+"</td>";
	   reportBodyHtml += "<td>"+noOfWorkingDyas+"</td>";
	   reportBodyHtml += "<td>"+noOfEmps+"</td>";
	   reportBodyHtml += "<td style='font-weight: bold;background-color: #eee;'>"+totalHours+"</td>";
	   reportBodyHtml += "<td style='font-weight: bold;background-color: #ccc;'>"+utilizationPercentage+" %</td></tr>";
	   index +=1;
	});
	
	$(".teamUtilizationChartDataBody").html(reportBodyHtml);
}

function getUtilizationData(data){
	var jsonData = JSON.parse(data);
	taskUtilizationData = [];
	$.each(jsonData, function(key, value) {
		
	   var billableMins = value.BILLABLE;
	   var nonBillableMins = value.NONBILLABLE;
	   var noOfWorkingMins = (value.NUMBEROFWORKINGDAYS * 8 * 60);
	   var holidayMins = value.HOLIDAY;
	   var leaveMins = value.LEAVE;
	   var noOfEmps = value.NUMBEROFEMPS;
		
	   var totalTaskMins = billableMins + nonBillableMins;
	   var totalMins =(noOfWorkingMins * noOfEmps) - holidayMins - leaveMins;
	   var utilizationPercentage = 0;
	   if (totalMins > 0) {
		   utilizationPercentage = (totalTaskMins/totalMins) * 100;
	   }
	   
	   item = {};
	   item['y'] = utilizationPercentage;
	   item['label'] = key;
	   item['USERGROUPID']=value.USERGROUPID;
	   taskUtilizationData.push(item);
	});
	
	return taskUtilizationData;
}

function getUtilizationComparisonData(data,isDateWise){
	var jsonData = JSON.parse(data);
	var taskUtilizationComparisonData = [];
	
	$.each(jsonData, function(key, yearDataList) {
		var dataPoints = [];
		$.each(yearDataList, function(index, value) {
		   var noOfWorkingMins = (value.NUMBEROFWORKINGDAYS * 8 * 60);
			
		   var totalTaskMins = value.BILLABLE + value.NONBILLABLE;
		   var totalMins =(noOfWorkingMins * value.NUMBEROFEMPS) - value.LEAVE - value.HOLIDAY;
		   var utilizationPercentage = 0;
		   if (totalMins > 0) {
			   utilizationPercentage = (totalTaskMins/totalMins) * 100;
		   }
		  
		   
		   var dataPoint = {};
		   dataPoint['y'] = utilizationPercentage;
		   var dateString = '1';
		   if(isDateWise) {
			   dateString = value.DATE;
		   }
		   dataPoint['x'] = new Date(value.YEAR, parseInt(value.MONTH)-1, dateString);
		   dataPoint['USERGROUPID']=value.USERGROUPID;
		   dataPoints.push(dataPoint);
		});
		var item ={};
		item['type']='line';
		item['axisYType']='primary';
		item['name']=key;
		item['showInLegend']=true;
		item['markerSize']=0;
		item['yValueFormatString']= "# ' %'";
		item['dataPoints']= dataPoints;
		
		taskUtilizationComparisonData.push(item);
	});
	
	return taskUtilizationComparisonData;
}

$(function() {
  $('input[name="monthRangeSelector"]').daterangepicker({
    timePicker: false,
       
        autoclose: true,
        locale: {
	 		calendarMode : 'month',
			format: 'MMM-YYYY',
            applyLabel: 'Apply Date',
            fromLabel: 'First Date',
            toLabel: 'Second Date',
            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            firstDay: 1
        }
  });
});

$(function() {
	  $('input[name="monthDateRangeSelector"]').daterangepicker({
	    timePicker: false,
	       
	        autoclose: true,
	        locale: {
		 		calendarMode : 'month',
				format: 'MMM-YYYY',
	            applyLabel: 'Apply Date',
	            fromLabel: 'First Date',
	            toLabel: 'Second Date',
	            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
	            firstDay: 1
	        }
	  });
	});