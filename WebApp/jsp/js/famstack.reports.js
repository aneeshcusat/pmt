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
}

function moveToCurrent(){
	var date = new Date(getLastMonday(new Date()));
	$('.weekSelector').val(formatDate(date));
	
	 var date =  new Date();
	 $('.dailySelector').val(formatDate(date));
	 
	 var date =  new Date();
	 $('.monthSelector').val(formatMonthDate(date));
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

$(document).ready(function(){
$('.dailySelector').val(formatDate(new Date()));
$('.dailySelector').datepicker({ 
	defaultViewDate:getLastMonday(new Date()),
	defaultDate:getLastMonday(new Date()),
	defaultDate:new Date(),
	daysOfWeekHighlighted:1,
	format: 'dd-M-yyyy',
	startDate:new Date(),
	autoclose:true
}).on('changeDate', function(e) {
	var date = new Date($(".monthSelector").val());
	clearReportDataTable();
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
	clearReportDataTable();
});;

$('.monthSelector').val(formatMonthDate(getFirstDayOfMonth(new Date())));
$('.monthSelector').datepicker({ 
	defaultDate:getFirstDayOfMonth(new Date()),
	viewMode:1,
	minViewMode:1,
	daysOfWeekHighlighted:1,
	format: 'M-yyyy',
	startDate:getFirstDayOfMonth(new Date()),
	autoclose:true
}).on('changeDate', function(e) {
	var date = new Date($(".monthSelector").val());
	clearReportDataTable();
});;
});

function getFirstDayOfMonth(date){
	return new Date(date.getFullYear(), date.getMonth(), 1);
}

$(".autoReportType").on("change",function(){
	 $(".autoReportDuration option").remove();
	 $('.autoReportDuration').append($('<option>').val("DAILY").text("Daily"));
	 $('.autoReportDuration').append($('<option>').val("WEEKLY").text("Weekly"));
	 $('.autoReportDuration').append($('<option>').val("WEEKLY_DAILY").text("Weekly Day wise"));
	 $('.autoReportDuration').append($('<option>').val("MONTHLY").text("Monthly"));

	 clearReportDataTable();
	 
	 var selectedValue = $(this).val();
	 if("USER_SITE_ACTIVITY" == selectedValue) {
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
	 } else  if("USER_UTILIZATION" == selectedValue) {
		 
	 } else  if("WEEKLY_PO_ESTIMATION" == selectedValue) {
		 $(".autoReportDuration option[value='DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY']").remove();
	 } else  if("WEEKLY_PROJECT_HOURS" == selectedValue) {
		 $(".autoReportDuration option[value='DAILY']").remove();
		 $(".autoReportDuration option[value='WEEKLY_DAILY']").remove();
		 $(".autoReportDuration option[value='MONTHLY']").remove();
	 }
	 $(".autoReportDuration").trigger("change");
});

$(".autoReportDuration").on("change",function(){
	 clearReportDataTable();
	 
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
	 }
});


$(".refreshButton").on("click", refreshReportData);

function refreshReportData() {
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
		 }
	 } else  if("WEEKLY_PO_ESTIMATION" == autoReportType) {
	 } else  if("WEEKLY_PROJECT_HOURS" == autoReportType) {
	 }
	 
	 doAjaxRequestWithGlobal("GET", fsApplicationHome + "/getReportData",{"reportType":reportType,"reportStartDate":reportStartDate,"reportEndDate":reportEndDate} , function(data) {
		fillReportTableData(data, reportType, autoReportDuration);
	}, function(e) {
	}, false);
}


function fillReportTableData(data, reportType,autoReportDuration){
	var fileName = "";
	var jsonData = JSON.parse(data);
	 if("USER_SITE_ACTIVITY" == reportType) {
		 fillUserActivityReportData(data);
		 fileName = "User Site Activity Report";
	 } else  if("USER_UTILIZATION" == reportType) {
		 fillUserUtilizationReportData(data);
		 fileName = "User Utilization Report";
	 } else  if("WEEKWISE_USER_UTILIZATION_MONTHLY" == reportType) {
		 fillUserUtilizationMonthlyReportData(data);
		 fileName = "User Montly Utilization Report";
	 } else  if("DAILYWISE_USER_UTILIZATION_WEEKLY" == reportType) {
		 
		 
		 fileName = "User Weekly Utilization Report";
	 } else  if("WEEKLY_PO_ESTIMATION" == reportType) {
		 fillPOEstimationReportData(data);
		 fileName = "PO Estimation Report";
	 } else  if("WEEKLY_PROJECT_HOURS" == reportType) {
		 
		 
		 fileName = "Weekly Project Hours Report";
	 }
	 
	 fileName = fileName + "_" + jsonData.REPORT_DATE;
	 
	 initializeExportTable(fileName);
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
		if(parseInt(value.leaveHours) > 0) {
			reportBodyHtml += "<td><span style='color: brown;font-weight: bold;'>"+value.leaveOrHoliday+"</span></td>";
		} else {
			reportBodyHtml += "<td>"+value.leaveOrHoliday+"</td>";
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
			if(parseInt(value.userUtilizationMap[weekValue].leaveHours) > 0) {
				reportBodyHtml += "<td><span style='color: brown;font-weight: bold;'>"+value.userUtilizationMap[weekValue].leaveOrHoliday+"</span></td>";
			} else {
				reportBodyHtml += "<td></td>";
			}
			if(parseInt(value.userUtilizationMap[weekValue].totalWithLeave) > 0) {
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

$(".exportButton").on("click", function(){$(".reportDataTable button.xlsx").click();;});

function clearReportDataTable(){
	 $(".reportDataTable .retportDataHeader").html("");
	 $(".reportDataTable .reportDataBody").html("");
	 if (exportTable != null) {
		 exportTable.remove();
	 }
}

var exportTable = null;
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
}