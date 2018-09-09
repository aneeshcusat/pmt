//

//Filters
$(".dashboadgroup").on("change", function(){
	$(".accountInfo option:not([class*='hide'])").addClass("hide");
	$(".accountInfo option:first").removeClass("hide");
	$(".accountInfo option:nth-child(2)").removeClass("hide");
	$(".accountInfo option.UG"+$(this).val()).removeClass("hide");
	$(".accountInfo").prop("selectedIndex",0);
	
	$(".resourceInfo option:not([class*='hide'])").addClass("hide");
	$(".resourceInfo option:first").removeClass("hide");
	$(".resourceInfo option.UG"+$(this).val()).removeClass("hide");
	$(".resourceInfo").prop("selectedIndex",0);

	$( ".accountInfo" ).trigger( "change" );

	checkResourceChange();
	refreshEmployeeDetails();
	refreshProjectDetails();
	reinitializeCalenders();
	
	refreshAllCompare();
	
	$(".dbcheckbox:not([class*='hide'])").addClass("hide");
	$('.dbcheckbox input:checkbox').removeAttr('checked');
	$(".dashboadaccountcompare .dbcheckbox.UG"+$(this).val()).removeClass("hide");
	$(".dbrescompare .dbcheckbox.UG"+$(this).val()).removeClass("hide");
});

$(".accountInfo").on("change", function(){
	$(".projectTeamInfo option:not([class*='hide'])").addClass("hide");
	$(".projectTeamInfo option:first").removeClass("hide");
	$(".projectTeamInfo option:nth-child(2)").removeClass("hide");
	$(".projectTeamInfo option.AC"+$(this).val()).removeClass("hide");
	$(".projectTeamInfo").prop("selectedIndex",0);
	$(".projectTeamInfo" ).trigger( "change" );
});

$(".projectTeamInfo").on("change", function(){
	$(".projectSubTeamInfo option:not([class*='hide'])").addClass("hide");
	$(".projectSubTeamInfo option:first").removeClass("hide");
	$(".projectSubTeamInfo option:nth-child(2)").removeClass("hide");
	$(".projectSubTeamInfo option.PT"+$(this).val()).removeClass("hide");
	$(".projectSubTeamInfo").prop("selectedIndex",0);
	$(".projectSubTeamInfo" ).trigger( "change" );
});

$(".projectSubTeamInfo").on("change", function(){
	filterProjectDetails(true);
});

$(".resourceInfo").on("change", function(){
	filterProjectDetails(true);
	checkResourceChange();	
});

$(".acccbutfilter").on("click",function(){
	var itemChecked = $(this).is(':checked');
	var itemCheckedValue = $(this).val();
	if (itemChecked){
		$(".dbptcompare .ACCB"+itemCheckedValue).removeClass("hide");
	} else {
		$(".dbptcompare .ACCB"+itemCheckedValue).addClass("hide");
	}
	refreshAllCompare();
});

$(".ptcbutfilter").on("click",function(){
	var itemChecked = $(this).is(':checked');
	var itemCheckedValue = $(this).val();
	if (itemChecked){
		$(".dbstcompare .PTCB"+itemCheckedValue).removeClass("hide");
	} else {
		$(".dbstcompare .PTCB"+itemCheckedValue).addClass("hide");
	}
	refreshAllCompare();
});

$(".stcbutfilter").on("click",function(){
	refreshAllCompare();
});

$(".rescbutfilter").on("click",function(){
	refreshResourceCompareChart();
});

$(".dbCompareUtilization").on("change",function(){
	refreshAllCompare();
});

function checkResourceChange(){
	if ($(".resourceInfo").prop("selectedIndex") > 0) {
		$(".accountDivWidget").removeClass("col-md-4");
		$(".accountDivWidget").addClass("col-md-10");
		$(".resourceUtilDivWidget").removeClass("col-md-6");
		$(".resourceUtilDivWidget").hide();
	} else {
		$(".accountDivWidget").removeClass("col-md-10");
		$(".accountDivWidget").addClass("col-md-4");
		$(".resourceUtilDivWidget").addClass("col-md-6");
		$(".resourceUtilDivWidget").show();
	}
}

