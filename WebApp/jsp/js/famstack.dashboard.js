//$("td[data-date='1'][data-month='7']").css("background-color","red");

//Filters
$(".dashboadgroup").on("change", function(){
	$(".accountInfo option:not([class*='hide'])").addClass("hide");
	$(".accountInfo option:first").removeClass("hide");
	$(".accountInfo option:nth-child(2)").removeClass("hide");
	$(".accountInfo option.UG"+$(this).val()).removeClass("hide");
	$(".accountInfo").prop("selectedIndex",0);
	$( ".accountInfo" ).trigger( "change" );
	
	$(".resourceInfo option:not([class*='hide'])").addClass("hide");
	$(".resourceInfo option:first").removeClass("hide");
	$(".resourceInfo option.UG"+$(this).val()).removeClass("hide");
	$(".resourceInfo").prop("selectedIndex",0);
	$(".resourceInfo" ).trigger( "change" );
	refreshEmployeeDetails();
	refreshProjectDetails();
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
	filterProjectDetails();
});

$(".resourceInfo").on("change", function(){
	filterProjectDetails();
});

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
	  refreshResouceUtilDiv();
	  refreshOverAllUtilization();
	  refreshAccountDiv();
});


function filterProjectDetails(){

	$(".projectDetailsRow").addClass("hide");
	$(".projectDetailsRow").removeClass("filteredRow");
	
	var filter =".projectDetailsRow";
	
	if($(".resourceInfo").prop("selectedIndex") > 0){
		filter = filter+".prjUserId" + $(".resourceInfo").val();
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
	refreshUtilizationCalendar();
	refreshOverAllUtilization();
	refreshAccountDiv();
	refreshResouceUtilDiv();
	performProjectSearch();
}

function refreshEmployeeDetails(){
	var groupId = $(".dashboadgroup").val();
	loadStatusForDivStart("dashboardEmpBWDiv");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardEmpBW", {"groupId":groupId}, function(data) {
		 $('.dashboardEmpBWDiv').html(data);
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
	loadStatusForDivStart("dashboardProjDetailsDiv");
	var groupId = $(".dashboadgroup").val();
	var filters = "";
	var dateRange = $("#daterangeText").val();
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardProjectDetails", {"groupId":groupId,"filters":filters, "dateRange":dateRange}, function(data) {
		 $('.dashboardProjDetailsDiv').html(data);
		 loadStatusForDivEnd("dashboardProjDetailsDiv");
		 performProjectSearch();
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("dashboardProjDetailsDiv");
    }, false);
	
}


var refreshUtilizationCalendar = function() {
	loadStatusForDivStart("fullcaledarbs");
		$('#fullcaledartu').fullCalendar('refetchEvents');
	loadStatusForDivEnd("fullcaledarbs");
};

var refreshOverAllUtilization = function() {
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

function refreshResouceUtilDiv(){
	var dataString = getDashboardFilterDataJson();
	loadStatusForDivStart("resutildiv");
	doAjaxRequestWithGlobal("GET", "/bops/dashboard/dashboardResourceUtilizationChart",dataString , function(responseData) {
		$(".resutildiv").html(responseData);
		 loadStatusForDivEnd("resutildiv");
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("resutildiv");
    }, false);

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

function showBandWidth(){
	$(".dashboadhome").hide();
	$(".dashboadtu").hide();
	$(".dashboardbandwidth").show();
	fullCalendar.initbs();
}

function showHome(){
	$(".dashboadhome").show();
	$(".dashboadtu").hide();
	$(".dashboardbandwidth").hide();
}

function showTu(){
	$(".dashboadhome").hide();
	$(".dashboadtu").show();
	$(".dashboardbandwidth").hide();
	fullCalendar.inittu();
}

function showMyProjects()
{
	if(!$(".myprojectslink").hasClass("active")){
		$(".myprojectslink").addClass("active");
		$(".totalprojectslink").removeClass("active");
	}
}


function showTotalProjects()
{
	if(!$(".totalprojectslink").hasClass("active")){
		$(".totalprojectslink").addClass("active");
		$(".myprojectslink").removeClass("active");
	}
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
	refreshOverAllUtilization();
	refreshAccountDiv();
	refreshResouceUtilDiv();
}

$(document).ready(function() {
	jQuery('.calenderdiv').datetimepicker({
	  format:'d.m.Y',
	  timepicker:false,
	  inline:true,
	  startDate: new Date(),
	  lang:'en'
	});

	//sortSelect('.dashboadgroup', 'text', 'asc');
	//sortSelect('.resourceInfo', 'text', 'asc');
	refreshDashBoard();
});
