function formatDate(date) {
	var monthNames = [
	            	    "Jan", "Feb", "Mar",
	            	    "Apr", "May", "Jun", "Jul",
	            	    "Aug", "Sep", "Oct",
	            	    "Nov", "Dec"
	            	  ];
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
	var monthNames = [
	            	    "Jan", "Feb", "Mar",
	            	    "Apr", "May", "Jun", "Jul",
	            	    "Aug", "Sep", "Oct",
	            	    "Nov", "Dec"
	            	  ];
	  var monthIndex = date.getMonth();
	  var year = date.getFullYear();
	  var dateformatedString =  monthNames[monthIndex] + '-' + year;
	  
	  return dateformatedString;
	}

function moveToPrevious(){
	 var date =  new Date($(".weekSelector").val());
	 date.setDate(date.getDate() - 7);
	 $('.weekSelector').val(formatDate(date));
	 
	 var date =  new Date($(".dailySelector").val());
	 date.setDate(date.getDate() - 1);
	 $('.dailySelector').val(formatDate(date));
	 
	 var date =  new Date($(".monthSelector").val());
	 date.setMonth(date.getMonth() - 1);
	 $('.monthSelector').val(formatMonthDate(date));
	 
	 checkValidDate();
	 refreshReportDataOrDownload(false);
}

function moveToNext(){
	 var date =  new Date($(".weekSelector").val());
	 date.setDate(date.getDate() + 7);
	 $('.weekSelector').val(formatDate(date));
	 
	 var date =  new Date($(".dailySelector").val());
	 date.setDate(date.getDate() + 1);
	 $('.dailySelector').val(formatDate(date));
	 
	 var date =  new Date($(".monthSelector").val());
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
	var date = new Date($(".monthSelector").val());
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
	var date = new Date($(".weekSelector").val());
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
	var date = new Date($(".monthSelector").val());
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
	 $('.autoReportDuration').append($('<option>').val("MONTHLY_ENE").text("Monthly End to End"));

	 clearReportDataTable();
	 
	 var selectedValue = $(this).val();
	 if("USER_SITE_ACTIVITY" == selectedValue) {
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
	 } else  if("USER_UTILIZATION" == selectedValue) {
		 
	 } else  if("WEEKLY_PO_ESTIMATION" == selectedValue) {
		 $(".autoReportDuration option[value='DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
	 } else  if("WEEKLY_PROJECT_HOURS" == selectedValue) {
		 $(".autoReportDuration option[value='DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY']").remove();
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
	 } else  if("TEAM_UTILIZATION_CHART" == selectedValue) {
		 $(".autoReportDuration option[value='MONTHLY_ENE']").remove();
	 }
	 $(".autoReportDuration").trigger("change");
});