$("#dashboarddatepicker").daterangepicker({                    
    ranges: filterDateMap,
    opens: 'left',
    buttonClasses: ['btn btn-default'],
    applyClass: 'btn-small btn-primary',
    cancelClass: 'btn-small',
    format: 'MM.DD.YYYY',
    separator: ' to ',
    startDate: filterDateMap['Today'][0],
    endDate: filterDateMap['Today'][1]          
  },function(start, end) {
	  $("#daterangeText").val(start.format('MM/DD/YYYY') + ' - ' + end.format('MM/DD/YYYY'));
	  $("#dashboarddatepicker span").html($("#daterangeText").val());
	  refreshProjectDetails();
	  filterProjectDetails(true);
	  fillDateBackGroupInDatePicker();
	  refreshAllCompare();
});

function filterProjectDetails(isAdmin){

	$(".projectDetailsRow").addClass("hide");
	$(".projectDetailsRow").removeClass("filteredRow");
	
	var filter =".projectDetailsRow";
	
	if (isAdmin){
		if($(".resourceInfo").prop("selectedIndex") > 0){
			filter = filter+".prjUserId" + $(".resourceInfo").val();
		} 
	} else {
		if(!$(".totalprojectslink").hasClass("active")){
			filter = filter+".prjUserId" + $(".resourceInfo").val();
		}
	}
	if($(".projectSubTeamInfo").prop("selectedIndex") > 1){
		filter = filter+".prjSubTeam" + $(".projectSubTeamInfo").val();
	} 
	
	if($(".projectTeamInfo").prop("selectedIndex") > 1){
		filter = filter+".prjTeam" +$(".projectTeamInfo").val();
	} 
	if($(".accountInfo").prop("selectedIndex") > 1){
		filter = filter+".prjAccount" + $(".accountInfo").val();
	} 
	console.log("filter : " + filter);
	
	$(filter).removeClass("hide");
	if (!$(filter).hasClass("filteredRow")){
		$(filter).addClass("filteredRow");
	}
	
	if(isAdmin) {
		refreshUtilizationCalendar();
		refreshBandwidthCalendar();
		refreshOverAllUtilization();
		refreshAccountDiv();
		refreshTotalUtilizationDiv("month");
		refreshResouceUtilDiv();
	}
	performProjectSearch();
}

function refreshEmployeeDetails(){
	if (!isDashBoardHome()){
		return;
	}
	var groupId = $(".dashboadgroup").val();
	loadStatusForDivStart("dashboardEmpBWDiv");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardEmpBW", {"groupId":groupId}, function(data) {
		 $('.dashboardEmpBWDiv').html(data);
		 refreshUpdateUserStatus();
		 loadStatusForDivEnd("dashboardEmpBWDiv");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("dashboardEmpBWDiv");
    }, false);
	
	loadStatusForDivStart("dashboardEmpLeaveDiv");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardEmpLeave", {"groupId":groupId}, function(data) {
		 $('.dashboardEmpLeaveDiv').html(data);
		 loadStatusForDivEnd("dashboardEmpLeaveDiv");
	}, function(e) {
       famstacklog("ERROR: ", e);
       famstackalert(e);
       loadStatusForDivEnd("dashboardEmpLeaveDiv");
   },false);
}


function refreshProjectDetails(){
	if (!isDashBoardHome()){
		return;
	}
	loadStatusForDivStart("dashboardProjDetailsDiv");
	var groupId = $(".dashboadgroup").val();
	var filters = "";
	var dateRange = $("#daterangeText").val();
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardProjectDetails", {"groupId":groupId,"filters":filters, "dateRange":dateRange}, function(data) {
		 $('.dashboardProjDetailsDiv').html(data);
		 loadStatusForDivEnd("dashboardProjDetailsDiv");
		 filterProjectDetails(false);
		 performProjectSearch();
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("dashboardProjDetailsDiv");
    }, false);
	
}

