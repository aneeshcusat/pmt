<%@include file="includes/header.jsp"%>
<style>
.dashboard .dbwidget {
    min-height: 1px;
  padding-left: 5px; 
  padding-right: 5px;
}
.dashboard .panel{
margin-bottom: 5px;
}
.dashboard .panel .panel-default{
	text-align: center !important;
}
.dashboard .panel .panel-title-box{
	float: none !important;
	text-align:center !important;
}

.dashboard .panel .panel-heading .panel-title-box span{
font-size: 12px;
}

.dashboard .panel .panel-heading{
	background-color: white;
	padding: 3px !important;
}

.dashboard .panel .panel-heading .panel-title-box h3{
	font-size:1.5em !important;
	color:#686868;
	font-weight: 700;	
}
.dashboard .panel .panel-heading .dbheading{
    font-size: 1.5em !important;
    font-weight: 700;
    line-height: 18px;
    color: #686868;
    padding: 0px;
    margin: 0px;
 }

.dashboard .dbwidget .dbcontentbox{
	box-shadow: 1px 1px 5px #888888;
}


.dashboard .projectsummary {
    text-align: center;
    /*background-color: #B5B5B5;*/
}

.dashboard input.searchbox, select.searchbox {
  border: 0;
  outline: 0;
  background: transparent;
  border-bottom: 1px solid #B5B5B5;
  margin-right: 15px;
  min-width: 100px;
}

span.searchboxlabel {
  margin-right: 15px;
  min-width: 100px;
}

.dashboard .projecttitleblock{
	background-color: #EDEDED;
}

.dashboard .legend{
	width: 13px;
	height: 8px;
	margin-left: 5px;
	margin-top: 5px;
	margin-right: 2px;
	display: inline-block;
}

.dashboard .legend.dot{
	width: 10px;
	height: 10px;
	border-radius:20px;
}

.dashboard .legendtext{
    font-size: 11px;
    color: darkgray;
    font-weight: 600;

}

.dashboard .projectsummary .total{
    display: block;
    font-size: .9em;
    font-weight: bold;
   /*padding-top: 12px;*/
}

.dashboard .projectsummary .numberofproject{
    display: block;
     font-size: 2.5em;
    font-weight: bold;
    line-height: 1;
}

.dashboard .projectsummary .hours{
    display: block;
}

.dsfilter{
    background-color: white;
    height: 26px;
    margin: 2px 0 5px 0;
    margin-right: 50px;
}
.dashboard{
	padding-left: 0px !important;
}

.dashboard .fixedheadertable {
padding-left: 5px;
}
.dashboard .fixedheadertable table {
        width: 100%;
        margin-top: 5px;
        margin-bottom: 0px;
    }

.dashboard .fixedheadertable thead, .dashboard .fixedheadertable tbody, .dashboard .fixedheadertable tr, .dashboard .fixedheadertable td, .dashboard .fixedheadertable th { 
	display: block; 
}

.dashboard .fixedheadertable tr:after {
    content: ' ';
    display: block;
    visibility: hidden;
    clear: both;
}

.dashboard .fixedheadertable thead th {
    height: 25px;
 	background: #fff;
    color: #56688A;
    font-size: 10px;   
}

.xdsoft_datetimepicker .xdsoft_datepicker.active{
width: 100%;
margin-left: 2px;
}

.dashboard div.xdsoft_calendar,.dashboard .xdsoft_datetimepicker .xdsoft_monthpicker{
	width: 100%;
}
.dashboard .xdsoft_datetimepicker .xdsoft_month
{
	width: 38%;
}

.dashboard .xdsoft_datetimepicker .xdsoft_year {
    width: 40px;
    margin-left: 3px;
}

.dashboard .xdsoft_datetimepicker .xdsoft_next, .dashboard .xdsoft_datetimepicker .xdsoft_prev, .dashboard .xdsoft_datetimepicker .xdsoft_today_button
{
	width: 16px;
}
.xdsoft_datetimepicker .xdsoft_label{
	font-size: 9px;
	line-height: 15px;
}
.dashboard .fixedheadertable tbody {
    height: 250px;
    overflow-y: auto;
}

.dashboard .fixedheadertable thead {
}


.dashboard .fixedheadertable tbody td, .dashboard .fixedheadertable thead th {
    width: 14%;
    float: left;
}

.dashboard .rtborder {
	border-right: 2px #E5E5E5 solid;
}

 
 ::-webkit-scrollbar {
    width: 7px;
}
::-webkit-scrollbar-track {
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3); 
    -webkit-border-radius: 10px;
    border-radius: 10px;
}
::-webkit-scrollbar-thumb {
    -webkit-border-radius: 10px;
    border-radius: 10px;
    background: rgba(0, 0, 0, 0.02); 
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); 
}
::-webkit-scrollbar-thumb:window-inactive {
    background: rgba(0, 0, 0, 0.02); 
}

.dashboard .fixedheadertable td.emputilization{
	width: 20%; 
	height:41px;
	text-align:center;
	vertical-align:middle;
	padding: 0px;
	border-top:none;
	border: 1px solid #E5E5E5;
}

.dashboard .userdetails{
	padding-top: 5px;
	display: inline-block;
	padding-right: 5px;
}
.dashboard .percentage{
	margin-top: 10px;
	display: block;
}
</style>


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
<!-- PAGE CONTENT WRAPPER -->
<div class="dashboard col-md-12" style="
    padding-right: 5px;
