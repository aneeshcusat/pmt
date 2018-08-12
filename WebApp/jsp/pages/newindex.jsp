<%@include file="includes/header.jsp"%>
<link rel="stylesheet" type="text/css" id="theme" href="${fn:escapeXml(css)}/pages/dashboard.css"/>
 <div class="col-md-12 dsfilter">
 <div class="col-md-12">
 <span style="float: right;display: none">
 	<span class="searchboxlabel">Select Account</span>
 	<span class="searchboxlabel">Select Team</span>
 	<span class="searchboxlabel">Select Sub Team</span>
 	<span class="searchboxlabel">Select Resource</span>
 	<span class="searchboxlabel">Select Date</span>
 </span>
 </div>
 <div class="col-md-12">
 <span style="float: left;font-size: 13px;margin-top: 1px;">
 	Dashboard
 </span>
 	<span style="float: right">
 	<select class="searchbox">
 		<option>Select Division</option>
 		<option>All</option>
 	</select>
 	<select class="searchbox">
 		<option>Select Account</option>
 		<option>All</option>
 	</select>
 	<select class="searchbox">
 		<option>Select Team</option>
 		<option>All</option>
 	</select>
 	<select class="searchbox">
 		<option>Select Sub Team</option>
 		<option>All</option>
 	</select>
 	<select class="searchbox">
 		<option>Select Resource</option>
 		<option>All</option>
 	</select>
 	<select class="searchbox">
 		<option>Select Date</option>
 		<option>All</option>
 	</select>
 	<i class="fas fa-expand"></i></span>
 	</div>
</div>
<div class="dashboardcontainer">
<%@include file="includes/dashboardhome.jsp" %>
<%@include file="includes/dashboardtu.jsp" %>
<%@include file="includes/dashboardbandwidth.jsp" %> 
</div>
<%@include file="includes/footer.jsp" %>
<script type="text/javascript" src="${js}/plugins/fullcalendar/fullcalendar.min.js"></script>
<script type="text/javascript" src="${js}/famstack.calender.js"></script>
<script>
$(document).ready(function() {
	jQuery('.calenderdiv').datetimepicker({
	  format:'d.m.Y',
	  timepicker:false,
	  inline:true,
	  startDate: new Date(),
	  lang:'en'
	});
});

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

</script>