var refreshUtilizationCalendar = function() {
	if (!isDashBoardTu()){
		return;
	}
	loadStatusForDivStart("fullcaledartu");
		$('#fullcaledartu').fullCalendar('refetchEvents');
	loadStatusForDivEnd("fullcaledartu");
};

var refreshBandwidthCalendar = function() {
	if (!isDashBoardBW()){
		return;
	}
	loadStatusForDivStart("fullcaledarbs");
		$('#fullcaledarbs').fullCalendar('refetchEvents');
	loadStatusForDivEnd("fullcaledarbs");
};

var refreshOverAllUtilization = function() {
	if (!isDashBoardHome()){
		return;
	}
	var dataString = getDashboardFilterDataJson();
	loadStatusForDivStart("utilizationChart");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardOverAllUtilization",dataString , function(responseData) {
		 var responseJson = JSON.parse(responseData);
		 $("#utilizationChart").html("");
		 var data = [];
		 if (responseJson.billable != 0 ||  responseJson.nonBillable != 0) {
		  data = [{label: "Billable", value: responseJson.billable},{label: "Non Billable", value: responseJson.nonBillable}];
		 }
			Morris.Donut({
			    element: 'utilizationChart',
			    data: data.length ? data : [ { label:"No Data", value:100 } ],
			    colors: ['#0BB4C1',
			             '#E3E3E3'],
			    resize: true,
			    formatter:function (y, data) { return y + "%"; }
			});
		 loadStatusForDivEnd("utilizationChart");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("utilizationChart");
    }, false);
};


function refreshAccountDiv(){
	if (!isDashBoardHome()){
		return;
	}
	var dataString = getDashboardFilterDataJson();
	loadStatusForDivStart("accountUtilizationChart");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardAccountUtilizationChart",dataString , function(responseData) {
		$(".accountUtilizationChart").html(responseData);
		 loadStatusForDivEnd("accountUtilizationChart");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("accountUtilizationChart");
    }, false);

}

function refreshTotalUtilizationDiv(type){
	if (!isDashBoardTu()){
		return;
	}
	var dataString = getDashboardFilterDataJson();
	loadStatusForDivStart("tatalutilizationChart");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardTotalUtilizationChart/"+type,dataString , function(responseData) {
		$(".tatalutilizationChart").html(responseData);
		 loadStatusForDivEnd("tatalutilizationChart");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("tatalutilizationChart");
    }, false);

}

var chartTeamContainer;
var chartResourceContainer;

function initizalizeLineChart(id, data, yKeys, labels, colors){
	return Morris.Line({
		  element: id,//'chartTeamContainer',
		  data: [/*           
	             {month:'2015-01-10', "sale":'20',  "cash":'25',  "cheque":'41', "void": '8', "declined": '13'},             
	             {month:'2015-03-10', "sale":'56',  "cash":'12',  "cheque":'25', "void": '18', "declined": '8'},             
	             {month:'2015-05-10', "sale":'56',  "cash":'36',  "cheque":'11', "void": '5', "declined": '6'},             
	    */],
	    xkey: 'month', 
	    ykeys: yKeys,
	    labels: labels,
	    lineColors:colors,
	    parseTime:true,
	    grid:false,
	    ymax:'100',
	    postUnits:'%',
	    hideHover:true,
	    //hoverCallback: function (index, options, content, row) {},
	    pointSize:1,
	    smooth:false,
	    lineWidth:"1px",
	    stacked: true,
	    gridTextSize:'9px',
	    xLabelFormat: function(d) {
	 	   console.log(d);
	 	   return ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov', 'Dec'][d.getMonth()] + ", " + d.getFullYear();
			}
	 }).on('click', function(i, row){
	   console.log(row);
	});
}