">
<div class="col-md-10 dbwidget">
	<div class="row">
		<div class="col-md-2 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Utilization</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 175px;">
					<div class="utilizationChart" style="height: 150px">
					
					</div>
					
					<div class="col-md-12 center-block"" style="height: 10px;">
						<span style="background-color: #0BB4C1;" class="legend dot"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend dot"></span><span class="legendtext">Non Billable</span>
					</div>
					</div>
					
				</div>
			</div>
		</div>
		<div class="col-md-4 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Accounts</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 175px;">
					<div class="col-md-12 center-block" style="height: 10px;text-align: center;">
						<span style="background-color: #0BB4C1;" class="legend"></span><span class="legendtext">Billable</span>
						<span style="background-color: #E3E3E3;" class="legend"></span><span class="legendtext">Non Billable</span>
					</div>
					<div class="utilizationChart" style="height: 150px">
					
					</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box">
						<h3>Resource Utilization</h3>
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 175px;"></div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-md-12 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title-box" style="width: 100%">
						<span class="dbheading" style="float: left">Project Summary</span>
						<span style="float: right;"><span class="fa fa-search fa-3x"></span>
						<input style="min-width: 250px;" class="searchbox" type="text" placeholder="Search for projects by Id, team, PO etc.."/>
						<span class="fa fa-expand fa-lg"></span></span>
						
					</div>
				</div>
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 340px;">
						<div class="col-md-12 projecttitleblock">
						<div class="col-md-2">
							<div class="projectsummary" style="color: white; background-color: #B5B5B5">
								<span class="total">Total Projects</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary" style="color: red">
								<span class="total">Backlogs</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2" style="color: brown">
							<div class="projectsummary">
								<span class="total">Unassigned</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary" style="color: lightgreen">
								<span class="total">In Progress</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary" style="color: green">
								<span class="total">Completed</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary" style="color: blue">
								<span class="total">Upcomming</span>
								<span class="numberofproject">25</span>
								<span class="hours">(125 Hrs)</span>
							</div>
						</div>
					</div>
					<div class="col-md-12 fixedheadertable">
					<table class="table ">
				    <thead>
				    <tr>
				    	<th>Projects</th>
				        <th>Team</th>
				        <th>Sub Team</th>
				        <th>Project Lead</th>
				        <th>Assigned To</th>
				        <th>Time</th>
				        <th>Status</th>
				    </tr>
				    </thead>
				    <tbody>
				      <c:forEach var="i" begin="0" end="30" step="1">
				    <tr>
				        <td class="filterable-cell rtborder">Ford</td>
				        <td class="filterable-cell">Escort</td>
				        <td class="filterable-cell">Blue</td>
				        <td class="filterable-cell">2000</td>
				        <td class="filterable-cell">2000</td>
				        <td class="filterable-cell">2000</td>
				        <td class="filterable-cell">2000</td>
				    </tr>
				    </c:forEach>
				    </tbody>
				</table>
				</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</div>
<div class="col-md-2 dbwidget" style="
    padding-left:  0px;
    padding-right:  0px;
">
	<div class="col-md-12 dbwidget">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="panel-title-box">
					<span class="dbheading">Bandwidth</span><span style="float: right">View summary</span>
				</div>
			</div>
			<div class="panel-body padding-0">
				<div class="dbcontentbox" style="height: 540px;">
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 50%;text-align: center;border-bottom: 0;"><span style="font-size: 11px;color: green;font-weight: bold;">All (45)</span></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;"><span style="font-size: 8px">Last Day</span></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;"><span style="font-size: 8px">Today</span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 210px" >
				    <c:forEach var="i" begin="0" end="30" step="1">
					    <tr style="padding-bottom: 0px;margin-top: 0px; border-bottom: 1px solid #dddddd">
					        <td class="filterable-cell" style="width: 60%;padding: 0px;border-top:0">
					        <span class="userdetails">
					        	<img  style="width: 30px;border-radius: 20px;" alt="image" src="${assets}/images/users/user-online.png"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
					        </span><b>test user</b></td>
					        <td class="filterable-cell emputilization" style="background-color: #ececec;border-top:0"><span class="percentage">${i} %</span></td>
					        <td class="filterable-cell emputilization" style="background-color: #d7d7d7;border-top:0"><span class="percentage">--%</span></td>
					    </tr>
					</c:forEach>
				    </tbody>
				    </table>
					</div>
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 50%;border-bottom:0"><span style="font-size: 13px;color: red;font-weight: 500;">On Leave (45)</span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 80px" >
				    <c:forEach var="i" begin="0" end="30" step="1">
					    <tr style="padding-bottom: 0px;margin-top: 0px; border-bottom: 1px solid #dddddd">
					        <td class="filterable-cell" style="width: 60%;padding: 0px;;border-top:0">
					        <span class="userdetails" style="margin-bottom: 5px">
					        <img  style="width: 30px;border-radius: 20px;" alt="image" src="${assets}/images/users/user-offline.png"  onerror="this.src='${assets}/images/users/no-image.jpg'"></a>
					        </span><b>test user</b></td>
					    </tr>
					</c:forEach>
				    </tbody>
				    </table>				
				    </div>
				    <div class="calenderdiv" style="width: 200px">
				    </div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<%@include file="includes/footer.jsp" %>
<script>
jQuery('.calenderdiv').datetimepicker({
	  format:'d.m.Y',
	  timepicker:false,
	  inline:true,
	  startDate: new Date(),
	  lang:'en'
	});
</script>