$(".autoReportDuration").on("change",function(){
	 clearReportDataTableOnDateChange();
	 
	 var selectedValue = $(this).val();
	 $('.monthSelector').addClass("hide");
	 $(".weekSelector").addClass("hide");
	 $(".dailySelector").addClass("hide");
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
	}
	var autoReportDuration = $(".autoReportDuration").val();
	var autoReportType = $(".autoReportType").val();
	var startDate = "";
	var endDate = "";
	 if("DAILY" == autoReportDuration) {
		var dayDate = $(".dailySelector").val();
		startDate = dayDate;
		endDate = dayDate;
	 } else if ("WEEKLY" == autoReportDuration) {
		 var weekStartDate =  $(".weekSelector").val();
		 var weekEndDate =  new Date(weekStartDate);
		 weekEndDate.setDate(weekEndDate.getDate() + 6);
		 startDate = weekStartDate;
		 endDate = weekEndDate;
	 } else if ("WEEKLY_DAILY" == autoReportDuration) {
		 var weekStartDate =  $(".weekSelector").val();
		 var weekEndDate =  new Date(weekStartDate);
		 weekEndDate.setDate(weekEndDate.getDate() + 6);
		 startDate = weekStartDate;
		 endDate = weekEndDate;
	 } else if ("MONTHLY" == autoReportDuration) {
		 var monthDate =  new Date("01-" + $('.monthSelector').val());
		 var firstDayOfMonth = new Date(monthDate.getFullYear(), monthDate.getMonth(), 1);
		 var lastDayOfMonth = new Date(monthDate.getFullYear(), monthDate.getMonth() + 1, 0);
		 var firstMondayOfMonth = getLastMonday(firstDayOfMonth);
		 var lastSundayOfMonth = getLastSundayOfMonth(lastDayOfMonth);
		 if ("USER_UTILIZATION" == autoReportType) {
			 startDate = firstMondayOfMonth;
			 endDate = lastSundayOfMonth;
		 } else {
			 startDate = firstDayOfMonth;
			 endDate = lastDayOfMonth;
		 }
	 } else if ("MONTHLY_ENE" == autoReportDuration) {
		 var firstDayOfMonth =  new Date("01-" + $('.monthSelector').val());
		 var lastDayOfMonth = new Date(firstDayOfMonth.getFullYear(), firstDayOfMonth.getMonth() + 1, 0);
		 startDate = firstDayOfMonth;
		 endDate = lastDayOfMonth;
	 }
	 
	 var reportStartDate = formatCalenderDate(new Date(startDate));
	 var reportEndDate = formatCalenderDate(new Date(endDate));
	 
	 var reportType = autoReportType;
	 if("USER_SITE_ACTIVITY" == autoReportType) {
	 } else  if("USER_UTILIZATION" == autoReportType) {
		 if ("MONTHLY" == autoReportDuration) {
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

function fillUserUtilizationReportData(data) {
	var jsonData = JSON.parse(data);
	var reportHeader = $(".reportDataTemplate .reportDataHeader-utilization").clone();
	var headerHtml = $(reportHeader).html();
	
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
		reportBodyHtml += "<td>"+value.billableHours+"</td>";
		reportBodyHtml += "<td>"+value.nonBillableHours+"</td>";
		if(parseInt(value.leaveOrHolidayMins) > 0) {
			reportBodyHtml += "<td><span style='color: brown;font-weight: bold;'>"+value.leaveOrHolidayHours+"</span></td>";
		} else {
			reportBodyHtml += "<td>"+value.leaveOrHolidayHours+"</td>";
		}
		
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
	var headerWeekList = "<td colspan='3' data-tableexport-colspan='3'></td>";
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
		
		if(value.reportingManager == null) {
			reportBodyHtml += "<td></td>";
		} else {
			reportBodyHtml += "<td>"+value.reportingManager+"</td>";
		}
		$.each(jsonData.WEEK_LIST, function( weekIndex, weekValue ) {
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
	   var totalMins =(noOfWorkingMins * noOfEmps) - (value.HOLIDAY - value.LEAVE);
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
	   var totalMins =(noOfWorkingMins * noOfEmps) - (holidayMins - leaveMins);
	   var utilizationPercentage = 0;
	   if (totalMins > 0) {
		   utilizationPercentage = (totalTaskMins/totalMins) * 100;
	   }
	   
	   item = {};
	   item['y'] = utilizationPercentage;
	   item['label'] = key;
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
		   var totalMins =(noOfWorkingMins * value.NUMBEROFEMPS) - (value.HOLIDAY - value.LEAVE);
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

var teamUtilizationComparisonChartData =[{
	type:"line",
	axisYType: "primary",
	name: "San Fransisco",
	showInLegend: true,
	markerSize: 0,
	yValueFormatString: "# ' %'",
	dataPoints: [		
		{ x: new Date(2014, 00, 01), y: 850 },
		{ x: new Date(2014, 01, 01), y: 889 },
		{ x: new Date(2014, 02, 01), y: 890 },
		{ x: new Date(2014, 03, 01), y: 899 },
		{ x: new Date(2014, 04, 01), y: 903 },
		{ x: new Date(2014, 05, 01), y: 925 },
		{ x: new Date(2014, 06, 01), y: 899 },
		{ x: new Date(2014, 07, 01), y: 875 },
		{ x: new Date(2014, 08, 01), y: 927 },
		{ x: new Date(2014, 09, 01), y: 949 },
		{ x: new Date(2014, 10, 01), y: 946 },
		{ x: new Date(2014, 11, 01), y: 927 },
		{ x: new Date(2015, 00, 01), y: 950 },
		{ x: new Date(2015, 01, 01), y: 998 },
		{ x: new Date(2015, 02, 01), y: 998 },
		{ x: new Date(2015, 03, 01), y: 1050 },
		{ x: new Date(2015, 04, 01), y: 1050 },
		{ x: new Date(2015, 05, 01), y: 999 },
		{ x: new Date(2015, 06, 01), y: 998 },
		{ x: new Date(2015, 07, 01), y: 998 },
		{ x: new Date(2015, 08, 01), y: 1050 },
		{ x: new Date(2015, 09, 01), y: 1070 },
		{ x: new Date(2015, 10, 01), y: 1050 },
		{ x: new Date(2015, 11, 01), y: 1050 },
		{ x: new Date(2016, 00, 01), y: 995 },
		{ x: new Date(2016, 01, 01), y: 1090 },
		{ x: new Date(2016, 02, 01), y: 1100 },
		{ x: new Date(2016, 03, 01), y: 1150 },
		{ x: new Date(2016, 04, 01), y: 1150 },
		{ x: new Date(2016, 05, 01), y: 1150 },
		{ x: new Date(2016, 06, 01), y: 1100 },
		{ x: new Date(2016, 07, 01), y: 1100 },
		{ x: new Date(2016, 08, 01), y: 1150 },
		{ x: new Date(2016, 09, 01), y: 1170 },
		{ x: new Date(2016, 10, 01), y: 1150 },
		{ x: new Date(2016, 11, 01), y: 1150 },
		{ x: new Date(2017, 00, 01), y: 1150 },
		{ x: new Date(2017, 01, 01), y: 1200 },
		{ x: new Date(2017, 02, 01), y: 1200 },
		{ x: new Date(2017, 03, 01), y: 1200 },
		{ x: new Date(2017, 04, 01), y: 1190 },
		{ x: new Date(2017, 05, 01), y: 1170 }
	]
},
{
	type: "line",
	axisYType: "primary",
	name: "Manhattan",
	showInLegend: true,
	markerSize: 0,
	yValueFormatString: "$#,###k",
	dataPoints: [
		{ x: new Date(2014, 00, 01), y: 1200 },
		{ x: new Date(2014, 01, 01), y: 1200 },
		{ x: new Date(2014, 02, 01), y: 1190 },
		{ x: new Date(2014, 03, 01), y: 1180 },
		{ x: new Date(2014, 04, 01), y: 1250 },
		{ x: new Date(2014, 05, 01), y: 1270 },
		{ x: new Date(2014, 06, 01), y: 1300 },
		{ x: new Date(2014, 07, 01), y: 1300 },
		{ x: new Date(2014, 08, 01), y: 1358 },
		{ x: new Date(2014, 09, 01), y: 1410 },
		{ x: new Date(2014, 10, 01), y: 1480 },
		{ x: new Date(2014, 11, 01), y: 1500 },
		{ x: new Date(2015, 00, 01), y: 1500 },
		{ x: new Date(2015, 01, 01), y: 1550 },
		{ x: new Date(2015, 02, 01), y: 1550 },
		{ x: new Date(2015, 03, 01), y: 1590 },
		{ x: new Date(2015, 04, 01), y: 1600 },
		{ x: new Date(2015, 05, 01), y: 1590 },
		{ x: new Date(2015, 06, 01), y: 1590 },
		{ x: new Date(2015, 07, 01), y: 1620 },
		{ x: new Date(2015, 08, 01), y: 1670 },
		{ x: new Date(2015, 09, 01), y: 1720 },
		{ x: new Date(2015, 10, 01), y: 1750 },
		{ x: new Date(2015, 11, 01), y: 1820 },
		{ x: new Date(2016, 00, 01), y: 2000 },
		{ x: new Date(2016, 01, 01), y: 1920 },
		{ x: new Date(2016, 02, 01), y: 1750 },
		{ x: new Date(2016, 03, 01), y: 1850 },
		{ x: new Date(2016, 04, 01), y: 1750 },
		{ x: new Date(2016, 05, 01), y: 1730 },
		{ x: new Date(2016, 06, 01), y: 1700 },
		{ x: new Date(2016, 07, 01), y: 1730 },
		{ x: new Date(2016, 08, 01), y: 1720 },
		{ x: new Date(2016, 09, 01), y: 1740 },
		{ x: new Date(2016, 10, 01), y: 1750 },
		{ x: new Date(2016, 11, 01), y: 1750 },
		{ x: new Date(2017, 00, 01), y: 1750 },
		{ x: new Date(2017, 01, 01), y: 1770 },
		{ x: new Date(2017, 02, 01), y: 1750 },
		{ x: new Date(2017, 03, 01), y: 1750 },
		{ x: new Date(2017, 04, 01), y: 1730 },
		{ x: new Date(2017, 05, 01), y: 1730 }
	]
},
{
	type: "line",
	axisYType: "primary",
	name: "Seatle",
	showInLegend: true,
	markerSize: 0,
	yValueFormatString: "$#,###k",
	dataPoints: [
		{ x: new Date(2014, 00, 01), y: 409 },
		{ x: new Date(2014, 01, 01), y: 415 },
		{ x: new Date(2014, 02, 01), y: 419 },
		{ x: new Date(2014, 03, 01), y: 429 },
		{ x: new Date(2014, 04, 01), y: 429 },
		{ x: new Date(2014, 05, 01), y: 450 },
		{ x: new Date(2014, 06, 01), y: 450 },
		{ x: new Date(2014, 07, 01), y: 445 },
		{ x: new Date(2014, 08, 01), y: 450 },
		{ x: new Date(2014, 09, 01), y: 450 },
		{ x: new Date(2014, 10, 01), y: 440 },
		{ x: new Date(2014, 11, 01), y: 429 },
		{ x: new Date(2015, 00, 01), y: 435 },
		{ x: new Date(2015, 01, 01), y: 450 },
		{ x: new Date(2015, 02, 01), y: 475 },
		{ x: new Date(2015, 03, 01), y: 475 },
		{ x: new Date(2015, 04, 01), y: 475 },
		{ x: new Date(2015, 05, 01), y: 489 },
		{ x: new Date(2015, 06, 01), y: 495 },
		{ x: new Date(2015, 07, 01), y: 495 },
		{ x: new Date(2015, 08, 01), y: 500 },
		{ x: new Date(2015, 09, 01), y: 508 },
		{ x: new Date(2015, 10, 01), y: 520 },
		{ x: new Date(2015, 11, 01), y: 525 },
		{ x: new Date(2016, 00, 01), y: 525 },
		{ x: new Date(2016, 01, 01), y: 529 },
		{ x: new Date(2016, 02, 01), y: 549 },
		{ x: new Date(2016, 03, 01), y: 550 },
		{ x: new Date(2016, 04, 01), y: 568 },
		{ x: new Date(2016, 05, 01), y: 575 },
		{ x: new Date(2016, 06, 01), y: 579 },
		{ x: new Date(2016, 07, 01), y: 575 },
		{ x: new Date(2016, 08, 01), y: 585 },
		{ x: new Date(2016, 09, 01), y: 589 },
		{ x: new Date(2016, 10, 01), y: 595 },
		{ x: new Date(2016, 11, 01), y: 595 },
		{ x: new Date(2017, 00, 01), y: 595 },
		{ x: new Date(2017, 01, 01), y: 600 },
		{ x: new Date(2017, 02, 01), y: 624 },
		{ x: new Date(2017, 03, 01), y: 635 },
		{ x: new Date(2017, 04, 01), y: 650 },
		{ x: new Date(2017, 05, 01), y: 675 }
	]
},
{
	type: "line",
	axisYType: "primary",
	name: "Los Angeles",
	showInLegend: true,
	markerSize: 0,
	yValueFormatString: "$#,###k",
	dataPoints: [
		{ x: new Date(2014, 00, 01), y: 529 },
		{ x: new Date(2014, 01, 01), y: 540 },
		{ x: new Date(2014, 02, 01), y: 539 },
		{ x: new Date(2014, 03, 01), y: 565 },
		{ x: new Date(2014, 04, 01), y: 575 },
		{ x: new Date(2014, 05, 01), y: 579 },
		{ x: new Date(2014, 06, 01), y: 589 },
		{ x: new Date(2014, 07, 01), y: 579 },
		{ x: new Date(2014, 08, 01), y: 579 },
		{ x: new Date(2014, 09, 01), y: 579 },
		{ x: new Date(2014, 10, 01), y: 569 },
		{ x: new Date(2014, 11, 01), y: 525 },
		{ x: new Date(2015, 00, 01), y: 535 },
		{ x: new Date(2015, 01, 01), y: 575 },
		{ x: new Date(2015, 02, 01), y: 599 },
		{ x: new Date(2015, 03, 01), y: 619 },
		{ x: new Date(2015, 04, 01), y: 639 },
		{ x: new Date(2015, 05, 01), y: 648 },
		{ x: new Date(2015, 06, 01), y: 640 },
		{ x: new Date(2015, 07, 01), y: 645 },
		{ x: new Date(2015, 08, 01), y: 648 },
		{ x: new Date(2015, 09, 01), y: 649 },
		{ x: new Date(2015, 10, 01), y: 649 },
		{ x: new Date(2015, 11, 01), y: 649 },
		{ x: new Date(2016, 00, 01), y: 650 },
		{ x: new Date(2016, 01, 01), y: 665 },
		{ x: new Date(2016, 02, 01), y: 675 },
		{ x: new Date(2016, 03, 01), y: 695 },
		{ x: new Date(2016, 04, 01), y: 690 },
		{ x: new Date(2016, 05, 01), y: 699 },
		{ x: new Date(2016, 06, 01), y: 699 },
		{ x: new Date(2016, 07, 01), y: 699 },
		{ x: new Date(2016, 08, 01), y: 699 },
		{ x: new Date(2016, 09, 01), y: 699 },
		{ x: new Date(2016, 10, 01), y: 709 },
		{ x: new Date(2016, 11, 01), y: 699 },
		{ x: new Date(2017, 00, 01), y: 700 },
		{ x: new Date(2017, 01, 01), y: 700 },
		{ x: new Date(2017, 02, 01), y: 724 },
		{ x: new Date(2017, 03, 01), y: 739 },
		{ x: new Date(2017, 04, 01), y: 749 },
		{ x: new Date(2017, 05, 01), y: 740 }
	]
}];