function refreshTotalUtilizationCompare(){
	if (!isDashBoardCompare()){
		return;
	}
	var dataString = getDashboardCompareFilterDataJson();
	loadStatusForDivStart("totalUtilizationComparediv");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardTotalUtilizationCompare",dataString , function(responseData) {
		$(".totalUtilizationComparediv").html(responseData);
		 loadStatusForDivEnd("totalUtilizationComparediv");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("totalUtilizationComparediv");
    }, false);

}

function refreshTeamCompareChart(){
	if (!isDashBoardCompare()){
		return;
	}

	if ("" == getAllCheckedAccounts()){
		return;
	}
	
	var dataString = getDashboardCompareFilterDataJson();
	var type = $(".dbCompareUtilization").val();
	loadStatusForDivStart("chartTeamContainer");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardChartTeamsCompare/"+type,dataString , function(responseData) {
		var responseDataJson = JSON.parse(responseData);
		if(!chartTeamContainer){
			chartTeamContainer = initizalizeLineChart("chartTeamContainer", responseDataJson.data, responseDataJson.ykeys, responseDataJson.labels,responseDataJson.lineColors);
		} else {
			chartTeamContainer.options.ykeys =responseDataJson.ykeys;
			chartTeamContainer.options.labels =responseDataJson.labels;
			chartTeamContainer.options.lineColors =responseDataJson.lineColors;
			chartTeamContainer.setData(responseDataJson.data);
		}
		loadStatusForDivEnd("chartTeamContainer");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("chartTeamContainer");
    }, false);
}

function refreshResourceCompareChart(){
	if (!isDashBoardCompare()){
		return;
	}
	
	if ("" == getAllCheckedResources()){
		return;
	}
	
	var dataString = getDashboardCompareFilterDataJson();
	loadStatusForDivStart("chartResourceContainer");
	var type = $(".dbCompareUtilization").val();
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardChartResourcesCompare/"+type,dataString , function(responseData) {
		var responseDataJson = JSON.parse(responseData);
		if(!chartResourceContainer){
			chartResourceContainer = initizalizeLineChart("chartResourceContainer", responseDataJson.data, responseDataJson.ykeys, responseDataJson.labels,responseDataJson.lineColors);
		} else {
			chartResourceContainer.options.ykeys =responseDataJson.ykeys;
			chartResourceContainer.options.labels =responseDataJson.labels;
			chartResourceContainer.options.lineColors =responseDataJson.lineColors;
			chartResourceContainer.setData(responseDataJson.data);
		}
		loadStatusForDivEnd("chartResourceContainer");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("chartResourceContainer");
    }, false);
}

function refreshResouceUtilDiv(){
	if (!isDashBoardHome()){
		return;
	}
	if ($(".resourceUtilDivWidget").is(':visible')) {
		var dataString = getDashboardFilterDataJson();
		loadStatusForDivStart("resutildiv");
		doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardResourceUtilizationChart",dataString , function(responseData) {
			$(".resutildiv").html(responseData);
			$('[data-toggle="tooltip"]').tooltip();
			 loadStatusForDivEnd("resutildiv");
		}, function(e) {
	        famstacklog("ERROR: ", e);
	        famstackalert(e);
	        loadStatusForDivEnd("resutildiv");
	    }, false);
	}
}


function getDashboardCompareFilterDataJson(){
	var dateRange = $("#daterangeText").val();
	var groupId = $(".dashboadgroup").val();
	var accountIds = getAllCheckedAccounts();
	var teamIds = getAllCheckedTeams();
	var subTeamIds = getAllCheckedSubTeams();
	var userIds = getAllCheckedResources();
	
	return {"userGroupId":groupId,"dateRange":dateRange,"accountIds":accountIds,"teamIds":teamIds,"subTeamIds":subTeamIds,"userIds":userIds};
}


