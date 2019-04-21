<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- PAGE CONTENT WRAPPER -->
<div class="dashboard col-md-10 dashboadhome" style="padding-right: 0px;">
<%@include file="dashboardfilter.jsp" %>
<div class="col-md-12 dbwidget">
	<div class="row">
		<div class="col-md-3 dbwidget">
			<div class="panel panel-default">
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 225px;">
						<div class="paneltitlebox">
							<span>TEAM UTILISATION</span>
						</div>
						<div id="utilizationDonutChart" class="utilizationDonutChart" style="height: 125px">
							  <div id="specificChart" class="donut-size">
							      <div class="pie-wrapper">
							        <span class="label">
							          <span class="num">0</span><span class="smaller">%</span>
							        </span>
							        <div class="pie">
							          <div class="left-side half-circle"></div>
							          <div class="right-side half-circle"></div>
							        </div>
							        <div class="shadow"></div>
							      </div>
							    </div>
						</div>
						
						<div class="col-md-12 center-block"" style="height: 20px;border-bottom: 4px solid #29abe2;">
							<span class="legendtext" style="font-style: italic;">Bandwidth: 8hrs/day</span>
							<span class="legendtext" style="float: right;"><a style="text-decoration: underline;" href="#">View Trend</a></span>
						</div>
						<div class="col-md-12">
							<div class="col-md-6 utilization border" >
								<span class="utilizationtext">BILLABLE</span>
								<span class="utilizationhrs billableUtilizationhrs">0 Hrs</span>
							</div>
							<div class="col-md-6 utilization">
								<span  class="utilizationtext">NON BILLABLE</span>
								<span class="utilizationhrs nonBillableUtilizationhrs">0 Hrs</span>
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
		<div class="col-md-3 dbwidget accountDivWidget">
			<div class="panel panel-default">
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 225px;">
						<div class="paneltitlebox">
							<span>ACCOUNTS</span>
						</div>
						<div class="col-md-12 accountUtilizationPieChart" style="height: 170px;margin-top: 12px">
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="col-md-6 dbwidget resourceUtilDivWidget">
			<div class="panel panel-default">
				<div class="panel-body padding-0">
					<div class="dbcontentbox" style="height: 225px;">
						<div class="paneltitlebox">
							<span>RESOURCE UTILIZATION (20)</span>
						</div>
						<div class="dbcontentbox resutildiv"  style="height: 200px;overflow: auto;overflow-x: hidden;">
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12 dbwidget">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="" style="width: 100%">
						<span class="paneltitlebox" style="float: left">PROJECT SUMMARY</span>
						<span style="float: right;"><span class="fa fa-search fa-2x" style="color: lightgray"></span>
						<input style="" id="projectSearchInput" class="searchbox projectSearchInput" type="text" placeholder="Search for projects by Id, team, PO etc.."/>
						<a href="#" class="panel-fullscreen"><img  style="width: 17px;margin-right: 5px;" alt="image" src="${assets}/images/fullscreenimg.png"></a>
						</span>
						
					</div>
				</div>
				<div class="panel-body padding-0" style="overflow: hidden;">
					<div class="dbcontentbox projectdetails">
						<div class="col-md-12 projecttitleblock">
						<div class="col-md-2">
							<div class="projectsummary totalprojectcount active" data-type="ALL">
								<span class="total">Projects</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary backlogcount"  data-type="BACKLOG">
								<span class="total">Backlogs</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary unassignedcount" data-type="UNASSIGNED">
								<span class="total">Unassigned</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary inprogress"  data-type="INPROGRESS">
								<span class="total">In Progress</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary completed"  data-type="COMPLETED">
								<span class="total">Completed</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
						<div class="col-md-2">
							<div class="projectsummary upcoming" data-type="UPCOMING">
								<span class="total">Upcomming</span>
								<span class="numberofproject">0</span>
								<span class="hours"></span>
							</div>
						</div>
					</div>
					<div class="col-md-12 fixedheadertable">
							<table class="table ">
						    <thead>
						    <tr>
						    	<th class="rtborder">Projects</th>
						    	<th class="panelHideTD" style="display: none">PO Id</th>
						        <th>Team</th>
						        <th>Sub Team</th>
						        <th class="panelHideTD" style="display: none">Account</th>
						        <th class="panelHideTD" style="display: none">Client</th>
						        <th>Project Lead</th>
						        <th>Assigned To</th>
		    				    <th class="panelHideTD" style="display: none">Type</th>
		    				    <th class="panelHideTD" style="display: none">Category</th>
						        <th>Time</th>
						        <th>Status</th>
						    </tr>
						    </thead>
						    <tbody class="dashboardProjDetailsDiv">
						     
						    </tbody>
						</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
</div>
</div>
<div class="dashboard col-md-2 dashboadhome" style="padding-left:  0px;padding-right:  0px;padding-top: 3px">
	<div class="col-md-12 dbwidget">
		<div class="panel panel-default">
			<div class="panel-heading" style="background-color: #29abe2;border: 0;">
				<div class="panel-title-box">
					<span class="bandwidthtitle">BANDWIDTH</span>
				</div>
			</div>
			<div class="panel-body padding-0" style="background-color: #3d3d3d;">
				<div class="dbcontentbox" style="height: 560px;">
				<div style="margin-top: 5px;margin-bottom: 5px">
					<span style="font-size: 12px;color: #52be7a;font-weight: bold;margin-left: 10px;border-bottom: 1px solid #52be7a;">All <span class="allUserCount">(0)</span></span>
					<span style="font-size: 12px;color: #d0d0d0;font-weight: bold;margin-left: 15px;">Free <span class="freeUserCount">(2)</span></span>
				</div>
				<div  style="margin-top: 10px;">
					<input type=text class="bandwidthsearch" placeholder="Search"/>
				</div>
				<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 55%;text-align: center;border-bottom: 0;background-color: #3d3d3d;"></th>
				        <th style="width: 25%; text-align: center;border-bottom: 0;background-color: #3d3d3d;"><span style="font-size: 8px;color: #cecece">Last Day</span></th>
				        <th style="width: 20%; text-align: center;border-bottom: 0;background-color: #3d3d3d;"><span style="font-size: 8px;color: #cecece">Today</span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 210px" class="dashboardEmpBWDiv">
			
				    </tbody>
				    </table>
					</div>
					<div class="fixedheadertable">
					<table class="table">
				    <thead>
				    <tr>
				    	<th style="width: 100%;border-bottom:0;background-color: #3d3d3d;"><span style="font-size: 13px;color: #e65b4c;font-weight: 500;">On Leave <span class="allLeaveUserCount">(0)</span></span></th>
				    </tr>
				    </thead>
				    <tbody style="height: 80px" class="dashboardEmpLeaveDiv">
				     
				    </tbody>
				    </table>				
				    </div>
				      <div class="" style="height: 170px;background-color: #6c6c6c">
					    <div class="calenderdiv" style="width: 200px;">
					    </div>
				    </div>
				</div>
			</div>
		</div>
	</div>
</div>