//$("td[data-date='1'][data-month='7']").css("background-color","red");

$(document).ready(function() {
	jQuery('.calenderdiv').datetimepicker({
	  format:'d.m.Y',
	  timepicker:false,
	  inline:true,
	  startDate: new Date(),
	  lang:'en'
	});

	sortSelect('.dashboadgroup', 'text', 'asc');
	sortSelect('.resourceInfo', 'text', 'asc');
	refreshEmployeeDetails();
	refreshProjectDetails();
});

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

$(".resourceInfo").on("change", function(){
	
});

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
		 performSearch();
	}, function(e) {
        famstacklog("ERROR: ", e);
        famstackalert(e);
        loadStatusForDivEnd("dashboardProjDetailsDiv");
    }, false);
	
}


var refreshUtilizationCalendar = function() {
	var userId = 0;
	var events = {
	        url: "getAjaxFullcalendar",
	        type: 'GET',
	        data: {
	        	userId: userId
	        }
	    }
	$('#fullcaledartu').fullCalendar('removeEventSource', events);
	$('#fullcaledartu').fullCalendar('addEventSource', events);
	$('#fullcaledartu').fullCalendar('refetchEvents');
}

$('#projectSearchInput').keydown(function(e){
	performSearch();
});

$('#projectSearchInput').keyup(function(e){
	performSearch();
});

function performSearch(){
	var serarchText = $('#projectSearchInput').val();
	if (serarchText != "") {
	$('.projectDetailsRow').addClass("hide");
    $('.projectDetailsRow').each(function(){
       if($(this).text().toUpperCase().indexOf(serarchText.toUpperCase()) != -1){
           $(this).removeClass("hide");
       }
    });
	} else {
		$('.projectDetailsRow').removeClass("hide");
	}
	countProjectDetailsItem();
}

function countProjectDetailsItem(){
	var totalprojectcount = $(".projectDetailsRow:not([class*='hide'])").length;
	var backlogcount = $(".projectDetailsRow.BACKLOG:not([class*='hide'])").length;
	var unassignedcount = $(".projectDetailsRow.UNASSIGNED:not([class*='hide'])").length;
	var inprogress = $(".projectDetailsRow.INPROGRESS:not([class*='hide'])").length;
	var completed = $(".projectDetailsRow.COMPLETED:not([class*='hide'])").length;
	var upcoming = $(".projectDetailsRow.UPCOMING:not([class*='hide'])").length;
	
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
		$(".totalprojectslink").removeClass("active")
	}
}


function showTotalProjects()
{
	if(!$(".totalprojectslink").hasClass("active")){
		$(".totalprojectslink").addClass("active");
		$(".myprojectslink").removeClass("active")
	}
}


$(".projectsummary").on("click",function(){
	$(".projectsummary").removeClass("active");
	$(this).addClass("active");
	
	showProjectDetails($(this).attr("data-type"));
});

function showProjectDetails(type){
	if (type == "ALL") {
		 $(".projectDetailsRow").addClass("activeRow");
	} else {
		$('.projectDetailsRow').removeClass("activeRow");
		$(".projectDetailsRow."+type).addClass("activeRow");
	}
	performSearch();
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
});


Morris.Donut({
    element: 'utilizationChart',
    data: [{label: "Billable", value: 80},
    	  {label: "Non Billable", value: 20}],
    colors: ['#0BB4C1',
             '#E3E3E3'],
    resize: true,
    formatter:function (y, data) { return y + "%" }
});