function getDashboardFilterDataJson(){
	var dateRange = $("#daterangeText").val();
	var groupId = $(".dashboadgroup").val();
	var accountId = "";
	var teamId = "";
	var subTeamId = "";
	var userId = "";
	if($(".resourceInfo").prop("selectedIndex") > 0){
		userId = $(".resourceInfo").val();
	} 
	if($(".projectSubTeamInfo").prop("selectedIndex") > 1){
		subTeamId = $(".projectSubTeamInfo").val();
	} 
	if($(".projectTeamInfo").prop("selectedIndex") > 1){
		teamId= $(".projectTeamInfo").val();
	} 
	if($(".accountInfo").prop("selectedIndex") > 1){
		accountId = $(".accountInfo").val();
	} 
	
	return {"userGroupId":groupId,"dateRange":dateRange,"accountId":accountId,"teamId":teamId,"subTeamId":subTeamId,"userId":userId};
}

$('#projectSearchInput').keydown(function(e){
	performProjectSearch();
});

$('#projectSearchInput').keyup(function(e){
	performProjectSearch();
});

function performProjectSearch(){
	var serarchText = $('#projectSearchInput').val();
	if (serarchText != "") {
	$('.projectDetailsRow.filteredRow').addClass("hide");
    $('.projectDetailsRow.filteredRow').each(function(){
       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
           $(this).removeClass("hide");
       }
    });
	} else {
		$('.projectDetailsRow.filteredRow').removeClass("hide");
	}
	countProjectDetailsItem();
}

function countProjectDetailsItem(){
	var totalprojectcount = $(".projectDetailsRow.filteredRow:not([class*='hide'])").length;
	var backlogcount = $(".projectDetailsRow.filteredRow.BACKLOG:not([class*='hide'])").length;
	var unassignedcount = $(".projectDetailsRow.filteredRow.UNASSIGNED:not([class*='hide'])").length;
	var inprogress = $(".projectDetailsRow.filteredRow.INPROGRESS:not([class*='hide'])").length;
	var completed = $(".projectDetailsRow.filteredRow.COMPLETED:not([class*='hide'])").length;
	var upcoming = $(".projectDetailsRow.filteredRow.UPCOMING:not([class*='hide'])").length;
	
	$('.projectsummary.totalprojectcount .numberofproject').html(totalprojectcount);
	$('.projectsummary.backlogcount .numberofproject').html(backlogcount);
	$('.projectsummary.unassignedcount .numberofproject').html(unassignedcount);
	$('.projectsummary.inprogress .numberofproject').html(inprogress);
	$('.projectsummary.completed .numberofproject').html(completed);
	$('.projectsummary.upcoming .numberofproject').html(upcoming);
}


function loadStatusForDivStart(containerClass){
	 $("."+containerClass).waitMe({
			effect : "roundBounce",
			text : "",
			bg : 'rgba(255,255,255,0.7)',
			color : "#0BB4C1"
	});
}

function loadStatusForDivEnd(containerClass){
	 $("."+containerClass).waitMe("hide");
}

function refreshAllCompare(){
	refreshTotalUtilizationCompare();
	refreshResourceCompareChart();
	refreshTeamCompareChart();
}

function showBandWidth(){
	$(".dashboadhome").hide();
	$(".dashboadtu").hide();
	$(".dashboardbandwidth").show();
	fullCalendar.initbs();
	refreshDashBoard();
	
}

function showHome(){
	$(".dashboadhome").show();
	$(".dashboadtu").hide();
	$(".dashboardbandwidth").hide();
	$(".dashboadcompare").hide();
	refreshDashBoard();
}

function showTu(){
	$(".dashboadhome").hide();
	$(".dashboadtu").show();
	$(".dashboardbandwidth").hide();
	fullCalendar.inittu();
	refreshDashBoard();
}

function showCompare(){
	$(".dashboadhome").hide();
	$(".dashboadcompare").show();
	refreshAllCompare();
}


function reinitializeCalenders(){
	if (isDashBoardTu()){
		$('#fullcaledartu').fullCalendar('destroy');
		fullCalendar.inittu();
	} else if (isDashBoardBW()){
		$('#fullcaledarbs').fullCalendar('destroy');
		fullCalendar.initbs();		
	}
}

function showMyProjects()
{
	if(!$(".myprojectslink").hasClass("active")){
		$(".myprojectslink").addClass("active");
		$(".totalprojectslink").removeClass("active");
	}
	filterProjectDetails(false);
}


function showTotalProjects()
{
	if(!$(".totalprojectslink").hasClass("active")){
		$(".totalprojectslink").addClass("active");
		$(".myprojectslink").removeClass("active");
	}
	filterProjectDetails(false);
}


$(".projectsummary").on("click",function(){
	$(".projectsummary").removeClass("active");
	$(this).addClass("active");
	
	showProjectDetails($(this).attr("data-type"));
});

function showProjectDetails(type){
	if (type == "ALL") {
		 $(".projectDetailsRow.filteredRow").addClass("activeRow");
		 $(".projectDetailsRow.filteredRow").removeClass("hide");
	} else {
		$('.projectDetailsRow.filteredRow').removeClass("activeRow");
		$('.projectDetailsRow.filteredRow').addClass("hide");
		$(".projectDetailsRow.filteredRow."+type).addClass("activeRow");
		$(".projectDetailsRow.filteredRow."+type).removeClass("hide");
	}
}

function refreshDashBoard(){
	refreshEmployeeDetails();
	refreshProjectDetails();
	 filterProjectDetails(true);
}

Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
};

function fillDateBackGroupInDatePicker() {
	if (!isDashBoardHome()){
		return;
	}
	var dateString = $("#daterangeText").val();
	startDate = new Date(dateString.split("-")[0].trim());
	stopDate = new Date(dateString.split("-")[1].trim());
    var currentDate = new Date(startDate);
    $(".dscolor-filter").css('background-color','white');
    $(".xdsoft_current").css('background-color','#3af');
    fillCalendarColor(startDate, "darkgray");
    while (currentDate < stopDate) {
        currentDate = currentDate.addDays(1);
        fillCalendarColor(currentDate, "lightgray");
    }
    fillCalendarColor(stopDate, "darkgray");
    $(".xdsoft_current").css('background-color','#3af');
}

function fillCalendarColor(currentDate, color){
	var date = currentDate.getDate();
	var month = currentDate.getMonth();
	var year = currentDate.getFullYear();
	$("td[data-date='"+date+"'][data-month='"+month+"'][data-year='"+year+"']").css('background-color',color);
	$("td[data-date='"+date+"'][data-month='"+month+"'][data-year='"+year+"']").addClass("dscolor-filter");
}

$(document).ready(function() {
	jQuery('.calenderdiv').datetimepicker({
	  format:'d.m.Y',
	  timepicker:false,
	  inline:true,
	  startDate: new Date(),
	  lang:'en',
	  onChangeDateTime:function(dp,$input){
		 // fillDateBackGroupInDatePicker();
	  }
	});

	//sortSelect('.dashboadgroup', 'text', 'asc');
	//sortSelect('.resourceInfo', 'text', 'asc');
	refreshDashBoard();
	window.setInterval(fillDateBackGroupInDatePicker, 1000);
});


function refreshUpdateUserStatus(){
	if (!isDashBoardHome()){
		return;
	}
	var groupId = $(".dashboadgroup").val();
	doAjaxRequestWithGlobal("POST", "/bops/dashboard/userPingCheck",  {"groupId":groupId},function(data) {
    	var userStatus = JSON.parse(data);
    	$.each(userStatus, function(idx, elem){
    		changeDashBoardOnlineStatus(elem.userId, elem.userAvailableMsg, elem.status);
    	});
    },function(error) {
    	famstacklog("ERROR: ", error);
    },false);
}



function isDashBoardHome(){
	if ($(".dashboadhome").is(':visible')) {
		return true;
	}
	return false;
}

function isDashBoardTu(){
	if ($(".dashboadtu").is(':visible')) {
		return true;
	}
	return false;
}
function isDashBoardBW(){
	if ($(".dashboardbandwidth").is(':visible')) {
		return true;
	}
	return false;
}


function isDashBoardCompare(){
	if ($(".dashboadcompare").is(':visible')) {
		return true;
	}
	return false;
}

function getAllCheckedAccounts(){
	var accountIds = "";
	$('.acccbutfilter:checked').each(function(){
		if (accountIds != "") {
			accountIds+="#";
		}
		accountIds += $(this).val();
	});
	
	return accountIds;
}

function getAllCheckedTeams(){
	var teamIds = "";
	$('.ptcbutfilter:checked').each(function(){
		if (teamIds != "") {
			teamIds+="#";
		}
		teamIds += $(this).val();
	});
	
	return teamIds;
}

function getAllCheckedSubTeams(){
	var subTeamIds = "";
	$('.stcbutfilter:checked').each(function(){
		if (subTeamIds != "") {
			subTeamIds+="#";
		}
		subTeamIds += $(this).val();
	});
	
	return subTeamIds;
}


function getAllCheckedResources(){
	var resourceIds = "";
	$('.rescbutfilter:checked').each(function(){
		if (resourceIds != "") {
			resourceIds+="#";
		}
		resourceIds += $(this).val();
	});
	
	return resourceIds;
}


/*
 * #content{
  position:relative;
}
.mydiv{
  border:1px solid #368ABB;
  background-color:#43A4DC;
  position:absolute;
}
.mydiv:after{
  content:no-close-quote;
  position:absolute;
  top:50%;
  left:50%;
  background-color:black;
  width:4px;
  height:4px;
  border-radius:50%;
  margin-left:-2px;
  margin-top:-2px;
}
#div1{
  left:200px;
  top:200px;
  width:50px;
  height:50px;
}
#div2{
  left:0px;
  top:500px;
  width:50px;
  height:40px;
}
#line{
  position:absolute;
  width:2px;
  margin-top:-1px;
  background-color:red;
}

 * <div id="content">
  <div id="div1" class="mydiv"></div>
  <div id="div2" class="mydiv"></div>
  <div id="line"></div>
</div>
 * function adjustLine(from, to, line){

	var fT = from.offsetTop;
  var tT = to.offsetTop;// 	 + to.offsetHeight;
  var fL = from.offsetLeft;// + from.offsetWidth;
  var tL = to.offsetLeft;//	 + to.offsetWidth;
  
  var CA   = Math.abs(tT - fT);
  var CO   = Math.abs(tL - fL);
  var H    = Math.sqrt(CA*CA + CO*CO);
  var ANG  = 180 / Math.PI * Math.acos( CA/H );

  if(tT > fT){
      var top  = (tT-fT)/2 + fT;
  }else{
      var top  = (fT-tT)/2 + tT;
  }
  if(tL > fL){
      var left = (tL-fL)/2 + fL;
  }else{
      var left = (fL-tL)/2 + tL;
  }

  if(( fT < tT && fL < tL) || ( tT < fT && tL < fL) || (fT > tT && fL > tL) || (tT > fT && tL > fL)){
    ANG *= -1;
  }
  top-= H/2;

  line.style["-webkit-transform"] = 'rotate('+ ANG +'deg)';
  line.style["-moz-transform"] = 'rotate('+ ANG +'deg)';
  line.style["-ms-transform"] = 'rotate('+ ANG +'deg)';
  line.style["-o-transform"] = 'rotate('+ ANG +'deg)';
  line.style["-transform"] = 'rotate('+ ANG +'deg)';
  line.style.top    = top+'px';
  line.style.left   = left+'px';
  line.style.height = H + 'px';
}
adjustLine(
  document.getElementById('div1'), 
  document.getElementById('div2'),
  document.getElementById('line')
);
*/
